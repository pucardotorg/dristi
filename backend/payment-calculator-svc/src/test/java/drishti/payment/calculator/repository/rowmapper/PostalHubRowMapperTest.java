package drishti.payment.calculator.repository.rowmapper;

import digit.models.coremodels.AuditDetails;
import drishti.payment.calculator.web.models.PostalHub;
import drishti.payment.calculator.web.models.enums.Classification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostalHubRowMapperTest {

    @InjectMocks
    private PostalHubRowMapper postalHubRowMapper;

    @Mock
    private ResultSet rs;

    @BeforeEach
    void setUp() {
        postalHubRowMapper = new PostalHubRowMapper();
        rs = mock(ResultSet.class);
    }

    @Test
    void testMapRow() throws SQLException {
        // Arrange
        when(rs.getString("hub_id")).thenReturn("HUB123");
        when(rs.getString("name")).thenReturn("Main Hub");
        when(rs.getString("pincode")).thenReturn("123456");
        when(rs.getString("classification")).thenReturn("ltd");
        when(rs.getString("tenant_id")).thenReturn("TENANT001");
        when(rs.getString("created_by")).thenReturn("admin");
        when(rs.getLong("created_time")).thenReturn(123456789L);
        when(rs.getString("last_modified_by")).thenReturn("admin");
        when(rs.getLong("last_modified_time")).thenReturn(123456790L);
        when(rs.getInt("row_version")).thenReturn(1);

        // Act
        PostalHub result = postalHubRowMapper.mapRow(rs, 1);

        // Assert
        assertEquals("HUB123", result.getHubId());
        assertEquals("Main Hub", result.getName());
        assertEquals("123456", result.getPincode());
        assertEquals(Classification.fromValue("ltd"), result.getClassification());
        assertEquals("TENANT001", result.getTenantId());

        AuditDetails auditDetails = result.getAuditDetails();
        assertEquals("admin", auditDetails.getCreatedBy());
        assertEquals(123456789L, auditDetails.getCreatedTime());
        assertEquals("admin", auditDetails.getLastModifiedBy());
        assertEquals(123456790L, auditDetails.getLastModifiedTime());

        assertEquals(1, result.getRowVersion());
    }
}

