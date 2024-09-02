package drishti.payment.calculator.service;

import drishti.payment.calculator.web.models.Calculation;
import drishti.payment.calculator.web.models.SummonCalculationCriteria;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EPoliceFeesCalculationTest {

    @InjectMocks
    private EPoliceFeesCalculation ePoliceFeesCalculation;

    @BeforeEach
    void setUp() {
        ePoliceFeesCalculation = new EPoliceFeesCalculation();
    }

    @Test
    void testCalculatePayment_ValidInputs() {
        RequestInfo requestInfo = new RequestInfo();
        SummonCalculationCriteria criteria = SummonCalculationCriteria.builder()
                .summonId("SUMMON123")
                .tenantId("TENANT1")
                .build();

        Calculation calculation = ePoliceFeesCalculation.calculatePayment(requestInfo, criteria);

        assertNotNull(calculation);
        assertEquals("SUMMON123", calculation.getApplicationId());
        assertEquals("TENANT1", calculation.getTenantId());
        assertEquals(0.0, calculation.getTotalAmount());
    }

    @Test
    void testCalculatePayment_NullRequestInfo() {
        RequestInfo requestInfo = null;
        SummonCalculationCriteria criteria = SummonCalculationCriteria.builder()
                .summonId("SUMMON123")
                .tenantId("TENANT1")
                .build();

        Calculation calculation = ePoliceFeesCalculation.calculatePayment(requestInfo, criteria);

        assertNotNull(calculation);
        assertEquals("SUMMON123", calculation.getApplicationId());
        assertEquals("TENANT1", calculation.getTenantId());
        assertEquals(0.0, calculation.getTotalAmount());
    }

    @Test
    void testCalculatePayment_NullCriteria() {
        RequestInfo requestInfo = new RequestInfo();
        SummonCalculationCriteria criteria = null;

        Exception exception = assertThrows(NullPointerException.class, () -> {
            ePoliceFeesCalculation.calculatePayment(requestInfo, criteria);
        });

        assertNotNull(exception);
    }

    @Test
    void testCalculatePayment_NullInputs() {
        RequestInfo requestInfo = null;
        SummonCalculationCriteria criteria = null;

        Exception exception = assertThrows(NullPointerException.class, () -> {
            ePoliceFeesCalculation.calculatePayment(requestInfo, criteria);
        });

        assertNotNull(exception);
    }
}
