package org.egov.eTreasury.controller;

import org.egov.common.contract.models.Document;
import org.egov.eTreasury.model.*;
import org.egov.eTreasury.service.PaymentService;
import org.egov.eTreasury.util.ResponseInfoFactory;
import lombok.extern.slf4j.Slf4j;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    private final ResponseInfoFactory responseInfoFactory;

    public PaymentController(PaymentService paymentService, ResponseInfoFactory responseInfoFactory) {
        this.paymentService = paymentService;
        this.responseInfoFactory = responseInfoFactory;
    }

    @PostMapping("/v1/_verifyConnection")
    public ConnectionResponse verifyServerConnection(@RequestParam(value = "tenantId", required = false) String tenantId,@RequestBody RequestInfo request) {
        log.info("Verifying Server Connection for request: {}", request);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request, true);
        ConnectionStatus connectionStatus = paymentService.verifyConnection();
        log.info("Verified Server Connection for request: {}", request);
        return ConnectionResponse.builder().responseInfo(responseInfo).connectionStatus(connectionStatus).build();
    }

    @PostMapping("/v1/_processChallan")
    public HtmlResponse processPayment(@RequestBody ChallanRequest request) {
        log.info("Processing payment for request: {}", request);
        Payload paymentPage = paymentService.processPayment(request.getChallanData(), request.getRequestInfo());
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true);
        log.info("Payment processed successfully for request: {}", request);
        return HtmlResponse.builder().payload(paymentPage).responseInfo(responseInfo).build();
    }

    @PostMapping("/v1/_decryptTreasuryResponse")
    public TreasuryPaymentResponse decryptTreasuryResponse(@RequestBody TreasuryRequest request) {
        log.info("Decrypting Treasury Response for request: {}", request);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true);
        TreasuryPaymentData treasuryPaymentData = paymentService.decryptAndProcessTreasuryPayload(request.getTreasuryParams(), request.getRequestInfo());
        log.info("Decrypted Treasury Response successfully for request: {}", request);
        return TreasuryPaymentResponse.builder()
                .responseInfo(responseInfo).treasuryPaymentData(treasuryPaymentData).build();
    }

    @PostMapping("/v1/_getPaymentReceipt")
    public PrintResponse getTreasuryPaymentReceipt(@RequestParam String billId, @RequestBody RequestInfo requestInfo) {
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        Document document = paymentService.getTreasuryPaymentData(billId);
        return PrintResponse.builder()
                .responseInfo(responseInfo)
                .document(document).build();
    }

//    @PostMapping("/v1/_doubleVerification")
//    public HtmlResponse verifyDetails(@RequestBody VerificationRequest request) {
//        log.info("Performing double verification for request: {}", request);
//        Payload verificationPage = paymentService.doubleVerifyPayment(request.getVerificationData(), request.getRequestInfo());
//        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true);
//        log.info("Double verification successful for request: {}", request);
//        return HtmlResponse.builder().payload(verificationPage).responseInfo(responseInfo).build();
//    }

//    @PostMapping("/v1/_printPayInSlip")
//    public PrintResponse printPayInSlip(@RequestBody PrintRequest request) {
//        log.info("Fetching pay-in slip for details: {}", request);
//        Document document = paymentService.printPayInSlip(request.getPrintDetails(), request.getRequestInfo());
//        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true);
//        log.info("Pay-in slip fetched successfully for details: {}", request);
//        return PrintResponse.builder().responseInfo(responseInfo).document(document).build();
//    }

//    @PostMapping("/v1/_transactionDetails")
//    public TransactionResponse processTransaction(@RequestBody TransactionRequest request) {
//        log.info("Fetching transaction details for request: {}", request);
//        TransactionDetails transactionDetails = paymentService.fetchTransactionDetails(request.getTransactionDetails(), request.getRequestInfo());
//        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true);
//        log.info("Transaction details fetched successfully for request: {}", request);
//        return TransactionResponse.builder().transactionDetails(transactionDetails).responseInfo(responseInfo).build();
//    }

//    @PostMapping("/v1/_refundPayment")
//    public RefundResponse processRefundPayment(@RequestBody RefundRequest request) {
//        log.info("Processing refund for request: {}", request);
//        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true);
//        RefundData refundData = paymentService.processRefund(request.getRefundDetails(), request.getRequestInfo());
//        log.info("Refund processed successfully for request: {}", request);
//        return RefundResponse.builder().responseInfo(responseInfo).refundData(refundData).build();
//    }

//    @PostMapping("/v1/_refundStatus")
//    public RefundResponse checkRefundStatus(@RequestBody RefundStatusRequest request) {
//        log.info("Verifying refund status for request: {}", request);
//        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true);
//        RefundData refundData = paymentService.checkRefundStatus(request.getRefundStatus(), request.getRequestInfo());
//        log.info("Verified Refund Status successfully for request: {}", request);
//        return RefundResponse.builder().responseInfo(responseInfo).refundData(refundData).build();
//    }

}
