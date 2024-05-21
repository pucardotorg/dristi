package org.pucar.dristi.validators;

import org.apache.commons.lang3.ObjectUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.EvidenceRequest;
import org.springframework.stereotype.Component;

import static org.pucar.dristi.config.ServiceConstants.ILLEGAL_ARGUMENT_EXCEPTION_CODE;
@Component
public class EvidenceValidator {
    public void validateEvidenceRegistration(EvidenceRequest evidenceRequest) throws CustomException {
        RequestInfo requestInfo = evidenceRequest.getRequestInfo();

        evidenceRequest.getArtifacts().forEach(artifact -> {
            if(ObjectUtils.isEmpty(artifact.getTenantId()) || ObjectUtils.isEmpty(artifact.getCaseId())){
                throw new CustomException(ILLEGAL_ARGUMENT_EXCEPTION_CODE,"tenantId and caseId are mandatory for creating advocate");
            }
        });
    }
}
