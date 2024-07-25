package org.pucar.dristi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.PendingTaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PendingTaskMapConfig {

    private final MdmsUtil mdmsUtil;
    private final ObjectMapper objectMapper;
    private final Configuration configuration;

    @Getter
    private Map<String, List<PendingTaskType>> pendingTaskTypeMap;

    @Autowired
    public PendingTaskMapConfig(MdmsUtil mdmsUtil, ObjectMapper objectMapper, Configuration configuration) {
        this.mdmsUtil = mdmsUtil;
        this.objectMapper = objectMapper;
        this.configuration = configuration;
    }

    @PostConstruct
    public void loadConfigData() {
        RequestInfo requestInfo = RequestInfo.builder().build();
        JSONArray pendingTaskTypeList = mdmsUtil.fetchMdmsData(requestInfo,configuration.getStateLevelTenantId(),configuration.getMdmsModuleName(),List.of(configuration.getMdmsMasterName()))
                .get(configuration.getMdmsModuleName()).get(configuration.getMdmsMasterName());;
        pendingTaskTypeMap = new HashMap<>();

        try {
            for (Object o : pendingTaskTypeList) {
                PendingTaskType pendingTaskType = objectMapper.convertValue(o, PendingTaskType.class);
                String workflowModule = pendingTaskType.getWorkflowModule();

                pendingTaskTypeMap.computeIfAbsent(workflowModule, k -> new ArrayList<>());

                pendingTaskTypeMap.get(workflowModule).add(pendingTaskType);
            }
        } catch (Exception e) {
            log.error("Unable to create pending task map :: {}",e.getMessage());
        }
    }

}
