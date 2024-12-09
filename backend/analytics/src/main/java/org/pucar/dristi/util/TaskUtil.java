package org.pucar.dristi.util;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.pucar.dristi.config.ServiceConstants.TASK_PATH;

@Slf4j
@Component
public class TaskUtil {

    private final Configuration config;
    private final ServiceRequestRepository repository;
    private final Util util;

    @Autowired
    public TaskUtil(Configuration config, ServiceRequestRepository repository, Util util) {
        this.config = config;
        this.repository = repository;
        this.util = util;
    }

    public Object getTask(JSONObject request, String tenantId, String taskNumber, String referenceId, String state) {
        StringBuilder url = getSearchURLWithParams();
        log.info("Inside TaskUtil getTask :: URL: {}", url);

        request.put("tenantId", tenantId);
        JSONObject criteria = new JSONObject();
        if (taskNumber != null)
            criteria.put("taskNumber", taskNumber);
        if (tenantId != null)
            criteria.put("tenantId", tenantId);
        if (referenceId != null)
            criteria.put("referenceId", referenceId);
        if (state != null)
            criteria.put("state", state);
        request.put("criteria", criteria);

        log.info("Inside HearingUtil getHearing :: Request: {}", request);
        try {
            String response = repository.fetchResult(url, request);
            log.info("Inside TaskUtil getTask :: Response: {}", response);

            JSONArray tasks = util.constructArray(response, TASK_PATH);
            return tasks.length() > 0 ? tasks.get(0) : null;
        } catch (Exception e) {
            log.error("Error while fetching or processing the task response for URL: {} with request: {}", url, request, e);
            throw new RuntimeException("Error while fetching or processing the task response", e);
        }
    }

    private StringBuilder getSearchURLWithParams() {
        StringBuilder url = new StringBuilder(config.getTaskHost());
        url.append(config.getTaskSearchPath());
        return url;
    }
}
