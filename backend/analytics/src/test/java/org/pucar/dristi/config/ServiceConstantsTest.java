package org.pucar.dristi.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceConstantsTest {

    @Test
    void testServiceConstants() {
        // Testing exception messages
        assertEquals("External Service threw an Exception: ", ServiceConstants.EXTERNAL_SERVICE_EXCEPTION);
        assertEquals("Error while fetching result from URL: {} with request: {}", ServiceConstants.SEARCHER_SERVICE_EXCEPTION);
        assertEquals("Exception occurred while fetching category lists from mdms: ", ServiceConstants.ERROR_WHILE_FETCHING_FROM_MDMS);

        // Testing ES index formats
        assertEquals("{\"index\":{\"_index\":\"%s\",\"_id\":\"%s\"}}\n", ServiceConstants.ES_INDEX_HEADER_FORMAT);
        assertEquals("{\"Data\": {\"id\":\"%s\",\"name\":\"%s\",\"entityType\":\"%s\",\"referenceId\":\"%s\",\"status\":\"%s\",\"assignedTo\":%s,\"assignedRole\":%s,\"cnrNumber\":\"%s\",\"filingNumber\":\"%s\",\"isCompleted\":%b,\"stateSla\":%d,\"businessServiceSla\":%d,\"additionalDetails\":%s}}\n", ServiceConstants.ES_INDEX_DOCUMENT_FORMAT);
        assertEquals("TENANTID", ServiceConstants.TENANTID_MDC_STRING);

        // Testing search params
        assertEquals("?applicationNumber=", ServiceConstants.APPLICATION_NUMBER);
        assertEquals("&cnrNumber=", ServiceConstants.CNR_NUMBER);
        assertEquals("&hearingId=", ServiceConstants.HEARING_ID);
        assertEquals("&tenantId=", ServiceConstants.TENANT_ID);
        assertEquals("?orderId=", ServiceConstants.ORDER_ID);
        assertEquals("&status=", ServiceConstants.STATUS);
        assertEquals("?filingNumber=", ServiceConstants.FILLING_NUMBER);
        assertEquals("?taskNumber=", ServiceConstants.TASK_NUMBER);
        assertEquals("?orderNumber=", ServiceConstants.ORDER_NUMBER);

        // Testing JSON paths
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
        assertEquals("$.state.isTerminateState", ServiceConstants.IS_TERMINATE_STATE_PATH);
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
        assertEquals("$.caseId", ServiceConstants.CASE_ID_PATH);
        assertEquals("$.filingNumber", ServiceConstants.FILING_NUMBER_PATH);
        assertEquals("$.errors", ServiceConstants.ERRORS_PATH);
        assertEquals("$.orderType", ServiceConstants.ORDER_TYPE_PATH);

        // Testing other constants
        assertEquals("uief87324", ServiceConstants.RES_MSG_ID);
        assertEquals("successful", ServiceConstants.SUCCESSFUL);
        assertEquals("failed", ServiceConstants.FAILED);

        // Testing date formats
        assertEquals("yyyy-MM-dd", ServiceConstants.DOB_FORMAT_Y_M_D);
        assertEquals("dd/MM/yyyy", ServiceConstants.DOB_FORMAT_D_M_Y);
        assertEquals("IllegalArgumentException", ServiceConstants.ILLEGAL_ARGUMENT_EXCEPTION_CODE);
        assertEquals("ObjectMapper not able to convertValue in userCall", ServiceConstants.OBJECTMAPPER_UNABLE_TO_CONVERT);
        assertEquals("dd-MM-yyyy HH:mm:ss", ServiceConstants.DOB_FORMAT_D_M_Y_H_M_S);
        assertEquals("createdDate", ServiceConstants.CREATED_DATE);
        assertEquals("lastModifiedDate", ServiceConstants.LAST_MODIFIED_DATE);
        assertEquals("dob", ServiceConstants.DOB);
        assertEquals("pwdExpiryDate", ServiceConstants.PWD_EXPIRY_DATE);
        assertEquals("INVALID_DATE_FORMAT", ServiceConstants.INVALID_DATE_FORMAT_CODE);
        assertEquals("Failed to parse date format in user", ServiceConstants.INVALID_DATE_FORMAT_MESSAGE);
        assertEquals("CITIZEN", ServiceConstants.CITIZEN_UPPER);
        assertEquals("Citizen", ServiceConstants.CITIZEN_LOWER);
        assertEquals("user", ServiceConstants.USER);

        assertEquals("PARSING ERROR", ServiceConstants.PARSING_ERROR);
        assertEquals("Failed to parse response of workflow business service search", ServiceConstants.FAILED_TO_PARSE_BUSINESS_SERVICE_SEARCH);
        assertEquals("BUSINESSSERVICE_NOT_FOUND", ServiceConstants.BUSINESS_SERVICE_NOT_FOUND);
        assertEquals("The businessService ", ServiceConstants.THE_BUSINESS_SERVICE);
        assertEquals(" is not found", ServiceConstants.NOT_FOUND);
        assertEquals("?tenantId=", ServiceConstants.TENANTID);
        assertEquals("&businessServices=", ServiceConstants.BUSINESS_SERVICES);
        assertEquals("Pending Task Exception", ServiceConstants.Pending_Task_Exception);
    }
}
