package org.trade.option.client.nse;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.trade.option.client.grow.OcSymbolEnum;
import org.trade.option.client.nse.domain.CeOrPe;
import org.trade.option.client.nse.domain.OptionData;
import org.trade.option.entity.Option;
import org.trade.option.entity.OptionEntity;
import org.trade.option.repository.mongo.OptionEntityRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class NseClientTest {

    @Autowired
    NseClient nseClient;

    @Autowired
    OptionEntityRepository optionEntityRepository;
    DateTimeFormatter timestampFormatter =
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("dd-MMM-yyyy HH:mm:ss")
                    .toFormatter();
    DateTimeFormatter dateFormatter =
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("dd-MMM-yyyy")
                    .toFormatter();
    @Test
    public void getOptionChainTest() throws Exception {
        OptionData optionChain = nseClient.getOptionChainData(OcSymbolEnum.NIFTY.getOhlcSymbol());
        System.out.println("******* optionChain response: "+optionChain.getFiltered().getData().size());

        List<Option> ceOptionList = optionChain.getFiltered().getData()
                .stream()
                .map(oc -> buildOption(oc.getCE()))
                .collect(Collectors.toList());
        List<Option> peOptionList = optionChain.getFiltered().getData()
                .stream()
                .map(oc -> buildOption(oc.getPE()))
                .collect(Collectors.toList());

        OptionEntity optionEntity = OptionEntity.builder()
                .id(UUID.randomUUID())
                .symbol(optionChain.getFiltered().getData().get(0).getPE().getUnderlying())
                .underlyingValue(optionChain.getRecords().getUnderlyingValue())
                .timestamp(LocalDateTime.parse(optionChain.getRecords().getTimestamp(), timestampFormatter))
                .totCeOI(optionChain.getFiltered().getCE().getTotOI())
                .totCeVol(optionChain.getFiltered().getCE().getTotVol())
                .totPeOI(optionChain.getFiltered().getPE().getTotOI())
                .totPeVol(optionChain.getFiltered().getPE().getTotVol())
                .peOptionList(peOptionList)
                .ceOptionList(ceOptionList)
                        .build();
        log.info("\nSymbol: "+optionEntity.getSymbol()+"\nLTP: "+optionEntity.getUnderlyingValue()+
                "\nUpdated time: "+optionEntity.getTimestamp()+"\nTotal CE OI: "+optionEntity.getTotCeOI()+
                "\nTotal PE OI: "+optionEntity.getTotPeOI()+"\nCE Count: "+optionEntity.getCeOptionList().size()+
                "\nPE Count: "+optionEntity.getPeOptionList().size());
        optionEntityRepository.save(optionEntity);

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

//    @Test
//    public void getNseCookieTest() throws JsonProcessingException {
//        String cookie = nseClient.getNseCookie();
//        System.out.println("************ "+cookie);
//    }

}
