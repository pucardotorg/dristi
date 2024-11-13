package org.egov.web.notification.mail.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.mdms.model.*;
import org.egov.tracer.model.CustomException;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;


import org.egov.web.notification.mail.config.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.web.notification.mail.utils.Constants.ERROR_WHILE_FETCHING_FROM_MDMS;


@Slf4j
@Component
public class MdmsUtil {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ApplicationConfiguration configs;

    public Map<String, Map<String, JSONArray>> fetchMdmsData(RequestInfo requestInfo, String tenantId,
                                                             String moduleName, HashMap<String, String> masterNameList) {
        StringBuilder uri = new StringBuilder();
        uri.append(configs.getMdmsHost()).append(configs.getMdmsEndPoint());
        MdmsCriteriaReq mdmsCriteriaReq = getMdmsRequest(requestInfo, tenantId, moduleName, masterNameList);
        log.info("MDMS Criteria :: {}",mdmsCriteriaReq);
        Object response;
        MdmsResponse mdmsResponse;
        try {
            response = restTemplate.postForObject(uri.toString(), mdmsCriteriaReq, Map.class);
            mdmsResponse = mapper.convertValue(response, MdmsResponse.class);
        }
        catch (CustomException e) {
            log.error("Custom Exception occurred in MDMS Utility :: {}", e.toString());
            throw e;
        }
        catch (Exception e) {
            log.error(ERROR_WHILE_FETCHING_FROM_MDMS, e);
            throw new CustomException(ERROR_WHILE_FETCHING_FROM_MDMS,e.getMessage());
        }

        return mdmsResponse.getMdmsRes();
    }

    private MdmsCriteriaReq getMdmsRequest(RequestInfo requestInfo, String tenantId, String moduleName,
                                           HashMap<String, String> masterNameList) {
        List<MasterDetail> masterDetailList = new ArrayList<>();
        for(Map.Entry<String, String> entrySet : masterNameList.entrySet()) {
            MasterDetail masterDetail = new MasterDetail();
            masterDetail.setName(entrySet.getKey());
            masterDetail.setFilter(entrySet.getValue());
            masterDetailList.add(masterDetail);
        }

        ModuleDetail moduleDetail = new ModuleDetail();
        moduleDetail.setMasterDetails(masterDetailList);
        moduleDetail.setModuleName(moduleName);
        List<ModuleDetail> moduleDetailList = new ArrayList<>();
        moduleDetailList.add(moduleDetail);

        MdmsCriteria mdmsCriteria = new MdmsCriteria();
        mdmsCriteria.setTenantId(tenantId.split("\\.")[0]);
        mdmsCriteria.setModuleDetails(moduleDetailList);

        MdmsCriteriaReq mdmsCriteriaReq = new MdmsCriteriaReq();
        mdmsCriteriaReq.setMdmsCriteria(mdmsCriteria);
        mdmsCriteriaReq.setRequestInfo(requestInfo);

        return mdmsCriteriaReq;
    }
}
