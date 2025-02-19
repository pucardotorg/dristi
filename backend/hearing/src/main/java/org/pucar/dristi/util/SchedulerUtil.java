package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.BulkRescheduleRequest;
import org.pucar.dristi.web.models.BulkRescheduleResponse;
import org.pucar.dristi.web.models.ScheduleHearing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SchedulerUtil {

    private final ServiceRequestRepository repository;
    private final Configuration configuration;
    private final ObjectMapper mapper;

    @Autowired
    public SchedulerUtil(ServiceRequestRepository repository, Configuration configuration, ObjectMapper mapper) {
        this.repository = repository;
        this.configuration = configuration;
        this.mapper = mapper;
    }

    public List<ScheduleHearing> callBulkReschedule(BulkRescheduleRequest request) {

        StringBuilder uri = new StringBuilder();
        uri.append(configuration.getSchedulerHost()).append(configuration.getBulkRescheduleEndPoint());
        Object response = repository.fetchResult(uri, request);
        List<ScheduleHearing> result;

        try {
            BulkRescheduleResponse rescheduleResponse = mapper.convertValue(response, BulkRescheduleResponse.class);
            result = rescheduleResponse.getReScheduleHearings();

        } catch (Exception e) {
            log.error("Error occurred while calling bulk reschedule");
            throw new CustomException("", "Error occurred while calling bulk reschedule");
        }
        return result;

    }
}
