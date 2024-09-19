package digit.service;

import digit.util.MdmsUtil;
import digit.util.TaskUtil;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.repository.ServiceRequestRepository;
import digit.web.models.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class DemandServiceTest {

    @InjectMocks
    private DemandService demandService;

    @Mock
    private Configuration config;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private ServiceRequestRepository repository;

    @Mock
    private Task task;

    @Mock
    private TaskRequest taskRequest;

    @Mock
    private RequestInfo requestInfo;

    @Mock
    private DeliveryChannel deliveryChannel;

    @Mock
    private RespondentDetails respondentDetails;

    @Mock
    private CalculationResponse calculationResponse;

    @Mock
    private DemandResponse demandResponse;

    @Mock
    private List<Demand> demands;

    @Mock
    private TaskDetails taskDetails;

    @Mock
    private List<Calculation> calculations;

    @Mock
    private Address address;

    @Mock
    private MdmsUtil mdmsUtil;

    @Mock
    private TaskUtil taskUtil;

    @Test
    public void fetchPaymentDetailsAndGenerateDemandAndBillTest() {
        // Arrange
//        List<Calculation> calculations = Collections.singletonList(mock(Calculation.class));
//        BillResponse billResponse = mock(BillResponse.class);
//
//        Map<String, Map<String, JSONArray>> mdmsRes = new HashMap<>();
//        mdmsRes.put("payment", new HashMap<>());
//
//        List<String> masterList = new ArrayList<>();
//        masterList.add("PaymentMasterCode");
//
//
//        lenient().when(mdmsUtil.fetchMdmsData(requestInfo, "kl", "payment", masterList)).thenReturn(mdmsRes);
//        when(taskRequest.getTask()).thenReturn(task);
//        when(task.getTaskDetails()).thenReturn(taskDetails);
//        when(taskDetails.getDeliveryChannel()).thenReturn(deliveryChannel);
//        when(taskDetails.getRespondentDetails()).thenReturn(respondentDetails);
//        when(respondentDetails.getAddress()).thenReturn(address);
//        when(deliveryChannel.getChannelName()).thenReturn(ChannelName.SMS.toString());
//        when(task.getTenantId()).thenReturn("tenant1");
//        when(task.getTaskNumber()).thenReturn("TN001");
//        when(repository.fetchResult(any(), any())).thenReturn(new Object());
//        when(mapper.convertValue(any(), eq(CalculationResponse.class))).thenReturn(calculationResponse);
//        when(calculationResponse.getCalculation()).thenReturn(calculations);
//        when(repository.fetchResult(any(), any())).thenReturn(new Object());
//        when(mapper.convertValue(any(), eq(DemandResponse.class))).thenReturn(demandResponse);
//        when(demandResponse.getDemands()).thenReturn(demands);
//        when(task.getTenantId()).thenReturn("tenant1");
//        when(task.getTaskNumber()).thenReturn("TN001");
//        when(config.getTaskBusinessService()).thenReturn("TBS001");
//        when(config.getBillingServiceHost()).thenReturn("http://billing");
//        when(config.getFetchBillEndpoint()).thenReturn("/fetch");
//        when(task.getStatus()).thenReturn("PAYMENT");
//        when(taskUtil.callUpdateTask(taskRequest)).thenReturn(null);
//
//        when(repository.fetchResult(any(), any())).thenReturn(new Object());
//        when(mapper.convertValue(any(), eq(BillResponse.class))).thenReturn(billResponse);
//        BillResponse result = demandService.fetchPaymentDetailsAndGenerateDemandAndBill(taskRequest);

        // Assert
//        assertNotNull(result);
//        assertEquals(billResponse, result);
    }

    @Test
    void testGeneratePaymentDetails() {
        // Arrange


        when(task.getTaskDetails()).thenReturn(taskDetails);
        when(taskDetails.getDeliveryChannel()).thenReturn(deliveryChannel);
        when(taskDetails.getRespondentDetails()).thenReturn(respondentDetails);
        when(respondentDetails.getAddress()).thenReturn(address);
        when(deliveryChannel.getChannelName()).thenReturn(ChannelName.SMS.toString());
        when(task.getTenantId()).thenReturn("tenant1");
        when(task.getTaskNumber()).thenReturn("TN001");

        when(config.getPaymentCalculatorHost()).thenReturn("http://calculator");
        when(config.getPaymentCalculatorCalculateEndpoint()).thenReturn("/calculate");

        when(repository.fetchResult(any(), any())).thenReturn(new Object());
        when(mapper.convertValue(any(), eq(CalculationResponse.class))).thenReturn(calculationResponse);
        when(calculationResponse.getCalculation()).thenReturn(calculations);

        // Act
        List<Calculation> result = demandService.generatePaymentDetails(requestInfo, task);

        // Assert
        assertNotNull(result);
        assertEquals(calculations, result);
        verify(repository).fetchResult(any(), any());
        verify(mapper).convertValue(any(), eq(CalculationResponse.class));
    }

    @Test
    void testGenerateDemands() {
        // Arrange
        RequestInfo requestInfo = mock(RequestInfo.class);
        Calculation calculation = mock(Calculation.class);
        List<Calculation> calculations = Collections.singletonList(calculation);
        DemandResponse demandResponse = mock(DemandResponse.class);
        List<Demand> demands = Collections.singletonList(mock(Demand.class));

//        when(calculation.getTenantId()).thenReturn("tenant1");
//
//        when(config.getTaxConsumerType()).thenReturn("CT001");
//        when(config.getTaskModuleCode()).thenReturn("TM001");
//        when(config.getTaxPeriodFrom()).thenReturn(1234567890L);
//        when(config.getTaxPeriodTo()).thenReturn(1234567899L);
//        when(config.getBillingServiceHost()).thenReturn("http://billing");
//        when(config.getDemandCreateEndpoint()).thenReturn("/create");

        //when(repository.fetchResult(any(), any())).thenReturn(new Object());
        //when(mapper.convertValue(any(), eq(DemandResponse.class))).thenReturn(demandResponse);
        //when(demandResponse.getDemands()).thenReturn(demands);

        // Act
        //List<Demand> result = demandService.generateDemands(requestInfo, calculations,task);

        // Assert
        //assertNotNull(result);
        //assertEquals(demands, result);
        //verify(repository).fetchResult(any(), any());
        //verify(mapper).convertValue(any(), eq(DemandResponse.class));
    }

    @Test
    void testGetBill() {
        // Arrange
        RequestInfo requestInfo = mock(RequestInfo.class);
        Task task = mock(Task.class);
        BillResponse billResponse = mock(BillResponse.class);

        when(task.getTaskType()).thenReturn("SUMMONS");
        when(task.getTenantId()).thenReturn("tenant1");
        when(task.getTaskNumber()).thenReturn("TN001");
        when(config.getBillingServiceHost()).thenReturn("http://billing");
        when(config.getTaskSummonBusinessService()).thenReturn("SUMMONS");
        when(config.getFetchBillEndpoint()).thenReturn("/fetch");

        when(repository.fetchResult(any(), any())).thenReturn(new Object());
        when(mapper.convertValue(any(), eq(BillResponse.class))).thenReturn(billResponse);

        // Act
        BillResponse result = demandService.getBill(requestInfo, task);

        // Assert
        assertNotNull(result);
        assertEquals(billResponse, result);
        verify(repository).fetchResult(any(), any());
        verify(mapper).convertValue(any(), eq(BillResponse.class));
    }
}