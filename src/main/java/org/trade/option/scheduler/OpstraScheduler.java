package org.trade.option.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.trade.option.annotations.TrackExecutionTime;
import org.trade.option.client.grow.OcSymbolEnum;
import org.trade.option.service.impl.OpstraOptionChainService;

@Service
@Slf4j
public class OpstraScheduler {

    @Autowired
    OpstraOptionChainService optionChainService;

    @TrackExecutionTime
    @Scheduled(fixedDelayString = "PT07M")
    public void runNifty() {
        log.info("Inside runNifty method in NiftyScheduler");
        optionChainService.saveOptionData(OcSymbolEnum.NIFTY);
        log.info("Completed runNifty method in NiftyScheduler");
    }

    @TrackExecutionTime
    @Scheduled(initialDelayString = "PT01M", fixedDelayString = "PT07M")
    public void runBnf() {
        log.info("Inside runBnf method in NiftyScheduler");
        optionChainService.saveOptionData(OcSymbolEnum.BANK_NIFTY);
        log.info("Completed runBnf method in NiftyScheduler");
    }
}
