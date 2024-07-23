package org.pucar.dristi.repository.rowmapper;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.Advocate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AdvocateRowMapperTest {

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
//        assertTrue(exception.getMessage().contains("Error occurred while processing document ResultSet: Database error"));
    }
}
