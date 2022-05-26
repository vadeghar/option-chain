package org.trade.option.client.grow;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GrowClientTest {
    @LocalServerPort
    private int port;
    @Autowired
    GrowClient growClient;

//    @Test
    public void getOptionChaingTest() {
        GrowOptionChainResponse response = growClient.getOptionChain("nifty", "2022-05-26");
        System.out.println(" Size: "+response.getOptionChains().size());
    }

    @Test
    public void getOhlcTest() {
        GrowOhlc ohlv = growClient.getOhlc("BANKNIFTY");
        System.out.println("Response: "+ohlv.getValue());
    }

}
