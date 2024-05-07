package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.CaseRegistrationEnrichment;
import org.pucar.dristi.enrichment.WitnessRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.repository.WitnessRepository;
import org.pucar.dristi.validators.CaseRegistrationValidator;
import org.pucar.dristi.validators.WitnessRegistrationValidator;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


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

    public List<Witness> registerWitnessRequest(WitnessRequest body) {
        try {
            validator.validateCaseRegistration(body);
            enrichmentUtil.enrichCaseRegistration(body);

            producer.push("save-witness-application", body);
            return body.getWitnesses();
        }
        catch (CustomException e){
            log.error("Custom Exception occurred while creating advocate");
            throw e;
        } catch (Exception e){
            log.error("Error occurred while creating advocate");
            throw new CustomException("CASE_CREATE_EXCEPTION",e.getMessage());
        }


    }

    public List<Witness> searchWitnesses(WitnessSearchRequest witnessSearchCriteria) {

        try {
            // Fetch applications from database according to the given search criteria
            List<Witness> witnesses = witnessRepository.getApplications(witnessSearchCriteria.getSearchCriteria());

            // If no applications are found matching the given criteria, return an empty list
            if(CollectionUtils.isEmpty(witnesses))
                return new ArrayList<>();
            return witnesses;
        }
        catch (CustomException e){
            log.error("Custom Exception occurred while searching");
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching to search results");
            throw new CustomException("CASE_SEARCH_EXCEPTION",e.getMessage());
        }
    }

    public List<Witness> updateWitness(WitnessRequest witnessRequest) {

        try {

            // Validate whether the application that is being requested for update indeed exists
            List<Witness> witnessList = new ArrayList<>();
            witnessRequest.getWitnesses().forEach(witness -> {
                Witness existingApplication;
                try {
                    existingApplication = validator.validateApplicationExistence(witness);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error validating existing application");
                    throw new CustomException("WITNESS_CREATE_EXCEPTION","Error validating existing application: "+ e.getMessage());
                }
                witnessList.add(existingApplication);
            });

            witnessRequest.setWitnesses(witnessList);

            // Enrich application upon update
            enrichmentUtil.enrichWitnessApplicationUponUpdate(witnessRequest);

            producer.push(config.getWitnessUpdateTopic(), witnessRequest);

            return witnessRequest.getWitnesses();

        } catch (CustomException e){
            log.error("Custom Exception occurred while updating witness");
            throw e;
        } catch (Exception e){
            log.error("Error occurred while updating witness");
            throw new CustomException("WITNESS_UPDATE_EXCEPTION","Error occurred while updating witness: " + e.getMessage());
        }

    }
}