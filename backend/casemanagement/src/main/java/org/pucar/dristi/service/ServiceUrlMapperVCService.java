package org.pucar.dristi.service;

import org.egov.tracer.model.CustomException;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.web.models.CredentialRequest;
import org.pucar.dristi.web.models.VcCredentialRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ServiceUrlMapperVCService {

    private final ServiceUrlEntityRequestService serviceUrlEntityRequestService;
    private final Producer producer;
    private final FileDownloadService fileDownloadService;

    @Autowired
    public ServiceUrlMapperVCService(ServiceUrlEntityRequestService serviceUrlEntityRequestService,
                     Producer producer,
                     FileDownloadService fileDownloadService) {
        this.serviceUrlEntityRequestService = serviceUrlEntityRequestService;
        this.producer = producer;
        this.fileDownloadService = fileDownloadService;
    }

    public VcCredentialRequest generateVc(VcCredentialRequest vcCredentialRequest) {
        String refCode = vcCredentialRequest.getReferenceCode();
        if ("summons-order".equals(refCode)) {
            String signedHashValue = fileDownloadService.downloadAndExtractSignature(vcCredentialRequest);
            CredentialRequest credentialRequest = serviceUrlEntityRequestService.getEntityDetails(signedHashValue, vcCredentialRequest);
            producer.push("create-vc", credentialRequest);
        } else {
            throw new CustomException("INVALID_REFERENCE_CODE", "The reference code " + refCode + " is not recognized.");
        }
        return vcCredentialRequest;
    }
}
