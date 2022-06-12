package org.trade.option.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.trade.option.service.iface.OptionChainService;

@Service
@Slf4j
public class OpstraScheduler {

    @Autowired
    @Qualifier("opstraOptionChainService")
    OptionChainService optionChainService;

//    @TrackExecutionTime
//    @Scheduled(fixedDelayString = "PT07M")
//    public void runNifty() {
//        if(LocalTime.now().isBefore(LocalTime.parse(ExpiryUtils.START_TIME))
//            || LocalTime.now().isAfter(LocalTime.parse(ExpiryUtils.END_TIME )))
//            return;
//        log.info("Inside runNifty method in NiftyScheduler");
//        optionChainService.saveOptionData(OcSymbolEnum.NIFTY);
//        log.info("Completed runNifty method in NiftyScheduler");
//    }
//
//    @TrackExecutionTime
//    @Scheduled(initialDelayString = "PT01M", fixedDelayString = "PT07M")
//    public void runBnf() {
//        if(LocalTime.now().isBefore(LocalTime.parse(ExpiryUtils.START_TIME))
//                || LocalTime.now().isAfter(LocalTime.parse(ExpiryUtils.END_TIME )))
//            return;
//        log.info("Inside runBnf method in NiftyScheduler");
//        optionChainService.saveOptionData(OcSymbolEnum.BANK_NIFTY);
//        log.info("Completed runBnf method in NiftyScheduler");
//    }
}
