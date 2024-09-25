package digit.repository.querybuilder;

import digit.helper.QueryBuilderHelper;
import digit.web.models.OptOutSearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class RescheduleRequestOptOutQueryBuilderTest {

    @Mock
    private QueryBuilderHelper queryBuilderHelper;

    @InjectMocks
    private RescheduleRequestOptOutQueryBuilder queryBuilder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOptOutQuery_withAllCriteria() {
        OptOutSearchCriteria optOutSearchCriteria = new OptOutSearchCriteria();
        optOutSearchCriteria.setJudgeId("judge1");
        optOutSearchCriteria.setCaseId("case1");
        optOutSearchCriteria.setIndividualId("individual1");
        optOutSearchCriteria.setRescheduleRequestId("request1");
        optOutSearchCriteria.setTenantId("tenant1");

        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT  oo.individual_id ,oo.judge_id ,oo.case_id ,oo.reschedule_request_id ,oo.opt_out_dates , oo.created_by,oo.last_modified_by,oo.created_time,oo.last_modified_time, oo.row_version , oo.tenant_id  FROM reschedule_request_opt_out_detail oo  WHERE  oo.judge_id = ?  AND  oo.case_id = ?  AND  oo.individual_id = ?  AND  oo.reschedule_request_id = ?  AND  oo.tenant_id = ? ";

        doNothing().when(queryBuilderHelper).addClauseIfRequired(any(StringBuilder.class), anyList());

        String actualQuery = queryBuilder.getOptOutQuery(optOutSearchCriteria, preparedStmtList, null, null);

        assertEquals(5, preparedStmtList.size());

        verify(queryBuilderHelper, times(5)).addClauseIfRequired(any(StringBuilder.class), anyList());
    }

    @Test
    public void testGetOptOutQuery_withNoCriteria() {
        OptOutSearchCriteria optOutSearchCriteria = new OptOutSearchCriteria();
        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT  oo.id ,oo.individual_id ,oo.judge_id ,oo.case_id ,oo.reschedule_request_id ,oo.opt_out_dates , oo.created_by,oo.last_modified_by,oo.created_time,oo.last_modified_time, oo.row_version , oo.tenant_id  FROM reschedule_request_opt_out_detail oo ";

        String actualQuery = queryBuilder.getOptOutQuery(optOutSearchCriteria, preparedStmtList, null, null);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(0, preparedStmtList.size());

        verify(queryBuilderHelper, never()).addClauseIfRequired(any(StringBuilder.class), anyList());
    }

    @Test
    public void testGetOptOutQuery_withLimitAndOffset() {
        OptOutSearchCriteria optOutSearchCriteria = new OptOutSearchCriteria();
        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT  oo.individual_id ,oo.judge_id ,oo.case_id ,oo.reschedule_request_id ,oo.opt_out_dates , oo.created_by,oo.last_modified_by,oo.created_time,oo.last_modified_time, oo.row_version , oo.tenant_id  FROM reschedule_request_opt_out_detail oo  LIMIT ? OFFSET ? ";

        doNothing().when(queryBuilderHelper).addClauseIfRequired(any(StringBuilder.class), anyList());

        String actualQuery = queryBuilder.getOptOutQuery(optOutSearchCriteria, preparedStmtList, 10, 0);

        assertEquals(2, preparedStmtList.size());

        verify(queryBuilderHelper, times(0)).addClauseIfRequired(any(StringBuilder.class), anyList());
    }

}