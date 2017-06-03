package main.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Non-thread-safe. Relying on the caller to be thread safe.
 */
public class LogFileWriter {

    private File mLogFile;

//    private PrintStream mOutStream;
    private BufferedWriter mBufferedWritter;

    public static LogFileWriter getWriterWithFileName(String fileName) throws IOException {
        LogFileWriter instance = new LogFileWriter();

//        instance.mFileName = fileName;

        instance.mLogFile = new File(fileName);
        if (!instance.mLogFile.exists()) {
            instance.mLogFile.createNewFile();
        }

        instance.mBufferedWritter = new BufferedWriter(new FileWriter(fileName, true));

//        logFileWriter.mOutStream =
//                new PrintStream(new BufferedOutputStream(
//                        new FileOutputStream(fileName, true))
//                );


        return instance;
    }

    public String getLogFilePath() {
        return mLogFile.getAbsolutePath();
    }

    public void println(String text) {
        try {
            mBufferedWritter.write(text);
            mBufferedWritter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeFile() {
        try {
            mBufferedWritter.flush();
            mBufferedWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        mOutStream.flush();
//        mOutStream.close();
    }

    @Override
    protected void finalize() {
        closeFile();
    }
}
