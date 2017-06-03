package main.log;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created on 5/27/17.
 */
class LogFileWriterTest {

    File testDir;
    File testFile;

    String testDirName = "testLogDir";
    String baseFileName = "test_log_file_";
    String logFileName = null;

    @BeforeEach
    void setUp() {
        // create testDir
        testDir = new File(testDirName);
        testDir.mkdir();

        // prepare file name
        logFileName = baseFileName + UUID.randomUUID();
        testFile = new File(testDir, logFileName);
    }

    @AfterEach
    void tearDown() {
        // remove file logFileName, testDir
        deleteDirWithFilesNoSubDir(testDir);
        logFileName = null;
    }

    @Test
    void getWriterWithFileName() {
        LogFileWriter writer = null;
        try {
            writer = LogFileWriter.getWriterWithFileName(testFile.getAbsolutePath());

            assertEquals(writer.getLogFilePath(), testFile.getAbsolutePath());
        } catch (IOException ioe) {
            assertTrue(false);  // exception means error
        } finally {
            if (writer != null) {
                writer.closeFile();
            }
        }
    }

    @Test
    void println() {
        String content = "hello hahaha";
        LogFileWriter writer = null;
        try {
            writer = LogFileWriter.getWriterWithFileName(testFile.getAbsolutePath());

            writer.println(content);

        } catch (IOException ioe) {
            assertTrue(false);  // exception means error
        } finally {
            if (writer != null) {
                writer.closeFile();
            }
        }

        String fileContent = readTestFile(testFile);
        assertTrue(fileContent.equals(content));
    }

    @Test
    void appendToPrintln() {

        // 1. write something
        String content1 = "hello hahaha";
        LogFileWriter writer = null;
        try {
            writer = LogFileWriter.getWriterWithFileName(testFile.getAbsolutePath());

            writer.println(content1);

        } catch (IOException ioe) {
            assertTrue(false);  // exception means error
        } finally {
            if (writer != null) {
                writer.closeFile();
            }
        }

        // 2. append something
        String content2 = " qawetjlgal";
        try {
            writer = LogFileWriter.getWriterWithFileName(testFile.getAbsolutePath());

            writer.println(content2);

        } catch (IOException ioe) {
            assertTrue(false);  // exception means error
        } finally {
            if (writer != null) {
                writer.closeFile();
            }
        }

        String contentInFile = readTestFile(testFile);
        assertTrue(contentInFile.equals(content1 + content2));  // TODO should have a \n in between?
    }

    /**
     * Delete a directory (maybe have files)
     * @param directory
     */
    private void deleteDirWithFilesNoSubDir(File directory) {
        if (directory != null && directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete();
                }
            }

            directory.delete();
        }
    }

    /**
     * Read the content of the test file
     * @param testFile
     * @return
     */
    private String readTestFile(File testFile) {

        BufferedReader breader = null;

        try {
            breader = new BufferedReader(new FileReader(testFile));

            String line;
            StringBuilder content = new StringBuilder();
            while (null != (line = breader.readLine())) {
                content.append(line);
            }

            return content.toString();

        } catch (IOException ioe) {
            return null;
        } finally {
            if (breader != null) {
                try {
                    breader.close();
                } catch (IOException ioe) {}
            }
        }
    }
}