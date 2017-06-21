package vn.com.aris.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;


public class DateUtils {

    static final String DATE_FORMAT = "yyyy/MM/dd";
    static final String DATE_FROMAT_SQL = "%Y/%m/%d";
    static final String DATE_FORMAT_CSV = "yyyyMMdd";
    public static final String APPLE_REPORT_DATE_FORMAT = "yyyyMMdd";
    public static final String GOOGLE_REPORT_DATE_FORMAT = "yyyyMM";
    
    /**
     * 
     * @method convertDateFromSql
     * @description Convert string to Date (java.util.Date) with default format
     * @author dung.dd
     * @date Nov 23, 2015
     * @param
     * @return Date
     */
    public static Date convertDateFromSql(String dateStr){
        Date date = null;
        
        try {
            
            if(StringUtils.isNotEmpty(dateStr)) {
                SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
                date = sf.parse(dateStr);
            }
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    /**
     * 
     * @method ConvertDateToString
     * @description Convert Date to String (with default format0
     * @author dung.dd
     * @date Nov 23, 2015
     * @param
     * @return String
     */
    public static String ConvertDateToString(Date date){
        String result = "";
        
        if(date != null) {
            SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
            result = sf.format(date);
        }
        
        return result;
    }
    
    /**
     * 
     * @method combineDateSql
     * @description combine variable Date to Sql
     * @author dung.dd
     * @date Nov 24, 2015
     * @param
     * @return String
     */
    public static String combineDateSql(String dateValue){
        return " DATE_FORMAT( " + dateValue + ", '" + DATE_FROMAT_SQL + "') ";
    }
    
    /**
     * 
     * @method getWeekOfYearFromDate
     * @description
     * @author dung.dd
     * @date Nov 24, 2015
     * @param
     * @return int
     */
    public static int getWeekOfYearFromDate(Date date) {
        Calendar calendar  = Calendar.getInstance();   
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        System.out.println(calendar.get(Calendar.WEEK_OF_YEAR));
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }
    
    /**
     * 
     * @method getCalenderFromDate
     * @description
     * @author dung.dd
     * @date Nov 24, 2015
     * @param
     * @return Calendar
     */
    public static Calendar getCalenderFromDate(Date date) {
        Calendar calendar  = Calendar.getInstance(Locale.GERMAN);   
        calendar.setTime(date);
        return calendar;
    }
    
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = sdf.parse("2015/11/16");
        
        Date d2 = sdf.parse("2015/12/06");
        

        int numberOfDays = calculateNumberOfDaysBetween(date, d2);
        int numberOfWeeks = calculateNumberOfWeeks(date, d2);
        System.out.println(numberOfDays);


        
    }
    
    
    /**
     * 
     * @method calculateNumberOfDaysBetween
     * @description
     * @author dung.dd
     * @date Nov 26, 2015
     * @param
     * @return int
     */
    public static int calculateNumberOfDaysBetween(Date startDate, Date endDate) {
        Calendar cal = Calendar.getInstance();
        if (startDate.before(endDate)) {
            cal.setTime(startDate);
        } else {
            cal.setTime(endDate);
            endDate = startDate;
        }
        if(startDate.compareTo(endDate) == 0) {
            return 0;
        }
        
        long startDateTime = startDate.getTime();
        long endDateTime = endDate.getTime();
        long milPerDay = 1000*60*60*24; 

        int numOfDays = (int) ((endDateTime - startDateTime) / milPerDay); // calculate vacation duration in days
        if(numOfDays > 367) {
            return numOfDays;
        }
                
        
        int count = 0;
        while (cal.getTime().before(endDate)) {
            cal.add(Calendar.DATE, 1);
            count++;
        }
        return count;

//        long startDateTime = startDate.getTime();
//        long endDateTime = endDate.getTime();
//        long milPerDay = 1000*60*60*24; 
//
//        int numOfDays = (int) ((endDateTime - startDateTime) / milPerDay); // calculate vacation duration in days
//
//        return numOfDays; // add one day to include start date in interval
    } 
    
    /**
     * 
     * @method calculateNumberOfWeeks
     * @description
     * @author User
     * @date Nov 26, 2015
     * @param
     * @return int
     */
    public static int calculateNumberOfWeeks(Date startDate, Date endDate) {
        
        int numOfDays = calculateNumberOfDaysBetween(startDate, endDate); 

        return (int) (numOfDays / 6); 
    }
    
    
    public static int calculateNumberOfMonths(Date startDate, Date endDate) {
        Calendar cal = Calendar.getInstance();
        if (startDate.before(endDate)) {
            cal.setTime(startDate);
        } else {
            cal.setTime(endDate);
            endDate = startDate;
        }
        int count = 0;
        while (cal.getTime().before(endDate)) {
            cal.add(Calendar.MONTH, 1);
            count++;
        }
        return count;
    }
    
     
    
    
    public static Date getEndDateOfWeek(Date fromDate) {
        return addDate(fromDate, 6);
    }
    
    /**
     * 
     * @method addDate
     * @description
     * @author dung.dd
     * @date Nov 26, 2015
     * @param
     * @return Date
     */
    public static Date addDate(Date fromDate, int numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);
        calendar.add(Calendar.DATE, numberOfDays);
        return calendar.getTime();
    }
    
    /**
     * 
     * @method addWeek
     * @description
     * @author dung.dd
     * @date Nov 26, 2015
     * @param
     * @return Date
     */
    public static Date addWeek(Date fromDate, int numberOfWeeks) {
        return addDate(fromDate, numberOfWeeks * 6);
    }
    
    
    /***
     * 
     * @method addMonth
     * @description
     * @author dung.dd
     * @date Nov 26, 2015
     * @param
     * @return Date
     */
    public static Date addMonth(Date fromDate, int numberOfMonths) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);
        calendar.add(Calendar.MONTH, numberOfMonths);
        return calendar.getTime();
    }
    
    public static Date addYear(Date fromDate, int numberOfYears) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);
        calendar.add(Calendar.YEAR, numberOfYears);
        return calendar.getTime();
    }
    
    
    
    /**
     * 
     * @method getDate
     * @description
     * @author dung.dd
     * @date Nov 26, 2015
     * @param
     * @return Date
     */
    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }
    
    /**
     * 
     * @method convertDateToCsvFormat
     * @description
     * @author User
     * @date Nov 29, 2015
     * @param
     * @return String
     */
    public static String convertDateToCsvFormat(Date date) {
        String result = "";
        
        if(date != null) {
            SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT_CSV);
            result = sf.format(date);
        }
        
        return result;
    }
    
}
