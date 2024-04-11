package org.pucar.repository.rowmapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateClerk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdvocateRowMapperTest {

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private AdvocateClerkRowMapper rowMapper;

    @Test
    public void testExtractData() throws SQLException {
        // Mocking ResultSet data
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("aapplicationnumber")).thenReturn("123");
        when(resultSet.getLong("alastmodifiedtime")).thenReturn(123L);
        when(resultSet.getString("acreatedby")).thenReturn("John Doe");
        when(resultSet.getLong("acreatedtime")).thenReturn(456L);
        when(resultSet.getString("alastmodifiedby")).thenReturn("Jane Doe");
        when(resultSet.getString("aid")).thenReturn("250ecda9-7676-4677-aeb7-13679cbdc1aa");
        when(resultSet.getString("atenantid")).thenReturn("tenant-123");
        when(resultSet.getString("stateregnnumber")).thenReturn("BAR-123");
        when(resultSet.getBoolean("aisactive")).thenReturn(true);

        List<AdvocateClerk> advocates = rowMapper.extractData(resultSet);

        // Assertions
        assertEquals(1, advocates.size());
        AdvocateClerk advocate = advocates.get(0);
        assertEquals("123", advocate.getApplicationNumber());
        assertEquals("tenant-123", advocate.getTenantId());
        assertEquals("250ecda9-7676-4677-aeb7-13679cbdc1aa", advocate.getId().toString());
        assertEquals(true, advocate.getIsActive());
        assertEquals("John Doe", advocate.getAuditDetails().getCreatedBy());
        assertEquals(456L, advocate.getAuditDetails().getCreatedTime());
        assertEquals("Jane Doe", advocate.getAuditDetails().getLastModifiedBy());
        assertEquals(123L, advocate.getAuditDetails().getLastModifiedTime());
    }
}
