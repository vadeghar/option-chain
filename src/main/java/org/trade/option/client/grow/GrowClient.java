package org.trade.option.client.grow;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GrowClient {
    private final RestTemplate restTemplate;
    private final String GROW_OC_URL = "https://groww.in/v1/api/option_chain_service/v1/option_chain/:symbol?expiry=:expiry";
    private final String GROW_INDICES_OHLC_URL = "https://groww.in/v1/api/stocks_data/v1/accord_points/exchange/NSE/segment/CASH/latest_indices_ohlc/:symbol";



    public GrowClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GrowOptionChainResponse getOptionChain(String symbol, String expiry) {
        log.info("Inside getOptionChain of GrowClient");
        String url = GROW_OC_URL.replace(":symbol", symbol).replace(":expiry", expiry);
        log.info("Calling URL: "+url);
        return restTemplate.getForObject(url, GrowOptionChainResponse.class);
    }

    public GrowOhlc getOhlc(String symbol) {
        log.info("Inside getOhlc of GrowClient");
        String url = GROW_INDICES_OHLC_URL.replace(":symbol", symbol);
        log.info("Calling URL: "+url);
        return restTemplate.getForObject(url, GrowOhlc.class);
    }




}
