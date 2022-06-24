package org.trade.option.client.nse;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.trade.option.annotations.TrackExecutionTime;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class NseClient {
    private final RestTemplate restTemplate;
    private final String NSE_OC_URL = "https://www.nseindia.com/api/option-chain-indices?symbol=:symbol";

    private String cookieValue;
    public NseClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @TrackExecutionTime
    public NseOptionChainResponse getOptionChain(String symbol) throws JsonProcessingException {
        log.info("Inside getOptionChain of NseClient");
        try {
            String url = NSE_OC_URL.replace(":symbol", symbol);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "*/*");
            headers.set("User-Agent", "PostmanRuntime/7.28.4");
            headers.set("Cookie", cookieValue);
            HttpEntity entity = new HttpEntity(headers);
            log.info("Calling URL: " + url);
            ResponseEntity<NseOptionChainResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, NseOptionChainResponse.class);
            return response.getBody();
//            return restTemplate.getForObject(url,String.class);
        } catch (HttpClientErrorException e) {
            log.error("******************** Client Error: "+e.getMessage());
            cookieValue = getNseCookie();
            return getOptionChain(symbol);
        } catch (Exception e) {
            log.error("******************** RETURNING NULL VALUE, SEE THE ERROR BELOW ");
            e.printStackTrace();
            return null;
        }
    }

    public String getNseCookie() {
        log.info("Inside getNseCookie of NseClient");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("User-Agent", "PostmanRuntime/7.28.4");
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange("https://www.nseindia.com", HttpMethod.GET, entity, String.class);
        StringBuilder sb = new StringBuilder();
        Optional<Map.Entry<String, List<String>>> cookie =  response.getHeaders().entrySet().stream().filter(es-> es.getKey().equals("Set-Cookie")).findFirst();
        if(cookie.isPresent()) {
            Map.Entry<String, List<String>> entry = cookie.get();
            for(String s : entry.getValue()) {
                if(s.contains("nsit")) {
                    if(sb.toString().length() > 0)
                        sb.append(" ");
                    sb.append(s.substring(s.indexOf("nsit"), s.indexOf(";")+1));
                }
                if(s.contains("nseappid")) {
                    if(sb.toString().length() > 0)
                        sb.append(" ");
                    sb.append(s.substring(s.indexOf("nseappid"), s.indexOf(";")+1));
                }
            }
        }
        log.info("Returning cookie "+sb.toString());
        return sb.toString();
    }

}
