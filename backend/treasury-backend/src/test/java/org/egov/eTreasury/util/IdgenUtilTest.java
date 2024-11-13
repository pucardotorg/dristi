package org.egov.eTreasury.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.idgen.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.eTreasury.config.PaymentConfiguration;
import org.egov.eTreasury.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.egov.eTreasury.config.ServiceConstants.IDGEN_ERROR;
import static org.egov.eTreasury.config.ServiceConstants.NO_IDS_FOUND_ERROR;

class IdgenUtilTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private ServiceRequestRepository restRepo;

    @Mock
    private PaymentConfiguration configs;

    @InjectMocks
    private IdgenUtil idgenUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetIdList_Success() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenant1";
        String idName = "idName";
        String idFormat = "idFormat";
        int count = 2;


        List<IdResponse> idResponses = new ArrayList<>();
        idResponses.add(IdResponse.builder().id("ID1").build());
        idResponses.add(IdResponse.builder().id("ID2").build());

        IdGenerationResponse response = IdGenerationResponse.builder().idResponses(idResponses).build();

        when(configs.getIdGenHost()).thenReturn("http://example.com");
        when(configs.getIdGenPath()).thenReturn("/idgen/v1/_generate");
        when(restRepo.fetchResult(any(), any(IdGenerationRequest.class))).thenReturn(response);
        when(mapper.convertValue(response, IdGenerationResponse.class)).thenReturn(response);

        // Act
        List<String> result = idgenUtil.getIdList(requestInfo, tenantId, idName, idFormat, count);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ID1", result.get(0));
        assertEquals("ID2", result.get(1));
    }

    @Test
    void testGetIdList_NoIdsFound() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenant1";
        String idName = "idName";
        String idFormat = "idFormat";
        Integer count = 2;

        List<IdRequest> reqList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            reqList.add(IdRequest.builder().idName(idName).format(idFormat).tenantId(tenantId).build());
        }

        IdGenerationResponse response = IdGenerationResponse.builder().idResponses(Collections.emptyList()).build();

        when(configs.getIdGenHost()).thenReturn("http://example.com");
        when(configs.getIdGenPath()).thenReturn("/idgen/v1/_generate");
        when(restRepo.fetchResult(any(), any(IdGenerationRequest.class))).thenReturn(response);
        when(mapper.convertValue(response, IdGenerationResponse.class)).thenReturn(response);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            idgenUtil.getIdList(requestInfo, tenantId, idName, idFormat, count);
        });
        assertEquals(IDGEN_ERROR, exception.getCode());
        assertEquals(NO_IDS_FOUND_ERROR, exception.getMessage());
    }
}
