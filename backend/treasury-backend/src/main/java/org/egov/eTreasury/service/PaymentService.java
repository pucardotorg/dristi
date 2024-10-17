package org.egov.eTreasury.service;

import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.eTreasury.config.PaymentConfiguration;
import org.egov.eTreasury.enrichment.TreasuryEnrichment;
import org.egov.eTreasury.kafka.Producer;
import org.egov.eTreasury.repository.TreasuryPaymentRepository;
import org.egov.eTreasury.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import digit.models.coremodels.PaymentDetail;
import lombok.extern.slf4j.Slf4j;
import org.egov.eTreasury.model.*;
import org.egov.eTreasury.repository.AuthSekRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.egov.eTreasury.config.ServiceConstants.*;

@Service
@Slf4j
public class PaymentService {

    private final PaymentConfiguration config;

    private final ETreasuryUtil treasuryUtil;

    private final ObjectMapper objectMapper;

    private final EncryptionUtil encryptionUtil;

    private final Producer producer;

    private final AuthSekRepository repository;

    private final CollectionsUtil collectionsUtil;

    private final FileStorageUtil fileStorageUtil;

    private final TreasuryPaymentRepository treasuryPaymentRepository;

    private final PdfServiceUtil pdfServiceUtil;

    private final TreasuryEnrichment treasuryEnrichment;

    @Autowired
    public PaymentService(PaymentConfiguration config, ETreasuryUtil treasuryUtil,
                          ObjectMapper objectMapper, EncryptionUtil encryptionUtil,
                          Producer producer, AuthSekRepository repository, CollectionsUtil collectionsUtil,
                          FileStorageUtil fileStorageUtil, TreasuryPaymentRepository treasuryPaymentRepository, PdfServiceUtil pdfServiceUtil, TreasuryEnrichment treasuryEnrichment) {
        this.config = config;
        this.treasuryUtil = treasuryUtil;
        this.objectMapper = objectMapper;
        this.encryptionUtil = encryptionUtil;
        this.producer = producer;
        this.repository = repository;
        this.collectionsUtil = collectionsUtil;
        this.fileStorageUtil = fileStorageUtil;
        this.treasuryPaymentRepository = treasuryPaymentRepository;
        this.pdfServiceUtil = pdfServiceUtil;
        this.treasuryEnrichment = treasuryEnrichment;
    }

    public ConnectionStatus verifyConnection() {
        try {
            ResponseEntity<String> responseEntity = treasuryUtil.callConnectionService(config.getServerStatusUrl(), String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                return objectMapper.readValue(responseEntity.getBody(), ConnectionStatus.class);
            } else {
                throw new CustomException(AUTHENTICATION_FAILED, "Authentication request failed with status: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Establishing a connection with ETreasury server failed: ", e);
            return ConnectionStatus.builder().status("FAIL").build();
        }
    }

    private Map<String, String> authenticate() {
        Map<String, String> secretMap;
        try {
            // Generate client secret and app key
            secretMap = encryptionUtil.getClientSecretAndAppKey(config.getClientSecret(), config.getPublicKey());
            // Prepare authentication request payload
            AuthRequest authRequest = new AuthRequest(secretMap.get("encodedAppKey"));
            String payload = objectMapper.writeValueAsString(authRequest);
            // Call the authentication service
            ResponseEntity<?> responseEntity = treasuryUtil.callAuthService(config.getClientId(), secretMap.get("encryptedClientSecret"),
            payload, config.getAuthUrl());
            log.info("Status Code: {}", responseEntity.getStatusCode());
            log.info("Response Body: {}", responseEntity.getBody());
            // Process the response
            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                AuthResponse response = objectMapper.convertValue(responseEntity.getBody(), AuthResponse.class);
                secretMap.put("sek", response.getData().getSek());
                secretMap.put(AUTH_TOKEN, response.getData().getAuthToken());
            } else {
               throw new CustomException(AUTHENTICATION_FAILED, "Authentication request failed with status: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Authentication process failed: ", e);
            throw new CustomException(AUTHENTICATION_ERROR, "Error occurred during authentication");
        }
        return secretMap;
    }

    public Payload processPayment(ChallanData challanData, RequestInfo requestInfo) {
        try {
            // Authenticate and get secret map
            Map<String, String> secretMap = authenticate();

            // Decrypt the SEK using the appKey
            String decryptedSek = encryptionUtil.decryptAES(secretMap.get("sek"), secretMap.get("appKey"));

            // Prepare the request body
            ChallanDetails challanDetails  = treasuryEnrichment.generateChallanDetails(challanData, requestInfo);

            AuthSek authSek = buildAuthSek(challanData, secretMap, decryptedSek, challanDetails.getDepartmentId());
            saveAuthTokenAndSek(requestInfo, authSek);

            String postBody = generatePostBody(decryptedSek, objectMapper.writeValueAsString(challanDetails));

            // Prepare headers
            Headers headers = new Headers();
            headers.setClientId(config.getClientId());
            headers.setAuthToken(secretMap.get(AUTH_TOKEN));
            String headersData = objectMapper.writeValueAsString(headers);

            return Payload.builder()
                    .url(config.getChallanGenerateUrl())
                    .data(postBody).headers(headersData).build();
        } catch (Exception e) {
            log.error("Payment processing error: ", e);
            throw new CustomException(PAYMENT_PROCESSING_ERROR, "Error occurred during generation oF challan");
        }
    }

    private AuthSek buildAuthSek(ChallanData challanData, Map<String, String> secretMap, String decryptedSek, String departmentId) {
        return AuthSek.builder()
                .authToken(secretMap.get(AUTH_TOKEN))
                .decryptedSek(decryptedSek)
                .billId(challanData.getBillId())
                .businessService(challanData.getBusinessService())
                .serviceNumber(challanData.getServiceNumber())
                .mobileNumber(challanData.getMobileNumber())
                .totalDue(challanData.getTotalDue())
                .paidBy(challanData.getPaidBy())
                .sessionTime(System.currentTimeMillis())
                .departmentId(departmentId)
                .build();
    }

    public String printPayInSlipPdf(TreasuryPaymentRequest request) {
        try {
            ByteArrayResource byteArrayResource = pdfServiceUtil.generatePdfFromPdfService(request);
            return fileStorageUtil.saveDocumentToFileStore(byteArrayResource.getByteArray()).getFileStore();
        } catch (Exception e) {
            log.error("Error occurred when creating pdf for payment", e);
            return null;
        }
    }

    public TreasuryPaymentData decryptAndProcessTreasuryPayload(TreasuryParams treasuryParams, RequestInfo requestInfo) {
        log.info("Decrypting Treasury Payload for authToken: {}", treasuryParams.getAuthToken());

        try {
            Optional<AuthSek> optionalAuthSek = repository.getAuthSek(treasuryParams.getAuthToken()).stream().findFirst();
            if (optionalAuthSek.isEmpty()) {
                log.error("No AuthSek found for authToken: {}", treasuryParams.getAuthToken());
                throw new CustomException(AUTH_SEK_NOT_FOUND, "No AuthSek found for the provided authToken");
            }

            AuthSek authSek = optionalAuthSek.get();
            String decryptedSek = authSek.getDecryptedSek();
            String decryptedRek = encryptionUtil.decryptResponse(treasuryParams.getRek(), decryptedSek);
            String decryptedData = encryptionUtil.decryptResponse(treasuryParams.getData(), decryptedRek);

            log.info("Decrypted data: {}", decryptedData);

            TransactionDetails transactionDetails = objectMapper.readValue(decryptedData, TransactionDetails.class);
            TreasuryPaymentData data = createTreasuryPaymentData(transactionDetails, authSek);

            requestInfo.getUserInfo().setTenantId(config.getEgovStateTenantId());

            log.info("Request info: {}", requestInfo);

            TreasuryPaymentRequest request = TreasuryPaymentRequest.builder()
                    .requestInfo(requestInfo)
                    .treasuryPaymentData(data)
                    .build();

            String fileStore = printPayInSlipPdf(request);
            data.setFileStoreId(fileStore);

            log.info("Saving Payment Data: {}", data);

            producer.push(config.getSaveTreasuryPaymentData(), request);

            return data;

        } catch (Exception e) {
            log.error("Error occurred during decrypting Treasury Response: ", e);
            throw new CustomException(TREASURY_RESPONSE_ERROR, "Error occurred during decrypting Treasury Response");
        }
    }

    private TreasuryPaymentData createTreasuryPaymentData(TransactionDetails transactionDetails, AuthSek authSek) {
        return TreasuryPaymentData.builder()
                .grn(transactionDetails.getGrn())
                .challanTimestamp(transactionDetails.getChallanTimestamp())
                .bankRefNo(transactionDetails.getBankRefNo())
                .bankTimestamp(transactionDetails.getBankTimestamp())
                .bankCode(transactionDetails.getBankCode())
                .status(transactionDetails.getStatus().charAt(0))
                .cin(transactionDetails.getCin())
                .amount(new BigDecimal(transactionDetails.getAmount()))
                .partyName(transactionDetails.getPartyName())
                .departmentId(transactionDetails.getDepartmentId())
                .remarkStatus(transactionDetails.getRemarkStatus())
                .remarks(transactionDetails.getRemarks())
                .billId(authSek.getBillId())
                .businessService(authSek.getBusinessService())
                .totalDue(authSek.getTotalDue())
                .mobileNumber(authSek.getMobileNumber())
                .tenantId(config.getEgovStateTenantId())
                .paidBy(authSek.getPaidBy())
                .build();
    }


    private void saveAuthTokenAndSek(RequestInfo requestInfo, AuthSek authSek) {
        AuthSekRequest request = new AuthSekRequest(requestInfo, authSek);
        producer.push("save-auth-sek", request);
    }

    private String generatePostBody(String decryptedSek, String jsonData) {
        try {
            // Convert SEK to AES key
            SecretKey aesKey = new SecretKeySpec(decryptedSek.getBytes(StandardCharsets.UTF_8), "AES");

            // Initialize AES cipher in encryption mode
            Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);

            // Encrypt JSON data
            byte[] encryptedDataBytes = aesCipher.doFinal(jsonData.getBytes(StandardCharsets.UTF_8));
            String encryptedData = Base64.getEncoder().encodeToString(encryptedDataBytes);

            // Generate HMAC using JSON data and SEK
            String hmac = encryptionUtil.generateHMAC(jsonData, decryptedSek);

            // Create PostBody object and convert to JSON string
            PostBody postBody = new PostBody(hmac, encryptedData);
            return objectMapper.writeValueAsString(postBody);
        } catch (Exception e) {
            log.error("Error during post body generation: ", e);
            throw new CustomException("POST_BODY_GENERATION_ERROR", "Error occurred generating post body");
        }
    }

    private Long convertTimestampToMillis(String timestampStr) {
        List<DateTimeFormatter> formatters = new ArrayList<>();
        formatters.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
        formatters.add(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss.SSSSS"));
        LocalDateTime dateTime = null;
        for (DateTimeFormatter formatter : formatters) {
            try {
                dateTime = LocalDateTime.parse(timestampStr, formatter);
                break;
            } catch (Exception e) {
                // Try next formatter if parsing fails
            }
        }
        if (dateTime != null) {
            return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        } else {
            return null;
        }
    }
    public Document getTreasuryPaymentData(String billId) {
        Optional<TreasuryPaymentData> optionalPaymentData = treasuryPaymentRepository.getTreasuryPaymentData(billId)
                .stream().findFirst();
        if (optionalPaymentData.isPresent()) {
            return  Document.builder().fileStore(optionalPaymentData.get().getFileStoreId()).documentType("application/pdf").build();
        } else {
            log.error("No Payment data for given bill Id");
            throw new CustomException(INVALID_BILL_ID, "Given Bill Id Has no Payment Data");
        }
    }

    public void callCollectionServiceAndUpdatePayment(TreasuryPaymentRequest request) {

        String paymentStatus = String.valueOf(request.getTreasuryPaymentData().getStatus());
        BigDecimal totalAmountPaid = new BigDecimal(String.valueOf(request.getTreasuryPaymentData().getAmount()));
        if (paymentStatus.equals("N")) {
            if (config.isTest()) {
                totalAmountPaid = BigDecimal.valueOf(request.getTreasuryPaymentData().getTotalDue());
            }
        }

        PaymentDetail paymentDetail = PaymentDetail.builder()
                .billId(request.getTreasuryPaymentData().getBillId())
                .totalDue(BigDecimal.valueOf(request.getTreasuryPaymentData().getTotalDue()))
                .totalAmountPaid(totalAmountPaid)
                .businessService(request.getTreasuryPaymentData().getBusinessService()).build();
        Payment payment = Payment.builder()
                .tenantId(config.getEgovStateTenantId())
                .paymentDetails(Collections.singletonList(paymentDetail))
                .payerName(request.getTreasuryPaymentData().getPartyName())
                .paidBy(request.getTreasuryPaymentData().getPaidBy())
                .mobileNumber(request.getTreasuryPaymentData().getMobileNumber())
                .transactionNumber(request.getTreasuryPaymentData().getGrn())
                .transactionDate(convertTimestampToMillis(request.getTreasuryPaymentData().getChallanTimestamp()))
                .instrumentNumber(request.getTreasuryPaymentData().getBankRefNo())
                .instrumentDate(convertTimestampToMillis(request.getTreasuryPaymentData().getBankTimestamp()))
                .totalAmountPaid(new BigDecimal(String.valueOf(request.getTreasuryPaymentData().getAmount())))
                .paymentMode("ONLINE")
                .fileStoreId(request.getTreasuryPaymentData().getFileStoreId())
                .build();

        if (paymentStatus.equals("Y")) {
            payment.setPaymentStatus("DEPOSITED");
        }
        PaymentRequest paymentRequest = new PaymentRequest(request.getRequestInfo(), payment);
        collectionsUtil.callService(paymentRequest, config.getCollectionServiceHost(), config.getCollectionsPaymentCreatePath());
    }

//    public Payload doubleVerifyPayment(VerificationData verificationData, RequestInfo requestInfo) {
//        try {
//            VerificationDetails verificationDetails = verificationData.getVerificationDetails();
//            // Authenticate and get secret map
//            Map<String, String> secretMap = authenticate();
//
//            // Decrypt the SEK using the appKey
//            String decryptedSek = encryptionUtil.decryptAES(secretMap.get("sek"), secretMap.get("appKey"));
//            AuthSek authSek = AuthSek.builder()
//                    .authToken(secretMap.get("authToken"))
//                    .decryptedSek(decryptedSek)
//                    .billId(verificationData.getBillId())
//                    .businessService(verificationData.getBusinessService())
//                    .serviceNumber(verificationData.getServiceNumber())
//                    .totalDue(verificationData.getTotalDue())
//                    .paidBy(verificationData.getPaidBy())
//                    .sessionTime(System.currentTimeMillis())
//                    .departmentId(verificationDetails.getDepartmentId()).build();
//            saveAuthTokenAndSek(requestInfo, authSek);
//
//            // Prepare the request body
//            verificationDetails.setOfficeCode(config.getOfficeCode());
//            verificationDetails.setServiceDeptCode(config.getServiceDeptCode());
//            String postBody = generatePostBody(decryptedSek, objectMapper.writeValueAsString(verificationDetails));
//
//            // Prepare headers
//            Headers headers = new Headers();
//            headers.setClientId(config.getClientId());
//            headers.setAuthToken(secretMap.get("authToken"));
//            String headersData = objectMapper.writeValueAsString(headers);
//
//            return Payload.builder()
//                    .url(config.getDoubleVerificationUrl())
//                    .data(postBody).headers(headersData).build();
//        } catch (Exception e) {
//            log.error("Double verification Error: ", e);
//            throw new CustomException("DOUBLE_VERIFICATION_ERROR", "Error occurred during double verification");
//        }
//    }

//    public Document printPayInSlip(PrintDetails printDetails, RequestInfo requestInfo) {
//        try {
//            // Authenticate and get secret map
//            Map<String, String> secretMap = authenticate();
//
//            // Decrypt the SEK using the appKey
//            String decryptedSek = encryptionUtil.decryptAES(secretMap.get("sek"), secretMap.get("appKey"));
//
//            // Prepare the request body
//            String postBody = generatePostBody(decryptedSek, objectMapper.writeValueAsString(printDetails));
//
//            // Prepare headers
//            Headers headers = new Headers();
//            headers.setClientId(config.getClientId());
//            headers.setAuthToken(secretMap.get("authToken"));
//            String headersData = objectMapper.writeValueAsString(headers);
//
//            // Call the service
//            ResponseEntity<byte[]> responseEntity = callService(headersData, postBody, config.getPrintSlipUrl(), byte[].class, MediaType.MULTIPART_FORM_DATA);
//
//            // Process the response
//            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
//                 return fileStorageUtil.saveDocumentToFileStore(responseEntity.getBody());
//            } else {
//                throw new CustomException("PRINT_SLIP_FAILED", "Pay in slip request failed");
//            }
//        } catch (Exception e) {
//            log.error("Print slip generation Error: ", e);
//            throw new CustomException("PRINT_SLIP_ERROR", "Error occurred during pay in slip generation");
//        }
//    }


//    public TransactionDetails fetchTransactionDetails(TransactionDetails transactionDetails, RequestInfo requestInfo) {
//        try {
//            // Authenticate and get secret map
//            Map<String, String> secretMap = authenticate();
//
//            // Decrypt the SEK using the appKey
//            String decryptedSek = encryptionUtil.decryptAES(secretMap.get("sek"), secretMap.get("appKey"));
//
//            // Prepare the request body
//            transactionDetails.setDepartmentId(config.getDeptReferenceId());
//            String postBody = generatePostBody(decryptedSek, objectMapper.writeValueAsString(transactionDetails));
//
//            // Prepare headers
//            Headers headers = new Headers();
//            headers.setClientId(config.getClientId());
//            headers.setAuthToken(secretMap.get("authToken"));
//            String headersData = objectMapper.writeValueAsString(headers);
//
//            // Call the service
//            ResponseEntity<TransactionDetails> responseEntity = callService(headersData, postBody, config.getTransactionDetailsUrl(), TransactionDetails.class, MediaType.APPLICATION_JSON);
//            return objectMapper.convertValue(responseEntity.getBody(), TransactionDetails.class);
//        } catch (Exception e) {
//            log.error("Transaction details retrieval failed: ", e);
//            throw new CustomException("TRANSACTION_DETAILS_ERROR", "Error ccurred during transaction details retrieval");
//        }
//    }

//    public RefundData processRefund(RefundDetails refundDetails, RequestInfo requestInfo) {
//        try {
//            // Authenticate and get secret map
//            Map<String, String> secretMap = authenticate();
//
//            // Decrypt the SEK using the appKey
//            String decryptedSek = encryptionUtil.decryptAES(secretMap.get("sek"), secretMap.get("appKey"));
//
//            // Prepare the request body
//            String postBody = generatePostBodyForRefund(decryptedSek, objectMapper.writeValueAsString(refundDetails));
//
//            // Call the service
//            ResponseEntity<TreasuryResponse> responseEntity = treasuryUtil.callRefundService(config.getClientId(), secretMap.get("authToken"), postBody, config.getRefundRequestUrl(), TreasuryResponse.class);
//            TreasuryResponse response = responseEntity.getBody();
//            String decryptedRek = encryptionUtil.decryptResponse(response.getRek(), decryptedSek);
//            String decryptedData = encryptionUtil.decryptResponse(response.getData(), decryptedRek);
//
//            return objectMapper.convertValue(decryptedData, RefundData.class);
//        } catch (Exception e) {
//            log.error("Refund Request failed: ", e);
//            throw new CustomException("REFUND_REQUEST_ERROR", "Error occurred during  refund request");
//        }
//    }

//    public RefundData checkRefundStatus(RefundStatus refundStatus, RequestInfo requestInfo) {
//        try {
//            // Authenticate and get secret map
//            Map<String, String> secretMap = authenticate();
//
//            // Decrypt the SEK using the appKey
//            String decryptedSek = encryptionUtil.decryptAES(secretMap.get("sek"), secretMap.get("appKey"));
//
//            // Prepare the request body
//            String postBody = generatePostBodyForRefund(decryptedSek, objectMapper.writeValueAsString(refundStatus));
//
//            // Call the service
//            ResponseEntity<TreasuryResponse> responseEntity = treasuryUtil.callRefundService(config.getClientId(),
//            secretMap.get("authToken"), postBody, config.getRefundStatusUrl(), TreasuryResponse.class);
//            TreasuryResponse response = responseEntity.getBody();
//            String decryptedRek = encryptionUtil.decryptResponse(response.getRek(), decryptedSek);
//            String decryptedData = encryptionUtil.decryptResponse(response.getData(), decryptedRek);
//
//            return objectMapper.convertValue(decryptedData, RefundData.class);
//        } catch (Exception e) {
//            log.error("Refund Request failed: ", e);
//            throw new CustomException("REFUND_REQUEST_ERROR", "Error occurred during  refund request");
//        }
//    }


//    private <T> ResponseEntity<T> callService(String headersData, String postBody, String url, Class<T> responseType, MediaType mediaType) {
//        return treasuryUtil.callService(headersData, postBody, url, responseType, mediaType);
//    }



//    private String generatePostBodyForRefund(String decryptedSek, String jsonData) {
//        try {
//            // Convert SEK to AES key
//            SecretKey aesKey = new SecretKeySpec(decryptedSek.getBytes(StandardCharsets.UTF_8), "AES");
//
//            // Initialize AES cipher in encryption mode
//            Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//            aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
//
//            // Encrypt JSON data
//            byte[] encryptedDataBytes = aesCipher.doFinal(jsonData.getBytes(StandardCharsets.UTF_8));
//            String encryptedData = Base64.getEncoder().encodeToString(encryptedDataBytes);
//
//            // Generate HMAC using JSON data and SEK
//            String hmac = encryptionUtil.generateHMAC(jsonData, decryptedSek);
//
//            // Create PostBody object and convert to JSON string
//            RefundPostBody refundPostBody = new RefundPostBody(hmac, encryptedData);
//            return objectMapper.writeValueAsString(refundPostBody);
//        } catch (Exception e) {
//            log.error("Error during post body generation: ", e);
//            throw new CustomException("POST_BODY_GENERATION_ERROR", "Error occurred generating post body");
//        }
//    }

//    private void updatePaymentStatus(AuthSek authSek, TransactionDetails transactionDetails, RequestInfo requestInfo, String fileStoreId) {
//        log.info("Updating payment status for billId: {}", authSek.getBillId());
//        PaymentDetail paymentDetail = PaymentDetail.builder()
//            .billId(authSek.getBillId())
//            .totalDue(BigDecimal.valueOf(authSek.getTotalDue()))
//            .totalAmountPaid(new BigDecimal(transactionDetails.getAmount()))
//            .businessService(authSek.getBusinessService()).build();
//        Payment payment = Payment.builder()
//            .tenantId(config.getEgovStateTenantId())
//            .paymentDetails(Collections.singletonList(paymentDetail))
//            .payerName(transactionDetails.getPartyName())
//            .paidBy(authSek.getPaidBy())
//            .mobileNumber(authSek.getMobileNumber())
//            .transactionNumber(transactionDetails.getGrn())
//            .transactionDate(convertTimestampToMillis(transactionDetails.getChallanTimestamp()))
//            .instrumentNumber(transactionDetails.getBankRefNo())
//            .instrumentDate(convertTimestampToMillis(transactionDetails.getBankTimestamp()))
//            .totalAmountPaid(new BigDecimal(transactionDetails.getAmount()))
//            .paymentMode("ONLINE")
//            .fileStoreId(fileStoreId)
//            .build();
//        String paymentStatus = transactionDetails.getStatus();
//        if (paymentStatus.equals("Y")) {
//            payment.setPaymentStatus("DEPOSITED");
//        }
//        PaymentRequest paymentRequest = new PaymentRequest(requestInfo, payment);
//        collectionsUtil.callService(paymentRequest, config.getCollectionServiceHost(), config.getCollectionsPaymentCreatePath());
//        log.info("Payment request sent to collections service: {}", paymentRequest);
//    }

}
