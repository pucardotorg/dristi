package digit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.repository.ServiceRequestRepository;
import digit.web.models.CaseDiary;
import digit.web.models.CaseDiaryRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.User;
import org.egov.common.contract.workflow.ProcessInstance;
import org.egov.common.contract.workflow.ProcessInstanceRequest;
import org.egov.common.contract.workflow.ProcessInstanceResponse;
import org.egov.common.contract.workflow.State;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static digit.config.ServiceConstants.WORKFLOW_SERVICE_EXCEPTION;

@Component
@Slf4j
public class WorkflowService {

    private ObjectMapper mapper;

    private ServiceRequestRepository repository;

    private Configuration config;

    @Autowired
    public WorkflowService(ObjectMapper mapper, ServiceRequestRepository repository, Configuration config) {
        this.mapper = mapper;
        this.repository = repository;
        this.config = config;
    }

    public void updateWorkflowStatus(CaseDiaryRequest caseDiaryRequest) {
            try {
                ProcessInstance processInstance = getProcessInstance(caseDiaryRequest.getDiary());
                ProcessInstanceRequest workflowRequest = new ProcessInstanceRequest(caseDiaryRequest.getRequestInfo(), Collections.singletonList(processInstance));
                log.info("ProcessInstance Request :: {}", workflowRequest);
                String state=callWorkFlow(workflowRequest).getState();
                log.info("Workflow State for diary for Judge :: {} for date :: {} and state :: {}",
                        caseDiaryRequest.getDiary().getJudgeId(), caseDiaryRequest.getDiary().getDate(), state);
                caseDiaryRequest.getDiary().setStatus(state);
            } catch(CustomException e){
                throw e;
            } catch (Exception e) {
                log.error("Error updating workflow status :: {}", e.toString());
                throw new CustomException(WORKFLOW_SERVICE_EXCEPTION,"Error updating workflow status: "+e.getMessage());
            }
    }
    public State callWorkFlow(ProcessInstanceRequest workflowReq) {
        try {
            StringBuilder url = new StringBuilder(config.getWfHost().concat(config.getWfTransitionPath()));
            Object optional = repository.fetchResult(url, workflowReq);
            log.info("Workflow Response :: {}", optional);
            ProcessInstanceResponse response = mapper.convertValue(optional, ProcessInstanceResponse.class);
            return response.getProcessInstances().get(0).getState();
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error calling workflow :: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION,e.getMessage());
        }
    }

    public ProcessInstance getProcessInstance(CaseDiary diary) {
        try {
            Workflow workflow = diary.getWorkflow();
            ProcessInstance processInstance = new ProcessInstance();
            if (StringUtils.equals(diary.getDiaryType().toLowerCase(), "adiary")) {
                processInstance.setBusinessId(diary.getJudgeId() + "-" + diary.getDiaryDate());
            } else if (StringUtils.equals(diary.getDiaryType().toLowerCase(), "bdiary")) {
                // TODO check with atul about courtRoom issue
                processInstance.setBusinessId(String.valueOf(diary.getCaseNumber()));
            }
            processInstance.setAction(workflow.getAction());
            processInstance.setModuleName(config.getCaseDiaryBusinessName());
            processInstance.setTenantId(diary.getTenantId());
            processInstance.setBusinessService(config.getCaseDiaryBusinessServiceName());
            processInstance.setDocuments(workflow.getDocuments());
            processInstance.setComment(workflow.getComments());
            if (!CollectionUtils.isEmpty(workflow.getAssignes())) {
                List<User> users = new ArrayList<>();
                workflow.getAssignes().forEach(uuid -> {
                    User user = new User();
                    user.setUuid(uuid);
                    users.add(user);
                });
                processInstance.setAssignes(users);
            }
            return processInstance;
        } catch (Exception e) {
            log.error("Error getting process instance for CASE :: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION,e.getMessage());
        }
    }
}
 