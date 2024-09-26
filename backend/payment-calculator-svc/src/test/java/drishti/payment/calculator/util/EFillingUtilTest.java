package drishti.payment.calculator.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import drishti.payment.calculator.web.models.EFilingParam;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static drishti.payment.calculator.config.ServiceConstants.CASE_MODULE;
import static drishti.payment.calculator.config.ServiceConstants.E_FILLING_MASTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EFillingUtilTest {

    @Mock
    private MdmsUtil mdmsUtil;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EFillingUtil eFillingUtil;

    @BeforeEach
    void setUp() {
        eFillingUtil = new EFillingUtil(mdmsUtil, objectMapper);
    }

    @Test
    void testGetEFillingDefaultData() {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(User.builder().uuid("some-uuid").build());
        String tenantId = "tenant-id";

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonArray.add(jsonObject);

        Map<String, Map<String, JSONArray>> response = new HashMap<>();
        Map<String, JSONArray> caseModuleMap = new HashMap<>();
        caseModuleMap.put(E_FILLING_MASTER, jsonArray);
        response.put(CASE_MODULE, caseModuleMap);

        when(mdmsUtil.fetchMdmsData(requestInfo, tenantId, CASE_MODULE, Collections.singletonList(E_FILLING_MASTER))).thenReturn(response);

        EFilingParam eFilingParam = new EFilingParam();
        when(objectMapper.convertValue(jsonObject, EFilingParam.class)).thenReturn(eFilingParam);

        EFilingParam result = eFillingUtil.getEFillingDefaultData(requestInfo, tenantId);

        assertNotNull(result);
        assertEquals(eFilingParam, result);

        verify(mdmsUtil, times(1)).fetchMdmsData(requestInfo, tenantId, CASE_MODULE, Collections.singletonList(E_FILLING_MASTER));
        verify(objectMapper, times(1)).convertValue(jsonObject, EFilingParam.class);
    }
}
