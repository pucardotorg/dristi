package drishti.payment.calculator.repository.rowmapper;

import digit.models.coremodels.AuditDetails;
import drishti.payment.calculator.web.models.Address;
import drishti.payment.calculator.web.models.PostalHub;
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
public class PostalHubRowMapperTest {

    @InjectMocks
    private PostalHubRowMapper postalHubRowMapper;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        postalHubRowMapper = new PostalHubRowMapper();
        resultSet = mock(ResultSet.class);
    }

    @Test
    void testMapRow() throws SQLException {
        // Arrange
        when(resultSet.getString("hub_id")).thenReturn("hub123");
        when(resultSet.getString("name")).thenReturn("Postal Hub 1");
        when(resultSet.getString("pincode")).thenReturn("123456");

        when(resultSet.getString("addressid")).thenReturn("address123");
        when(resultSet.getString("tenantid")).thenReturn("tenant123");
        when(resultSet.getString("doorno")).thenReturn("123");
        when(resultSet.getDouble("latitude")).thenReturn(12.34);
        when(resultSet.getDouble("longitude")).thenReturn(56.78);
        when(resultSet.getDouble("locationaccuracy")).thenReturn(1.23);
        when(resultSet.getString("type")).thenReturn("Residential");
        when(resultSet.getString("addressline1")).thenReturn("Line 1");
        when(resultSet.getString("addressline2")).thenReturn("Line 2");
        when(resultSet.getString("landmark")).thenReturn("Landmark");
        when(resultSet.getString("city")).thenReturn("City");
        when(resultSet.getString("pincode")).thenReturn("123456");
        when(resultSet.getString("buildingname")).thenReturn("Building");
        when(resultSet.getString("street")).thenReturn("Street");

        when(resultSet.getString("tenant_id")).thenReturn("tenant123");

        when(resultSet.getString("created_by")).thenReturn("user1");
        when(resultSet.getLong("created_time")).thenReturn(1000L);
        when(resultSet.getString("last_modified_by")).thenReturn("user2");
        when(resultSet.getLong("last_modified_time")).thenReturn(2000L);

        when(resultSet.getInt("row_version")).thenReturn(1);

        // Act
        PostalHub postalHub = postalHubRowMapper.mapRow(resultSet, 1);

        // Assert
        assertEquals("hub123", postalHub.getHubId());
        assertEquals("Postal Hub 1", postalHub.getName());
        assertEquals("123456", postalHub.getPincode());

        Address address = postalHub.getAddress();
        assertEquals("address123", address.getId());
        assertEquals("tenant123", address.getTenantId());
        assertEquals("123", address.getDoorNo());
        assertEquals(12.34, address.getLatitude());
        assertEquals(56.78, address.getLongitude());
        assertEquals(1.23, address.getLocationAccuracy());
        assertEquals("Residential", address.getType());
        assertEquals("Line 1", address.getAddressLine1());
        assertEquals("Line 2", address.getAddressLine2());
        assertEquals("Landmark", address.getLandmark());
        assertEquals("City", address.getCity());
        assertEquals("123456", address.getPincode());
        assertEquals("Building", address.getBuildingName());
        assertEquals("Street", address.getStreet());

        assertEquals("tenant123", postalHub.getTenantId());

        AuditDetails auditDetails = postalHub.getAuditDetails();
        assertEquals("user1", auditDetails.getCreatedBy());
        assertEquals(1000L, auditDetails.getCreatedTime());
        assertEquals("user2", auditDetails.getLastModifiedBy());
        assertEquals(2000L, auditDetails.getLastModifiedTime());

        assertEquals(1, postalHub.getRowVersion());

        // Verify interactions with the ResultSet
        verify(resultSet).getString("hub_id");
        verify(resultSet).getString("name");

        verify(resultSet).getString("addressid");
        verify(resultSet).getString("tenantid");
        verify(resultSet).getString("doorno");
        verify(resultSet).getDouble("latitude");
        verify(resultSet).getDouble("longitude");
        verify(resultSet).getDouble("locationaccuracy");
        verify(resultSet).getString("type");
        verify(resultSet).getString("addressline1");
        verify(resultSet).getString("addressline2");
        verify(resultSet).getString("landmark");
        verify(resultSet).getString("city");
        verify(resultSet).getString("buildingname");
        verify(resultSet).getString("street");

        verify(resultSet).getString("tenant_id");

        verify(resultSet).getString("created_by");
        verify(resultSet).getLong("created_time");
        verify(resultSet).getString("last_modified_by");
        verify(resultSet).getLong("last_modified_time");

        verify(resultSet).getInt("row_version");
    }
}

