package org.pucar.dristi.repository.rowMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pucar.dristi.web.models.Application;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ApplicationRowMapperTest {

    @Test
    public void testExtractData() throws SQLException {
        // Mocking the ResultSet
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(true, true, false);
        Mockito.when(resultSet.getString("id")).thenReturn("123e4567-e89b-12d3-a456-556642440000", "123e4567-e89b-12d3-a456-556642440001");
        Mockito.when(resultSet.getLong("lastmodifiedtime")).thenReturn(123456789L, 987654321L);
        Mockito.when(resultSet.getString("createdby")).thenReturn("John", "Alice");
        Mockito.when(resultSet.getLong("createdtime")).thenReturn(123456789L, 987654321L);
        Mockito.when(resultSet.getString("lastmodifiedby")).thenReturn("Doe", "Smith");
        Mockito.when(resultSet.getString("applicationnumber")).thenReturn("APP-001", "APP-002");
        Mockito.when(resultSet.getString("cnrnumber")).thenReturn("CN-001", "CN-002");
        Mockito.when(resultSet.getString("filingnumber")).thenReturn("F-001", "F-002");
        Mockito.when(resultSet.getString("referenceid")).thenReturn("123e4567-e89b-12d3-a456-556642440000", "123e4567-e89b-12d3-a456-556642440001");
        Mockito.when(resultSet.getString("createddate")).thenReturn("2024-05-27", "2024-05-28");
        Mockito.when(resultSet.getString("applicationcreatedby")).thenReturn("123e4567-e89b-12d3-a456-556642440002", "123e4567-e89b-12d3-a456-556642440003");
        Mockito.when(resultSet.getString("tenantid")).thenReturn("tenant1", "tenant2");
        Mockito.when(resultSet.getBoolean("isactive")).thenReturn(true, false);
        Mockito.when(resultSet.getString("status")).thenReturn("Pending", "Approved");
        Mockito.when(resultSet.getString("comment")).thenReturn("Test comment 1", "Test comment 2");
        Mockito.when(resultSet.getString("additionaldetails")).thenReturn("Details 1", "Details 2");

        // Creating ApplicationRowMapper instance
        ApplicationRowMapper rowMapper = new ApplicationRowMapper();

        // Invoking the extractData method
        List<Application> applications = rowMapper.extractData(resultSet);

        // Assertions
        Assertions.assertEquals(2, applications.size());
        Assertions.assertEquals("APP-001", applications.get(0).getApplicationNumber());
        Assertions.assertEquals("CN-002", applications.get(1).getCnrNumber());
    }

}
