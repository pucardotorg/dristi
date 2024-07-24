package org.pucar.dristi.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.DemandRequest;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class BillingUtilTest {
    @InjectMocks
    private BillingUtil billingUtil;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Configuration configs;

    @Test
    public void createDemand_createsDemandSuccessfully() {
        CaseRequest caseRequest = new CaseRequest();
        CourtCase caseObj = new CourtCase();
        caseObj.setTenantId("tenantId");
        caseObj.setFilingNumber("filingNumber");
        caseRequest.setCases(caseObj);
        caseRequest.setRequestInfo(new RequestInfo());

        when(configs.getBillingHost()).thenReturn("http://localhost:8080");
        when(configs.getDemandCreateEndPoint()).thenReturn("/demand/create");
        when(configs.getCaseBusinessServiceName()).thenReturn("caseBusinessServiceName");

        Map<String, Object> response = new HashMap<>();
        when(restTemplate.postForObject(anyString(), any(DemandRequest.class), eq(Map.class))).thenReturn(response);

        billingUtil.createDemand(caseRequest);

        verify(restTemplate, times(1)).postForObject(anyString(), any(DemandRequest.class), eq(Map.class));
    }

    @Test
    public void createDemand_throwsExceptionWhenRestTemplateFails() {
        CaseRequest caseRequest = new CaseRequest();
        CourtCase caseObj = new CourtCase();
        caseObj.setTenantId("tenantId");
        caseObj.setFilingNumber("filingNumber");
        caseRequest.setCases(caseObj);
        caseRequest.setRequestInfo(new RequestInfo());

        when(configs.getBillingHost()).thenReturn("http://localhost:8080");
        when(configs.getDemandCreateEndPoint()).thenReturn("/demand/create");
        when(configs.getCaseBusinessServiceName()).thenReturn("caseBusinessServiceName");

        when(restTemplate.postForObject(anyString(), any(DemandRequest.class), eq(Map.class))).thenThrow(new RuntimeException());

        assertThrows(CustomException.class, () -> billingUtil.createDemand(caseRequest));
    }

}