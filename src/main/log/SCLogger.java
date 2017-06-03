package main.log;

/**
 * Created on 5/27/17.
 *
 * Log to (by default both) standard and file output.
 */
public class SCLogger extends AbstractLogger {

    //region Properties
    StandardOutputLogger mSTDOutLogger = null;
    FileLogger mFileLogger = null;

    //endregion


    //region Getting instance
    private SCLogger() {}

    private void init(String logFileName) {
        mSTDOutLogger = StandardOutputLogger.getInstance();

        synchronized (this) {
            if (logFileName != null && logFileName.length() != 0) {
                FileLogger.setFilePathAndStartLogger(logFileName);
                mFileLogger = FileLogger.getInstance();
                mSTDOutLogger.logInfo("File logger established with file: " + logFileName);
            } else {
                mSTDOutLogger.logInfo("No file logger is available due to non-valid file name.");
            }
        }
    }

    private static class LazyHolder {
        private static final SCLogger INSTANCE = new SCLogger();
    }

    /**
     * Establish file logger
     * @param logFileName
     */
    public static void setFilePathAndStartLogger(String logFileName) {
        SCLogger instance = LazyHolder.INSTANCE;
        instance.init(logFileName);
    }

    /**
     * setFilePathAndStartLogger must be called first
     * @return
     */
    public static SCLogger getInstance() {
        return LazyHolder.INSTANCE;
    }

    //endregion


    //region Functions

    @Override
    protected void log(String msg) {
        if (mSTDOutLogger != null) {
            mSTDOutLogger.log(msg);
        }
        if (mFileLogger != null) {
            mFileLogger.log(msg);
        }
    }

    @Override
    public void logInfo(String info) {
        if (mSTDOutLogger != null) {
            mSTDOutLogger.logInfo(info);
        }
        if (mFileLogger != null) {
            mFileLogger.logInfo(info);
        }
    }

    @Override
    public void logWarning(String warning) {
        if (mSTDOutLogger != null) {
            mSTDOutLogger.logWarning(warning);
        }
        if (mFileLogger != null) {
            mFileLogger.logWarning(warning);
        }
    }

    @Override
    public void logError(String error) {
        if (mSTDOutLogger != null) {
            mSTDOutLogger.logError(error);
        }
        if (mFileLogger != null) {
            mFileLogger.logError(error);
        }
    }

    @Override
    public void shutdown() {
        if (mSTDOutLogger != null) {
            mSTDOutLogger.shutdown();
        }
        if (mFileLogger != null) {
            mFileLogger.shutdown();
        }
    }

    //endregion
}
