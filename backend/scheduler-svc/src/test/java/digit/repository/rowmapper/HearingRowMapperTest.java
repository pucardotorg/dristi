package digit.repository.rowmapper;

import digit.models.coremodels.AuditDetails;
import digit.web.models.ScheduleHearing;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HearingRowMapperTest {

    @InjectMocks
    private HearingRowMapper mapper;

    @Mock
    private ResultSet resultSet;

    @Test
    public void testMapRow() throws SQLException {
        // Mock data for ResultSet
        when(resultSet.getString("description")).thenReturn("Sample hearing description");
        when(resultSet.getString("hearing_booking_id")).thenReturn("HB001");
        when(resultSet.getString("tenant_id")).thenReturn("T001");
        when(resultSet.getString("court_id")).thenReturn("C001");
        when(resultSet.getString("judge_id")).thenReturn("J001");
        when(resultSet.getString("case_id")).thenReturn("CASE001");
        when(resultSet.getString("hearing_type")).thenReturn("ADMISSION");
        when(resultSet.getLong("hearing_date")).thenReturn(System.currentTimeMillis());
        when(resultSet.getString("title")).thenReturn("Hearing Title");
        when(resultSet.getString("status")).thenReturn("SCHEDULED");
        when(resultSet.getString("start_time")).thenReturn("1");
        when(resultSet.getString("end_time")).thenReturn("2");
        when(resultSet.getString("reschedule_request_id")).thenReturn("R001");
        when(resultSet.getString("created_by")).thenReturn("admin");
        when(resultSet.getLong("created_time")).thenReturn(System.currentTimeMillis());
        when(resultSet.getString("last_modified_by")).thenReturn("admin");
        when(resultSet.getLong("last_modified_time")).thenReturn(System.currentTimeMillis());
        when(resultSet.getInt("row_version")).thenReturn(1);

        // Call mapRow and validate
        ScheduleHearing hearing = mapper.mapRow(resultSet, 1);

        // Assert the mapped values

        assertEquals("Sample hearing description", hearing.getDescription());
        assertEquals("HB001", hearing.getHearingBookingId());
        assertEquals("T001", hearing.getTenantId());
        assertEquals("C001", hearing.getCourtId());
        assertEquals("J001", hearing.getJudgeId());
        assertEquals("CASE001", hearing.getCaseId());
        assertEquals("ADMISSION", hearing.getHearingType());
        assertEquals("Hearing Title", hearing.getTitle());
        assertEquals("SCHEDULED", hearing.getStatus());

        // Verify LocalDateTime parsing
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Verify AuditDetails
        AuditDetails auditDetails = hearing.getAuditDetails();
        assertEquals("admin", auditDetails.getCreatedBy());
        assertEquals("admin", auditDetails.getLastModifiedBy());
        assertEquals(resultSet.getLong("created_time"), auditDetails.getCreatedTime());
        assertEquals(resultSet.getLong("last_modified_time"), auditDetails.getLastModifiedTime());

        assertEquals(1, hearing.getRowVersion());
    }

}