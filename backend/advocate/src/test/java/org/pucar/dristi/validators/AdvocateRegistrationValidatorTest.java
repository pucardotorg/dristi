package org.pucar.dristi.validators;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.repository.AdvocateRepository;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateRequest;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class AdvocateRegistrationValidatorTest {

    @InjectMocks
    private AdvocateRegistrationValidator validator;

    @Mock
    private IndividualService individualService;

    @Mock
    private AdvocateRepository repository;

    private AdvocateRequest advocateRequest;
    private RequestInfo requestInfo;

    @BeforeEach
    void setUp() {
        advocateRequest = new AdvocateRequest();
        requestInfo = new RequestInfo();
        requestInfo.setUserInfo(new User());
        advocateRequest.setRequestInfo(requestInfo);
    }

    @Test
    void validateAdvocateRegistration_ValidAdvocate_NoExceptionThrown() {
        Advocate advocate = new Advocate();
        advocate.setIndividualId("validIndividualId");
        advocate.setTenantId("validTenantId");
        advocateRequest.setAdvocate(advocate);

        when(individualService.searchIndividual(requestInfo, "validIndividualId", new HashMap<>())).thenReturn(true);

        assertDoesNotThrow(() -> validator.validateAdvocateRegistration(advocateRequest));
    }

    @Test
    void validateAdvocateRegistration_InvalidIndividualId_ThrowsIllegalArgumentException() {
        Advocate advocate = new Advocate();
        advocate.setIndividualId("invalidIndividualId");
        advocate.setTenantId("validTenantId");
        advocateRequest.setAdvocate(advocate);

        when(individualService.searchIndividual(requestInfo, "invalidIndividualId", new HashMap<>())).thenReturn(false);

        assertThrows(CustomException.class, () -> validator.validateAdvocateRegistration(advocateRequest));
    }

    @Test
    void validateAdvocateRegistration_MissingTenantId_ThrowsCustomException() {
        Advocate advocate = new Advocate();
        advocate.setIndividualId("validIndividualId");
        // Missing tenantId intentionally
        advocateRequest.setAdvocate(advocate);


        assertThrows(CustomException.class, () -> validator.validateAdvocateRegistration(advocateRequest));
    }

    @Test
    void validateAdvocateRegistration_MissingUserInfo_ThrowsCustomException() {
        Advocate advocate = new Advocate();
        advocate.setIndividualId("validIndividualId");
        // Missing UserInfos intentionally
        advocateRequest.getRequestInfo().setUserInfo(null);
        advocateRequest.setAdvocate(advocate);


        assertThrows(CustomException.class, () -> validator.validateAdvocateRegistration(advocateRequest));
    }

    @Test
    void validateApplicationExistence_ApplicationExists() {
        // Arrange
        Advocate advocate = new Advocate();
        advocate.setApplicationNumber("testAppNumber");
        advocate.setTenantId("testTenantId");
        List<Advocate> advocates = new ArrayList<>();
        advocates.add(advocate);
        List<AdvocateSearchCriteria> existingApplications = new ArrayList<>();
        AdvocateSearchCriteria advocateSearchCriteria = new AdvocateSearchCriteria();
        advocateSearchCriteria.setApplicationNumber("appNumber");
        advocateSearchCriteria.setResponseList(advocates);

        existingApplications.add(advocateSearchCriteria);
        when(repository.getAdvocates(anyList(), any(), anyInt(), anyInt())).thenReturn(existingApplications);

        // Act
        Advocate result = validator.validateApplicationExistence(advocate);

        // Assert
        assertNotNull(result);
        assertEquals(advocate, result);
    }

    @Test
    void validateApplicationExistence_ApplicationDoesNotExist() {
        // Arrange
        Advocate advocate = new Advocate();
        advocate.setApplicationNumber("testAppNumber");
        advocate.setTenantId("testTenantId");
        List<Advocate> advocates = new ArrayList<>();
        List<AdvocateSearchCriteria> existingApplications = new ArrayList<>();
        AdvocateSearchCriteria advocateSearchCriteria = new AdvocateSearchCriteria();
        advocateSearchCriteria.setApplicationNumber("appNumber");
        advocateSearchCriteria.setResponseList(advocates);

        existingApplications.add(advocateSearchCriteria);

        when(repository.getAdvocates(anyList(), any(), anyInt(), anyInt())).thenReturn(existingApplications);

        // Act + Assert
        assertThrows(CustomException.class, () -> validator.validateApplicationExistence(advocate));
    }
}
