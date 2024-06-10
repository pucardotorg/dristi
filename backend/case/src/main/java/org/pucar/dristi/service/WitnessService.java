package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.WitnessRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.WitnessRepository;
import org.pucar.dristi.validators.WitnessRegistrationValidator;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.*;


@Service
@Slf4j
public class WitnessService {

    @Autowired
    private WitnessRegistrationValidator validator;

    @Autowired
    private WitnessRegistrationEnrichment enrichmentUtil;

    @Autowired
    private WitnessRepository witnessRepository;

    @Autowired
    private Configuration config;
    @Autowired
    private Producer producer;

    public Witness registerWitnessRequest(WitnessRequest body) {
        try {
            validator.validateCaseRegistration(body);

            enrichmentUtil.enrichWitnessRegistration(body);

            producer.push(config.getWitnessCreateTopic(), body);

            return body.getWitness();
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while creating witness :: {}",e.toString());
            throw new CustomException(CREATE_WITNESS_ERR, e.getMessage());
        }


    }

    public List<Witness> searchWitnesses(WitnessSearchRequest witnessSearchCriteria) {

        try {
            // Fetch applications from database according to the given search criteria
            List<Witness> witnesses = witnessRepository.getApplications(witnessSearchCriteria.getSearchCriteria());
            log.info("Witness Applications Size :: {}", witnesses.size());

            // If no applications are found matching the given criteria, return an empty list
            if (CollectionUtils.isEmpty(witnesses))
                return new ArrayList<>();
            return witnesses;
        } catch (CustomException e) {
            log.error("Custom Exception occurred while searching");
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching to search results");
            throw new CustomException(SEARCH_WITNESS_ERR, e.getMessage());
        }
    }

    public Witness updateWitness(WitnessRequest witnessRequest) {

        try {

            // Validate whether the application that is being requested for update indeed exists
            try {
                validator.validateApplicationExistence(witnessRequest.getRequestInfo(), witnessRequest.getWitness());
            } catch (Exception e) {
                log.error("Error validating existing application");
                throw new CustomException(VALIDATION_ERR, "Error validating existing application: " + e.getMessage());
            }

            // Enrich application upon update
            enrichmentUtil.enrichWitnessApplicationUponUpdate(witnessRequest);

            producer.push(config.getWitnessUpdateTopic(), witnessRequest);

            return witnessRequest.getWitness();
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating witness");
            throw new CustomException(UPDATE_WITNESS_ERR, "Error occurred while updating witness: " + e.getMessage());
        }

    }
}