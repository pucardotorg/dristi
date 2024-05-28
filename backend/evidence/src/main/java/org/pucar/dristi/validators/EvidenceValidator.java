package org.pucar.dristi.validators;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.ObjectUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.EvidenceRepository;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.EvidenceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.ILLEGAL_ARGUMENT_EXCEPTION_CODE;
import static org.pucar.dristi.config.ServiceConstants.MDMS_DATA_NOT_FOUND;

@Slf4j
@Component
public class EvidenceValidator {
    @Autowired
    private EvidenceRepository repository;
    @Autowired
    private MdmsUtil mdmsUtil;
    public void     validateEvidenceRegistration(EvidenceRequest evidenceRequest) throws CustomException {
        RequestInfo requestInfo = evidenceRequest.getRequestInfo();

            if(ObjectUtils.isEmpty(evidenceRequest.getArtifact().getTenantId()) || ObjectUtils.isEmpty(evidenceRequest.getArtifact().getCaseId())){
                throw new CustomException(ILLEGAL_ARGUMENT_EXCEPTION_CODE,"tenantId and caseId are mandatory for creating advocate");
            }
    }

    public Artifact validateApplicationExistence(EvidenceRequest evidenceRequest) {
        RequestInfo requestInfo = evidenceRequest.getRequestInfo();
        List<Artifact> existingApplications = repository.getArtifacts(String.valueOf(evidenceRequest.getArtifact().getId()),evidenceRequest.getArtifact().getTenantId(),evidenceRequest.getArtifact().getArtifactNumber(),evidenceRequest.getArtifact().getEvidenceNumber(),evidenceRequest.getArtifact().getCaseId(),evidenceRequest.getArtifact().getStatus());
        log.info("Existing application :: {}", existingApplications.size());
        if (existingApplications.isEmpty())
            throw new CustomException("VALIDATION EXCEPTION", "Evidence does not exist");

        return existingApplications.get(0);
    }

}
