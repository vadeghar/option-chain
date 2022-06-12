package org.trade.option.client.opstra;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.trade.option.client.grow.OcSymbolEnum;
import org.trade.option.utils.ExpiryUtils;

import java.time.format.DateTimeFormatter;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpstraClientTest {
    @LocalServerPort
    private int port;

    private static final String OPSTRA_EXP_FORMAT = "ddMMMYYYY";
    private static final DateTimeFormatter opstraExpFormatter = DateTimeFormatter.ofPattern(OPSTRA_EXP_FORMAT);
    @Autowired
    OpstraClient client;

    @Test
    public void getOptionChainTest() throws Exception{
        String expiry = ExpiryUtils.getCurrentExpiry().format(opstraExpFormatter);
        OpstraOptionChainResponse response = client.getOptionChain(OcSymbolEnum.BANK_NIFTY.getOhlcSymbol(), expiry);
        System.out.println(" Size: " + response.getData().size());
    }
}