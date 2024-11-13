package org.pucar.dristi.service;

import org.egov.common.contract.models.AuditDetails;
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
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.HEARING_UPDATE_EXCEPTION;

@ExtendWith(MockitoExtension.class)
public class HearingServiceTest {

    @Mock
    private HearingRegistrationValidator validator;

    @Mock
    private HearingRegistrationEnrichment enrichmentUtil;

    @Mock
    private WorkflowService workflowService;

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
        HearingCriteria criteria = HearingCriteria.builder()
                .hearingId("hearingId")
                .applicationNumber("applicationNumber")
                .cnrNumber("cnrNumber")
                .filingNumber("filingNumber")
                .tenantId("tenantId")
                .fromDate(LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC))
                .toDate(LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC))
                .build();

        User user = new User();
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(user);
        HearingSearchRequest request = HearingSearchRequest.builder()
                .requestInfo(requestInfo)
                .criteria(criteria)
                .build();

        // Arrange
        when(hearingRepository.getHearings(any(HearingSearchRequest.class))).thenReturn(Collections.singletonList(new Hearing()));

        // Act
        List<Hearing> hearingList = hearingService.searchHearing(request);
        // Assert
        assertNotNull(hearingList);
        assertFalse(hearingList.isEmpty());
    }

    @Test
    void testSearchHearing_CustomException() {
        HearingCriteria criteria = HearingCriteria.builder()
                .hearingId("hearingId")
                .applicationNumber("applicationNumber")
                .cnrNumber("cnrNumber")
                .filingNumber("filingNumber")
                .tenantId("tenantId")
                .fromDate(LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC))
                .toDate(LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC))
                .build();

        User user = new User();
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(user);
        HearingSearchRequest request = HearingSearchRequest.builder()
                .requestInfo(requestInfo)
                .criteria(criteria)
                .build();
        // Arrange
        when(hearingRepository.getHearings(any(HearingSearchRequest.class)))
                .thenThrow(new CustomException("Search failed","Throw custom exception"));

        // Act & Assert
        assertThrows(CustomException.class, () -> hearingService.searchHearing(request));
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
        when(hearingRepository.getHearings(any()))
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
        when(hearingRepository.getHearings(any()))
                .thenReturn(Collections.emptyList());

        // Act
        HearingExists exists = hearingService.isHearingExist(hearingExistsRequest);

        // Assert
        assertNotNull(exists);
        assertFalse(exists.getExists());
    }

    @Test
    void testUpdateTranscriptAdditionalAttendees_Success() {
        // Arrange
        Hearing hearing = new Hearing();
        hearing.setTranscript(Collections.singletonList("old transcript"));
        hearing.setAuditDetails(new AuditDetails());

        Hearing updatedHearing = new Hearing();
        updatedHearing.setTranscript(Collections.singletonList("new transcript"));
        updatedHearing.setAuditDetails(new AuditDetails());

        HearingRequest hearingRequest = new HearingRequest();
        hearingRequest.setRequestInfo(new RequestInfo());
        hearingRequest.setHearing(updatedHearing);

        when(validator.validateHearingExistence(any(RequestInfo.class),any(Hearing.class))).thenReturn(hearing);

        // Act
        Hearing result = hearingService.updateTranscriptAdditionalAttendees(hearingRequest);

        // Assert
        assertNotNull(result);
        assertEquals("new transcript", result.getTranscript().get(0));
        verify(enrichmentUtil, times(1)).enrichHearingApplicationUponUpdate(hearingRequest);
        verify(hearingRepository, times(1)).updateTranscriptAdditionalAttendees(updatedHearing);
        assertEquals(updatedHearing.getAuditDetails(), result.getAuditDetails());
    }

    @Test
    void testUpdateTranscriptAdditionalAttendees_CustomException() {
        // Arrange
        HearingRequest hearingRequest = new HearingRequest();
        hearingRequest.setRequestInfo(new RequestInfo());
        hearingRequest.setHearing(new Hearing());

        when(validator.validateHearingExistence(any(RequestInfo.class),any(Hearing.class)))
                .thenReturn(new Hearing());
        doThrow(new RuntimeException("Unexpected error"))
                .when(hearingRepository).updateTranscriptAdditionalAttendees(any(Hearing.class));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> hearingService.updateTranscriptAdditionalAttendees(hearingRequest));

        assertEquals(HEARING_UPDATE_EXCEPTION, exception.getCode());
        assertTrue(exception.getMessage().contains("Error occurred while updating hearing: Unexpected error"));
        verify(enrichmentUtil, times(1)).enrichHearingApplicationUponUpdate(hearingRequest);
        verify(hearingRepository, times(1)).updateTranscriptAdditionalAttendees(any(Hearing.class));
    }

    @Test
    void updateTranscriptAdditionalAttendees_ValidationFails() {
        // Arrange
        HearingRequest hearingRequest = new HearingRequest();
        hearingRequest.setRequestInfo(new RequestInfo());
        hearingRequest.setHearing(new Hearing());

        when(validator.validateHearingExistence(any(RequestInfo.class), any(Hearing.class)))
                .thenThrow(new CustomException("VALIDATION_ERROR", "Validation failed"));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> hearingService.updateTranscriptAdditionalAttendees(hearingRequest));

        assertEquals("VALIDATION_ERROR", exception.getCode());
        assertEquals("Validation failed", exception.getMessage());
        verify(enrichmentUtil, never()).enrichHearingApplicationUponUpdate(any(HearingRequest.class));
        verify(hearingRepository, never()).updateTranscriptAdditionalAttendees(any(Hearing.class));
    }

    @Test
    void updateStartAndTime_success() {
        // Arrange
        UpdateTimeRequest updateTimeRequest = new UpdateTimeRequest();
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("test-user-uuid");
        requestInfo.setUserInfo(userInfo);
        updateTimeRequest.setRequestInfo(requestInfo);

        Hearing hearing = new Hearing();
        hearing.setHearingId("hearing-id");
        hearing.setAuditDetails(new AuditDetails());

        updateTimeRequest.setHearings(List.of
                (hearing));

        Hearing existingHearing = new Hearing();
        existingHearing.setAuditDetails(new AuditDetails());

        when(validator.validateHearingExistence(requestInfo, hearing)).thenReturn(existingHearing);

        // Act
        hearingService.updateStartAndTime(updateTimeRequest);

        // Assert
        assertEquals(userInfo.getUuid(), hearing.getAuditDetails().getLastModifiedBy());
        assertNotNull(hearing.getAuditDetails().getLastModifiedTime());
    }

    @Test
    void updateStartAndTime_missingHearingId() {
        // Arrange
        UpdateTimeRequest updateTimeRequest = new UpdateTimeRequest();
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("test-user-uuid");
        requestInfo.setUserInfo(userInfo);
        updateTimeRequest.setRequestInfo(requestInfo);

        Hearing hearing = new Hearing();
        updateTimeRequest.setHearings(List.of(hearing));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> hearingService.updateStartAndTime(updateTimeRequest));
        assertEquals("Exception while updating hearing start and end time", exception.getCode());
        assertEquals("Hearing Id is required for updating start and end time", exception.getMessage());

        verify(producer, never()).push(anyString(), any());
    }

    @Test
    void updateStartAndTime_exception() {
        // Arrange
        UpdateTimeRequest updateTimeRequest = new UpdateTimeRequest();
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("test-user-uuid");
        requestInfo.setUserInfo(userInfo);
        updateTimeRequest.setRequestInfo(requestInfo);

        Hearing hearing = new Hearing();
        hearing.setHearingId("hearing-id");
        hearing.setAuditDetails(new AuditDetails());

        updateTimeRequest.setHearings(List.of(hearing));

        when(validator.validateHearingExistence(requestInfo, hearing)).thenThrow(new RuntimeException("Validation failed"));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> hearingService.updateStartAndTime(updateTimeRequest));
        assertEquals("Exception while updating hearing start and end time", exception.getCode());
        assertEquals("Error occurred while updating hearing start and end time: Validation failed", exception.getMessage());

        verify(producer, never()).push(anyString(), any());
    }

    @Test
    void uploadWitnessDeposition_Success() {
        // Arrange
        HearingRequest hearingRequest = new HearingRequest();
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("test-user-uuid");
        requestInfo.setUserInfo(userInfo);
        hearingRequest.setRequestInfo(requestInfo);
        hearingRequest.setHearing(new Hearing());

        when(validator.validateHearingExistence(requestInfo, hearingRequest.getHearing())).thenReturn(hearingRequest.getHearing());

        // Act
        Hearing result = hearingService.uploadWitnessDeposition(hearingRequest);

        // Assert
        assertNotNull(result);
    }

    @Test
    void uploadWitnessDeposition_CustomException() {
        // Arrange
        HearingRequest hearingRequest = new HearingRequest();
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid("test-user-uuid");
        requestInfo.setUserInfo(userInfo);
        hearingRequest.setRequestInfo(requestInfo);
        hearingRequest.setHearing(new Hearing());

        when(validator.validateHearingExistence(requestInfo, hearingRequest.getHearing()))
                .thenThrow(new CustomException("Hearing not found", "throw custom exception"));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> hearingService.uploadWitnessDeposition(hearingRequest));
        assertEquals("Hearing not found", exception.getCode());
        assertTrue(exception.getMessage().contains("custom exception"));
    }
}
