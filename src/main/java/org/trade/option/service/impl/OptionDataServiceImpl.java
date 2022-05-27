package org.trade.option.service.impl;

import org.springframework.stereotype.Service;
import org.trade.option.entity.OptionData;
import org.trade.option.repository.OptionDataRepository;
import org.trade.option.service.iface.OptionDataService;

import java.util.List;

@Service
public class OptionDataServiceImpl implements OptionDataService {
    private final OptionDataRepository optionDataRepository;

    public OptionDataServiceImpl(OptionDataRepository optionDataRepository) {
        this.optionDataRepository = optionDataRepository;
    }

    @Override
    public List<OptionData> findAll() {
        return optionDataRepository.findAll();
    }
}
