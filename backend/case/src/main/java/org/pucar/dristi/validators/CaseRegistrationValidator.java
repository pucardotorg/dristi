package org.pucar.dristi.validators;

import static org.pucar.dristi.config.ServiceConstants.DELETE_DRAFT_WORKFLOW_ACTION;
import static org.pucar.dristi.config.ServiceConstants.INDIVIDUAL_NOT_FOUND;
import static org.pucar.dristi.config.ServiceConstants.INVALID_ADVOCATE_DETAILS;
import static org.pucar.dristi.config.ServiceConstants.INVALID_ADVOCATE_ID;
import static org.pucar.dristi.config.ServiceConstants.INVALID_COMPLAINANT_DETAILS;
import static org.pucar.dristi.config.ServiceConstants.INVALID_DOCUMENT_DETAILS;
import static org.pucar.dristi.config.ServiceConstants.INVALID_FILESTORE_ID;
import static org.pucar.dristi.config.ServiceConstants.INVALID_LINKEDCASE_ID;
import static org.pucar.dristi.config.ServiceConstants.MDMS_DATA_NOT_FOUND;
import static org.pucar.dristi.config.ServiceConstants.SAVE_DRAFT_CASE_WORKFLOW_ACTION;
import static org.pucar.dristi.config.ServiceConstants.SUBMIT_CASE_WORKFLOW_ACTION;
import static org.pucar.dristi.config.ServiceConstants.VALIDATION_ERR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.util.AdvocateUtil;
import org.pucar.dristi.util.FileStoreUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.AdvocateMapping;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.JoinCaseRequest;
import org.pucar.dristi.web.models.Party;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import net.minidev.json.JSONArray;

@Component
public class CaseRegistrationValidator {

	private IndividualService individualService;

	private CaseRepository repository;


	private MdmsUtil mdmsUtil;

	private FileStoreUtil fileStoreUtil;

	private AdvocateUtil advocateUtil;

	private Configuration config;
	
	@Autowired
	public CaseRegistrationValidator(IndividualService indService, CaseRepository caseRepo,
			 MdmsUtil mdmsUtil, FileStoreUtil fileStoreUtil, AdvocateUtil advocateUtil,
			Configuration config) {
		this.individualService = indService;
		this.repository = caseRepo;
		this.mdmsUtil = mdmsUtil;
		this.fileStoreUtil = fileStoreUtil;
		this.advocateUtil = advocateUtil;
		this.config = config;
		
	}

	/*
	 * To do validation-> 1. Validate MDMS data 2. Fetch court department info from
	 * HRMS 3. Validate artifact Ids
	 */

	public void validateCaseRegistration(CaseRequest caseRequest) throws CustomException {
		CourtCase courtCase = caseRequest.getCases();

		if (ObjectUtils.isEmpty(courtCase.getTenantId()))
			throw new CustomException(VALIDATION_ERR, "tenantId is mandatory for creating case");
		if (ObjectUtils.isEmpty(courtCase.getCaseCategory()))
			throw new CustomException(VALIDATION_ERR, "caseCategory is mandatory for creating case");
		if (ObjectUtils.isEmpty(courtCase.getStatutesAndSections()))
			throw new CustomException(VALIDATION_ERR, "statute and sections is mandatory for creating case");
		if (!(SAVE_DRAFT_CASE_WORKFLOW_ACTION.equalsIgnoreCase(courtCase.getWorkflow().getAction())
				|| DELETE_DRAFT_WORKFLOW_ACTION.equalsIgnoreCase(courtCase.getWorkflow().getAction()))) {
			if (ObjectUtils.isEmpty(courtCase.getLitigants()))
				throw new CustomException(VALIDATION_ERR, "litigants is mandatory for creating case");
		}
		if (ObjectUtils.isEmpty(caseRequest.getRequestInfo().getUserInfo())) {
			throw new CustomException(VALIDATION_ERR, "user info is mandatory for creating case");
		}
	}

	//TODO: Since this method is only called on update and it is checking various things apart from existence, it
	//should be renamed to validateUpdateRequest. Cognitive load on this method is too high. Request to rewrite/refactor
	public Boolean validateApplicationExistence(CaseRequest caseRequest) {
		validateCaseRegistration(caseRequest);
		CourtCase courtCase = caseRequest.getCases();
		RequestInfo requestInfo = caseRequest.getRequestInfo();

		if (ObjectUtils.isEmpty(courtCase.getTenantId()))
			throw new CustomException(VALIDATION_ERR, "tenantId is mandatory for updating case");
		if (!(SUBMIT_CASE_WORKFLOW_ACTION.equalsIgnoreCase(courtCase.getWorkflow().getAction())
				|| SAVE_DRAFT_CASE_WORKFLOW_ACTION.equalsIgnoreCase(courtCase.getWorkflow().getAction())
				|| DELETE_DRAFT_WORKFLOW_ACTION.equalsIgnoreCase(courtCase.getWorkflow().getAction()))) {
			if (ObjectUtils.isEmpty(courtCase.getFilingDate()))
				throw new CustomException(VALIDATION_ERR, "filingDate is mandatory for updating case");
		}

		List<CaseCriteria> existingApplications = repository.getApplications(Collections.singletonList(CaseCriteria
				.builder().filingNumber(courtCase.getFilingNumber()).caseId(String.valueOf(courtCase.getId()))
				.cnrNumber(courtCase.getCnrNumber()).courtCaseNumber(courtCase.getCourtCaseNumber()).build()),
				requestInfo);
		if (existingApplications.isEmpty())
			return false;
		Map<String, Map<String, JSONArray>> mdmsData = mdmsUtil.fetchMdmsData(requestInfo, courtCase.getTenantId(),
				config.getCaseBusinessServiceName(), createMasterDetails());

		if (mdmsData.get(config.getCaseBusinessServiceName()) == null)
			throw new CustomException(MDMS_DATA_NOT_FOUND, "MDMS data does not exist");
		if (!courtCase.getLitigants().isEmpty()) {
			courtCase.getLitigants().forEach(litigant -> {
				if (litigant.getIndividualId() != null) {
					if (!individualService.searchIndividual(requestInfo, litigant.getIndividualId()))
						throw new CustomException(INDIVIDUAL_NOT_FOUND, INVALID_COMPLAINANT_DETAILS);
				} else
					throw new CustomException(INDIVIDUAL_NOT_FOUND, INVALID_COMPLAINANT_DETAILS);
			});
		}
		if (courtCase.getDocuments() != null && !courtCase.getDocuments().isEmpty()) {
			courtCase.getDocuments().forEach(document -> {
				if (document.getFileStore() != null) {
					if (!fileStoreUtil.doesFileExist(courtCase.getTenantId(), document.getFileStore()))
						throw new CustomException(INVALID_FILESTORE_ID, INVALID_DOCUMENT_DETAILS);
				} else
					throw new CustomException(INVALID_FILESTORE_ID, INVALID_DOCUMENT_DETAILS);
			});
		}
		if (courtCase.getRepresentatives() != null && !courtCase.getRepresentatives().isEmpty()) {
			courtCase.getRepresentatives().forEach(rep -> {
				if (rep.getAdvocateId() != null) {
					if (!advocateUtil.doesAdvocateExist(requestInfo, rep.getAdvocateId()))
						throw new CustomException(INVALID_ADVOCATE_ID, INVALID_ADVOCATE_DETAILS);
				} else
					throw new CustomException(INVALID_ADVOCATE_ID, INVALID_ADVOCATE_DETAILS);
			});
		}
		if (courtCase.getLinkedCases() != null && !courtCase.getLinkedCases().isEmpty()) {
			boolean isValidLinkedCase = courtCase.getLinkedCases().stream().allMatch(linkedCase -> existingApplications
					.get(0).getResponseList().stream()
					.anyMatch(existingCase -> existingCase.getLinkedCases().stream()
							.anyMatch(existingLinkedCase -> (linkedCase.getId() != null
									&& linkedCase.getId().equals(existingLinkedCase.getId()))
									|| (linkedCase.getIsActive() != null
											&& linkedCase.getIsActive().equals(existingLinkedCase.getIsActive()))
									|| (linkedCase.getCaseNumber() != null
											&& linkedCase.getCaseNumber().equals(existingLinkedCase.getCaseNumber()))
									|| (linkedCase.getReferenceUri() != null && linkedCase.getReferenceUri()
											.equals(existingLinkedCase.getReferenceUri())))));
			if (!isValidLinkedCase)
				throw new CustomException(INVALID_LINKEDCASE_ID, "Invalid linked case details");
		}

		return true;
	}

	public boolean canLitigantJoinCase(JoinCaseRequest joinCaseRequest) {
		RequestInfo requestInfo = joinCaseRequest.getRequestInfo();
		Party litigant = joinCaseRequest.getLitigant();

		if (litigant.getIndividualId() != null) { // validation for IndividualId for litigant
			if (!individualService.searchIndividual(requestInfo, litigant.getIndividualId())) {
				throw new CustomException(INDIVIDUAL_NOT_FOUND, INVALID_COMPLAINANT_DETAILS);
			}
		} else {
			throw new CustomException(INDIVIDUAL_NOT_FOUND, INVALID_COMPLAINANT_DETAILS);
		}

		if (litigant.getDocuments() != null && !litigant.getDocuments().isEmpty()) {// validation for documents for
																					// litigant
			litigant.getDocuments().forEach(document -> {
				if (document.getFileStore() != null) {
					if (!fileStoreUtil.doesFileExist(litigant.getTenantId(), document.getFileStore()))
						throw new CustomException(INVALID_FILESTORE_ID, INVALID_DOCUMENT_DETAILS);
				} else
					throw new CustomException(INVALID_FILESTORE_ID, INVALID_DOCUMENT_DETAILS);
			});
		}
		return true;

	}

	public boolean canRepresentativeJoinCase(JoinCaseRequest joinCaseRequest) {
		RequestInfo requestInfo = joinCaseRequest.getRequestInfo();
		AdvocateMapping representative = joinCaseRequest.getRepresentative();

		if (representative.getAdvocateId() != null) { // validation for advocateId for representative
			if (!advocateUtil.doesAdvocateExist(requestInfo, representative.getAdvocateId()))
				throw new CustomException(INVALID_ADVOCATE_ID, INVALID_ADVOCATE_DETAILS);
		} else {
			throw new CustomException(INVALID_ADVOCATE_ID, INVALID_ADVOCATE_DETAILS);
		}
		if (representative.getDocuments() != null && !representative.getDocuments().isEmpty()) { // validation for
																									// documents for
																									// representative
			representative.getDocuments().forEach(document -> {
				if (document.getFileStore() != null) {
					if (!fileStoreUtil.doesFileExist(representative.getTenantId(), document.getFileStore()))
						throw new CustomException(INVALID_FILESTORE_ID, INVALID_DOCUMENT_DETAILS);
				} else {
					throw new CustomException(INVALID_FILESTORE_ID, INVALID_DOCUMENT_DETAILS);
				}
			});
		}
		return true;
	}

	private List<String> createMasterDetails() {
		List<String> masterList = new ArrayList<>();
		masterList.add("ComplainantType");
		masterList.add("CaseCategory");
		masterList.add("PaymentMode");
		masterList.add("ResolutionMechanism");

		return masterList;
	}

}