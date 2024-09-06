package digit.repository.querybuilder;

import digit.helper.QueryBuilderHelper;
import digit.web.models.ReScheduleHearingReqSearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class ReScheduleHearingQueryBuilderTest {

    @Mock
    private QueryBuilderHelper helper;

    @InjectMocks
    private ReScheduleHearingQueryBuilder queryBuilder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetReScheduleRequestQuery_withAllCriteria() {
        ReScheduleHearingReqSearchCriteria searchCriteria = new ReScheduleHearingReqSearchCriteria();
        searchCriteria.setRescheduledRequestId(List.of("1", "2"));
        searchCriteria.setTenantId("tenant1");
        searchCriteria.setJudgeId("judge1");
        searchCriteria.setCaseId("case1");
        searchCriteria.setHearingBookingId("booking1");
        searchCriteria.setRequesterId("requester1");
        searchCriteria.setStatus(1L);
        searchCriteria.setDueDate(2L);

        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT hbr.reschedule_request_id, hbr.hearing_booking_id, hbr.tenant_id, hbr.judge_id, hbr.case_id,hbr.requester_id,hbr.reason,hbr.status,hbr.action_comment,hbr.documents, hbr.created_by,hbr.last_modified_by,hbr.created_time,hbr.last_modified_time, hbr.row_version, hbr.suggested_days , hbr.available_days  FROM hearing_booking_reschedule_request hbr  WHERE  hbr.reschedule_request_id IN ( ? )  AND  hbr.tenant_id = ?  AND  hbr.judge_id = ?  AND  hbr.case_id = ?  AND  hbr.hearing_booking_id = ?  AND  hbr.requester_id = ?  AND  hbr.status = ?  AND  hbr.last_modified_time < ?  ORDER BY hbr.last_modified_time DESC";

        when(helper.createQuery(any())).thenReturn("?");
        doNothing().when(helper).addToPreparedStatement(anyList(), any());

        String actualQuery = queryBuilder.getReScheduleRequestQuery(searchCriteria, preparedStmtList, null, null);

        assertEquals(7, preparedStmtList.size());

        verify(helper, times(8)).addClauseIfRequired(any(StringBuilder.class), anyList());
    }

    @Test
    public void testGetReScheduleRequestQuery_withNoCriteria() {
        ReScheduleHearingReqSearchCriteria searchCriteria = new ReScheduleHearingReqSearchCriteria();
        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT hbr.reschedule_request_id, hbr.hearing_booking_id, hbr.tenant_id, hbr.judge_id, hbr.case_id,hbr.requester_id,hbr.reason,hbr.status,hbr.action_comment,hbr.documents, hbr.created_by,hbr.last_modified_by,hbr.created_time,hbr.last_modified_time, hbr.row_version, hbr.suggested_days , hbr.available_days  FROM hearing_booking_reschedule_request hbr  ORDER BY hbr.last_modified_time DESC";

        String actualQuery = queryBuilder.getReScheduleRequestQuery(searchCriteria, preparedStmtList, null, null);

        assertEquals(0, preparedStmtList.size());

        verify(helper, never()).addClauseIfRequired(any(StringBuilder.class), anyList());
    }


}