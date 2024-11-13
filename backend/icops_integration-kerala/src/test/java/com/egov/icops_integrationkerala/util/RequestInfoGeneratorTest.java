package com.egov.icops_integrationkerala.util;

import com.egov.icops_integrationkerala.config.IcopsConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RequestInfoGeneratorTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private IcopsConfiguration configs;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RequestInfoGenerator requestInfoGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateSystemRequestInfo_Success() throws Exception {
        // Arrange
        String username = "testUser";
        String password = "testPass";
        String url = "http://test.com";

        when(configs.getUsername()).thenReturn(username);
        when(configs.getPassword()).thenReturn(password);
        when(configs.getUrl()).thenReturn(url);

        String responseBody = "{\"access_token\":\"testToken\",\"UserRequest\":{\"id\":1,\"userName\":\"testUser\"}}";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);

        when(restTemplate.exchange(
                eq(url),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(responseEntity);

        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(responseBody)).thenReturn(jsonNode);
        when(jsonNode.get("access_token")).thenReturn(jsonNode);
        when(jsonNode.asText()).thenReturn("testToken");
        when(jsonNode.get("UserRequest")).thenReturn(jsonNode);

        User mockUser = new User();
        when(objectMapper.treeToValue(jsonNode, User.class)).thenReturn(mockUser);

        RequestInfo mockRequestInfo = new RequestInfo();
        when(objectMapper.convertValue(any(), eq(RequestInfo.class))).thenReturn(mockRequestInfo);

        // Act
        RequestInfo result = requestInfoGenerator.generateSystemRequestInfo();

        // Assert
        assertNotNull(result);
        verify(restTemplate).exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void generateSystemRequestInfo_Exception() {
        // Arrange
        when(configs.getUsername()).thenReturn("testUser");
        when(configs.getPassword()).thenReturn("testPass");
        when(configs.getUrl()).thenReturn("http://test.com");

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenThrow(new RuntimeException("Test exception"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> requestInfoGenerator.generateSystemRequestInfo());
    }
}