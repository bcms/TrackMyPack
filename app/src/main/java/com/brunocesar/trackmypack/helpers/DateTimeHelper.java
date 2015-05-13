package com.brunocesar.trackmypack.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BrunoCesar on 05/05/2015.
 */
public class DateTimeHelper {

   /* public static String toDatabase(String dateStr, String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        String result = null;

        try {
            simpleDateFormat.applyPattern(format);
            Date date = simpleDateFormat.parse(dateStr);

            simpleDateFormat.applyPattern(FormatConstants.DATABASE_DATE_TIME_FORMAT);
            result = simpleDateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String fromDatabase(String dateStr, String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        String result = null;

        try {
            simpleDateFormat.applyPattern(FormatConstants.DATABASE_DATE_TIME_FORMAT);
            Date date = simpleDateFormat.parse(dateStr);

            simpleDateFormat.applyPattern(format);
            result = simpleDateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }*/
}
