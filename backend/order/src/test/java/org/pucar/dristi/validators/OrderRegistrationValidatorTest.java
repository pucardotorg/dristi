package org.pucar.dristi.validators;

import static org.junit.jupiter.api.Assertions.*;

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
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.OrderRequest;

import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.FileStoreUtil;
import org.pucar.dristi.web.models.*;

import java.util.ArrayList;
import java.util.List;

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
    private MdmsUtil mdmsUtil;

    @Mock
    private FileStoreUtil fileStoreUtil;

    @Mock
    private Configuration configuration;

    @InjectMocks
    private OrderRegistrationValidator orderRegistrationValidator;

    @BeforeEach
    void setUp() {
        orderRegistrationValidator = new OrderRegistrationValidator(repository, caseUtil, mdmsUtil,fileStoreUtil,configuration);
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
