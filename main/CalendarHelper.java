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
            Calendar cal = Calendar.getInstance();
            Date curDate = convertLocalDateToDate(d);
            cal.setTime(curDate);
            cal.add(Calendar.MONTH, + nMonths);
            res = convertDateToLocalDate(cal.getTime());
        }

        return res;
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

        if (!(strDate == null)  && !strDate.isEmpty()&& strDate.isBlank()) {
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
        return parseDate(s);
    }


}
