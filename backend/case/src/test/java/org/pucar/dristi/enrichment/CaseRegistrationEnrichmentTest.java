package org.pucar.dristi.enrichment;

import static org.junit.jupiter.api.Assertions.*;

import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CourtCase;

import java.util.Arrays;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class CaseRegistrationEnrichmentTest {

    @Mock
    private IdgenUtil idgenUtil;

    @InjectMocks
    private CaseRegistrationEnrichment caseRegistrationEnrichment;

    private CaseRequest caseRequest;

    private CourtCase courtCase;

    @BeforeEach
    void setUp() {
        RequestInfo requestInfo = new RequestInfo();

        courtCase = new CourtCase();
        courtCase.setLinkedCases(Collections.emptyList());
        courtCase.setLitigants(Collections.emptyList());
        courtCase.setRepresentatives(Collections.emptyList());
        courtCase.setFilingNumber("filingNumber1");
        courtCase.setCaseNumber("caseNumber1");

        caseRequest = new CaseRequest(requestInfo, Arrays.asList(courtCase));
    }

//    @Test
//    void testEnrichCaseRegistration() {
//
//        caseRegistrationEnrichment.enrichCaseRegistration(caseRequest);
//
//        assertNotNull(courtCase.getLinkedCases());
//        assertNotNull(courtCase.getLitigants());
//        assertNotNull(courtCase.getRepresentatives());
//        assertTrue(courtCase.getFilingNumber().equals("filingNumber1"));
//        assertTrue(courtCase.getCaseNumber().equals("caseNumber1"));
//    }
}

