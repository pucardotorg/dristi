package org.pucar.dristi.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class TaskQueryBuilder {

    private static final String BASE_CASE_QUERY = "SELECT task.id as id, task.tenantid as tenantid, task.orderid as orderid, task.createddate as createddate," +
            " task.filingnumber as filingnumber, task.tasknumber as tasknumber, task.datecloseby as datecloseby, task.dateclosed as dateclosed, task.taskdescription as taskdescription, task.cnrnumber as cnrnumber," +
            " task.taskdetails as taskdetails, task.tasktype as tasktype, task.assignedto as assignedto, task.status as status, task.isactive as isactive,task.additionaldetails as additionaldetails, task.createdby as createdby," +
            " task.lastmodifiedby as lastmodifiedby, task.createdtime as createdtime, task.lastmodifiedtime as lastmodifiedtime";
    private static final String FROM_TASK_TABLE = " FROM dristi_task task";
    private static final String ORDERBY_CREATEDTIME = " ORDER BY task.createdtime DESC ";

    private static final String DOCUMENT_SELECT_QUERY_CASE = "SELECT doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore," +
            " doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.task_id as task_id";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_task_document doc";

    private static final String AMOUNT_SELECT_QUERY_CASE = "SELECT amount.id as id, amount.type as type, amount.amount as amount," +
            " amount.paymentRefNumber as paymentRefNumber, amount.status as status, amount.additionaldetails as additionaldetails, amount.task_id as task_id";
    private static final String FROM_AMOUNT_TABLE = " FROM dristi_task_amount amount";

    private static final String BASE_CASE_EXIST_QUERY = "SELECT COUNT(*) FROM dristi_task task";

    public String checkTaskExistQuery(String cnrNumber, String filingNumber, UUID taskId, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_CASE_EXIST_QUERY);
            boolean firstCriteria = true;
            firstCriteria = addTaskCriteria(cnrNumber, query, firstCriteria, "task.cnrnumber = ?" ,preparedStmtList);
            firstCriteria = addTaskCriteria(filingNumber, query, firstCriteria, "task.filingnumber = ?",preparedStmtList);
            addTaskCriteria(taskId != null ? taskId.toString() : null, query, firstCriteria, "task.id = ?",preparedStmtList);

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building task search query", e);
            throw new CustomException(TASK_SEARCH_QUERY_EXCEPTION, "Error occurred while building the task search query: " + e.getMessage());
        }
    }


    public String getTaskSearchQuery(String id, String tenantId, String status, UUID orderId, String cnrNumber, String taskNumber, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_CASE_QUERY);
            query.append(FROM_TASK_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria

            firstCriteria = addTaskCriteria(id, query, firstCriteria, "task.id = ?", preparedStmtList);
            firstCriteria = addTaskCriteria(tenantId, query, firstCriteria, "task.tenantid = ?", preparedStmtList);
            firstCriteria = addTaskCriteria(status, query, firstCriteria, "task.status = ?", preparedStmtList);
            firstCriteria = addTaskCriteria(orderId != null ? orderId.toString() : null, query, firstCriteria, "task.orderid = ?", preparedStmtList);
            firstCriteria = addTaskCriteria(cnrNumber, query, firstCriteria, "task.cnrnumber = ?", preparedStmtList);
            addTaskCriteria(taskNumber, query, firstCriteria, "task.tasknumber = ?", preparedStmtList);

            query.append(ORDERBY_CREATEDTIME);

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building task search query :: {}", e.toString());
            throw new CustomException(TASK_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the task search query: " + e.getMessage());
        }
    }

    private boolean addTaskCriteria(String criteria, StringBuilder query, boolean firstCriteria, String str, List<Object> preparedStmtList) {
        if (criteria != null && !criteria.isEmpty()) {
            addClauseIfRequired(query, firstCriteria);
            query.append(str);
            preparedStmtList.add(criteria);
            firstCriteria = false;
        }
        return firstCriteria;
    }


    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY_CASE);
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
            log.error("Error while building document search query :: {}",e.toString());
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the query for task document search: " + e.getMessage());
        }
    }

    public String getAmountSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(AMOUNT_SELECT_QUERY_CASE);
            query.append(FROM_AMOUNT_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE amount.task_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while building amount search query :: {}",e.toString());
            throw new CustomException(AMOUNT_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the query for amount: " + e.getMessage());
        }
    }

    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }

}