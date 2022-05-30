package org.trade.option.client.nse;

import lombok.Data;

@Data
public class Index {
    private String key;
    private String index;
    private String indexSymbol;
    private Double last;
    private Double variation;
    private Double percentChange;
    private Double open;
    private Double high;
    private Double low;
    private Double previousClose;
    private Double yearHigh;
    private Double yearLow;
    private String pe;
    private String pb;
    private String dy;
    private String declines;
    private String advances;
    private String unchanged;
}
