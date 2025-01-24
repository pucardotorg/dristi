package digit.repository.querybuilder;

import digit.web.models.CaseDiaryExistCriteria;
import digit.web.models.CaseDiarySearchCriteria;
import digit.web.models.Pagination;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;

@Component
@Slf4j
public class DiaryEntryQueryBuilder {

    private static final String BASE_DIARY_ENTRY_QUERY = "SELECT dde.id as id,dde.tenant_id as tenantId,dde.case_number as caseNumber,dde.judge_id as judgeId, " +
            "dde.entry_date as entryDate,dde.businessOfDay as businessOfDay,dde.reference_id as referenceId,dde.reference_type as referenceType," +
            "dde.hearingDate as hearingDate,dde.additional_details as additionalDetails,dde.created_by as createdBy,dde.last_modified_by as lastModifiedBy," +
            "dde.created_time as createdTime,dde.last_modified_time as lastModifiedTime ";

    private static final String FROM_DIARY_ENTRY_TABLE = "FROM dristi_diaryentries dde";

    private static final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM ({baseQuery}) total_result";

    private static final String DEFAULT_ORDERBY_CLAUSE = " ORDER BY createdtime DESC ";

    private static final String ORDERBY_CLAUSE = " ORDER BY {orderBy} {sortingOrder} ";

    public String getDiaryEntryQuery(CaseDiarySearchCriteria searchCriteria, List<Object> preparedStatementValues, List<Integer> preparedStatementTypeValues) {

        StringBuilder query = new StringBuilder(BASE_DIARY_ENTRY_QUERY);
        query.append(FROM_DIARY_ENTRY_TABLE);

        boolean firstCriteria = true;

        if (searchCriteria != null) {
            if (searchCriteria.getTenantId() != null) {
                addWhereClause(query, firstCriteria);
                query.append("dde.tenant_id = ?");
                preparedStatementValues.add(searchCriteria.getTenantId());
                preparedStatementTypeValues.add(Types.VARCHAR);
                firstCriteria = false;
            }
            if (searchCriteria.getDate() != null) {
                addWhereClause(query, firstCriteria);
                query.append("dde.entry_date = ?");
                preparedStatementValues.add(searchCriteria.getDate());
                preparedStatementTypeValues.add(Types.BIGINT);
                firstCriteria = false;
            }
            if (searchCriteria.getCaseId() != null) {
                addWhereClause(query, firstCriteria);
                query.append("dde.case_number = ?");
                preparedStatementValues.add(searchCriteria.getCaseId());
                preparedStatementTypeValues.add(Types.BIGINT);
                firstCriteria = false;
            }

            if (searchCriteria.getJudgeId() != null) {
                addWhereClause(query, firstCriteria);
                query.append("dde.judge_id = ?");
                preparedStatementValues.add(searchCriteria.getJudgeId());
                preparedStatementTypeValues.add(Types.VARCHAR);
            }
        }

        return query.toString();
    }

    private void addWhereClause(StringBuilder query, boolean firstCriteria) {
        if (firstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }

    public String getTotalCountQuery(String baseQuery) {
        return TOTAL_COUNT_QUERY.replace("{baseQuery}", baseQuery);
    }

    public String addPaginationQuery(String diaryEntryQuery, List<Object> preparedStmtList, @Valid Pagination pagination, List<Integer> preparedStmtArgList) {

        preparedStmtList.add(pagination.getLimit());
        preparedStmtArgList.add(Types.INTEGER);

        preparedStmtList.add(pagination.getOffSet());
        preparedStmtArgList.add(Types.INTEGER);
        return diaryEntryQuery + " LIMIT ? OFFSET ?";
    }

    public String addOrderByQuery(String query, Pagination pagination) {
        if (isPaginationInvalid(pagination) || pagination.getSortBy().contains(";")) {
            return query + DEFAULT_ORDERBY_CLAUSE;
        } else {
            query = query + ORDERBY_CLAUSE;
        }
        return query.replace("{orderBy}", pagination.getSortBy()).replace("{sortingOrder}", pagination.getOrder().name());
    }

    private static boolean isPaginationInvalid(Pagination pagination) {
        return pagination == null || pagination.getSortBy() == null || pagination.getOrder() == null;
    }

    public String getExistingDiaryEntry(CaseDiaryExistCriteria caseDiaryExistCriteria, List<Object> preparedStatementValues, List<Integer> preparedStatementTypeValues) {

        StringBuilder query = new StringBuilder(BASE_DIARY_ENTRY_QUERY);
        query.append(FROM_DIARY_ENTRY_TABLE);

        boolean firstCriteria = true;

        if (caseDiaryExistCriteria != null) {

            if (caseDiaryExistCriteria.getId() != null) {
                addWhereClause(query, firstCriteria);
                query.append("dde.id = ?");
                preparedStatementValues.add(caseDiaryExistCriteria.getId());
                preparedStatementTypeValues.add(Types.VARCHAR);
                firstCriteria = false;
            }
            if (caseDiaryExistCriteria.getTenantId() != null) {
                addWhereClause(query,firstCriteria);
                query.append("dde.tenant_id = ?");
                preparedStatementValues.add(caseDiaryExistCriteria.getTenantId());
                preparedStatementTypeValues.add(Types.VARCHAR);
            }
        }

        return query.toString();
    }

}
