package org.trade.option.client.nse;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.trade.option.annotations.TrackExecutionTime;
import org.trade.option.client.nse.domain.OptionData;
import org.trade.option.exceptions.NseClientException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class NseClient {
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("nseWebClient")
    private WebClient nseWebClient;
    private final String NSE_OC_URL = "https://www.nseindia.com/api/option-chain-indices?symbol=:symbol";

    private String cookieValue;
    public NseClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Deprecated
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

    @TrackExecutionTime
    public OptionData getOptionChainData(String symbol) throws Exception {
        log.info("Inside getOptionChain of NseClient");
        try {
            String url = "/option-chain-indices?symbol="+symbol;
            OptionData response = nseWebClient.get()
                    .uri(url)
                    .header("Cookie", getNseCookie())
//                    .cookie("Cookie", )
                    .retrieve()
                    .onStatus(HttpStatus::is5xxServerError, errorResponse ->
                        errorResponse.bodyToMono(String.class).map(NseClientException::new)
                    )
                    .bodyToMono(OptionData.class)
                    .block();

           return response;
        } catch (NseClientException e) {
            log.error("******************** Client Error: "+e.getMessage());
            cookieValue = getNseCookie();
            return getOptionChainData(symbol);
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
        Optional<Map.Entry<String, List<String>>> cookie =  response.getHeaders().entrySet().stream().filter(es-> es.getKey().equalsIgnoreCase("Set-Cookie")).findFirst();
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
        log.info("Returning cookie ********** ");
        return sb.toString();
    }

}

//bm_sv=BC18275DF919CD399E10C9A59DECC434~YAAQjHMsMZKfy76GAQAAoMzO2xMYqUlhwU2vkqlCye609DJhN24yNEU36LgoOGHeZKi7chdBJRf0Ah50cdOsxOg5GeZvGJ5wzlmjORhTISsOMRLT3JDKza3g1/bPDr1l6WNXgqHzXLul6GplLLX6u6xjDjzPyuI61lxohMQp6jRRx6qVNnKc6ew/wHt4kuk7G4C1KdeFebpvhX/A/JzyfvzdTRp58nvL9+iGjsj0U5i5MT0dSRM//h1q64hK3ikDaoY=~1;