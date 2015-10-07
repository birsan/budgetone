package ro.birsan.budgetone.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Irinel on 9/8/2015.
 */
public class DateTimeHelper {
    //public static String ISO8601Format = "yyyy-MM-dd HH:mm:ss.SSS";
    public static String ISO8601Format = "yyyy-MM-dd HH:mm:ss";
    public static DateFormat ISO8601DateFormat = new SimpleDateFormat(ISO8601Format);

    public static int monthsBetween(Date date1, Date date2)
    {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(date1);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(date2);

        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        return diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
    }
}
