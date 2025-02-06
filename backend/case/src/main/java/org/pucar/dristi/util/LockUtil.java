package org.pucar.dristi.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LockUtil {

    private final Configuration configuration;
    private final ServiceRequestRepository repository;
    private final ObjectMapper objectMapper;

    @Autowired
    public LockUtil(Configuration configuration, ServiceRequestRepository repository, ObjectMapper objectMapper) {
        this.configuration = configuration;
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public boolean isLockPresent(RequestInfo requestInfo, String uniqueId, String tenantId) throws JsonProcessingException {

        StringBuilder uri = new StringBuilder();

        uri.append(configuration.getLockSvcHost())
                .append(configuration.getLockEndPoint())
                .append("?uniqueId=").append(uniqueId)
                .append("&tenantId=").append(tenantId);

        RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder()
                .requestInfo(requestInfo).build();

        Object object = repository.fetchResult(uri, requestInfoWrapper);
        String jsonString = objectMapper.writeValueAsString(object);
        // Parse the JSON string
        JsonNode rootNode = objectMapper.readTree(jsonString);

        JsonNode lockNode = rootNode.path("Lock");

        if (!lockNode.isMissingNode() && lockNode.has("isLocked")) {
            return lockNode.get("isLocked").asBoolean();
        }
        return false;

    }
}
