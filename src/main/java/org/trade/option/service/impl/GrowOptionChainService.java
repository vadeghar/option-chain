package org.trade.option.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.trade.option.client.grow.*;
import org.trade.option.entity.OptionData;
import org.trade.option.repository.OptionDataRepository;
import org.trade.option.service.iface.OptionChainService;
import org.trade.option.utils.ExpiryUtils;
import org.trade.option.utils.OptionTypeEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("growOptionChainService")
@Slf4j
public class GrowOptionChainService implements OptionChainService {
    private final GrowClient client;
    private final OptionDataRepository optionDataRepository;
    private static final String OC_EXP_FORMAT = "yyyy-MM-dd";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(OC_EXP_FORMAT);

    private static final Integer lastNrecords = 3;
    public GrowOptionChainService(GrowClient client, OptionDataRepository optionDataRepository) {
        this.client = client;
        this.optionDataRepository = optionDataRepository;
    }
    @Override
    public void saveOptionData(OcSymbolEnum symbol) {
        log.info("Inside saveOptionData of OptionChainService");
        LocalDate currentExpiry = ExpiryUtils.getCurrentExpiry();
        log.info("OHLC Symbol: "+symbol.getOhlcSymbol());
        GrowOhlc ohlc = client.getOhlc(symbol.getOhlcSymbol());
        log.info("Current value for symbol "+symbol.getSymbol()+" is "+ohlc.getValue());
        Integer atmStrike = ExpiryUtils.getATM(ohlc.getValue());
        log.info("ATM strike price for "+ohlc.getValue()+" is "+atmStrike);

        String expiry = currentExpiry.format(DateTimeFormatter.ofPattern(OC_EXP_FORMAT));
        log.info("Current expiry is: "+expiry+" Symbol: "+symbol.getSymbol());
        GrowOptionChainResponse response = client.getOptionChain(symbol.getSymbol(), expiry);
        log.info("Received response size: "+response.getOptionChains().size());

        saveInRepository(atmStrike, response, ohlc.getValue(), symbol, currentExpiry);
    }
    private void saveInRepository(Integer atmStrike, GrowOptionChainResponse response, Double spotPrice, OcSymbolEnum symbol, LocalDate expiryDate) {
        Integer uptoStrikePrice = atmStrike + (lastNrecords * 100);
        List<OptionData> saveToDbList = new ArrayList<>();
        saveToDbList.addAll(response.getOptionChains().stream()
                .filter(opt -> (opt.getStrikePrice()/100 >= atmStrike && opt.getStrikePrice()/100 < uptoStrikePrice))
                .map(o -> prepareOptionData(o.getCallOption(), spotPrice, o.getStrikePrice()/100, expiryDate, OptionTypeEnum.CE))
                .collect(Collectors.toList()));
        Integer uptoStrikePriceDown = atmStrike - (lastNrecords * 100);
        saveToDbList.addAll(response.getOptionChains().stream()
                .filter(opt -> (opt.getStrikePrice()/100 <= atmStrike && opt.getStrikePrice()/100 > uptoStrikePriceDown))
                .map(o -> prepareOptionData(o.getPutOption(), spotPrice, o.getStrikePrice()/100, expiryDate, OptionTypeEnum.PE))
                .collect(Collectors.toList()));

        optionDataRepository.saveAll(saveToDbList);



    }
    private OptionData prepareOptionData(OptionDataDto o, Double spotPrice, Long strikePrice, LocalDate expiryDate, OptionTypeEnum typeEnum) {
        OptionData existingRecord = optionDataRepository.findByStrikePriceAndOptionType(strikePrice.intValue(), typeEnum.name());
        OptionData optionData = OptionData.builder()
                .symbol(o.getGrowwContractId())
                .oi(o.getOpenInterest())
                .curDate(LocalDate.now())
                .spotPrice(spotPrice)
                .updatedAt(LocalDateTime.now())
                .changeInOi(o.getOpenInterest()-o.getPrevOpenInterest())
                .strikePrice(strikePrice.intValue())
                .optionType(typeEnum.name())
                .expiry(expiryDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .sourceSystem("GROW")
                .build();
        return optionData;
    }
}
