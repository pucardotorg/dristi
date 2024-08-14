package org.pucar.dristi.validators;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.EvidenceRepository;
import org.pucar.dristi.util.*;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.*;

@Slf4j
@Component
public class EvidenceValidator {
    private final EvidenceRepository repository;
    private final CaseUtil caseUtil;
    private final ApplicationUtil applicationUtil;

    private final OrderUtil orderUtil;
    private final HearingUtil hearingUtil;


    @Autowired
    public EvidenceValidator(EvidenceRepository repository, CaseUtil caseUtil, ApplicationUtil applicationUtil,OrderUtil orderUtil,HearingUtil hearingUtil) {
        this.repository = repository;
        this.caseUtil = caseUtil;
        this.applicationUtil = applicationUtil;
        this.orderUtil = orderUtil;
        this.hearingUtil = hearingUtil;
    }

    public void validateEvidenceRegistration(EvidenceRequest evidenceRequest) throws CustomException {

        if (ObjectUtils.isEmpty(evidenceRequest.getArtifact().getTenantId()) || ObjectUtils.isEmpty(evidenceRequest.getArtifact().getCaseId()) || ObjectUtils.isEmpty(evidenceRequest.getArtifact().getFilingNumber())) {
            throw new CustomException(ILLEGAL_ARGUMENT_EXCEPTION_CODE, "tenantId, caseId and filing number are mandatory for creating advocate");
        }
        if (evidenceRequest.getRequestInfo().getUserInfo() == null) {
            throw new CustomException(ENRICHMENT_EXCEPTION, "User info not found!!!");
        }

        CaseExistsRequest caseExistsRequest = createCaseExistsRequest(evidenceRequest.getRequestInfo(), evidenceRequest.getArtifact());
        // Validate caseid
        if (!caseUtil.fetchCaseDetails(caseExistsRequest)) {
            throw new CustomException(CASE_EXCEPTION, "case does not exist");
        }
        // Validate applicationNumbers
        if(evidenceRequest.getArtifact().getApplication() != null) {
            ApplicationExistsRequest applicationExistsRequest = createApplicationExistRequest(evidenceRequest.getRequestInfo(), evidenceRequest.getArtifact());
            if (!applicationUtil.fetchApplicationDetails(applicationExistsRequest)) {
                throw new CustomException(APPLICATION_EXCEPTION, "application does not exist");
            }
        }
        // Validate orderId
        if(evidenceRequest.getArtifact().getOrder() != null) {
            OrderExistsRequest orderExistsRequest = createOrderExistRequest(evidenceRequest.getRequestInfo(), evidenceRequest.getArtifact());
            if (!orderUtil.fetchOrderDetails(orderExistsRequest)) {
                throw new CustomException(ORDER_EXCEPTION, "order does not exist");
            }
        }
        // Validate hearingId
        if(evidenceRequest.getArtifact().getHearing() != null) {
            HearingExistsRequest hearingExistsRequest = createHearingExistRequest(evidenceRequest.getRequestInfo(), evidenceRequest.getArtifact());
            if (!hearingUtil.fetchHearingDetails(hearingExistsRequest)) {
                throw new CustomException(HEARING_EXCEPTION, "hearing does not exist");
            }
        }
    }

    public Artifact validateEvidenceExistence(EvidenceRequest evidenceRequest) {
        try {
            validateEvidenceRegistration(evidenceRequest);
        }
        catch (Exception e) {
            log.error("Error occurred while updating evidence", e);
            throw new CustomException("EVIDENCE_UPDATE_EXCEPTION", "Error occurred while updating evidence: " + e.toString());
        }

        EvidenceSearchCriteria evidenceSearchCriteria = createEvidenceSearchCriteria(evidenceRequest);

        // Get existing applications using the repository method with EvidenceSearchCriteria
        List<Artifact> existingApplications = repository.getArtifacts(evidenceSearchCriteria,null);

        log.info("Existing application :: {}", existingApplications.size());

        // Check if any existing applications are found
        if (existingApplications.isEmpty()) {
            throw new CustomException("VALIDATION EXCEPTION", "Evidence does not exist");
        }

        // Return the first existing application
        return existingApplications.get(0);
    }
    EvidenceSearchCriteria createEvidenceSearchCriteria(EvidenceRequest evidenceRequest) {
        EvidenceSearchCriteria evidenceSearchCriteria = new EvidenceSearchCriteria();
        evidenceSearchCriteria.setId(String.valueOf(evidenceRequest.getArtifact().getId()));
        evidenceSearchCriteria.setCaseId(evidenceRequest.getArtifact().getCaseId());
        evidenceSearchCriteria.setApplicationNumber(evidenceRequest.getArtifact().getApplication());
        evidenceSearchCriteria.setFilingNumber(evidenceRequest.getArtifact().getFilingNumber());
        evidenceSearchCriteria.setHearing(evidenceRequest.getArtifact().getHearing());
        evidenceSearchCriteria.setOrder(evidenceRequest.getArtifact().getOrder());
        evidenceSearchCriteria.setSourceId(evidenceRequest.getArtifact().getSourceID());
        evidenceSearchCriteria.setSourceName(evidenceRequest.getArtifact().getSourceName());
        return evidenceSearchCriteria;
    }
    public CaseExistsRequest createCaseExistsRequest(RequestInfo requestInfo, Artifact artifact) {
        CaseExistsRequest caseExistsRequest = new CaseExistsRequest();
        CaseExists caseExists = new CaseExists();
        caseExists.setCaseId(artifact.getCaseId());
        caseExists.setFilingNumber(artifact.getFilingNumber());
        List<CaseExists> criteriaList = new ArrayList<>();
        criteriaList.add(caseExists);
        caseExistsRequest.setRequestInfo(requestInfo);
        caseExistsRequest.setCriteria(criteriaList);
        return caseExistsRequest;
    }
    public ApplicationExistsRequest createApplicationExistRequest(RequestInfo requestInfo, Artifact artifact){
        ApplicationExistsRequest applicationExistsRequest = new ApplicationExistsRequest();
        applicationExistsRequest.setRequestInfo(requestInfo);
        List<ApplicationExists> criteriaList = new ArrayList<>();
        ApplicationExists applicationExists = new ApplicationExists();
        applicationExists.setApplicationNumber(artifact.getApplication());
        criteriaList.add(applicationExists);
        applicationExistsRequest.setApplicationExists(criteriaList);
        return applicationExistsRequest;
    }
    public OrderExistsRequest createOrderExistRequest(RequestInfo requestInfo, Artifact artifact){
        OrderExistsRequest orderExistsRequest = new OrderExistsRequest();
        orderExistsRequest.setRequestInfo(requestInfo);
        List<OrderExists> criteriaList = new ArrayList<>();
        OrderExists orderExists = new OrderExists();
        orderExists.setOrderId(UUID.fromString(artifact.getOrder()));
        criteriaList.add(orderExists);
        orderExistsRequest.setOrder(criteriaList);
        return orderExistsRequest;
    }

    public HearingExistsRequest createHearingExistRequest(RequestInfo requestInfo, Artifact artifact){
        HearingExistsRequest hearingExistsRequest = new HearingExistsRequest();
        hearingExistsRequest.setRequestInfo(requestInfo);
        HearingExists hearingExists = new HearingExists();
        hearingExists.setHearingId(artifact.getHearing());
        hearingExistsRequest.setOrder(hearingExists);
        return hearingExistsRequest;
    }
}
