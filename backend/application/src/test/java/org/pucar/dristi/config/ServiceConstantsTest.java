package org.pucar.dristi.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceConstantsTest {

    @Test
    void testConstants() {
        assertEquals("External Service threw an Exception: ", ServiceConstants.EXTERNAL_SERVICE_EXCEPTION);
        assertEquals("Exception while fetching from searcher: ", ServiceConstants.SEARCHER_SERVICE_EXCEPTION);
        assertEquals("IDGEN ERROR", ServiceConstants.IDGEN_ERROR);
        assertEquals("No ids returned from idgen Service", ServiceConstants.NO_IDS_FOUND_ERROR);
        assertEquals("Exception occurred while fetching category lists from mdms: ", ServiceConstants.ERROR_WHILE_FETCHING_FROM_MDMS);
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
    }
}