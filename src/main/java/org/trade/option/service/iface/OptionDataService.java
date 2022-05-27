package org.trade.option.service.iface;

import org.trade.option.entity.OptionData;

import java.util.List;

public interface OptionDataService {
    public List<OptionData> findAll();
}
