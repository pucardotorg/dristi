package org.pucar.dristi.web.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.pucar.dristi.service.OpenApiService;
import org.pucar.dristi.web.models.CaseListResponse;
import org.pucar.dristi.web.models.CaseSummaryResponse;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class OpenapiApiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private OpenApiService openApiService;

    @InjectMocks
    private OpenapiApiController openapiApiController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(openapiApiController).build();
    }

    @Test
    public void testGetCaseByCNR_ValidInput_Success() throws Exception {
        // Arrange
        String tenantId = "AB";
        String cnrNumber = "ABCDEFGHIJKLMNOP";
        CaseSummaryResponse mockResponse = new CaseSummaryResponse();

        when(openApiService.getCaseByCnrNumber(tenantId, cnrNumber))
                .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/openapi/v1/{tenantId}/case/cnr/{cnrNumber}", tenantId, cnrNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(openApiService).getCaseByCnrNumber(tenantId, cnrNumber);
    }

    @Test
    public void testGetCaseByCNR_InvalidTenantId_ShouldFail() throws Exception {
        // Arrange
        String tenantId = "A"; // Invalid tenant ID (less than 2 chars)
        String cnrNumber = "ABCDEFGHIJKLMNOP";

        // Act & Assert
        mockMvc.perform(get("/openapi/v1/{tenantId}/case/cnr/{cnrNumber}", tenantId, cnrNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetCaseByCaseNumber_ValidInput_Success() throws Exception {
        // Arrange
        String tenantId = "AB";
        Integer year = 2024;
        String caseType = "CMP";
        Integer caseNumber = 12345;
        CaseSummaryResponse mockResponse = new CaseSummaryResponse();

        // Act & Assert
        mockMvc.perform(get("/openapi/v1/{tenantId}/case/{year}/{caseType}", tenantId, year, caseType)
                        .param("caseNumber", caseNumber.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCaseByCaseNumber_InvalidYear_ShouldFail() throws Exception {
        // Arrange
        String tenantId = "AB";
        Integer year = 2023; // Below minimum allowed year
        String caseType = "CMP";
        Integer caseNumber = 12345;

        // Act & Assert
        mockMvc.perform(get("/openapi/v1/{tenantId}/case/{year}/{caseType}",  tenantId, year, caseType)
                        .param("caseNumber", caseNumber.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetCaseListByCaseType_ValidInput_Success() throws Exception {
        // Arrange
        String tenantId = "AB";
        Integer year = 2024;
        String caseType = "CMP";
        Integer offset = 0;
        Integer limit = 10;
        String sort = "registrationDate,asc";
        CaseListResponse mockResponse = new CaseListResponse();

        when(openApiService.getCaseListByCaseType(tenantId, year, caseType, offset, limit, sort))
                .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/openapi/v1/{tenantId}/case/{year}/{caseType}", tenantId, year, caseType)
                        .param("offset", offset.toString())
                        .param("limit", limit.toString())
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(openApiService).getCaseListByCaseType(tenantId, year, caseType, offset, limit, sort);
    }

    @Test
    public void testGetCaseListByCaseType_DefaultParameters_Success() throws Exception {
        // Arrange
        String tenantId = "AB";
        Integer year = 2024;
        String caseType = "CMP";
        CaseListResponse mockResponse = new CaseListResponse();

        when(openApiService.getCaseListByCaseType(tenantId, year, caseType, 0, 10, "registrationDate,desc"))
                .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/openapi/v1/{tenantId}/case/{year}/{caseType}", tenantId, year, caseType)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(openApiService).getCaseListByCaseType(tenantId, year, caseType, 0, 10, "registrationDate,desc");
    }

    @Test
    public void testGetCaseListByCaseType_InvalidSort_ShouldFail() throws Exception {
        // Arrange
        String tenantId = "AB";
        Integer year = 2024;
        String caseType = "CMP";
        String invalidSort = "invalidField,invalid";

        // Act & Assert
        mockMvc.perform(get("/openapi/v1/{tenantId}/case/{year}/{caseType}", tenantId, year, caseType)
                        .param("sort", invalidSort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getCaseByCaseNumber_ValidInput_Success() throws Exception {
        // Arrange
        String tenantId = "AB";
        Integer year = 2024;
        String caseType = "CMP";
        Integer caseNumber = 12345;
        CaseSummaryResponse mockResponse = new CaseSummaryResponse();

        // Act & Assert
        mockMvc.perform(get("/openapi/v1/{tenantId}/case/{year}/{caseType}/{caseNumber}", tenantId, year, caseType, caseNumber)
                        .contentType(MediaType.APPLICATION_JSON));
    }
}
