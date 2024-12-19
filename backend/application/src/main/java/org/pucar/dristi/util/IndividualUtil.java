package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.Individual;
import org.pucar.dristi.web.models.IndividualSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.INDIVIDUAL_UTILITY_EXCEPTION;


@Component
@Slf4j
public class IndividualUtil {


    private ServiceRequestRepository serviceRequestRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public IndividualUtil(ServiceRequestRepository serviceRequestRepository, ObjectMapper objectMapper) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.objectMapper = objectMapper;
    }

    public List<Individual> getIndividualByIndividualId(IndividualSearchRequest individualRequest, StringBuilder uri) {
        List<Individual> individuals = new ArrayList<>();
        try {
            Object responseMap = serviceRequestRepository.fetchResult(uri, individualRequest);
            if (responseMap != null) {
                String jsonString = objectMapper.writeValueAsString(responseMap);
                log.info("Response :: {}", jsonString);
                JsonNode rootNode = objectMapper.readTree(jsonString);

                JsonNode individualNode = rootNode.path("Individual");

                if (individualNode.isArray()) {
                    for (JsonNode node : individualNode) {
                        Individual individual = objectMapper.treeToValue(node, Individual.class);
                        individuals.add(individual);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error occurred in individual utility", e);
            throw new CustomException(INDIVIDUAL_UTILITY_EXCEPTION, "Error in individual utility service: " + e.getMessage());
        }

        return individuals;
    }
}
