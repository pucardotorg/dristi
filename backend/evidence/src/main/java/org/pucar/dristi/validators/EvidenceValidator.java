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
    public void validateEvidenceRegistration(EvidenceRequest evidenceRequest) throws CustomException {
        RequestInfo requestInfo = evidenceRequest.getRequestInfo();

        evidenceRequest.getArtifacts().forEach(artifact -> {
            if(ObjectUtils.isEmpty(artifact.getTenantId()) || ObjectUtils.isEmpty(artifact.getCaseId())){
                throw new CustomException(ILLEGAL_ARGUMENT_EXCEPTION_CODE,"tenantId and caseId are mandatory for creating advocate");
            }
        });
    }

    public Artifact validateApplicationExistence(EvidenceRequest evidenceRequest) {
        List<Artifact> artifact = evidenceRequest.getArtifacts();
        RequestInfo requestInfo = evidenceRequest.getRequestInfo();
        List<Artifact> existingApplications = repository.getArtifacts(String.valueOf(artifact.get(0).getId()),artifact.get(0).getTenantId(),artifact.get(0).getArtifactNumber(),artifact.get(0).getEvidenceNumber(),artifact.get(0).getCaseId(),artifact.get(0).getStatus());
        log.info("Existing application :: {}", existingApplications.size());
        if (existingApplications.isEmpty())
            throw new CustomException("VALIDATION EXCEPTION", "Evidence does not exist");

        return existingApplications.get(0);
    }

}
