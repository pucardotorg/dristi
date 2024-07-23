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

    @Mock
    private TaskRowMapper rowMapper;

    @Mock
    private AmountRowMapper amountRowMapper;

    @Mock
    private DocumentRowMapper documentRowMapper;

    @InjectMocks
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetApplications_Success() {
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

        // Mock query builder method
        when(queryBuilder.getTaskSearchQuery(any(),any()))
                .thenReturn("SELECT * FROM tasks WHERE ...");

        // Mock JDBC template query method
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(TaskRowMapper.class)))
                .thenReturn(mockTaskList);

        // Test the method
        List<Task> result = taskRepository.getApplications(new TaskCriteria());

        // Verify the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(id1, result.get(0).getId()); // Compare UUIDs directly
        assertEquals(id2, result.get(1).getId()); // Compare UUIDs directly

        // Verify method calls
        verify(queryBuilder, times(1)).getTaskSearchQuery(any(),anyList());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(TaskRowMapper.class));
    }


    @Test
    public void testGetApplications_EmptyResult() {
        // Mock query builder method
        when(queryBuilder.getTaskSearchQuery(any(), anyList()))
                .thenReturn("SELECT * FROM tasks WHERE ...");

        // Mock JDBC template query method to return empty list
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(TaskRowMapper.class)))
                .thenReturn(new ArrayList<>());

        // Test the method
        List<Task> result = taskRepository.getApplications(new TaskCriteria());

        // Verify the result
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify method calls
        verify(queryBuilder, times(1)).getTaskSearchQuery(any(), anyList());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(TaskRowMapper.class));
    }

    @Test
    public void testGetApplications_Exception() {
        // Mock query builder method
        when(queryBuilder.getTaskSearchQuery(any(), anyList()))
                .thenReturn("SELECT * FROM tasks WHERE ...");

        // Mock JDBC template query method to throw exception
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(TaskRowMapper.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Test the method and expect CustomException
        try {
            taskRepository.getApplications(new TaskCriteria());
            fail("Expected CustomException was not thrown");
        } catch (CustomException e) {
            // Verify the exception
            assertEquals("Exception while fetching task application list: Database error", e.getMessage());
            // Optionally assert other details from the exception
        }

        // Verify method calls
        verify(queryBuilder, times(1)).getTaskSearchQuery(any(), anyList());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(TaskRowMapper.class));
    }

    @Test
    public void testCheckTaskExists_NoMatch() {
        // Mock data
        TaskExists taskExists = new TaskExists();
        taskExists.setCnrNumber("123");
        taskExists.setExists(null); // Initial state

        // Mock query builder method
        when(queryBuilder.checkTaskExistQuery(eq("123"), isNull(), isNull(), anyList()))
                .thenReturn("SELECT COUNT(*) FROM tasks WHERE cnrnumber = ?");

        // Mock JDBC template queryForObject method to return zero count
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class)))
                .thenReturn(0);

        // Test the method
        TaskExists result = taskRepository.checkTaskExists(taskExists);

        // Verify the result
        assertNotNull(result);
        assertFalse(result.getExists());

        // Verify method calls
        verify(queryBuilder, times(1)).checkTaskExistQuery(eq("123"), isNull(), isNull(), anyList());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), eq(Integer.class));
    }

    @Test
    public void testCheckTaskExists_Match() {
        // Mock data
        TaskExists taskExists = new TaskExists();
        taskExists.setCnrNumber("123");
        taskExists.setExists(null); // Initial state

        // Mock query builder method
        when(queryBuilder.checkTaskExistQuery(eq("123"), isNull(), isNull(), anyList()))
                .thenReturn("SELECT COUNT(*) FROM tasks WHERE cnrnumber = ?");

        // Mock JDBC template queryForObject method to return count > 0
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class)))
                .thenReturn(1);

        // Test the method
        TaskExists result = taskRepository.checkTaskExists(taskExists);

        // Verify the result
        assertNotNull(result);
        assertTrue(result.getExists());

        // Verify method calls
        verify(queryBuilder, times(1)).checkTaskExistQuery(eq("123"), isNull(), isNull(), anyList());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), eq(Integer.class));
    }

    @Test
    public void testCheckTaskExists_Exception() {
        // Mock data
        TaskExists taskExists = new TaskExists();
        taskExists.setCnrNumber("123");
        taskExists.setExists(null); // Initial state

        // Mock query builder method
        when(queryBuilder.checkTaskExistQuery(eq("123"), isNull(), isNull(), anyList()))
                .thenReturn("SELECT COUNT(*) FROM tasks WHERE cnrnumber = ?");

        // Mock JDBC template queryForObject method to throw exception
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Test the method and expect CustomException
        assertThrows(CustomException.class, () -> taskRepository.checkTaskExists(taskExists));

        // Verify method calls
        verify(queryBuilder, times(1)).checkTaskExistQuery(eq("123"), isNull(), isNull(), anyList());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), eq(Integer.class));
    }


}
