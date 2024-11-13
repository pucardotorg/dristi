package digit.repository.querybuilder;

import digit.helper.QueryBuilderHelper;
import digit.web.models.SearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CalendarQueryBuilder {

    @Autowired
    private QueryBuilderHelper queryBuilderHelper;

    private final String BASE_APPLICATION_QUERY = "SELECT jc.judge_id, jc.id, jc.rule_type, jc.date, jc.notes, jc.created_by,jc.last_modified_by,jc.created_time,jc.last_modified_time, jc.row_version ,jc.tenant_id ";

    private static final String FROM_TABLES = " FROM judge_calendar_rules jc ";

    private final String ORDER_BY = " ORDER BY ";

    private final String LIMIT_OFFSET = " LIMIT ? OFFSET ?";

    public String getJudgeCalendarQuery(SearchCriteria searchCriteria, List<Object> preparedStmtList) {

        StringBuilder query = new StringBuilder(BASE_APPLICATION_QUERY);
        query.append(FROM_TABLES);

        if (!ObjectUtils.isEmpty(searchCriteria.getTenantId())) {
            queryBuilderHelper.addClauseIfRequired(query, preparedStmtList);
            query.append(" jc.tenant_id = ? ");
            preparedStmtList.add(searchCriteria.getTenantId());
        }

        if (!ObjectUtils.isEmpty(searchCriteria.getJudgeId())) {
            queryBuilderHelper.addClauseIfRequired(query, preparedStmtList);
            query.append(" jc.judge_id = ? ");
            preparedStmtList.add(searchCriteria.getJudgeId());
        }

        if (!ObjectUtils.isEmpty(searchCriteria.getFromDate())) {
            queryBuilderHelper.addClauseIfRequired(query, preparedStmtList);
            query.append(" jc.date >= ? ");
            preparedStmtList.add(searchCriteria.getFromDate());
        }

        if (!ObjectUtils.isEmpty(searchCriteria.getToDate())) {
            queryBuilderHelper.addClauseIfRequired(query, preparedStmtList);
            query.append(" jc.date <= ?");
            preparedStmtList.add(searchCriteria.getToDate());
        }

        return query.toString();
    }

}
