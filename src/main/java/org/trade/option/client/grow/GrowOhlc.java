package org.trade.option.client.grow;

import lombok.Data;

@Data
public class GrowOhlc {
    private String type;
    private String symbol;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double value;
    private Double dayChange;
    private Double dayChangePerc;
    private Long tsInMillis;
}
