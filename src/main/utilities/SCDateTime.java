package main.utilities;

//import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SCDateTime {

    /**
     * Format of the date time used
     * z is time zone
     * SSS is millisecond
     */
    private final static String STR_DATE_FORMAT = "yyyy-MM-dd z HH:mm:ss.SSS";

    protected final static SimpleDateFormat mDateFormat = new SimpleDateFormat(STR_DATE_FORMAT);

//    @NotNull
    public static String getDateTime(long timeMilliSec) {
        return mDateFormat.format(new Date(timeMilliSec));
    }

//    @NotNull
    public static String getCurDateTime() {
        long curTime = System.currentTimeMillis();
        return getDateTime(curTime);
    }

    /**
     *
     * @return 20150101_1000
     */
    public static String getCurDateTimeCompact() {
        SimpleDateFormat compactDateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
        return compactDateFormat.format(new Date(System.currentTimeMillis()));
    }
}