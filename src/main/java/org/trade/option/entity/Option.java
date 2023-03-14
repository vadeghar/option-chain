package org.trade.option.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Option {
    private LocalDate expiryDate;
    private int strikePrice;
    private double openInterest;
    private int changeInOpenInterest;
    private double percChangeInOpenInterest;
    private int totalTradedVolume;
    private double impliedVolatility;
    private double lastPrice;
    private double change;
    private double percChange;
    private int totalBuyQuantity;
    private int totalSellQuantity;
}
