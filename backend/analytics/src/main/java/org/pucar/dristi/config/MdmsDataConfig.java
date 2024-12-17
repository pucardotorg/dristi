package org.pucar.dristi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.CaseOutcomeType;
import org.pucar.dristi.web.models.CaseOverallStatusType;
import org.pucar.dristi.web.models.PendingTaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MdmsDataConfig {

    private final MdmsUtil mdmsUtil;
    private final ObjectMapper objectMapper;
    private final Configuration configuration;

    @Getter
    private Map<String, List<PendingTaskType>> pendingTaskTypeMap;

    @Getter
    private Map<String, List<CaseOverallStatusType>> caseOverallStatusTypeMap;

    @Getter
    private Map<String, CaseOutcomeType> caseOutcomeTypeMap;

    @Autowired
    public MdmsDataConfig(MdmsUtil mdmsUtil, ObjectMapper objectMapper, Configuration configuration) {
        this.mdmsUtil = mdmsUtil;
        this.objectMapper = objectMapper;
        this.configuration = configuration;
    }

    @PostConstruct
    public void loadConfigData() {
        loadPendingTaskMap();
        loadCaseOverallStatusMap();
        loadCaseOutcomeMap();
    }
    private void loadPendingTaskMap(){
        RequestInfo requestInfo = RequestInfo.builder().build();
        JSONArray pendingTaskTypeList = mdmsUtil.fetchMdmsData(requestInfo,configuration.getStateLevelTenantId(),configuration.getMdmsPendingTaskModuleName(),List.of(configuration.getMdmsPendingTaskMasterName()))
                .get(configuration.getMdmsPendingTaskModuleName()).get(configuration.getMdmsPendingTaskMasterName());
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

    private void loadCaseOverallStatusMap(){
        RequestInfo requestInfo = RequestInfo.builder().build();
        JSONArray caseOverallStatusTypeList = mdmsUtil.fetchMdmsData(requestInfo,configuration.getStateLevelTenantId(),configuration.getMdmsCaseOverallStatusModuleName(),List.of(configuration.getMdmsCaseOverallStatusMasterName()))
                .get(configuration.getMdmsCaseOverallStatusModuleName()).get(configuration.getMdmsCaseOverallStatusMasterName());
        caseOverallStatusTypeMap = new HashMap<>();

        try {
            for (Object o : caseOverallStatusTypeList) {
                CaseOverallStatusType caseOverallStatusType = objectMapper.convertValue(o, CaseOverallStatusType.class);
                String workflowModule = caseOverallStatusType.getWorkflowModule();

                caseOverallStatusTypeMap.computeIfAbsent(workflowModule, k -> new ArrayList<>());

                caseOverallStatusTypeMap.get(workflowModule).add(caseOverallStatusType);
            }
        } catch (Exception e) {
            log.error("Unable to create case-overall-status map :: {}",e.getMessage());
        }
    }

    private void loadCaseOutcomeMap(){
        RequestInfo requestInfo = RequestInfo.builder().build();
        JSONArray caseOutcomeTypeList = mdmsUtil.fetchMdmsData(requestInfo,configuration.getStateLevelTenantId(),configuration.getMdmsCaseOutcomeModuleName(),List.of(configuration.getMdmsCaseOutcomeMasterName()))
                .get(configuration.getMdmsCaseOutcomeModuleName()).get(configuration.getMdmsCaseOutcomeMasterName());
        caseOutcomeTypeMap = new HashMap<>();

        try {
            for (Object o : caseOutcomeTypeList) {
                CaseOutcomeType caseOutcomeType = objectMapper.convertValue(o, CaseOutcomeType.class);
                String orderType = caseOutcomeType.getOrderType();
                caseOutcomeTypeMap.put(orderType,caseOutcomeType);
            }
        } catch (Exception e) {
            log.error("Unable to create case outcome map :: {}",e.getMessage());
        }
    }
}
