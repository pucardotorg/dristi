package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.util.DateUtil;
import org.pucar.dristi.web.models.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OpenApiServiceTest {

    @Mock
    private Configuration configuration;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private DateUtil dateUtil;

    @InjectMocks
    private OpenApiService openApiService;

    private CaseSummaryResponse mockCaseSummaryResponse;

    @BeforeEach
    public void setup() {
        // Setup mock case summary
        CaseSummary mockCaseSummary = new CaseSummary();
        mockCaseSummary.setFilingNumber("TEST-FILING-001");

        mockCaseSummaryResponse = new CaseSummaryResponse();
        mockCaseSummaryResponse.setCaseSummary(mockCaseSummary);
    }

    @Test
    public void testGetCaseByCnrNumber_ElasticSearchDisabled() {
        // Arrange
        String tenantId = "TEST";
        String cnrNumber = "TEST-CNR-001";

        when(configuration.getIsElasticSearchEnabled()).thenReturn(false);
        when(configuration.getCaseServiceHost()).thenReturn("http://test-host");
        when(configuration.getCaseServiceSearchByCnrNumberEndpoint()).thenReturn("/search");
        when(configuration.getJudgeName()).thenReturn("Test Judge");
        when(configuration.getHearingServiceHost()).thenReturn("http://hearing-host");
        when(configuration.getHearingSearchEndpoint()).thenReturn("/search");

        when(serviceRequestRepository.fetchResult(any(StringBuilder.class), any(OpenApiCaseSummaryRequest.class)))
                .thenReturn(mockCaseSummaryResponse);

        when(objectMapper.convertValue(any(), eq(CaseSummaryResponse.class)))
                .thenReturn(mockCaseSummaryResponse);
        when(objectMapper.convertValue(any(), eq(HearingListResponse.class)))
                .thenReturn(new HearingListResponse());

        // Act
        CaseSummaryResponse response = openApiService.getCaseByCnrNumber(tenantId, cnrNumber);

        // Assert
        assertNotNull(response);
        assertEquals("Test Judge", response.getCaseSummary().getJudgeName());

        // Verify interactions
        verify(serviceRequestRepository).fetchResult(any(StringBuilder.class), any(OpenApiCaseSummaryRequest.class));
        verify(configuration).getIsElasticSearchEnabled();
    }

    @Test
    public void testGetCaseByCnrNumber_ElasticSearchEnabled_ThrowsException() {
        // Arrange
        String tenantId = "TEST";
        String cnrNumber = "TEST-CNR-001";

        when(configuration.getIsElasticSearchEnabled()).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            openApiService.getCaseByCnrNumber(tenantId, cnrNumber);
        });
    }

    @Test
    public void testGetCaseListByCaseType_ValidInput() {
        // Arrange
        String tenantId = "TEST";
        Integer year = 2024;
        String caseType = "CMP";
        Integer offset = 0;
        Integer limit = 10;
        String sort = "registrationDate,asc";

        CaseListResponse mockCaseListResponse = new CaseListResponse();
        List<Hearing> hearings = new ArrayList<Hearing>();
        List<CaseListLineItem> caseSummaryList = new ArrayList<>();
        mockCaseListResponse.setCaseList(caseSummaryList);

        when(configuration.getIsElasticSearchEnabled()).thenReturn(false);
        when(configuration.getCaseServiceHost()).thenReturn("http://test-host");
        when(configuration.getCaseServiceSearchByCaseTypeEndpoint()).thenReturn("/search");
        when(dateUtil.getYearInSeconds(any(Integer.class))).thenReturn(List.of(1672531200L, 1672617600L));

        when(serviceRequestRepository.fetchResult(any(StringBuilder.class), any(OpenApiCaseSummaryRequest.class)))
                .thenReturn(mockCaseListResponse);

        when(objectMapper.convertValue(any(), eq(CaseListResponse.class)))
                .thenReturn(mockCaseListResponse);

        // Act
        CaseListResponse response = openApiService.getCaseListByCaseType(tenantId, year, caseType, offset, limit, sort);

        // Assert
        assertNotNull(response);
        assertEquals(caseSummaryList, response.getCaseList());

        // Verify interactions
        verify(serviceRequestRepository).fetchResult(any(StringBuilder.class), any(OpenApiCaseSummaryRequest.class));
    }

    @Test
    public void testGetCaseByCaseNumber_ValidInput() {
        // Arrange
        String tenantId = "TEST";
        Integer year = 2024;
        String caseType = "CMP";
        Integer caseNumber = 12345;

        when(configuration.getIsElasticSearchEnabled()).thenReturn(false);
        when(configuration.getCaseServiceHost()).thenReturn("http://test-host");
        when(configuration.getCaseServiceSearchByCaseNumberEndpoint()).thenReturn("/search");
        when(configuration.getJudgeName()).thenReturn("Test Judge");
        when(configuration.getHearingServiceHost()).thenReturn("http://hearing-host");
        when(configuration.getHearingSearchEndpoint()).thenReturn("/search");

        when(serviceRequestRepository.fetchResult(any(StringBuilder.class), any(OpenApiCaseSummaryRequest.class)))
                .thenReturn(mockCaseSummaryResponse);

        when(objectMapper.convertValue(any(), eq(CaseSummaryResponse.class)))
                .thenReturn(mockCaseSummaryResponse);
        when(objectMapper.convertValue(any(), eq(HearingListResponse.class)))
                .thenReturn(new HearingListResponse());

        // Act
        CaseSummaryResponse response = openApiService.getCaseByCaseNumber(tenantId, year, caseType, caseNumber);

        // Assert
        assertNotNull(response);
        assertEquals("Test Judge", response.getCaseSummary().getJudgeName());

        // Verify interactions
        verify(serviceRequestRepository).fetchResult(any(StringBuilder.class), any(OpenApiCaseSummaryRequest.class));
    }

    @Test
    public void testEnrichNextHearingDate_SingleScheduledHearing() {
        // Arrange
        String filingNumber = "TEST-FILING-001";

        Hearing scheduledHearing = new Hearing();
        scheduledHearing.setStatus("Scheduled");
        scheduledHearing.setStartTime(1672531200L); // Example timestamp

        HearingListResponse hearingListResponse = new HearingListResponse();
        hearingListResponse.setHearingList(List.of(scheduledHearing));

        when(configuration.getHearingServiceHost()).thenReturn("http://hearing-host");
        when(configuration.getHearingSearchEndpoint()).thenReturn("/search");

        when(serviceRequestRepository.fetchResult(any(StringBuilder.class), any(HearingSearchRequest.class)))
                .thenReturn(hearingListResponse);

        when(objectMapper.convertValue(any(), eq(HearingListResponse.class)))
                .thenReturn(hearingListResponse);

        // Act
        Long nextHearingDate = openApiService.enrichNextHearingDate(filingNumber);

        // Assert
        assertEquals(1672531200L, nextHearingDate);

        // Verify interactions
        verify(serviceRequestRepository).fetchResult(any(StringBuilder.class), any(HearingSearchRequest.class));
    }

    @Test
    public void testEnrichNextHearingDate_MultipleScheduledHearings_ThrowsException() {
        // Arrange
        String filingNumber = "TEST-FILING-001";

        Hearing hearing1 = new Hearing();
        hearing1.setStatus("Scheduled");
        hearing1.setStartTime(1672531200L);

        Hearing hearing2 = new Hearing();
        hearing2.setStatus("Scheduled");
        hearing2.setStartTime(1672617600L);

        HearingListResponse hearingListResponse = new HearingListResponse();
        hearingListResponse.setHearingList(List.of(hearing1, hearing2));

        when(configuration.getHearingServiceHost()).thenReturn("http://hearing-host");
        when(configuration.getHearingSearchEndpoint()).thenReturn("/search");

        when(serviceRequestRepository.fetchResult(any(StringBuilder.class), any(HearingSearchRequest.class)))
                .thenReturn(hearingListResponse);

        when(objectMapper.convertValue(any(), eq(HearingListResponse.class)))
                .thenReturn(hearingListResponse);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            openApiService.enrichNextHearingDate(filingNumber);
        });
    }

    @Test
    public void testEnrichNextHearingDate_NoScheduledHearings() {
        // Arrange
        String filingNumber = "TEST-FILING-001";

        HearingListResponse hearingListResponse = new HearingListResponse();
        hearingListResponse.setHearingList(new ArrayList<>());

        when(configuration.getHearingServiceHost()).thenReturn("http://hearing-host");
        when(configuration.getHearingSearchEndpoint()).thenReturn("/search");

        when(serviceRequestRepository.fetchResult(any(StringBuilder.class), any(HearingSearchRequest.class)))
                .thenReturn(hearingListResponse);

        when(objectMapper.convertValue(any(), eq(HearingListResponse.class)))
                .thenReturn(hearingListResponse);

        // Act
        Long nextHearingDate = openApiService.enrichNextHearingDate(filingNumber);

        // Assert
        assertNull(nextHearingDate);

        // Verify interactions
        verify(serviceRequestRepository).fetchResult(any(StringBuilder.class), any(HearingSearchRequest.class));
    }

    @Test
    public void testGetCaseListTypeWithElasticSearchEnabled() {
        // Arrange
        String tenantId = "TEST";
        Integer year = 2024;
        String caseType = "CMP";
        Integer offset = 0;
        Integer limit = 10;
        String sort = "registrationDate,asc";

        when(configuration.getIsElasticSearchEnabled()).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            openApiService.getCaseListByCaseType(tenantId, year, caseType, offset, limit, sort);
        });
    }

    @Test
    public void testGetCaseByCaseNumberWithElasticSearchEnabled() {
        // Arrange
        String tenantId = "TEST";
        Integer year = 2024;
        String caseType = "CMP";
        Integer caseNumber = 12345;

        when(configuration.getIsElasticSearchEnabled()).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            openApiService.getCaseByCaseNumber(tenantId, year, caseType, caseNumber);
        });
    }
}