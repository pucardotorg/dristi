package digit.repository.querybuilder;

import digit.helper.QueryBuilderHelper;
import digit.web.models.SearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalendarQueryBuilderTest {

    @Mock
    private QueryBuilderHelper queryBuilderHelper;

    @InjectMocks
    private CalendarQueryBuilder calendarQueryBuilder;

    private SearchCriteria searchCriteria;

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
    }

    @Test
    public void testGetJudgeCalendarQuery_withAllCriteria() {
        searchCriteria = new SearchCriteria() {
            @Override
            public String getTenantId() {
                return "tenant1";
            }

            @Override
            public String getJudgeId() {
                return "judge1";
            }

            @Override
            public String getCourtId() {
                return "";
            }

            @Override
            public Long getFromDate() {
                return LocalDate.parse("2023-01-01").toEpochDay();
            }

            @Override
            public Long getToDate() {
                return LocalDate.parse("2023-01-01").toEpochDay();
            }
        };

        List<Object> preparedStmtList = new ArrayList<>();
        String expectedQuery = "SELECT jc.judge_id, jc.id, jc.rule_type, jc.date, jc.notes, jc.created_by, jc.last_modified_by, jc.created_time, jc.last_modified_time, jc.row_version, jc.tenant_id " +
                "FROM judge_calendar_rules jc ";

        doNothing().when(queryBuilderHelper).addClauseIfRequired(any(StringBuilder.class), anyList());

        String actualQuery = calendarQueryBuilder.getJudgeCalendarQuery(searchCriteria, preparedStmtList);

        assertNotNull(preparedStmtList);

        verify(queryBuilderHelper, times(4)).addClauseIfRequired(any(StringBuilder.class), anyList());
    }

    @Test
    public void testGetJudgeCalendarQuery_withNoCriteria() {
        List<Object> preparedStmtList = new ArrayList<>();
        String expectedQuery = "SELECT jc.judge_id, jc.id, jc.rule_type, jc.date, jc.notes, jc.created_by, jc.last_modified_by, jc.created_time, jc.last_modified_time, jc.row_version, jc.tenant_id " +
                "FROM judge_calendar_rules jc ";

        String actualQuery = calendarQueryBuilder.getJudgeCalendarQuery(searchCriteria, preparedStmtList);

        assertEquals(0, preparedStmtList.size());

        verify(queryBuilderHelper, never()).addClauseIfRequired(any(StringBuilder.class), anyList());
    }

    @Test
    public void testGetJudgeCalendarQuery_withTenantId() {
        searchCriteria = new SearchCriteria() {
            @Override
            public String getTenantId() {
                return "tenant1";
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

        List<Object> preparedStmtList = new ArrayList<>();
        String expectedQuery = "SELECT jc.judge_id, jc.id, jc.rule_type, jc.date, jc.notes, jc.created_by, jc.last_modified_by, jc.created_time, jc.last_modified_time, jc.row_version, jc.tenant_id " +
                "FROM judge_calendar_rules jc " +
                "WHERE jc.tenant_id = ? ";

        doNothing().when(queryBuilderHelper).addClauseIfRequired(any(StringBuilder.class), anyList());

        String actualQuery = calendarQueryBuilder.getJudgeCalendarQuery(searchCriteria, preparedStmtList);

        assertEquals(1, preparedStmtList.size());
        assertNotNull(preparedStmtList);

        verify(queryBuilderHelper, times(1)).addClauseIfRequired(any(StringBuilder.class), anyList());
    }

    @Test
    public void testGetJudgeCalendarQuery_withJudgeId() {
        searchCriteria = new SearchCriteria() {
            @Override
            public String getTenantId() {
                return "";
            }

            @Override
            public String getJudgeId() {
                return "judge1";
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

        List<Object> preparedStmtList = new ArrayList<>();
        String expectedQuery = "SELECT jc.judge_id, jc.id, jc.rule_type, jc.date, jc.notes, jc.created_by, jc.last_modified_by, jc.created_time, jc.last_modified_time, jc.row_version, jc.tenant_id " +
                "FROM judge_calendar_rules jc " +
                "WHERE jc.judge_id = ? ";

        doNothing().when(queryBuilderHelper).addClauseIfRequired(any(StringBuilder.class), anyList());

        String actualQuery = calendarQueryBuilder.getJudgeCalendarQuery(searchCriteria, preparedStmtList);

        assertEquals(1, preparedStmtList.size());

        verify(queryBuilderHelper, times(1)).addClauseIfRequired(any(StringBuilder.class), anyList());
    }

    @Test
    public void testGetJudgeCalendarQuery_withFromDate() {
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
                return LocalDate.parse("2023-01-01").toEpochDay();
            }

            @Override
            public Long getToDate() {
                return null;
            }
        };

        List<Object> preparedStmtList = new ArrayList<>();
        String expectedQuery = "SELECT jc.judge_id, jc.id, jc.rule_type, jc.date, jc.notes, jc.created_by, jc.last_modified_by, jc.created_time, jc.last_modified_time, jc.row_version, jc.tenant_id " +
                "FROM judge_calendar_rules jc " +
                "WHERE TO_DATE(jc.date, 'YYYY-MM-DD') >= ? ";

        doNothing().when(queryBuilderHelper).addClauseIfRequired(any(StringBuilder.class), anyList());

        String actualQuery = calendarQueryBuilder.getJudgeCalendarQuery(searchCriteria, preparedStmtList);

        assertEquals(1, preparedStmtList.size());

        verify(queryBuilderHelper, times(1)).addClauseIfRequired(any(StringBuilder.class), anyList());
    }

    @Test
    public void testGetJudgeCalendarQuery_withToDate() {
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
                return LocalDate.parse("2023-12-31").toEpochDay();
            }
        };

        List<Object> preparedStmtList = new ArrayList<>();

        doNothing().when(queryBuilderHelper).addClauseIfRequired(any(StringBuilder.class), anyList());

        String actualQuery = calendarQueryBuilder.getJudgeCalendarQuery(searchCriteria, preparedStmtList);

        assertEquals(1, preparedStmtList.size());

        verify(queryBuilderHelper, times(1)).addClauseIfRequired(any(StringBuilder.class), anyList());
    }
}