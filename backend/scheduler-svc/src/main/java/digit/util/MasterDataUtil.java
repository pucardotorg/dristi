package digit.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.config.ServiceConstants;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MasterDataUtil {

    @Autowired
    private MdmsUtil mdmsUtil;
    @Autowired
    private ServiceConstants serviceConstants;
    @Autowired
    private Configuration config;

    @Cacheable(value = "mdms-cache", key = "#masterName")
    public <T> List<T> getDataFromMDMS(Class<T> clazz, String masterName ,String module) {
        log.info("operation = getDataFromMDMS, result = IN_PROGRESS");
        RequestInfo requestInfo = new RequestInfo();
        Map<String, Map<String, JSONArray>> defaultHearingsData =
                mdmsUtil.fetchMdmsData(requestInfo, config.getEgovStateTenantId(),
                        module,
                        Collections.singletonList(masterName));
        JSONArray jsonArray = defaultHearingsData.get(module).get(masterName);
        List<T> result = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (Object obj : jsonArray) {
            T clazzObj = objectMapper.convertValue(obj, clazz);
            result.add(clazzObj);
        }
        log.info("operation = getDataFromMDMS, result = SUCCESS");
        return result;
    }
}
