package main.debug;

import main.utilities.SCDateTime;

/**
 * For ease of debug
 */
public class Debug {

    public static boolean debug_on = true;

    private static final String START_DEBUG_MSG = "\n>>>>>>>>>>>>>>> debug >>>>>>>>>>>>>>>\n";
    private static final String END_DEBUG_MSG = "\n<<<<<<<<<<<<<<< debug end <<<<<<<<<<<<<<<\n";


    /**
     * Print the debug message to standard output
     * @param debugMsg
     */
    public static void println(String debugMsg) {
        if (debug_on) {
            StringBuilder sbuilder = new StringBuilder();
            sbuilder.append(START_DEBUG_MSG).append(debugMsg).append(END_DEBUG_MSG);
            System.out.println(sbuilder.toString());
        }
    }

    /**
     * Print the debug message with timestamp
     * @param debugMsg
     */
    public static void printlnWithTimestamp(String debugMsg) {
        if (debug_on) {
            StringBuilder sbuilder = new StringBuilder();
            sbuilder.append(START_DEBUG_MSG);
            sbuilder.append(SCDateTime.getCurDateTime()).append("\n");
            sbuilder.append(debugMsg).append(END_DEBUG_MSG);
            System.out.println(sbuilder.toString());
        }
    }
}
