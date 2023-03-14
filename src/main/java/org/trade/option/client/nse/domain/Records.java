package org.trade.option.client.nse.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Records {
    private ArrayList<String> expiryDates;
    private String timestamp;
    private double underlyingValue;
    private ArrayList<Integer> strikePrices;
}
