package digit.repository;

import digit.repository.querybuilder.CalendarQueryBuilder;
import digit.repository.rowmapper.CalendarRowMapper;
import digit.web.models.JudgeCalendarRule;
import digit.web.models.SearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalendarRepositoryTest {

    @Mock
    private CalendarQueryBuilder queryBuilder;

    @Mock
    private CalendarRowMapper rowMapper;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private CalendarRepository calendarRepository;

    private SearchCriteria searchCriteria;
    private List<JudgeCalendarRule> judgeCalendarRules;

    @BeforeEach
    public void setUp() {
        searchCriteria = new SearchCriteria() {
            @Override
            public String getTenantId() {
                return "";
            }

            @Override
            public String getJudgeId() {
                return "";
            }

            @Override
            public String getCourtId() {
                return "";
            }

            @Override
            public Long getFromDate() {
                return null;
            }

            @Override
            public Long getToDate() {
                return null;
            }
        };
        judgeCalendarRules = new ArrayList<>();
        JudgeCalendarRule rule = new JudgeCalendarRule();
        judgeCalendarRules.add(rule);
    }

    @Test
    public void testGetJudgeRule_Success() {
        List<Object> preparedStmtList = new ArrayList<>();
        when(queryBuilder.getJudgeCalendarQuery(any(SearchCriteria.class), any(List.class))).thenReturn("SELECT * FROM calendar");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(CalendarRowMapper.class))).thenReturn(judgeCalendarRules);

        List<JudgeCalendarRule> result = calendarRepository.getJudgeRule(searchCriteria);

        assertEquals(1, result.size());
        verify(queryBuilder, times(1)).getJudgeCalendarQuery(any(SearchCriteria.class), any(List.class));
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(CalendarRowMapper.class));
    }

    @Test
    public void testGetJudgeRule_EmptyResult() {
        List<Object> preparedStmtList = new ArrayList<>();
        when(queryBuilder.getJudgeCalendarQuery(any(SearchCriteria.class), any(List.class))).thenReturn("SELECT * FROM calendar");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(CalendarRowMapper.class))).thenReturn(Collections.emptyList());

        List<JudgeCalendarRule> result = calendarRepository.getJudgeRule(searchCriteria);

        assertEquals(0, result.size());
        verify(queryBuilder, times(1)).getJudgeCalendarQuery(any(SearchCriteria.class), any(List.class));
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(CalendarRowMapper.class));
    }

    @Test
    public void testGetJudgeRule_Exception() {
        List<Object> preparedStmtList = new ArrayList<>();
        when(queryBuilder.getJudgeCalendarQuery(any(SearchCriteria.class), any(List.class))).thenReturn("SELECT * FROM calendar");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(CalendarRowMapper.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            calendarRepository.getJudgeRule(searchCriteria);
        });

        assertEquals("Database error", exception.getMessage());
        verify(queryBuilder, times(1)).getJudgeCalendarQuery(any(SearchCriteria.class), any(List.class));
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(CalendarRowMapper.class));
    }
}
