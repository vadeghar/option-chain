package org.trade.option.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.trade.option.annotations.TrackExecutionTime;
import org.trade.option.client.grow.OcSymbolEnum;
import org.trade.option.service.impl.OpstraOptionChainService;

import java.time.LocalTime;

@Service
@Slf4j
public class OpstraScheduler {

    @Autowired
    OpstraOptionChainService optionChainService;

    @TrackExecutionTime
    @Scheduled(fixedDelayString = "PT07M")
    public void runNifty() {
        if(LocalTime.now().isBefore(LocalTime.parse( "09:15" ))
            || LocalTime.now().isAfter(LocalTime.parse( "15:30" )))
            return;
        log.info("Inside runNifty method in NiftyScheduler");
        optionChainService.saveOptionData(OcSymbolEnum.NIFTY);
        log.info("Completed runNifty method in NiftyScheduler");
    }

    @TrackExecutionTime
    @Scheduled(initialDelayString = "PT01M", fixedDelayString = "PT07M")
    public void runBnf() {
        if(LocalTime.now().isBefore(LocalTime.parse( "09:15" ))
                || LocalTime.now().isAfter(LocalTime.parse( "15:30" )))
            return;
        log.info("Inside runBnf method in NiftyScheduler");
        optionChainService.saveOptionData(OcSymbolEnum.BANK_NIFTY);
        log.info("Completed runBnf method in NiftyScheduler");
    }
}
