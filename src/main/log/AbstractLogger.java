package main.log;

import main.os.ComputerName;
import main.os.OSInfo;
import main.utilities.SCDateTime;

public abstract class AbstractLogger {

    public static final String INFO = "[INFO] ";
    public static final String WARNING = "[? WARNING] ";
    public static final String ERROR = "[!!! ERROR] ";


    /**
     * version infomration
     */
    protected final String VERSION_INFO = "SoCool, Version: " + main.common.ProjectInfo.VERSION;

    /*
        Functions
     */

    /**
     * Log the input msg
     * @param msg
     */
    abstract protected void log(String msg);

    /**
     * Log a info in the format of "[date time] INFO | info"
     * @param info
     */
    abstract public void logInfo(String info);
    /**
     * Log a warning in the format of "[date time] WARNING | warning"
     * @param warning
     */
    abstract public void logWarning(String warning);
    /**
     * Log an error in the format of "[date time] ERROR | error"
     * @param error
     */
    abstract public void logError(String error);

    public void startLogging() {
        this.log(messageForLoggingStart());
    }
    public void stopLogging() {
        this.log(messageForLoggingStop());
    }

    abstract public void shutdown();


    /*
        Helper functions
     */

    protected String buildMsgWithDateTimePrefix() {
        String prefix = "[" + SCDateTime.getCurDateTime() + "] ";
        return prefix;
    }

    protected String buildInfoMsg(String info) {
        return this.buildMsgWithDateTimePrefix() + INFO + info;
    }

    protected String buildWarningMsg(String warning) {
        return this.buildMsgWithDateTimePrefix() + WARNING + warning;
    }

    protected String buildErrorMsg(String error) {
        return this.buildMsgWithDateTimePrefix() + ERROR + error;
    }

    /**
     * The message when logging starts.
     * @return
     */
    protected String messageForLoggingStart() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("#======================================#\n")
                .append("# " + VERSION_INFO + "\n")
                .append("# Computer name: " + ComputerName.getComputerName() + "\n")
                .append("# OS: " + OSInfo.getOsFullInfo() + "\n")
                .append("# User: " + OSInfo.getUserName() + "\n")
                .append("# Home dir: " + OSInfo.getUserHomeDir() + "\n")
                .append("# Current dir: " + OSInfo.getUserCurDir() + "\n")
                .append("# Start time: " + SCDateTime.getCurDateTime() + "\n")
                .append("#======================================#\n");

        return sbuilder.toString();
    }

    protected String messageForLoggingStop() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("\n#======================================#\n")
                .append("# Stop time: " + SCDateTime.getCurDateTime() + "\n")
                .append("#======================================#\n");

        return sbuilder.toString();
    }
}
