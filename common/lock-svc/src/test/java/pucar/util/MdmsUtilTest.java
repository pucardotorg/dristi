package pucar.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.MdmsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;
import pucar.config.Configuration;

public class MdmsUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private Configuration configs;

    @InjectMocks
    private MdmsUtil mdmsUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchMdmsData_Success() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenantId";
        String moduleName = "moduleName";
        List<String> masterNameList = Arrays.asList("master1", "master2");

        Map<String, Map<String, JSONArray>> mockMdmsRes = new HashMap<>();
        mockMdmsRes.put("moduleName", new HashMap<>());

        MdmsResponse mockMdmsResponse = new MdmsResponse();
        mockMdmsResponse.setMdmsRes(mockMdmsRes);

        doReturn("http://mdms-host").when(configs).getMdmsHost();
        doReturn("/mdms-endpoint").when(configs).getMdmsEndPoint();
        doReturn(new HashMap<>()).when(restTemplate).postForObject(any(String.class), any(MdmsCriteriaReq.class), any(Class.class));
        doReturn(mockMdmsResponse).when(mapper).convertValue(any(), any(Class.class));

        // Act
        Map<String, Map<String, JSONArray>> result = mdmsUtil.fetchMdmsData(requestInfo, tenantId, moduleName, masterNameList);

        // Assert
        assertNotNull(result);
        assertEquals(mockMdmsRes, result);
    }

    @Test
    public void testFetchMdmsData_Exception() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenantId";
        String moduleName = "moduleName";
        List<String> masterNameList = Arrays.asList("master1", "master2");

        doReturn("http://mdms-host").when(configs).getMdmsHost();
        doReturn("/mdms-endpoint").when(configs).getMdmsEndPoint();
        doThrow(new RuntimeException("Error")).when(restTemplate).postForObject(any(String.class), any(MdmsCriteriaReq.class), any(Class.class));

        // Act
        Map<String, Map<String, JSONArray>> result = mdmsUtil.fetchMdmsData(requestInfo, tenantId, moduleName, masterNameList);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetMdmsRequest() throws Exception {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenantId";
        String moduleName = "moduleName";
        List<String> masterNameList = Arrays.asList("master1", "master2");

        // Use reflection to access the private method
        Method method = MdmsUtil.class.getDeclaredMethod("getMdmsRequest", RequestInfo.class, String.class, String.class, List.class);
        method.setAccessible(true);

        // Act
        MdmsCriteriaReq result = (MdmsCriteriaReq) method.invoke(mdmsUtil, requestInfo, tenantId, moduleName, masterNameList);

        // Assert
        assertNotNull(result);
        assertEquals(requestInfo, result.getRequestInfo());
        assertEquals(tenantId.split("\\.")[0], result.getMdmsCriteria().getTenantId());
        assertEquals(moduleName, result.getMdmsCriteria().getModuleDetails().get(0).getModuleName());
        assertEquals(masterNameList.size(), result.getMdmsCriteria().getModuleDetails().get(0).getMasterDetails().size());
    }
}