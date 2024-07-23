package org.pucar.dristi.repository;

import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.repository.querybuilder.TaskQueryBuilder;
import org.pucar.dristi.repository.rowmapper.AmountRowMapper;
import org.pucar.dristi.repository.rowmapper.DocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.TaskRowMapper;
import org.pucar.dristi.web.models.Amount;
import org.pucar.dristi.web.models.Task;
import org.pucar.dristi.web.models.TaskExists;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskRepositoryTest {

    @InjectMocks
    private TaskRepository taskRepository;

    @Mock
    private TaskQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private TaskRowMapper rowMapper;

    @Mock
    private AmountRowMapper amountRowMapper;

    @Mock
    private DocumentRowMapper documentRowMapper;

    private String id;
    private String tenantId;
    private String status;
    private UUID orderId;
    private String cnrNumber;

    @BeforeEach
    void setUp() {
        id = "test-id";
        tenantId = "test-tenant";
        status = "test-status";
        orderId = UUID.randomUUID();
        cnrNumber = "test-cnrNumber";
    }

//    @Test
//    void testGetApplicationsSuccess() {
//        String taskQuery = "task-query";
//        String amountQuery = "amount-query";
//        String documentQuery = "document-query";
//
//        Task task = new Task();
//        task.setId(UUID.randomUUID());
//
//        Amount amount = new Amount();
//        Document document = new Document();
//
//        List<Task> tasks = Collections.singletonList(task);
//        Map<UUID, Amount> amountMap = new HashMap<>();
//        amountMap.put(task.getId(), amount);
//        Map<UUID, List<Document>> documentMap = new HashMap<>();
//        documentMap.put(task.getId(), Collections.singletonList(document));
//
//        when(queryBuilder.getTaskSearchQuery(id, tenantId, status, orderId, cnrNumber)).thenReturn(taskQuery);
//        when(jdbcTemplate.query(taskQuery, rowMapper)).thenReturn(tasks);
//
//        when(queryBuilder.getAmountSearchQuery(anyList(), anyList())).thenReturn(amountQuery);
//        when(jdbcTemplate.query(amountQuery, any(Object[].class), amountRowMapper)).thenReturn(amountMap);
//
//        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList())).thenReturn(documentQuery);
//        when(jdbcTemplate.query(documentQuery, any(Object[].class), eq(documentRowMapper))).thenReturn(documentMap);
//        List<Task> result = taskRepository.getApplications(id, tenantId, status, orderId, cnrNumber);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(amount, result.get(0).getAmount());
//        assertEquals(document, result.get(0).getDocuments().get(0));
//
//        verify(queryBuilder).getTaskSearchQuery(id, tenantId, status, orderId, cnrNumber);
//        verify(jdbcTemplate).query(taskQuery, rowMapper);
//        verify(queryBuilder).getAmountSearchQuery(anyList(), anyList());
//        verify(jdbcTemplate).query(amountQuery, any(Object[].class), eq(amountRowMapper));
//        verify(queryBuilder).getDocumentSearchQuery(anyList(), anyList());
//        verify(jdbcTemplate).query(documentQuery, any(Object[].class), eq(documentRowMapper));
//    }

    @Test
    void testGetApplicationsNoResults() {
        String taskQuery = "task-query";
        when(queryBuilder.getTaskSearchQuery(id, tenantId, status, orderId, cnrNumber)).thenReturn(taskQuery);
        when(jdbcTemplate.query(taskQuery, rowMapper)).thenReturn(Collections.emptyList());

        List<Task> result = taskRepository.getApplications(id, tenantId, status, orderId, cnrNumber);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(queryBuilder).getTaskSearchQuery(id, tenantId, status, orderId, cnrNumber);
        verify(jdbcTemplate).query(taskQuery, rowMapper);
    }

    @Test
    void testGetApplicationsException() {
        String taskQuery = "task-query";
        when(queryBuilder.getTaskSearchQuery(id, tenantId, status, orderId, cnrNumber)).thenReturn(taskQuery);
        when(jdbcTemplate.query(taskQuery, rowMapper)).thenThrow(new RuntimeException("DB Error"));

        CustomException exception = assertThrows(CustomException.class, () -> taskRepository.getApplications(id, tenantId, status, orderId, cnrNumber));
        assertEquals("Error while searching task", exception.getCode());
        assertEquals("Exception while fetching task application list: DB Error", exception.getMessage());

        verify(queryBuilder).getTaskSearchQuery(id, tenantId, status, orderId, cnrNumber);
        verify(jdbcTemplate).query(taskQuery, rowMapper);
    }

    @Test
    void testCheckTaskExistsSuccess() {
        String cnrNumber = "cnrNumber";
        String filingNumber = "filingNumber";
        String existQuery = "exist-query";

        TaskExists taskExists = new TaskExists();
        taskExists.setCnrNumber(cnrNumber);
        taskExists.setFilingNumber(filingNumber);

        when(queryBuilder.checkTaskExistQuery(cnrNumber, filingNumber)).thenReturn(existQuery);
        when(jdbcTemplate.queryForObject(existQuery, Integer.class)).thenReturn(1);

        TaskExists result = taskRepository.checkTaskExists(taskExists);

        assertNotNull(result);
        assertTrue(result.getExists());

        verify(queryBuilder).checkTaskExistQuery(cnrNumber, filingNumber);
        verify(jdbcTemplate).queryForObject(existQuery, Integer.class);
    }

    @Test
    void testCheckTaskExistsNoResults() {
        String cnrNumber = "cnrNumber";
        String filingNumber = "filingNumber";
        String existQuery = "exist-query";

        TaskExists taskExists = new TaskExists();
        taskExists.setCnrNumber(cnrNumber);
        taskExists.setFilingNumber(filingNumber);

        when(queryBuilder.checkTaskExistQuery(cnrNumber, filingNumber)).thenReturn(existQuery);
        when(jdbcTemplate.queryForObject(existQuery, Integer.class)).thenReturn(0);

        TaskExists result = taskRepository.checkTaskExists(taskExists);

        assertNotNull(result);
        assertFalse(result.getExists());

        verify(queryBuilder).checkTaskExistQuery(cnrNumber, filingNumber);
        verify(jdbcTemplate).queryForObject(existQuery, Integer.class);
    }

    @Test
    void testCheckTaskExistsException() {
        String cnrNumber = "cnrNumber";
        String filingNumber = "filingNumber";
        String existQuery = "exist-query";

        TaskExists taskExists = new TaskExists();
        taskExists.setCnrNumber(cnrNumber);
        taskExists.setFilingNumber(filingNumber);

        when(queryBuilder.checkTaskExistQuery(cnrNumber, filingNumber)).thenReturn(existQuery);
        when(jdbcTemplate.queryForObject(existQuery, Integer.class)).thenThrow(new RuntimeException("DB Error"));

        CustomException exception = assertThrows(CustomException.class, () -> taskRepository.checkTaskExists(taskExists));
        assertEquals("Error while checking task exist", exception.getCode());
        assertEquals("Custom exception while checking task exist : DB Error", exception.getMessage());

        verify(queryBuilder).checkTaskExistQuery(cnrNumber, filingNumber);
        verify(jdbcTemplate).queryForObject(existQuery, Integer.class);
    }

    @Test
    void testCheckTaskExistsNoCnrOrFilingNumber() {
        TaskExists taskExists = new TaskExists();

        TaskExists result = taskRepository.checkTaskExists(taskExists);

        assertNotNull(result);
        assertFalse(result.getExists());

        verify(queryBuilder, never()).checkTaskExistQuery(anyString(), anyString());
        verify(jdbcTemplate, never()).queryForObject(anyString(), any(Class.class));
    }
}