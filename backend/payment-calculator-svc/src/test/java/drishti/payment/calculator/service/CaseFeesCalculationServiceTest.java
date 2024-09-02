package drishti.payment.calculator.service;

import drishti.payment.calculator.config.Configuration;
import drishti.payment.calculator.util.EFillingUtil;
import drishti.payment.calculator.web.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CaseFeesCalculationServiceTest {

    @InjectMocks
    private CaseFeesCalculationService caseFeesCalculationService;

    @Mock
    private EFillingUtil eFillingUtil;

    @Mock
    private Configuration config;

    @Test
    public void calculateCaseFees_HappyPath() {
        EFillingCalculationReq request = new EFillingCalculationReq();
        EFillingCalculationCriteria criteria = new EFillingCalculationCriteria();
        criteria.setNumberOfApplication(1);
        criteria.setCheckAmount(1000.0);
        request.setCalculationCriteria(Arrays.asList(criteria));
        EFilingParam eFilingDefaultData = new EFilingParam();
        eFilingDefaultData.setCourtFee(100.0);
        eFilingDefaultData.setApplicationFee(50.0);
        eFilingDefaultData.setVakalathnamaFee(20.0);
        eFilingDefaultData.setAdvocateWelfareFund(10.0);
        eFilingDefaultData.setAdvocateClerkWelfareFund(5.0);
        eFilingDefaultData.setPetitionFeePercentage(0.1);
        eFilingDefaultData.setDefaultPetitionFee(200.0);

        when(eFillingUtil.getEFillingDefaultData(request.getRequestInfo(), criteria.getTenantId())).thenReturn(eFilingDefaultData);

        List<Calculation> result = caseFeesCalculationService.calculateCaseFees(request);

        assertEquals(1, result.size());
        assertEquals(385.0, result.get(0).getTotalAmount());
    }

    @Test
    public void calculateCaseFees_NoApplications() {
        EFillingCalculationReq request = new EFillingCalculationReq();
        EFillingCalculationCriteria criteria = new EFillingCalculationCriteria();
        criteria.setNumberOfApplication(0);
        criteria.setCheckAmount(1000.0);
        request.setCalculationCriteria(Arrays.asList(criteria));
        EFilingParam eFilingDefaultData = new EFilingParam();
        eFilingDefaultData.setCourtFee(100.0);
        eFilingDefaultData.setApplicationFee(50.0);
        eFilingDefaultData.setVakalathnamaFee(20.0);
        eFilingDefaultData.setAdvocateWelfareFund(10.0);
        eFilingDefaultData.setAdvocateClerkWelfareFund(5.0);
        eFilingDefaultData.setPetitionFeePercentage(0.1);
        eFilingDefaultData.setDefaultPetitionFee(200.0);

        when(eFillingUtil.getEFillingDefaultData(request.getRequestInfo(), criteria.getTenantId())).thenReturn(eFilingDefaultData);

        List<Calculation> result = caseFeesCalculationService.calculateCaseFees(request);

        assertEquals(1, result.size());
        assertEquals(335.0, result.get(0).getTotalAmount());
    }

    @Test
    public void calculateCaseFees_NoCheckAmount() {
        EFillingCalculationReq request = new EFillingCalculationReq();
        EFillingCalculationCriteria criteria = new EFillingCalculationCriteria();
        criteria.setNumberOfApplication(1);
        criteria.setCheckAmount(0.0);
        request.setCalculationCriteria(Arrays.asList(criteria));
        EFilingParam eFilingDefaultData = new EFilingParam();
        eFilingDefaultData.setCourtFee(100.0);
        eFilingDefaultData.setApplicationFee(50.0);
        eFilingDefaultData.setVakalathnamaFee(20.0);
        eFilingDefaultData.setAdvocateWelfareFund(10.0);
        eFilingDefaultData.setAdvocateClerkWelfareFund(5.0);
        eFilingDefaultData.setPetitionFeePercentage(0.1);
        eFilingDefaultData.setDefaultPetitionFee(200.0);

        when(eFillingUtil.getEFillingDefaultData(request.getRequestInfo(), criteria.getTenantId())).thenReturn(eFilingDefaultData);

        List<Calculation> result = caseFeesCalculationService.calculateCaseFees(request);

        assertEquals(1, result.size());
        assertEquals(385.0, result.get(0).getTotalAmount());
    }
}
