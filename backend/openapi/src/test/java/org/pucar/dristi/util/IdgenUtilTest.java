package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.idgen.IdGenerationRequest;
import org.egov.common.contract.idgen.IdGenerationResponse;
import org.egov.common.contract.idgen.IdRequest;
import org.egov.common.contract.idgen.IdResponse;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IdgenUtilTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private ServiceRequestRepository restRepo;

    @Mock
    private Configuration configs;

    @InjectMocks
    private IdgenUtil idgenUtil;

    private RequestInfo mockRequestInfo;
    private IdGenerationResponse mockIdGenerationResponse;

    @BeforeEach
    public void setup() {
        // Setup mock RequestInfo
        mockRequestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("test-uuid");
        userInfo.setId(1L);
        mockRequestInfo.setUserInfo(userInfo);

        // Setup mock ID Generation Response
        mockIdGenerationResponse = new IdGenerationResponse();
        mockIdGenerationResponse.setResponseInfo(new ResponseInfo());
    }

    @Test
    public void testGetIdList_SingleId_Success() {
        // Arrange
        String tenantId = "test-tenant";
        String idName = "test-id-name";
        String idFormat = "test-format";
        Integer count = 1;

        // Create mock ID responses
        List<IdResponse> idResponses = new ArrayList<>();
        IdResponse idResponse = new IdResponse();
        idResponse.setId("generated-id-1");
        idResponses.add(idResponse);
        mockIdGenerationResponse.setIdResponses(idResponses);

        // Mock configuration
        when(configs.getIdGenHost()).thenReturn("http://idgen-host");
        when(configs.getIdGenPath()).thenReturn("/generate");

        // Mock repository call
        when(restRepo.fetchResult(any(StringBuilder.class), any(IdGenerationRequest.class)))
                .thenReturn(mockIdGenerationResponse);

        // Mock mapping
        when(mapper.convertValue(any(), eq(IdGenerationResponse.class)))
                .thenReturn(mockIdGenerationResponse);

        // Act
        List<String> generatedIds = idgenUtil.getIdList(mockRequestInfo, tenantId, idName, idFormat, count);

        // Assert
        assertNotNull(generatedIds);
        assertEquals(1, generatedIds.size());
        assertEquals("generated-id-1", generatedIds.get(0));

        // Verify interactions
        verify(restRepo).fetchResult(any(StringBuilder.class), any(IdGenerationRequest.class));
        verify(mapper).convertValue(any(), eq(IdGenerationResponse.class));
    }

    @Test
    public void testGetIdList_MultipleIds_Success() {
        // Arrange
        String tenantId = "test-tenant";
        String idName = "test-id-name";
        String idFormat = "test-format";
        Integer count = 3;

        // Create mock ID responses
        List<IdResponse> idResponses = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            IdResponse idResponse = new IdResponse();
            idResponse.setId("generated-id-" + i);
            idResponses.add(idResponse);
        }
        mockIdGenerationResponse.setIdResponses(idResponses);

        // Mock configuration
        when(configs.getIdGenHost()).thenReturn("http://idgen-host");
        when(configs.getIdGenPath()).thenReturn("/generate");

        // Mock repository call
        when(restRepo.fetchResult(any(StringBuilder.class), any(IdGenerationRequest.class)))
                .thenReturn(mockIdGenerationResponse);

        // Mock mapping
        when(mapper.convertValue(any(), eq(IdGenerationResponse.class)))
                .thenReturn(mockIdGenerationResponse);

        // Act
        List<String> generatedIds = idgenUtil.getIdList(mockRequestInfo, tenantId, idName, idFormat, count);

        // Assert
        assertNotNull(generatedIds);
        assertEquals(count, generatedIds.size());
        assertEquals("generated-id-1", generatedIds.get(0));
        assertEquals("generated-id-2", generatedIds.get(1));
        assertEquals("generated-id-3", generatedIds.get(2));

        // Verify interactions
        verify(restRepo).fetchResult(any(StringBuilder.class), any(IdGenerationRequest.class));
        verify(mapper).convertValue(any(), eq(IdGenerationResponse.class));
    }

    @Test
    public void testGetIdList_NoIdsGenerated_ThrowsCustomException() {
        // Arrange
        String tenantId = "test-tenant";
        String idName = "test-id-name";
        String idFormat = "test-format";
        Integer count = 1;

        // Create empty ID responses
        mockIdGenerationResponse.setIdResponses(new ArrayList<>());

        // Mock configuration
        when(configs.getIdGenHost()).thenReturn("http://idgen-host");
        when(configs.getIdGenPath()).thenReturn("/generate");

        // Mock repository call
        when(restRepo.fetchResult(any(StringBuilder.class), any(IdGenerationRequest.class)))
                .thenReturn(mockIdGenerationResponse);

        // Mock mapping
        when(mapper.convertValue(any(), eq(IdGenerationResponse.class)))
                .thenReturn(mockIdGenerationResponse);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            idgenUtil.getIdList(mockRequestInfo, tenantId, idName, idFormat, count);
        });

        // Verify error details
        assertEquals("IDGEN ERROR", exception.getCode());
        assertEquals("No ids returned from idgen Service", exception.getMessage());

        // Verify interactions
        verify(restRepo).fetchResult(any(StringBuilder.class), any(IdGenerationRequest.class));
        verify(mapper).convertValue(any(), eq(IdGenerationResponse.class));
    }

    @Test
    public void testGetIdList_VerifyIdRequestParameters() {
        // Arrange
        String tenantId = "test-tenant";
        String idName = "test-id-name";
        String idFormat = "test-format";
        Integer count = 2;

        // Create mock ID responses
        List<IdResponse> idResponses = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            IdResponse idResponse = new IdResponse();
            idResponse.setId("generated-id-" + i);
            idResponses.add(idResponse);
        }
        mockIdGenerationResponse.setIdResponses(idResponses);

        // Mock configuration
        when(configs.getIdGenHost()).thenReturn("http://idgen-host");
        when(configs.getIdGenPath()).thenReturn("/generate");

        // Mock repository call
        when(restRepo.fetchResult(any(StringBuilder.class), any(IdGenerationRequest.class)))
                .thenAnswer(invocation -> {
                    // Verify the IdGenerationRequest
                    IdGenerationRequest request = invocation.getArgument(1);

                    // Assert request details
                    assertNotNull(request);
                    assertNotNull(request.getIdRequests());
                    assertEquals(count, request.getIdRequests().size());

                    // Check each IdRequest
                    for (IdRequest idRequest : request.getIdRequests()) {
                        assertEquals(tenantId, idRequest.getTenantId());
                        assertEquals(idName, idRequest.getIdName());
                        assertEquals(idFormat, idRequest.getFormat());
                    }

                    return mockIdGenerationResponse;
                });

        // Mock mapping
        when(mapper.convertValue(any(), eq(IdGenerationResponse.class)))
                .thenReturn(mockIdGenerationResponse);

        // Act
        List<String> generatedIds = idgenUtil.getIdList(mockRequestInfo, tenantId, idName, idFormat, count);

        // Assert
        assertNotNull(generatedIds);
        assertEquals(count, generatedIds.size());

        // Verify interactions
        verify(restRepo).fetchResult(any(StringBuilder.class), any(IdGenerationRequest.class));
    }

    @Test
    public void testGetIdList_EmptyTenantId_ThrowsException() {
        String tenantId = "";
        assertThrows(NullPointerException.class, () ->
                idgenUtil.getIdList(mockRequestInfo, tenantId, "test-id", "test-format", 1));
    }

}