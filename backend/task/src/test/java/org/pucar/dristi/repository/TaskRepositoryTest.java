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
    void testCheckTaskExistsNotFound() {
        // Arrange
        TaskExists taskExists = new TaskExists();

        // Act
        TaskExists result = taskRepository.checkTaskExists(taskExists);

        // Assert
        assertFalse(result.getExists());
    }
}
