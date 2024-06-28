package org.pucar.dristi.repository;

import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.pucar.dristi.repository.querybuilder.TaskQueryBuilder;
import org.pucar.dristi.repository.rowmapper.AmountRowMapper;
import org.pucar.dristi.repository.rowmapper.DocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.TaskRowMapper;
import org.pucar.dristi.web.models.Amount;
import org.pucar.dristi.web.models.Task;
import org.pucar.dristi.web.models.TaskExists;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetApplications() {
        // Arrange
        String id = "1";
        String tenantId = "tenant1";
        String status = "active";
        UUID orderId = UUID.randomUUID();
        String cnrNumber = "CNR123";
        String taskNumber = "TASK123";

        String taskQuery = "SELECT * FROM tasks WHERE id = ?";
        String amountQuery = "SELECT * FROM amounts WHERE task_id IN (?)";
        String documentQuery = "SELECT * FROM documents WHERE task_id IN (?)";

        List<Object> preparedStmtList = new ArrayList<>();
        List<Task> taskList = new ArrayList<>();
        Task task = new Task();
        task.setId(UUID.randomUUID());
        taskList.add(task);

        Map<UUID, Amount> amountMap = new HashMap<>();
        Amount amount = new Amount();
        amountMap.put(task.getId(), amount);

        Map<UUID, List<Document>> documentMap = new HashMap<>();
        List<Document> documents = new ArrayList<>();
        Document document = new Document();
        documents.add(document);
        documentMap.put(task.getId(), documents);

        when(queryBuilder.getTaskSearchQuery(anyString(), anyString(), anyString(), any(), anyString(), anyString(), anyList()))
                .thenReturn(taskQuery);
        when(queryBuilder.getAmountSearchQuery(anyList(), anyList()))
                .thenReturn(amountQuery);
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList()))
                .thenReturn(documentQuery);

        // Act
        List<Task> result = taskRepository.getApplications(id, tenantId, status, orderId, cnrNumber, taskNumber);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void testGetApplicationsThrowsCustomException() {
        // Arrange
        when(queryBuilder.getTaskSearchQuery(anyString(), anyString(), anyString(), any(), anyString(), anyString(), anyList()))
                .thenThrow(new RuntimeException("Exception"));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            taskRepository.getApplications("1", "tenant1", "active", UUID.randomUUID(), "CNR123", "TASK123");
        });
        assertEquals("Exception while fetching task application list: Exception", exception.getMessage());
    }

    @Test
    void testCheckTaskExists() {
        // Arrange
        TaskExists taskExists = new TaskExists();
        taskExists.setCnrNumber("CNR123");
        taskExists.setFilingNumber("FILING123");
        taskExists.setTaskId(UUID.fromString("30b4edc0-298f-4f1e-a518-c2189a119ac6"));

        String taskExistQuery = "SELECT COUNT(*) FROM tasks WHERE cnrNumber = ? AND filingNumber = ? AND taskId = ?";
        when(queryBuilder.checkTaskExistQuery(anyString(), anyString(), any(), anyList()))
                .thenReturn(taskExistQuery);
        when(jdbcTemplate.queryForObject(eq(taskExistQuery), any(), eq(Integer.class)))
                .thenReturn(1);

        // Act
        TaskExists result = taskRepository.checkTaskExists(taskExists);

        // Assert
        assertTrue(result.getExists());
    }

    @Test
    void testCheckTaskExistsNotFound() {
        // Arrange
        TaskExists taskExists = new TaskExists();

        // Act
        TaskExists result = taskRepository.checkTaskExists(taskExists);

        // Assert
        assertFalse(result.getExists());
    }

    @Test
    void testCheckTaskExistsThrowsCustomException() {
        // Arrange
        TaskExists taskExists = new TaskExists();
        taskExists.setCnrNumber("CNR123");
        taskExists.setFilingNumber("FILING123");
        taskExists.setTaskId(UUID.fromString("30b4edc0-298f-4f1e-a518-c2189a119ac6"));

        when(queryBuilder.checkTaskExistQuery(anyString(), anyString(), any(), anyList()))
                .thenThrow(new RuntimeException("Exception"));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            taskRepository.checkTaskExists(taskExists);
        });
        assertEquals("Custom exception while checking task exist : Exception", exception.getMessage());
    }
}
