package org.trade.option.service.iface;

import org.springframework.data.domain.Sort;
import org.trade.option.entity.OptionData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OptionDataService {
    public List<OptionData> findAll();
    public List<OptionData> findAll(String symbol, LocalDateTime updatedFrom, Sort sort);
//    public Map<Double, String> getSpotPricesBySymbolAndDate(String symbol, LocalDateTime updateFrom, Sort sort);
}
