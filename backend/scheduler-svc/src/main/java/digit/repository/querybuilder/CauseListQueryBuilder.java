package digit.repository.querybuilder;

import digit.web.models.CauseListSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CauseListQueryBuilder {

    private final String BASE_APPLICATION_QUERY = "SELECT * ";

    private static final String FROM_TABLES = " FROM cause_list cl ";

    private static final String CAUSE_LIST_FILE_STORE_QUERY = "SELECT file_store_id FROM cause_list_document ";

    private static final String CAUSE_LIST_FILE_ORDER_BY = " ORDER BY created_time DESC ";

    private static final String CASE_TYPE_QUERY = "SELECT * FROM dristi_case_hearing_type_priority";

    private final String ORDER_BY = " ORDER BY cl.hearing_date, cl.judge_id, cl.hearing_type";

    public String getCauseListQuery(CauseListSearchCriteria searchCriteria, List<Object> preparedStmtList) {
        StringBuilder query = new StringBuilder(BASE_APPLICATION_QUERY);
        query.append(FROM_TABLES);

        if (!ObjectUtils.isEmpty(searchCriteria.getCourtId())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" cl.court_id = ? ");
            preparedStmtList.add(searchCriteria.getCourtId());
        }
        if(!CollectionUtils.isEmpty(searchCriteria.getJudgeIds())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" cl.judge_id IN ( ").append(createQuery(searchCriteria.getJudgeIds())).append(" ) ");
            addToPreparedStatement(preparedStmtList, searchCriteria.getJudgeIds(), new ArrayList<>());
        }
        if(!CollectionUtils.isEmpty(searchCriteria.getCaseIds())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" cl.case_id IN ( ").append(createQuery(searchCriteria.getCaseIds())).append(" ) ");
            addToPreparedStatement(preparedStmtList, searchCriteria.getCaseIds(), new ArrayList<>());
        }
        if (!ObjectUtils.isEmpty(searchCriteria.getSearchDate())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" cl.hearing_date = ? ");
            preparedStmtList.add(searchCriteria.getSearchDate().toString());
        } else {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" cl.hearing_date = ? ");
            preparedStmtList.add(LocalDate.now().plusDays(1).toString());
        }
        query.append(ORDER_BY);

        return query.toString();
    }

    public String getCauseListFileStoreQuery(CauseListSearchCriteria searchCriteria, List<Object> preparedStmtList, List<Integer> preparedStmtArgsList) {
        StringBuilder query = new StringBuilder(CAUSE_LIST_FILE_STORE_QUERY);

        if (!ObjectUtils.isEmpty(searchCriteria.getCourtId())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" court_id = ? ");
            preparedStmtList.add(searchCriteria.getCourtId());
            preparedStmtArgsList.add(Types.VARCHAR);
        }
        if(!CollectionUtils.isEmpty(searchCriteria.getJudgeIds())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" judge_id IN ( ").append(createQuery(searchCriteria.getJudgeIds())).append(" ) ");
            addToPreparedStatement(preparedStmtList, searchCriteria.getJudgeIds(), preparedStmtArgsList);
        }
        if (!ObjectUtils.isEmpty(searchCriteria.getSearchDate())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" hearing_date = ? ");
            preparedStmtList.add(searchCriteria.getSearchDate().toString());
            preparedStmtArgsList.add(Types.VARCHAR);
        }

        query.append(CAUSE_LIST_FILE_ORDER_BY);
        return query.toString();
    }
    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
        if (preparedStmtList.isEmpty()) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }

    private String createQuery(List<String> ids) {
        StringBuilder builder = new StringBuilder();
        int length = ids.size();
        for (int i = 0; i < length; i++) {
            builder.append(" ?");
            if (i != length - 1)
                builder.append(",");
        }
        return builder.toString();
    }

    private void addToPreparedStatement(List<Object> preparedStmtList, List<String> ids, List<Integer> preparedStmtArgsList) {
        ids.forEach(id -> {
            preparedStmtList.add(id);
            preparedStmtArgsList.add(Types.VARCHAR);
        });
    }

    public String getCaseTypeQuery(List<Object> preparedStmtList, List<Integer> preparedStmtArgsList) {
        return CASE_TYPE_QUERY;
    }

}
