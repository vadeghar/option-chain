package org.trade.option.utils;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class ExpiryUtils {
    private static final String LAST_EXP_FORMAT = "ddMMM";
    private static final String EXP_FORMAT = "YYMdd";

    public static String START_TIME="09:00";
    public static String END_TIME="15:35";
    public static String getTradeSymbol(String stockName, Integer strikePrice, OptionTypeEnum optionType) {
        LocalDate expiryDay = getCurrentExpiry();
        if(Holidays.holidays.contains(expiryDay)) {
            expiryDay = expiryDay.plusDays(1);
        }
        String symbol = "";
        if(isMonthlyExpiry(expiryDay)) {
            symbol = stockName+expiryDay.format(DateTimeFormatter.ofPattern(LAST_EXP_FORMAT))+strikePrice+optionType.name();
        } else {
            symbol = stockName+expiryDay.format(DateTimeFormatter.ofPattern(EXP_FORMAT))+strikePrice+optionType.name();
        }
        return symbol.toUpperCase();
    }

    public static Integer getATM(Double ltp) { // 35613
        Integer atm = 0;
        Integer digitCount = 0;
        Integer divisibleBy = 0;
        if(ltp.toString().contains(".")) {
            String lp = ltp.toString().substring(0, ltp.toString().indexOf("."));
            digitCount = lp.toCharArray().length;
            System.out.println("No. of digits at left side: "+digitCount);
        }
        if(digitCount.equals(5))
            divisibleBy = 100;
        if(digitCount.equals(4))
            divisibleBy = 10;
        Integer i = Double.valueOf(ltp/divisibleBy).intValue(); // 356
        Double r = ltp%divisibleBy; //35613%100
        atm =  i * divisibleBy; // 356 * 100 = 35600
        if(digitCount.equals(5) && r > 50) {
            atm = atm + 100;
        }
        if(digitCount.equals(4) && r > 5) {
            atm = atm + 10;
        }
        System.out.println("Final ATM: "+atm);
        return atm;
    }

    public static LocalDate getCurrentExpiry() {
        if(LocalDate.now().getDayOfWeek().equals(DayOfWeek.THURSDAY))
            return LocalDate.now();
        return LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
    }
    public static boolean isMonthlyExpiry(LocalDate curExpiryDay) {
        LocalDate lastThursdayInMonth = LocalDate.now().with(TemporalAdjusters.lastInMonth(DayOfWeek.THURSDAY));
        return (curExpiryDay.isEqual(lastThursdayInMonth) || curExpiryDay.isAfter(lastThursdayInMonth));
    }
}
