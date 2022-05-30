package org.trade.option.client.nse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class FilteredRecords {
    private List<OptionDataDto> data;
    @JsonProperty("CE")
    TotalOi ce;
    @JsonProperty("PE")
    TotalOi pe;
    @Data
    public static class TotalOi {
        private Long totOI;
        private Long totVol;
    }
}
