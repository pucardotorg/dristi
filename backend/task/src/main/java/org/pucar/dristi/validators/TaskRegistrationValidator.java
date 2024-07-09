package org.pucar.dristi.validators;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.TaskRepository;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.util.OrderUtil;
import org.pucar.dristi.web.models.Task;
import org.pucar.dristi.web.models.TaskExists;
import org.pucar.dristi.web.models.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import static org.pucar.dristi.config.ServiceConstants.CREATE_TASK_ERR;
import static org.pucar.dristi.config.ServiceConstants.UPDATE_TASK_ERR;

@Component
public class TaskRegistrationValidator {

    private final TaskRepository repository;
    private final Configuration config;
    private final OrderUtil orderUtil;

    @Autowired
    public TaskRegistrationValidator(TaskRepository repository, MdmsUtil mdmsUtil, Configuration config, OrderUtil orderUtil) {
        this.repository = repository;
        this.config = config;
        this.orderUtil = orderUtil;
    }



    public void validateTaskRegistration(TaskRequest taskRequest) throws CustomException {
        Task task = taskRequest.getTask();

        if (ObjectUtils.isEmpty(task.getTenantId()))
            throw new CustomException(CREATE_TASK_ERR, "tenantId is mandatory for creating task");
        if (ObjectUtils.isEmpty(taskRequest.getRequestInfo().getUserInfo())) {
            throw new CustomException(CREATE_TASK_ERR, "User info is mandatory for creating task");
        }
        if (ObjectUtils.isEmpty(taskRequest.getTask().getTaskType())) {
            throw new CustomException(CREATE_TASK_ERR, "Task type is mandatory for creating task");
        }
        if (ObjectUtils.isEmpty(taskRequest.getTask().getCreatedDate())) {
            throw new CustomException(CREATE_TASK_ERR, "CreatedDate mandatory for creating task");
        }
        if (!orderUtil.fetchOrderDetails(taskRequest.getRequestInfo(),task.getOrderId())) {
            throw new CustomException(CREATE_TASK_ERR, "Invalid order ID");
        }
    }

    public Boolean validateApplicationExistence(Task task, RequestInfo requestInfo) {

        if (ObjectUtils.isEmpty(task.getTenantId()))
            throw new CustomException(UPDATE_TASK_ERR, "tenantId is mandatory for updating task");
        if (ObjectUtils.isEmpty(requestInfo.getUserInfo())) {
            throw new CustomException(UPDATE_TASK_ERR, "user info is mandatory for creating task");
        }
        TaskExists taskExists = new TaskExists();
        taskExists.setFilingNumber(task.getFilingNumber());
        taskExists.setCnrNumber(task.getCnrNumber());
        taskExists.setTaskId(task.getId());

        return repository.checkTaskExists(taskExists).getExists();
    }

}