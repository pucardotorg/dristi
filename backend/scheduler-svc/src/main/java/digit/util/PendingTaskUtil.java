package digit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import digit.config.Configuration;
import digit.repository.ServiceRequestRepository;
import digit.web.models.PendingTask;
import digit.web.models.PendingTaskRequest;
import digit.web.models.ReScheduleHearing;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static digit.config.ServiceConstants.*;

@Component
@Slf4j
public class PendingTaskUtil {

    private final ObjectMapper objectMapper;

    private final Configuration configuration;

    private final ServiceRequestRepository serviceRequestRepository;

    private final DateUtil dateUtil;

    public PendingTaskUtil(ObjectMapper objectMapper, Configuration configuration, RestTemplate restTemplate, ServiceRequestRepository serviceRequestRepository, DateUtil dateUtil) {
        this.objectMapper = objectMapper;
        this.configuration = configuration;
        this.serviceRequestRepository = serviceRequestRepository;
        this.dateUtil = dateUtil;
    }

    public PendingTask createPendingTask(ReScheduleHearing reScheduleHearing) {
        PendingTask pendingTask = new PendingTask();
        pendingTask.setEntityType(PENDING_TASK_ENTITY_TYPE);
        pendingTask.setName(PENDING_TASK_NAME);
        pendingTask.setReferenceId(reScheduleHearing.getRescheduledRequestId());
        pendingTask.setStatus(PENDING_TASK_STATUS);
        pendingTask.setFilingNumber(reScheduleHearing.getCaseId());
        pendingTask.setAssignedRole(List.of("JUDGE_ROLE"));
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime slaDate=currentTime.plusDays(configuration.getJudgePendingSla());

        log.info("sla date {}", slaDate);
        pendingTask.setStateSla(dateUtil.getEpochFromLocalDateTime(slaDate));
        return pendingTask;
    }

    public void callAnalytics(PendingTaskRequest request) {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        StringBuilder uri = new StringBuilder(configuration.getAnalyticsHost().concat(configuration.getAnalyticsEndpoint()));
        try {
            serviceRequestRepository.fetchResult(uri, request);
        } catch (HttpClientErrorException e) {
            log.error(EXTERNAL_SERVICE_EXCEPTION, e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(SEARCHER_SERVICE_EXCEPTION, e);
        }
    }
}
