package org.pucar.dristi.enrichment;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.Hearing;
import org.pucar.dristi.web.models.HearingRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HearingRegistrationEnrichmentTest {

    @InjectMocks
    private HearingRegistrationEnrichment hearingRegistrationEnrichment;

    @Mock
    private IdgenUtil idgenUtil;

    @Mock
    private Configuration configuration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method to create a mock HearingRequest
    private HearingRequest createMockHearingRequest() {
        Hearing hearing = new Hearing();
        hearing.setAuditDetails(new AuditDetails());
        hearing.setDocuments(Arrays.asList(new Document(), new Document()));
        hearing.setFilingNumber(Collections.singletonList("tenant-123"));

        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        HearingRequest hearingRequest = new HearingRequest();
        hearingRequest.setHearing(hearing);
        hearingRequest.setRequestInfo(requestInfo);

        return hearingRequest;
    }

    @Test
    void testEnrichHearingRegistration_Success() {
        // Given
        HearingRequest hearingRequest = createMockHearingRequest();
        String mockTenantId = "tenant123";
        String mockHearingId = "HEARING123";
        String mockHearingNumber = "tenant-123" + "-" + mockHearingId;

        when(configuration.getHearingConfig()).thenReturn("hearingConfig");
        when(configuration.getHearingFormat()).thenReturn("hearingFormat");
        when(idgenUtil.getIdList(any(), any(), any(), any(),eq(1), eq(false))).thenReturn(Collections.singletonList(mockHearingId));

        // When
        hearingRegistrationEnrichment.enrichHearingRegistration(hearingRequest);

        // Then
        assertNotNull(hearingRequest.getHearing().getAuditDetails());
        assertNotNull(hearingRequest.getHearing().getId());
        assertEquals(mockHearingNumber, hearingRequest.getHearing().getHearingId());
        verify(idgenUtil).getIdList(any(), eq(mockTenantId), eq("hearingConfig"), eq("hearingFormat"), eq(1), eq(false));
    }

    @Test
    void testEnrichHearingRegistration_WithDocuments() {
        // Given
        HearingRequest hearingRequest = createMockHearingRequest();
        when(configuration.getHearingConfig()).thenReturn("hearingConfig");
        when(configuration.getHearingFormat()).thenReturn("hearingFormat");
        when(idgenUtil.getIdList(any(), anyString(), anyString(), anyString(), anyInt(), anyBoolean())).thenReturn(Collections.singletonList("HEARING_ID"));

        // When
        hearingRegistrationEnrichment.enrichHearingRegistration(hearingRequest);

        // Then
        assertNotNull(hearingRequest.getHearing().getDocuments());
        hearingRequest.getHearing().getDocuments().forEach(document -> {
            assertNotNull(document.getId());
            assertEquals(document.getId(), document.getDocumentUid());
        });
    }

    @Test
    void testEnrichHearingRegistration_ExceptionHandling() {
        // Given
        HearingRequest hearingRequest = createMockHearingRequest();
        when(configuration.getHearingConfig()).thenReturn("hearingConfig");
        when(configuration.getHearingFormat()).thenReturn("hearingFormat");
        when(idgenUtil.getIdList(any(), anyString(), anyString(), anyString(), anyInt(), anyBoolean())).thenThrow(new RuntimeException("Test Exception"));

        // When & Then
        CustomException thrownException = assertThrows(CustomException.class, () -> {
            hearingRegistrationEnrichment.enrichHearingRegistration(hearingRequest);
        });
        assertEquals("Error hearing in enrichment service: Test Exception", thrownException.getMessage());
    }

    @Test
    void testEnrichHearingApplicationUponUpdate_Success() {
        // Given
        HearingRequest hearingRequest = createMockHearingRequest();
        hearingRequest.getHearing().getDocuments().forEach(document -> document.setId(UUID.randomUUID().toString()));

        // When
        hearingRegistrationEnrichment.enrichHearingApplicationUponUpdate(hearingRequest);

        // Then
        assertNotNull(hearingRequest.getHearing().getAuditDetails().getLastModifiedTime());
        assertNotNull(hearingRequest.getHearing().getAuditDetails().getLastModifiedBy());
    }

    @Test
    void testEnrichHearingApplicationUponUpdate_NullDocumentIds() {
        // Given
        HearingRequest hearingRequest = createMockHearingRequest();
        hearingRequest.getHearing().getDocuments().forEach(document -> document.setId(null));

        // When
        hearingRegistrationEnrichment.enrichHearingApplicationUponUpdate(hearingRequest);

        // Then
        hearingRequest.getHearing().getDocuments().forEach(document -> {
            assertNotNull(document.getId());
        });
    }

    @Test
    void testEnrichHearingApplicationUponUpdate_ExceptionHandling() {
        // Given
        HearingRequest hearingRequest = createMockHearingRequest();
        hearingRequest.setHearing(null);
        // When & Then
        assertThrows(CustomException.class, () -> {
            hearingRegistrationEnrichment.enrichHearingApplicationUponUpdate(hearingRequest);
        });
    }
}
