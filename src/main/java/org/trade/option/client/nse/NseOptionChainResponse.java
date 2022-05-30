package org.trade.option.client.nse;

import lombok.Data;

@Data
public class NseOptionChainResponse {
    private Records records;
    private FilteredRecords filtered;
    private Index index;
}
