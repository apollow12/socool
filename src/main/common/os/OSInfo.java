package main.common.os;

/**
 * Get the information of the Operating System
 */
public class OSInfo {

    /**
     * The name of the OS
     */
    private static String osName = System.getProperty("os.name").toLowerCase();

    /**
     * "/" in Unix
     */
    private static String fileSeparator = System.getProperty("file.separator");

    /**
     * ":" in Unix
     */
    private static String pathSeparator = System.getProperty("path.separator");

    /**
     * "\n" in Unix
     */
    private static String lineSeparator = System.getProperty("line.separator");

    public static String getOsName()
    {
        return osName;
    }

    public static String getOsArch()
    {
        return System.getProperty("os.arch");
    }

    public static String getOsVersion()
    {
        return System.getProperty("os.version");
    }

    public static String getOsFullInfo()
    {
        StringBuffer info = new StringBuffer();
        info.append(osName).append(" ").append(System.getProperty("os.arch")).append(" ").append(System.getProperty("os.version"));

        return info.toString();
    }

    public static String getUserName() {
        return System.getProperty("user.name");
    }

    public static String getUserHomeDir() {
        return System.getProperty("user.home");
    }

    public static String getUserCurDir() {
        return System.getProperty("user.dir");
    }

    public static boolean isLinux(){
        return osName.indexOf("linux") >= 0;
    }

    public static boolean isMacOS(){
        return (osName.indexOf("mac") >= 0) && (osName.indexOf("os") > 0) && (osName.indexOf("x") < 0);
    }

    public static boolean isMacOSX(){
        return (osName.indexOf("mac") >= 0) && (osName.indexOf("os") > 0) && (osName.indexOf("x") > 0);
    }

    public static boolean isWindows(){
        return (osName.indexOf("windows") >= 0);
    }

    /**
     * "/" in Unix, "\" in Windows
     */
    public static String getFileSeparator()
    {
        return fileSeparator;
    }

    /**
     * ":" in Unix, ";" in Windows, like in PATH system variable
     */
    public static String getPathSeparator()
    {
        return pathSeparator;
    }

    /**
     * "\n" in Unix, "\r\n" (CR+LF) in Windows
     */
    public static String getLineSeparator()
    {
        return lineSeparator;
    }
}
