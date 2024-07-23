package org.pucar.dristi.validators;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.repository.WitnessRepository;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.INDIVIDUAL_NOT_FOUND;

import static org.pucar.dristi.config.ServiceConstants.VALIDATION_ERR;

@Component
public class WitnessRegistrationValidator {

    @Autowired
    private IndividualService individualService;

    @Autowired
    private CaseRepository repository;

    @Autowired
    private WitnessRepository witnessRepository;

    public void validateCaseRegistration(WitnessRequest witnessRequest) throws CustomException {
        RequestInfo requestInfo = witnessRequest.getRequestInfo();
        Witness witness = witnessRequest.getWitness();
        if (ObjectUtils.isEmpty(witness.getCaseId()))
            throw new CustomException("INVALID_CASE_ID", "caseId is mandatory for creating witness");
        if (ObjectUtils.isEmpty(witness.getIndividualId()))
            throw new CustomException("INDIVIDUAL_NOT_FOUND", "individualId is mandatory for creating witness");
        if (!individualService.searchIndividual(requestInfo, witness.getIndividualId()))
            throw new CustomException(INDIVIDUAL_NOT_FOUND, "Invalid complainant details");
    }

    public Witness validateApplicationExistence(RequestInfo requestInfo, Witness witness) {
        List<Witness> existingApplications = witnessRepository.getApplications(Collections.singletonList(WitnessSearchCriteria.builder().caseId(witness.getCaseId()).build()));
        if (existingApplications.isEmpty())
            throw new CustomException(VALIDATION_ERR, "Witness Application does not exist");
        if (ObjectUtils.isEmpty(witness.getCaseId()))
            throw new CustomException("EG_WT_APP_ERR", "caseId is mandatory for creating witness");
        if (ObjectUtils.isEmpty(witness.getIndividualId()))
            throw new CustomException("EG_WT_APP_ERR", "individualId is mandatory for creating witness");
        if (!individualService.searchIndividual(requestInfo, witness.getIndividualId()))
            throw new CustomException(INDIVIDUAL_NOT_FOUND, "Invalid complainant details");
        return existingApplications.get(0);
    }
}