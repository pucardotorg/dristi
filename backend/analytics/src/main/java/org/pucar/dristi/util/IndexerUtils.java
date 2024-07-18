package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.kafka.consumer.EventConsumerConfig;
import org.pucar.dristi.web.models.CaseOverallStatus;
import org.pucar.dristi.web.models.CaseStageSubStage;
import org.pucar.dristi.web.models.PendingTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.pucar.dristi.config.ServiceConstants.*;

@Service
@Slf4j
public class IndexerUtils {

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	private final RestTemplate restTemplate;

	private final Configuration config;

	private final CaseUtil caseUtil;

	private final HearingUtil hearingUtil;

	private final EvidenceUtil evidenceUtil;

	private final TaskUtil taskUtil;

	private final ApplicationUtil applicationUtil;

	private final OrderUtil orderUtil;

	private final Producer producer;

	private final ObjectMapper mapper;

	@Autowired
    public IndexerUtils(RestTemplate restTemplate, Configuration config, CaseUtil caseUtil, HearingUtil hearingUtil, EvidenceUtil evidenceUtil, TaskUtil taskUtil, ApplicationUtil applicationUtil, OrderUtil orderUtil, Producer producer, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.config = config;
        this.caseUtil = caseUtil;
        this.hearingUtil = hearingUtil;
        this.evidenceUtil = evidenceUtil;
        this.taskUtil = taskUtil;
        this.applicationUtil = applicationUtil;
        this.orderUtil = orderUtil;
        this.producer = producer;
        this.mapper = mapper;
    }

    public JSONObject createRequestInfo() {
		JSONObject userInfo = new JSONObject();
		userInfo.put("id", 73);
		userInfo.put("tenantId",config.getStateLevelTenantId());

		JSONObject requestInfo = new JSONObject();
		requestInfo.put("apiId", "org.egov.pt");
		requestInfo.put("ver", "1.0");
		requestInfo.put("ts", 1502890899493L);
		requestInfo.put("action", "asd");
		requestInfo.put("did", "4354648646");
		requestInfo.put("key", "xyz");
		requestInfo.put("msgId", "654654");
		requestInfo.put("requesterId", "61");
		requestInfo.put("authToken", "02dbe5be-28df-4d82-954f-3d27c56cca7d");
		requestInfo.put("userInfo", userInfo);

		return requestInfo;
	}

	/**
	 * A Poll thread that polls es for its status and keeps the kafka container
	 * paused until ES is back up. Once ES is up, container is resumed and all the
	 * stacked up records in the queue are processed.
	 */
	public void orchestrateListenerOnESHealth() {
		EventConsumerConfig.pauseContainer();
		log.info("Polling ES....");
		final Runnable esPoller = new Runnable() {
			boolean threadRun = true;

			public void run() {
				if (threadRun) {
					Object response = null;
					try {
                        String url = config.getEsHostUrl() + "/_search";
						final HttpHeaders headers = new HttpHeaders();
						headers.add("Authorization", getESEncodedCredentials());
						final HttpEntity entity = new HttpEntity(headers);
						response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
					} catch (Exception e) {
						log.error("ES is DOWN..");
					}
					if (response != null) {
						log.info("ES is UP!");
						EventConsumerConfig.resumeContainer();
						threadRun = false;
					}
				}
			}
		};
		scheduler.scheduleAtFixedRate(esPoller, 0, Long.parseLong(config.getPollInterval()), TimeUnit.SECONDS);
	}

	public String buildString(Object object) {
		// JsonPath cannot be applied on the type JSONObject. String has to be built of
		// it and then used.
		String[] array = object.toString().split(":");
		StringBuilder jsonArray = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			jsonArray.append(array[i]);
			if (i != array.length - 1)
				jsonArray.append(":");
		}
		return jsonArray.toString();
	}

	public String getESEncodedCredentials() {
		String credentials = config.getEsUsername() + ":" + config.getEsPassword();
		byte[] credentialsBytes = credentials.getBytes();
		byte[] base64CredentialsBytes = Base64.getEncoder().encode(credentialsBytes);
		return "Basic " + new String(base64CredentialsBytes);
	}

	public String buildPayload(PendingTask pendingTask) {

		String id = pendingTask.getId();
		String name = pendingTask.getName();
		String entityType = pendingTask.getEntityType();
		String referenceId = pendingTask.getReferenceId();
		String status = pendingTask.getStatus();
		Long stateSla = pendingTask.getStateSla();
		Long businessServiceSla = pendingTask.getBusinessServiceSla();
		List<User> assignedToList = pendingTask.getAssignedTo();
		List<String> assignedRoleList = pendingTask.getAssignedRole();
		String assignedTo = new JSONArray(assignedToList).toString();
		String assignedRole = new JSONArray(assignedRoleList).toString();
		Boolean isCompleted = pendingTask.getIsCompleted();
		String cnrNumber = pendingTask.getCnrNumber();
		String filingNumber = pendingTask.getFilingNumber();
		String additionalDetails = "{}";
		try {
			additionalDetails = mapper.writeValueAsString(pendingTask.getAdditionalDetails());
		}catch (Exception e){
			log.error("Error while building API payload");
			throw new CustomException(Pending_Task_Exception, "Error occurred while preparing pending task: " + e);
		}


		return String.format(
				ES_INDEX_HEADER_FORMAT + ES_INDEX_DOCUMENT_FORMAT,
				config.getIndex(), referenceId, id, name, entityType, referenceId, status, assignedTo, assignedRole, cnrNumber, filingNumber, isCompleted, stateSla, businessServiceSla, additionalDetails
		);
	}

	public String buildPayload(String jsonItem) {

		String id = JsonPath.read(jsonItem, ID_PATH);
		String name = "";
		String entityType = JsonPath.read(jsonItem, BUSINESS_SERVICE_PATH);
		String referenceId = JsonPath.read(jsonItem, BUSINESS_ID_PATH);
		String status = JsonPath.read(jsonItem, STATE_PATH);
		Object stateSlaObj = JsonPath.read(jsonItem, STATE_SLA_PATH);
		Long stateSla = stateSlaObj != null ? ((Number) stateSlaObj).longValue() : null;
		Object businessServiceSlaObj = JsonPath.read(jsonItem, BUSINESS_SERVICE_SLA_PATH);
		Long businessServiceSla = businessServiceSlaObj != null ? ((Number) businessServiceSlaObj).longValue() : null;
		List<Object> assignedToList = JsonPath.read(jsonItem, ASSIGNES_PATH);
		List<String> assignedRoleList = JsonPath.read(jsonItem, ASSIGNED_ROLE_PATH);
		String assignedTo = new JSONArray(assignedToList).toString();
		String assignedRole = new JSONArray(assignedRoleList).toString();
		Boolean isCompleted = JsonPath.read(jsonItem, IS_TERMINATE_STATE_PATH);
		String tenantId = JsonPath.read(jsonItem, TENANT_ID_PATH);
		String action = JsonPath.read(jsonItem, ACTION_PATH);
		String additionalDetails;

        log.info("Inside indexer utils build payload:: entityType: {}, referenceId: {}", entityType, referenceId);
		Map<String, String> details = processEntity(entityType, referenceId,status,action,tenantId);
		String cnrNumber = details.get("cnrNumber");
		String filingNumber = details.get("filingNumber");
		try {
			additionalDetails = mapper.writeValueAsString(new HashMap<String,Object>());
		}catch (Exception e){
			log.error("Error while building listener payload");
			throw new CustomException(Pending_Task_Exception, "Error occurred while preparing pending task: " + e);
		}
        if (filingNumber != null && !filingNumber.isEmpty()) {
            return String.format(
                    ES_INDEX_HEADER_FORMAT + ES_INDEX_DOCUMENT_FORMAT,
                    config.getIndex(), referenceId, id, name, entityType, referenceId, status, assignedTo, assignedRole, cnrNumber, filingNumber, isCompleted, stateSla, businessServiceSla, additionalDetails
            );
        }
        return null;
    }

	private Map<String, String> processEntity(String entityType, String referenceId, String status, String action, String tenantId) {
		Map<String, String> caseDetails = new HashMap<>();
		JSONObject request = new JSONObject();
		request.put("RequestInfo", createRequestInfo());
		if(!action.contains("SAVE_DRAFT")) try {
		switch (entityType.toLowerCase()) {
			case "hearing":
				caseDetails = processHearingEntity(request, referenceId, action, tenantId);
                break;
			case "case":
				caseDetails = processCaseEntity(request, referenceId, action, tenantId);
				break;
			case "evidence":
				caseDetails = processEvidenceEntity(request, referenceId);
				break;
			case "async-voluntary-submission-services":
			case "asynsubmissionwithresponse":
			case "asyncsubmissionwithoutresponse":
				caseDetails = processApplicationEntity(request, referenceId);
				break;
			default:
				if (entityType.toLowerCase().contains("task")) {
					caseDetails = processTaskEntity(request, referenceId);
				}
				else if (entityType.toLowerCase().contains("order")) {
					caseDetails = processOrderEntity(request, referenceId, status, tenantId);
				}
				else {
					log.error("Unexpected entityType: {}", entityType);
				}
				break;
		}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return caseDetails;
	}

	private Map<String, String> processHearingEntity(JSONObject request, String referenceId, String action, String tenantId) throws InterruptedException {
		Map<String, String> caseDetails = new HashMap<>();
		Thread.sleep(config.getApiCallDelayInSeconds()*1000);
		Object hearingObject = hearingUtil.getHearing(request, null, null, referenceId, config.getStateLevelTenantId());

		List<String> cnrNumbers = JsonPath.read(hearingObject.toString(), CNR_NUMBERS_PATH);
		String cnrNumber;
		String filingNumber;

		if (cnrNumbers == null || cnrNumbers.isEmpty()) {
			List<String> filingNumberList = JsonPath.read(hearingObject.toString(), FILING_NUMBER_PATH);
			if (filingNumberList != null && !filingNumberList.isEmpty()) {
				filingNumber = filingNumberList.get(0);
			} else {
				log.info("Inside indexer util processEntity:: Both cnr and filing numbers are not present");
				return caseDetails;
			}
			Object caseObject = caseUtil.getCase(request, config.getStateLevelTenantId(), null, filingNumber, null);
			cnrNumber = JsonPath.read(caseObject.toString(), CNR_NUMBER_PATH);
		} else {
			cnrNumber = cnrNumbers.get(0);
			Object caseObject = caseUtil.getCase(request, config.getStateLevelTenantId(), cnrNumber, null, null);
			filingNumber = JsonPath.read(caseObject.toString(), FILING_NUMBER_PATH);
		}

		caseDetails.put("cnrNumber", cnrNumber);
		caseDetails.put("filingNumber", filingNumber);

		String hearingType = JsonPath.read(hearingObject.toString(), HEARING_TYPE_PATH);
		publishToCaseOverallStatus(determineHearingStage( filingNumber, tenantId, hearingType, action ));
		return caseDetails;
	}

	private Map<String, String> processCaseEntity(JSONObject request, String referenceId, String action, String tenantId) throws InterruptedException {
		Map<String, String> caseDetails = new HashMap<>();
		Thread.sleep(config.getApiCallDelayInSeconds()*1000);
        Object caseObject = caseUtil.getCase(request, config.getStateLevelTenantId(), null, referenceId, null);
		String cnrNumber = JsonPath.read(caseObject.toString(), CNR_NUMBER_PATH);

		publishToCaseOverallStatus(determineCaseStage(referenceId,tenantId,action));

		caseDetails.put("cnrNumber", cnrNumber);
		caseDetails.put("filingNumber", referenceId);

		return caseDetails;
	}

	private Map<String, String> processEvidenceEntity(JSONObject request, String referenceId) throws InterruptedException {
		Map<String, String> caseDetails = new HashMap<>();
		Thread.sleep(config.getApiCallDelayInSeconds()*1000);
		Object artifactObject = evidenceUtil.getEvidence(request, config.getStateLevelTenantId(), referenceId);
		String caseId = JsonPath.read(artifactObject.toString(), CASE_ID_PATH);
		Object caseObject = caseUtil.getCase(request, config.getStateLevelTenantId(), null, null, caseId);
		String filingNumber = JsonPath.read(caseObject.toString(), FILING_NUMBER_PATH);
		String cnrNumber = JsonPath.read(caseObject.toString(), CNR_NUMBER_PATH);

		caseDetails.put("cnrNumber", cnrNumber);
		caseDetails.put("filingNumber", filingNumber);

		return caseDetails;
	}

	private Map<String, String> processTaskEntity(JSONObject request, String referenceId) throws InterruptedException {
		Map<String, String> caseDetails = new HashMap<>();
		Thread.sleep(config.getApiCallDelayInSeconds()*1000);
		Object taskObject = taskUtil.getTask(request, config.getStateLevelTenantId(), referenceId);
		String cnrNumber = JsonPath.read(taskObject.toString(), CNR_NUMBER_PATH);
		Object caseObject = caseUtil.getCase(request, config.getStateLevelTenantId(), cnrNumber, null, null);
		String filingNumber = JsonPath.read(caseObject.toString(), FILING_NUMBER_PATH);

		caseDetails.put("cnrNumber", cnrNumber);
		caseDetails.put("filingNumber", filingNumber);

		return caseDetails;
	}

	private Map<String, String> processApplicationEntity(JSONObject request, String referenceId) throws InterruptedException {
		Map<String, String> caseDetails = new HashMap<>();
		Thread.sleep(config.getApiCallDelayInSeconds()*1000);
		Object applicationObject = applicationUtil.getApplication(request, config.getStateLevelTenantId(), referenceId);
		String caseId = JsonPath.read(applicationObject.toString(), CASE_ID_PATH);
		String cnrNumber = JsonPath.read(applicationObject.toString(), CNR_NUMBER_PATH);
		String filingNumber = JsonPath.read(applicationObject.toString(), FILING_NUMBER_PATH);
		Object caseObject = caseUtil.getCase(request, config.getStateLevelTenantId(), cnrNumber, filingNumber, caseId);

		caseDetails.put("cnrNumber", JsonPath.read(caseObject.toString(), CNR_NUMBER_PATH));
		caseDetails.put("filingNumber", JsonPath.read(caseObject.toString(), FILING_NUMBER_PATH));

		return caseDetails;
	}

	private Map<String, String> processOrderEntity(JSONObject request, String referenceId, String status, String tenantId) throws InterruptedException {
		Map<String, String> caseDetails = new HashMap<>();
		Thread.sleep(config.getApiCallDelayInSeconds()*1000);
		Object orderObject = orderUtil.getOrder(request, referenceId, config.getStateLevelTenantId());
		String cnrNumber = JsonPath.read(orderObject.toString(), CNR_NUMBER_PATH);
		String filingNumber = JsonPath.read(orderObject.toString(), FILING_NUMBER_PATH);
		Object caseObject = caseUtil.getCase(request, config.getStateLevelTenantId(), cnrNumber, filingNumber, null);

		caseDetails.put("cnrNumber", JsonPath.read(caseObject.toString(), CNR_NUMBER_PATH));
		caseDetails.put("filingNumber", JsonPath.read(caseObject.toString(), FILING_NUMBER_PATH));
		String orderType = JsonPath.read(orderObject.toString(), ORDER_TYPE_PATH);

		publishToCaseOverallStatus(determineOrderStage(filingNumber, tenantId, orderType, status));
		return caseDetails;
	}


	private CaseOverallStatus determineCaseStage(String filingNumber, String tenantId, String action) {
        return switch (action.toLowerCase()) {
			case "send_back", "submit_case" ->new CaseOverallStatus(filingNumber, tenantId, "Pre-Trial", "Filing");
            case "validate" -> new CaseOverallStatus(filingNumber, tenantId, "Pre-Trial", "Cognizance");
            case "admit" -> new CaseOverallStatus(filingNumber, tenantId, "Pre-Trial", "Appearance");
            default -> null;
        };
	}

	private CaseOverallStatus determineHearingStage(String filingNumber, String tenantId, String hearingType, String action) {

		switch (hearingType.toLowerCase()) {
			case "evidence":
				if (action.equalsIgnoreCase("create")) {
					return new CaseOverallStatus(filingNumber, tenantId, "Trial", "Evidence");
				}
				break;
			case "arguments":
				if (action.equalsIgnoreCase("create")) {
					return new CaseOverallStatus(filingNumber, tenantId, "Trial", "Arguments");
				}
				break;
			case "judgement":
				if (action.equalsIgnoreCase("create")) {
					return new CaseOverallStatus(filingNumber, tenantId, "Post-Trial", "Judgment");
				}
				break;
		}
		return null;
	}

	private CaseOverallStatus determineOrderStage(String filingNumber, String tenantId, String orderType, String status) {

		if (orderType.equalsIgnoreCase("judgement") && status.equalsIgnoreCase("published")) {
			return new CaseOverallStatus(filingNumber, tenantId, "Post-Trial", "Post-Judgement");
		}
		return null;
	}

	private void publishToCaseOverallStatus(CaseOverallStatus caseOverallStatus) {
		try {
			if(caseOverallStatus==null){
				log.info("Case overall workflow update not eligible");
			}
			else if(caseOverallStatus.getFilingNumber()==null){
				log.error("Filing number not present for Case overall workflow update");
			}
			else{
				log.info("Publishing to kafka topic: {}, case: {}",config.getCaseOverallStatusTopic(), caseOverallStatus);
				CaseStageSubStage caseStageSubStage = new CaseStageSubStage(caseOverallStatus);
				producer.push(config.getCaseOverallStatusTopic(), caseStageSubStage);
			}
		} catch (Exception e) {
			log.error("Error in publishToCaseOverallStatus method", e);
		}
	}

	public void esPost(String uri, String request) {
		try {
            log.debug("Record being indexed: {}", request);

			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.add("Authorization", getESEncodedCredentials());
			final HttpEntity<String> entity = new HttpEntity<>(request, headers);

			String response = restTemplate.postForObject(uri, entity, String.class);
			if (uri.contains("_bulk") && JsonPath.read(response, ERRORS_PATH).equals(true)) {
				log.info("Indexing FAILED!!!!");
                log.info("Response from ES: {}", response);
			}
		} catch (final ResourceAccessException e) {
			log.error("ES is DOWN, Pausing kafka listener.......");
			orchestrateListenerOnESHealth();
		} catch (Exception e) {
			log.error("Exception while trying to index the ES documents. Note: ES is not Down.", e);
		}
	}

	public void esPostManual(String uri, String request) throws Exception {
		try {
			log.debug("Record being indexed: {}", request);

			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.add("Authorization", getESEncodedCredentials());
			final HttpEntity<String> entity = new HttpEntity<>(request, headers);

			String response = restTemplate.postForObject(uri, entity, String.class);
			if (uri.contains("_bulk") && JsonPath.read(response, ERRORS_PATH).equals(true)) {
				log.info("Indexing FAILED!!!!");
				log.info("Response from ES: {}", response);
				throw new Exception("Error while updating index");
			}
		}
		catch (Exception e) {
			log.error("Exception while trying to index the ES documents. Note: ES is not Down.", e);
			throw e;
		}
	}

}
