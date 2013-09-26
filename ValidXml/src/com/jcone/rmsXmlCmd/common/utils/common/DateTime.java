package com.jcone.rmsXmlCmd.common.utils.common;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateTime {

    public static final int YYYYMM = 0;

    public static final int YYYYMMDD = 1;

    public static final int YYYYMMDDHHMI = 2;

    public static final int YYYYMMDDHHMISS = 3;

    /**
     * 문자?�로 ?�어???�짜�?�??�폴???�맷(YYYY-MM-DD)?�로 �?��?�다.<BR>
     * ?�맷?�기??길이�?�?��?�면 ?�래 문자?�을 그�?�?리턴?�다.<BR>
     * <BR>
     * 
     * @param date ?�짜 문자??
     * @param format �?��????��
     * @return String �?��??문자??( yyyy-mm-dd )
     */
    public static String formatDate(String date) {
        return formatDate(date, YYYYMMDD);
    }

    /**
     * 문자?�로 ?�어???�짜�?각각???�맷???�라???�구?�하??반환?�다.<BR>
     * ?�맷?�기??길이�?�?��?�면 ?�래 문자?�을 그�?�?리턴?�다.<BR>
     * <BR>
     * 
     * @param date ?�짜 문자??
     * @param format �?��????��
     * @return String �?��??문자??( yyyy.mm )
     * @return String �?��??문자??( yyyy.mm.dd )
     * @return String �?��??문자??( yyyy.mm.dd hh:mi )
     * @return String �?��??문자??( yyyy.mm.dd hh:mi:ss )
     */
    public static String formatDate(String date, int format) {
        StringBuffer rDate = new StringBuffer();

        if (date == null) {
            return "";
        }
        if (!(format == YYYYMM || format == YYYYMMDD || format == YYYYMMDDHHMI || format == YYYYMMDDHHMISS)) {
            return date;
        }

        if (format == YYYYMM && date.length() < 6) {
            return date;
        }
        if (format == YYYYMMDD && date.length() < 8) {
            return date;
        }
        if (format == YYYYMMDDHHMI && date.length() < 12) {
            return date;
        }
        if (format == YYYYMMDDHHMISS && date.length() < 14) {
            return date;
        }

        rDate.append(date.substring(0, 4));
        rDate.append("-");
        rDate.append(date.substring(4, 6));

        if (format >= YYYYMMDD) {
            rDate.append("-");
            rDate.append(date.substring(6, 8));

            if (format >= YYYYMMDDHHMI) {
                rDate.append(" ");
                rDate.append(date.substring(8, 10));
                rDate.append(":");
                rDate.append(date.substring(10, 12));

                if (format == YYYYMMDDHHMISS) {
                    rDate.append(":");
                    rDate.append(date.substring(12, 14));
                }
            }
        }

        return rDate.toString();
    }   

    // 금일???�짜�?구하??메소??
    public static String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        return date.toString();
    }

    public static Date check(String s) throws ParseException {
        return check(s, "yyyyMMdd");
    }

    public static Date check(String s, String format) throws ParseException {
        if (s == null) {
            throw new ParseException("date string to check is null", 0);
        }
        if (format == null) {
            throw new ParseException("format string to check date is null", 0);
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
        Date date = null;
        try {
            date = formatter.parse(s);
        } catch (ParseException e) {
            throw new ParseException(String.valueOf(String
                    .valueOf((new StringBuffer(" wrong date:\"")).append(s)
                            .append("\" with format \"").append(format).append(
                                    "\""))), 0);
        }
        if (!formatter.format(date).equals(s)) {
            throw new ParseException(String.valueOf(String
                    .valueOf((new StringBuffer("Out of bound date:\"")).append(
                            s).append("\" with format \"").append(format)
                            .append("\""))), 0);
        } else {
            return date;
        }
    }
    
    /**
     * 2005-01-01 Date ??��???�월?�을 20050101 �?�?��?�다.
     * 
     * @param szDate ?�짜.
     * @param szType ?�짜??��
     * @return
     */
    public static String formatDate(String szDate, String szType)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(szType);
        
        String szStringDate = sdf.format(szDate);
        
        return szStringDate;
    }

    public static boolean isValid(String s) throws Exception {
        return isValid(s, "yyyyMMdd");
    }

    public static boolean isValid(String s, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
        Date date = null;
        try {
            date = formatter.parse(s);
        } catch (ParseException e) {
            boolean flag = false;
            return flag;
        }
        return formatter.format(date).equals(s);
    }

    public static String getDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
                Locale.KOREA);
        return formatter.format(new Date());
    }

    public static int getDay() {
        return getNumberByPattern("dd");
    }
    
    public static String getDayString() {
        return getFormatString("dd");
    }

    public static int getYear() {
        return getNumberByPattern("yyyy");
    }
    
    public static String getYearString() {
        return getFormatString("yyyy");
    }

    public static int getMonth() {
        return getNumberByPattern("MM");
    }
    
    public static String getMonthString() {
        return getFormatString("MM");
    }

    public static int getNumberByPattern(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.KOREA);
        String dateString = formatter.format(new Date());
        return Integer.parseInt(dateString);
    }

    public static String getFormatString(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.KOREA);
        String dateString = formatter.format(new Date());
        return dateString;
    }

    public static String getShortDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd",
                Locale.KOREA);
        return formatter.format(new Date());
    }

    public static String getShortTimeString() {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss",
                Locale.KOREA);
        return formatter.format(new Date());
    }

    public static String getTimeStampString() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd-HH:mm:ss:SSS", Locale.KOREA);
        return formatter.format(new Date());
    }
    
    public static String getTimeStampSecoundString() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyyMMddHHmmss", Locale.KOREA);
        return formatter.format(new Date());
    }    

    public static String getTimeString() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss",
                Locale.KOREA);
        return formatter.format(new Date());
    }

    public static int whichDay(String s) throws ParseException {
        return whichDay(s, "yyyyMMdd");
    }

    public static int whichDay(String s, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
        Date date = check(s, format);
        Calendar calendar = formatter.getCalendar();
        calendar.setTime(date);
        return calendar.get(7);
    }

    public static int daysBetween(String from, String to) throws ParseException {
        return daysBetween(from, to, "yyyyMMdd");
    }

    public static int daysBetween(String from, String to, String format)
            throws ParseException {
        //SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
        Date d1 = check(from, format);
        Date d2 = check(to, format);
        long duration = (d2.getTime() - d1.getTime())/1000;
        return (int) duration;
    }

    public static int ageBetween(String from, String to) throws ParseException {
        return ageBetween(from, to, "yyyyMMdd");
    }

    public static int ageBetween(String from, String to, String format)
            throws ParseException {
        return daysBetween(from, to, format) / 365;
    }

    public static String addDays(String s, int day) throws ParseException {
        return addDays(s, day, "yyyyMMdd");
    }

    public static String addDays(String s, int day, String format)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
        Date date = check(s, format);
        date.setTime(date.getTime() + (long) day * (long) 1000 * (long) 60
                * (long) 60 * (long) 24);
        return formatter.format(date);
    }

    public static String addMonths(String s, int month) throws Exception {
        return addMonths(s, month, "yyyyMMdd");
    }

    public static String addMonths(String s, int addMonth, String format)
            throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
        Date date = check(s, format);
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.KOREA);
        int year = Integer.parseInt(yearFormat.format(date));
        int month = Integer.parseInt(monthFormat.format(date));
        int day = Integer.parseInt(dayFormat.format(date));
        month += addMonth;
        if (addMonth > 0) {
            while (month > 12) {
                month -= 12;
                year++;
            }
        } else {
            while (month <= 0) {
                month += 12;
                year--;
            }
        }
        DecimalFormat fourDf = new DecimalFormat("0000");
        DecimalFormat twoDf = new DecimalFormat("00");
        String tempDate = String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(String.valueOf(fourDf
                        .format(year)))))).append(
                String.valueOf(twoDf.format(month))).append(
                String.valueOf(twoDf.format(day)))));
        Date targetDate = null;
        try {
            targetDate = check(tempDate, "yyyyMMdd");
        } catch (ParseException pe) {
            day = lastDay(year, month);
            tempDate = String.valueOf(String.valueOf((new StringBuffer(String
                    .valueOf(String
                            .valueOf(String.valueOf(fourDf.format(year))))))
                    .append(String.valueOf(twoDf.format(month))).append(
                            String.valueOf(twoDf.format(day)))));
            targetDate = check(tempDate, "yyyyMMdd");
        }
        return formatter.format(targetDate);
    }

    public static String addYears(String s, int year) throws Exception {
        return addYears(s, year, "yyyyMMdd");
    }

    public static String addYears(String s, int year, String format)
            throws Exception {
        return addMonths(s, year * 12);
    }

    public static int monthsBetween(String from, String to)
            throws ParseException {
        return monthsBetween(from, to, "yyyyMMdd");
    }

    public static int monthsBetween(String from, String to, String format)
            throws ParseException {
        //SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
        Date fromDate = check(from, format);
        Date toDate = check(to, format);
        if (fromDate.compareTo(toDate) == 0) {
            return 0;
        }
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.KOREA);
        int fromYear = Integer.parseInt(yearFormat.format(fromDate));
        int toYear = Integer.parseInt(yearFormat.format(toDate));
        int fromMonth = Integer.parseInt(monthFormat.format(fromDate));
        int toMonth = Integer.parseInt(monthFormat.format(toDate));
        int fromDay = Integer.parseInt(dayFormat.format(fromDate));
        int toDay = Integer.parseInt(dayFormat.format(toDate));
        int result = 0;
        result += (toYear - fromYear) * 12;
        result += toMonth - fromMonth;
        if (toDay - fromDay > 0) {
            result += toDate.compareTo(fromDate);
        }
        return result;
    }

    public static String lastDayOfMonth(String src) throws ParseException {
        return lastDayOfMonth(src, "yyyyMMdd");
    }

    public static String lastDayOfMonth(String src, String format)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
        Date date = check(src, format);
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        int year = Integer.parseInt(yearFormat.format(date));
        int month = Integer.parseInt(monthFormat.format(date));
        int day = lastDay(year, month);
        DecimalFormat fourDf = new DecimalFormat("0000");
        DecimalFormat twoDf = new DecimalFormat("00");
        String tempDate = String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(String.valueOf(fourDf
                        .format(year)))))).append(
                String.valueOf(twoDf.format(month))).append(
                String.valueOf(twoDf.format(day)))));
        date = check(tempDate, format);
        return formatter.format(date);
    }

    private static int lastDay(int year, int month) throws ParseException {
        int day = 0;
        switch (month) {
            case 1 : // '\001'
            case 3 : // '\003'
            case 5 : // '\005'
            case 7 : // '\007'
            case 8 : // '\b'
            case 10 : // '\n'
            case 12 : // '\f'
                day = 31;
                break;

            case 2 : // '\002'
                if (year % 4 == 0) {
                    if (year % 100 == 0 && year % 400 != 0) {
                        day = 28;
                    } else {
                        day = 29;
                    }
                } else {
                    day = 28;
                }
                break;

            case 4 : // '\004'
            case 6 : // '\006'
            case 9 : // '\t'
            case 11 : // '\013'
            default :
                day = 30;
                break;
        }
        return day;
    }
}
