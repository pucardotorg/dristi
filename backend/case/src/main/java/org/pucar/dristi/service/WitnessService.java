package org.pucar.dristi.service;

import static org.pucar.dristi.config.ServiceConstants.CREATE_WITNESS_ERR;
import static org.pucar.dristi.config.ServiceConstants.SEARCH_WITNESS_ERR;
import static org.pucar.dristi.config.ServiceConstants.UPDATE_WITNESS_ERR;
import static org.pucar.dristi.config.ServiceConstants.VALIDATION_ERR;

import java.util.ArrayList;
import java.util.List;

import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.WitnessRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.WitnessRepository;
import org.pucar.dristi.validators.WitnessRegistrationValidator;
import org.pucar.dristi.web.models.Witness;
import org.pucar.dristi.web.models.WitnessRequest;
import org.pucar.dristi.web.models.WitnessSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class WitnessService {

    private WitnessRegistrationValidator validator;

    private WitnessRegistrationEnrichment enrichmentUtil;

    private WitnessRepository witnessRepository;

    private Configuration config;

    private Producer producer;

    @Autowired
    public WitnessService(WitnessRegistrationValidator validator, Producer producer, Configuration config, WitnessRepository witnessRepository, WitnessRegistrationEnrichment enrichmentUtil) {
        this.validator = validator;
        this.producer = producer;
        this.config = config;
        this.witnessRepository = witnessRepository;
        this.enrichmentUtil = enrichmentUtil;
    }

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