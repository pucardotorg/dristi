//package org.pucar.dristi.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.egov.common.contract.request.RequestInfo;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.pucar.dristi.util.CaseConvertClass;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//class CaseEncryptionServiceImplTest {
//
//    @Mock
//    private CaseEncryptionServiceRestConnection encryptionServiceRestConnection;
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @InjectMocks
//    private CaseEncryptionServiceImpl caseEncryptionServiceImpl;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testEncryptJson_Success() throws IOException {
//        // Mock CaseEncryptionServiceRestConnection
//        CaseEncryptionServiceRestConnection mockConnection = Mockito.mock(CaseEncryptionServiceRestConnection.class);
//        Mockito.when(mockConnection.callEncrypt(anyString(), anyString(), anyList())).thenReturn("encrypted_data");
//
//        // Set up service with mock connection
//        CaseEncryptionServiceImpl service = new CaseEncryptionServiceImpl(mockConnection, new ObjectMapper());
//
//        // Sample JSON object
//        Object plainTextJson = new HashMap<>();
//        ((Map) plainTextJson).put("key", "value");
//
//        // Expected encrypted value
//        String expectedEncryptedValue = "encrypted_data";
//
//        // Call encryptJson
//        Object encryptedValue = service.encryptJson(plainTextJson, "model", "tenantId", String.class);
//
//        JsonNode encryptedJsonNode = mock(JsonNode.class);
//        // Assert converted value matches expected
//        String convertedValue = CaseConvertClass.convertTo(encryptedJsonNode, String.class);
//        assertEquals(expectedEncryptedValue, convertedValue);
//    }
//
//    @Test
//    void testEncryptJson() throws IOException {
//        // Arrange
//        String model = "someModel";
//        String tenantId = "someTenantId";
//        Object plaintextJson = new MyPlainTextJson();
//        JsonNode encryptedJsonNode = mock(JsonNode.class);
//        MyEncryptedJson expectedEncryptedJson = new MyEncryptedJson();
//
//        // Mocking the inherited encryptJson method
//        CaseEncryptionServiceImpl spyService = spy(caseEncryptionServiceImpl);
//        doReturn(encryptedJsonNode).when(spyService).encryptJson(any(Object.class), eq(model), eq(tenantId));
//
//        // Mock CaseConvertClass.convertTo to return the expectedEncryptedJson
//        when(CaseConvertClass.convertTo(encryptedJsonNode, MyEncryptedJson.class))
//                .thenReturn(expectedEncryptedJson);
//
//        // Act
//        MyEncryptedJson result = spyService.encryptJson(plaintextJson, model, tenantId, MyEncryptedJson.class);
//
//        // Assert
//        assertEquals(expectedEncryptedJson, result);
//    }
//
//    @Test
//    void testDecryptJson() throws IOException {
//        // Arrange
//        RequestInfo requestInfo = new RequestInfo();
//        String model = "someModel";
//        String purpose = "somePurpose";
//        JsonNode ciphertextJsonNode = mock(JsonNode.class);
//        JsonNode decryptedJsonNode = mock(JsonNode.class);
//        MyPlainTextJson expectedPlainTextJson = new MyPlainTextJson();
//
//        // Mocking the inherited decryptJson method
//        CaseEncryptionServiceImpl spyService = spy(caseEncryptionServiceImpl);
//        doReturn(decryptedJsonNode).when(spyService).decryptJson(eq(requestInfo), eq(ciphertextJsonNode), eq(model), eq(purpose));
//
//        // Mock CaseConvertClass.convertTo to return the expectedPlainTextJson
//        when(CaseConvertClass.convertTo(decryptedJsonNode, MyPlainTextJson.class))
//                .thenReturn(expectedPlainTextJson);
//
//        // Act
//        MyPlainTextJson result = spyService.decryptJson(requestInfo, ciphertextJsonNode, model, purpose, MyPlainTextJson.class);
//
//        // Assert
//        assertEquals(expectedPlainTextJson, result);
//    }
//
//    @Test
//    void testEncryptValue() throws IOException {
//        // Arrange
//        List<Object> plaintext = Collections.singletonList("plaintext");
//        String tenantId = "someTenantId";
//        String type = "someType";
//        List<String> expectedEncryptedValue = Collections.singletonList("encryptedValue");
//
//        when(encryptionServiceRestConnection.callEncrypt(eq(tenantId), eq(type), eq(plaintext)))
//                .thenReturn(plaintext);  // Mock the encryption response
//
//        // Convert the encryption response to JsonNode using ObjectMapper
//        JsonNode encryptionResponseNode = objectMapper.valueToTree(plaintext);
//
//        // Mock CaseConvertClass.convertTo to return the expected encrypted value
//        when(CaseConvertClass.convertTo(encryptionResponseNode, List.class))
//                .thenReturn(expectedEncryptedValue);
//
//        // Act
//        List<String> result = caseEncryptionServiceImpl.encryptValue(plaintext, tenantId, type);
//
//        // Assert
//        assertEquals(expectedEncryptedValue, result);
//    }
//
//    // Define custom classes for the test (replace with actual classes)
//    static class MyPlainTextJson { /* Your fields here */ }
//    static class MyEncryptedJson { /* Your fields here */ }
//}
