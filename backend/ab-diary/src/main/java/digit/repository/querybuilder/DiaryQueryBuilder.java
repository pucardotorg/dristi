package digit.repository.querybuilder;

import digit.web.models.CaseDiarySearchCriteria;
import digit.web.models.Pagination;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;

@Component
@Slf4j
public class DiaryQueryBuilder {

    private static final String BASE_DIARY_QUERY = "SELECT dcd.id as id, dcd.tenant_id as tenantId,dcd.case_number as caseNumber," +
            "dcd.diary_date as diaryDate, dcd.diary_type as diaryType,dcd.judge_id as judgeId,dcd.additional_details as additionalDetails," +
            "dcd.created_by as diaryCreateBy,dcd.last_modified_by as diaryLastModifiedBy,dcd.created_time as diaryCreatedTime," +
            "dcd.last_modified_time as diaryLastModifiedTime, dcdd.filestore_id as fileStoreId , dcdd.id as documentId," +
            "dcdd.document_uid as documentUid,dcdd.document_name as documentName,dcdd.document_type as documentType," +
            "dcdd.casediary_id as caseDiaryId,dcdd.is_active as documentIsActive,dcdd.additional_details as documentAdditionalDetails," +
            "dcdd.created_by as documentCreatedBy,dcdd.last_modified_by as documentLastModifiedBy," +
            "dcdd.created_time as documentCreatedTime , dcdd.last_modified_time as documentLastModifiedTime ";

    private static final String FROM_DIARY_ENTRY_TABLE = "FROM dristi_casediary dcd join dristi_casediary_documents dcdd on dcd.id = dcdd.casediary_id ";

    private static final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM ({baseQuery}) total_result";

    private static final String DEFAULT_ORDER_BY_CLAUSE = " ORDER BY dcd.created_time DESC ";

    private static final String ORDER_BY_CLAUSE = " ORDER BY {orderBy} {sortingOrder} ";

    public String getCaseDiaryQuery(CaseDiarySearchCriteria searchCriteria, List<Object> preparedStatementValues, List<Integer> preparedStatementTypeValues) {

        StringBuilder query = new StringBuilder(BASE_DIARY_QUERY);
        query.append(FROM_DIARY_ENTRY_TABLE);

        boolean firstCriteria = true;

        if (searchCriteria != null) {
            if (searchCriteria.getTenantId() != null) {
                addWhereClause(query, firstCriteria);
                query.append("dcd.tenant_id = ?");
                preparedStatementValues.add(searchCriteria.getTenantId());
                preparedStatementTypeValues.add(Types.VARCHAR);
                firstCriteria = false;
            }
            if (searchCriteria.getDate() != null) {
                addWhereClause(query, firstCriteria);
                query.append("dcd.diary_date = ?");
                preparedStatementValues.add(searchCriteria.getDate());
                preparedStatementTypeValues.add(Types.BIGINT);
                firstCriteria = false;
            }
            if (searchCriteria.getCaseId() != null) {
                addWhereClause(query, firstCriteria);
                query.append("dcd.case_number = ?");
                preparedStatementValues.add(searchCriteria.getCaseId());
                preparedStatementTypeValues.add(Types.BIGINT);
                firstCriteria = false;
            }

            if (searchCriteria.getJudgeId() != null) {
                addWhereClause(query, firstCriteria);
                query.append("dcd.judge_id = ?");
                preparedStatementValues.add(searchCriteria.getJudgeId());
                preparedStatementTypeValues.add(Types.VARCHAR);
                firstCriteria =false;
            }

            if (searchCriteria.getDiaryType() != null) {
                addWhereClause(query,firstCriteria);
                query.append("dcd.diary_type = ?");
                preparedStatementValues.add(searchCriteria.getDiaryType());
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
            return query + DEFAULT_ORDER_BY_CLAUSE;
        } else {
            query = query + ORDER_BY_CLAUSE;
        }
        return query.replace("{orderBy}", pagination.getSortBy()).replace("{sortingOrder}", pagination.getOrder().name());
    }

    private static boolean isPaginationInvalid(Pagination pagination) {
        return pagination == null || pagination.getSortBy() == null || pagination.getOrder() == null;
    }
}
