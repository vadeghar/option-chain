package org.trade.option.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.trade.option.annotations.TrackExecutionTime;
import org.trade.option.client.grow.OcSymbolEnum;
import org.trade.option.service.iface.OptionChainService;
import org.trade.option.utils.ExpiryUtils;

import java.time.LocalTime;
import java.time.ZoneId;

@Service
@Slf4j
public class NseScheduler {

    @Autowired
    @Qualifier("nseOptionChainService")
    OptionChainService optionChainService;

    @TrackExecutionTime
    @Scheduled(fixedDelayString = "PT2M")
    public void runNifty() {
        if(LocalTime.now(ZoneId.of("Asia/Kolkata")).isBefore(LocalTime.parse(ExpiryUtils.START_TIME))
                || LocalTime.now(ZoneId.of("Asia/Kolkata")).isAfter(LocalTime.parse(ExpiryUtils.END_TIME )))
            return;
        log.info("Inside runNifty method in NiftyScheduler");
        optionChainService.saveOptionData(OcSymbolEnum.NIFTY);
        log.info("Completed runNifty method in NiftyScheduler");
    }

    @TrackExecutionTime
    @Scheduled(fixedDelayString = "PT150S", initialDelay = 5000)
    public void runBankNifty() {
        if(LocalTime.now(ZoneId.of("Asia/Kolkata")).isBefore(LocalTime.parse(ExpiryUtils.START_TIME))
                || LocalTime.now(ZoneId.of("Asia/Kolkata")).isAfter(LocalTime.parse(ExpiryUtils.END_TIME )))
            return;
        log.info("Inside runBankNifty method in NiftyScheduler");
        optionChainService.saveOptionData(OcSymbolEnum.BANK_NIFTY);
        log.info("Completed runBankNifty method in NiftyScheduler");
    }

}
