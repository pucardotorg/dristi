package org.pucar.dristi.enrichment;

import com.fasterxml.jackson.databind.JsonNode;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.Application;
import org.pucar.dristi.web.models.ApplicationRequest;
import org.pucar.dristi.web.models.StatuteSection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.ENRICHMENT_EXCEPTION;

@ExtendWith(MockitoExtension.class)
class ApplicationEnrichmentTest {

    @Mock
    private IdgenUtil idgenUtil;

    @Mock
    private CaseUtil caseUtil;

    @Mock
    private Configuration configuration;

    @InjectMocks
    private ApplicationEnrichment applicationEnrichment;

    private ApplicationRequest applicationRequest;

    @BeforeEach
    void setUp() {
        User userInfo = User.builder().uuid("user-uuid").tenantId("tenant-id").build();
        RequestInfo requestInfo = RequestInfo.builder().userInfo(userInfo).build();
        Application application = Application.builder()
                .statuteSection(new StatuteSection())
                .filingNumber("KL-123")
                .documents(Collections.singletonList(new Document()))
                .build();
        applicationRequest = ApplicationRequest.builder()
                .requestInfo(requestInfo)
                .application(application)
                .build();
    }

    @Test
    void enrichApplication() {
        String mockedTenantId = "KL123";
        String mockedAppNumber = "CMP123";
        when(idgenUtil.getIdList(any(), any(), any(), any(), anyInt(),any()))
                .thenReturn(Collections.singletonList("CMP123"));
        JsonNode caseDetails = mock(JsonNode.class);
        when(caseDetails.has("courtId")).thenReturn(true);
        when(caseDetails.get("courtId")).thenReturn(mock(JsonNode.class));
        when(caseDetails.get("courtId").asText()).thenReturn("court123");

        when(caseUtil.searchCaseDetails(any())).thenReturn(caseDetails);
        when(configuration.getCmpConfig()).thenReturn("config");
        when(configuration.getCmpFormat()).thenReturn("format");
        applicationEnrichment.enrichApplication(applicationRequest);

        Application application = applicationRequest.getApplication();
        assertEquals(mockedAppNumber, application.getApplicationNumber());

        assertNotNull(application.getId());
        assertNotNull(application.getAuditDetails());
        assertNotNull(application.getStatuteSection().getId());
        assertNotNull(application.getStatuteSection().getAuditdetails());

        application.getDocuments().forEach(document -> {
            assertNotNull(document.getId());
        });

        verify(idgenUtil, times(1)).getIdList(any(), any(), any(), any(), anyInt(),any());
    }

    @Test
    void enrichApplicationUponUpdate() {
        AuditDetails auditDetails = AuditDetails.builder()
                .createdBy("user-uuid")
                .createdTime(System.currentTimeMillis() - 1000)
                .lastModifiedBy("user-uuid")
                .lastModifiedTime(System.currentTimeMillis() - 1000)
                .build();
        Application application = applicationRequest.getApplication();
        application.setAuditDetails(auditDetails);

        applicationEnrichment.enrichApplicationUponUpdate(applicationRequest);

        assertEquals("user-uuid", application.getAuditDetails().getLastModifiedBy());
        assertTrue(application.getAuditDetails().getLastModifiedTime() > auditDetails.getCreatedTime());
    }
    @Test
    public void testEnrichApplicationUponUpdateFailure() {
        ApplicationRequest request = new ApplicationRequest();

        CustomException customException = assertThrows(CustomException.class, () -> {
            applicationEnrichment.enrichApplicationUponUpdate(request);
        });
        assertEquals(ENRICHMENT_EXCEPTION, customException.getCode());

    }
}
