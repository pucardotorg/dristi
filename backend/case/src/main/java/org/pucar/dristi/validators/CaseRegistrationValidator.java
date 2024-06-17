package org.pucar.dristi.validators;

import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.config.ServiceConstants;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.service.CaseService;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.util.AdvocateUtil;
import org.pucar.dristi.util.FileStoreUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    private CaseService caseService;

    @Autowired
    private MdmsUtil mdmsUtil;

    @Autowired
    private FileStoreUtil fileStoreUtil;

    @Autowired
    private AdvocateUtil advocateUtil;

    @Autowired
    private Configuration config;

    /*  To do validation->
        1. Validate MDMS data
        2. Fetch court department info from HRMS
        3. Validate artifact Ids
    */
    @Autowired
    public void setCaseService(@Lazy CaseService caseService) {
        this.caseService = caseService;
    }

    public void validateCaseRegistration(CaseRequest caseRequest) throws CustomException {
        CourtCase courtCase = caseRequest.getCases();

        if (ObjectUtils.isEmpty(courtCase.getTenantId()))
            throw new CustomException(VALIDATION_ERR, "tenantId is mandatory for creating case");
        if (ObjectUtils.isEmpty(courtCase.getCaseCategory()))
            throw new CustomException(VALIDATION_ERR, "caseCategory is mandatory for creating case");
        if (ObjectUtils.isEmpty(courtCase.getStatutesAndSections()))
            throw new CustomException(VALIDATION_ERR, "statute and sections is mandatory for creating case");
        if (!(courtCase.getWorkflow().getAction().equalsIgnoreCase(SAVE_DRAFT_CASE_WORKFLOW_ACTION) ||
                courtCase.getWorkflow().getAction().equalsIgnoreCase(DELETE_DRAFT_WORKFLOW_ACTION))) {
            if (ObjectUtils.isEmpty(courtCase.getLitigants()))
                throw new CustomException(VALIDATION_ERR, "litigants is mandatory for creating case");
        }
        if (ObjectUtils.isEmpty(caseRequest.getRequestInfo().getUserInfo())) {
            throw new CustomException(VALIDATION_ERR, "user info is mandatory for creating case");
        }
    }

    public Boolean validateApplicationExistence(CaseRequest caseRequest) {
        validateCaseRegistration(caseRequest);
        CourtCase courtCase = caseRequest.getCases();
        RequestInfo requestInfo = caseRequest.getRequestInfo();

        if (ObjectUtils.isEmpty(courtCase.getTenantId()))
            throw new CustomException(VALIDATION_ERR, "tenantId is mandatory for updating case");
        if (!(courtCase.getWorkflow().getAction().equalsIgnoreCase(SUBMIT_CASE_WORKFLOW_ACTION) ||
                courtCase.getWorkflow().getAction().equalsIgnoreCase(SAVE_DRAFT_CASE_WORKFLOW_ACTION) ||
                courtCase.getWorkflow().getAction().equalsIgnoreCase(DELETE_DRAFT_WORKFLOW_ACTION))){
            if(ObjectUtils.isEmpty(courtCase.getFilingDate()))
                throw new CustomException(VALIDATION_ERR, "filingDate is mandatory for updating case");
        }

        List<CaseCriteria> existingApplications = repository.getApplications(Collections.singletonList(CaseCriteria.builder().filingNumber(courtCase.getFilingNumber()).caseId(String.valueOf(courtCase.getId()))
                .cnrNumber(courtCase.getCnrNumber()).courtCaseNumber(courtCase.getCourCaseNumber()).build()));
        if (existingApplications.isEmpty())
            return false;
        Map<String, Map<String, JSONArray>> mdmsData = mdmsUtil.fetchMdmsData(requestInfo, courtCase.getTenantId(), config.getCaseBusinessServiceName(), createMasterDetails());

        if (mdmsData.get(config.getCaseBusinessServiceName()) == null)
            throw new CustomException(MDMS_DATA_NOT_FOUND, "MDMS data does not exist");
        if (!courtCase.getLitigants().isEmpty()) {
            courtCase.getLitigants().forEach(litigant -> {
                if(litigant.getIndividualId()!=null){
                    if (!individualService.searchIndividual(requestInfo, litigant.getIndividualId()))
                        throw new CustomException(INDIVIDUAL_NOT_FOUND, "Invalid complainant details");
                }
                else
                    throw new CustomException(INDIVIDUAL_NOT_FOUND, "Invalid complainant details");
            });
        }
        if (courtCase.getDocuments() != null && !courtCase.getDocuments().isEmpty()) {
            courtCase.getDocuments().forEach(document -> {
                if(document.getFileStore()!=null){
                    if (!fileStoreUtil.fileStore(courtCase.getTenantId(), document.getFileStore()))
                        throw new CustomException(INVALID_FILESTORE_ID, "Invalid document details");
                }
                else
                    throw new CustomException(INVALID_FILESTORE_ID, "Invalid document details");
            });
        }
        if (courtCase.getRepresentatives() != null && !courtCase.getRepresentatives().isEmpty()) {
            courtCase.getRepresentatives().forEach(rep -> {
                if(rep.getAdvocateId()!=null){
                    if (!advocateUtil.fetchAdvocateDetails(requestInfo, rep.getAdvocateId()))
                        throw new CustomException(INVALID_ADVOCATE_ID, "Invalid advocate details");
                }
                else
                    throw new CustomException(INVALID_ADVOCATE_ID, "Invalid advocate details");
            });
        }
        if (courtCase.getLinkedCases() != null && !courtCase.getLinkedCases().isEmpty()) {
            boolean isValidLinkedCase = courtCase.getLinkedCases().stream().allMatch(linkedCase ->
                    existingApplications.get(0).getResponseList().stream().anyMatch(existingCase ->
                            existingCase.getLinkedCases().stream().anyMatch(existingLinkedCase ->
                                    (linkedCase.getId() != null && linkedCase.getId().equals(existingLinkedCase.getId())) ||
                                            (linkedCase.getIsActive() != null && linkedCase.getIsActive() == existingLinkedCase.getIsActive()) ||
                                            (linkedCase.getCaseNumber() != null &&  linkedCase.getCaseNumber().equals(existingLinkedCase.getCaseNumber())) ||
                                            (linkedCase.getReferenceUri() != null && linkedCase.getReferenceUri().equals(existingLinkedCase.getReferenceUri()))
                            )
                    )
            );
            if (!isValidLinkedCase)
                throw new CustomException(INVALID_LINKEDCASE_ID, "Invalid linked case details");
        }

        return true;
    }

    private List<String> createMasterDetails() {
        List<String> masterList = new ArrayList<>();
        masterList.add("ComplainantType");
        masterList.add("CaseCategory");
        masterList.add("PaymentMode");
        masterList.add("ResolutionMechanism");

        return masterList;
    }


}