package org.pucar.dristi.util;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.encryption.EncryptionService;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.Party;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class EncryptionDecryptionUtilTest {

    @Mock
    private EncryptionService encryptionService;

    @Value("${state.level.tenant.id}")
    private String stateLevelTenantId = "defaultTenantId";

    @Value("${decryption.abac.enabled}")
    private boolean abacEnabled = false;

    private EncryptionDecryptionUtil encryptionDecryptionUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        encryptionDecryptionUtil = new EncryptionDecryptionUtil(encryptionService, stateLevelTenantId, abacEnabled);
    }

    @Test
    void testEncryptObject_NullObject() {
        // Test with null object
        Object encryptedObject = encryptionDecryptionUtil.encryptObject(null, "key", Object.class);
        assertNull(encryptedObject);
    }

    @Test
    void testEncryptObject_Success() throws IOException {
        // Mock behavior for successful encryption
        Object objectToEncrypt = new Object();
        Object encryptedObject = new Object();
        when(encryptionService.encryptJson(any(), anyString(), anyString(), any())).thenReturn(encryptedObject);

        Object result = encryptionDecryptionUtil.encryptObject(objectToEncrypt, "key", Object.class);
        assertNotNull(result);
        assertEquals(encryptedObject, result);
    }

    @Test
    void testEncryptObject_EncryptionNullError() throws IOException {
        // Mock behavior to return null from encryption service
        when(encryptionService.encryptJson(any(), anyString(), anyString(), any())).thenReturn(null);

        Object object = new Object();
        CustomException exception = assertThrows(CustomException.class, () ->
                encryptionDecryptionUtil.encryptObject(object, "key", Object.class));
        assertEquals("ENCRYPTION_NULL_ERROR", exception.getCode());
    }

    @Test
    void testEncryptObject_ExceptionThrown() throws IOException {
        // Mock behavior to throw an exception
        when(encryptionService.encryptJson(any(), anyString(), anyString(), any()))
                .thenThrow(new IOException("Encryption error"));
        Object object = new Object();
        CustomException exception = assertThrows(CustomException.class, () ->
                    encryptionDecryptionUtil.encryptObject(object, "key", Object.class));

        assertEquals("ENCRYPTION_ERROR", exception.getCode());
    }

    @Test
    void testDecryptObject_NullObject() {
        // Test with null object
        Object decryptedObject = encryptionDecryptionUtil.decryptObject(null, "key", Object.class, null);
        assertNull(decryptedObject);
    }

    @Test
    void testDecryptObject_Success() throws IOException {
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
    void testDecryptObject_DecryptionNullError() throws IOException {
        // Mock behavior to return null from decryption service
        when(encryptionService.decryptJson(any(), any(), anyString(), anyString(), any())).thenReturn(null);

        Object object = new Object();
        RequestInfo requestInfo = new RequestInfo();
        CustomException exception = assertThrows(CustomException.class, () ->
                encryptionDecryptionUtil.decryptObject(object, "key", Object.class, requestInfo));
        assertEquals("DECRYPTION_NULL_ERROR", exception.getCode());
    }

    @Test
    void testDecryptObject_ExceptionThrown() throws IOException {
        // Mock behavior to throw an exception
        when(encryptionService.decryptJson(any(), any(), anyString(), anyString(), any()))
                .thenThrow(new IOException("Decryption error"));
        Object object = new Object();
        RequestInfo requestInfo = new RequestInfo();
        CustomException exception = assertThrows(CustomException.class, () ->
            encryptionDecryptionUtil.decryptObject(object, "key", Object.class, requestInfo));
        assertEquals("DECRYPTION_SERVICE_ERROR", exception.getCode());
    }

    @Test
    void testIsUserDecryptingForSelf_ExceptionThrownWithCustomException() throws CustomException {
        Object objectToDecrypt = createCourtCase();
        User userInfo = User.builder().uuid(UUID.randomUUID().toString()).type("EMPLOYEE").build();

        CustomException exception = assertThrows(CustomException.class, () ->
                encryptionDecryptionUtil.isUserDecryptingForSelf(objectToDecrypt, userInfo));
        assertEquals("DECRYPTION_NOTLIST_ERROR", exception.getCode());
    }

    @Test
    void testIsUserDecryptingForSelf_NoLitigants() {
        // Test with no litigants
        Object objectToDecrypt = createCourtCase();
        objectToDecrypt = (Object) Collections.singletonList(objectToDecrypt);
        User userInfo = User.builder().uuid(UUID.randomUUID().toString()).type("EMPLOYEE").build();

        boolean result = encryptionDecryptionUtil.isUserDecryptingForSelf(objectToDecrypt, userInfo);
        assertFalse(result);
    }

    @Test
    void testIsUserDecryptingForSelf_MultipleLitigants() {
        // Test with multiple litigants

        Object objectToDecrypt = createCourtCase().addLitigantsItem(createLitigant(UUID.randomUUID())).addLitigantsItem(createLitigant(UUID.randomUUID()));
        objectToDecrypt = (Object) Collections.singletonList(objectToDecrypt);
        User userInfo = User.builder().uuid(UUID.randomUUID().toString()).type("EMPLOYEE").build();

        boolean result = encryptionDecryptionUtil.isUserDecryptingForSelf(objectToDecrypt, userInfo);
        assertFalse(result);
    }

    @Test
    void testIsUserDecryptingForSelf_SingleLitigant_MatchingUUID() {
        // Test with single litigant matching userInfo UUID
        UUID userUUID = UUID.randomUUID();
        Object objectToDecrypt = addLitigant(createCourtCase(), userUUID);
        objectToDecrypt = (Object) Collections.singletonList(objectToDecrypt);
        User userInfo = User.builder().uuid(userUUID.toString()).type("EMPLOYEE").build();

        boolean result = encryptionDecryptionUtil.isUserDecryptingForSelf(objectToDecrypt, userInfo);
        assertTrue(result);
    }

    @Test
    void testIsUserDecryptingForSelf_SingleLitigant_NoMatchUUID() {
        // Test with single litigant not matching userInfo UUID
        UUID userUUID = UUID.randomUUID();
        UUID litigantUUID = UUID.randomUUID();
        Object objectToDecrypt = addLitigant(createCourtCase(), litigantUUID);
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

    private Party createLitigant(UUID uuid) {
        Party litigant = new Party();
        litigant.setId(uuid);
        return litigant;
    }

    private CourtCase addLitigant(CourtCase courtCase, UUID uuid) {
        List<Party> litigants = courtCase.getLitigants();
        litigants.add(createLitigant(uuid));
        courtCase.setLitigants(litigants);
        return courtCase;
    }

}
