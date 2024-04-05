package org.pucar.repository.rowmapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.web.models.Advocate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdvocateRegistrationRowMapperTest {

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private AdvocateRegistrationRowMapper rowMapper;

    @Test
    public void testExtractData() throws SQLException {
        // Mocking ResultSet data
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("aapplicationnumber")).thenReturn("123");
        when(resultSet.getLong("alastModifiedTime")).thenReturn(123L);
        when(resultSet.getString("acreatedBy")).thenReturn("John Doe");
        when(resultSet.getLong("acreatedTime")).thenReturn(456L);
        when(resultSet.getString("alastModifiedBy")).thenReturn("Jane Doe");
        when(resultSet.getString("aid")).thenReturn("abc-123");
        when(resultSet.getString("atenantid")).thenReturn("tenant-123");
        when(resultSet.getString("abarRegistrationNumber")).thenReturn("BAR-123");
        when(resultSet.getString("aorganisationID")).thenReturn("org-123");
        when(resultSet.getString("aindividualId")).thenReturn("ind-123");
        when(resultSet.getString("aisActive")).thenReturn("true");
        when(resultSet.getString("aadditionalDetails")).thenReturn("Additional Details");

        // Call the method to be tested
        List<Advocate> advocates = rowMapper.extractData(resultSet);

        // Assertions
        assertEquals(1, advocates.size());
        Advocate advocate = advocates.get(0);
        assertEquals("123", advocate.getApplicationNumber());
        assertEquals("tenant-123", advocate.getTenantId());
        assertEquals("abc-123", advocate.getId().toString());
        assertEquals("BAR-123", advocate.getBarRegistrationNumber());
        assertEquals("org-123", advocate.getOrganisationID().toString());
        assertEquals("ind-123", advocate.getIndividualId());
        assertEquals(true, advocate.getIsActive());
        assertEquals("Additional Details", advocate.getAdditionalDetails());
        assertEquals("John Doe", advocate.getAuditDetails().getCreatedBy());
        assertEquals(456L, advocate.getAuditDetails().getCreatedTime());
        assertEquals("Jane Doe", advocate.getAuditDetails().getLastModifiedBy());
        assertEquals(123L, advocate.getAuditDetails().getLastModifiedTime());
    }
}
