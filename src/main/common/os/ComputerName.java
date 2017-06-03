package main.common.os;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Created on 5/27/17.
 */
public class ComputerName {

    private static final String STR_COMPUTERNAME = "COMPUTERNAME";
    private static final String STR_HOSTNAME = "HOSTNAME";

    public static String getComputerName() {
        Map<String, String> env = System.getenv();

        if (env.containsKey(STR_COMPUTERNAME)) {  // windows
            return env.get(STR_COMPUTERNAME);
        } else if (env.containsKey(STR_HOSTNAME)) {
            return env.get(STR_HOSTNAME); // Linux and Mac?
        } else {
            return getNetHostName();
        }
    }

    private static String getNetHostName() {
        String hostname = "Unknown Computer";

        try {
            InetAddress addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException uhe) {

        }
        return hostname;
    }
}
