package com.jasen.kimjaeseung.homebase.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kimjaeseung on 2018. 3. 11..
 */

public class DateUtils {
    public static Calendar StringToCalendar(String day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date scheduleDate = null;
        try {
            scheduleDate = sdf.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(scheduleDate);

        return calendar;
    }
}
