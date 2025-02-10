package drishti.payment.calculator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.models.coremodels.AuditDetails;
import drishti.payment.calculator.helper.EFilingParamTestBuilder;
import drishti.payment.calculator.util.CaseUtil;
import drishti.payment.calculator.util.EFillingUtil;
import drishti.payment.calculator.web.models.Calculation;
import drishti.payment.calculator.web.models.EFilingParam;
import drishti.payment.calculator.web.models.EFillingCalculationCriteria;
import drishti.payment.calculator.web.models.EFillingCalculationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static drishti.payment.calculator.config.ServiceConstants.ADVOCATE_FEE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CaseFeeCalculationServiceTest {

    @InjectMocks
    private CaseFeeCalculationService caseFeesCalculationService;

    @Mock
    private EFillingUtil eFillingUtil;

    @Mock
    private CaseUtil caseUtil;

    @Test
    @DisplayName("do calculate case fees with one criteria")
    public void doCalculateCaseFeesWithOneCriteria() {
        EFilingParam eFilingParam = EFilingParamTestBuilder.builder().withConfig().withPetitionFee().withAdvocateFee().build();
        when(eFillingUtil.getEFillingDefaultData(any(), anyString())).thenReturn(eFilingParam);
        EFillingCalculationRequest request = EFillingCalculationRequest.builder()
                .calculationCriteria(Collections.singletonList(
                        EFillingCalculationCriteria.builder().tenantId("kl").numberOfApplication(1).checkAmount(50000.0).isDelayCondonation(false).build()
                )).build();

        List<Calculation> result = caseFeesCalculationService.calculateCaseFees(request);
        assertEquals(1, result.size());
        Calculation calculation = result.get(0);
        assertEquals(5, calculation.getBreakDown().size());
    }


    @Test
    @DisplayName("do calculate case fees with one criteria and litigant advocate")
    public void doCalculateCaseFeesWithOneCriteriaAndLitigantAdvocate() {
        EFilingParam eFilingParam = EFilingParamTestBuilder.builder().withConfig().withPetitionFee().withAdvocateFee().build();
        when(eFillingUtil.getEFillingDefaultData(any(), anyString())).thenReturn(eFilingParam);
        Map<String, List<JsonNode>> mockLitigantAdvocateMap = Collections.singletonMap("1", Collections.singletonList(new ObjectMapper().createObjectNode().put("id", "101")));
        when(caseUtil.getAdvocateForLitigant(any(), anyString(), anyString())).thenReturn(mockLitigantAdvocateMap);
        EFillingCalculationRequest request = EFillingCalculationRequest.builder()
                .calculationCriteria(Collections.singletonList(
                        EFillingCalculationCriteria.builder().tenantId("kl").filingNumber("KL-123").numberOfApplication(1).checkAmount(50000.0).isDelayCondonation(false).build()
                )).build();

        List<Calculation> result = caseFeesCalculationService.calculateCaseFees(request);
        assertEquals(1, result.size());
        Calculation calculation = result.get(0);
        assertEquals(5, calculation.getBreakDown().size());
    }


    @Test
    @DisplayName("do calculate case fees with one criteria and litigant advocate with more than one litigant advocates")
    public void doCalculateCaseFeesWithOneCriteriaAndLitigantAdvocateWithMoreThanOneLitigantAdvocates() {
        EFilingParam eFilingParam = EFilingParamTestBuilder.builder().withConfig().withPetitionFee().withAdvocateFee().build();
        when(eFillingUtil.getEFillingDefaultData(any(), anyString())).thenReturn(eFilingParam);
        Map<String, List<JsonNode>> mockLitigantAdvocateMap = new HashMap<>();
        mockLitigantAdvocateMap.put("1", Collections.nCopies(4, new ObjectMapper().createObjectNode().put("id", "101")));
        mockLitigantAdvocateMap.put("2", Collections.nCopies(2, new ObjectMapper().createObjectNode().put("id", "102")));
        when(caseUtil.getAdvocateForLitigant(any(), anyString(), anyString())).thenReturn(mockLitigantAdvocateMap);
        EFillingCalculationRequest request = EFillingCalculationRequest.builder()
                .calculationCriteria(Collections.singletonList(
                        EFillingCalculationCriteria.builder().tenantId("kl").filingNumber("KL-123").numberOfApplication(1).checkAmount(50000.0).isDelayCondonation(false).build()
                )).build();

        List<Calculation> result = caseFeesCalculationService.calculateCaseFees(request);
        assertEquals(1, result.size());
        Calculation calculation = result.get(0);

        assertEquals(5, calculation.getBreakDown().size());
        assertEquals(125.0, calculation.getBreakDown().get(4).getAmount(), 0.01);
    }



}
