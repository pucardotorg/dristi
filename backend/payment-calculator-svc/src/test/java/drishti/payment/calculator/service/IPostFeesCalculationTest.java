package drishti.payment.calculator.service;

import drishti.payment.calculator.repository.PostalServiceRepository;
import drishti.payment.calculator.util.IPostUtil;
import drishti.payment.calculator.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IPostFeesCalculationTest {

    @Mock
    private IPostUtil iPostUtil;

    @Mock
    private PostalServiceRepository repository;

    @InjectMocks
    private IPostFeesCalculation iPostFeesCalculation;

    private RequestInfo requestInfo;
    private SummonCalculationCriteria criteria;
    private IPostConfigParams iPostFeesDefaultData;
    private PostalServiceSearchCriteria searchCriteria;
    private List<PostalService> postalServices;
    private SpeedPost speedPost;
    private List<WeightRange> weightRanges;
    private WeightRange weightRange;
    private Map<String, DistanceRange> distanceRanges;
    private DistanceRange distanceRange;

    @BeforeEach
    void setUp() {
        requestInfo = new RequestInfo();
        criteria = SummonCalculationCriteria.builder().tenantId("tenant1").receiverPincode("123456").summonId("summon1").build();

        iPostFeesDefaultData = IPostConfigParams.builder()
                .courtFee(100.0)
                .envelopeChargeIncludingGst(10.0)
                .gstPercentage(0.18)
                .pageWeight(5.0)
                .printingFeePerPage(2.0)
                .businessFee(50.0)
                .speedPost(new SpeedPost())
                .build();

        speedPost = iPostFeesDefaultData.getSpeedPost();
        weightRange = WeightRange.builder().minWeight(0).maxWeight(100).distanceRanges(new HashMap<>()).build();
        weightRanges = Collections.singletonList(weightRange);
        speedPost.setWeightRanges(weightRanges);

        distanceRange = DistanceRange.builder().minDistance(0.0).maxDistance(500.0).fee(20.0).build();
        distanceRanges = Collections.singletonMap("range1", distanceRange);
        weightRange.setDistanceRanges(distanceRanges);

        searchCriteria = PostalServiceSearchCriteria.builder().pincode("123456").build();
        postalServices = Collections.singletonList(PostalService.builder().distanceKM(100.0).build());
    }

    @Test
    void testCalculatePayment() {
        when(iPostUtil.getIPostFeesDefaultData(any(RequestInfo.class), anyString())).thenReturn(iPostFeesDefaultData);
        when(repository.getPostalService(any(PostalServiceSearchCriteria.class))).thenReturn(postalServices);

        Calculation result = iPostFeesCalculation.calculatePayment(requestInfo, criteria);

        assertNotNull(result);
        assertEquals("summon1", result.getApplicationId());
        assertEquals("tenant1", result.getTenantId());

        verify(iPostUtil, times(1)).getIPostFeesDefaultData(any(RequestInfo.class), anyString());
        verify(repository, times(1)).getPostalService(any(PostalServiceSearchCriteria.class));
    }

    @Test
    void testGetSpeedPostFee() {
        Double speedPostFee = iPostFeesCalculation.getSpeedPostFee(50.0, 100.0, speedPost);
        assertEquals(20.0, speedPostFee);
    }

}


