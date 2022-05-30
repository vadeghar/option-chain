package org.trade.option.client.nse;

import lombok.Data;

import java.util.List;
@Data
public class Records {
    private List<String> expiryDates;
    private List<OptionDataDto> data;
    private String timestamp;
    private Double underlyingValue;
}
