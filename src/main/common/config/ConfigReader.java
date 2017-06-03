package main.common.config;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;


/**
 * This class is used to read configuration file
 * 
 */
public class ConfigReader
{
	File configFile;
	
	/**
	 * Hashmap to store values of the keys
	 */
	Map<String, String> keyValueMap = new HashMap<String, String>();
	
	/**
	 * Constructs the ConfigReader. Read the configuration file
	 * @param configFileName
	 *            name of the config file
	 * @throws IOException
	 */
	public ConfigReader(String configFileName) throws IOException {
		configFile = new File(configFileName);
		
		BufferedReader bReader = new BufferedReader(new FileReader(configFile));
		
		//BufferedReader bReader = new BufferedReader( new FileReader(configFileName));
		
		String line;
		
		while((line = bReader.readLine()) != null) {
			// trim white spaces
			line = line.trim();
			
			if (line.length() == 0) {}	// empty line
			else if (line.startsWith("#")) {}	// comment line
			else {
				int posEq = line.indexOf("=");
				if(posEq == -1){continue;}	// no "=" in the line
				
				String keyName = line.substring(0, posEq);
				String value = line.substring(posEq + 1, line.length());
				
				// trim. Make keys in upper case
				keyName = keyName.trim().toUpperCase();
				value = value.trim();
				
				// put <key, value> in map
				keyValueMap.put(keyName, value);
			}
		}
		
		bReader.close();
	}
	
	public String getConfigFileName() {
		return configFile.getAbsolutePath();
	}
	
	/**
	 * Returns true if the specified key has a value
	 * @param key
	 *            key. can be in either lower or upper case
	 * @return true if the specified key has a value
	 */
	public boolean hasValue(String key)
	{
		return keyValueMap.containsKey(key.toUpperCase().trim());
	}
	
	/**
	 * Get value in String of the specified key.
	 * Key will be converted to a String in upper case.
	 * @param key
	 *            key. can be in either lower or upper case
	 * @return value of the specified key, in String
	 */
	public String getString(String key)
	{
		return keyValueMap.get(key.toUpperCase().trim());
	}
	
	/**
	 * Get value in int of the specified key
	 * @param key
	 *            key
	 * @return value of the specified key, in int
	 */
	public int getInt(String key) {
		try {
			return Integer.parseInt(getString(key));
		} catch (Exception e) {
			System.err.println("Invalid int for key: " + key + ". " + e.getLocalizedMessage());
			return -1;
	    }
	}
	
	/**
	 * Get value in double of the specified key
	 * @param key
	 *            key
	 * @return value of the specified key, in double
	 */
	public double getDouble(String key) {
		try {
			return Double.parseDouble(getString(key));
		} catch (Exception e) {
			System.err.println("Invalid double for key: " + key + ". " + e.getLocalizedMessage());
			return -1;
		}
	}

    public void printConfig() {
        System.out.println("========== Config ==========");
        for(String key : keyValueMap.keySet())
            System.out.println(key + ": " + keyValueMap.get(key));
        System.out.println("============================");
    }

/*
 * ========= Region Starts: for test ================================================
 */

	public static void runMain(String[] args) {
		if((args == null) || args.length == 0) {
			System.out.println("Specify the config file as the first parameter.");
			args = new String[]{"config.ini"};	// hard code for test
		}
		
		try {
			ConfigReader cr = new ConfigReader(args[0]);
			cr.printConfig();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
/* 
 * ========= Region Ends ==================================================
 */
}

