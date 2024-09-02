package drishti.payment.calculator.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import drishti.payment.calculator.web.models.IPostConfigParams;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static drishti.payment.calculator.config.ServiceConstants.I_POST_MASTER;
import static drishti.payment.calculator.config.ServiceConstants.SUMMON_MODULE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IPostUtilTest {

    @Mock
    private MdmsUtil mdmsUtil;

    @Mock
    private ObjectMapper objectMapper;

    private IPostUtil iPostUtil;

    @BeforeEach
    void setUp() {
        iPostUtil = new IPostUtil(mdmsUtil, objectMapper);
    }

    @Test
    void testGetIPostFeesDefaultData() {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(User.builder().uuid("some-uuid").build());
        String tenantId = "tenant-id";

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonArray.add(jsonObject);

        Map<String, Map<String, JSONArray>> response = new HashMap<>();
        Map<String, JSONArray> summonModuleMap = new HashMap<>();
        summonModuleMap.put(I_POST_MASTER, jsonArray);
        response.put(SUMMON_MODULE, summonModuleMap);

        when(mdmsUtil.fetchMdmsData(requestInfo, tenantId, SUMMON_MODULE, Collections.singletonList(I_POST_MASTER))).thenReturn(response);

        IPostConfigParams iPostConfigParams = new IPostConfigParams();
        when(objectMapper.convertValue(jsonObject, IPostConfigParams.class)).thenReturn(iPostConfigParams);

        IPostConfigParams result = iPostUtil.getIPostFeesDefaultData(requestInfo, tenantId);

        assertNotNull(result);
        assertEquals(iPostConfigParams, result);

        verify(mdmsUtil, times(1)).fetchMdmsData(requestInfo, tenantId, SUMMON_MODULE, Collections.singletonList(I_POST_MASTER));
        verify(objectMapper, times(1)).convertValue(jsonObject, IPostConfigParams.class);
    }
}
