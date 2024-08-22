package org.pucar.dristi.service;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ElasticSearchRepository;
import org.pucar.dristi.util.jsonmapper.*;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.pucar.dristi.config.ServiceConstants.*;

@Service
@Slf4j
public class CaseManagerService {

	private final ElasticSearchRepository esRepository;
	private final Configuration configuration;
	private final CourtCaseMapper courtCaseMapper;
	private final HearingMapper hearingMapper;
	private final WitnessMapper witnessMapper;
	private final OrderMapper orderMapper;
	private final TaskMapper taskMapper;
	private final ApplicationMapper applicationMapper;
	private final ArtifactMapper artifactMapper;

	@Autowired
	public CaseManagerService(ElasticSearchRepository esRepository, Configuration configuration,
							  CourtCaseMapper courtCaseMapper, HearingMapper hearingMapper,
							  WitnessMapper witnessMapper, OrderMapper orderMapper,
							  TaskMapper taskMapper, ApplicationMapper applicationMapper,
							  ArtifactMapper artifactMapper) {
		this.esRepository = esRepository;
		this.configuration = configuration;
		this.courtCaseMapper = courtCaseMapper;
		this.hearingMapper = hearingMapper;
		this.witnessMapper = witnessMapper;
		this.orderMapper = orderMapper;
		this.taskMapper = taskMapper;
		this.applicationMapper = applicationMapper;
		this.artifactMapper = artifactMapper;
	}

	public List<CaseFile> getCaseFiles(CaseRequest caseRequest) {
		List<CaseFile> caseFileList = new ArrayList<>();
		String filingNumber = caseRequest.getFilingNumber();

		try {
			List<CourtCase> courtCaseList = getCases(filingNumber);
			if (courtCaseList == null || courtCaseList.isEmpty()) {
				log.info("No court cases found for filing number: {}", filingNumber);
				return caseFileList;
			}

			for (CourtCase courtCase : courtCaseList) {
				CaseFile caseFile = new CaseFile();
				caseFile.setCourtCase(courtCase);

				caseFile.setHearings(getHearings(filingNumber));
				caseFile.setWitnesses(getWitnesses(filingNumber));
				caseFile.setOrders(getOrderTasks(filingNumber));
				caseFile.setApplications(getApplications(filingNumber));
				caseFile.setEvidence(getArtifacts(filingNumber));

				caseFileList.add(caseFile);
			}
		} catch (CustomException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error building case files using filing number: {}", filingNumber, e);
			throw new CustomException("CASE_FILE_ERROR", "Error building case files using filing number:" + e.getMessage());
		}
		return caseFileList;
	}

	public <T> List<T> retrieveDocuments(String searchKeyValue, String indexName, String searchKeyPath, String sortKeyPath, String sortOrder, Function<JSONObject, T> converter, String errorCode) {
		try {
			List<T> documentList = new ArrayList<>();

			log.info("Retrieving documents from index: {} with search key: {}", indexName, searchKeyValue);
			String uri = configuration.getEsHostUrl() + indexName + configuration.getSearchPath();
			String request = String.format(
					ES_TERM_QUERY,
					searchKeyPath, searchKeyValue, sortKeyPath, sortOrder
			);
			log.debug("Elasticsearch request: {}", request);
			String response = esRepository.fetchDocuments(uri, request);
			log.debug("Elasticsearch response: {}", response);

			JSONArray jsonArray = constructJsonArray(response, ES_HITS_PATH);
			for (int i = 0; i < jsonArray.length(); i++) {
				if (jsonArray.get(i) != null) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					T document = converter.apply(jsonObject);
					documentList.add(document);
				}
			}
			return documentList;
		} catch (CustomException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error retrieving documents using: {}", searchKeyValue, e);
			throw new CustomException(errorCode, "Error retrieving documents:" + e.getMessage());
		}
	}

	public List<CourtCase> getCases(String filingNumber) {
		return retrieveDocuments(filingNumber, configuration.getCaseIndex(), CASE_BASE_PATH + FILING_NUMBER, CASE_BASE_PATH + CREATED_TIME, ORDER_ASC, courtCaseMapper::getCourtCase, "COURT_CASE_ERROR");
	}

	public List<Hearing> getHearings(String filingNumber) {
		return retrieveDocuments(filingNumber, configuration.getHearingIndex(), HEARING_BASE_PATH + FILING_NUMBER, CREATED_TIME_ABSOLUTE, ORDER_ASC, hearingMapper::getHearing, "HEARING_ERROR");
	}

	public List<Witness> getWitnesses(String filingNumber) {
		return retrieveDocuments(filingNumber, configuration.getWitnessIndex(), WITNESS_BASE_PATH + FILING_NUMBER, WITNESS_BASE_PATH + CREATED_TIME, ORDER_ASC, witnessMapper::getWitness, "WITNESS_ERROR");
	}

	public List<OrderTasks> getOrderTasks(String filingNumber) throws Exception {
		List<OrderTasks> orderTasksList = new ArrayList<>();
		List<Order> orderList = getOrders(filingNumber);

		for (Order order : orderList) {
			List<Task> taskList = getTasks(order.getId().toString());
			OrderTasks orderTasks = OrderTasks.builder().order(order).tasks(taskList).build();
			orderTasksList.add(orderTasks);
		}
		log.info("Retrieved {} order tasks for filing number: {}", orderTasksList.size(), filingNumber);
		return orderTasksList;
	}

	public List<Order> getOrders(String filingNumber) {
		return retrieveDocuments(filingNumber, configuration.getOrderIndex(), ORDER_BASE_PATH + FILING_NUMBER, ORDER_BASE_PATH + CREATED_TIME, ORDER_ASC, orderMapper::getOrder, "ORDER_ERROR");
	}

	public List<Task> getTasks(String orderId) {
		return retrieveDocuments(orderId, configuration.getTaskIndex(), TASK_BASE_PATH + ORDER_ID, TASK_BASE_PATH + CREATED_TIME, ORDER_ASC, taskMapper::getTask, "TASK_ERROR");
	}

	public List<Application> getApplications(String filingNumber) {
		return retrieveDocuments(filingNumber, configuration.getApplicationIndex(), APPLICATION_BASE_PATH + FILING_NUMBER, CREATED_TIME_ABSOLUTE, ORDER_ASC, applicationMapper::getApplication, "APPLICATION_ERROR");
	}

	public List<Artifact> getArtifacts(String filingNumber) {
		return retrieveDocuments(filingNumber, configuration.getArtifactIndex(), ARTIFACT_BASE_PATH + FILING_NUMBER, CREATED_TIME_ABSOLUTE, ORDER_ASC, artifactMapper::getArtifact, "ARTIFACT_ERROR");
	}

	public JSONArray constructJsonArray(String json, String jsonPath) {
		try {
			Object result = JsonPath.read(json, jsonPath);
			if (result instanceof List && ((List<?>) result).isEmpty()) {
				return new JSONArray(); // Return an empty JSONArray if the list is empty
			}
			return new JSONArray(result.toString());
		} catch (PathNotFoundException e) {
			log.error("JSON path not found: {}", jsonPath, e);
			throw new CustomException("JSON_PATH_NOT_FOUND", e.getMessage());
		} catch (JSONException e) {
			log.error("Error parsing JSON: {}", json, e);
			throw new CustomException("JSON_PARSING_ERR", e.getMessage());
		} catch (Exception e) {
			log.error("Exception while constructing JSON array: ", e);
			log.error("Object: {}", json);
			throw new CustomException("JSON_ARRAY_CONSTRUCTION_ERR", e.getMessage());
		}
	}
}
