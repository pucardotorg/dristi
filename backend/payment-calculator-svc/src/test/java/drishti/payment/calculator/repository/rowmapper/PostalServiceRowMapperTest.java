package drishti.payment.calculator.repository.rowmapper;

import digit.models.coremodels.AuditDetails;
import drishti.payment.calculator.web.models.PostalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostalServiceRowMapperTest {

    @InjectMocks
    private PostalServiceRowMapper postalServiceRowMapper;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        postalServiceRowMapper = new PostalServiceRowMapper();
        resultSet = mock(ResultSet.class);
    }

    @Test
    void testMapRow() throws SQLException {
        // Arrange
        when(resultSet.getString("postal_hub_id")).thenReturn("hub123");
        when(resultSet.getString("postal_service_id")).thenReturn("service123");
        when(resultSet.getString("pincode")).thenReturn("123456");
        when(resultSet.getDouble("distance_km")).thenReturn(10.5);
        when(resultSet.getString("tenant_id")).thenReturn("tenant123");
        when(resultSet.getString("created_by")).thenReturn("user1");
        when(resultSet.getLong("created_time")).thenReturn(1000L);
        when(resultSet.getString("last_modified_by")).thenReturn("user2");
        when(resultSet.getLong("last_modified_time")).thenReturn(2000L);
        when(resultSet.getInt("row_version")).thenReturn(1);

        // Act
        PostalService postalService = postalServiceRowMapper.mapRow(resultSet, 1);

        // Assert
        assertEquals("hub123", postalService.getPostalHubId());
        assertEquals("service123", postalService.getPostalServiceId());
        assertEquals("123456", postalService.getPincode());
        assertEquals(10.5, postalService.getDistanceKM());
        assertEquals("tenant123", postalService.getTenantId());

        AuditDetails auditDetails = postalService.getAuditDetails();
        assertEquals("user1", auditDetails.getCreatedBy());
        assertEquals(1000L, auditDetails.getCreatedTime());
        assertEquals("user2", auditDetails.getLastModifiedBy());
        assertEquals(2000L, auditDetails.getLastModifiedTime());

        assertEquals(1, postalService.getRowVersion());

        // Verify interactions with the ResultSet
        verify(resultSet).getString("postal_hub_id");
        verify(resultSet).getString("postal_service_id");
        verify(resultSet).getString("pincode");
        verify(resultSet).getDouble("distance_km");
        verify(resultSet).getString("tenant_id");
        verify(resultSet).getString("created_by");
        verify(resultSet).getLong("created_time");
        verify(resultSet).getString("last_modified_by");
        verify(resultSet).getLong("last_modified_time");
        verify(resultSet).getInt("row_version");
    }
}
