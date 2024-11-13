package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.util.MdmsV2Util;
import org.pucar.dristi.web.models.CredentialRequest;
import org.pucar.dristi.web.models.Mdms;
import org.pucar.dristi.web.models.VcCredentialRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Service
public class ServiceUrlMapperVCService {

    private final ServiceUrlEntityRequestService serviceUrlEntityRequestService;
    private final Producer producer;
    private final FileDownloadService fileDownloadService;
    private final Configuration configuration;
    private final MdmsV2Util mdmsV2Util;

    @Autowired
    public ServiceUrlMapperVCService(ServiceUrlEntityRequestService serviceUrlEntityRequestService,
                     Producer producer,
                     FileDownloadService fileDownloadService,
                     Configuration configuration,
                     MdmsV2Util mdmsV2Util) {
        this.serviceUrlEntityRequestService = serviceUrlEntityRequestService;
        this.producer = producer;
        this.fileDownloadService = fileDownloadService;
        this.configuration = configuration;
        this.mdmsV2Util = mdmsV2Util;

    }

    public VcCredentialRequest generateVc(VcCredentialRequest vcCredentialRequest) {
        Set<String> uniqueIdentifiers = new HashSet<>();
        uniqueIdentifiers.add(vcCredentialRequest.getModuleName());
        JsonNode data = mdmsV2Util.fetchMdmsV2Schema(vcCredentialRequest.getRequestInfo(), vcCredentialRequest.getTenantId(), null, uniqueIdentifiers, null, null);

        if (data != null && !data.isEmpty()) {
            JsonNode firstElement = data.get(0);
            if (firstElement.has("code") && Objects.equals(firstElement.get("code").asText(), configuration.getVcCode())) {
                String signedHashValue = fileDownloadService.downloadAndExtractSignature(vcCredentialRequest);
                CredentialRequest credentialRequest = serviceUrlEntityRequestService.getEntityDetails(signedHashValue, vcCredentialRequest);
                producer.push(configuration.getCreateVc(), credentialRequest);
            } else {
                throw new CustomException("UNEXCEPTED_MODULE_NAME", "The module name " + vcCredentialRequest.getModuleName() + " is not excepted.");
            }
        } else {
            throw new CustomException("INVALID_MODULE_NAME", "The module name " + vcCredentialRequest.getModuleName() + " is not recognized.");
        }
        return vcCredentialRequest;
    }
}
