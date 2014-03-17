package qz;

import java.io.BufferedReader;

import java.util.HashMap;

public class Grade {

    static final String USAGE = "/Users/Joel/CompSci/qz/gradeUsage.txt";
    
    public static void main(String... args) {
        if (args.length > 0) {
            answers = new HashMap<Integer, String>();
            parseArgs(args);
        } else {
            Quiz.printUsage(USAGE);
        }
    }

    private static void parseArgs(String[] args) {
        BufferedReader file = Quiz.getFile(args[0]);
    }

    /** Keys are question numbers, values are answers.
     *  Answers in form "B: 2", ie 2 people answered with B.
     */
    private static HashMap<Integer, String> answers;
    
}
