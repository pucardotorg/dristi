//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.egov.common.contract.request.RequestInfo;
import org.egov.encryption.EncryptionServiceImpl;
import org.pucar.dristi.util.CaseConvertClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class CaseEncryptionServiceImpl extends EncryptionServiceImpl {
    private static final Logger log = LoggerFactory.getLogger(CaseEncryptionServiceImpl.class);

    @Autowired
    private CaseEncryptionServiceRestConnection encryptionServiceRestConnection;

    @Autowired
    private ObjectMapper objectMapper;

    public CaseEncryptionServiceImpl() {
    }


    public <E, P> P encryptJson(Object plaintextJson, String model, String tenantId, Class<E> valueType) throws IOException {
        return CaseConvertClass.convertTo(this.encryptJson(plaintextJson, model, tenantId), valueType);
    }


    public <E, P> P decryptJson(RequestInfo requestInfo, Object ciphertextJson, String model, String purpose, Class<E> valueType) throws IOException {
        return CaseConvertClass.convertTo(this.decryptJson(requestInfo, ciphertextJson, model, purpose), valueType);
    }


    public List<String> encryptValue(List<Object> plaintext, String tenantId, String type) throws IOException {
        Object encryptionResponse = this.encryptionServiceRestConnection.callEncrypt(tenantId, type, plaintext);
        return (List)CaseConvertClass.convertTo(this.objectMapper.valueToTree(encryptionResponse), List.class);
    }
}
