package org.pucar.dristi.web.models;

import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.Test;
import org.pucar.dristi.web.models.CaseOverallStatus;
import org.pucar.dristi.web.models.CaseStageSubStage;

import static org.junit.jupiter.api.Assertions.*;

class CaseStageSubStageTest {

    @Test
    void testNoArgsConstructor() {
        CaseStageSubStage caseStageSubStage = new CaseStageSubStage();
        assertNull(caseStageSubStage.getRequestInfo());
        assertNull(caseStageSubStage.getCaseOverallStatus());
    }

    @Test
    void testAllArgsConstructor() {
        RequestInfo requestInfo = new RequestInfo();
        CaseOverallStatus caseOverallStatus = new CaseOverallStatus();
        CaseStageSubStage caseStageSubStage = new CaseStageSubStage(requestInfo, caseOverallStatus);
        assertEquals(requestInfo, caseStageSubStage.getRequestInfo());
        assertEquals(caseOverallStatus, caseStageSubStage.getCaseOverallStatus());
    }

    @Test
    void testBuilder() {
        RequestInfo requestInfo = new RequestInfo();
        CaseOverallStatus caseOverallStatus = new CaseOverallStatus();
        CaseStageSubStage caseStageSubStage = CaseStageSubStage.builder()
                .requestInfo(requestInfo)
                .caseOverallStatus(caseOverallStatus)
                .build();

        assertEquals(requestInfo, caseStageSubStage.getRequestInfo());
        assertEquals(caseOverallStatus, caseStageSubStage.getCaseOverallStatus());
    }

    @Test
    void testSettersAndGetters() {
        CaseStageSubStage caseStageSubStage = new CaseStageSubStage();
        RequestInfo requestInfo = new RequestInfo();
        CaseOverallStatus caseOverallStatus = new CaseOverallStatus();

        caseStageSubStage.setRequestInfo(requestInfo);
        caseStageSubStage.setCaseOverallStatus(caseOverallStatus);

        assertEquals(requestInfo, caseStageSubStage.getRequestInfo());
        assertEquals(caseOverallStatus, caseStageSubStage.getCaseOverallStatus());
    }

    @Test
    void testToString() {
        RequestInfo requestInfo = new RequestInfo();
        CaseOverallStatus caseOverallStatus = new CaseOverallStatus();
        CaseStageSubStage caseStageSubStage = CaseStageSubStage.builder()
                .requestInfo(requestInfo)
                .caseOverallStatus(caseOverallStatus)
                .build();

        String expected = "CaseStageSubStage(requestInfo=" + requestInfo + ", caseOverallStatus=" + caseOverallStatus + ")";
        assertEquals(expected, caseStageSubStage.toString());
    }
}
