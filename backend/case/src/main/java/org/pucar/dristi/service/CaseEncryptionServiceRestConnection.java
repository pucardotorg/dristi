//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import org.egov.encryption.config.EncProperties;
import org.egov.encryption.web.contract.EncReqObject;
import org.egov.encryption.web.contract.EncryptionRequest;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
class CaseEncryptionServiceRestConnection {
    private static final Logger log = LoggerFactory.getLogger(CaseEncryptionServiceRestConnection.class);
    @Autowired
    private EncProperties encProperties;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    CaseEncryptionServiceRestConnection() {
    }

    Object callEncrypt(String tenantId, String type, Object value) throws IOException {
        EncReqObject encReqObject = new EncReqObject(tenantId, type, value);
        EncryptionRequest encryptionRequest = new EncryptionRequest();
        encryptionRequest.setEncryptionRequests(new ArrayList(Collections.singleton(encReqObject)));

        try {
            ResponseEntity<String> response = this.restTemplate.postForEntity(this.encProperties.getEgovEncHost() + this.encProperties.getEgovEncEncryptPath(), encryptionRequest, String.class, new Object[0]);
            return this.objectMapper.readTree((String)response.getBody()).get(0);
        } catch (Exception var7) {
            Exception e = var7;
            log.error("Error occurred while calling Encryption Service", e);
            throw new CustomException("ENCRYPTION_SERVICE_ERROR", "Error occurred while calling Encryption Service");
        }
    }

    JsonNode callDecrypt(Object ciphertext) {
        try {
            ResponseEntity<JsonNode> response = this.restTemplate.postForEntity(this.encProperties.getEgovEncHost() + this.encProperties.getEgovEncDecryptPath(), ciphertext, JsonNode.class, new Object[0]);
            return (JsonNode)response.getBody();
        } catch (Exception var3) {
            throw new CustomException("ENCRYPTION_SERVICE_ERROR", "Error occurred while calling Encryption Service");
        }
    }
}
