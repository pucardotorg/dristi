package org.pucar.dristi.validator;

import com.jayway.jsonpath.JsonPath;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ApplicationRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.FileStoreUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.util.OrderUtil;
import org.pucar.dristi.web.models.Application;
import org.pucar.dristi.web.models.ApplicationExists;
import org.pucar.dristi.web.models.ApplicationRequest;
import org.pucar.dristi.web.models.OrderExistsRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.INVALID_FILESTORE_ID;
import static org.pucar.dristi.config.ServiceConstants.ORDER_EXCEPTION;

@ExtendWith(MockitoExtension.class)
public class ApplicationValidatorTest {

    @Mock
    private ApplicationRepository repository;
    @Mock
    private CaseUtil caseUtil;
    @Mock
    private OrderUtil orderUtil;

    @Mock
    private Configuration configuration;

    @Mock
    private MdmsUtil mdmsUtil;

    @Mock
    private FileStoreUtil fileStoreUtil;

    private List<ApplicationExists> applicationExistsList;

    @InjectMocks
    private ApplicationValidator validator;

    private ApplicationRequest applicationRequest;
    private Application application;
    private RequestInfo requestInfo;

    @BeforeEach
    public void setUp() {
        requestInfo = new RequestInfo();
        application = new Application();
        applicationRequest = new ApplicationRequest();
        applicationRequest.setRequestInfo(requestInfo);
        applicationRequest.setApplication(application);
    }

    @Test
    public void testValidateApplication_Success() {
        User user = new User();
        List<UUID> onBehalfOf = new ArrayList<>();
        onBehalfOf.add(UUID.randomUUID());
        application.setTenantId("tenantId");
        application.setCreatedDate(123453335l);
        application.setCreatedBy(UUID.randomUUID());
        requestInfo.setUserInfo(user); // Simulating non-empty user info
        application.setOnBehalfOf(onBehalfOf);
        application.setCnrNumber("cnrNumber");
        application.setCaseId("caseId");
        application.setApplicationType("applicationType");
        application.setFilingNumber("filingNumber");
        application.setReferenceId(UUID.randomUUID());
        when(configuration.getApplicationModule()).thenReturn("ApplicationModule");
        when(configuration.getApplicationTypePath()).thenReturn("$.ApplicationType[?(@.code == '{}')]");
        when(caseUtil.fetchCaseDetails(any())).thenReturn(true);

        // Mocking mdmsUtil.fetchMdmsData
        String mdmsData = "{ \"ApplicationType\": [ { \"code\": \"applicationType\" } ] }";
        when(mdmsUtil.fetchMdmsData(any(), any(), any(), any())).thenReturn(mdmsData);

        // Mocking the static JsonPath.read method using MockedStatic
        try (MockedStatic<JsonPath> mockedJsonPath = mockStatic(JsonPath.class)) {
            mockedJsonPath.when(() -> JsonPath.read(mdmsData, "$.ApplicationType[?(@.code == 'applicationType')]"))
                    .thenReturn(List.of(Map.of("code", "applicationType")));
        }
        assertDoesNotThrow(() -> validator.validateApplication(applicationRequest));
    }

    @Test
    public void testValidateApplication_InvalidApplicationType() {
        User user = new User();
        List<UUID> onBehalfOf = new ArrayList<>();
        onBehalfOf.add(UUID.randomUUID());
        application.setTenantId("tenantId");
        application.setCreatedDate(123453335l);
        application.setCreatedBy(UUID.randomUUID());
        requestInfo.setUserInfo(user); // Simulating non-empty user info
        application.setOnBehalfOf(onBehalfOf);
        application.setCnrNumber("cnrNumber");
        application.setCaseId("caseId");
        application.setApplicationType("applicationType");
        application.setFilingNumber("filingNumber");
        application.setReferenceId(UUID.randomUUID());
        when(configuration.getApplicationModule()).thenReturn("ApplicationModule");
        when(configuration.getApplicationTypePath()).thenReturn("$.ApplicationType[?(@.code == '{}')]");
        when(caseUtil.fetchCaseDetails(any())).thenReturn(true);
        when(mdmsUtil.fetchMdmsData(any(), any(), any(), any())).thenReturn("{ \"ApplicationType\": [ { \"code\": \"type\" } ] }");
        assertThrows(CustomException.class,() -> validator.validateApplication(applicationRequest));
    }

    @Test
    public void testValidateApplication_MissingCase() {
        User user = new User();
        application.setTenantId("tenantId");
        application.setCreatedBy(UUID.randomUUID());
        application.setCaseId("CaseId");
        requestInfo.setUserInfo(user);
        application.setCnrNumber("cnrNumber");
        application.setFilingNumber("filingNumber");
        application.setReferenceId(UUID.randomUUID());
        when(caseUtil.fetchCaseDetails(any())).thenReturn(false);
        Exception exception = assertThrows(CustomException.class, () -> validator.validateApplication(applicationRequest));
        assertEquals("case does not exist", exception.getMessage());
    }

    @Test
    public void testValidateApplicationExistence_MissingId() {
        application.setCnrNumber("cnrNumber");
        application.setFilingNumber("filingNumber");
        application.setCaseId("caseId");
        application.setReferenceId(UUID.randomUUID());
        when(caseUtil.fetchCaseDetails(any())).thenReturn(true);

        Exception exception = assertThrows(CustomException.class, () -> validator.validateApplicationExistence(requestInfo, application));
        assertEquals("id is mandatory for updating application", exception.getMessage());
    }

    @Test
    public void testValidateApplicationExistence() {
        application.setCnrNumber("cnrNumber");
        application.setFilingNumber("filingNumber");
        application.setCaseId("caseId");
        application.setId(UUID.randomUUID());
        application.setApplicationType("applicationType");
        application.setReferenceId(UUID.randomUUID());
        when(configuration.getApplicationModule()).thenReturn("ApplicationModule");
        when(configuration.getApplicationTypePath()).thenReturn("$.ApplicationType[?(@.code == '{}')]");
        when(caseUtil.fetchCaseDetails(any())).thenReturn(true);
        // Mocking mdmsUtil.fetchMdmsData
        String mdmsData = "{ \"ApplicationType\": [ { \"code\": \"applicationType\" } ] }";
        when(mdmsUtil.fetchMdmsData(any(), any(), any(), any())).thenReturn(mdmsData);

        // Setup ApplicationExists list for repository response
        ApplicationExists appExists = new ApplicationExists();
        appExists.setExists(true);
        applicationExistsList = new ArrayList<>();
        applicationExistsList.add(appExists);
        when(repository.checkApplicationExists(anyList())).thenReturn(applicationExistsList);
        // Mocking the static JsonPath.read method using MockedStatic
        try (MockedStatic<JsonPath> mockedJsonPath = mockStatic(JsonPath.class)) {
            mockedJsonPath.when(() -> JsonPath.read(mdmsData, "$.ApplicationType[?(@.code == 'applicationType')]"))
                    .thenReturn(List.of(Map.of("code", "applicationType")));
        }

        assertTrue(validator.validateApplicationExistence(requestInfo, application));
    }

    @Test
    public void testValidateApplicationExistence_WithMissingCase_ShouldThrowException() {
        application.setCaseId("caseID");
        application.setId(UUID.randomUUID());
        application.setFilingNumber("file123");
        application.setReferenceId(UUID.randomUUID());
        when(caseUtil.fetchCaseDetails(any())).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateApplicationExistence(requestInfo, application));

        assertEquals("case does not exist", exception.getMessage());
    }

    @Test
    void testCreateOrderExistRequest() {
        // Arrange
        application.setReferenceId(UUID.fromString("f35961ed-c3eb-452d-85e8-cde5f33221ce"));

        // Act
        OrderExistsRequest result = validator.createOrderExistRequest(requestInfo, application);

        // Assert
        assertNotNull(result);
        assertEquals(requestInfo, result.getRequestInfo());
        assertNotNull(result.getOrder());
        assertEquals(1, result.getOrder().size());
        assertEquals(UUID.fromString("f35961ed-c3eb-452d-85e8-cde5f33221ce"), result.getOrder().get(0).getOrderId());
    }

    @Test
    void testValidateOrderDetails_OrderExists() {
        // Arrange
        application.setReferenceId(UUID.fromString("f35961ed-c3eb-452d-85e8-cde5f33221ce"));

        applicationRequest.setRequestInfo(requestInfo);
        applicationRequest.setApplication(application);

        when(orderUtil.fetchOrderDetails(any(OrderExistsRequest.class))).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> validator.validateOrderDetails(applicationRequest));

    }

    @Test
    void testValidateOrderDetails_OrderDoesNotExist() {
        // Arrange
        application.setReferenceId(UUID.fromString("f35961ed-c3eb-452d-85e8-cde5f33221ce"));

        applicationRequest.setRequestInfo(requestInfo);
        applicationRequest.setApplication(application);

        when(orderUtil.fetchOrderDetails(any(OrderExistsRequest.class))).thenReturn(false);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> validator.validateOrderDetails(applicationRequest));
        assertEquals(ORDER_EXCEPTION, exception.getCode());
        assertEquals("Order does not exist", exception.getMessage());

    }

    @Test
    void testValidateOrderDetails_NoReferenceId() {
        // Arrange
        application.setReferenceId(null);

        applicationRequest.setRequestInfo(requestInfo);
        applicationRequest.setApplication(application);

        // Act & Assert
        assertDoesNotThrow(() -> validator.validateOrderDetails(applicationRequest));

    }

    @Test
    public void testValidateOrder_InvalidDocumentFileStore() {
        when(fileStoreUtil.doesFileExist(anyString(), any())).thenReturn(false);
        CustomException exception = assertThrows(CustomException.class, this::invokeValidator);
        assertEquals(INVALID_FILESTORE_ID, exception.getCode());
        assertEquals("Invalid document details", exception.getMessage());
    }

    private void invokeValidator() {
        Document document = new Document();
        document.setFileStore("invalidFileStore");
        application.setDocuments(Collections.singletonList(document));
        application.setTenantId("pg");
        validator.validateApplicationExistence(applicationRequest.getRequestInfo(), application);
    }
}