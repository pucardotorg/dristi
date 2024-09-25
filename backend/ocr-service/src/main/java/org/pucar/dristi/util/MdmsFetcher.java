package org.pucar.dristi.util;

import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.*;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Properties;
import org.pucar.dristi.config.ServiceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;

@Component
public class MdmsFetcher {
    private static final Logger log = LoggerFactory.getLogger(MdmsFetcher.class);
    @Autowired
    private Properties properties;
    @Autowired
    private RestTemplate restTemplate;

    public MdmsFetcher() {
    }

    public JSONArray getMdmsForOCR(String filter) {
        return this.getMdmsForFilter(ServiceConstants.MDMS_MODULE_OCR, ServiceConstants.MDMS_MASTER_OCR, filter);
    }

    public JSONArray getMdmsForFilter(String moduleName, String masterName, String filter) {
        MasterDetail masterDetail = MasterDetail.builder().name(masterName).filter(filter).build();
        ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(moduleName).masterDetails(Arrays.asList(masterDetail)).build();
        MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(this.properties.getStateLevelTenantId()).moduleDetails(Arrays.asList(moduleDetail)).build();
        MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().requestInfo(RequestInfo.builder().build()).mdmsCriteria(mdmsCriteria).build();

        try {
            ResponseEntity<MdmsResponse> response = this.restTemplate.postForEntity(this.properties.getMdmsHost() + this.properties.getMdmsSearchEndpoint(), mdmsCriteriaReq, MdmsResponse.class, new Object[0]);
            return (JSONArray) ((Map) ((MdmsResponse) response.getBody()).getMdmsRes().get(moduleName)).get(masterName);
        } catch (Exception e) {
            log.error(ServiceConstants.MDMS_FETCH_ERROR_MESSAGE, e);
            throw new CustomException(ServiceConstants.MDMS_FETCH_ERROR_CODE, e.getMessage());
        }
    }
}
