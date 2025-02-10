package org.pucar.dristi.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.models.individual.Individual;
import org.egov.tracer.model.CustomException;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.AdvocateUtil;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.IndexerUtils;
import org.pucar.dristi.util.PendingTaskUtil;
import org.pucar.dristi.web.models.PendingTask;
import org.pucar.dristi.web.models.PendingTaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;

@Service
@Slf4j
public class PendingTaskService {

    private final Configuration config;
    private final IndexerUtils indexerUtils;
    private final PendingTaskUtil pendingTaskUtil;
    private final IndividualService individualService;
    private final AdvocateUtil advocateUtil;
    private final CaseUtil caseUtil;
    private final ObjectMapper objectMapper;

    @Autowired
    public PendingTaskService(Configuration config, IndexerUtils indexerUtils, PendingTaskUtil pendingTaskUtil, IndividualService individualService, AdvocateUtil advocateUtil, CaseUtil caseUtil, ObjectMapper objectMapper) {
        this.config = config;
        this.indexerUtils = indexerUtils;
        this.pendingTaskUtil = pendingTaskUtil;
        this.individualService = individualService;
        this.advocateUtil = advocateUtil;
        this.caseUtil = caseUtil;
        this.objectMapper = objectMapper;
    }

    public PendingTask createPendingTask(PendingTaskRequest pendingTaskRequest) {
        try {
            log.info("Inside Pending Task service:: PendingTaskRequest: {}", pendingTaskRequest);
            String bulkRequest = indexerUtils.buildPayload(pendingTaskRequest.getPendingTask());
            if (!bulkRequest.isEmpty()) {
                String uri = config.getEsHostUrl() + config.getBulkPath();
                indexerUtils.esPostManual(uri, bulkRequest);
            }
            return pendingTaskRequest.getPendingTask();
        } catch (CustomException e) {
            log.error("Custom Exception occurred while creating hearing");
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while creating hearing");
            throw new CustomException(Pending_Task_Exception, e.getMessage());
        }
    }

    public void updatePendingTask(String topic, Map<String, Object> joinCaseJson) {
        try {
            String filingNumber = joinCaseJson.get("caseFilingNumber").toString();
            JsonNode pendingTaskNode = pendingTaskUtil.callPendingTask(filingNumber);
            if(Objects.equals(topic, LITIGANT_JOIN_CASE_TOPIC) && joinCaseJson.get("litigant") != null) {
                updatePendingTaskForLitigant(joinCaseJson, pendingTaskNode);
            } else if (Objects.equals(topic, REPRESENTATIVE_JOIN_CASE_TOPIC) && joinCaseJson.get("representative") != null) {
                updatePendingTaskForAdvocate(joinCaseJson, pendingTaskNode, false);
            } else if(Objects.equals(topic, REPRESENTATIVE_REPLACE_JOIN_CASE)) {
                updatePendingTaskForAdvocate(joinCaseJson, pendingTaskNode, true);
            }

        } catch (Exception e) {
            log.error(ERROR_UPDATING_PENDING_TASK, e);
            throw new CustomException(ERROR_UPDATING_PENDING_TASK, e.getMessage());
        }
    }

    public void updatePendingTaskForLitigant(Map<String, Object> joinCaseJson, JsonNode pendingTaskNode) {
        try {
            log.info("Updating Pending task for Litigant.");
            JsonNode hitsNode = pendingTaskNode.path("hits").path("hits");
            List<JsonNode> filteredTask = filterPendingTaskLitigant(hitsNode);
            filteredTask = checkComplainantJoinCase(filteredTask, joinCaseJson);

            if(filteredTask != null){
                Map<String, Object> auditDetails = (Map<String, Object>) joinCaseJson.get("auditDetails");
                addAssigneeToPendingTask(filteredTask, auditDetails.get("createdBy").toString());
                pendingTaskUtil.updatePendingTask(filteredTask);
                log.info("Updated Pending Task for litigant.");
            }
            log.info("Updated Pending Task for litigant.");
        } catch (Exception e){
            log.error(ERROR_UPDATING_PENDING_TASK, e);
            throw new CustomException(ERROR_UPDATING_PENDING_TASK, e.getMessage());
        }

    }

    public void updatePendingTaskForAdvocate(Map<String, Object> joinCaseJson, JsonNode pendingTaskNode, Boolean isAdvocateReplace) {
        try {
            log.info("Updating Pending task for Advocate.");
            Map<String, Object> representative = (Map<String, Object>) joinCaseJson.get("representative");
            List<Map<String, Object>> parties = (List<Map<String, Object>>) representative.get("representing");

            RequestInfo requestInfo = (RequestInfo) joinCaseJson.get("requestInfo");

            String advocateUuid = getAdvocateUuid(requestInfo, representative);
            List<String> litigantUuids = getLitigantUuids(parties, requestInfo);

            JsonNode hitsNode = pendingTaskNode.path("hits").path("hits");

            List<JsonNode> filteredTasks = new ArrayList<>();
            filteredTasks = filterPendingTaskAdvocate(hitsNode, litigantUuids);

            if(isAdvocateReplace){
                replaceAssigneeToPendingTask(filteredTasks, advocateUuid, requestInfo);
            }
            else {
                addAssigneeToPendingTask(filteredTasks, advocateUuid);
            }
            pendingTaskUtil.updatePendingTask(filteredTasks);
            log.info("Pending Task for advocate is updated.");
        } catch (Exception e){
            log.error(ERROR_UPDATING_PENDING_TASK, e);
            throw new CustomException(ERROR_UPDATING_PENDING_TASK, e.getMessage());
        }
    }

    private void replaceAssigneeToPendingTask(List<JsonNode> filteredTasks, String uuid, RequestInfo requestInfo) {
        for(JsonNode task : filteredTasks) {
            JsonNode dataNode = task.path("_source").path("Data");
            ArrayNode assignedToArray = (ArrayNode) dataNode.withArray("assignedTo");
            List<JsonNode> litigantNode = new ArrayList<>();
            for(JsonNode uuidNode : assignedToArray) {
                if(uuidNode.has("uuid") && !isAdvocateUuid(uuidNode.get("uuid").asText(), requestInfo)){
                    litigantNode.add(uuidNode);
                }
            }
            assignedToArray.removeAll();

            for(JsonNode node : litigantNode) {
                assignedToArray.add(node);
            }
            ObjectNode newAssignee = assignedToArray.addObject();
            newAssignee.put("uuid", uuid);
        }
    }

    private boolean isAdvocateUuid(String uuid, RequestInfo requestInfo) {
        List<Individual> individuals = individualService.getIndividuals(requestInfo, Collections.singletonList(uuid));
        String individualId = individuals.get(0).getIndividualId();
        if(advocateUtil.isAdvocateExists(requestInfo, List.of(individualId))) {
            return true;
        }
        return false;
    }

    private static void addAssigneeToPendingTask(List<JsonNode> filteredTasks, String uuid) {
        for (JsonNode task : filteredTasks) {
            JsonNode dataNode = task.path("_source").path("Data");
            ArrayNode assignedToArray = (ArrayNode) dataNode.withArray("assignedTo");
            ObjectNode uuidNode = assignedToArray.addObject();
            uuidNode.put("uuid", uuid);
        }
    }

    private List<String> getLitigantUuids(List<Map<String, Object>> parties, RequestInfo requestInfo) {
        List<String> uuids = new ArrayList<>();
        for (Map<String, Object> party : parties) {
            String individualId = party.get("individualId").toString();
            List<Individual> individuals = individualService.getIndividualsByIndividualId(requestInfo, individualId);
            String uuid = individuals.get(0).getUserUuid();
            uuids.add(uuid);
        }
        return uuids;
    }

    private String getAdvocateUuid(RequestInfo requestInfo, Map<String, Object> representative) {
        Set<String> advocateIndividualId = advocateUtil.getAdvocate(requestInfo, List.of(representative.get("advocateId").toString()));
        return individualService.getIndividualsByIndividualId(requestInfo, advocateIndividualId.stream().findFirst().orElse(null)).get(0).getUserUuid();
    }

    private List<JsonNode> filterPendingTaskAdvocate(JsonNode hitsNode, List<String> uuids) {
        List<JsonNode> filteredTasks = new ArrayList<>();
        for (JsonNode hit : hitsNode) {
            JsonNode dataNode = hit.path("_source").path("Data");
            JsonNode assignedToNode = dataNode.path("assignedTo");

            boolean isAssigned = false;
            for (JsonNode assigned : assignedToNode) {
                String assignedUuid = assigned.path("uuid").asText();
                if (uuids.contains(assignedUuid)) {
                    isAssigned = true;
                    break;
                }
            }
            if (isAssigned) {
                filteredTasks.add(hit);
            }
        }
        return filteredTasks;
    }

    private List<JsonNode> filterPendingTaskLitigant(JsonNode hitsNode) {
        List<JsonNode> filteredTasks = new ArrayList<>();
        for(JsonNode hit: hitsNode) {
            JsonNode dataNode = hit.path("_source").path("Data");
            if(dataNode.get("entityType").toString().equals("application-order-submission-feedback") && dataNode.get("status").toString().equals("PENDINGRESPONSE")) {
                filteredTasks.add(dataNode);
            }
        }
        return filteredTasks;
    }

    private List<JsonNode> checkComplainantJoinCase(List<JsonNode> tasks, Map<String, Object> joinCase) {
        Object caseObject = caseUtil.getCase((JSONObject) joinCase.get("requestInfo"), "kl", null, joinCase.get("caseFilingNumber").toString(), null);
        List<Map<String, Object>> litigantList = JsonPath.read(caseObject, "litigants");

        litigantList = litigantList.stream()
                .filter(litigant -> {
                    Object partyType = litigant.get("partyType");
                    return partyType != null && partyType.toString().matches("complainant\\\\..*");
                })
                .collect(Collectors.toList());

        List<String> litigantUuids = getLitigantUuids(litigantList, (RequestInfo) joinCase.get("requestInfo"));

        return tasks.stream()
                .filter(task -> {
                    JsonNode assignedTo = task.at("/_source/Data/assignedTo");
                    if (assignedTo.isArray()) {
                        for (JsonNode userNode : assignedTo) {
                            JsonNode uuidNode = userNode.get("uuid");
                            if (uuidNode != null && litigantUuids.contains(uuidNode.asText())) {
                                return false;
                            }
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());

    }
}