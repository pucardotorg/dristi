package org.pucar.dristi.validators;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.TaskRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.OrderUtil;
import org.pucar.dristi.web.models.Task;
import org.pucar.dristi.web.models.TaskCriteria;
import org.pucar.dristi.web.models.TaskExists;
import org.pucar.dristi.web.models.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class TaskRegistrationValidator {

    private final TaskRepository repository;
    private final OrderUtil orderUtil;
    private final CaseUtil caseUtil;

    @Autowired
    public TaskRegistrationValidator(TaskRepository repository, OrderUtil orderUtil, CaseUtil caseUtil) {
        this.repository = repository;
        this.orderUtil = orderUtil;
        this.caseUtil = caseUtil;
    }


    public void validateTaskRegistration(TaskRequest taskRequest) throws CustomException {
        Task task = taskRequest.getTask();
        RequestInfo requestInfo = taskRequest.getRequestInfo();

        if (ObjectUtils.isEmpty(requestInfo.getUserInfo())) {
            throw new CustomException(CREATE_TASK_ERR, "User info is mandatory for creating task");
        }
        List<Role> roles = requestInfo.getUserInfo().getRoles();

        boolean isPendingTaskRole = false;
        for (Role role : roles) {
            isPendingTaskRole = role.getCode().equalsIgnoreCase(PENDING_TASK_CREATOR);
        }

        if (PENDING_TASK.equalsIgnoreCase(task.getTaskType())) {

            JsonNode caseDetails = caseUtil.searchCaseDetails(requestInfo, task.getTenantId(), task.getCnrNumber(), task.getFilingNumber(), null);
            if (caseDetails.isEmpty()) {
                log.error("user is trying to create task which he is not associated, userInfo:{}", requestInfo.getUserInfo());
                throw new CustomException(CREATE_TASK_ERR, "you are not allowed to create task");

            }

        } else {
            if (isPendingTaskRole) {
                throw new CustomException(CREATE_TASK_ERR, "you are not allowed to create task");
            }
            if (task.getOrderId() == null) {
                throw new CustomException(CREATE_TASK_ERR, "Order ID cannot be null");
            }
            if (!orderUtil.fetchOrderDetails(requestInfo, task.getOrderId())) {
                throw new CustomException(CREATE_TASK_ERR, "Invalid order ID");
            }
        }
    }

    public Boolean validateApplicationExistence(Task task, RequestInfo requestInfo) {

        if (ObjectUtils.isEmpty(requestInfo.getUserInfo())) {
            throw new CustomException(UPDATE_TASK_ERR, "user info is mandatory for creating task");
        }
        TaskExists taskExists = new TaskExists();
        taskExists.setFilingNumber(task.getFilingNumber());
        taskExists.setCnrNumber(task.getCnrNumber());
        taskExists.setTaskId(task.getId());

        return repository.checkTaskExists(taskExists).getExists();
    }

    public Task validateApplicationUploadDocumentExistence(Task task, RequestInfo requestInfo) {

        if (ObjectUtils.isEmpty(requestInfo.getUserInfo())) {
            throw new CustomException(UPLOAD_TASK_DOCUMENT_ERROR, "user info is mandatory for creating task");
        }

        TaskCriteria taskCriteria = TaskCriteria.builder()
                .id(String.valueOf(task.getId()))
                .cnrNumber(task.getCnrNumber())
                .tenantId(task.getTenantId())
                .taskNumber(task.getTaskNumber()).build();

        List<Task> tasks = repository.getTasks(taskCriteria, null);
        if (tasks == null) {
            throw new CustomException(UPLOAD_TASK_DOCUMENT_ERROR, "Tasks list is null");
        }

        return tasks.stream()
                .findFirst()
                .map(existingTask -> {
                    existingTask.setDocuments(task.getDocuments());
                    return existingTask;
                })
                .orElseThrow(() -> new CustomException(UPLOAD_TASK_DOCUMENT_ERROR, "No task found for the given criteria"));

    }
}