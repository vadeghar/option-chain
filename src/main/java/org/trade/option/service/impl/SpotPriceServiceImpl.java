package org.trade.option.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.trade.option.entity.SpotPrice;
import org.trade.option.repository.SpotPriceRepository;
import org.trade.option.service.iface.SpotPriceService;

import java.util.List;

@Service
public class SpotPriceServiceImpl implements SpotPriceService {
    private final SpotPriceRepository spotPriceRepository;

    public SpotPriceServiceImpl(SpotPriceRepository spotPriceRepository) {
        this.spotPriceRepository = spotPriceRepository;
    }

    @Override
    public SpotPrice save(SpotPrice spotPrice) {
        return spotPriceRepository.save(spotPrice);
    }

    @Override
    public void saveAll(List<SpotPrice> spotPriceList) {
        spotPriceRepository.saveAll(spotPriceList);
    }

    @Override
    public SpotPrice getLastInserted(String symbol) {
        return spotPriceRepository.findTopBySymbolOrderByIdDesc(symbol);
    }

    public List<SpotPrice> getSpotPriceBySymbol(String symbol, String date, Sort sort) {
        return spotPriceRepository.getSpotPriceBySymbol(symbol, date, sort);
    }

}
