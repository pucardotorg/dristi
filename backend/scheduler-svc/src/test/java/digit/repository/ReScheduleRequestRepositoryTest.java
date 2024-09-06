package digit.repository;

import digit.repository.querybuilder.ReScheduleHearingQueryBuilder;
import digit.repository.rowmapper.ReScheduleHearingRowMapper;
import digit.web.models.ReScheduleHearing;
import digit.web.models.ReScheduleHearingReqSearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReScheduleRequestRepositoryTest {

    @Mock
    private ReScheduleHearingRowMapper rowMapper;

    @Mock
    private ReScheduleHearingQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ReScheduleRequestRepository reScheduleRequestRepository;

    private ReScheduleHearingReqSearchCriteria criteria;
    private List<ReScheduleHearing> reScheduleHearings;

    @BeforeEach
    public void setUp() {
        criteria = new ReScheduleHearingReqSearchCriteria();
        reScheduleHearings = new ArrayList<>();
        ReScheduleHearing reScheduleHearing = new ReScheduleHearing();
        reScheduleHearings.add(reScheduleHearing);
    }

    @Test
    public void testGetReScheduleRequest_Success() {
        List<Object> preparedStmtList = new ArrayList<>();
        when(queryBuilder.getReScheduleRequestQuery(any(ReScheduleHearingReqSearchCriteria.class), any(List.class), anyInt(), anyInt())).thenReturn("SELECT * FROM reschedule_requests");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(ReScheduleHearingRowMapper.class))).thenReturn(reScheduleHearings);

        List<ReScheduleHearing> result = reScheduleRequestRepository.getReScheduleRequest(criteria, 10, 0);

        assertEquals(1, result.size());
        verify(queryBuilder, times(1)).getReScheduleRequestQuery(any(ReScheduleHearingReqSearchCriteria.class), any(List.class), anyInt(), anyInt());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(ReScheduleHearingRowMapper.class));
    }

    @Test
    public void testGetReScheduleRequest_EmptyResult() {
        List<Object> preparedStmtList = new ArrayList<>();
        when(queryBuilder.getReScheduleRequestQuery(any(ReScheduleHearingReqSearchCriteria.class), any(List.class), anyInt(), anyInt())).thenReturn("SELECT * FROM reschedule_requests");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(ReScheduleHearingRowMapper.class))).thenReturn(Collections.emptyList());

        List<ReScheduleHearing> result = reScheduleRequestRepository.getReScheduleRequest(criteria, 10, 0);

        assertEquals(0, result.size());
        verify(queryBuilder, times(1)).getReScheduleRequestQuery(any(ReScheduleHearingReqSearchCriteria.class), any(List.class), anyInt(), anyInt());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(ReScheduleHearingRowMapper.class));
    }

    @Test
    public void testGetReScheduleRequest_Exception() {
        List<Object> preparedStmtList = new ArrayList<>();
        when(queryBuilder.getReScheduleRequestQuery(any(ReScheduleHearingReqSearchCriteria.class), any(List.class), anyInt(), anyInt())).thenReturn("SELECT * FROM reschedule_requests");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(ReScheduleHearingRowMapper.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reScheduleRequestRepository.getReScheduleRequest(criteria, 10, 0);
        });

        assertEquals("Database error", exception.getMessage());
        verify(queryBuilder, times(1)).getReScheduleRequestQuery(any(ReScheduleHearingReqSearchCriteria.class), any(List.class), anyInt(), anyInt());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(ReScheduleHearingRowMapper.class));
    }
}
