package digit.service;


import digit.config.Configuration;
import digit.enrichment.RescheduleRequestOptOutEnrichment;
import digit.kafka.producer.Producer;
import digit.repository.RescheduleRequestOptOutRepository;
import digit.validator.RescheduleRequestOptOutValidator;
import digit.web.models.OptOut;
import digit.web.models.OptOutRequest;
import digit.web.models.OptOutSearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RescheduleRequestOptOutServiceTest {

    @Mock
    private RescheduleRequestOptOutRepository optOutRepository;

    @Mock
    private RescheduleRequestOptOutValidator optOutValidator;

    @Mock
    private RescheduleRequestOptOutEnrichment optOutEnrichment;

    @Mock
    private Producer producer;

    @Mock
    private Configuration config;

    @Mock
    private ReScheduleHearingService reScheduleHearingService;

    @InjectMocks
    private RescheduleRequestOptOutService optOutService;

    private OptOutRequest optOutRequest;
    private OptOut optOut;

    @BeforeEach
    void setUp() {
        optOut = new OptOut();
        optOut.setRescheduleRequestId("rescheduleRequestId");
        optOut.setJudgeId("judgeId");
        optOut.setTenantId("tenantId");
        optOut.setOptoutDates(List.of(1L, 2L));
        optOutRequest = new OptOutRequest();
        optOutRequest.setOptOut(optOut);
    }

    @Test
    void testCreate() {
        // Arrange
        when(config.getOptOutTopic()).thenReturn("optOutTopic");

        // Act
        OptOut result = optOutService.create(optOutRequest);

        // Assert
        verify(optOutValidator).validateRequest(optOutRequest);
        verify(optOutEnrichment).enrichCreateRequest(optOutRequest);
        verify(producer).push("optOutTopic", optOutRequest);
        assertEquals(optOut, result);
    }

    @Test
    void testSearch() {
        OptOutSearchRequest searchRequest = new OptOutSearchRequest();
        List<OptOut> expectedOptOuts = new ArrayList<>();
        when(optOutRepository.getOptOut(searchRequest.getCriteria(), 10, 0)).thenReturn(expectedOptOuts);

        List<OptOut> result = optOutService.search(searchRequest, 10, 0);

        verify(optOutRepository).getOptOut(searchRequest.getCriteria(), 10, 0);
        assertEquals(expectedOptOuts, result);
    }
}
