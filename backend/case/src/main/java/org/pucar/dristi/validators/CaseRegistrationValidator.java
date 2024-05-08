package org.pucar.dristi.validators;

import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.util.FileStoreUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
public class CaseRegistrationValidator {
    @Autowired
    private IndividualService individualService;
    @Autowired
    private CaseRepository repository;

    @Autowired
    private MdmsUtil mdmsUtil;

    @Autowired
    private FileStoreUtil fileStoreUtil;

    public void validateCaseRegistration(CaseRequest caseRequest) throws CustomException{
        RequestInfo requestInfo = caseRequest.getRequestInfo();

        caseRequest.getCases().forEach(courtCase -> {
            if(ObjectUtils.isEmpty(courtCase.getTenantId()))
                throw new CustomException(CREATE_CASE_ERR, "tenantId is mandatory for creating case");
            if(ObjectUtils.isEmpty(courtCase.getFilingDate()))
                throw new CustomException(CREATE_CASE_ERR, "filingDate is mandatory for creating case");
            if(ObjectUtils.isEmpty(courtCase.getCaseCategory()))
                throw new CustomException(CREATE_CASE_ERR, "caseCategory is mandatory for creating case");
            if(ObjectUtils.isEmpty(courtCase.getStatutesAndSections()))
                throw new CustomException(CREATE_CASE_ERR, "statute and sections is mandatory for creating case");
            if(ObjectUtils.isEmpty(courtCase.getLitigants()))
                throw new CustomException(CREATE_CASE_ERR, "litigants is mandatory for creating case");

            Map<String, Map<String, JSONArray>> mdmsData  = mdmsUtil.fetchMdmsData(requestInfo, courtCase.getTenantId(), "case", createMasterDetails());

            if(mdmsData.get("case")==null)
                throw new CustomException(MDMS_DATA_NOT_FOUND,"mdms data does not exist");

            courtCase.getLitigants().forEach(litigant -> {
                if(!individualService.searchIndividual(requestInfo, litigant.getIndividualId()))
                    throw new CustomException(INDIVIDUAL_NOT_FOUND,"Requested Individual not found or does not exist");
            });

            courtCase.getDocuments().forEach(document -> {
                if(!fileStoreUtil.fileStore(courtCase.getTenantId(), document.getFileStore()))
                    throw new CustomException(INVALID_FILESTORE_ID,"Invalid file store ID");
            });

        });
    }

    private List<String> createMasterDetails() {
        List<String> masterList = new ArrayList<>();
        masterList.add("ComplainantType");
        masterList.add("CaseCategory");
        masterList.add("PaymentMode");
        masterList.add("ResolutionMechanism");

        return masterList;
    }

    public CourtCase validateApplicationExistence(CourtCase courtCase) {
        List<CourtCase> existingApplications = repository.getApplications(Collections.singletonList(CaseCriteria.builder().filingNumber(courtCase.getFilingNumber()).build()));
        if(existingApplications.isEmpty()) throw new CustomException("VALIDATION EXCEPTION","Case Application does not exist");
        return existingApplications.get(0);
    }
}