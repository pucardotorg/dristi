package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskRowMapperTest {

    @InjectMocks
    private TaskRowMapper taskRowMapper;

    @Mock
    private ResultSet rs;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testExtractDataSuccess() throws Exception {
        String taskNumber = "tasknumber";
        UUID taskId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        String tenantId = "tenantid";
        String filingNumber = "filingnumber";
        String cnrNumber = "cnrnumber";
        String createdDate = "01-01-2023";
        String dateCloseBy = "15-01-2023";
        String dateClosed = "20-01-2023";
        String taskDescription = "taskdescription";
        String taskDetails = "taskdetails";
        String taskType = "tasktype";
        String assignedTo = "assignedto";
        String status = "status";
        Boolean isActive = true;

        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("tasknumber")).thenReturn(taskNumber);
        when(rs.getString("id")).thenReturn(taskId.toString());
        when(rs.getString("orderid")).thenReturn(orderId.toString());
        when(rs.getString("tenantid")).thenReturn(tenantId);
        when(rs.getString("filingnumber")).thenReturn(filingNumber);
        when(rs.getString("cnrnumber")).thenReturn(cnrNumber);
        when(rs.getString("createddate")).thenReturn(createdDate);
        when(rs.getString("datecloseby")).thenReturn(dateCloseBy);
        when(rs.getString("dateclosed")).thenReturn(dateClosed);
        when(rs.getString("taskdescription")).thenReturn(taskDescription);
        when(rs.getString("taskdetails")).thenReturn(taskDetails);
        when(rs.getString("tasktype")).thenReturn(taskType);
        when(rs.getString("assignedto")).thenReturn(assignedTo);
        when(rs.getString("status")).thenReturn(status);
        when(rs.getString("isactive")).thenReturn(isActive.toString());
        when(rs.getLong("createdtime")).thenReturn(1L);
        when(rs.getString("createdby")).thenReturn("createdby");
        when(rs.getLong("lastmodifiedtime")).thenReturn(2L);
        when(rs.getString("lastmodifiedby")).thenReturn("lastmodifiedby");

        PGobject pgObject = new PGobject();
        pgObject.setType("json");
        pgObject.setValue("{\"key\":\"value\"}");
        when(rs.getObject("additionaldetails")).thenReturn(pgObject);

        List<Task> result = taskRowMapper.extractData(rs);

        assertNotNull(result);
        assertEquals(1, result.size());
        Task task = result.get(0);
        assertNotNull(task);
        assertEquals(taskId, task.getId());
        assertEquals(orderId, task.getOrderId());
        assertEquals(tenantId, task.getTenantId());
        assertEquals(filingNumber, task.getFilingNumber());
        assertEquals(cnrNumber, task.getCnrNumber());
        assertEquals(LocalDate.parse(createdDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")), task.getCreatedDate());
        assertEquals(LocalDate.parse(dateCloseBy, DateTimeFormatter.ofPattern("dd-MM-yyyy")), task.getDateCloseBy());
        assertEquals(LocalDate.parse(dateClosed, DateTimeFormatter.ofPattern("dd-MM-yyyy")), task.getDateClosed());
        assertEquals(taskDescription, task.getTaskDescription());
        assertEquals(taskDetails, task.getTaskDetails());
        assertEquals(taskType, task.getTaskType());
        assertEquals(assignedTo, task.getAssignedTo());
        assertEquals(status, task.getStatus());
        assertEquals(isActive, task.getIsActive());
        assertEquals("createdby", task.getAuditDetails().getCreatedBy());
        assertEquals(1L, task.getAuditDetails().getCreatedTime());
        assertEquals("lastmodifiedby", task.getAuditDetails().getLastModifiedBy());
        assertEquals(2L, task.getAuditDetails().getLastModifiedTime());
        assertNotNull(task.getAdditionalDetails());

        verify(rs, times(2)).getString("tasknumber");
        verify(rs, times(1)).getString("id");
        verify(rs, times(1)).getString("orderid");
        verify(rs, times(1)).getString("tenantid");
        verify(rs, times(1)).getString("filingnumber");
        verify(rs, times(1)).getString("cnrnumber");
        verify(rs, times(1)).getString("createddate");
        verify(rs, times(1)).getString("datecloseby");
        verify(rs, times(1)).getString("dateclosed");
        verify(rs, times(1)).getString("taskdescription");
        verify(rs, times(1)).getString("taskdetails");
        verify(rs, times(1)).getString("tasktype");
        verify(rs, times(1)).getString("assignedto");
        verify(rs, times(1)).getString("status");
        verify(rs, times(1)).getString("isactive");
        verify(rs, times(1)).getLong("createdtime");
        verify(rs, times(1)).getString("createdby");
        verify(rs, times(1)).getLong("lastmodifiedtime");
        verify(rs, times(1)).getString("lastmodifiedby");
        verify(rs, times(1)).getObject("additionaldetails");
    }

    @Test
    void testExtractDataNoAdditionalDetails() throws Exception {
        String taskNumber = "tasknumber";
        UUID taskId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        String tenantId = "tenantid";
        String filingNumber = "filingnumber";
        String cnrNumber = "cnrnumber";
        String createdDate = "01-01-2023";
        String dateCloseBy = "15-01-2023";
        String dateClosed = "20-01-2023";
        String taskDescription = "taskdescription";
        String taskDetails = "taskdetails";
        String taskType = "tasktype";
        String assignedTo = "assignedto";
        String status = "status";
        Boolean isActive = true;

        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("tasknumber")).thenReturn(taskNumber);
        when(rs.getString("id")).thenReturn(taskId.toString());
        when(rs.getString("orderid")).thenReturn(orderId.toString());
        when(rs.getString("tenantid")).thenReturn(tenantId);
        when(rs.getString("filingnumber")).thenReturn(filingNumber);
        when(rs.getString("cnrnumber")).thenReturn(cnrNumber);
        when(rs.getString("createddate")).thenReturn(createdDate);
        when(rs.getString("datecloseby")).thenReturn(dateCloseBy);
        when(rs.getString("dateclosed")).thenReturn(dateClosed);
        when(rs.getString("taskdescription")).thenReturn(taskDescription);
        when(rs.getString("taskdetails")).thenReturn(taskDetails);
        when(rs.getString("tasktype")).thenReturn(taskType);
        when(rs.getString("assignedto")).thenReturn(assignedTo);
        when(rs.getString("status")).thenReturn(status);
        when(rs.getString("isactive")).thenReturn(isActive.toString());
        when(rs.getLong("createdtime")).thenReturn(1L);
        when(rs.getString("createdby")).thenReturn("createdby");
        when(rs.getLong("lastmodifiedtime")).thenReturn(2L);
        when(rs.getString("lastmodifiedby")).thenReturn("lastmodifiedby");
        when(rs.getObject("additionaldetails")).thenReturn(null);

        List<Task> result = taskRowMapper.extractData(rs);

        assertNotNull(result);
        assertEquals(1, result.size());
        Task task = result.get(0);
        assertNotNull(task);
        assertEquals(taskId, task.getId());
        assertEquals(orderId, task.getOrderId());
        assertEquals(tenantId, task.getTenantId());
        assertEquals(filingNumber, task.getFilingNumber());
        assertEquals(cnrNumber, task.getCnrNumber());
        assertEquals(LocalDate.parse(createdDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")), task.getCreatedDate());
        assertEquals(LocalDate.parse(dateCloseBy, DateTimeFormatter.ofPattern("dd-MM-yyyy")), task.getDateCloseBy());
        assertEquals(LocalDate.parse(dateClosed, DateTimeFormatter.ofPattern("dd-MM-yyyy")), task.getDateClosed());
        assertEquals(taskDescription, task.getTaskDescription());
        assertEquals(taskDetails, task.getTaskDetails());
        assertEquals(taskType, task.getTaskType());
        assertEquals(assignedTo, task.getAssignedTo());
        assertEquals(status, task.getStatus());
        assertEquals(isActive, task.getIsActive());
        assertEquals("createdby", task.getAuditDetails().getCreatedBy());
        assertEquals(1L, task.getAuditDetails().getCreatedTime());
        assertEquals("lastmodifiedby", task.getAuditDetails().getLastModifiedBy());
        assertEquals(2L, task.getAuditDetails().getLastModifiedTime());
        assertNull(task.getAdditionalDetails());

        verify(rs, times(2)).getString("tasknumber");
        verify(rs, times(1)).getString("id");
        verify(rs, times(1)).getString("orderid");
        verify(rs, times(1)).getString("tenantid");
        verify(rs, times(1)).getString("filingnumber");
        verify(rs, times(1)).getString("cnrnumber");
        verify(rs, times(1)).getString("createddate");
        verify(rs, times(1)).getString("datecloseby");
        verify(rs, times(1)).getString("dateclosed");
        verify(rs, times(1)).getString("taskdescription");
        verify(rs, times(1)).getString("taskdetails");
        verify(rs, times(1)).getString("tasktype");
        verify(rs, times(1)).getString("assignedto");
        verify(rs, times(1)).getString("status");
        verify(rs, times(1)).getString("isactive");
        verify(rs, times(1)).getLong("createdtime");
        verify(rs, times(1)).getString("createdby");
        verify(rs, times(1)).getLong("lastmodifiedtime");
        verify(rs, times(1)).getString("lastmodifiedby");
        verify(rs, times(1)).getObject("additionaldetails");
    }

    @Test
    void testExtractDataSQLException() throws Exception {
        when(rs.next()).thenThrow(new SQLException("DB Error"));

        CustomException exception = assertThrows(CustomException.class, () -> taskRowMapper.extractData(rs));
        assertEquals("Error in row mapper", exception.getCode());
        assertEquals("Error occurred while processing Task ResultSet: DB Error", exception.getMessage());

        verify(rs, times(1)).next();
    }

    @Test
    void testExtractDataCustomException() throws Exception {
        when(rs.next()).thenReturn(true);
        when(rs.getString("tasknumber")).thenThrow(new CustomException("CUSTOM_ERROR", "Custom error"));

        CustomException exception = assertThrows(CustomException.class, () -> taskRowMapper.extractData(rs));
        assertEquals("CUSTOM_ERROR", exception.getCode());
        assertEquals("Custom error", exception.getMessage());

        verify(rs, times(1)).next();
        verify(rs, times(1)).getString("tasknumber");
    }

    @Test
    void testExtractDataDateParseException() throws Exception {
        String taskNumber = "tasknumber";
        UUID taskId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        String tenantId = "tenantid";
        String filingNumber = "filingnumber";
        String cnrNumber = "cnrnumber";
        String invalidDate = "invalid-date";
        String taskDescription = "taskdescription";
        String taskDetails = "taskdetails";
        String taskType = "tasktype";
        String assignedTo = "assignedto";
        String status = "status";
        Boolean isActive = true;

        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("tasknumber")).thenReturn(taskNumber);
        when(rs.getString("id")).thenReturn(taskId.toString());
        when(rs.getString("orderid")).thenReturn(orderId.toString());
        when(rs.getString("tenantid")).thenReturn(tenantId);
        when(rs.getString("filingnumber")).thenReturn(filingNumber);
        when(rs.getString("cnrnumber")).thenReturn(cnrNumber);
        when(rs.getString("createddate")).thenReturn(invalidDate);
        when(rs.getString("datecloseby")).thenReturn(invalidDate);
        when(rs.getString("dateclosed")).thenReturn(invalidDate);
        when(rs.getString("taskdescription")).thenReturn(taskDescription);
        when(rs.getString("taskdetails")).thenReturn(taskDetails);
        when(rs.getString("tasktype")).thenReturn(taskType);
        when(rs.getString("assignedto")).thenReturn(assignedTo);
        when(rs.getString("status")).thenReturn(status);
        when(rs.getString("isactive")).thenReturn(isActive.toString());
        when(rs.getLong("createdtime")).thenReturn(1L);
        when(rs.getString("createdby")).thenReturn("createdby");
        when(rs.getLong("lastmodifiedtime")).thenReturn(2L);
        when(rs.getString("lastmodifiedby")).thenReturn("lastmodifiedby");

        PGobject pgObject = new PGobject();
        pgObject.setType("json");
        pgObject.setValue("{\"key\":\"value\"}");
        when(rs.getObject("additionaldetails")).thenReturn(pgObject);

        List<Task> result = taskRowMapper.extractData(rs);

        assertNotNull(result);
        assertEquals(1, result.size());
        Task task = result.get(0);
        assertNotNull(task);
        assertEquals(taskId, task.getId());
        assertEquals(orderId, task.getOrderId());
        assertEquals(tenantId, task.getTenantId());
        assertEquals(filingNumber, task.getFilingNumber());
        assertEquals(cnrNumber, task.getCnrNumber());
        assertNull(task.getCreatedDate());
        assertNull(task.getDateCloseBy());
        assertNull(task.getDateClosed());
        assertEquals(taskDescription, task.getTaskDescription());
        assertEquals(taskDetails, task.getTaskDetails());
        assertEquals(taskType, task.getTaskType());
        assertEquals(assignedTo, task.getAssignedTo());
        assertEquals(status, task.getStatus());
        assertEquals(isActive, task.getIsActive());
        assertEquals("createdby", task.getAuditDetails().getCreatedBy());
        assertEquals(1L, task.getAuditDetails().getCreatedTime());
        assertEquals("lastmodifiedby", task.getAuditDetails().getLastModifiedBy());
        assertEquals(2L, task.getAuditDetails().getLastModifiedTime());
        assertNotNull(task.getAdditionalDetails());

        verify(rs, times(2)).getString("tasknumber");
        verify(rs, times(1)).getString("id");
        verify(rs, times(1)).getString("orderid");
        verify(rs, times(1)).getString("tenantid");
        verify(rs, times(1)).getString("filingnumber");
        verify(rs, times(1)).getString("cnrnumber");
        verify(rs, times(1)).getString("createddate");
        verify(rs, times(1)).getString("datecloseby");
        verify(rs, times(1)).getString("dateclosed");
        verify(rs, times(1)).getString("taskdescription");
        verify(rs, times(1)).getString("taskdetails");
        verify(rs, times(1)).getString("tasktype");
        verify(rs, times(1)).getString("assignedto");
        verify(rs, times(1)).getString("status");
        verify(rs, times(1)).getString("isactive");
        verify(rs, times(1)).getLong("createdtime");
        verify(rs, times(1)).getString("createdby");
        verify(rs, times(1)).getLong("lastmodifiedtime");
        verify(rs, times(1)).getString("lastmodifiedby");
        verify(rs, times(1)).getObject("additionaldetails");
    }
}
