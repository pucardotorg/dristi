package org.pucar.dristi.service;

import org.egov.common.contract.request.User;
import org.egov.common.models.individual.Individual;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IndividualUtil;
import org.pucar.dristi.web.models.IndividualSearch;
import org.pucar.dristi.web.models.IndividualSearchRequest;

import org.egov.common.contract.request.RequestInfo;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IndividualServiceTest {

    @Mock
    private IndividualUtil individualUtil;

    @Mock
    private Configuration config;

    @InjectMocks
    private IndividualService individualService;

    @Test
    void testSearchIndividual_Success() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        requestInfo.setUserInfo(user);
        requestInfo.getUserInfo().setTenantId("tenantId");
        String individualId = "individualId";
        Map<String, String> individualUserUUID = new HashMap<>();
        IndividualSearchRequest individualSearchRequest = new IndividualSearchRequest();
        IndividualSearch individualSearch = new IndividualSearch();
        individualSearch.setIndividualId(individualId);
        individualSearchRequest.setRequestInfo(requestInfo);
        individualSearchRequest.setIndividual(individualSearch);

        when(config.getIndividualHost()).thenReturn("individualHost");
        when(config.getIndividualSearchEndpoint()).thenReturn("searchEndpoint");
        when(individualUtil.individualCall(any(), any(), any()))
                .thenReturn(true);

        // Act
        Boolean result = individualService.searchIndividual(requestInfo, individualId, individualUserUUID);

        // Assert
        assertTrue(result);
        verify(individualUtil).individualCall(any(), any(), any());
    }

    @Test
    void testSearchIndividual_Exception() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        requestInfo.setUserInfo(user);
        requestInfo.getUserInfo().setTenantId("tenantId");
        String individualId = "individualId";
        Map<String, String> individualUserUUID = new HashMap<>();
        IndividualSearchRequest individualSearchRequest = new IndividualSearchRequest();
        IndividualSearch individualSearch = new IndividualSearch();
        individualSearch.setIndividualId(individualId);
        individualSearchRequest.setRequestInfo(requestInfo);
        individualSearchRequest.setIndividual(individualSearch);

        when(config.getIndividualHost()).thenReturn("individualHost");
        when(config.getIndividualSearchEndpoint()).thenReturn("searchEndpoint");
        when(individualUtil.individualCall(individualSearchRequest, new StringBuilder("individualHost").append("searchEndpoint").append("?limit=1000").append("&offset=0").append("&tenantId=tenantId").append("&includeDeleted=true"), individualUserUUID))
                .thenThrow(new RuntimeException("Error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> individualService.searchIndividual(requestInfo, individualId, individualUserUUID));
    }

    @Test
    void testGetIndividuals_Success() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        requestInfo.setUserInfo(user);
        requestInfo.getUserInfo().setTenantId("tenantId");
        List<String> uuids = new ArrayList<>();
        uuids.add("uuid1");
        IndividualSearchRequest individualSearchRequest = new IndividualSearchRequest();
        IndividualSearch individualSearch = new IndividualSearch();
        individualSearch.setUserUuid(uuids);
        individualSearchRequest.setRequestInfo(requestInfo);
        individualSearchRequest.setIndividual(individualSearch);

        when(config.getIndividualHost()).thenReturn("individualHost");
        when(config.getIndividualSearchEndpoint()).thenReturn("searchEndpoint");
        when(individualUtil.getIndividualByIndividualId(any(), any()))
                .thenReturn(Collections.singletonList(new Individual()));

        // Act
        List<Individual> result = individualService.getIndividuals(requestInfo, uuids);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(individualUtil).getIndividualByIndividualId(any(), any());
    }

    @Test
    void testGetIndividuals_Exception() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        requestInfo.setUserInfo(user);
        requestInfo.getUserInfo().setTenantId("tenantId");
        List<String> uuids = new ArrayList<>();
        uuids.add("uuid1");
        IndividualSearchRequest individualSearchRequest = new IndividualSearchRequest();
        IndividualSearch individualSearch = new IndividualSearch();
        individualSearch.setUserUuid(uuids);
        individualSearchRequest.setRequestInfo(requestInfo);
        individualSearchRequest.setIndividual(individualSearch);

        when(config.getIndividualHost()).thenReturn("individualHost");
        when(config.getIndividualSearchEndpoint()).thenReturn("searchEndpoint");
        when(individualUtil.getIndividualByIndividualId(any(), any()))
                .thenReturn(null);

        // Act
        List<Individual> result = individualService.getIndividuals(requestInfo, uuids);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(individualUtil).getIndividualByIndividualId(any(), any());
    }
}
