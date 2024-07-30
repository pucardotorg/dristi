package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.web.models.CaseOverallStatus;
import org.pucar.dristi.web.models.CaseStageSubStage;
import org.pucar.dristi.web.models.CaseOutcome;
import org.pucar.dristi.web.models.Outcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.*;

@Slf4j
@Component
public class CaseOverallStatusUtil {

	private final Configuration config;
    private final HearingUtil hearingUtil;
	private final OrderUtil orderUtil;
	private final Producer producer;
	private final ObjectMapper mapper;


	@Autowired
	public CaseOverallStatusUtil(Configuration config, HearingUtil hearingUtil, OrderUtil orderUtil, Producer producer, ObjectMapper mapper) {
		this.config = config;
        this.hearingUtil = hearingUtil;
        this.orderUtil = orderUtil;
        this.producer = producer;
		this.mapper = mapper;
    }

	public Object checkCaseOverAllStatus(String entityType, String referenceId, String status, String action, String tenantId, JSONObject requestInfo) {
		try {
			JSONObject request = new JSONObject();
			request.put("RequestInfo", requestInfo);
			if(config.getCaseBusinessServiceList().contains(entityType)){
				return processCaseOverallStatus(request, referenceId, action, tenantId);
			} else if (config.getHearingBusinessServiceList().contains(entityType)) {
				return processHearingCaseOverallStatus(request, referenceId, action, tenantId);
			} else if (config.getOrderBusinessServiceList().contains(entityType)) {
				return processOrderOverallStatus(request, referenceId, status, tenantId);
			}
			log.error("Case overall status not supported for entityType: {}", entityType);
			return null;
		} catch (InterruptedException e) {
			log.error("Processing interrupted for entityType: {}", entityType, e);
			Thread.currentThread().interrupt(); // Restore the interrupted status
			throw new RuntimeException(e);
		}
	}

	private Object processOrderOverallStatus(JSONObject request, String referenceId, String status, String tenantId) throws InterruptedException {
		Thread.sleep(config.getApiCallDelayInSeconds()*1000);
		Object orderObject = orderUtil.getOrder(request, referenceId, config.getStateLevelTenantId());
		String filingNumber = JsonPath.read(orderObject.toString(), FILING_NUMBER_PATH);
		String orderType = JsonPath.read(orderObject.toString(), ORDER_TYPE_PATH);
		publishToCaseOverallStatus(determineOrderStage(filingNumber, tenantId, orderType, status),request);
		publishToCaseOutcome(determineCaseOutcome(filingNumber, tenantId, orderType, status, orderObject),request);
		return orderObject;
	}

	private Object processCaseOverallStatus(JSONObject request, String referenceId, String action, String tenantId) {
		publishToCaseOverallStatus(determineCaseStage(referenceId,tenantId,action), request);
		return null;
	}

	private Object processHearingCaseOverallStatus(JSONObject request, String referenceId, String action, String tenantId) throws InterruptedException {
		Thread.sleep(config.getApiCallDelayInSeconds()*1000);
		Object hearingObject = hearingUtil.getHearing(request, null, null, referenceId, config.getStateLevelTenantId());
		List<String> filingNumberList = JsonPath.read(hearingObject.toString(), FILING_NUMBER_PATH);
		String filingNumber;
		if (filingNumberList != null && !filingNumberList.isEmpty()) {
			filingNumber = filingNumberList.get(0);
		}
		else {
			log.info("Inside indexer util processHearingCaseOverallStatus:: Filing number not present");
			throw new RuntimeException("Filing number not present for case overall status");
		}
		String hearingType = JsonPath.read(hearingObject.toString(), HEARING_TYPE_PATH);
		publishToCaseOverallStatus(determineHearingStage( filingNumber, tenantId, hearingType, action ), request);
		return hearingObject;
	}

	private CaseOverallStatus determineCaseStage(String filingNumber, String tenantId, String action) {
		return switch (action.toLowerCase()) {
			case "send_back", "submit_case" ->new CaseOverallStatus(filingNumber, tenantId, "Pre-Trial", "Filing");
			case "validate" -> new CaseOverallStatus(filingNumber, tenantId, "Pre-Trial", "Cognizance");
			case "admit" -> new CaseOverallStatus(filingNumber, tenantId, "Pre-Trial", "Appearance");
			case "save_draft" -> new CaseOverallStatus(filingNumber, tenantId, "Pre-Trial", "Draft");
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

	private void publishToCaseOverallStatus(CaseOverallStatus caseOverallStatus, JSONObject request) {
		try {
			if(caseOverallStatus==null){
				log.info("Case overall workflow update not eligible");
			}
			else if(caseOverallStatus.getFilingNumber()==null){
				log.error("Filing number not present for Case overall workflow update");
			}
			else{
				RequestInfo requestInfo = mapper.readValue(request.getJSONObject("RequestInfo").toString(), RequestInfo.class);
				AuditDetails auditDetails = new AuditDetails();
				auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getUuid());
				auditDetails.setLastModifiedTime(System.currentTimeMillis());
				caseOverallStatus.setAuditDetails(auditDetails);
				CaseStageSubStage caseStageSubStage = new CaseStageSubStage(requestInfo,caseOverallStatus);
				log.info("Publishing to kafka topic: {}, caseStageSubstage: {}",config.getCaseOverallStatusTopic(), caseStageSubStage);
				producer.push(config.getCaseOverallStatusTopic(), caseStageSubStage);
			}
		} catch (Exception e) {
			log.error("Error in publishToCaseOverallStatus method", e);
		}
	}

	private Outcome determineCaseOutcome(String filingNumber, String tenantId, String orderType, String status, Object orderObject) {

		if (!status.equalsIgnoreCase("published"))
			return null;

		if (orderType.equalsIgnoreCase("WITHDRAWAL"))
			return new Outcome(filingNumber, tenantId, "Withdrawn");

		if (orderType.equalsIgnoreCase("SETTLEMENT"))
			return new Outcome(filingNumber, tenantId, "Settled");

		if (orderType.equalsIgnoreCase("CASE_TRANSFER"))
			return new Outcome(filingNumber, tenantId, "Transferred");

		if (orderType.equalsIgnoreCase("Abated"))
			return new Outcome(filingNumber, tenantId, "Abated");

		if (orderType.equalsIgnoreCase("JUDGEMENT"))
            return new Outcome(filingNumber, tenantId, JsonPath.read(orderObject.toString(), ORDER_FINDINGS_PATH));

		return null;
	}

	private void publishToCaseOutcome(Outcome outcome, JSONObject request) {
		try {
			if(outcome==null){
				log.info("Case outcome update not eligible");
			}
			else if(outcome.getFilingNumber()==null){
				log.error("Filing number not present for case outcome update");
			}
			else{
				RequestInfo requestInfo = mapper.readValue(request.getJSONObject("RequestInfo").toString(), RequestInfo.class);
				AuditDetails auditDetails = new AuditDetails();
				auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getUuid());
				auditDetails.setLastModifiedTime(System.currentTimeMillis());
				outcome.setAuditDetails(auditDetails);
				CaseOutcome caseOutcome = new CaseOutcome(requestInfo,outcome);
				log.info("Publishing to kafka topic: {}, caseOutcome: {}",config.getCaseOutcomeTopic(), caseOutcome);
				producer.push(config.getCaseOutcomeTopic(), caseOutcome);
			}
		} catch (Exception e) {
			log.error("Error in publishToCaseOutcome method", e);
		}
	}
}
