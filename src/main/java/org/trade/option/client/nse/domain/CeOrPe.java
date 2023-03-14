package org.trade.option.client.nse.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CeOrPe {
    private int strikePrice;
    private String expiryDate;
    private String underlying;
    private String identifier;
    private double openInterest;
    private int changeinOpenInterest;
    private double pchangeinOpenInterest;
    private int totalTradedVolume;
    private double impliedVolatility;
    private double lastPrice;
    private double change;
    private double pChange;
    private int totalBuyQuantity;
    private int totalSellQuantity;
    private int bidQty;
    private double bidprice;
    private int askQty;
    private double askPrice;
    private double underlyingValue;
    private int totOI;
    private int totVol;
}
