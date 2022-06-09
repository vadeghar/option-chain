package org.trade.option.client.nse;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.trade.option.client.grow.OcSymbolEnum;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NseClientTest {

    @Autowired
    NseClient nseClient;
    @Test
    public void getOptionChainTest() throws JsonProcessingException {
        NseOptionChainResponse optionChain = nseClient.getOptionChain(OcSymbolEnum.NIFTY.getOhlcSymbol());
        System.out.println("******* optionChain response: "+optionChain.getFiltered().getData().size());
    }

//    @Test
//    public void getNseCookieTest() throws JsonProcessingException {
//        String cookie = nseClient.getNseCookie();
//        System.out.println("************ "+cookie);
//    }

}
