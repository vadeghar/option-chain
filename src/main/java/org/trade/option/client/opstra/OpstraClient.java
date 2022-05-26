package org.trade.option.client.opstra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class OpstraClient {
    private final RestTemplate restTemplate;
    @Value("${org.opstra.cookie}")
    private String cookieValue;
    public OpstraClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    private final String OPSTRA_OC_URL = "https://opstra.definedge.com/api/openinterest/free/:symbol&:expiry";
    ObjectMapper mapper = new ObjectMapper();
    public OpstraOptionChainResponse getOptionChain(String symbol, String expiry) throws JsonProcessingException {
        log.info("Inside getOptionChain of OpstraClient");
        String url = OPSTRA_OC_URL.replace(":symbol", symbol).replace(":expiry", expiry.toUpperCase());
        HttpHeaders headers = new HttpHeaders();
        headers.set("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36");
        headers.set("cookie",
                "AWSALBAPP-1=_remove_; AWSALBAPP-2=_remove_; AWSALBAPP-3=_remove_; _ga=GA1.2.221848406.1653480222; _gid=GA1.2.1863870012.1653480222; JSESSIONID=867EFAD7E60275F5DF7902D55A101DF9; _gat=1; AWSALBAPP-0=AAAAAAAAAABTYra8SRapyo5qx7B7Ji64gX0rkm3nMeFzQ5f5p46nM0Pvz1hKfMmzeh32xrHz4trFiIrWj+aBZYou/YZ3jw1r+1GVjoqm9BYiFo9cHdLYoBTBRGAzCRm8xvqW1KAHTvWE2Q==");
        HttpEntity entity = new HttpEntity(headers);
        log.info("Calling URL: "+url);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return mapper.readValue(response.getBody(), OpstraOptionChainResponse.class);
    }

}
