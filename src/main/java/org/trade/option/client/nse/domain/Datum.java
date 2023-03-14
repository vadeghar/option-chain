package org.trade.option.client.nse.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Datum {
    private int strikePrice;
    private String expiryDate;
    @JsonProperty("CE")
    private CeOrPe cE;
    @JsonProperty("PE")
    private CeOrPe pE;
}
