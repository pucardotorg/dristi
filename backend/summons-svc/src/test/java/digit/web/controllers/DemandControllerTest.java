package digit.web.controllers;

import digit.service.DemandService;
import digit.web.models.BillResponse;
import digit.web.models.TaskRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DemandControllerTest {

    @Mock
    private DemandService demandService;

    @InjectMocks
    private DemandController demandController;

    @Mock
    private TaskRequest taskRequest;

    @Mock
    private BillResponse billResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateDemandAndBillForTask_Success() {
        // Arrange
        when(demandService.fetchPaymentDetailsAndGenerateDemandAndBill(taskRequest)).thenReturn(billResponse);

        // Act
        ResponseEntity<BillResponse> response = demandController.generateDemandAndBillForTask(taskRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(billResponse, response.getBody());
    }

    @Test
    void generateBillForTask_Success() {
        // Arrange
        when(demandService.getBill(taskRequest.getRequestInfo(), taskRequest.getTask())).thenReturn(billResponse);

        // Act
        ResponseEntity<BillResponse> response = demandController.generateBillForTask(taskRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(billResponse, response.getBody());
    }

    @Test
    void generateDemandAndBillForTask_Exception() {
        // Arrange
        when(demandService.fetchPaymentDetailsAndGenerateDemandAndBill(taskRequest)).thenThrow(new RuntimeException("Test exception"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> demandController.generateDemandAndBillForTask(taskRequest));
    }

    @Test
    void generateBillForTask_Exception() {
        // Arrange
        when(demandService.getBill(taskRequest.getRequestInfo(), taskRequest.getTask())).thenThrow(new RuntimeException("Test exception"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> demandController.generateBillForTask(taskRequest));
    }
}
