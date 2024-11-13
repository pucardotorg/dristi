package digit.repository.querybuilder;


import digit.helper.QueryBuilderHelper;
import digit.web.models.ReScheduleHearingReqSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Component
@Slf4j
public class ReScheduleHearingQueryBuilder {


    @Autowired
    private QueryBuilderHelper helper;

    private final String BASE_APPLICATION_QUERY = "SELECT hbr.reschedule_request_id, hbr.hearing_booking_id, hbr.tenant_id, hbr.judge_id, hbr.case_id,hbr.requester_id,hbr.reason,hbr.status, hbr.created_by,hbr.last_modified_by,hbr.created_time,hbr.last_modified_time, hbr.row_version, hbr.suggested_days , hbr.available_days, hbr.litigants, hbr.representatives  ";

    private static final String FROM_TABLES = " FROM hearing_booking_reschedule_request hbr ";

    private final String ORDER_BY = " ORDER BY hbr.last_modified_time DESC";

    private final String LIMIT_OFFSET = " LIMIT ? OFFSET ?";

    public String getReScheduleRequestQuery(ReScheduleHearingReqSearchCriteria searchCriteria, List<Object> preparedStmtList, Integer limit, Integer offset) {
        StringBuilder query = new StringBuilder(BASE_APPLICATION_QUERY);
        query.append(FROM_TABLES);

        if (!CollectionUtils.isEmpty(searchCriteria.getRescheduledRequestId())) {
            helper.addClauseIfRequired(query, preparedStmtList);
            query.append(" hbr.reschedule_request_id IN ( ").append(helper.createQuery(searchCriteria.getRescheduledRequestId())).append(" ) ");
            helper.addToPreparedStatement(preparedStmtList, searchCriteria.getRescheduledRequestId());
        }

        if (!ObjectUtils.isEmpty(searchCriteria.getTenantId())) {
            helper.addClauseIfRequired(query, preparedStmtList);
            query.append(" hbr.tenant_id = ? ");
            preparedStmtList.add(searchCriteria.getTenantId());
        }

        if (!ObjectUtils.isEmpty(searchCriteria.getJudgeId())) {
            helper.addClauseIfRequired(query, preparedStmtList);
            query.append(" hbr.judge_id = ? ");
            preparedStmtList.add(searchCriteria.getJudgeId());
        }

        //bug
        if (!ObjectUtils.isEmpty(searchCriteria.getCaseId())) {
            helper.addClauseIfRequired(query, preparedStmtList);
            query.append(" hbr.case_id = ? ");
            preparedStmtList.add(searchCriteria.getJudgeId());
        }

        if (!ObjectUtils.isEmpty(searchCriteria.getHearingBookingId())) {
            helper.addClauseIfRequired(query, preparedStmtList);
            query.append(" hbr.hearing_booking_id = ? ");
            preparedStmtList.add(searchCriteria.getHearingBookingId());
        }
        if (!ObjectUtils.isEmpty(searchCriteria.getRequesterId())) {
            helper.addClauseIfRequired(query, preparedStmtList);
            query.append(" hbr.requester_id = ? ");
            preparedStmtList.add(searchCriteria.getRequesterId());
        }
        if (!ObjectUtils.isEmpty(searchCriteria.getStatus())) {
            helper.addClauseIfRequired(query, preparedStmtList);
            query.append(" hbr.status = ? ");
            preparedStmtList.add(searchCriteria.getStatus().toString());
        }
        if (!ObjectUtils.isEmpty(searchCriteria.getDueDate())) {
            helper.addClauseIfRequired(query, preparedStmtList);
            query.append(" hbr.last_modified_time < ?  ");
            preparedStmtList.add(searchCriteria.getDueDate());
        }
        query.append(ORDER_BY);
        if (!ObjectUtils.isEmpty(limit) && !ObjectUtils.isEmpty(offset)) {
            query.append(LIMIT_OFFSET);
            preparedStmtList.add(limit);
            preparedStmtList.add(offset);
        }


        return query.toString();
    }
}
