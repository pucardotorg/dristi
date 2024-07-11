package org.pucar.dristi.service;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pucar.dristi.config.ServiceConfiguration;
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
	private final ServiceConfiguration config;
	private final CourtCaseMapper courtCaseMapper;
	private final HearingMapper hearingMapper;
	private final WitnessMapper witnessMapper;
	private final OrderMapper orderMapper;
	private final TaskMapper taskMapper;
	private final ApplicationMapper applicationMapper;
	private final ArtifactMapper artifactMapper;

	@Autowired
	public CaseManagerService(ElasticSearchRepository esRepository, ServiceConfiguration config,
							  CourtCaseMapper courtCaseMapper, HearingMapper hearingMapper,
							  WitnessMapper witnessMapper, OrderMapper orderMapper,
							  TaskMapper taskMapper, ApplicationMapper applicationMapper,
							  ArtifactMapper artifactMapper) {
		this.esRepository = esRepository;
		this.config = config;
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
		try {
			List<CourtCase> courtCaseList = getCases(caseRequest.getFilingNumber());
			for (CourtCase courtCase : courtCaseList) {
				CaseFile caseFile = CaseFile.builder()
						.cases(courtCase)
						.hearings(getHearings(caseRequest.getFilingNumber()))
						.witnesses(getWitnesses(caseRequest.getFilingNumber()))
						.orders(getOrderTasks(caseRequest.getFilingNumber()))
						.applications(getApplications(caseRequest.getFilingNumber()))
						.evidence(getArtifacts(caseRequest.getFilingNumber()))
						.build();
				caseFileList.add(caseFile);
			}
		} catch (Exception e) {
			log.error("Error getting case files using filing number: {}", caseRequest.getFilingNumber(), e);
			throw new RuntimeException(e);
		}
		return caseFileList;
	}

	private <T> List<T> retrieveDocuments(String searchKeyValue, String indexName, String searchKeyPath, String sortKeyPath, String sortOrder, Function<JSONObject, T> converter) throws Exception {
		List<T> documentList = new ArrayList<>();

		String uri = config.getEsHostUrl() + indexName + config.getSearchPath();
		String request = String.format(
				ES_TERM_QUERY,
				searchKeyPath, searchKeyValue, sortKeyPath, sortOrder
		);
		String response = esRepository.fetchDocuments(uri, request);

		JSONArray jsonArray = constructJsonArray(response, ES_HITS_PATH);
		for (int i = 0; i < jsonArray.length(); i++) {
			if (jsonArray.get(i) != null) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				T document = converter.apply(jsonObject);
				documentList.add(document);
			}
		}
		return documentList;
	}

	public List<CourtCase> getCases(String filingNumber) throws Exception {
		return retrieveDocuments(filingNumber, config.getCaseIndex(), CASE_BASE_PATH + FILING_NUMBER, CASE_BASE_PATH + CREATED_TIME, ORDER_ASC, courtCaseMapper::getCourtCase);
	}

	public List<Hearing> getHearings(String filingNumber) throws Exception {
		return retrieveDocuments(filingNumber, config.getHearingIndex(), HEARING_BASE_PATH + FILING_NUMBER, CREATED_TIME_ABSOLUTE, ORDER_ASC, hearingMapper::getHearing);
	}

	public List<Witness> getWitnesses(String filingNumber) throws Exception {
		return retrieveDocuments(filingNumber, config.getWitnessIndex(), WITNESS_BASE_PATH + FILING_NUMBER, WITNESS_BASE_PATH + CREATED_TIME, ORDER_ASC, witnessMapper::getWitness);
	}

	public List<OrderTasks> getOrderTasks(String filingNumber) throws Exception {
		List<OrderTasks> orderTasksList = new ArrayList<>();
		List<Order> orderList = getOrders(filingNumber);

		for (Order order : orderList) {
			List<Task> taskList = getTasks(order.getId().toString());
			OrderTasks orderTasks = OrderTasks.builder().order(order).tasks(taskList).build();
			orderTasksList.add(orderTasks);
		}
		return orderTasksList;
	}

	public List<Order> getOrders(String filingNumber) throws Exception {
		return retrieveDocuments(filingNumber, config.getOrderIndex(), ORDER_BASE_PATH + FILING_NUMBER, ORDER_BASE_PATH + CREATED_TIME, ORDER_ASC, orderMapper::getOrder);
	}

	public List<Task> getTasks(String orderId) throws Exception {
		return retrieveDocuments(orderId, config.getTaskIndex(), TASK_BASE_PATH + ORDER_ID, TASK_BASE_PATH + CREATED_TIME, ORDER_ASC, taskMapper::getTask);
	}

	public List<Application> getApplications(String filingNumber) throws Exception {
		return retrieveDocuments(filingNumber, config.getApplicationIndex(), APPLICATION_BASE_PATH + FILING_NUMBER, CREATED_TIME_ABSOLUTE, ORDER_ASC, applicationMapper::getApplication);
	}

	public List<Artifact> getArtifacts(String filingNumber) throws Exception {
		return retrieveDocuments(filingNumber, config.getArtifactIndex(), ARTIFACT_BASE_PATH + FILING_NUMBER, CREATED_TIME_ABSOLUTE, ORDER_ASC, artifactMapper::getArtifact);
	}

	public JSONArray constructJsonArray(String json, String jsonPath) throws Exception {
		try {
			Object result = JsonPath.read(json, jsonPath);
			if (result instanceof List && ((List<?>) result).isEmpty()) {
				return new JSONArray(); // Return an empty JSONArray if the list is empty
			}
			return new JSONArray(result.toString());
		} catch (PathNotFoundException e) {
			log.error("JSON path not found: {}", jsonPath, e);
			return new JSONArray();
		} catch (JSONException e) {
			log.error("Error parsing JSON: {}", json, e);
			throw e;
		} catch (Exception e) {
			log.error("Exception while constructing JSON array: ", e);
			log.error("Object: {}", json);
			throw e;
		}
	}
}
