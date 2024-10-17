package org.pucar.dristi.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.Pagination;
import org.pucar.dristi.web.models.TaskCaseSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.DOCUMENT_SEARCH_QUERY_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.TASK_SEARCH_QUERY_EXCEPTION;

@Slf4j
@Component
public class TaskCaseQueryBuilder {

    private static final String BASE_TASK_QUERY = "SELECT task.id as id, task.tenantid as tenantid, task.orderid as orderid, task.createddate as createddate," +
            " task.filingnumber as filingnumber, task.tasknumber as tasknumber, task.datecloseby as datecloseby, task.dateclosed as dateclosed, task.taskdescription as taskdescription, task.cnrnumber as cnrnumber," +
            " task.taskdetails as taskdetails, task.assignedto as assignedto, task.tasktype as tasktype, task.assignedto as assignedto, task.status as status, task.isactive as isactive,task.additionaldetails as additionaldetails, task.createdby as createdby," +
            " task.lastmodifiedby as lastmodifiedby, task.createdtime as createdtime, task.lastmodifiedtime as lastmodifiedtime ,c.caseTitle as caseName , o.orderType as orderType";

    private static final String DOCUMENT_SWITCH_CASE = " ,CASE WHEN EXISTS (SELECT 1 FROM dristi_task_document dtd WHERE dtd.task_id = task.id AND dtd.documentType = 'SIGNED')" +
            "THEN 'SIGNED' ELSE 'SIGN_PENDING' END AS documentstatus";
    private static final String FROM_TASK_TABLE = " FROM dristi_task task";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_task_document doc";
    private static final String DOCUMENT_SELECT_QUERY_TASK = "SELECT doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore," +
            " doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.task_id as task_id";
    private static final String TOTAL_COUNT_QUERY = " SELECT COUNT(*) from ({baseQuery}) AS total_count ";
    private static final String ORDERBY_CLAUSE = " ORDER BY {orderBy} {sortingOrder} ";
    private static final String DEFAULT_ORDERBY_CLAUSE = " ORDER BY createdtime DESC ";
    private static final String PAGINATION_QUERY = " LIMIT ? OFFSET ? ";
    private static final String DEFAULT_JOIN_CLAUSE =
            " JOIN dristi_orders o ON task.orderId = o.id " +
                    " JOIN dristi_cases c ON task.cnrNumber = c.cnrNumber ";
    private static final String ROW_NUM_CLAUSE = " ,ROW_NUMBER() OVER (PARTITION BY task.id ORDER BY task.createdtime DESC) AS row_num ";
    private static final String WITH_CLAUSE_QUERY = " WITH task_case_results AS ( {baseQuery} ) ";
    private static final String DOCUMENT_LEFT_JOIN = " LEFT JOIN dristi_task_document dtd ON task.id = dtd.task_id ";
    private static final String TASK_CASE_SELECT_QUERY = " SELECT * FROM task_case_results ";
    private static final String ROW_NUM_WHERE_CLAUSE = " WHERE row_num = 1 ";

    public String getTaskTableSearchQuery(TaskCaseSearchCriteria criteria, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_TASK_QUERY);
            query.append(DOCUMENT_SWITCH_CASE);
            query.append(ROW_NUM_CLAUSE);
            query.append(FROM_TASK_TABLE);
            query.append(DEFAULT_JOIN_CLAUSE);
            query.append(DOCUMENT_LEFT_JOIN);
            getWhereFields(criteria, query, preparedStmtList);
            return query.toString();
        } catch (Exception e) {
            log.error("Error while building application search query {}", e.getMessage());
            throw new CustomException(TASK_SEARCH_QUERY_EXCEPTION, "Error occurred while building the task-table search query: " + e.getMessage());
        }
    }
    public String addWithClauseQuery(String query){
        return WITH_CLAUSE_QUERY.replace("{baseQuery}", query);
    }

    public String getFinalTaskCaseSearchQuery() {
        return TASK_CASE_SELECT_QUERY + ROW_NUM_WHERE_CLAUSE;
    }
    public String addApplicationStatusQuery(TaskCaseSearchCriteria searchCriteria, String query, List<Object> preparedStmtList){
        if(!ObjectUtils.isEmpty(searchCriteria.getApplicationStatus())) {
            preparedStmtList.add(searchCriteria.getApplicationStatus());
            return query + " AND documentstatus IN ( ? )";
        }
        return query;
    }

    public String addPaginationQuery(String query, Pagination pagination, List<Object> preparedStatementList) {
        preparedStatementList.add(pagination.getLimit());
        preparedStatementList.add(pagination.getOffSet());
        return query + PAGINATION_QUERY;
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

    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY_TASK);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.task_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while building document search query :: {}", e.toString());
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the query for task document search: " + e.getMessage());
        }
    }

    public String getTotalCountQuery(String baseQuery) {
        return TOTAL_COUNT_QUERY.replace("{baseQuery}", baseQuery);
    }

    private void getWhereFields(TaskCaseSearchCriteria taskCaseSearchCriteria, StringBuilder query, List<Object> preparedStmtList) {

        if (!CollectionUtils.isEmpty(taskCaseSearchCriteria.getCompleteStatus())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" task.status IN ( ").append(createQuery(taskCaseSearchCriteria.getCompleteStatus())).append(" ) ");
            addToPreparedStatement(preparedStmtList, taskCaseSearchCriteria.getCompleteStatus());
        }

        if (!CollectionUtils.isEmpty(taskCaseSearchCriteria.getOrderType())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" orderType IN ( ").append(createQuery(taskCaseSearchCriteria.getOrderType())).append(" ) ");
            addToPreparedStatement(preparedStmtList, taskCaseSearchCriteria.getOrderType());

        }

        if (!ObjectUtils.isEmpty(taskCaseSearchCriteria.getSearchText())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append("(task.tasknumber ILIKE ").append(" ? ").append(" or task.cnrnumber ILIKE ").append(" ? ").append(" )");
            preparedStmtList.add(taskCaseSearchCriteria.getSearchText());
            preparedStmtList.add(taskCaseSearchCriteria.getSearchText());
        }

    }

    public void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
        if (preparedStmtList.isEmpty()) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }

    public String createQuery(List<String> ids) {
        StringBuilder builder = new StringBuilder();
        int length = ids.size();
        for (int i = 0; i < length; i++) {
            builder.append(" ?");
            if (i != length - 1)
                builder.append(",");
        }
        return builder.toString();
    }


    public void addToPreparedStatement(List<Object> preparedStmtList, List<String> ids) {
        ids.forEach(id -> {
            preparedStmtList.add(id);
        });
    }

}