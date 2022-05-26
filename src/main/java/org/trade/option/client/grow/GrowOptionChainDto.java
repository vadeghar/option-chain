package org.trade.option.client.grow;

import lombok.Data;

@Data
public class GrowOptionChainDto {
    private Long strikePrice;
    private OptionDataDto callOption;
    private OptionDataDto putOption;
}
