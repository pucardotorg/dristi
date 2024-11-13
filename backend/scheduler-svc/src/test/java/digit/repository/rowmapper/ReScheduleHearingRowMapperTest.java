package digit.repository.rowmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.web.models.ReScheduleHearing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReScheduleHearingRowMapperTest {

    @InjectMocks
    private ReScheduleHearingRowMapper reScheduleHearingRowMapper;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testMapRow_withAllFieldsPresent() throws Exception {
        // Mocking values in ResultSet
        when(resultSet.getString("reschedule_request_id")).thenReturn("123");
        when(resultSet.getString("hearing_booking_id")).thenReturn("456");
        when(resultSet.getString("tenant_id")).thenReturn("tenant");
        when(resultSet.getString("judge_id")).thenReturn("judge");
        when(resultSet.getString("case_id")).thenReturn("case");
        when(resultSet.getString("requester_id")).thenReturn("requester");
        when(resultSet.getString("reason")).thenReturn("reason");
        when(resultSet.getString("status")).thenReturn("APPROVED");
        when(resultSet.getString("suggested_days")).thenReturn("[\"2023-07-01\",\"2023-07-02\"]");
        when(resultSet.getString("available_days")).thenReturn("[\"2023-07-03\",\"2023-07-04\"]");
        when(resultSet.getString("created_by")).thenReturn("creator");
        when(resultSet.getLong("created_time")).thenReturn(123456789L);
        when(resultSet.getString("last_modified_by")).thenReturn("modifier");
        when(resultSet.getLong("last_modified_time")).thenReturn(987654321L);
        when(resultSet.getInt("row_version")).thenReturn(1);

        // Mocking objectMapper to return parsed data
        List<LocalDate> suggestedDays = List.of(LocalDate.parse("2023-07-01"), LocalDate.parse("2023-07-02"));
        List<LocalDate> availableDays = List.of(LocalDate.parse("2023-07-03"), LocalDate.parse("2023-07-04"));
        when(objectMapper.readValue("[\"2023-07-01\",\"2023-07-02\"]", new TypeReference<List<LocalDate>>() {})).thenReturn(suggestedDays);
        when(objectMapper.readValue("[\"2023-07-03\",\"2023-07-04\"]", new TypeReference<List<LocalDate>>() {})).thenReturn(availableDays);

        // Act
        ReScheduleHearing reScheduleHearing = reScheduleHearingRowMapper.mapRow(resultSet, 1);

        // Assert
        assertNotNull(reScheduleHearing);
        assertEquals("123", reScheduleHearing.getRescheduledRequestId());
        assertEquals("456", reScheduleHearing.getHearingBookingId());
        assertEquals("tenant", reScheduleHearing.getTenantId());
        assertEquals("judge", reScheduleHearing.getJudgeId());
        assertEquals("reason", reScheduleHearing.getReason());
        assertEquals(1, reScheduleHearing.getRowVersion());
    }

    @Test
    public void testMapRow_withNullStatus() throws Exception {
        // Mocking ResultSet for the case with null status
        when(resultSet.getString("reschedule_request_id")).thenReturn("123");
        when(resultSet.getString("status")).thenReturn(null);

        // Act
        ReScheduleHearing reScheduleHearing = reScheduleHearingRowMapper.mapRow(resultSet, 1);

        // Assert
        assertNotNull(reScheduleHearing);
        assertNull(reScheduleHearing.getStatus());
    }

    @Test
    public void testMapRow_withNullSuggestedDaysAndAvailableDays() throws Exception {
        // Mocking ResultSet for null values in suggested and available days
        when(resultSet.getString("reschedule_request_id")).thenReturn("123");
        when(resultSet.getString("suggested_days")).thenReturn(null);
        when(resultSet.getString("available_days")).thenReturn(null);

        // Act
        ReScheduleHearing reScheduleHearing = reScheduleHearingRowMapper.mapRow(resultSet, 1);

        // Assert
        assertNotNull(reScheduleHearing);
        assertNull(reScheduleHearing.getSuggestedDates());
        assertNull(reScheduleHearing.getAvailableDates());
    }

    @Test
    public void testMapRow_withNullLitigantsAndRepresentatives() throws Exception {
        // Mocking ResultSet for null values in litigants and representatives
        when(resultSet.getString("reschedule_request_id")).thenReturn("123");
        when(resultSet.getString("litigants")).thenReturn(null);
        when(resultSet.getString("representatives")).thenReturn(null);

        // Act
        ReScheduleHearing reScheduleHearing = reScheduleHearingRowMapper.mapRow(resultSet, 1);

        // Assert
        assertNotNull(reScheduleHearing);
        assertNull(reScheduleHearing.getLitigants());
        assertNull(reScheduleHearing.getRepresentatives());
    }

    @Test
    public void testMapRow_withJsonProcessingException() throws Exception {
        // Mocking ResultSet
        when(resultSet.getString("reschedule_request_id")).thenReturn("123");
        when(resultSet.getString("suggested_days")).thenReturn("[\"2023-07-01\",\"2023-07-02\"]");

        // Simulating a JsonProcessingException in objectMapper
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(new JsonProcessingException("Test exception") {});

        // Act and Assert: Expect a RuntimeException to be thrown
        assertThrows(RuntimeException.class, () -> {
            reScheduleHearingRowMapper.mapRow(resultSet, 1);
        });
    }

    @Test
    public void testMapRow_withSQLException() throws SQLException {
        // Mock ResultSet to throw SQLException when retrieving a value
        when(resultSet.getString(anyString())).thenThrow(new SQLException("SQL error"));

        // Act and Assert: Expect a SQLException to be thrown
        assertThrows(SQLException.class, () -> {
            reScheduleHearingRowMapper.mapRow(resultSet, 1);
        });
    }
}
