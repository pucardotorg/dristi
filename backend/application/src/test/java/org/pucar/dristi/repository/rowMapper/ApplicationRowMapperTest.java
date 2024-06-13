package org.pucar.dristi.repository.rowMapper;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pucar.dristi.web.models.Application;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ApplicationRowMapperTest {
    private ResultSet resultSet;
    private ApplicationRowMapper applicationRowMapper;

    @BeforeEach
    public void setUp() {
        resultSet = Mockito.mock(ResultSet.class);
        applicationRowMapper = new ApplicationRowMapper();
    }
    @Test
    public void testExtractData() throws SQLException {
        // Mocking the ResultSet
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("id")).thenReturn("123e4567-e89b-12d3-a456-556642440000", "123e4567-e89b-12d3-a456-556642440001");
        when(resultSet.getLong("lastmodifiedtime")).thenReturn(123456789L, 987654321L);
        when(resultSet.getString("createdby")).thenReturn("John", "Alice");
        when(resultSet.getLong("createdtime")).thenReturn(123456789L, 987654321L);
        when(resultSet.getString("lastmodifiedby")).thenReturn("Doe", "Smith");
        when(resultSet.getString("applicationnumber")).thenReturn("APP-001", "APP-002");
        when(resultSet.getString("cnrnumber")).thenReturn("CN-001", "CN-002");
        when(resultSet.getString("filingnumber")).thenReturn("F-001", "F-002");
        when(resultSet.getString("caseid")).thenReturn("Case-001", "Case-002");
        when(resultSet.getString("referenceid")).thenReturn("123e4567-e89b-12d3-a456-556642440000", "123e4567-e89b-12d3-a456-556642440001");
        when(resultSet.getString("createddate")).thenReturn("2024-05-27", "2024-05-28");
        when(resultSet.getString("applicationcreatedby")).thenReturn("123e4567-e89b-12d3-a456-556642440002", "123e4567-e89b-12d3-a456-556642440003");
        when(resultSet.getString("tenantid")).thenReturn("tenant1", "tenant2");
        when(resultSet.getBoolean("isactive")).thenReturn(true, false);
        when(resultSet.getString("status")).thenReturn("Pending", "Approved");
        when(resultSet.getString("comment")).thenReturn("Test comment 1", "Test comment 2");
        when(resultSet.getString("additionaldetails")).thenReturn("Details 1", "Details 2");

        // Creating ApplicationRowMapper instance
        ApplicationRowMapper rowMapper = new ApplicationRowMapper();

        // Invoking the extractData method
        List<Application> applications = rowMapper.extractData(resultSet);

        // Assertions
        Assertions.assertEquals(2, applications.size());
        Assertions.assertEquals("APP-001", applications.get(0).getApplicationNumber());
        Assertions.assertEquals("CN-002", applications.get(1).getCnrNumber());
    }
    @Test
    public void testExtractDataWithSQLException() throws SQLException {
        // Simulate SQLException
        when(resultSet.next()).thenThrow(new SQLException("SQL error"));

        assertThrows(CustomException.class, () -> {
            applicationRowMapper.extractData(resultSet);
        });
    }

    @Test
    public void testExtractDataWithRuntimeException() throws SQLException {
        // Simulate RuntimeException (e.g., NullPointerException)
        when(resultSet.next()).thenThrow(new RuntimeException("Runtime error"));

        assertThrows(CustomException.class, () -> {
            applicationRowMapper.extractData(resultSet);
        });
    }
}
