package org.pucar.dristi.validators;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.TaskRepository;
import org.pucar.dristi.service.TaskService;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.util.OrderUtil;
import org.pucar.dristi.web.models.Task;
import org.pucar.dristi.web.models.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.CREATE_TASK_ERR;
import static org.pucar.dristi.config.ServiceConstants.UPDATE_TASK_ERR;

@Component
public class TaskRegistrationValidator {

    @Autowired
    private TaskRepository repository;

    private TaskService taskService;

    @Autowired
    private MdmsUtil mdmsUtil;

    @Autowired
    private Configuration config;

    @Autowired
    private CaseUtil caseUtil;

    @Autowired
    private OrderUtil orderUtil;


    @Autowired
    public void setCaseService(@Lazy TaskService taskService) {
        this.taskService = taskService;
    }

    public void validateCaseRegistration(TaskRequest taskRequest) throws CustomException {
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
        // To be uncommented later       
//        if (!orderUtil.fetchOrderDetails(taskRequest.getRequestInfo(),task.getOrderId())) {
//            throw new CustomException(CREATE_TASK_ERR, "Invalid order ID");
//        }
        if (task.getValidate() && !caseUtil.fetchCaseDetails(taskRequest.getRequestInfo(),task.getCnrNumber(),task.getFilingNumber())) {
            throw new CustomException(CREATE_TASK_ERR, "Invalid case details");
        }
    }

    public Boolean validateApplicationExistence(Task task, RequestInfo requestInfo) {

        if (ObjectUtils.isEmpty(task.getTenantId()))
            throw new CustomException(UPDATE_TASK_ERR, "tenantId is mandatory for updating task");
        if (ObjectUtils.isEmpty(requestInfo.getUserInfo())) {
            throw new CustomException(UPDATE_TASK_ERR, "user info is mandatory for creating task");
        }
        List<Task> existingApplications = repository.getApplications(task.getId().toString(), task.getTenantId(), task.getStatus(),task.getOrderId(),task.getCnrNumber());
        return !existingApplications.isEmpty();
    }

}