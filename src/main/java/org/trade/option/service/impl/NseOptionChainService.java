package org.trade.option.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.trade.option.client.grow.OcSymbolEnum;
import org.trade.option.client.nse.FilteredRecords;
import org.trade.option.client.nse.NseClient;
import org.trade.option.client.nse.NseOptionChainResponse;
import org.trade.option.client.nse.OptionDataDto;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Qualifier("nseOptionChainService")
@Slf4j
public class NseOptionChainService implements OptionChainService {

    private final NseClient client;
    private final OptionDataRepository optionDataRepository;
    private static final String OPSTRA_EXP_FORMAT = "ddMMMYYYY";
    private static final DateTimeFormatter opstraExpFormatter = DateTimeFormatter.ofPattern(OPSTRA_EXP_FORMAT);

    private static final Integer lastNrecords = 3;

    public NseOptionChainService(NseClient client, OptionDataRepository optionDataRepository) {
        this.client = client;
        this.optionDataRepository = optionDataRepository;
    }

    @Override
    public void saveOptionData(OcSymbolEnum symbol) {
        log.info("Inside saveOptionData of NseOptionChainService");
        LocalDate currentExpiry = ExpiryUtils.getCurrentExpiry();
        log.info("Current value for symbol "+symbol.getOhlcSymbol());
        String expiry = currentExpiry.format(opstraExpFormatter);
        log.info("Current expiry is: "+expiry+" Symbol: "+symbol.getOhlcSymbol());
        NseOptionChainResponse response = null;
        try{
            response = client.getOptionChain(symbol.getOhlcSymbol());
            log.info("Last updated at NSE: "+response.getRecords().getTimestamp());
            log.info("Index: "+symbol.getOhlcSymbol()+" CE total OI: "+response.getFiltered().getCe().getTotOI());
            log.info("Index: "+symbol.getOhlcSymbol()+" PE total OI: "+response.getFiltered().getPe().getTotOI());
            saveInRepository(ExpiryUtils.getATM(response.getRecords().getUnderlyingValue()), response.getFiltered(), response.getRecords().getUnderlyingValue(), symbol, currentExpiry);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in NSE Option Chain Service: "+e.getMessage());
        }
    }

    private void saveInRepository(Integer atmStrike, FilteredRecords response, Double spotPrice, OcSymbolEnum symbol, LocalDate expiryDate) {
        List<OptionData> saveToDbList = new ArrayList<>();
        Integer depth = symbol == OcSymbolEnum.BANK_NIFTY ? 100 : 50;
        Integer maxStrikePrice = atmStrike + (lastNrecords * depth);
        Integer minStrikePrice = atmStrike - (lastNrecords * depth);

        saveToDbList.addAll(response.getData().stream()
                .filter(opt -> (opt.getStrikePrice() <= maxStrikePrice && opt.getStrikePrice() >= minStrikePrice))
                .map(o -> prepareOptionData(o, spotPrice, symbol, expiryDate, OptionTypeEnum.CE))
                .collect(Collectors.toList()));

        saveToDbList.addAll(response.getData().stream()
                .filter(opt -> (opt.getStrikePrice() <= maxStrikePrice && opt.getStrikePrice() >= minStrikePrice ))
                .map(o -> prepareOptionData(o, spotPrice, symbol, expiryDate, OptionTypeEnum.PE))
                .collect(Collectors.toList()));
        optionDataRepository.saveAll(saveToDbList);

    }

    private OptionData prepareOptionData(OptionDataDto o, Double spotPrice, OcSymbolEnum symbol, LocalDate expiryDate, OptionTypeEnum typeEnum) {
        Integer divisibleBy = symbol.getOhlcSymbol().equals("BANKNIFTY") ? 25 : 50;
        OptionData existingData = optionDataRepository.findFirstByStrikePriceAndOptionTypeOrderByIdDesc(o.getStrikePrice(), typeEnum.name());
        Long netChangeInOi = Long.valueOf(0);
        Integer changeInOi = typeEnum == OptionTypeEnum.CE ? o.getCe().getChangeinOpenInterest() : o.getPe().getChangeinOpenInterest();

        if(Objects.nonNull(existingData)) {
            log.info("Found existing record for strike: "+o.getStrikePrice()+" "+typeEnum.name()+" : "+existingData.getChangeInOi()+" Record ID: "+existingData.getId());
            log.info("ChangeInOi: "+changeInOi+", existing change in OI: "+existingData.getChangeInOi());
            log.info("Net change in oi: "+(changeInOi-existingData.getChangeInOi()));
            netChangeInOi = changeInOi -existingData.getChangeInOi();
        }
        OptionData optionData = OptionData.builder()
                .symbol(ExpiryUtils.getTradeSymbol(symbol.getOhlcSymbol(),o.getStrikePrice(),typeEnum))
                .oi(Long.valueOf(typeEnum == OptionTypeEnum.CE ? o.getCe().getOpenInterest() : o.getPe().getOpenInterest()))
                .curDate(LocalDate.now())
                .spotPrice(spotPrice)
                .updatedAt(LocalDateTime.now())
                .changeInOi(Long.valueOf(changeInOi))
                .optionType(typeEnum.name())
                .strikePrice(o.getStrikePrice())
                .netChangeInOi(netChangeInOi)
                .expiry(expiryDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .sourceSystem("NSE")
                .build();
        return optionData;
    }
}
