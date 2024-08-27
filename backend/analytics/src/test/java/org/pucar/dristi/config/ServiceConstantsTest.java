package org.pucar.dristi.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceConstantsTest {

    @Test
    void testServiceConstants() {
        // Testing exception messages
        assertExceptionValues();

        // Testing ES index formats
        assertEquals("{\"index\":{\"_index\":\"%s\",\"_id\":\"%s\"}}\n", ServiceConstants.ES_INDEX_HEADER_FORMAT);
        assertEquals("{\"Data\": {\"id\":\"%s\",\"name\":\"%s\",\"entityType\":\"%s\",\"referenceId\":\"%s\",\"status\":\"%s\",\"assignedTo\":%s,\"assignedRole\":%s,\"cnrNumber\":\"%s\",\"filingNumber\":\"%s\",\"isCompleted\":%b,\"stateSla\":%d,\"businessServiceSla\":%d,\"additionalDetails\":%s}}\n", ServiceConstants.ES_INDEX_DOCUMENT_FORMAT);

        // Testing JSON paths
        assertJsonPaths();

        // Testing other constants
        assertOtherConstants();
    }

    void assertExceptionValues(){
        assertEquals("External Service threw an Exception: ", ServiceConstants.EXTERNAL_SERVICE_EXCEPTION);
        assertEquals("Error while fetching result from URL: {} with request: {}", ServiceConstants.SEARCHER_SERVICE_EXCEPTION);
        assertEquals("Exception occurred while fetching category lists from mdms: ", ServiceConstants.ERROR_WHILE_FETCHING_FROM_MDMS);
        assertEquals("Custom Exception",ServiceConstants.CUSTOM_EXCEPTION);
        assertEquals("Pending Task Exception", ServiceConstants.PENDING_TASK_EXCEPTION);
        assertEquals("IllegalArgumentException", ServiceConstants.ILLEGAL_ARGUMENT_EXCEPTION_CODE);
        assertEquals("PARSING ERROR", ServiceConstants.PARSING_ERROR);
    }

    void assertJsonPaths(){
        assertEquals("$.ProcessInstances.*", ServiceConstants.PROCESS_INSTANCE_PATH);
        assertEquals("$.RequestInfo", ServiceConstants.REQUEST_INFO_PATH);
        assertEquals("$.id", ServiceConstants.ID_PATH);
        assertEquals("$.businessService", ServiceConstants.BUSINESS_SERVICE_PATH);
        assertEquals("$.businessId", ServiceConstants.BUSINESS_ID_PATH);
        assertEquals("$.state.state", ServiceConstants.STATE_PATH);
        assertEquals("$.tenantId", ServiceConstants.TENANT_ID_PATH);
        assertEquals("$.action", ServiceConstants.ACTION_PATH);
        assertEquals("$.assignes.*", ServiceConstants.ASSIGNES_PATH);
        assertEquals("$.state.actions.*.roles.*", ServiceConstants.ASSIGNED_ROLE_PATH);
        assertEquals("$.stateSla", ServiceConstants.STATE_SLA_PATH);
        assertEquals("$.businesssServiceSla", ServiceConstants.BUSINESS_SERVICE_SLA_PATH);
        assertEquals("$.HearingList.*", ServiceConstants.HEARING_PATH);
        assertEquals("$.hearingType", ServiceConstants.HEARING_TYPE_PATH);
        assertEquals("$.criteria.*.responseList[0]", ServiceConstants.CASE_PATH);
        assertEquals("$.artifacts.*", ServiceConstants.ARTIFACT_PATH);
        assertEquals("$.list.*", ServiceConstants.TASK_PATH);
        assertEquals("$.list.*", ServiceConstants.ORDER_PATH);
        assertEquals("$.applicationList.*", ServiceConstants.APPLICATION_PATH);
        assertEquals("$.cnrNumbers", ServiceConstants.CNR_NUMBERS_PATH);
        assertEquals("$.cnrNumber", ServiceConstants.CNR_NUMBER_PATH);
        assertEquals("$.filingNumber", ServiceConstants.FILING_NUMBER_PATH);
        assertEquals("$.errors", ServiceConstants.ERRORS_PATH);
        assertEquals("$.orderType", ServiceConstants.ORDER_TYPE_PATH);
    }

    void assertOtherConstants(){
        assertEquals("uief87324", ServiceConstants.RES_MSG_ID);
        assertEquals("successful", ServiceConstants.SUCCESSFUL);
        assertEquals("failed", ServiceConstants.FAILED);
        assertEquals("RequestInfo",ServiceConstants.REQUEST_INFO);
        assertEquals("Authorization",ServiceConstants.AUTHORIZATION);
        assertEquals("cnrNumber",ServiceConstants.CNR_NUMBER);
        assertEquals("filingNumber",ServiceConstants.FILING_NUMBER);
        assertEquals("TENANTID", ServiceConstants.TENANTID_MDC_STRING);
    }
}
