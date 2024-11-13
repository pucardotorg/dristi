package digit.repository.rowmapper;

import digit.models.coremodels.AuditDetails;
import digit.web.models.JudgeCalendarRule;
import digit.web.models.enums.JudgeRuleType;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalendarRowMapperTest {

    @InjectMocks
    private CalendarRowMapper mapper;

    @Mock
    private ResultSet resultSet;

    @Test
    public void testMapRow() throws SQLException {
        // Mock data for ResultSet
        when(resultSet.getString("id")).thenReturn("1");
        when(resultSet.getString("judge_id")).thenReturn("J001");
        when(resultSet.getString("rule_type")).thenReturn("LEAVE");
        when(resultSet.getString("date")).thenReturn("1");
        when(resultSet.getString("notes")).thenReturn("Sample note");
        when(resultSet.getString("tenant_id")).thenReturn("T001");
        when(resultSet.getString("created_by")).thenReturn("admin");
        when(resultSet.getLong("created_time")).thenReturn(System.currentTimeMillis());
        when(resultSet.getString("last_modified_by")).thenReturn("admin");
        when(resultSet.getLong("last_modified_time")).thenReturn(System.currentTimeMillis());
        when(resultSet.getInt("row_version")).thenReturn(1);

        // Call mapRow and validate
        JudgeCalendarRule calendarRule = mapper.mapRow(resultSet, 1);

        // Assert the mapped values
        assertEquals("1", calendarRule.getId());
        assertEquals("J001", calendarRule.getJudgeId());
        assertEquals(JudgeRuleType.LEAVE.toString(), calendarRule.getRuleType());
        assertEquals("Sample note", calendarRule.getNotes());
        assertEquals("T001", calendarRule.getTenantId());

        AuditDetails auditDetails = calendarRule.getAuditDetails();
        assertEquals("admin", auditDetails.getCreatedBy());
        assertEquals("admin", auditDetails.getLastModifiedBy());
        // You may need to adjust time comparison based on how you handle timestamps in your application
        assertEquals(resultSet.getLong("created_time"), auditDetails.getCreatedTime());
        assertEquals(resultSet.getLong("last_modified_time"), auditDetails.getLastModifiedTime());

        assertEquals(1, calendarRule.getRowVersion());
    }

}
