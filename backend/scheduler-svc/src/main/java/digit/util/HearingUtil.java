package digit.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import digit.config.Configuration;
import digit.repository.ServiceRequestRepository;
import digit.web.models.hearing.Hearing;
import digit.web.models.hearing.HearingListSearchRequest;
import digit.web.models.hearing.HearingRequest;
import digit.web.models.hearing.HearingUpdateBulkRequest;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static digit.config.ServiceConstants.*;

@Component
@Slf4j
public class HearingUtil {

    private final ObjectMapper objectMapper;

    private final Configuration configuration;

    private final ServiceRequestRepository serviceRequestRepository;

    public HearingUtil(ObjectMapper objectMapper, Configuration configuration, ServiceRequestRepository serviceRequestRepository) {
        this.objectMapper = objectMapper;
        this.configuration = configuration;
        this.serviceRequestRepository = serviceRequestRepository;
    }

    public void callHearing(HearingUpdateBulkRequest hearingRequest) {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        StringBuilder uri = new StringBuilder(configuration.getHearingHost().concat(configuration.getHearingUpdateEndPoint()));
        try {
            serviceRequestRepository.fetchResult(uri,hearingRequest);
        } catch (HttpClientErrorException e) {
            log.error(EXTERNAL_SERVICE_EXCEPTION, e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(SEARCHER_SERVICE_EXCEPTION, e);
        }
    }

    public List<Hearing> fetchHearing(HearingListSearchRequest request) {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        StringBuilder uri = new StringBuilder(configuration.getHearingHost().concat(configuration.getHearingSearchEndPoint()));

        Object response = serviceRequestRepository.fetchResult(uri,request);
        List<Hearing> hearingList = null;
        try {
            JsonNode jsonNode = objectMapper.valueToTree(response);
            JsonNode hearingListNode = jsonNode.get("HearingList");
            hearingList = objectMapper.readValue(hearingListNode.toString(), new TypeReference<>() {
            });
        } catch (HttpClientErrorException e) {
            log.error(EXTERNAL_SERVICE_EXCEPTION, e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(SEARCHER_SERVICE_EXCEPTION, e);
        }
        return hearingList;
    }
}
