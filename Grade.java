package qz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileWriter;

import java.lang.StringBuilder;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Grade {

    static final String USAGE = "/Users/Joel/CompSci/qz/gradeUsage.txt";
    static final String TAB = "   ";
    
    public static void main(String... args) {
        if (args.length > 0) {
            gradeFile(args[0]);
        } else {
            Quiz.printUsage(USAGE);
        }
    }

    protected static void gradeFile(String ansFile) {
        questions = new HashMap<String, ArrayList<String>>();
        BufferedReader file = Quiz.getFile(ansFile);
        ArrayList<String> participants = new ArrayList<String>();
        try {
            String line = file.readLine();
            while (line != null) {
                if (line.equals("")) {
                    line = file.readLine();
                    continue;
                }
                String[] parts = line.split("\\p{Space}+");
                if (parts[0].equals("Name:")) {
                    String name = parts[1];
                    if (parts.length > 2) {
                        for (int i = 2; i < parts.length; i += 1) {
                            name += " ";
                            name += parts[i];
                        }
                    }
                    participants.add(name);
                } else {
                    ArrayList<String> responses = new ArrayList<String>();
                    if (questions.containsKey(parts[0])) {
                        responses = questions.get(parts[0]);
                    }
                    responses.add(parts[1]);
                    questions.put(parts[0], responses);
                }
                line = file.readLine();
            }
            String filename = makeName(ansFile);
            createGradedFile(participants, filename);
        } catch (IOException io) {
            System.err.println("Something went wrong!!!");
            System.exit(1);
        }
    }

    private static String makeName(String name) {
        String filename = "";
        String[] parts = name.split("\\.");
        if (parts.length > 1) {
            parts = Arrays.copyOf(parts, parts.length - 1);
        }
        for (String s : parts) {
            filename += s;
            filename += ".";
        }
        filename += "grd";
        return filename;
    }

    private static void createGradedFile(ArrayList<String> participants, String filename) {
        StringBuilder str = new StringBuilder();
        if (participants.size() > 0) {
            str.append("Participants:");
            for (String name : participants) {
                str.append(" " + name + ",");
            }
            str.deleteCharAt(str.length() - 1);
            str.append("\n\n");
        }
        try {
            FileWriter file = new FileWriter(filename);
            file.write(str.toString());
            file.write(getResponses());
            file.close();
        } catch (IOException io) {
            System.err.println("Something went wrong while creating graded file!");
            System.exit(1);
        }
    }

    private static String getResponses() {
        StringBuilder str = new StringBuilder();
        int qNum = 1;
        ArrayList<String> responses = new ArrayList<String>();
        while (questions.containsKey(qNum + ".")) {
            str.append(qNum + ".\n");
            responses = questions.get(qNum + ".");
            HashMap<Character, Integer> answers = new HashMap<Character, Integer>();
            for (String response : responses) {
                char c = response.charAt(0);
                int num = 1;
                if (answers.containsKey(c)) {
                    num = answers.get(c) + 1;
                }
                answers.put(c, num);
            }
            Set<Character> letters = answers.keySet();
            char curChar = 'A';
            while (answers.size() > 0) {
                if (answers.containsKey(curChar)) {
                    str.append(TAB);
                    str.append(curChar + ": ");
                    str.append(answers.get(curChar) + "\n");
                    answers.remove(curChar);
                }
                curChar += 1;
            }
            qNum += 1;
        }
        return str.toString();
    }

    /** Keys are question numbers + ".", values are lists of the answers for that question.
     */
    private static HashMap<String, ArrayList<String>> questions;
    
}
