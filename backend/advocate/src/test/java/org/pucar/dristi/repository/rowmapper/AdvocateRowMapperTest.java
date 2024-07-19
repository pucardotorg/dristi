package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.JsonNode;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Advocate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class AdvocateRowMapperTest {

    @Mock
    private ResultSet rs;

    private AdvocateRowMapper rowMapper;

    @BeforeEach
    void setUp() {
        rowMapper = new AdvocateRowMapper();
    }

    @Test
    void shouldExtractDataSuccessfully() throws SQLException {
        // Arrange
        lenient().when(rs.next()).thenReturn(true).thenReturn(false);
        lenient().when(rs.getString("applicationnumber")).thenReturn("1234");
        lenient().when(rs.getString("tenantid")).thenReturn("tenant1");
        lenient().when(rs.getString("id")).thenReturn(UUID.randomUUID().toString());
        lenient().when(rs.getString("barregistrationnumber")).thenReturn("bar123");
        lenient().when(rs.getString("organisationid")).thenReturn(UUID.randomUUID().toString());
        lenient().when(rs.getString("individualid")).thenReturn("ind123");
        lenient().when(rs.getString("isactive")).thenReturn("true");
        lenient().when(rs.getString("advocatetype")).thenReturn("type1");
        lenient().when(rs.wasNull()).thenReturn(false);

        // Act
        List<Advocate> results = rowMapper.extractData(rs);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        Advocate result = results.get(0);
        assertEquals("1234", result.getApplicationNumber());
        assertEquals("tenant1", result.getTenantId());
        assertEquals("bar123", result.getBarRegistrationNumber());
    }

    @Test
    void shouldHandleExceptionGracefully() throws SQLException {
        // Arrange
        when(rs.next()).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(CustomException.class, () -> rowMapper.extractData(rs));
    }

    @Test
    void shouldHandleException() throws SQLException {
        // Setup
        when(rs.next()).thenThrow(new CustomException());

        // Execution & Verification
        assertThrows(CustomException.class, () -> rowMapper.extractData(rs));
    }

    @Test
     void testExtractData_withLastModifiedTimeNull() throws SQLException {
        // Mock ResultSet
        ResultSet resultSetMock = Mockito.mock(ResultSet.class);
        when(resultSetMock.next()).thenReturn(true, false); // simulate one row
        when(resultSetMock.getString("applicationnumber")).thenReturn("sample_uuid");
        when(resultSetMock.getString("createdby")).thenReturn("user");
        when(resultSetMock.getLong("createdtime")).thenReturn(123456789L);
        when(resultSetMock.getString("lastmodifiedby")).thenReturn("admin");
        when(resultSetMock.getLong("lastmodifiedtime")).thenReturn(0L); // simulate wasNull condition
        when(resultSetMock.getString("id")).thenReturn(UUID.randomUUID().toString());
        when(resultSetMock.getString("tenantid")).thenReturn("tenant");
        when(resultSetMock.getString("barregistrationnumber")).thenReturn("bar123");
        when(resultSetMock.getString("organisationid")).thenReturn(UUID.randomUUID().toString());
        when(resultSetMock.getString("individualid")).thenReturn("individual123");
        when(resultSetMock.getBoolean("isactive")).thenReturn(true);
        when(resultSetMock.getString("advocatetype")).thenReturn("type");
        when(resultSetMock.getString("status")).thenReturn("active");
        when(resultSetMock.getObject("additionalDetails")).thenReturn(null); // simulate additionalDetails as null


        // Call extractData method
        List<Advocate> advocates = rowMapper.extractData(resultSetMock);

        // Assertions
        assertEquals(1, advocates.size());
        assertEquals(0, advocates.get(0).getAuditDetails().getLastModifiedTime()); // Verify lastModifiedTime is null
    }

    @Test
    void testExtractData_withAdditionalDetails() throws SQLException {
        // Mock ResultSet
        ResultSet resultSetMock = Mockito.mock(ResultSet.class);
        when(resultSetMock.next()).thenReturn(true, false); // simulate one row
        when(resultSetMock.getString("applicationnumber")).thenReturn("sample_uuid");
        when(resultSetMock.getString("createdby")).thenReturn("user");
        when(resultSetMock.getLong("createdtime")).thenReturn(123456789L);
        when(resultSetMock.getString("lastmodifiedby")).thenReturn("admin");
        when(resultSetMock.getLong("lastmodifiedtime")).thenReturn(123456789L);
        when(resultSetMock.getString("id")).thenReturn(UUID.randomUUID().toString());
        when(resultSetMock.getString("tenantid")).thenReturn("tenant");
        when(resultSetMock.getString("barregistrationnumber")).thenReturn("bar123");
        when(resultSetMock.getString("organisationid")).thenReturn(UUID.randomUUID().toString());
        when(resultSetMock.getString("individualid")).thenReturn("individual123");
        when(resultSetMock.getBoolean("isactive")).thenReturn(true);
        when(resultSetMock.getString("advocatetype")).thenReturn("type");
        when(resultSetMock.getString("status")).thenReturn("active");

        // Mock PGObject
        PGobject pgObjectMock = Mockito.mock(PGobject.class);
        when(pgObjectMock.getValue()).thenReturn("{}"); // JSON string
        when(resultSetMock.getObject("additionalDetails")).thenReturn(pgObjectMock);


        // Call extractData method
        List<Advocate> advocates = rowMapper.extractData(resultSetMock);

        // Assertions
        assertEquals(1, advocates.size());
        JsonNode additionalDetails = (JsonNode) advocates.get(0).getAdditionalDetails();
        assertEquals("{}", additionalDetails.toString()); // Verify additionalDetails is set correctly
    }

    @Test
    void testToUUID_withNullInput() {

        // Call toUUID method
        UUID result = rowMapper.toUUID(null);

        // Assertion
        assertEquals(null, result); // Verify result is null
    }
}
