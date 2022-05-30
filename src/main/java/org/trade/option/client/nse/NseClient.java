package org.trade.option.client.nse;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.trade.option.annotations.TrackExecutionTime;
import org.trade.option.client.opstra.OpstraOptionChainResponse;

@Service
@Slf4j
public class NseClient {
    private final RestTemplate restTemplate;
    private final String NSE_OC_URL = "https://www.nseindia.com/api/option-chain-indices?symbol=:symbol";

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
            headers.set("Cookie", "_ga=GA1.2.1488705909.1652194630; nsit=ugZxrGdApUEu2T1_Eg4jBc8H; nseappid=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcGkubnNlIiwiYXVkIjoiYXBpLm5zZSIsImlhdCI6MTY1MzkwNzA0MCwiZXhwIjoxNjUzOTEwNjQwfQ.CTbODmnq3pImnasibBgFk3Xfu4x-CylOiWnIR6a8k3s; AKA_A2=A; ak_bmsc=0C765D4F2836EAF149C01DC58A25F6E5~000000000000000000000000000000~YAAQSg3GF+DEHQuBAQAAdrWKFA8zamqgPtOX1+asNzGIhjiNujwRpfBe5v5/tuuxbd5P1uVsEBuOgMIcjD8Zvpv4V4FP356342yDSRTVk7g4fT6r+NafpX5E7GOZo0p1jes+sGSui31bNKiV0W08Df/kM3nsemidTT7GaApPoAfRRyvXzVjXk2silpTjREXSOB/jMAfPTomDKeWG31nbmN+Z48tS4xueHkwcvqcpEy6N0tIHesoyF0tcGJXb8pUTIsh+RjvcZ3fTWiYaI3nBz1L/XUkpchkXLpcgMSk7IRawIaFggqg9S8zthKw0US/GqWcyI0QX6o1gS/CkDM7tTu5Yo6k11hty8ZMV/x5VZ5J0/XihsUAXKTnx+oa1oA4Umwp1BMK1jDi0iP627TVq4G2+hq3xCp8YPcff8BKaTneYFzshrV/xaWgSvrSimN54g8mfbDQ61bS08gwSH5yH0nP8+51HWy1WV7F+29QSv/F5ixX1+7czJ8uAStxa; nseQuoteSymbols=[{\"symbol\":\"BANKNIFTY\",\"identifier\":null,\"type\":\"equity\"}]; RT=\"z=1&dm=nseindia.com&si=de29a99e-e8be-458e-9210-65175fd80544&ss=l3sljhpw&sl=1&tt=4h4&bcn=%2F%2F17de4c19.akstat.io%2F&ld=59a&nu=uwpfk91t&cl=l5e\"; bm_sv=3FE84B56E8FE5BC6E7E4749F48564A60~YAAQSg3GF1zHHQuBAQAAnhCLFA/bydTTV/hsnwmsLTOgppfqOcRWhP9qt3n39q87Ka3d7aFh+xS8vFS2mg4RPFamaicrkqpxSmh7XyBiqnK+7e+aPf42qZnv+Ozt45c8TqBxmFADZnb/UEAYhhIYrbxwXvG0gQfDz5GxXZvujeRxuKFVQiYngb9jy32F5aoIk78esVgDGPEDyuhR4a3OqaGd6Wi1sH/okwdRYxx50IKPZLXWQwKFzBvu2GD+xNzPKZ0=~1");
            HttpEntity entity = new HttpEntity(headers);
            log.info("Calling URL: " + url);
            ResponseEntity<NseOptionChainResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, NseOptionChainResponse.class);
            return response.getBody();
//            return restTemplate.getForObject(url,String.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
