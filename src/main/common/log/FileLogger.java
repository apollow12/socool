package main.common.log;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created on 5/27/17.
 *
 * Use a blocking queue to log to file. Log message to the file asynchronously
 */
public class FileLogger extends AbstractLogger {

    /*
      Properties
     */
    private ExecutorService mExecuterService;
    private BlockingQueue<String> mBlockingQueue;

    public static final String SHUTDOWN_LOGGER_MESSAGE = "SHUTDOWN | STOP THE FILE LOGGER";

    private LogFileWriter mLogFileWriter = null;

    private static boolean initialized = false;

    /*
      Get instance
     */
    private FileLogger() {}

    private void init(String fileName) {

        // guarantee only initialize for one time.
        synchronized (this) {
            if (initialized) {
                System.err.println("Unable to initialize file logger twice.");
                System.exit(1);
            }
            initialized = true;

            try {
                mLogFileWriter = LogFileWriter.getWriterWithFileName(fileName);
            } catch (IOException ioe) {
                System.err.println("Unable to start file logger");
                mLogFileWriter = null;
                return;
            }

            mExecuterService = Executors.newSingleThreadExecutor();
            mBlockingQueue = new LinkedBlockingQueue<String>();

            mExecuterService.execute(new LogWhenMessageAvailable(mBlockingQueue, mLogFileWriter));
        }

        super.startLogging();
    }

    private static class LazyHolder {
        private static final FileLogger INSTANCE = new FileLogger();
    }

    public static FileLogger getInstance() {
        return LazyHolder.INSTANCE;
    }

    public static void setFilePathAndStartLogger(String fileName) {
        FileLogger instance = LazyHolder.INSTANCE;
        instance.init(fileName);
    }


    /*
      Class functions
     */

    @Override
    protected void log(String msg) {
        if (mLogFileWriter == null) {
            System.err.println("File logger not started correctly.");
        }

        if (msg != null && msg.length() != 0) {
            boolean offerToQueue = mBlockingQueue.offer(msg);   // fail if no space is available
            if (!offerToQueue) {
                System.err.println("Put to file logger's blocking queue failed: " + msg);
            }
        }
    }

    /**
     * Log information asynchronously
     * @param info
     */
    @Override
    public void logInfo(String info) {
        if (info != null && info.length() != 0) {
            this.log(super.buildInfoMsg(info));
        }
    }

    @Override
    public void logWarning(String warning) {
        if (warning != null && warning.length() != 0) {
            this.log(super.buildWarningMsg(warning));
        }
    }

    @Override
    public void logError(String error) {
        if (error != null && error.length() != 0) {
            this.log(super.buildErrorMsg(error));
        }
    }

    /**
     * Shut down the file logger asynchronously
     */
    @Override
    public void shutdown() {
        super.stopLogging();
        this.log(SHUTDOWN_LOGGER_MESSAGE);
        mExecuterService.shutdown();
    }

    /*
        Executed by the logging thread
     */
    class LogWhenMessageAvailable implements Runnable {

        private BlockingQueue<String> blockingQueue;
        private LogFileWriter logFileWriter;

        public LogWhenMessageAvailable(BlockingQueue<String> queue, LogFileWriter writer) {
            this.blockingQueue = queue;
            this.logFileWriter = writer;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String msg = this.blockingQueue.take(); // wait indefinitely if queue is empty
                    if (msg.equals(SHUTDOWN_LOGGER_MESSAGE)) {
                        this.logFileWriter.closeFile(); // have to manually close the writer
                        return; // exit the logger
                    }
                    this.logFileWriter.println(msg);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

}
