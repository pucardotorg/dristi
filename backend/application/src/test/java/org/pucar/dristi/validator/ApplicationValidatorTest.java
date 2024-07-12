package org.pucar.dristi.validator;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.repository.ApplicationRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.OrderUtil;
import org.pucar.dristi.web.models.Application;
import org.pucar.dristi.web.models.ApplicationExists;
import org.pucar.dristi.web.models.ApplicationRequest;
import org.pucar.dristi.web.models.OrderExistsRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.ORDER_EXCEPTION;

@ExtendWith(MockitoExtension.class)
public class ApplicationValidatorTest {

    @Mock
    private ApplicationRepository repository;
    @Mock
    private CaseUtil caseUtil;
    @Mock
    private OrderUtil orderUtil;
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
        application.setCreatedDate("2024-05-01");
        application.setCreatedBy(UUID.randomUUID());
        requestInfo.setUserInfo(user); // Simulating non-empty user info
        application.setOnBehalfOf(onBehalfOf);
        application.setCnrNumber("cnrNumber");
        application.setCaseId("caseId");
        application.setApplicationType("applicationType");
        application.setFilingNumber("filingNumber");
        application.setReferenceId(UUID.randomUUID());
        when(caseUtil.fetchCaseDetails(any())).thenReturn(true);

        assertDoesNotThrow(() -> validator.validateApplication(applicationRequest));
    }

    @Test
    public void testValidateApplication_MissingTenantId() {
        application.setCaseId("caseId");
        when(caseUtil.fetchCaseDetails(any())).thenReturn(true);

        Exception exception = assertThrows(CustomException.class, () -> validator.validateApplication(applicationRequest));
        assertEquals("tenantId is mandatory for creating application", exception.getMessage());
    }

    @Test
    public void testValidateApplication_MissingApplicationType() {
        application.setTenantId("tenantId");
        application.setCaseId("caseId");
        when(caseUtil.fetchCaseDetails(any())).thenReturn(true);

        Exception exception = assertThrows(CustomException.class, () -> validator.validateApplication(applicationRequest));
        assertEquals("applicationType is mandatory for creating application", exception.getMessage());
    }

    @Test
    public void testValidateApplication_MissingCaseId() {
        application.setTenantId("tenantId");
        application.setCreatedDate("2024-05-01");
        application.setCreatedBy(UUID.randomUUID());

        Exception exception = assertThrows(CustomException.class, () -> validator.validateApplication(applicationRequest));
        assertEquals("caseId is mandatory for creating application", exception.getMessage());
    }

    @Test
    public void testValidateApplication_MissingCase() {
        User user = new User();
        application.setTenantId("tenantId");
        application.setCreatedDate("2024-05-01");
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
    public void testValidateApplicationExistence_Success() {
        application.setId(UUID.randomUUID());
        application.setCnrNumber("cnrNumber");
        application.setCaseId("caseId");
        application.setTenantId("tId");
        application.setApplicationType("ApplicationType");
        application.setFilingNumber("filingNumber");
        application.setStatus("status1");
        application.setReferenceId(UUID.randomUUID());
        List<Application> existingApplications = new ArrayList<>();
        existingApplications.add(application);
        when(caseUtil.fetchCaseDetails(any())).thenReturn(true);


        ApplicationExists applicationExists = new ApplicationExists();
        applicationExists.setExists(true);
        applicationExists.setFilingNumber(application.getFilingNumber());
        applicationExists.setCnrNumber(application.getCnrNumber());
        applicationExists.setApplicationNumber(application.getApplicationNumber());

        List<ApplicationExists> applicationExistsList = new ArrayList<>();
        applicationExistsList.add(applicationExists);

        when(repository.checkApplicationExists(anyList())).thenReturn(applicationExistsList);

        Boolean result = validator.validateApplicationExistence(requestInfo, application);
        assertTrue(result);
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
    public void testValidateApplicationExistence_WithMissingCaseId_ShouldThrowException() {
        application.setId(UUID.randomUUID());
        application.setCnrNumber("cnr123");
        application.setReferenceId(UUID.randomUUID());

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateApplicationExistence(requestInfo, application));
        assertEquals("caseId is mandatory for updating application", exception.getMessage());
    }
    @Test
    public void testValidateApplicationExistence_WithMissingTenantId_ShouldThrowException() {
        application.setId(UUID.randomUUID());
        application.setCaseId("caseId");
        application.setCnrNumber("cnr123");
        application.setFilingNumber("file123");
        when(caseUtil.fetchCaseDetails(any())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateApplicationExistence(requestInfo, application));

        assertEquals("tenantId is mandatory for updating application", exception.getMessage());
    }
    @Test
    public void testValidateApplicationExistence_WithMissingApplicationType_ShouldThrowException() {
        application.setId(UUID.randomUUID());
        application.setCaseId("caseId");
        application.setCnrNumber("cnr123");
        application.setTenantId("tID");
        application.setFilingNumber("file123");

        when(caseUtil.fetchCaseDetails(any())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class,
                () -> validator.validateApplicationExistence(requestInfo, application));

        assertEquals("applicationType is mandatory for updating application", exception.getMessage());
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
}