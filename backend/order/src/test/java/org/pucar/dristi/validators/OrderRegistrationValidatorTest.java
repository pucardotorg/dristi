package org.pucar.dristi.validators;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.OrderRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.FileStoreUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.*;

@ExtendWith(MockitoExtension.class)
class OrderRegistrationValidatorTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private CaseUtil caseUtil;

    @Mock
    private FileStoreUtil fileStoreUtil;

    @Mock
    private MdmsUtil mdmsUtil;

    @Mock
    private Configuration configuration;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderRegistrationValidator orderRegistrationValidator;

    @BeforeEach
    void setUp() {
        orderRegistrationValidator = new OrderRegistrationValidator(repository, caseUtil, fileStoreUtil,configuration,mdmsUtil,objectMapper);
    }

    @Test
    void testValidateOrderRegistration_success() {
        // Prepare test data
        Order order = new Order();
        order.setStatuteSection(new StatuteSection());
        order.setOrderCategory("Judicial");
        order.setCnrNumber("CNR12345");
        order.setFilingNumber("FIL12345");

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setRequestInfo(new RequestInfo());
        orderRequest.setOrder(order);

        // Mock behavior
        when(caseUtil.fetchCaseDetails(any(), eq(order.getCnrNumber()), eq(order.getFilingNumber())))
                .thenReturn(true);

        // Execute method
        assertDoesNotThrow(() -> orderRegistrationValidator.validateOrderRegistration(orderRequest));

        // Verify
        verify(caseUtil).fetchCaseDetails(any(), eq(order.getCnrNumber()), eq(order.getFilingNumber()));
    }

    @Test
    void testValidateOrderRegistration_missingStatuteSection() {
        // Prepare test data
        Order order = new Order();
        order.setOrderCategory("Judicial");

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setRequestInfo(new RequestInfo());
        orderRequest.setOrder(order);

        // Execute and verify exception
        CustomException exception = assertThrows(CustomException.class, () ->
                orderRegistrationValidator.validateOrderRegistration(orderRequest));
        assertEquals(CREATE_ORDER_ERR, exception.getCode());
        assertEquals("statute and section is mandatory for creating order", exception.getMessage());
    }

    @Test
    void testValidateOrderRegistration_invalidCase() {
        // Prepare test data
        Order order = new Order();
        order.setStatuteSection(new StatuteSection());
        order.setOrderCategory("Judicial");
        order.setCnrNumber("CNR12345");
        order.setFilingNumber("FIL12345");

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setRequestInfo(new RequestInfo());
        orderRequest.setOrder(order);

        // Mock behavior
        when(caseUtil.fetchCaseDetails(any(), eq(order.getCnrNumber()), eq(order.getFilingNumber())))
                .thenReturn(false);

        // Execute and verify exception
        CustomException exception = assertThrows(CustomException.class, () ->
                orderRegistrationValidator.validateOrderRegistration(orderRequest));
        assertEquals("INVALID_CASE_DETAILS", exception.getCode());
        assertEquals("Invalid Case", exception.getMessage());
    }

    @Test
    void testValidateApplicationExistence_true() {
        // Prepare test data
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setCnrNumber("CNR12345");
        order.setFilingNumber("FIL12345");

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrder(order);

        OrderExists orderExists = new OrderExists();
        orderExists.setExists(true);
        List<OrderExists> orderExistsList = new ArrayList<>();
        orderExistsList.add(orderExists);

        // Mock behavior
        when(repository.checkOrderExists(anyList())).thenReturn(orderExistsList);

        // Execute method
        boolean result = orderRegistrationValidator.validateApplicationExistence(orderRequest);

        // Verify
        assertTrue(result);
        verify(repository).checkOrderExists(anyList());
    }

    @Test
    void testValidateApplicationExistence_false() {
        // Prepare test data
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setCnrNumber("CNR12345");
        order.setFilingNumber("FIL12345");

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrder(order);

        List<OrderExists> orderExistsList = new ArrayList<>();

        // Mock behavior
        when(repository.checkOrderExists(anyList())).thenReturn(orderExistsList);

        // Execute method
        boolean result = orderRegistrationValidator.validateApplicationExistence(orderRequest);

        // Verify
        assertFalse(result);
        verify(repository).checkOrderExists(anyList());
    }

    @Test
    void testValidateDocuments_success() {
        // Prepare test data
        Order order = new Order();
        Document document = new Document();
        document.setFileStore("fileStoreId");
        List<Document> documents = new ArrayList<>();
        documents.add(document);
        order.setDocuments(documents);
        order.setTenantId("pg");
        order.setFilingNumber("123");
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrder(order);

        // Mock behavior
        when(fileStoreUtil.doesFileExist(anyString(), eq("fileStoreId"))).thenReturn(true);

        // Execute method
        assertDoesNotThrow(() -> orderRegistrationValidator.validateApplicationExistence(orderRequest));

        // Verify
        verify(fileStoreUtil).doesFileExist(anyString(), eq("fileStoreId"));
    }

    @Test
    void testValidateDocuments_invalidFileStore() {
        // Prepare test data
        Order order = new Order();
        Document document = new Document();
        document.setFileStore("fileStoreId");
        List<Document> documents = new ArrayList<>();
        documents.add(document);
        order.setDocuments(documents);
        order.setTenantId("pg");
        order.setFilingNumber("123");
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrder(order);

        // Mock behavior
        when(fileStoreUtil.doesFileExist(anyString(), eq("fileStoreId"))).thenReturn(false);

        // Execute and verify exception
        CustomException exception = assertThrows(CustomException.class, () ->
                orderRegistrationValidator.validateApplicationExistence(orderRequest));
        assertEquals(INVALID_FILESTORE_ID, exception.getCode());
        assertEquals(INVALID_DOCUMENT_DETAILS, exception.getMessage());
    }

    @Test
    void testValidateDocuments_missingFileStore() {
        // Prepare test data
        Order order = new Order();
        Document document = new Document();
        List<Document> documents = new ArrayList<>();
        documents.add(document);
        order.setDocuments(documents);
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrder(order);

        // Execute and verify exception
        CustomException exception = assertThrows(CustomException.class, () ->
                orderRegistrationValidator.validateApplicationExistence(orderRequest));
        assertEquals(INVALID_FILESTORE_ID, exception.getCode());
        assertEquals(INVALID_DOCUMENT_DETAILS, exception.getMessage());
    }
}
