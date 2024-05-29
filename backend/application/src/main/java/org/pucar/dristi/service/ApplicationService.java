package org.pucar.dristi.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.ApplicationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.ApplicationRepository;
import org.pucar.dristi.validator.ApplicationValidator;
import org.pucar.dristi.web.models.Application;
import org.pucar.dristi.web.models.ApplicationExists;
import org.pucar.dristi.web.models.ApplicationRequest;
import org.pucar.dristi.web.models.RequestInfoBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.*;

@Service
@Slf4j
public class ApplicationService {
    @Autowired
    private ApplicationValidator validator;

    @Autowired
    private ApplicationEnrichment enrichmentUtil;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private Configuration config;
    @Autowired
    private Producer producer;

    public Application createApplication(ApplicationRequest body) {
        try {
            validator.validateApplication(body);
            enrichmentUtil.enrichApplication(body);
            workflowService.updateWorkflowStatus(body);
            producer.push(config.getApplicationCreateTopic(), body);
            return body.getApplication();
        } catch (Exception e) {
            log.error("Error occurred while creating application {}", e.getMessage());
            throw new CustomException(CREATE_APPLICATION_ERR, e.getMessage());
        }
    }

    public Application updateApplication(ApplicationRequest applicationRequest) {
        try {
                Application application = applicationRequest.getApplication();
                Application existingApplication;
                try {
                    existingApplication = validator.validateApplicationExistence(applicationRequest.getRequestInfo(),application);
                } catch (Exception e) {
                    log.error("Error validating existing application {}", e.getMessage());
                    throw new CustomException(VALIDATION_ERR,"Error validating existing application: "+ e.getMessage());
                }
                existingApplication.setWorkflow(application.getWorkflow());// TODO check

            // Enrich application upon update
            enrichmentUtil.enrichApplicationUponUpdate(applicationRequest);

//            workflowService.updateWorkflowStatus(applicationRequest);// TODO check

            producer.push(config.getApplicationUpdateTopic(), applicationRequest);

            return applicationRequest.getApplication();

        } catch (CustomException e){
            log.error("Custom Exception occurred while updating application {}", e.getMessage());
            throw e;
        } catch (Exception e){
            log.error("Error occurred while updating application {}", e.getMessage());
            throw new CustomException(UPDATE_APPLICATION_ERR,"Error occurred while updating application: " + e.getMessage());
        }
    }

        public List<Application> searchApplications (String id, String filingNumber, String cnrNumber, String tenantId, String status, Integer limit, Integer offset, String sortBy, RequestInfoBody requestInfoBody){
            try {
                // Fetch applications from database according to the given search params
                List<Application> applicationList = applicationRepository.getApplications(id, filingNumber, cnrNumber, tenantId, status, limit, offset);
                log.info("No. of applications :: {}", applicationList.size());
                // If no applications are found, return an empty list
                if (CollectionUtils.isEmpty(applicationList))
                    return new ArrayList<>();
                applicationList.forEach(application -> application.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(requestInfoBody.getRequestInfo(), requestInfoBody.getTenantId(), application.getApplicationNumber()))));
                return applicationList;
            } catch (Exception e) {
                log.error("Error while fetching to search results {}", e.getMessage());
                throw new CustomException(APPLICATION_SEARCH_ERR, e.getMessage());
            }
        }

//        public ApplicationExists getExistingApplications (ApplicationExists applicationExists) {
//            try {
//                // Fetch applications from database according to the given search criteria
//                List<CourtCase> courtCases = caseRepository.getApplications(caseSearchRequests.getCriteria());
//                log.info("Court Case Applications Size :: {}", courtCases.size());
//
//                List<CaseExists> caseExistsList = new ArrayList<>();
//
//                for (CaseCriteria caseCriteria : caseSearchRequests.getCriteria()) {
//                    boolean notExists = courtCases.stream().filter(c -> c.getFilingNumber().equalsIgnoreCase(caseCriteria.getFilingNumber())
//                            && c.getCaseNumber().equalsIgnoreCase(caseCriteria.getCnrNumber())).toList().isEmpty();
//                    CaseExists caseExists = new CaseExists(caseCriteria.getCourtCaseNumber(), caseCriteria.getCnrNumber(), caseCriteria.getFilingNumber(), !notExists);
//                    caseExistsList.add(caseExists);
//                }
//
//                // If no applications are found matching the given criteria, return an empty list
//                if (CollectionUtils.isEmpty(courtCases))
//                    return new ArrayList<>();
//                courtCases.forEach(cases -> cases.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(caseSearchRequests.getRequestInfo(), cases.getTenantId(), cases.getCaseNumber()))));
//                return caseExistsList;
//            } catch (CustomException e) {
//                log.error("Custom Exception occurred while searching");
//                throw e;
//            } catch (Exception e) {
//                log.error("Error while fetching to search results");
//                throw new CustomException(CASE_EXIST_ERR, e.getMessage());
//            }
//        }
    }
