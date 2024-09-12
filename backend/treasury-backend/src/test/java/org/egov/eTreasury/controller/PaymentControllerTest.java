package org.egov.eTreasury.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eTreasury.model.*;
import org.egov.eTreasury.service.PaymentService;
import org.egov.eTreasury.util.ResponseInfoFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    void testVerifyServerConnection(){
        // Arrange
        ConnectionStatus connectionStatus = ConnectionStatus.builder().status("success").build();
        ResponseInfo responseInfo = new ResponseInfo();

        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), anyBoolean()))
                .thenReturn(responseInfo);
        when(paymentService.verifyConnection()).thenReturn(connectionStatus);

        // Act
        ConnectionResponse responseEntity = paymentController.verifyServerConnection("kl",
                new RequestInfo()
        );

        // Assert
        assertEquals(connectionStatus.getStatus(), responseEntity.getConnectionStatus().getStatus());
    }

    @Test
    void testProcessPayment(){
        // Arrange
        Payload payload = new Payload();

        when(paymentService.processPayment(any(), any())).thenReturn(payload);

        // Act
        HtmlResponse responseEntity = paymentController.processPayment(new ChallanRequest());

        // Assert
        assertNotNull(responseEntity);
    }

//    @Test
//    void testPrintPayInSlip() {
//        // Arrange
//        Document document = new Document();
//
//        when(paymentService.printPayInSlip(any(), any())).thenReturn(document);
//
//        // Act
//        PrintResponse responseEntity = paymentController.printPayInSlip(new PrintRequest());
//
//        // Assert
//        assertNotNull(responseEntity);
//    }

    @Test
    void testDecryptTreasuryResponse() {
        //ResponseEntity<TreasuryPaymentResponse> responseEntity = paymentController.decryptTreasuryResponse(new TreasuryRequest());
        //assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetTreasuryPaymentReceipt() {
        // Arrange
        Document document = new Document();


        when(paymentService.getTreasuryPaymentData(anyString())).thenReturn(document);

        // Act
        PrintResponse responseEntity = paymentController.getTreasuryPaymentReceipt(
                "bill123",
                new RequestInfo()
        );

        // Assert
        assertNotNull(responseEntity);
    }
}
