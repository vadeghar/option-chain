package org.trade.option.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Holidays {
    static List<LocalDate> holidays = new ArrayList<>();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    static {
        holidays.add(LocalDate.parse("26-Jan-2022", formatter));
        holidays.add(LocalDate.parse("01-Mar-2022", formatter));
        holidays.add(LocalDate.parse("18-Mar-2022", formatter));
        holidays.add(LocalDate.parse("01-Apr-2022", formatter));
        holidays.add(LocalDate.parse("14-Apr-2022", formatter));
        holidays.add(LocalDate.parse("15-Apr-2022", formatter));
        holidays.add(LocalDate.parse("03-May-2022", formatter));
        holidays.add(LocalDate.parse("16-May-2022", formatter));
        holidays.add(LocalDate.parse("09-Aug-2022", formatter));
        holidays.add(LocalDate.parse("15-Aug-2022", formatter));
        holidays.add(LocalDate.parse("16-Aug-2022", formatter));
        holidays.add(LocalDate.parse("31-Aug-2022", formatter));
        holidays.add(LocalDate.parse("05-Oct-2022", formatter));
        holidays.add(LocalDate.parse("24-Oct-2022", formatter));
        holidays.add(LocalDate.parse("26-Oct-2022", formatter));
        holidays.add(LocalDate.parse("08-Nov-2022", formatter));
    }
}
