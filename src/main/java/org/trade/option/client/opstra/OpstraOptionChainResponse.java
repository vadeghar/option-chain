package org.trade.option.client.opstra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpstraOptionChainResponse {
    List<OptionDataDto> data;
    private Double spotprice;
    private Integer spotstrike;
    private Integer lotsize;
}
