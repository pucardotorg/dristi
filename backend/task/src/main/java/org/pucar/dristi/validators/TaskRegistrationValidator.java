package org.pucar.dristi.validators;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.TaskRepository;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.util.OrderUtil;
import org.pucar.dristi.web.models.Task;
import org.pucar.dristi.web.models.TaskCriteria;
import org.pucar.dristi.web.models.TaskExists;
import org.pucar.dristi.web.models.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
public class TaskRegistrationValidator {

    private final TaskRepository repository;
    private final OrderUtil orderUtil;

    @Autowired
    public TaskRegistrationValidator(TaskRepository repository, OrderUtil orderUtil) {
        this.repository = repository;
        this.orderUtil = orderUtil;
    }



    public void validateTaskRegistration(TaskRequest taskRequest) throws CustomException {
        Task task = taskRequest.getTask();

        if (ObjectUtils.isEmpty(taskRequest.getRequestInfo().getUserInfo())) {
            throw new CustomException(CREATE_TASK_ERR, "User info is mandatory for creating task");
        }
        if (!orderUtil.fetchOrderDetails(taskRequest.getRequestInfo(),task.getOrderId())) {
            throw new CustomException(CREATE_TASK_ERR, "Invalid order ID");
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