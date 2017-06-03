package main.common.log;

public class StandardOutputLogger extends AbstractLogger {

    /*
      Get instance
     */
    private StandardOutputLogger() {
        super.startLogging();
    }

    private static class LazyHolder {
        private static final StandardOutputLogger INSTANCE = new StandardOutputLogger();
    }

    public static StandardOutputLogger getInstance() {
        return LazyHolder.INSTANCE;
    }


    /*
      Class functions
     */

    @Override
    protected void log(String message) {
        System.out.println(message);
    }

    @Override
    public void logInfo(String info) {
        System.out.println(super.buildInfoMsg(info));
    }

    @Override
    public void logWarning(String warning) {
        System.out.println(super.buildWarningMsg(warning));
    }

    @Override
    public void logError(String error) {
        System.out.println(super.buildErrorMsg(error));
    }

    @Override
    public void shutdown() {
        super.stopLogging();
    }
}
