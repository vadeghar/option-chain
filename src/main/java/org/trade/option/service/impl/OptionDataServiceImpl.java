package org.trade.option.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.trade.option.entity.OptionData;
import org.trade.option.repository.OptionDataRepository;
import org.trade.option.service.iface.OptionDataService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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

    @Override
    public List<OptionData> findAll(String symbol, LocalDateTime updatedFrom, Sort sort) {
        return optionDataRepository.findAll(symbol, updatedFrom, sort);
    }

//    @Override
//    public Map<Double, String> getSpotPricesBySymbolAndDate(String symbol, LocalDateTime updateFrom, Sort sort) {
//        List<Object[]> data = optionDataRepository.getSpotPricesBySymbolAndDate(symbol, updateFrom, sort);
//        SortedMap<Double, String> sm  = new TreeMap<Double, String>();
//        for(Object[] arr: data) {
//            sm.put(Double.valueOf(arr[0].toString()), String.valueOf(arr[1]));
//        }
////        Map<Object, Object[]> collect = data.stream().collect(Collectors.toMap(arr -> arr[0], Function.identity()));
//        return sm;
//    }
}
