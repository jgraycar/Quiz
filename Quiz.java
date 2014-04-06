package qz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.File;
import java.io.Reader;
import java.io.FileReader;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.Arrays;
import java.util.regex.Pattern;

import java.lang.StringBuilder;

public class Quiz {

    static final String USAGE = "/Users/Joel/CompSci/qz/Usage.txt";
    static final String TEST_USAGE = "/Users/Joel/CompSci/qz/takingTests.txt";

    protected static void printUsage(String file) {
        try {
            FileReader help = new FileReader(new File(file));
            StringBuilder str = new StringBuilder();
            for (int c = help.read(); c != -1; c = help.read()) {
                str.append(Character.toChars(c)[0]);
            }
            System.out.println(str.toString());
        } catch (IOException io) {
            System.out.println("Error occured while retrieving usage file.");
            System.exit(1);
        }
    }
        

    public static void main(String... args) {
        if (args.length > 0) {
            parseArgs(args);
        } else {
            printUsage(USAGE);
        }
    }

    private static void parseArgs(String[] args) {
        String action = "create";
        boolean anon = false;
        boolean givenFile = false;
        for (String arg : args) {
            switch (arg) {
            case "-c":
                action = "create";
                break;
            case "-t":
                action = "take";
                break;
            case "-a":
                anon = true;
                break;
            case "-g":
                action = "grade";
                break;
            case "-d":
                action = "data";
                break;
            default:
                givenFile = true;
                doAction(action, arg, anon);
            }
        }
        if (!givenFile) {
            System.err.println("Error: no file inputted.");
            System.exit(1);
        }
    }

    private static void doAction(String action, String arg, boolean anon) {
        switch (action) {
        case "grade":
            Grade.gradeFile(arg);
            break;
        case "create":
            createQuiz(arg, anon);
            break;
        case "take":
            takeQuiz(arg, anon);
            break;
        case "data":
            Grade.dataFile(arg);
            break;
        default:
            break;
        }
    }

    private static void createQuiz(String filename, boolean anon) {
        try {
            String[] parts = filename.split("\\.");
            if (parts.length > 1) {
                parts = Arrays.copyOf(parts, parts.length - 1);
            }
            filename = "";
            for (String s :  parts) {
                filename += s;
                filename += ".";
            }
            StringBuilder filetext = new StringBuilder();
            if (!anon) {
                filetext.append("Name: \n");
            }
            String[] questions = getQuestions();
            for (String str : questions) {
                filetext.append(str);
                filetext.append("\n");
            }
            FileWriter file = new FileWriter(filename + "qz");
            file.write(filetext.toString());
            file.close();
            File ansFile = new File(filename + "ans");
            ansFile.delete();
        } catch (IOException io) {
            System.err.printf("Error occured while parsing.\n");
            System.exit(1);
        }
    }

    private static String[] getQuestions() {
        boolean running = true;
        boolean qDone = false;
        Scanner inp = new Scanner(new InputStreamReader(System.in));
        LinkedList<String> questions = new LinkedList<String>();
        int qNum = 1;
        char aNum = "A".toCharArray()[0];
        while (running) {
            System.out.print("Q" + qNum + ": ");
            String line = inp.nextLine();
            if (line.length() == 0) {
                running = false;
                break;
            }
            questions.add(qNum + ". " + line);
            System.out.println("Answers:");
            while (!qDone) {
                System.out.print(aNum + ": ");
                line = inp.nextLine();
                if (line.length() == 0) {
                    qDone = true;
                } else {
                    questions.add(aNum + ": " + line);
                }
                aNum += 1;
            }
            qDone = false;
            qNum += 1;
            aNum = "A".toCharArray()[0];
        }
        if (questions.size() > 0) {
            ListIterator<String> qIter = questions.listIterator(0);
            String[] qs = new String[questions.size()];
            int index = 0;
            String str = "";
            for (str = qIter.next(); qIter.hasNext(); str = qIter.next()) {
                qs[index] = str;
                index += 1;
            }
            qs[index] = str;
            return qs;
        } else {
            System.exit(0);
        }
        return new String[1];
    }

    private static void takeQuiz(String filename, boolean anon) {
        Scanner inp = new Scanner(new InputStreamReader(System.in));
        try {
            BufferedReader file = getFile(filename);
            String line = file.readLine();
            String answer = "";
            boolean running = true;
            boolean firstTime = true;
            int currQ = 1;
            if (line.equals("Name: ")) {
                if (!anon) {
                    System.out.print(line);
                    answers.add(line + inp.nextLine());
                }
                line = file.readLine();
            }
            while (running) {
                String[] parts = line.split("\\.");
                if (isInt(parts[0])) {
                    if (!firstTime) {
                        getResponse(currQ);
                        currQ += 1;
                    }
                } else {
                    String[] parts2 = line.split(":");
                    potAns.add(parts2[0]);
                }
                running = true;
                firstTime = false;
                System.out.println(line);
                line = file.readLine();
                if (line == null) {
                    running = false;
                    getResponse(currQ);
                }
            }
            ListIterator<String> ansIter = answers.listIterator();
            String[] ans = new String[answers.size()];
            int index = 0;
            String str = "";
            for (str = ansIter.next(); ansIter.hasNext(); str = ansIter.next()) {
                ans[index] = str;
                index += 1;
            }
            ans[index] = str;
            createAnsFile(ans, filename);
        } catch (NullPointerException e) {
            System.err.printf("Error: no file %s found.\n", filename);
            System.exit(1);
        } catch (IOException io) {
            System.err.printf("Error occured while parsing.\n");
            System.exit(1);
        }
    }

    private static void getResponse(int currQ) {
        Scanner inp = new Scanner(new InputStreamReader(System.in));
        boolean running = true;
        int errs = 0;
        while (running) {
            System.out.print("> ");
            String answer = inp.nextLine();
            if (potAns.contains(answer.toUpperCase())) {
                answers.add(currQ + ". " + answer.toUpperCase());
                potAns = new LinkedList<String>();
                errs = 0;
                running = false;
            } else if (answer.equals("quit")) {
                System.out.println("Goodbye.");
                System.exit(0);
            } else if (answer.equals("help")) {
                printUsage(TEST_USAGE);
            } else {
                System.out.println("Error: " + answer
                                   + " is not a valid response.");
                errs += 1;
                if (errs > 3) {
                    printUsage(TEST_USAGE);
                    errs = 0;
                }
            }
        }
    }

    private static void createAnsFile(String[] ans, String filename) {
        try {
            String[] parts = filename.split("\\.");
            if (parts.length > 1) {
                parts[parts.length - 1] = "ans";
            } else {
                String temp = parts[0];
                parts = new String[2];
                parts[0] = temp;
                parts[1] = "ans";
            }
            filename = "";
            for (int i = 0; i < parts.length; i += 1) {
                filename += parts[i];
                if (i < parts.length - 1) {
                    filename += ".";
                }
            }
            FileWriter file = new FileWriter(filename, true);
            for (String s : ans) {
                file.write(s + "\n");
            }
            file.write("\n");
            file.close();
        } catch (IOException io) {
            System.err.println("Error arose while creating answer file.");
            System.exit(1);
        }
    }

    protected static boolean isInt(String inp) {
        try {
            int i = Integer.parseInt(inp);
            return true;
        } catch (NumberFormatException n) {
            return false;
        }
    }

    protected static BufferedReader getFile(String filename) {
        InputStream resource =
            qz.Quiz.class.getClassLoader().getResourceAsStream(filename);
        BufferedReader str = new BufferedReader(new InputStreamReader(resource));
        return str;
    }


    private static LinkedList<String> potAns = new LinkedList<String>();
    private static LinkedList<String> answers = new LinkedList<String>();

}
