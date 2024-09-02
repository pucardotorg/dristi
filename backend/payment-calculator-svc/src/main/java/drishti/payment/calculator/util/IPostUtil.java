package drishti.payment.calculator.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import drishti.payment.calculator.web.models.IPostConfigParams;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

import static drishti.payment.calculator.config.ServiceConstants.I_POST_MASTER;
import static drishti.payment.calculator.config.ServiceConstants.SUMMON_MODULE;

@Component
public class IPostUtil {
    private final MdmsUtil mdmsUtil;

    private final ObjectMapper objectMapper;

    @Autowired
    public IPostUtil(MdmsUtil mdmsUtil, ObjectMapper objectMapper) {
        this.mdmsUtil = mdmsUtil;
        this.objectMapper = objectMapper;
    }

    public IPostConfigParams getIPostFeesDefaultData(RequestInfo requestInfo, String tenantId) {


        Map<String, Map<String, JSONArray>> response = mdmsUtil.fetchMdmsData(requestInfo, tenantId, SUMMON_MODULE, Collections.singletonList(I_POST_MASTER));
        JSONArray array = response.get(SUMMON_MODULE).get(I_POST_MASTER);
        Object object = array.get(0);

        return objectMapper.convertValue(object, IPostConfigParams.class);
        //todo :add other methods
    }
}
