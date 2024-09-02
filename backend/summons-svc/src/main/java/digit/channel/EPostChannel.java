package digit.channel;

import digit.config.Configuration;
import digit.web.models.ChannelMessage;
import digit.web.models.ChannelResponse;
import digit.web.models.TaskRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class EPostChannel implements ExternalChannel {

    private final RestTemplate restTemplate;

    private final Configuration config;

    @Autowired
    public EPostChannel(RestTemplate restTemplate, Configuration config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    @Override
    public ChannelMessage sendSummons(TaskRequest request) {
        StringBuilder uri = new StringBuilder();
        uri.append(config.getEPostHost())
                .append(config.getEPostRequestEndPoint());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TaskRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ChannelResponse> responseEntity = restTemplate.postForEntity(uri.toString(),
                requestEntity, ChannelResponse.class);
        log.info("Response Body: {}", responseEntity.getBody());
        return responseEntity.getBody().getChannelMessage();
    }
}
