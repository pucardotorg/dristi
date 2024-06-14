package org.pucar.dristi.config;

import org.junit.jupiter.api.Test;
import org.pucar.dristi.config.ServiceConstants;

import static org.junit.jupiter.api.Assertions.*;

class ServiceConstantsTest {

    @Test
    void testServiceConstants() {
        assertEquals("External Service threw an Exception: ", ServiceConstants.EXTERNAL_SERVICE_EXCEPTION);
        assertEquals("Exception while fetching from searcher: ", ServiceConstants.SEARCHER_SERVICE_EXCEPTION);
        assertEquals("IDGEN ERROR", ServiceConstants.IDGEN_ERROR);
        assertEquals("No ids returned from idgen Service", ServiceConstants.NO_IDS_FOUND_ERROR);
        assertEquals("uief87324", ServiceConstants.RES_MSG_ID);
        assertEquals("successful", ServiceConstants.SUCCESSFUL);
        assertEquals("failed", ServiceConstants.FAILED);
        assertEquals("url", ServiceConstants.URL);
        assertEquals("URL_SHORTENING_ERROR", ServiceConstants.URL_SHORTENING_ERROR_CODE);
        assertEquals("Unable to shorten url: ", ServiceConstants.URL_SHORTENING_ERROR_MESSAGE);
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
        assertEquals("ADVOCATE_CREATE_EXCEPTION", ServiceConstants.EVIDENCE_CREATE_EXCEPTION);
        assertEquals("WORKFLOW_SERVICE_EXCEPTION", ServiceConstants.WORKFLOW_SERVICE_EXCEPTION);
        assertEquals("ENRICHMENT_EXCEPTION", ServiceConstants.ENRICHMENT_EXCEPTION);
        assertEquals("EVIDENCE_SEARCH_QUERY_EXCEPTION", ServiceConstants.EVIDENCE_SEARCH_QUERY_EXCEPTION);
        assertEquals("DOCUMENT_SEARCH_QUERY_EXCEPTION", ServiceConstants.DOCUMENT_SEARCH_QUERY_EXCEPTION);
        assertEquals("COMMENT_SEARCH_QUERY_EXCEPTION", ServiceConstants.COMMENT_SEARCH_QUERY_EXCEPTION);
        assertEquals("MDMS_DATA_NOT_FOUND", ServiceConstants.MDMS_DATA_NOT_FOUND);
    }
}
