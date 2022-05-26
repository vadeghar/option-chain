package org.trade.option.client.grow;

import lombok.Data;

@Data
public class OptionDataDto {
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double ltp;
    private Double dayChange;
    private Double dayChangePerc;
    private Double lowPriceRange;
    private Double highPriceRange;
    private Long volume;
    private Long totalBuyQty;
    private Long totalSellQty;
    private Long openInterest;
    private Long prevOpenInterest;
    private Integer lastTradeQty;
    private Long lastTradeTime;
    private String growwContractId;
    private String contractDisplayName;
    private String longDisplayName;
}
