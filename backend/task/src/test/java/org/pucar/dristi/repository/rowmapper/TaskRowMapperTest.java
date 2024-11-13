package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskRowMapperTest {

    @InjectMocks
    private TaskRowMapper taskRowMapper;

    @Mock
    private ResultSet rs;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

//    @Test
//    void testExtractDataSuccess() throws Exception {
//        String taskNumber = "tasknumber";
//        UUID taskId = UUID.randomUUID();
//        UUID orderId = UUID.randomUUID();
//        String tenantId = "tenantid";
//        String filingNumber = "filingnumber";
//        String cnrNumber = "cnrnumber";
//        Long createdDate = 123422452l;
//        Long dateCloseBy = 1242424l;
//        Long dateClosed = 12134l;
//        String taskDescription = "taskdescription";
//        String taskDetails = "taskdetails";
//        String taskType = "tasktype";
//        String assignedTo = "{\"name\":\"test\"}";
//
//        String status = "status";
//        Boolean isActive = true;
//
//        when(rs.next()).thenReturn(true).thenReturn(false);
//        when(rs.getString("tasknumber")).thenReturn(taskNumber);
//        when(rs.getString("id")).thenReturn(taskId.toString());
//        when(rs.getString("orderid")).thenReturn(orderId.toString());
//        when(rs.getString("tenantid")).thenReturn(tenantId);
//        when(rs.getString("filingnumber")).thenReturn(filingNumber);
//        when(rs.getString("cnrnumber")).thenReturn(cnrNumber);
//        when(rs.getLong("createddate")).thenReturn(createdDate);
//        when(rs.getLong("datecloseby")).thenReturn(dateCloseBy);
//        when(rs.getLong("dateclosed")).thenReturn(dateClosed);
//        when(rs.getString("taskdescription")).thenReturn(taskDescription);
//        when(rs.getString("taskdetails")).thenReturn(taskDetails);
//        when(rs.getString("tasktype")).thenReturn(taskType);
//        when(rs.getString("assignedto")).thenReturn(assignedTo);
//        when(rs.getString("status")).thenReturn(status);
//        when(rs.getString("isactive")).thenReturn(isActive.toString());
//        when(rs.getLong("createdtime")).thenReturn(1L);
//        when(rs.getString("createdby")).thenReturn("createdby");
//        when(rs.getLong("lastmodifiedtime")).thenReturn(2L);
//        when(rs.getString("lastmodifiedby")).thenReturn("lastmodifiedby");
//
//        PGobject pgObject = new PGobject();
//        pgObject.setType("json");
//        pgObject.setValue("{\"key\":\"value\"}");
//        when(rs.getObject("additionaldetails")).thenReturn(pgObject);
//
//        List<Task> result = taskRowMapper.extractData(rs);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        Task task = result.get(0);
//        assertNotNull(task);
//        assertEquals(taskId, task.getId());
//        assertEquals(orderId, task.getOrderId());
//        assertEquals(tenantId, task.getTenantId());
//        assertEquals(filingNumber, task.getFilingNumber());
//        assertEquals(cnrNumber, task.getCnrNumber());
//        assertEquals(taskDescription, task.getTaskDescription());
//        assertEquals(taskDetails, task.getTaskDetails());
//        assertEquals(taskType, task.getTaskType());
//        assertEquals(status, task.getStatus());
//        assertEquals(isActive, task.getIsActive());
//        assertEquals("createdby", task.getAuditDetails().getCreatedBy());
//        assertEquals(1L, task.getAuditDetails().getCreatedTime());
//        assertEquals("lastmodifiedby", task.getAuditDetails().getLastModifiedBy());
//        assertEquals(2L, task.getAuditDetails().getLastModifiedTime());
//    }

//    @Test
//    void testExtractDataNoAdditionalDetails() throws Exception {
//        String taskNumber = "tasknumber";
//        UUID taskId = UUID.randomUUID();
//        UUID orderId = UUID.randomUUID();
//        String tenantId = "tenantid";
//        String filingNumber = "filingnumber";
//        String cnrNumber = "cnrnumber";
//        Long createdDate = 123422452l;
//        Long dateCloseBy = 1242424l;
//        Long dateClosed = 12134l;
//        String taskDescription = "taskdescription";
//        String taskDetails = "taskdetails";
//        String taskType = "tasktype";
//        String assignedTo = "{\"name\":\"test\"}";
//        String status = "status";
//        Boolean isActive = true;
//
//        when(rs.next()).thenReturn(true).thenReturn(false);
//        when(rs.getString("tasknumber")).thenReturn(taskNumber);
//        when(rs.getString("id")).thenReturn(taskId.toString());
//        when(rs.getString("orderid")).thenReturn(orderId.toString());
//        when(rs.getString("tenantid")).thenReturn(tenantId);
//        when(rs.getString("filingnumber")).thenReturn(filingNumber);
//        when(rs.getString("cnrnumber")).thenReturn(cnrNumber);
//        when(rs.getLong("createddate")).thenReturn(createdDate);
//        when(rs.getLong("datecloseby")).thenReturn(dateCloseBy);
//        when(rs.getLong("dateclosed")).thenReturn(dateClosed);
//        when(rs.getString("taskdescription")).thenReturn(taskDescription);
//        when(rs.getString("taskdetails")).thenReturn(taskDetails);
//        when(rs.getString("tasktype")).thenReturn(taskType);
//        when(rs.getString("assignedto")).thenReturn(assignedTo);
//        when(rs.getString("status")).thenReturn(status);
//        when(rs.getString("isactive")).thenReturn(isActive.toString());
//        when(rs.getLong("createdtime")).thenReturn(1L);
//        when(rs.getString("createdby")).thenReturn("createdby");
//        when(rs.getLong("lastmodifiedtime")).thenReturn(2L);
//        when(rs.getString("lastmodifiedby")).thenReturn("lastmodifiedby");
//        when(rs.getObject("additionaldetails")).thenReturn(null);
//
//        List<Task> result = taskRowMapper.extractData(rs);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        Task task = result.get(0);
//        assertNotNull(task);
//        assertEquals(taskId, task.getId());
//        assertEquals(orderId, task.getOrderId());
//        assertEquals(tenantId, task.getTenantId());
//        assertEquals(filingNumber, task.getFilingNumber());
//        assertEquals(cnrNumber, task.getCnrNumber());
//        assertEquals(taskDescription, task.getTaskDescription());
//        assertEquals(taskDetails, task.getTaskDetails());
//        assertEquals(taskType, task.getTaskType());
//        assertEquals(status, task.getStatus());
//        assertEquals(isActive, task.getIsActive());
//        assertEquals("createdby", task.getAuditDetails().getCreatedBy());
//        assertEquals(1L, task.getAuditDetails().getCreatedTime());
//        assertEquals("lastmodifiedby", task.getAuditDetails().getLastModifiedBy());
//        assertEquals(2L, task.getAuditDetails().getLastModifiedTime());
//        assertNull(task.getAdditionalDetails());
//    }

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
        when(rs.getString("id")).thenThrow(new CustomException("CUSTOM_ERROR", "Custom error"));

        CustomException exception = assertThrows(CustomException.class, () -> taskRowMapper.extractData(rs));
        assertEquals("CUSTOM_ERROR", exception.getCode());
        assertEquals("Custom error", exception.getMessage());

        verify(rs, times(1)).next();
        verify(rs, times(1)).getString("id");
    }

//    @Test
//    void testExtractDataDateParseException() throws Exception {
//        String taskNumber = "tasknumber";
//        UUID taskId = UUID.randomUUID();
//        UUID orderId = UUID.randomUUID();
//        String tenantId = "tenantid";
//        String filingNumber = "filingnumber";
//        String cnrNumber = "cnrnumber";
//        Long invalidDate = 1234566l;
//        String taskDescription = "taskdescription";
//        String taskDetails = "taskdetails";
//        String taskType = "tasktype";
//        String assignedTo = "{\"name\":\"test\"}";
//        String status = "status";
//        Boolean isActive = true;
//
//        when(rs.next()).thenReturn(true).thenReturn(false);
//        when(rs.getString("tasknumber")).thenReturn(taskNumber);
//        when(rs.getString("id")).thenReturn(taskId.toString());
//        when(rs.getString("orderid")).thenReturn(orderId.toString());
//        when(rs.getString("tenantid")).thenReturn(tenantId);
//        when(rs.getString("filingnumber")).thenReturn(filingNumber);
//        when(rs.getString("cnrnumber")).thenReturn(cnrNumber);
//        when(rs.getLong("createddate")).thenReturn(invalidDate);
//        when(rs.getLong("datecloseby")).thenReturn(invalidDate);
//        when(rs.getLong("dateclosed")).thenReturn(invalidDate);
//        when(rs.getString("taskdescription")).thenReturn(taskDescription);
//        when(rs.getString("taskdetails")).thenReturn(taskDetails);
//        when(rs.getString("tasktype")).thenReturn(taskType);
//        when(rs.getString("assignedto")).thenReturn(assignedTo);
//        when(rs.getString("status")).thenReturn(status);
//        when(rs.getString("isactive")).thenReturn(isActive.toString());
//        when(rs.getLong("createdtime")).thenReturn(1L);
//        when(rs.getString("createdby")).thenReturn("createdby");
//        when(rs.getLong("lastmodifiedtime")).thenReturn(2L);
//        when(rs.getString("lastmodifiedby")).thenReturn("lastmodifiedby");
//
//        PGobject pgObject = new PGobject();
//        pgObject.setType("json");
//        pgObject.setValue("{\"key\":\"value\"}");
//        when(rs.getObject("additionaldetails")).thenReturn(pgObject);
//
//        List<Task> result = taskRowMapper.extractData(rs);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        Task task = result.get(0);
//        assertNotNull(task);
//        assertEquals(taskId, task.getId());
//        assertEquals(orderId, task.getOrderId());
//        assertEquals(tenantId, task.getTenantId());
//        assertEquals(filingNumber, task.getFilingNumber());
//        assertEquals(cnrNumber, task.getCnrNumber());
//        assertNotNull(task.getCreatedDate());
//        assertNotNull(task.getDateCloseBy());
//        assertNotNull(task.getDateClosed());
//        assertEquals(taskDescription, task.getTaskDescription());
//        assertEquals(taskDetails, task.getTaskDetails());
//        assertEquals(taskType, task.getTaskType());
//        assertEquals(status, task.getStatus());
//        assertEquals(isActive, task.getIsActive());
//        assertEquals("createdby", task.getAuditDetails().getCreatedBy());
//        assertEquals(1L, task.getAuditDetails().getCreatedTime());
//        assertEquals("lastmodifiedby", task.getAuditDetails().getLastModifiedBy());
//        assertEquals(2L, task.getAuditDetails().getLastModifiedTime());
//    }

    @Test
    public void testExtractData_CustomException() throws SQLException {

        when(rs.next()).thenThrow(new CustomException("CUSTOM_EXCEPTION", "Custom exception occurred"));

        assertThrows(CustomException.class, () -> taskRowMapper.extractData(rs));
    }

    @Test
    public void testExtractData_DateTimeParseException() throws SQLException {
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("id")).thenReturn("123e4567-e89b-12d3-a456-426614174000");
        when(rs.getString("orderid")).thenReturn("123e4567-e89b-12d3-a456-426614174000");
        when(rs.getString("createdby")).thenReturn("user");
        when(rs.getString("createddate")).thenReturn("invalid-date");

        assertThrows(CustomException.class, () -> taskRowMapper.extractData(rs));
    }
}
