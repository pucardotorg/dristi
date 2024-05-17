package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.pucar.dristi.web.models.Witness;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

 class WitnessRowMapperTest {

    @InjectMocks
    private WitnessRowMapper witnessRowMapper;

    @BeforeEach
    public void setUp() {
        witnessRowMapper = new WitnessRowMapper();
    }

    @Test
     void testExtractData_Success() throws SQLException {
        // Arrange
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("id")).thenReturn(UUID.randomUUID().toString());
        when(resultSet.getString("caseid")).thenReturn("case123");
        when(resultSet.getString("filingnumber")).thenReturn("file123");
        when(resultSet.getString("cnrnumber")).thenReturn("cnr123");
        when(resultSet.getString("individualid")).thenReturn("individual123");
        when(resultSet.getString("witnessidentifier")).thenReturn("witness123");
        when(resultSet.getString("remarks")).thenReturn("remark123");
        when(resultSet.getBoolean("isactive")).thenReturn(true);
        when(resultSet.getString("createdby")).thenReturn("user123");
        when(resultSet.getLong("createdtime")).thenReturn(System.currentTimeMillis());
        when(resultSet.getString("lastmodifiedby")).thenReturn("user456");
        when(resultSet.getLong("lastmodifiedtime")).thenReturn(System.currentTimeMillis());

        // Mocking additionalDetails as PGObject
        ObjectMapper objectMapper = new ObjectMapper();
        String additionalDetailsJson = "{\"key\": \"value\"}";
        when(resultSet.getObject("additionalDetails")).thenReturn(null); // Mocking as null for simplicity

        // Act
        List<Witness> witnesses = witnessRowMapper.extractData(resultSet);

        // Assert
        assertEquals(1, witnesses.size());
        Witness witness = witnesses.get(0);
        assertEquals("case123", witness.getCaseId());
        assertEquals("file123", witness.getFilingNumber());
        assertEquals("cnr123", witness.getCnrNumber());
        assertEquals("individual123", witness.getIndividualId());
        assertEquals("witness123", witness.getWitnessIdentifier());
        assertEquals("remark123", witness.getRemarks());
        assertEquals(true, witness.getIsActive());
        assertEquals("user123", witness.getAuditDetails().getCreatedBy());
        assertEquals("user456", witness.getAuditDetails().getLastModifiedBy());
        // Test that additionalDetails is not set (as we mocked it to null)
        assertEquals(null, witness.getAdditionalDetails());
    }

    // Add more test cases as necessary
}
