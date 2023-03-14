package org.trade.option.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.trade.option.client.grow.OcSymbolEnum;
import org.trade.option.client.nse.NseClient;
import org.trade.option.client.nse.domain.CeOrPe;
import org.trade.option.client.nse.domain.OptionData;
import org.trade.option.entity.Option;
import org.trade.option.entity.OptionEntity;
import org.trade.option.repository.mongo.OptionEntityRepository;
import org.trade.option.service.iface.OptionChainService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Qualifier("nseOptionChainService")
@Slf4j
public class NseOptionChainService implements OptionChainService {

    private final NseClient client;
    private final OptionEntityRepository optionEntityRepository;
//    private final SpotPriceService spotPriceService;
//    private final BankNiftyService bankNiftyService;
//    private final NiftyService niftyService;

    private static final Integer lastNrecords = 5;

    DateTimeFormatter timestampFormatter =
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("dd-MMM-yyyy HH:mm:ss")
                    .toFormatter()
                    .withZone(ZoneId.of("Asia/Kolkata"));
    DateTimeFormatter dateFormatter =
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("dd-MMM-yyyy")
                    .toFormatter()
                    .withZone(ZoneId.of("Asia/Kolkata"));

    public NseOptionChainService(NseClient client, OptionEntityRepository optionEntityRepository) {
        this.client = client;
        this.optionEntityRepository = optionEntityRepository;
    }

    @Override
    public void saveOptionData(OcSymbolEnum symbol) {
        log.info("Inside saveOptionData of NseOptionChainService");
        OptionData response;
        try{
            response = client.getOptionChainData(symbol.getOhlcSymbol());
            if(response == null) {
                log.error("******************* There is an Error, Response is null");
                return;
            }
            log.info("Last updated at NSE: "+response.getRecords().getTimestamp());
            OptionEntity optionEntity = buildOptionEntity(response);
            optionEntityRepository.save(optionEntity);
            log.info("Save completed: "+LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in NSE Option Chain Service: "+e.getMessage());
        }
    }

    private OptionEntity buildOptionEntity(OptionData optionData) {
        List<Option> ceOptionList = optionData.getFiltered().getData()
                .stream()
                .map(oc -> buildOption(oc.getCE()))
                .collect(Collectors.toList());
        List<Option> peOptionList = optionData.getFiltered().getData()
                .stream()
                .map(oc -> buildOption(oc.getPE()))
                .collect(Collectors.toList());

        return OptionEntity.builder()
                .id(UUID.randomUUID())
                .symbol(optionData.getFiltered().getData().get(0).getPE().getUnderlying())
                .underlyingValue(optionData.getRecords().getUnderlyingValue())
                .timestamp(LocalDateTime.parse(optionData.getRecords().getTimestamp(), timestampFormatter))
                .totCeOI(optionData.getFiltered().getCE().getTotOI())
                .totCeVol(optionData.getFiltered().getCE().getTotVol())
                .totPeOI(optionData.getFiltered().getPE().getTotOI())
                .totPeVol(optionData.getFiltered().getPE().getTotVol())
                .peOptionList(peOptionList)
                .ceOptionList(ceOptionList)
                .lastUpdatedTs(LocalDateTime.now())
                .build();
    }

    private Option buildOption(CeOrPe ceOrPe) {
        return Option.builder()
                .expiryDate(LocalDate.parse(ceOrPe.getExpiryDate(), dateFormatter))
                .change(ceOrPe.getChange())
                .changeInOpenInterest(ceOrPe.getChangeinOpenInterest())
                .impliedVolatility(ceOrPe.getImpliedVolatility())
                .openInterest(ceOrPe.getOpenInterest())
                .lastPrice(ceOrPe.getLastPrice())
                .percChangeInOpenInterest(ceOrPe.getPchangeinOpenInterest())
                .percChange(ceOrPe.getPChange())
                .totalBuyQuantity(ceOrPe.getTotalBuyQuantity())
                .totalSellQuantity(ceOrPe.getTotalSellQuantity())
                .totalTradedVolume(ceOrPe.getTotalTradedVolume())
                .strikePrice(ceOrPe.getStrikePrice())
                .build();
    }

//    private void saveInRepository(Integer atmStrike, FilteredRecords response, Double spotPrice, OcSymbolEnum symbol, String updatedAtSource) {
//        log.info("Inside saveInRepository of NseOptionChainService");
//        SpotPrice lastInserted = spotPriceService.getLastInserted(symbol.getOhlcSymbol());
//        if(lastInserted != null && lastInserted.getUpdatedAtSource().equals(updatedAtSource)) {
//            log.info("Received same data from NSE, Not saving into repository");
//            return;
//        }
//        SpotPrice sp = SpotPrice.builder()
//                        .lastPrice(spotPrice)
//                        .updatedAtSource(updatedAtSource)
//                        .symbol(symbol.getOhlcSymbol())
//                        .build();
//        spotPriceService.save(sp);
//        log.info("SpotPrice save - Completed");
//        Integer depth = symbol == OcSymbolEnum.BANK_NIFTY ? 100 : 50;
//        Integer maxStrikePrice = atmStrike + (lastNrecords * depth);
//        Integer minStrikePrice = atmStrike - (lastNrecords * depth);
//        if(symbol == OcSymbolEnum.BANK_NIFTY) {
//            log.info("BankNifty save - Started");
//            List<BankNifty> bankNiftyList = new ArrayList<>();
//            for(OptionDataDto bnf: response.getData()) {
//                if (bnf.getStrikePrice() >= minStrikePrice && bnf.getStrikePrice() <= maxStrikePrice) {
//                    bankNiftyList.addAll(mapToBankNifty(bnf, updatedAtSource));
//                }
//            }
//            bankNiftyService.saveAll(bankNiftyList);
//            log.info("BankNifty save - Completed");
//        } else if(symbol == OcSymbolEnum.NIFTY) {
//            log.info("Nifty save - Started");
//            List<Nifty> niftyList = new ArrayList<>();
//            for(OptionDataDto nf: response.getData()) {
//                if (nf.getStrikePrice() >= minStrikePrice && nf.getStrikePrice() <= maxStrikePrice) {
//                    niftyList.addAll(mapToNifty(nf, updatedAtSource));
//                }
//            }
//            niftyService.saveAll(niftyList);
//            log.info("Nifty save - Completed");
//        }
//    }
//
//    private List<BankNifty> mapToBankNifty(OptionDataDto bnf, String updatedAtSource) {
//        BankNifty lastInsertedCe = bankNiftyService.getLastInserted(bnf.getStrikePrice(), "CE", bnf.getExpiryDate());
//        List<BankNifty> cePe = new ArrayList<>();
//        if(lastInsertedCe != null && lastInsertedCe.getUpdatedAtSource().equals(updatedAtSource))
//            return cePe;
//        cePe.add(BankNifty.builder()
//                .strikePrice(bnf.getStrikePrice())
//                .lastPrice(bnf.getCe().getLastPrice())
//                .source("NSE")
//                .optionType("CE")
//                .changeInOi(bnf.getCe().getChangeinOpenInterest().longValue())
//                .totalOi(bnf.getCe().getOpenInterest().longValue())
//                .expiry(bnf.getExpiryDate())
//                .updatedAtSource(updatedAtSource)
//                .curChangeInOi(lastInsertedCe != null ? (bnf.getCe().getChangeinOpenInterest().longValue() - lastInsertedCe.getChangeInOi()) : 0)
//                .build()
//        );
//        BankNifty lastInsertedPe = bankNiftyService.getLastInserted(bnf.getStrikePrice(), "PE", bnf.getExpiryDate());
//        cePe.add(BankNifty.builder()
//                .strikePrice(bnf.getStrikePrice())
//                .lastPrice(bnf.getPe().getLastPrice())
//                .source("NSE")
//                .optionType("PE")
//                .changeInOi(bnf.getPe().getChangeinOpenInterest().longValue())
//                .totalOi(bnf.getPe().getOpenInterest().longValue())
//                .expiry(bnf.getExpiryDate())
//                .updatedAtSource(updatedAtSource)
//                .curChangeInOi(lastInsertedPe != null ? (bnf.getPe().getChangeinOpenInterest().longValue() - lastInsertedPe.getChangeInOi()) : 0)
//                .build()
//        );
//
//        return cePe;
//    }
//
//    private List<Nifty> mapToNifty(OptionDataDto bnf, String updatedAtSource) {
//        Nifty lastInsertedCe = niftyService.getLastInserted(bnf.getStrikePrice(), "CE", bnf.getExpiryDate());
//        List<Nifty> cePe = new ArrayList<>();
//        if(lastInsertedCe != null && lastInsertedCe.getUpdatedAtSource().equals(updatedAtSource))
//            return cePe;
//        cePe.add(Nifty.builder()
//                .strikePrice(bnf.getStrikePrice())
//                .lastPrice(bnf.getCe().getLastPrice())
//                .source("NSE")
//                .optionType("CE")
//                .changeInOi(bnf.getCe().getChangeinOpenInterest().longValue())
//                .totalOi(bnf.getCe().getOpenInterest().longValue())
//                .expiry(bnf.getExpiryDate())
//                .updatedAtSource(updatedAtSource)
//                .curChangeInOi(lastInsertedCe != null ? (bnf.getCe().getChangeinOpenInterest().longValue() - lastInsertedCe.getChangeInOi()) : 0)
//                .build()
//        );
//        Nifty lastInsertedPe = niftyService.getLastInserted(bnf.getStrikePrice(), "PE", bnf.getExpiryDate());
//        cePe.add(Nifty.builder()
//                .strikePrice(bnf.getStrikePrice())
//                .lastPrice(bnf.getPe().getLastPrice())
//                .source("NSE")
//                .optionType("PE")
//                .changeInOi(bnf.getPe().getChangeinOpenInterest().longValue())
//                .totalOi(bnf.getPe().getOpenInterest().longValue())
//                .expiry(bnf.getExpiryDate())
//                .updatedAtSource(updatedAtSource)
//                .curChangeInOi(lastInsertedPe != null ? (bnf.getPe().getChangeinOpenInterest().longValue() - lastInsertedPe.getChangeInOi()) : 0)
//                .build()
//        );
//
//        return cePe;
//    }

}
