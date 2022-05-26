package org.trade.option.test;

import org.trade.option.utils.OptionTypeEnum;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class Example {
    public static void main(String[] args) {
        System.out.println("Expiry Day: "+getSymbol("BANKNIFTY", 35600, OptionTypeEnum.PE));
    }
    private static final String LAST_EXP_FORMAT = "ddMMM";
    private static final String EXP_FORMAT = "YYMdd";
    //BANKNIFTY, 35600, OptionTypeEnum.CE
    public static String getSymbol(String stockName, Integer strikePrice, OptionTypeEnum optionType) {
        LocalDate expiryDay = getCurrentExpiry();
        String symbol = "";
        if(isMonthlyExpiry(expiryDay)) {
            symbol = stockName+expiryDay.format(DateTimeFormatter.ofPattern(LAST_EXP_FORMAT))+strikePrice+optionType.name();
        }
            symbol = stockName+expiryDay.format(DateTimeFormatter.ofPattern(EXP_FORMAT))+strikePrice+optionType.name();

        return symbol.toUpperCase();
    }

    private static LocalDate getCurrentExpiry() {
        if(LocalDate.now().getDayOfWeek().equals(DayOfWeek.THURSDAY))
            return LocalDate.now();
        return LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
    }
    public static boolean isMonthlyExpiry(LocalDate curExpiryDay) {
        LocalDate lastThursdayInMonth = LocalDate.now().with(TemporalAdjusters.lastInMonth(DayOfWeek.THURSDAY));
        return (curExpiryDay.isEqual(lastThursdayInMonth));
    }
}
