package org.pucar.dristi.web.models;

import org.egov.common.contract.models.AuditDetails;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaseOverallStatusTest {

    @Test
    void testNoArgsConstructor() {
        CaseOverallStatus caseOverallStatus = new CaseOverallStatus();
        assertNull(caseOverallStatus.getFilingNumber());
        assertNull(caseOverallStatus.getTenantId());
        assertNull(caseOverallStatus.getStage());
        assertNull(caseOverallStatus.getSubstage());
        assertNull(caseOverallStatus.getAuditDetails());
    }

    @Test
    void testAllArgsConstructor() {
        AuditDetails auditDetails = new AuditDetails();
        CaseOverallStatus caseOverallStatus = new CaseOverallStatus("123", "tenant", "stage1", "substage1", auditDetails);
        assertEquals("123", caseOverallStatus.getFilingNumber());
        assertEquals("tenant", caseOverallStatus.getTenantId());
        assertEquals("stage1", caseOverallStatus.getStage());
        assertEquals("substage1", caseOverallStatus.getSubstage());
        assertEquals(auditDetails, caseOverallStatus.getAuditDetails());
    }

    @Test
    void testCustomConstructor() {
        CaseOverallStatus caseOverallStatus = new CaseOverallStatus("123", "tenant", "stage1", "substage1");
        assertEquals("123", caseOverallStatus.getFilingNumber());
        assertEquals("tenant", caseOverallStatus.getTenantId());
        assertEquals("stage1", caseOverallStatus.getStage());
        assertEquals("substage1", caseOverallStatus.getSubstage());
        assertNull(caseOverallStatus.getAuditDetails());
    }

    @Test
    void testBuilder() {
        AuditDetails auditDetails = new AuditDetails();
        CaseOverallStatus caseOverallStatus = CaseOverallStatus.builder()
                .filingNumber("123")
                .tenantId("tenant")
                .stage("stage1")
                .substage("substage1")
                .auditDetails(auditDetails)
                .build();

        assertEquals("123", caseOverallStatus.getFilingNumber());
        assertEquals("tenant", caseOverallStatus.getTenantId());
        assertEquals("stage1", caseOverallStatus.getStage());
        assertEquals("substage1", caseOverallStatus.getSubstage());
        assertEquals(auditDetails, caseOverallStatus.getAuditDetails());
    }

    @Test
    void testSettersAndGetters() {
        CaseOverallStatus caseOverallStatus = new CaseOverallStatus();
        caseOverallStatus.setFilingNumber("123");
        caseOverallStatus.setTenantId("tenant");
        caseOverallStatus.setStage("stage1");
        caseOverallStatus.setSubstage("substage1");
        AuditDetails auditDetails = new AuditDetails();
        caseOverallStatus.setAuditDetails(auditDetails);

        assertEquals("123", caseOverallStatus.getFilingNumber());
        assertEquals("tenant", caseOverallStatus.getTenantId());
        assertEquals("stage1", caseOverallStatus.getStage());
        assertEquals("substage1", caseOverallStatus.getSubstage());
        assertEquals(auditDetails, caseOverallStatus.getAuditDetails());
    }
}
