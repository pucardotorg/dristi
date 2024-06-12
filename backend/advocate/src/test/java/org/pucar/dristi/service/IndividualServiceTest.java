package org.pucar.dristi.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IndividualUtil;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateClerkRequest;
import org.pucar.dristi.web.models.AdvocateRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IndividualServiceTest {

    @Mock
    IndividualUtil individualUtil;

    @Mock
    Configuration configuration;

    @Mock
    private  IndividualUtil individualUtils;

    @InjectMocks
    IndividualService individualService;


    @Test
    void testSearchIndividual() {
        // Create test data
        AdvocateRequest advocateRequest = new AdvocateRequest();
        advocateRequest.setAdvocate(new Advocate());
        advocateRequest.setRequestInfo(RequestInfo.builder().userInfo(User.builder().tenantId("pg").build()).build()); // Mock or provide necessary data
        Advocate advocate = new Advocate();
        advocate.setIndividualId("IND-2024-04-15-000023");
        advocate.setTenantId("pg");
        advocateRequest.setAdvocate(advocate);
        Map<String, String> individualUserUUID = new HashMap<>();

        // Mock the behavior of dependent components
        when(configuration.getIndividualHost()).thenReturn("http://example.com");
        when(configuration.getIndividualSearchEndpoint()).thenReturn("/search");
//        when(individualUtil.individualCall(any(), any(), any())).thenReturn(true);

        // Call the method under test
        Boolean result = individualService.searchIndividual(advocateRequest.getRequestInfo(),advocateRequest.getAdvocate().getIndividualId(), new HashMap<>());

        // Assertions
        assertNotNull(result);
    }

    @Test
    public void testSearchIndividual_Exception() {
        // Arrange
        AdvocateRequest advocateRequest = new AdvocateRequest();
        advocateRequest.setAdvocate(new Advocate());
        advocateRequest.setRequestInfo(RequestInfo.builder().userInfo(User.builder().tenantId("pg").build()).build()); // Mock or provide necessary data
        Advocate advocate = new Advocate();
        advocate.setIndividualId("IND-2024-04-15-000023");
        advocate.setTenantId("pg");
        advocateRequest.setAdvocate(advocate);
        Map<String, String> individualUserUUID = new HashMap<>();

        // Act and Assert
        assertThrows(Exception.class, () -> {
            individualService.searchIndividual(advocateRequest.getRequestInfo(),advocateRequest.getAdvocate().getIndividualId(), new HashMap<>());
        });
    }
}
