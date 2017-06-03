package main;

import main.config.Config;
import main.crawler.DirExplorer;
import main.log.SCLogger;

import java.io.File;

/**
 * This project cannot be for any commercial use without the permission of the author
 *
 * The overall main program.
 * Each component may has its own main function.
 *
 * @author yaoyao
 * @version 0.1
 *
 * HISTORY:
 * 	2017.5.27	created
 */
public class MainProgramSoCool {

    public static String HELP_MESSAGE = "Usage:\t" + "java MainProgramSoCool <config file>";

    public static void main(String[] args) {
        if((args == null) || (args.length == 0)){
            System.out.println(HELP_MESSAGE);
            System.exit(1);
        }

        initConfig(args[0]);

        setupLogger(Config.LOG_DIR, Config.LOG_FILE);

        stopLogger();

        testDirExplore();
    }

    /**
     * Load config from file. If no file, use default config in Config.java
     * @param configFileName the name of the config file.
     */
    private static void initConfig(String configFileName) {
        Config.readConfigFile(configFileName);
    }

    /**
     * Setup logger. The standard out logger won't really need to be set up.
     * It's more setting up file logger, with the dirName and filename read from config.
     * @param dirName the directory name of the logs
     * @param logFileName the file name of the log file. If null, there will be no file logger
     */
    private static void setupLogger(String dirName, String logFileName) {
        File dir = new File(dirName);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                System.err.println("Failed to create a directory: " + dir.getAbsolutePath());
                System.exit(1);
            }
        }
        File logFile = new File(dir, logFileName);

        SCLogger.setFilePathAndStartLogger(logFile.getAbsolutePath());
    }

    private static void stopLogger() {
        SCLogger.getInstance().shutdown();
    }

    /**
     * Start the file crawlers
     */
    private static void runCrawlers() {

    }

    private static void stopCrawlers() {

    }

    private static void testDirExplore() {
        String path = "/Users/yifan";
        String[] excludedDirPaths = new String[]{"/Users/yifan/abc"};

        DirExplorer de = new DirExplorer();
        de.startExploreNIO(path, excludedDirPaths);

        System.out.println("File count new IO: " + de.getCount_file());
        System.out.println("Dir count: " + de.getCount_dir());
        System.out.println("Time spent: " + de.timespent() + " ms");
    }
}

