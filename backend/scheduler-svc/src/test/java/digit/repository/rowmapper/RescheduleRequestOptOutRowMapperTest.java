package digit.repository.rowmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.models.coremodels.AuditDetails;
import digit.web.models.OptOut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RescheduleRequestOptOutRowMapperTest {

    @InjectMocks
    private RescheduleRequestOptOutRowMapper mapper;

    @Mock
    private ResultSet resultSet;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    public void testMapRow() throws SQLException, JsonProcessingException {

        when(resultSet.getString("judge_id")).thenReturn("J001");
        when(resultSet.getString("id")).thenReturn("id");
        when(resultSet.getString("case_id")).thenReturn("CASE001");
        when(resultSet.getString("reschedule_request_id")).thenReturn("RR001");
        when(resultSet.getString("individual_id")).thenReturn("IND001");
        when(resultSet.getInt("row_version")).thenReturn(1);
        when(resultSet.getLong("created_time")).thenReturn(System.currentTimeMillis());
        when(resultSet.getLong("last_modified_time")).thenReturn(System.currentTimeMillis());

        OptOut optOut = mapper.mapRow(resultSet, 1);

        assertEquals("J001", optOut.getJudgeId());
        assertEquals("CASE001", optOut.getCaseId());
        assertEquals("RR001", optOut.getRescheduleRequestId());
        assertEquals("IND001", optOut.getIndividualId());

        // Verify AuditDetails
        AuditDetails auditDetails = optOut.getAuditDetails();
        // You may need to adjust time comparison based on how you handle timestamps in your application
        assertEquals(resultSet.getLong("created_time"), auditDetails.getCreatedTime());
        assertEquals(resultSet.getLong("last_modified_time"), auditDetails.getLastModifiedTime());

        assertEquals(1, optOut.getRowVersion());
    }

    @Test
    public void testMapRowWithNullValues() throws SQLException, JsonProcessingException {
        when(resultSet.getString("judge_id")).thenReturn(null);
        when(resultSet.getString("id")).thenReturn("id");
        when(resultSet.getString("case_id")).thenReturn("CASE002");
        when(resultSet.getString("reschedule_request_id")).thenReturn("RR002");
        when(resultSet.getString("individual_id")).thenReturn("IND002");
        when(resultSet.getString("opt_out_dates")).thenReturn(null);
        when(resultSet.getInt("row_version")).thenReturn(2);
        when(resultSet.getLong("created_time")).thenReturn(System.currentTimeMillis());
        when(resultSet.getLong("last_modified_time")).thenReturn(System.currentTimeMillis());

        OptOut optOut = mapper.mapRow(resultSet, 1);

        assertEquals(null, optOut.getJudgeId()); // Expected to be null
        assertEquals("CASE002", optOut.getCaseId());
        assertEquals("RR002", optOut.getRescheduleRequestId());
        assertEquals("IND002", optOut.getIndividualId());
        assertEquals(null, optOut.getOptoutDates());
        assertEquals(2, optOut.getRowVersion());
    }

    @Test
    public void testMapRowWithJsonProcessingException() throws SQLException {
        // Mock data for ResultSet
        when(resultSet.getString("judge_id")).thenReturn("J003");
        when(resultSet.getString("case_id")).thenReturn("CASE003");
        when(resultSet.getString("reschedule_request_id")).thenReturn("RR003");
        when(resultSet.getString("individual_id")).thenReturn("IND003");
        when(resultSet.getString("opt_out_dates")).thenReturn("[\"2024-07-07\",\"2024-07-08\"]");
        when(resultSet.getInt("row_version")).thenReturn(3);
        when(resultSet.getLong("created_time")).thenReturn(System.currentTimeMillis());
        when(resultSet.getLong("last_modified_time")).thenReturn(System.currentTimeMillis());
        when(resultSet.getString("tenant_id")).thenReturn("T003");

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> mapper.mapRow(resultSet, 1)
        );
    }
}