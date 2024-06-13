package org.pucar.dristi.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.HearingRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.HearingRepository;
import org.pucar.dristi.validator.HearingRegistrationValidator;
import org.pucar.dristi.web.models.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HearingServiceTest {

    @Mock
    private HearingRegistrationValidator validator;

    @Mock
    private HearingRegistrationEnrichment enrichmentUtil;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private IndividualService individualService;

    @Mock
    private HearingRepository hearingRepository;

    @Mock
    private Producer producer;

    @Mock
    private Configuration config;

    @InjectMocks
    private HearingService hearingService;

    @Test
    void testCreateHearing_Success() {
        // Arrange
        HearingRequest hearingRequest = new HearingRequest();
        Hearing hearing = new Hearing();
        hearingRequest.setHearing(hearing);

        // Mock validator and enrichmentUtil behaviors
        doNothing().when(validator).validateHearingRegistration(hearingRequest);
        doNothing().when(enrichmentUtil).enrichHearingRegistration(hearingRequest);
        doNothing().when(workflowService).updateWorkflowStatus(hearingRequest);
        when(config.getHearingCreateTopic()).thenReturn("createTopic");

        // Act
        Hearing createdHearing = hearingService.createHearing(hearingRequest);

        // Assert
        assertNotNull(createdHearing);
        assertEquals(hearing, createdHearing);
        verify(producer).push("createTopic", hearingRequest);
    }

    @Test
    void testCreateHearing_CustomException() {
        // Arrange
        HearingRequest hearingRequest = new HearingRequest();

        // Mock validator behavior to throw CustomException
        doThrow(new CustomException("Validation failed","Throw custom exception")).when(validator).validateHearingRegistration(hearingRequest);

        // Act & Assert
        assertThrows(CustomException.class, () -> hearingService.createHearing(hearingRequest));
    }

    @Test
    void testSearchHearing_Success() {
        // Arrange
        when(hearingRepository.getHearings(anyString(), anyString(), anyString(), anyString(), anyString(), any(LocalDate.class), any(LocalDate.class), anyInt(), anyInt(), anyString()))
                .thenReturn(Collections.singletonList(new Hearing()));

        // Act
        List<Hearing> hearingList = hearingService.searchHearing("cnrNumber", "applicationNumber", "hearingId", "fightingNumber", "tenentId", LocalDate.now(), LocalDate.now(), 10, 0, "DESC");

        // Assert
        assertNotNull(hearingList);
        assertFalse(hearingList.isEmpty());
    }

    @Test
    void testSearchHearing_CustomException() {
        // Arrange
        when(hearingRepository.getHearings(anyString(), anyString(), anyString(), anyString(), anyString(), any(LocalDate.class), any(LocalDate.class), anyInt(), anyInt(), anyString()))
                .thenThrow(new CustomException("Search failed","Throw custom exception"));

        // Act & Assert
        assertThrows(CustomException.class, () -> hearingService.searchHearing("cnrNumber", "applicationNumber", "hearingId", "fightingNumber", "tenentId", LocalDate.now(), LocalDate.now(), 10, 0, "DESC"));
    }

    @Test
    void testUpdateHearing_Success() {
        // Arrange
        HearingRequest hearingRequest = new HearingRequest();
        RequestInfo requestInfo = new RequestInfo();
        Hearing hearing = new Hearing();
        hearingRequest.setHearing(hearing);
        hearingRequest.setRequestInfo(requestInfo);

        // Mock validator and workflowService behaviors
        when(validator.validateHearingExistence(requestInfo,hearing)).thenReturn(hearing);
        doNothing().when(workflowService).updateWorkflowStatus(hearingRequest);
        when(config.getHearingUpdateTopic()).thenReturn("updateTopic");

        // Act
        Hearing updatedHearing = hearingService.updateHearing(hearingRequest);

        // Assert
        assertNotNull(updatedHearing);
        assertEquals(hearing, updatedHearing);
        verify(producer).push("updateTopic", hearingRequest);
    }

    @Test
    void testUpdateHearing_CustomException() {
        // Arrange
        HearingRequest hearingRequest = new HearingRequest();

        // Mock validator behavior to throw CustomException
        when(validator.validateHearingExistence(any(),any())).thenThrow(new CustomException("Hearing not found","throw custom exception"));

        // Act & Assert
        assertThrows(CustomException.class, () -> hearingService.updateHearing(hearingRequest));
    }

    @Test
    void testIsHearingExist_Success() {
        // Arrange
        HearingExistsRequest hearingExistsRequest = new HearingExistsRequest();
        HearingExists hearingExists = new HearingExists();
        hearingExists.setHearingId("HearingId1");
        hearingExistsRequest.setOrder(hearingExists);
        User userInfo = new User();
        userInfo.setTenantId("tenantId");
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(userInfo);
        hearingExistsRequest.setRequestInfo(requestInfo);
        when(hearingRepository.getHearings(any(), any(), any(), any(), any(), isNull(), isNull(), any(), any(), any()))
                .thenReturn(Collections.singletonList(new Hearing()));

        // Act
        HearingExists exists = hearingService.isHearingExist(hearingExistsRequest);

        // Assert
        assertNotNull(exists);
        assertTrue(exists.getExists());
    }

    @Test
    void testIsHearingExist_NotExist() {
        // Arrange
        HearingExistsRequest hearingExistsRequest = new HearingExistsRequest();
        HearingExists hearingExists = new HearingExists();
        hearingExists.setHearingId("HearingId1");
        hearingExistsRequest.setOrder(hearingExists);
        User userInfo = new User();
        userInfo.setTenantId("tenantId");
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(userInfo);
        hearingExistsRequest.setRequestInfo(requestInfo);
        when(hearingRepository.getHearings(any(), any(), any(), any(), any(), isNull(), isNull(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        // Act
        HearingExists exists = hearingService.isHearingExist(hearingExistsRequest);

        // Assert
        assertNotNull(exists);
        assertFalse(exists.getExists());
    }
}
