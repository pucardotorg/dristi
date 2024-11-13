package digit.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import digit.config.Configuration;
import digit.repository.ServiceRequestRepository;
import jakarta.annotation.PostConstruct;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.common.utils.MultiStateInstanceUtil;
import org.egov.tracer.model.CustomException;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import static digit.config.ServiceConstants.*;

@Slf4j
@Service
public class UserService {

    @Autowired
    private Configuration configuration;

    @Autowired
    private ServiceRequestRepository requestRepository;

    public String internalMicroserviceRoleUuid = null;

    @Autowired
    private MultiStateInstanceUtil multiStateInstanceUtil;

    public static final String TENANT_ID_MDC_STRING = "TENANTID";


    @PostConstruct
    void initializeSystemUser(){
        RequestInfo requestInfo = new RequestInfo();
        StringBuilder uri = new StringBuilder();
        uri.append(configuration.getUserHost()).append(configuration.getUserSearchEndpoint()); // URL for user search call
        Map<String, Object> userSearchRequest = new HashMap<>();
        userSearchRequest.put("RequestInfo", requestInfo);
        userSearchRequest.put("tenantId", configuration.getEgovStateTenantId());
        userSearchRequest.put("roleCodes", Collections.singletonList(INTERNALMICROSERVICEROLE_CODE));
        if(multiStateInstanceUtil.getIsEnvironmentCentralInstance()){
            MDC.put(TENANT_ID_MDC_STRING, configuration.getEgovStateTenantId());
        }
        try {
            LinkedHashMap<String, Object> responseMap = (LinkedHashMap<String, Object>) requestRepository.fetchResult(uri, userSearchRequest);
            List<LinkedHashMap<String, Object>> users = (List<LinkedHashMap<String, Object>>) responseMap.get("user");
            if(users.isEmpty()) {
                createInternalMicroserviceUser(requestInfo);
            } else {
                internalMicroserviceRoleUuid = (String) users.get(0).get("uuid");
            }
        }catch (Exception e) {
            throw new CustomException("EG_USER_SEARCH_ERROR", "Service returned null while fetching user");
        }

    }

    private void createInternalMicroserviceUser(RequestInfo requestInfo){
        Map<String, Object> userCreateRequest = new HashMap<>();
        //Creating role with INTERNAL_MICROSERVICE_ROLE
        Role role = Role.builder()
                .name(INTERNALMICROSERVICEROLE_NAME).code(INTERNALMICROSERVICEROLE_CODE)
                .tenantId(configuration.getEgovStateTenantId()).build();
        User user = User.builder().userName(INTERNALMICROSERVICEUSER_USERNAME)
                .name(INTERNALMICROSERVICEUSER_NAME).mobileNumber(INTERNALMICROSERVICEUSER_MOBILENO)
                .type(INTERNALMICROSERVICEUSER_TYPE).tenantId(configuration.getEgovStateTenantId())
                .roles(Collections.singletonList(role)).id(0L).build();

        userCreateRequest.put("RequestInfo", requestInfo);
        userCreateRequest.put("user", user);

        StringBuilder uri = new StringBuilder();
        uri.append(configuration.getUserHost()).append(configuration.getUserCreateEndpoint()); // URL for user create call

        try {
            LinkedHashMap<String, Object> responseMap = (LinkedHashMap<String, Object>) requestRepository.fetchResult(uri, userCreateRequest);
            List<LinkedHashMap<String, Object>> users = (List<LinkedHashMap<String, Object>>) responseMap.get("user");
            internalMicroserviceRoleUuid = (String) users.get(0).get("uuid");
        }catch (Exception e) {
            throw new CustomException("EG_USER_CREATE_ERROR", "Service threw error while creating user");
        }
    }
}
