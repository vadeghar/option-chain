package org.trade.option.client.nse.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionData {
    private Records records;
    private Filtered filtered;
}
