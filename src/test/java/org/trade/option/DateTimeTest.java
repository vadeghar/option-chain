package org.trade.option;

import org.trade.option.utils.ExpiryUtils;

import java.time.LocalTime;
import java.time.ZoneId;

public class DateTimeTest {

    public static void main(String[] args) {
        System.out.println(LocalTime.now(ZoneId.of("Asia/Kolkata")).isBefore(LocalTime.parse(ExpiryUtils.START_TIME)));
        System.out.println(LocalTime.now(ZoneId.of("Asia/Kolkata")).isAfter(LocalTime.parse(ExpiryUtils.END_TIME )));
        if(LocalTime.now(ZoneId.of("Asia/Kolkata")).isBefore(LocalTime.parse(ExpiryUtils.START_TIME))
                || LocalTime.now(ZoneId.of("Asia/Kolkata")).isAfter(LocalTime.parse(ExpiryUtils.END_TIME )))
            return;

        if(LocalTime.now(ZoneId.of("Asia/Kolkata")).isAfter(LocalTime.parse(ExpiryUtils.START_TIME))
                && LocalTime.now(ZoneId.of("Asia/Kolkata")).isBefore(LocalTime.parse(ExpiryUtils.END_TIME))) {
            System.out.println(" In time ");
        } else {
            System.out.println(" Out of the time");
        }
    }
}
