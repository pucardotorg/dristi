package drishti.payment.calculator.web.controllers;

import drishti.payment.calculator.service.CaseFeeCalculationService;
import drishti.payment.calculator.service.PaymentCalculationService;
import drishti.payment.calculator.web.models.Calculation;
import drishti.payment.calculator.web.models.CalculationRes;
import drishti.payment.calculator.web.models.EFillingCalculationRequest;
import drishti.payment.calculator.web.models.SummonCalculationReq;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentApiControllerTest {

    @Mock
    private PaymentCalculationService paymentCalculationService;

    @Mock
    private CaseFeeCalculationService calculationService;

    @InjectMocks
    private PaymentApiController paymentApiController;

    private SummonCalculationReq request;
    private List<Calculation> calculations;
    private CalculationRes calculationRes;
    private EFillingCalculationRequest calculationReq;

    @BeforeEach
    void setUp() {
        // Initialize the test data
        calculationReq = new EFillingCalculationRequest();
        calculationReq.setRequestInfo(new RequestInfo());

        request = new SummonCalculationReq();
        request.setRequestInfo(new RequestInfo());

        Calculation calculation = new Calculation();
        calculations = Collections.singletonList(calculation);

        ResponseInfo responseInfo = new ResponseInfo();
        calculationRes = CalculationRes.builder()
                .responseInfo(responseInfo)
                .calculation(calculations)
                .build();
    }

    @Test
    void v1CalculatePost_success() {
        when(paymentCalculationService.calculateSummonFees(request)).thenReturn(calculations);

        ResponseEntity<CalculationRes> responseEntity = paymentApiController.v1CalculatePost(request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getCalculation()).isEqualTo(calculationRes.getCalculation());
        verify(paymentCalculationService).calculateSummonFees(request);
    }

    @Test
    void v1CalculatePost_serviceFailure() {
        when(paymentCalculationService.calculateSummonFees(request)).thenThrow(new RuntimeException("Service failure"));

        assertThatThrownBy(() -> paymentApiController.v1CalculatePost(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Service failure");

        verify(paymentCalculationService).calculateSummonFees(request);
    }

    @Test
    void caseFeesCalculation_success() {
        when(calculationService.calculateCaseFees(calculationReq)).thenReturn(calculations);
        ResponseEntity<CalculationRes> responseEntity = paymentApiController.caseFeesCalculation(calculationReq);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getCalculation()).isEqualTo(calculationRes.getCalculation());
        verify(calculationService).calculateCaseFees(calculationReq);
    }

    @Test
    void caseFeesCalculation_serviceFailure() {
        when(calculationService.calculateCaseFees(calculationReq)).thenThrow(new RuntimeException("Service failure"));

        assertThatThrownBy(() -> paymentApiController.caseFeesCalculation(calculationReq))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Service failure");

        verify(calculationService).calculateCaseFees(calculationReq);
    }

    @Test
    void v1CalculatePost_invalidRequest() {
        // Assuming that request validation fails
        SummonCalculationReq invalidRequest = new SummonCalculationReq();

        // Optionally, mock validation exception handling
        // when(summonCalculationService.calculateSummonFees(invalidRequest)).thenThrow(new MethodArgumentNotValidException());

        // Validate that the controller returns bad request or handles validation error as expected
        // This can vary based on your actual validation mechanism and controller advice setup
    }
}
