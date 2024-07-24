package org.pucar.dristi.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.models.individual.IndividualResponse;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IndividualUtil;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CourtCase;

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
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(new CourtCase());
        caseRequest.setRequestInfo(RequestInfo.builder().userInfo(User.builder().tenantId("pg").build()).build()); // Mock or provide necessary data

        // Mock the behavior of dependent components
        IndividualResponse individualResponse = new IndividualResponse(); // Mock or provide necessary data
        when(configuration.getIndividualHost()).thenReturn("http://example.com");
        when(configuration.getIndividualSearchEndpoint()).thenReturn("/search");
        when(individualUtil.individualCall(any(), any())).thenReturn(true);

        // Call the method under test
        Boolean result = individualService.searchIndividual(caseRequest.getRequestInfo(),"123");

        // Assertions
        assertNotNull(result);
    }

    @Test
    void testSearchIndividual_CustomException() {
        // Create test data
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(new CourtCase());

        when(configuration.getIndividualHost()).thenThrow(new CustomException());

        assertThrows(CustomException.class, () -> {
            individualService.searchIndividual(caseRequest.getRequestInfo(),"123");
        });
    }

    @Test
    void testSearchIndividual_Exception() {
        // Create test data
        CaseRequest caseRequest = new CaseRequest();
        caseRequest.setCases(new CourtCase());
        caseRequest.setRequestInfo(null); // Mock or provide necessary data

        assertThrows(Exception.class, () -> {
            individualService.searchIndividual(caseRequest.getRequestInfo(),"123");
        });
    }
}