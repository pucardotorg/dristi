package digit.repository.querybuilder;

import digit.helper.QueryBuilderHelper;
import digit.web.models.ScheduleHearingSearchCriteria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HearingQueryBuilderTest {

    @Mock
    private QueryBuilderHelper queryBuilderHelper;

    @InjectMocks
    private HearingQueryBuilder hearingQueryBuilder;

    @Test
    public void testGetHearingQuery_withAllCriteria() {
        ScheduleHearingSearchCriteria searchCriteria = new ScheduleHearingSearchCriteria();
        searchCriteria.setTenantId("tenant1");
        searchCriteria.setJudgeId("judge1");
        searchCriteria.setCourtId("court1");
        searchCriteria.setCaseId("case1");
        searchCriteria.setHearingType("ADMISSION");
        List<String> hearingIds = new ArrayList<>();
        hearingIds.add("hearing1");
        searchCriteria.setHearingIds(hearingIds);
        searchCriteria.setRescheduleId("reschedule1");
        List<String> status = new ArrayList<>();
        status.add("SCHEDULED");
        status.add("RESCHEDULED");
        searchCriteria.setStatus(status);
        searchCriteria.setStartDateTime(LocalDate.of(2023, 1, 1).atStartOfDay().toEpochSecond(ZoneOffset.UTC));
        searchCriteria.setEndDateTime(LocalDate.of(2023, 1, 2).atStartOfDay().toEpochSecond(ZoneOffset.UTC));
        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT  hb.hearing_booking_id, hb.tenant_id, hb.court_id,hb.hearing_date, hb.judge_id, hb.case_id, hb.hearing_type, hb.title, hb.description, hb.status, hb.start_time, hb.end_time, hb.created_by,hb.last_modified_by,hb.created_time,hb.last_modified_time, hb.row_version ,hb.reschedule_request_id FROM hearing_booking hb  hb.hearing_booking_id IN ( null )  hb.tenant_id = ?  hb.judge_id = ?  hb.court_id = ?  hb.case_id = ?  hb.hearing_type = ?  hb.start_time >= ?  hb.end_time <= ? hb.reschedule_request_id = ?  (  hb.status = ?  or hb.status = ? ) LIMIT ? OFFSET ?";

        String actualQuery = hearingQueryBuilder.getHearingQuery(searchCriteria, preparedStmtList, 0, 10);

        assertEquals(expectedQuery, actualQuery);

    }

    @Test
    public void testGetHearingQuery_withNoCriteria() {
        ScheduleHearingSearchCriteria searchCriteria = new ScheduleHearingSearchCriteria();
        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT  hb.hearing_booking_id, hb.tenant_id, hb.court_id, hb.judge_id, hb.case_id, hb.hearing_date, hb.event_type, hb.title, hb.description, hb.status, hb.start_time, hb.end_time, hb.created_by,hb.last_modified_by,hb.created_time,hb.last_modified_time, hb.row_version ,hb.reschedule_request_id FROM hearing_booking hb ";

        String actualQuery = hearingQueryBuilder.getHearingQuery(searchCriteria, preparedStmtList, null, null);

        assertEquals(0, preparedStmtList.size());

        verify(queryBuilderHelper, never()).addClauseIfRequired(any(StringBuilder.class), anyList());
    }

    @Test
    public void testGetJudgeAvailableDatesQuery_withAllCriteria() {
        ScheduleHearingSearchCriteria searchCriteria = new ScheduleHearingSearchCriteria();
        searchCriteria.setTenantId("tenant1");
        searchCriteria.setJudgeId("judge1");
        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT meeting_hours.hearing_date AS date,meeting_hours.total_hours  AS hours FROM (SELECT hb.hearing_date, SUM(EXTRACT(EPOCH FROM (TO_TIMESTAMP(hb.end_time, 'YYYY-MM-DD HH24:MI:SS') - TO_TIMESTAMP(hb.start_time, 'YYYY-MM-DD HH24:MI:SS'))) / 3600) AS total_hours FROM hearing_booking hb  WHERE  hb.tenant_id = ?  AND  hb.judge_id = ? ) AS meeting_hours  ( hb.status = ?  OR hb.status = ? )";

        doNothing().when(queryBuilderHelper).addClauseIfRequired(any(StringBuilder.class), anyList());

        String actualQuery = hearingQueryBuilder.getJudgeAvailableDatesQuery(searchCriteria, preparedStmtList);

        assertEquals(4, preparedStmtList.size());


        verify(queryBuilderHelper, times(3)).addClauseIfRequired(any(StringBuilder.class), anyList());
    }

    @Test
    public void testGetJudgeAvailableDatesQuery_withNoCriteria() {
        ScheduleHearingSearchCriteria searchCriteria = new ScheduleHearingSearchCriteria();
        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT meeting_hours.hearing_date AS date,meeting_hours.total_hours  AS hours FROM (SELECT hb.hearing_date, SUM(EXTRACT(EPOCH FROM (TO_TIMESTAMP(hb.end_time, 'YYYY-MM-DD HH24:MI:SS') - TO_TIMESTAMP(hb.start_time, 'YYYY-MM-DD HH24:MI:SS'))) / 3600) AS total_hours FROM hearing_booking hb  WHERE  ) AS meeting_hours  ( hb.status = ?  OR hb.status = ? )";

        doNothing().when(queryBuilderHelper).addClauseIfRequired(any(StringBuilder.class), anyList());
        String actualQuery = hearingQueryBuilder.getJudgeAvailableDatesQuery(searchCriteria, preparedStmtList);

        assertEquals(2, preparedStmtList.size());

        verify(queryBuilderHelper, times(1)).addClauseIfRequired(any(StringBuilder.class), anyList());
    }

}