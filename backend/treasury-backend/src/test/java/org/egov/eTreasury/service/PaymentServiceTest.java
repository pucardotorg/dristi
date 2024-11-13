package org.egov.eTreasury.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.eTreasury.config.PaymentConfiguration;
import org.egov.eTreasury.kafka.Producer;
import org.egov.eTreasury.model.*;
import org.egov.eTreasury.repository.AuthSekRepository;
import org.egov.eTreasury.repository.TreasuryPaymentRepository;
import org.egov.eTreasury.util.*;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentConfiguration config;
    @Mock
    private ETreasuryUtil treasuryUtil;
    @Mock
    private Producer producer;
    @Mock
    private CollectionsUtil collectionsUtil;
    @Mock
    private TreasuryPaymentRepository treasuryPaymentRepository;
    @Mock
    private AuthSekRepository authSekRepository;
    @Mock
    private EncryptionUtil encryptionUtil;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private TransactionDetails transactionDetails;

    @Test
    void verifyConnection_success() {
        ConnectionStatus mockStatus = new ConnectionStatus();
        when(config.getServerStatusUrl()).thenReturn("http://test-url.com");
        when(treasuryUtil.callConnectionService(anyString(), any()))
                .thenReturn(ResponseEntity.ok(mockStatus));

        ConnectionStatus result = paymentService.verifyConnection();

        assertNotNull(result);
    }

    @Test
    void verifyConnection_failure() {
        when(config.getServerStatusUrl()).thenReturn("http://test-url.com");
        when(treasuryUtil.callConnectionService(anyString(), eq(ConnectionStatus.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

        ConnectionStatus result = paymentService.verifyConnection();

        assertNotNull(result);
    }

    @Test
    void getTreasuryPaymentData_success() {
        String testBillId = "testBillId";
        TreasuryPaymentData mockPaymentData = new TreasuryPaymentData();
        mockPaymentData.setFileStoreId("testFileStoreId");

        when(treasuryPaymentRepository.getTreasuryPaymentData(testBillId))
                .thenReturn(Collections.singletonList(mockPaymentData));

        Document result = paymentService.getTreasuryPaymentData(testBillId);

        assertNotNull(result);
        assertEquals("testFileStoreId", result.getFileStore());
        assertEquals("application/pdf", result.getDocumentType());
    }

    @Test
    void getTreasuryPaymentData_notFound() {
        String testBillId = "nonExistentBillId";

        when(treasuryPaymentRepository.getTreasuryPaymentData(testBillId))
                .thenReturn(Collections.emptyList());

        assertThrows(CustomException.class, () -> paymentService.getTreasuryPaymentData(testBillId));
    }

    @Test
    void callCollectionServiceAndUpdatePayment_success() {
        TreasuryPaymentRequest mockRequest = mock(TreasuryPaymentRequest.class);
        TreasuryPaymentData mockPaymentData = mock(TreasuryPaymentData.class);
        when(mockRequest.getTreasuryPaymentData()).thenReturn(mockPaymentData);
        mockRequest.setTreasuryPaymentData(mockPaymentData);
        when(mockRequest.getTreasuryPaymentData()).thenReturn(mockPaymentData);
        when(mockPaymentData.getAmount()).thenReturn(BigDecimal.valueOf(10));
        when(mockPaymentData.getBillId()).thenReturn("testBillId");
        paymentService.callCollectionServiceAndUpdatePayment(mockRequest);
        assertNotEquals('Y', mockRequest.getTreasuryPaymentData().getStatus());
    }

    @Test
    void callCollectionServiceAndUpdatePayment_success_1() {
        TreasuryPaymentRequest mockRequest = mock(TreasuryPaymentRequest.class);
        TreasuryPaymentData mockPaymentData = mock(TreasuryPaymentData.class);
        when(mockRequest.getTreasuryPaymentData()).thenReturn(mockPaymentData);
        mockRequest.setTreasuryPaymentData(mockPaymentData);
        when(mockRequest.getTreasuryPaymentData()).thenReturn(mockPaymentData);
        when(mockPaymentData.getAmount()).thenReturn(BigDecimal.valueOf(10));
        when(mockPaymentData.getBillId()).thenReturn("testBillId");
        Character str = 'Y';
        when(mockRequest.getTreasuryPaymentData().getStatus()).thenReturn(str);
        paymentService.callCollectionServiceAndUpdatePayment(mockRequest);
        assertEquals(str, mockRequest.getTreasuryPaymentData().getStatus());
    }

    @Test
    void testPrivateMethod_saveAuthTokenAndSek() throws Exception {
        RequestInfo mockRequestInfo = new RequestInfo();
        AuthSek mockAuthSek = new AuthSek();

        Method privateMethod = PaymentService.class.getDeclaredMethod("saveAuthTokenAndSek", RequestInfo.class, AuthSek.class);
        privateMethod.setAccessible(true);

        privateMethod.invoke(paymentService, mockRequestInfo, mockAuthSek);

        verify(producer).push(eq("save-auth-sek"), any(AuthSekRequest.class));
    }

    @Test
    void testDecryptAndProcessTreasuryPayLoad() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, JsonProcessingException {
        TreasuryParams treasuryParams = mock(TreasuryParams.class);
        RequestInfo requestInfo = mock(RequestInfo.class);
        ArrayList<AuthSek> list = new ArrayList<>();
        list.add(mock(AuthSek.class));
        when(authSekRepository.getAuthSek(treasuryParams.getAuthToken())).thenReturn(list);
        Optional<AuthSek> optionalAuthSek = Optional.ofNullable(list.get(0));
        when(optionalAuthSek.get().getDecryptedSek()).thenReturn("testSek");
        when(treasuryParams.getRek()).thenReturn("testRek");
        when(treasuryParams.getData()).thenReturn("testData").toString();
        when(encryptionUtil.decryptResponse("testRek","testSek")).thenReturn("testRek");
        when(encryptionUtil.decryptResponse("testData", "testRek")).thenReturn("testData");
        when(objectMapper.readValue("testData", TransactionDetails.class)).thenReturn(transactionDetails);
        when(transactionDetails.getAmount()).thenReturn("10");
        when(transactionDetails.getStatus()).thenReturn("success");
        when(requestInfo.getUserInfo()).thenReturn(mock(User.class));

        TreasuryPaymentData treasuryPaymentData = paymentService.decryptAndProcessTreasuryPayload(treasuryParams,requestInfo);

        assertNotNull(treasuryPaymentData);
        assertEquals(treasuryPaymentData.getAmount(), BigDecimal.valueOf(10));
    }

    @Test
    void testDecryptAndProcessTreasuryPayLoadListNull() {
        TreasuryParams treasuryParams = mock(TreasuryParams.class);
        RequestInfo requestInfo = mock(RequestInfo.class);
        ArrayList<AuthSek> list = new ArrayList<>();
        when(authSekRepository.getAuthSek(treasuryParams.getAuthToken())).thenReturn(list);
       assertThrows(CustomException.class, () -> paymentService.decryptAndProcessTreasuryPayload(treasuryParams,requestInfo));
    }
}