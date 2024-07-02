package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.encryption.EncryptionService;
import org.egov.encryption.audit.AuditService;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.Party;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class EncryptionDecryptionUtilTest {

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private AuditService auditService;

    @Mock
    private ObjectMapper objectMapper;

    @Value("${state.level.tenant.id}")
    private String stateLevelTenantId = "defaultTenantId";

    @Value("${decryption.abac.enabled}")
    private boolean abacEnabled = false;

    private EncryptionDecryptionUtil encryptionDecryptionUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        encryptionDecryptionUtil = new EncryptionDecryptionUtil(encryptionService, auditService, objectMapper, stateLevelTenantId, abacEnabled);
    }

    @Test
    public void testEncryptObject_NullObject() {
        // Test with null object
        Object encryptedObject = encryptionDecryptionUtil.encryptObject(null, "key", Object.class);
        assertNull(encryptedObject);
    }

    @Test
    public void testEncryptObject_Success() throws IOException {
        // Mock behavior for successful encryption
        Object objectToEncrypt = new Object();
        Object encryptedObject = new Object();
        when(encryptionService.encryptJson(any(), anyString(), anyString(), any())).thenReturn(encryptedObject);

        Object result = encryptionDecryptionUtil.encryptObject(objectToEncrypt, "key", Object.class);
        assertNotNull(result);
        assertEquals(encryptedObject, result);
    }

    @Test
    public void testEncryptObject_EncryptionNullError() throws IOException {
        // Mock behavior to return null from encryption service
        when(encryptionService.encryptJson(any(), anyString(), anyString(), any())).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () ->
                encryptionDecryptionUtil.encryptObject(new Object(), "key", Object.class));
        assertEquals("ENCRYPTION_NULL_ERROR", exception.getCode());
    }

    @Test
    public void testEncryptObject_ExceptionThrown() throws IOException {
        // Mock behavior to throw an exception
        when(encryptionService.encryptJson(any(), anyString(), anyString(), any()))
                .thenThrow(new IOException("Encryption error"));

        CustomException exception = assertThrows(CustomException.class, () ->
                encryptionDecryptionUtil.encryptObject(new Object(), "key", Object.class));
        assertEquals("ENCRYPTION_ERROR", exception.getCode());
    }

    @Test
    public void testDecryptObject_NullObject() {
        // Test with null object
        Object decryptedObject = encryptionDecryptionUtil.decryptObject(null, "key", Object.class, null);
        assertNull(decryptedObject);
    }

    @Test
    public void testDecryptObject_Success() throws IOException {
        // Mock behavior for successful decryption
        Object objectToDecrypt = new Object();
        objectToDecrypt = (Object) Collections.singletonList(objectToDecrypt);
        RequestInfo requestInfo = new RequestInfo();
        Object decryptedObject = new Object();
        when(encryptionService.decryptJson(any(), any(), anyString(), anyString(), any())).thenReturn(decryptedObject);

        Object result = encryptionDecryptionUtil.decryptObject(objectToDecrypt, "key", Object.class, requestInfo);
        assertNotNull(result);
        assertEquals(decryptedObject, result);
    }

    @Test
    public void testDecryptObject_DecryptionNullError() throws IOException {
        // Mock behavior to return null from decryption service
        when(encryptionService.decryptJson(any(), any(), anyString(), anyString(), any())).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () ->
                encryptionDecryptionUtil.decryptObject(new Object(), "key", Object.class, new RequestInfo()));
        assertEquals("DECRYPTION_NULL_ERROR", exception.getCode());
    }

    @Test
    public void testDecryptObject_ExceptionThrown() throws IOException {
        // Mock behavior to throw an exception
        when(encryptionService.decryptJson(any(), any(), anyString(), anyString(), any()))
                .thenThrow(new IOException("Decryption error"));

        CustomException exception = assertThrows(CustomException.class, () ->
                encryptionDecryptionUtil.decryptObject(new Object(), "key", Object.class, new RequestInfo()));
        assertEquals("DECRYPTION_SERVICE_ERROR", exception.getCode());
    }

    @Test
    public void testIsUserDecryptingForSelf_ExceptionThrownWithCustomException() throws IOException {
        Object objectToDecrypt = createCourtCase();
        User userInfo = User.builder().uuid(UUID.randomUUID().toString()).type("EMPLOYEE").build();

        CustomException exception = assertThrows(CustomException.class, () ->
                encryptionDecryptionUtil.isUserDecryptingForSelf(objectToDecrypt, userInfo));
        assertEquals("DECRYPTION_NOTLIST_ERROR", exception.getCode());
    }

    @Test
    public void testIsUserDecryptingForSelf_NoLitigants() {
        // Test with no litigants
        Object objectToDecrypt = createCourtCase();
        objectToDecrypt = (Object) Collections.singletonList(objectToDecrypt);
        User userInfo = User.builder().uuid(UUID.randomUUID().toString()).type("EMPLOYEE").build();

        boolean result = encryptionDecryptionUtil.isUserDecryptingForSelf(objectToDecrypt, userInfo);
        assertFalse(result);
    }

    @Test
    public void testIsUserDecryptingForSelf_MultipleLitigants() {
        // Test with multiple litigants

        Object objectToDecrypt = createCourtCase().addLitigantsItem(createLitigant(UUID.randomUUID())).addLitigantsItem(createLitigant(UUID.randomUUID()));
        objectToDecrypt = (Object) Collections.singletonList(objectToDecrypt);
        User userInfo = User.builder().uuid(UUID.randomUUID().toString()).type("EMPLOYEE").build();

        boolean result = encryptionDecryptionUtil.isUserDecryptingForSelf(objectToDecrypt, userInfo);
        assertFalse(result);
    }

    @Test
    public void testIsUserDecryptingForSelf_SingleLitigant_MatchingUUID() {
        // Test with single litigant matching userInfo UUID
        UUID userUUID = UUID.randomUUID();
        Object objectToDecrypt = addLitigant(createCourtCase(),userUUID);
        objectToDecrypt = (Object) Collections.singletonList(objectToDecrypt);
        User userInfo = User.builder().uuid(userUUID.toString()).type("EMPLOYEE").build();

        boolean result = encryptionDecryptionUtil.isUserDecryptingForSelf(objectToDecrypt, userInfo);
        assertTrue(result);
    }

    @Test
    public void testIsUserDecryptingForSelf_SingleLitigant_NoMatchUUID() {
        // Test with single litigant not matching userInfo UUID
        UUID userUUID = UUID.randomUUID();
        UUID litigantUUID = UUID.randomUUID();
        Object objectToDecrypt = addLitigant(createCourtCase(),litigantUUID);
        objectToDecrypt = (Object) Collections.singletonList(objectToDecrypt);
        User userInfo = User.builder().uuid(userUUID.toString()).type("EMPLOYEE").build();

        boolean result = encryptionDecryptionUtil.isUserDecryptingForSelf(objectToDecrypt, userInfo);
        assertFalse(result);
    }

    private CourtCase createCourtCase() {
        CourtCase courtCase = new CourtCase();
        courtCase.setLitigants(new ArrayList<>());
        return courtCase;
    }

    private Party createLitigant(UUID uuid){
        Party litigant = new Party();
        litigant.setId(uuid);
        return litigant;
    }

    private CourtCase addLitigant(CourtCase courtCase,UUID uuid){
        List<Party> litigants  = courtCase.getLitigants();
        litigants.add(createLitigant(uuid));
        courtCase.setLitigants(litigants);
        return courtCase;
    }

}
