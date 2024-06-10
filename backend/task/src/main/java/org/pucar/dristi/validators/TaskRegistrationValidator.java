package org.pucar.dristi.validators;

import ch.qos.logback.core.rolling.helper.FileStoreUtil;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.TaskRepository;
import org.pucar.dristi.service.TaskService;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.Task;
import org.pucar.dristi.web.models.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.CREATE_TASK_ERR;

@Component
public class TaskRegistrationValidator {

    @Autowired
    private TaskRepository repository;

    private TaskService taskService;

    @Autowired
    private MdmsUtil mdmsUtil;

    @Autowired
    private FileStoreUtil fileStoreUtil;

    @Autowired
    private Configuration config;

    /*  To do validation->
        1. Validate MDMS data
        2. Fetch court department info from HRMS
        3. Validate artifact Ids
    */
    @Autowired
    public void setCaseService(@Lazy TaskService taskService) {
        this.taskService = taskService;
    }

    public void validateCaseRegistration(TaskRequest taskRequest) throws CustomException {
        Task task = taskRequest.getTask();

        if (ObjectUtils.isEmpty(task.getTenantId()))
            throw new CustomException(CREATE_TASK_ERR, "tenantId is mandatory for creating case");
        if (ObjectUtils.isEmpty(taskRequest.getRequestInfo().getUserInfo())) {
            throw new CustomException(CREATE_TASK_ERR, "user info is mandatory for creating case");
        }
    }

//    public Boolean validateApplicationExistence(CourtCase courtCase, RequestInfo requestInfo) {
//
//        if (ObjectUtils.isEmpty(courtCase.getTenantId()))
//            throw new CustomException(UPDATE_CASE_ERR, "tenantId is mandatory for updating case");
//        if (ObjectUtils.isEmpty(courtCase.getFilingDate()))
//            throw new CustomException(UPDATE_CASE_ERR, "filingDate is mandatory for updating case");
//
//        List<CaseCriteria> existingApplications = repository.getApplications(Collections.singletonList(CaseCriteria.builder().filingNumber(courtCase.getFilingNumber()).caseId(String.valueOf(courtCase.getId()))
//                .cnrNumber(courtCase.getCnrNumber()).courtCaseNumber(courtCase.getCourCaseNumber()).build()));
//        if (existingApplications.isEmpty())
//            return false;
//        Map<String, Map<String, JSONArray>> mdmsData = mdmsUtil.fetchMdmsData(requestInfo, courtCase.getTenantId(), config.getCaseBusinessServiceName(), createMasterDetails());
//
//        if (mdmsData.get(config.getCaseBusinessServiceName()) == null)
//            throw new CustomException(MDMS_DATA_NOT_FOUND, "MDMS data does not exist");
//        if (!courtCase.getLitigants().isEmpty()) {
//            courtCase.getLitigants().forEach(litigant -> {
//                if (!individualService.searchIndividual(requestInfo, litigant.getIndividualId()))
//                    throw new CustomException(INDIVIDUAL_NOT_FOUND, "Invalid complainant details");
//            });
//        }
//        if (courtCase.getDocuments() != null && !courtCase.getDocuments().isEmpty()) {
//            courtCase.getDocuments().forEach(document -> {
//                if (!fileStoreUtil.fileStore(courtCase.getTenantId(), document.getFileStore()))
//                    throw new CustomException(INVALID_FILESTORE_ID, "Invalid document details");
//            });
//        }
//        if (courtCase.getRepresentatives() != null && !courtCase.getRepresentatives().isEmpty()) {
//            courtCase.getRepresentatives().forEach(rep -> {
//                if (!advocateUtil.fetchAdvocateDetails(requestInfo, rep.getAdvocateId()))
//                    throw new CustomException(INVALID_ADVOCATE_ID, "Invalid advocate details");
//            });
//        }
//        if (courtCase.getLinkedCases() != null && !courtCase.getLinkedCases().isEmpty()) {
//            boolean isValidLinkedCase = courtCase.getLinkedCases().stream().allMatch(linkedCase ->
//                    existingApplications.get(0).getResponseList().stream().anyMatch(existingCase ->
//                            existingCase.getLinkedCases().stream().anyMatch(existingLinkedCase ->
//                                    (linkedCase.getId() != null && linkedCase.getId().equals(existingLinkedCase.getId())) ||
//                                            (linkedCase.getIsActive() != null && linkedCase.getIsActive() == existingLinkedCase.getIsActive()) ||
//                                            (linkedCase.getCaseNumber() != null &&  linkedCase.getCaseNumber().equals(existingLinkedCase.getCaseNumber())) ||
//                                            (linkedCase.getReferenceUri() != null && linkedCase.getReferenceUri().equals(existingLinkedCase.getReferenceUri()))
//                            )
//                    )
//            );
//            if (!isValidLinkedCase)
//                throw new CustomException(INVALID_LINKEDCASE_ID, "Invalid linked case details");
//        }
//        return true;
//    }

    private List<String> createMasterDetails() {
        List<String> masterList = new ArrayList<>();
        masterList.add("ComplainantType");
        masterList.add("CaseCategory");
        masterList.add("PaymentMode");
        masterList.add("ResolutionMechanism");

        return masterList;
    }


}