package qz;

public class Usage {

    public static void printQuizUsage() {
        System.out.println(QUIZ_USAGE);
    }

    public static void printGradeUsage() {
        System.out.println(GRADE_USAGE);
    }

    public static void printTestUsage() {
        System.out.println(TEST_USAGE);
    }

    private static final String QUIZ_USAGE = "qz.Quiz Usage Instructions\n\n qz.Quiz can be used to either create or take multiple choice quizzes. To take a quiz, run the program with the \"-t\" flag. The default behavior is to create a quiz, but this can also be explicitly specified through the \"-c\" flag.\n\n By default, a quiz will ask for the name of the quiz taker. To create an anonymous quiz, use the \"-a\" flag when creating it. To take a quiz anonymously that was not created using the \"-a\" flag, use the \"-a\" flag in combination with the \"-t\" flag.\n\n After specifying all flags, the name of the quiz can be optionally specified. The default name of a created quiz will be \"quiz.qz\" unless otherwise specified; if no filename is specified and the \"-t\" flag has been raised, the program will search for a quiz \"quiz.qz\" in the current directory. All quizzes will be appended with a \".qz\" tail, and all answer files with a \".ans\" tail.\n\n While creating a quiz, press the ENTER key to complete a question or answer. When all desired answers have been inputted, press answer on a blank answer line to complete the question. To complete the quiz, press the ENTER key on a blank question line.\n\n While taking a quiz, the only acceptable responses to a question are the letters of the choices displayed for each question, in upper- or lower-case. To exit a quiz prematurely, enter the \"quit\" command.\n";

    private static final String GRADE_USAGE = "Grade Usage Instructions\n\n";

    private static final String TEST_USAGE = "\nWhile taking a quiz, the only acceptable responses to a question are the letters of the choices displayed for each question, in upper- or lower-case. To exit a quiz prematurely, enter the \"quit\" command.\n";


}
