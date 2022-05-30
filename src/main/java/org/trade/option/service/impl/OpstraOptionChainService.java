package org.trade.option.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.trade.option.client.grow.OcSymbolEnum;
import org.trade.option.client.opstra.OpstraClient;
import org.trade.option.client.opstra.OpstraOptionChainResponse;
import org.trade.option.client.opstra.OptionDataDto;
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
@Qualifier("opstraOptionChainService")
@Slf4j
public class OpstraOptionChainService implements OptionChainService {

    private final OpstraClient client;
    private final OptionDataRepository optionDataRepository;
    private static final String OPSTRA_EXP_FORMAT = "ddMMMYYYY";
    private static final DateTimeFormatter opstraExpFormatter = DateTimeFormatter.ofPattern(OPSTRA_EXP_FORMAT);

    private static final Integer lastNrecords = 3;

    public OpstraOptionChainService(OpstraClient client, OptionDataRepository optionDataRepository) {
        this.client = client;
        this.optionDataRepository = optionDataRepository;
    }

    @Override
    public void saveOptionData(OcSymbolEnum symbol) {
        log.info("Inside saveOptionData of OpstraOptionChainService");
        LocalDate currentExpiry = ExpiryUtils.getCurrentExpiry();
        log.info("Current value for symbol "+symbol.getOhlcSymbol());
        String expiry = currentExpiry.format(opstraExpFormatter);
        log.info("Current expiry is: "+expiry+" Symbol: "+symbol.getOhlcSymbol());
        OpstraOptionChainResponse response = null;
        try{
            response = client.getOptionChain(symbol.getOhlcSymbol(), expiry);
            log.info("ATM strike price for "+response.getSpotprice()+" is "+response.getSpotstrike());
            log.info("Received response size: "+response.getData().size());
            saveInRepository(response.getSpotstrike(), response, response.getSpotprice(), symbol, currentExpiry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveInRepository(Integer atmStrike, OpstraOptionChainResponse response, Double spotPrice, OcSymbolEnum symbol, LocalDate expiryDate) {
        List<OptionData> saveToDbList = new ArrayList<>();
        Integer depth = symbol == OcSymbolEnum.BANK_NIFTY ? 100 : 50;
        Integer maxStrikePrice = atmStrike + (lastNrecords * depth);
        Integer minStrikePrice = atmStrike - (lastNrecords * depth);
        saveToDbList.addAll(response.getData().stream()
                .filter(opt -> (opt.getStrikePrice() <= maxStrikePrice && opt.getStrikePrice() >= minStrikePrice ))
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
        Long changeInOi = typeEnum == OptionTypeEnum.CE ? o.getChangeCallsOI().longValue()/divisibleBy : o.getChangePutsOI().longValue()/divisibleBy;

        if(Objects.nonNull(existingData)) {
            log.info("Found existing record for strike: "+o.getStrikePrice()+" "+typeEnum.name()+" : "+existingData.getChangeInOi()+" Record ID: "+existingData.getId());
            log.info("ChangeInOi: "+changeInOi+", existing change in OI: "+existingData.getChangeInOi());
            log.info("Net change in oi: "+(changeInOi-existingData.getChangeInOi()));
            netChangeInOi = changeInOi -existingData.getChangeInOi();
        }
        OptionData optionData = OptionData.builder()
                .symbol(ExpiryUtils.getTradeSymbol(symbol.getOhlcSymbol(),o.getStrikePrice(),typeEnum))
                .oi(typeEnum == OptionTypeEnum.CE ? o.getCallOptionsOI().longValue()/divisibleBy : o.getPutOptionsOI().longValue()/divisibleBy)
                .curDate(LocalDate.now())
                .spotPrice(spotPrice)
                .updatedAt(LocalDateTime.now())
                .changeInOi(changeInOi)
                .optionType(typeEnum.name())
                .strikePrice(o.getStrikePrice())
                .netChangeInOi(netChangeInOi)
                .expiry(expiryDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .sourceSystem("OPSTRA")
                .build();
        return optionData;
    }
}
