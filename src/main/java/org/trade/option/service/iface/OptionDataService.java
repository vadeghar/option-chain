package org.trade.option.service.iface;

import org.springframework.data.domain.Sort;
import org.trade.option.entity.OptionData;

import java.time.LocalDateTime;
import java.util.List;

public interface OptionDataService {
    public List<OptionData> findAll();
    public List<OptionData> findAll(String symbol, LocalDateTime updatedFrom, Sort sort);
}
