package org.trade.option.client.nse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OptionDataDto {

    private Integer strikePrice;
    private String expiryDate;
    @JsonProperty("PE")
    OptionData pe;
    @JsonProperty("CE")
    OptionData ce;

    @Data
    public static class OptionData {
        private float strikePrice;
        private String expiryDate;
        private String underlying;
        private String identifier;
        private Integer openInterest;
        private Integer changeinOpenInterest;
        private Double pchangeinOpenInterest;
        private Integer totalTradedVolume;
        private Double impliedVolatility;
        private Double lastPrice;
        private Double change;
        private Double pChange;
        private Integer totalBuyQuantity;
        private Integer totalSellQuantity;
        private Integer bidQty;
        private Integer bidprice;
        private Integer askQty;
        private Double askPrice;
        private Double underlyingValue;
    }

}
