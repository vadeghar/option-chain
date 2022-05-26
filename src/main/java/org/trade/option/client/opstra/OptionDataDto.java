package org.trade.option.client.opstra;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OptionDataDto {
    @JsonProperty("Call Options OI")
    private Double callOptionsOI;
    @JsonProperty("CallDelta")
    private Double callDelta;
    @JsonProperty("CallTheta")
    private Double callTheta;
    @JsonProperty("CallVega")
    private Double callVega;
    @JsonProperty("CallsLTP")
    private Double callsLTP;
    @JsonProperty("ChangeCalls OI")
    private Double changeCallsOI;
    @JsonProperty("ChangePuts OI")
    private Double changePutsOI;
    @JsonProperty("MaxPain")
    private Double maxPain;
    @JsonProperty("PCR")
    private Double pcr;
    @JsonProperty("Put Options OI")
    private Double putOptionsOI;
    @JsonProperty("PutDelta")
    private Double putDelta;
    @JsonProperty("PutTheta")
    private Double putTheta;
    @JsonProperty("PutVega")
    private Double putVega;
    @JsonProperty("PutsLTP")
    private Double putsLTP;
    @JsonProperty("StrikeValues")
    private Integer strikeValues;
    @JsonProperty("Strikes")
    private Integer strikePrice;
    @JsonProperty("WeightedCallOI")
    private Double weightedCallOI;
    @JsonProperty("WeightedPutOI")
    private Double weightedPutOI;
    @JsonProperty("calloipercent")
    private Double calloipercent;
    @JsonProperty("putoipercent")
    private Double putoipercent;
}
