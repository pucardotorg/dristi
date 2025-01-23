package org.pucar.dristi.enrichment;

import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.util.AdvocateUtil;
import org.pucar.dristi.web.models.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.pucar.dristi.config.ServiceConstants.E_SIGN;
import static org.pucar.dristi.config.ServiceConstants.E_SIGN_COMPLETE;

@ExtendWith(MockitoExtension.class)
class EnrichCaseWhenESignTest {

    @Mock
    private IndividualService individualService;

    @Mock
    private AdvocateUtil advocateUtil;

    @InjectMocks
    private EnrichCaseWhenESign enrichCaseWhenESign;

    private CaseRequest caseRequest;
    private RequestInfo requestInfo;
    private CourtCase courtCase;

    @BeforeEach
    void setUp() {
        requestInfo = new RequestInfo();
        courtCase = new CourtCase();
        caseRequest = new CaseRequest(requestInfo, courtCase);
    }

    @Test
    void testCanEnrich_True() {
        courtCase.setWorkflow(Workflow.builder().action(E_SIGN).build());
        assertTrue(enrichCaseWhenESign.canEnrich(caseRequest));
    }

    @Test
    void testCanEnrich_False() {
        courtCase.setWorkflow(Workflow.builder().action("OTHER_ACTION").build());
        assertFalse(enrichCaseWhenESign.canEnrich(caseRequest));
    }

    @Test
    void testEnrich_LitigantSigns() {
        String individualId = "12345";
        when(individualService.getIndividualId(requestInfo)).thenReturn(individualId);

        Party litigant = Party.builder().id(UUID.randomUUID())
                .individualId(individualId)
                .isActive(true)
                .hasSigned(false).build();
        Party litigantUnsigned = Party.builder().id(UUID.randomUUID())
                .individualId("individualId")
                .isActive(true)
                .hasSigned(false).build();
        courtCase.setLitigants(List.of(litigant,litigantUnsigned));

        enrichCaseWhenESign.enrich(caseRequest);

        assertTrue(litigant.getHasSigned());
    }

    @Test
    void testEnrich_AdvocateSigns() {
        String individualId = "67890";
        when(individualService.getIndividualId(requestInfo)).thenReturn(individualId);

        Party litigant = Party.builder().id(UUID.randomUUID())
                .individualId("individualId")
                .isActive(true)
                .hasSigned(false).build();
        courtCase.setLitigants(Collections.singletonList(litigant));


        Advocate advocate = Advocate.builder().id(UUID.randomUUID()).isActive(true).build();
        when(advocateUtil.fetchAdvocatesByIndividualId(requestInfo, individualId)).thenReturn(List.of(advocate));

        AdvocateMapping advocateMapping = AdvocateMapping.builder()
                .advocateId(advocate.getId().toString())
                .isActive(true)
                .hasSigned(false)
                .representing(Collections.singletonList(litigant))
                .build();
        courtCase.setRepresentatives(Collections.singletonList(advocateMapping));

        enrichCaseWhenESign.enrich(caseRequest);

        assertTrue(advocateMapping.getHasSigned());
    }

}
