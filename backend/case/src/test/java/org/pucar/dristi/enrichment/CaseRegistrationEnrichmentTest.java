package org.pucar.dristi.enrichment;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CourtCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaseRegistrationEnrichmentTest {

    @Mock
    private IdgenUtil idgenUtil;

    @Mock
    private Configuration config;

    @InjectMocks
    private CaseRegistrationEnrichment caseRegistrationEnrichment;

    private CaseRequest caseRequest;
    private CourtCase courtCase;
    private RequestInfo requestInfo;
    private User userInfo;

    @BeforeEach
    void setUp() {
        // Initialize RequestInfo with necessary user info
        requestInfo = new RequestInfo();
        userInfo = new User();
        userInfo.setUuid("user-uuid");
        requestInfo.setUserInfo(userInfo);

        // Create case-indexer.yml CaseRequest with case-indexer.yml single CourtCase
        caseRequest = new CaseRequest();
        courtCase = new CourtCase();
        courtCase.setTenantId("tenant-id");
        courtCase.setLinkedCases(new ArrayList<>());
        courtCase.setLitigants(new ArrayList<>());
        courtCase.setRepresentatives(new ArrayList<>());
        courtCase.setStatutesAndSections(new ArrayList<>());
        courtCase.setDocuments(new ArrayList<>());
        caseRequest.setCases(courtCase);

        // Set the request info in the case request
        caseRequest.setRequestInfo(requestInfo);
    }

    @Test
    void testEnrichCaseRegistration() {
        // Mock the config to return specific values
        when(config.getCaseFilingNumber()).thenReturn("caseFilingNumber");

        // Mock the ID generation to return case-indexer.yml list of IDs
        List<String> idList = Collections.singletonList("generated-id");
        when(idgenUtil.getIdList(any(RequestInfo.class), anyString(), anyString(), any(), anyInt()))
                .thenReturn(idList);

        // Call the method to test
        caseRegistrationEnrichment.enrichCaseRegistration(caseRequest);

        // Verify the behavior and assert the results
        verify(idgenUtil).getIdList(any(RequestInfo.class), eq("tenant-id"), eq("caseFilingNumber"), eq(null), eq(1));
        assertNotNull(courtCase.getAuditdetails());
        assertNotNull(courtCase.getLinkedCases());
        assertNotNull(courtCase.getLitigants());
        assertNotNull(courtCase.getRepresentatives());
        assertEquals("generated-id", courtCase.getFilingNumber());
        assertEquals("generated-id", courtCase.getCaseNumber());
    }
}

