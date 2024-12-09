package com.pucar.drishti.web.controllers;

import com.pucar.drishti.config.Configuration;
import com.pucar.drishti.service.InterceptorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EsignApiControllerTest {

    @Mock
    private InterceptorService interceptorService;

    @Mock
    private Configuration configuration;

    @InjectMocks
    private InterceptorApiController interceptorApiController;

    @BeforeEach
    public void setup() {
        interceptorApiController = new InterceptorApiController(interceptorService, configuration);
    }

    @Test
    public void testRedirectHandler() {
        String result = "someResult";
        String filestoreId = "someFilestoreId";
        String userType = "employee";
        String redirectUrl = "http://example.com/";

        when(configuration.getRedirectUrl()).thenReturn(redirectUrl);

        ResponseEntity<HttpHeaders> responseEntity = interceptorApiController.redirectHandler(result, filestoreId, userType);

        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.setLocation(URI.create(redirectUrl + userType + "/dristi?result=" + result + "&filestoreId=" + filestoreId));

        assertEquals(HttpStatus.TEMPORARY_REDIRECT, responseEntity.getStatusCode());
        assertEquals(expectedHeaders.getLocation(), responseEntity.getHeaders().getLocation());
    }

    @Test
    public void testESignV1Interceptor_Success() throws Exception {
        String response = "someResponse";
        String espTxnID = "tenantId-en-fileStoreId"; // Ensure espTxnID is in the expected format
        String tenantId = "tenantId";
        String pageModule = "en";
        String fileStoreId = "fileStoreId";
        String filestoreId = "someFilestoreId"; // Expected returned filestoreId

        when(interceptorService.process(response, espTxnID, tenantId, fileStoreId)).thenReturn(filestoreId);

        ModelAndView modelAndView = interceptorApiController.eSignV1Interceptor(response, espTxnID);

        assertEquals("redirect:/v1/redirect", modelAndView.getViewName());
        assertEquals("success", modelAndView.getModel().get("result"));
        assertEquals(filestoreId, modelAndView.getModel().get("filestoreId"));
        assertEquals("employee", modelAndView.getModel().get("userType"));
    }

    @Test
    public void testESignV1Interceptor_Error() {
        String response = "someResponse";
        String espTxnID = "tenantId-en-fileStoreId"; // Ensure espTxnID is in the expected format
        String tenantId = "tenantId";
        String fileStoreId = "fileStoreId";

        when(interceptorService.process(response, espTxnID, tenantId, fileStoreId)).thenThrow(new RuntimeException("Error"));

        ModelAndView modelAndView = interceptorApiController.eSignV1Interceptor(response, espTxnID);

        assertEquals("redirect:/v1/redirect", modelAndView.getViewName());
        assertEquals("error", modelAndView.getModel().get("result"));
        assertEquals("", modelAndView.getModel().get("filestoreId"));
        assertEquals("employee", modelAndView.getModel().get("userType"));
    }

}