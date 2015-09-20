package ro.birsan.budgetone.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Irinel on 9/8/2015.
 */
public class DateTimeHelper {
    //public static String ISO8601Format = "yyyy-MM-dd HH:mm:ss.SSS";
    public static String ISO8601Format = "yyyy-MM-dd HH:mm:ss";
    public static DateFormat ISO8601DateFormat = new SimpleDateFormat(ISO8601Format);
}
