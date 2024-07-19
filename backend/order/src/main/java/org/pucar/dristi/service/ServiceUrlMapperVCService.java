package org.pucar.dristi.service;

import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.web.models.CredentialRequest;
import org.pucar.dristi.web.models.VcCredentialRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class ServiceUrlMapperVCService {

    @Autowired
    private ServiceUrlEntityRequestService serviceUrlEntityRequestService;

    @Autowired
    private Producer producer;

    @Autowired
    private FileDownloadService fileDownloadService;

    public VcCredentialRequest generateVc(VcCredentialRequest vcCredentialRequest) {
        String refCode= vcCredentialRequest.getReferenceCode();
        switch (refCode) {
            case "summons-order":
                //urlMapping = "https://dristi-dev.pucar.org/task/v1/search";
                String signedHashValue=fileDownloadService.downloadAndExtractSignature(vcCredentialRequest);
                CredentialRequest credentialRequest=serviceUrlEntityRequestService.getEntityDetails(signedHashValue,vcCredentialRequest);
                producer.push("create-vc",credentialRequest);
                break;
        }
        return vcCredentialRequest;
    }
}
