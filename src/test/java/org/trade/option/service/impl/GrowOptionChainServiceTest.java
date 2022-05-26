package org.trade.option.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.trade.option.client.grow.OcSymbolEnum;
import org.trade.option.service.iface.OptionChainService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GrowOptionChainServiceTest {

    @Autowired
    @Qualifier("growOptionChainService")
    private OptionChainService optionChainService;
    @Test
    void saveOptionDataTest() {
        optionChainService.saveOptionData(OcSymbolEnum.BANK_NIFTY);
    }
}