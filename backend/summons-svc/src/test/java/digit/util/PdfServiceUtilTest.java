package digit.util;

import digit.config.Configuration;
import digit.web.models.*;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PdfServiceUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Configuration config;

    @Mock
    private Task task;

    @InjectMocks
    private PdfServiceUtil pdfServiceUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generatePdfFromPdfService_Success() {
        TaskRequest taskRequest = mock(TaskRequest.class);
        TaskDetails taskDetails = mock(TaskDetails.class);
        SummonsDetails summonsDetails = mock(SummonsDetails.class);
        CaseDetails caseDetails = mock(CaseDetails.class);
        RespondentDetails respondentDetails = mock(RespondentDetails.class);
        Address address = mock(Address.class);
        String tenantId = "tenant1";
        String pdfTemplateKey = "templateKey";
        ByteArrayResource byteArrayResource = new ByteArrayResource(new byte[]{1, 2, 3});

        when(config.getPdfServiceHost()).thenReturn("http://localhost");
        when(config.getPdfServiceEndpoint()).thenReturn("/pdf-service");
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ByteArrayResource.class)))
                .thenReturn(ResponseEntity.ok(byteArrayResource));
        when(taskRequest.getTask()).thenReturn(task);
        when(task.getTaskDetails()).thenReturn(taskDetails);
        when(taskDetails.getSummonDetails()).thenReturn(summonsDetails);
        when(taskDetails.getCaseDetails()).thenReturn(caseDetails);
        when(taskDetails.getRespondentDetails()).thenReturn(respondentDetails);
        when(respondentDetails.getAddress()).thenReturn(address);
        ByteArrayResource result = pdfServiceUtil.generatePdfFromPdfService(taskRequest, tenantId, pdfTemplateKey, false);

        assertNotNull(result);
        assertArrayEquals(new byte[]{1, 2, 3}, result.getByteArray());
    }

    @Test
    void generatePdfFromPdfService_Failure() {
        TaskRequest taskRequest = new TaskRequest();
        String tenantId = "tenant1";
        String pdfTemplateKey = "templateKey";

        when(config.getPdfServiceHost()).thenReturn("http://localhost");
        when(config.getPdfServiceEndpoint()).thenReturn("/pdf-service");
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ByteArrayResource.class)))
                .thenThrow(new RuntimeException("Service error"));

        CustomException exception = assertThrows(CustomException.class, () ->
                pdfServiceUtil.generatePdfFromPdfService(taskRequest, tenantId, pdfTemplateKey, false));

        assertEquals("SU_PDF_APP_ERROR", exception.getCode());
        assertEquals("Error getting response from Pdf Service", exception.getMessage());
    }
}