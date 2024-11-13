package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.IdGenerationRequest;
import org.pucar.dristi.web.models.IdGenerationResponse;
import org.pucar.dristi.web.models.IdResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.pucar.dristi.config.ServiceConstants.IDGEN_ERROR;
import static org.pucar.dristi.config.ServiceConstants.NO_IDS_FOUND_ERROR;

 class IdgenUtilTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private ServiceRequestRepository restRepo;

    @Mock
    private Configuration configs;

    @InjectMocks
    private IdgenUtil idgenUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testGetIdList_Success() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenantId";
        String idName = "idName";
        String idformat = "idformat";
        Integer count = 1;

        List<IdResponse> idResponses = new ArrayList<>();
        idResponses.add(IdResponse.builder().id("ID1").build());

        IdGenerationResponse idGenerationResponse = new IdGenerationResponse();
        idGenerationResponse.setIdResponses(idResponses);

        doReturn("http://idgen-host").when(configs).getIdGenHost();
        doReturn("/idgen-path").when(configs).getIdGenPath();
        doReturn(Collections.singletonMap("dummyKey", "dummyValue")).when(restRepo).fetchResult(any(StringBuilder.class), any(IdGenerationRequest.class));
        doReturn(idGenerationResponse).when(mapper).convertValue(any(), any(Class.class));

        // Act
        List<String> result = idgenUtil.getIdList(requestInfo, tenantId, idName, idformat, count,true);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ID1", result.get(0));
    }

    @Test
     void testGetIdList_NoIdsFound() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenantId";
        String idName = "idName";
        String idformat = "idformat";
        Integer count = 1;

        IdGenerationResponse idGenerationResponse = new IdGenerationResponse();
        idGenerationResponse.setIdResponses(Collections.emptyList());

        doReturn("http://idgen-host").when(configs).getIdGenHost();
        doReturn("/idgen-path").when(configs).getIdGenPath();
        doReturn(Collections.singletonMap("dummyKey", "dummyValue")).when(restRepo).fetchResult(any(StringBuilder.class), any(IdGenerationRequest.class));
        doReturn(idGenerationResponse).when(mapper).convertValue(any(), any(Class.class));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            idgenUtil.getIdList(requestInfo, tenantId, idName, idformat, count,true);
        });

        assertEquals(IDGEN_ERROR, exception.getCode());
        assertEquals(NO_IDS_FOUND_ERROR, exception.getMessage());
    }

    @Test
     void testGetIdList_Exception() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenantId";
        String idName = "idName";
        String idformat = "idformat";
        Integer count = 1;

        doReturn("http://idgen-host").when(configs).getIdGenHost();
        doReturn("/idgen-path").when(configs).getIdGenPath();
        doThrow(new RuntimeException("Error")).when(restRepo).fetchResult(any(StringBuilder.class), any(IdGenerationRequest.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            idgenUtil.getIdList(requestInfo, tenantId, idName, idformat, count,true);
        });
    }
}