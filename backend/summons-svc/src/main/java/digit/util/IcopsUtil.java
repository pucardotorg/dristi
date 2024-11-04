package digit.util;

import digit.config.Configuration;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
@Slf4j
public class IcopsUtil {

    private final RestTemplate restTemplate;

    private final Configuration config;

    public IcopsUtil(RestTemplate restTemplate, Configuration config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public LocationBasedJurisdiction getLocationBasedJurisdiction(LocationRequest request){
        StringBuilder uri = new StringBuilder();
        uri.append(config.getICopsHost())
                .append(config.getICopsLocationEndPoint());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LocationRequest> requestEntity = new HttpEntity<>(request, headers);
        log.info("request Body: {}", requestEntity);
        ResponseEntity<LocationBasedJurisdictionResponse> responseEntity = restTemplate.postForEntity(uri.toString(),
                requestEntity, LocationBasedJurisdictionResponse.class);
        log.info("Response Body: {}", responseEntity.getBody());
        return Objects.requireNonNull(responseEntity.getBody()).getLocationBasedJurisdiction();
    }
}
