package org.pucar.dristi.repository.rowmapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateClerk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdvocateClerkRowMapperTest {

    @Mock
    private ResultSet rs;

    private AdvocateClerkRowMapper rowMapper;

    @BeforeEach
    void setUp() {
        rowMapper = new AdvocateClerkRowMapper();
    }

    @Test
    void shouldExtractDataSuccessfully() throws SQLException {
        // Arrange
        lenient().when(rs.next()).thenReturn(true).thenReturn(false);
        lenient().when(rs.getString("applicationnumber")).thenReturn("1234");
        lenient().when(rs.getString("tenantid")).thenReturn("tenant1");
        lenient().when(rs.getString("id")).thenReturn(UUID.randomUUID().toString());
        lenient().when(rs.getString("stateregnnumber")).thenReturn("bar123");
        lenient().when(rs.getString("organisationid")).thenReturn(UUID.randomUUID().toString());
        lenient().when(rs.getString("individualid")).thenReturn("ind123");
        lenient().when(rs.getString("isactive")).thenReturn("true");
        lenient().when(rs.getString("advocatetype")).thenReturn("type1");
        lenient().when(rs.wasNull()).thenReturn(false);

        // Act
        List<AdvocateClerk> results = rowMapper.extractData(rs);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        AdvocateClerk result = results.get(0);
        assertEquals("1234", result.getApplicationNumber());
        assertEquals("tenant1", result.getTenantId());
        assertEquals("bar123", result.getStateRegnNumber());
    }
}
