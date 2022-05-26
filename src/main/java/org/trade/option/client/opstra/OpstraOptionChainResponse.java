package org.trade.option.client.opstra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
