package org.trade.option.client.nse.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filtered {
    private ArrayList<Datum> data;
    @JsonProperty("CE")
    private CeOrPe cE;
    @JsonProperty("PE")
    public CeOrPe pE;
}
