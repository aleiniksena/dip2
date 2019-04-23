package main;

import javafx.scene.control.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class CalendarHelper {

    public static LocalDate addMonthsToDate(LocalDate d, int nMonths){
        LocalDate res = null;

        if (d != null){
            Calendar cal = getCalendar(d);
            cal.add(Calendar.MONTH, + nMonths);
            res = convertDateToLocalDate(cal.getTime());
        }

        return res;
    }

    public static LocalDate now(){
        return convertDateToLocalDate(new Date());
    }

    public static Calendar getCalendar(LocalDate d){
        Calendar cal = Calendar.getInstance();
        Date curDate = convertLocalDateToDate(d);
        cal.setTime(curDate);
        return cal;
    }

    /*
    * The year is leap when (1 or 2):
    * 1) year %4 == 0 and year % 100 != 0
    * 2) year % 400 == 0
    * */
    public static boolean isYearLeap(int year){

        boolean result = false;

        if (year % 400 == 0){
            result = true;
        } else if (year % 4 == 0){
            if (year % 100 != 00){
                result = true;
            }
        }
        return result;
    }

    public static int getDateDiffYears(LocalDate min, LocalDate max){
        int result = 0;

        if ((min != null) && (max != null)){
            if (min.compareTo(max) > 0){
                LocalDate tmp = max;
                max = min;
                min = tmp;
            }

            Calendar cMin = getCalendar(min);
            Calendar cMax = getCalendar(max);
            int minY = cMin.get(Calendar.YEAR);
            int maxY = cMax.get(Calendar.YEAR);
            int minD = cMin.get(Calendar.DAY_OF_YEAR);
            int maxD = cMax.get(Calendar.DAY_OF_YEAR);
            result = maxY - minY;
            boolean isMinLeap = CalendarHelper.isYearLeap(minY);
            boolean isMaxLeap = CalendarHelper.isYearLeap(maxY);

            if ((isMinLeap) && (!isMaxLeap) &&(minD == 366)) {
                minD = 365;
            }

            if (minD > maxD){
                result --;
            }
        }
        return result;
    }

    public static LocalDate convertDateToLocalDate(Date val){
        LocalDate lVal = null;

        if (val != null){
            lVal =  val.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        return lVal;
    }

    public static Date convertLocalDateToDate(LocalDate val){
        Date dval = null;

        if (val != null){
            dval = Date.from(val.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return dval;
    }

    public static String convertDateToString(LocalDate d){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.dateFormat);
        String res = (d == null ? "" : d.format(formatter));

        return res;
    }

    //Convert from string to date
    public static LocalDate parseDate(String strDate){
        LocalDate result = null;

        if (!(strDate == null)  && !strDate.isEmpty()&& !strDate.isBlank()) {
            try {
                Date dres = new SimpleDateFormat(Constants.dateFormat).parse(strDate);

                if (dres != null) {
                    result = dres.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //DatePicker functions
    public static void updateDateValue(DatePicker d, LocalDate val){
        d.setValue(val);
    }

    public static LocalDate getSelectedDate(DatePicker p){
        String s = p.getEditor().getText();
        LocalDate result = parseDate(s);
        System.out.println("Parsed selected date: source = " + s + " parsed into = " + result);
        return result;
    }


}
