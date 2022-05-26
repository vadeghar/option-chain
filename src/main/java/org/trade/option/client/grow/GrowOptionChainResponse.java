package org.trade.option.client.grow;

import lombok.Data;

import java.util.List;

@Data
public class GrowOptionChainResponse {
    List<GrowOptionChainDto> optionChains;
}
