package main.common.config;

import main.common.debug.Debug;
import main.common.log.StandardOutputLogger;
import main.common.utilities.SCDateTime;
import main.common.utilities.SCString;

import java.io.IOException;

/**
 * Config of the system.
 *
 * This is used to store the default and read-in config
 */
public class Config
{
	/**
	 * Default name of the configuration file
	 */
	public static final String DEFAULT_CONFIG_FILE = "config.ini";

	/**
	 * Name of the configuration file
	 */
	public static String mConfigFile = DEFAULT_CONFIG_FILE;

	/**
	 * Logger to standard IO
	 */
	private static StandardOutputLogger mStdLogger = StandardOutputLogger.getInstance();

	/*
	 * =============== Region : COMMON ==============================================
	 */

	/**
	 * Flag for whether debug mode is on.
	 * If true, there will be debug statements.
	 */
	public static boolean DEBUG_ON = true;

	/**
	 * Default name for the log directory and file
	 */
	public static String LOG_DIR = null;
	public static String LOG_FILE = null;

	/*
	 * =============== Region : CRAWLER ==============================================
	 */



	/*
	 * =============== Region : DATA HOUSE ==============================================
	 */




	/*
	 * =============== Region: SEARCH ENGINE ==============================================
	 */



	/*
	 * =============== Region: CONTENT PRESENTER ==============================================
	 */



	/*
	 * =============== Region Ends ==============================================
	 */





	/**
	 * Reads the configuration file
	 * @param configFile
	 *            Configuration file. If null, default name "config.ini" will be used.
	 */
	public static void readConfigFile(String configFile)
	{
		if(configFile != null) {
            mConfigFile = configFile;
        }

		ConfigReader configReader = null;

		try {
			configReader = new ConfigReader(mConfigFile);
			mStdLogger.logInfo("Reading config file: " + configReader.getConfigFileName());
		} catch(IOException ioe) {
			mStdLogger.logError("ERROR reading config file: " + mConfigFile + ": " + ioe.getLocalizedMessage());
			System.exit(1);
		}

		// print the config
		configReader.printConfig();

		fillValues(configReader);
	}

	/**
	 * Read and fill the values for keys
	 * @param configReader
	 */
	private static void fillValues(ConfigReader configReader)
	{
		int value_Int = -1;

		/*
		 * =============== Region. Common parameters ==============================================
		 */
		// DEBUG_ON
		if(configReader.hasValue("DEBUG_ON"))
		{
			value_Int = configReader.getInt("DEBUG_ON");
			if(value_Int == 1) {
				DEBUG_ON = true;
				mStdLogger.logInfo("Debug Mode enabled");
			}
			else if(value_Int == 0) {
				DEBUG_ON = false;
				mStdLogger.logInfo("Debug Mode disabled, DEBUG_ON：" + value_Int);
			}
			Debug.debug_on = DEBUG_ON;
		}

		// LOG_DIR
        String value_String = "";
        if (configReader.hasValue("LOG_DIR")) {
            value_String = configReader.getString("LOG_DIR");
            if (!SCString.isEmpty(value_String)) {
                LOG_DIR = value_String;
            }
            mStdLogger.logInfo("Log to directory: " + LOG_DIR);
        }

        // LOG_FILE
        value_String = "";
		if (configReader.hasValue("LOG_FILE")) {
			value_String = configReader.getString("LOG_FILE");
			if (value_String != null && value_String.length() > 0) {
				LOG_FILE = SCDateTime.getCurDateTimeCompact() + "_" + value_String;
//                LOG_FILE = value_String;
			}
			mStdLogger.logInfo("Log to file: " + LOG_FILE);
		}

		/*
		 * =============== Region. Crawler ==============================================
		 */

	}

/*
 * ========= Region Starts: test ================================================
 */

	public static void runMain(String[] args)
	{
		Config.readConfigFile("config.ini");

	}
/*
* ========= Region Ends ==================================================
*/
}
