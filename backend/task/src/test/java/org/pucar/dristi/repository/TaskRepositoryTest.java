package org.pucar.dristi.repository;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.repository.querybuilder.TaskQueryBuilder;
import org.pucar.dristi.repository.rowmapper.AmountRowMapper;
import org.pucar.dristi.repository.rowmapper.DocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.TaskRowMapper;
import org.pucar.dristi.web.models.Pagination;
import org.pucar.dristi.web.models.Task;
import org.pucar.dristi.web.models.TaskCriteria;
import org.pucar.dristi.web.models.TaskExists;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TaskRepositoryTest {

    @Mock
    private TaskQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private TaskRepository taskRepository;

    @Mock
    private TaskRowMapper rowMapper;

    @Mock
    private AmountRowMapper amountRowMapper;

    @Mock
    private DocumentRowMapper documentRowMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testgetTasks_Success() {
        // Mock data
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        List<Task> mockTaskList = new ArrayList<>();
        Task mockTask1 = new Task();
        mockTask1.setId(id1); // Set a valid UUID for id
        Task mockTask2 = new Task();
        mockTask2.setId(id2); // Set a valid UUID for id

        mockTaskList.add(mockTask1);
        mockTaskList.add(mockTask2);
        String taskQuery = "SELECT * FROM tasks WHERE ...";

        when(queryBuilder.addOrderByQuery(anyString(), any(Pagination.class))).thenReturn(taskQuery);
        when(queryBuilder.addPaginationQuery(anyString(), any(Pagination.class), anyList(),anyList())).thenReturn(taskQuery);
        when(queryBuilder.getTotalCountQuery(anyString())).thenReturn(taskQuery);
        when(jdbcTemplate.queryForObject(eq(taskQuery),eq(Integer.class), any(Object[].class))).thenReturn(1);

        // Mock query builder method
        when(queryBuilder.getTaskSearchQuery(any(),any(),anyList())).thenReturn(taskQuery);

        // Mock JDBC template query method
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(TaskRowMapper.class)))
                .thenReturn(mockTaskList);

        // Test the method
        List<Task> result = taskRepository.getTasks(TaskCriteria.builder().build(),null);

        // Verify the result
        assertNotNull(result);

        // Verify method calls
        verify(queryBuilder, times(1)).getTaskSearchQuery(any(),anyList(),anyList());
    }


    @Test
    public void testgetTasks_EmptyResult() {
        // Mock query builder method
        when(queryBuilder.getTaskSearchQuery(any(), anyList(),anyList()))
                .thenReturn("SELECT * FROM tasks WHERE ...");

        // Mock JDBC template query method to return empty list
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(),any(TaskRowMapper.class)))
                .thenReturn(new ArrayList<>());

        // Test the method
        List<Task> result = taskRepository.getTasks(TaskCriteria.builder().build(),null);

        // Verify the result
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify method calls
        verify(queryBuilder, times(1)).getTaskSearchQuery(any(), anyList(),anyList());
    }

    @Test
    public void testCheckTaskExists_NoMatch() {
        // Mock data
        TaskExists taskExists = new TaskExists();
        taskExists.setCnrNumber("123");
        taskExists.setExists(null); // Initial state

        // Mock query builder method
        when(queryBuilder.checkTaskExistQuery(eq("123"), isNull(), isNull(),isNull(),isNull(), anyList()))
                .thenReturn("SELECT COUNT(*) FROM tasks WHERE cnrnumber = ?");

        // Mock JDBC template queryForObject method to return zero count
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(Object[].class)))
                .thenReturn(0);

        // Test the method
        TaskExists result = taskRepository.checkTaskExists(taskExists);

        // Verify the result
        assertNotNull(result);
        assertFalse(result.getExists());

        // Verify method calls
        verify(queryBuilder, times(1)).checkTaskExistQuery(eq("123"), isNull(), isNull(),isNull(),isNull(), anyList());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), any(Object[].class));
    }

    @Test
    public void testCheckTaskExists_Match() {
        // Mock data
        TaskExists taskExists = new TaskExists();
        taskExists.setCnrNumber("123");
        taskExists.setExists(null); // Initial state

        // Mock query builder method
        when(queryBuilder.checkTaskExistQuery(eq("123"), isNull(), isNull(),isNull(),isNull(), anyList()))
                .thenReturn("SELECT COUNT(*) FROM tasks WHERE cnrnumber = ?");

        // Mock JDBC template queryForObject method to return count > 0
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(Object[].class)))
                .thenReturn(1);

        // Test the method
        TaskExists result = taskRepository.checkTaskExists(taskExists);

        // Verify the result
        assertNotNull(result);
        assertTrue(result.getExists());

        // Verify method calls
        verify(queryBuilder, times(1)).checkTaskExistQuery(eq("123"), isNull(), isNull(),isNull(),isNull(), anyList());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), any(Object[].class));
    }

    @Test
    public void testCheckTaskExists_Exception() {
        // Mock data
        TaskExists taskExists = new TaskExists();
        taskExists.setCnrNumber("123");
        taskExists.setExists(null); // Initial state

        // Mock query builder method
        when(queryBuilder.checkTaskExistQuery(eq("123"), isNull(), isNull(),isNull(),isNull(), anyList()))
                .thenReturn("SELECT COUNT(*) FROM tasks WHERE cnrnumber = ?");

        // Mock JDBC template queryForObject method to throw exception
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(Object[].class)))
                .thenThrow(new RuntimeException("Database error"));

        // Test the method and expect CustomException
        assertThrows(CustomException.class, () -> taskRepository.checkTaskExists(taskExists));

        // Verify method calls
        verify(queryBuilder, times(1)).checkTaskExistQuery(eq("123"), isNull(), isNull(), isNull(),isNull(),anyList());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), any(Object[].class));
    }


}
