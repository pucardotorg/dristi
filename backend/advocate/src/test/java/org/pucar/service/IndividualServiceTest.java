package org.pucar.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.models.individual.IndividualResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.config.Configuration;
import org.pucar.util.IndividualUtil;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IndividualServiceTest {

    @Mock
    IndividualUtil individualUtil;

    @Mock
    Configuration configuration;

    @InjectMocks
    IndividualService individualService;


    @Test
    void testSearchIndividual() {
        // Create test data
        AdvocateRequest advocateRequest = new AdvocateRequest();
        advocateRequest.setAdvocates(new ArrayList<>());
        advocateRequest.setRequestInfo(RequestInfo.builder().userInfo(User.builder().tenantId("pg").build()).build()); // Mock or provide necessary data
        Advocate advocate = new Advocate();
        advocate.setIndividualId("IND-2024-04-15-000023");
        advocate.setTenantId("pg");
        advocateRequest.getAdvocates().add(advocate);
        Map<String, String> individualUserUUID = new HashMap<>();

        // Mock the behavior of dependent components
        IndividualResponse individualResponse = new IndividualResponse(); // Mock or provide necessary data
        when(configuration.getIndividualHost()).thenReturn("http://example.com");
        when(configuration.getIndividualSearchEndpoint()).thenReturn("/search");
        when(individualUtil.individualCall(any(), any(), any())).thenReturn(true);

        // Call the method under test
        Boolean result = individualService.searchIndividual(advocateRequest.getRequestInfo(),advocateRequest.getAdvocates().get(0).getIndividualId(), new HashMap<>());

        // Assertions
        assertNotNull(result);
    }
}
