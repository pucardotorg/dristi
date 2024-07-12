package org.pucar.dristi.repository.queryBuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.Pagination;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class ApplicationQueryBuilder {

    private static final String BASE_APP_QUERY =
            " SELECT app.id as id, app.tenantid as tenantid, app.caseid as caseid, app.filingnumber as filingnumber, app.cnrnumber as cnrnumber," +
                    " app.referenceid as referenceid, app.createddate as createddate, app.applicationcreatedby as applicationcreatedby," +
                    " app.onbehalfof as onbehalfof, app.applicationtype as applicationtype, app.applicationnumber as applicationnumber," +
                    " app.issuedby as issuedby, app.status as status, app.comment as comment, app.isactive as isactive," +
                    " app.additionaldetails as additionaldetails,"+
                    " app.createdby as createdby," +
                    " app.lastmodifiedby as lastmodifiedby, app.createdtime as createdtime, app.lastmodifiedtime as lastmodifiedtime," +
                    " app.status as status ";

    private  static  final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM ({baseQuery}) total_result";

    private static final String DOCUMENT_SELECT_QUERY_APP = "SELECT doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore," +
            "doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.application_id as application_id";

    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_application_document doc";

    private static final String BASE_STATUTE_SECTION_QUERY = " SELECT stse.id as id, stse.tenantid as tenantid" +
            ", stse.statute as statute, stse.application_id as application_id, " +
            "stse.sections as sections, stse.subsections as subsections, stse.strsections as strsections, stse.strsubsections as strsubsections, stse.additionaldetails as additionaldetails, stse.createdby as createdby," +
            " stse.lastmodifiedby as lastmodifiedby, stse.createdtime as createdtime, stse.lastmodifiedtime as lastmodifiedtime";

    private static final String FROM_STATUTE_SECTION_TABLE = " FROM dristi_application_statute_section stse";

    private static final String FROM_APP_TABLE = " FROM dristi_application app";
    private static final String ORDERBY_CLAUSE = " ORDER BY app.{orderBy} {sortingOrder} ";
    private static final String DEFAULT_ORDERBY_CLAUSE = " ORDER BY app.createdtime DESC ";
    private static final String BASE_APPLICATION_EXIST_QUERY = "SELECT COUNT(*) FROM dristi_application app";

    public String checkApplicationExistQuery(String filingNumber, String cnrNumber, String applicationNumber, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_APPLICATION_EXIST_QUERY);
            boolean firstCriteria = true; // To check if it's the first criteria

            firstCriteria = addCriteria(filingNumber, query, firstCriteria, "app.filingNumber = ?", preparedStmtList);
            firstCriteria = addCriteria(cnrNumber, query, firstCriteria, "app.cnrNumber = ?", preparedStmtList);
            addCriteria(applicationNumber, query, firstCriteria, "app.applicationNumber = ?", preparedStmtList);

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building application exist query {}", e.getMessage());
            throw new CustomException(APPLICATION_EXIST_EXCEPTION, "Error occurred while building the application exist query: " + e.getMessage());
        }
    }

    public String getApplicationSearchQuery(String id, String filingNumber, String cnrNumber, String tenantId, String status,String applicationNumber,List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_APP_QUERY);
            query.append(FROM_APP_TABLE);

            boolean firstCriteria = true; // To check if it's the first criteria
            firstCriteria = addCriteria(id, query, firstCriteria, "app.id = ?", preparedStmtList);
            firstCriteria = addCriteria(filingNumber, query, firstCriteria, "app.filingNumber = ?", preparedStmtList);
            firstCriteria = addCriteria(cnrNumber, query, firstCriteria, "app.cnrNumber = ?", preparedStmtList);
            firstCriteria = addCriteria(tenantId, query, firstCriteria, "app.tenantId = ?", preparedStmtList);
            firstCriteria = addCriteria(status, query, firstCriteria, "app.status = ?", preparedStmtList);
            addCriteria(applicationNumber, query, firstCriteria, "app.applicationNumber = ?", preparedStmtList);

            return query.toString();
        }
        catch (Exception e) {
            log.error("Error while building application search query {}", e.getMessage());
            throw new CustomException(APPLICATION_SEARCH_QUERY_EXCEPTION,"Error occurred while building the application search query: "+ e.getMessage());
        }
    }

    boolean addCriteria(String criteria, StringBuilder query, boolean firstCriteria, String str, List<Object> preparedStmtList) {
        if (criteria != null && !criteria.isEmpty()) {
            addClauseIfRequired(query, firstCriteria);
            query.append(str);
            preparedStmtList.add(criteria);
            firstCriteria = false;
        }
        return firstCriteria;
    }

    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }

    public String getTotalCountQuery(String baseQuery) {
        return TOTAL_COUNT_QUERY.replace("{baseQuery}", baseQuery);
    }

    public String addPaginationQuery(String query, Pagination pagination, List<Object> preparedStatementList) {
        preparedStatementList.add(pagination.getLimit());
        preparedStatementList.add(pagination.getOffSet());
        return query + " LIMIT ? OFFSET ?";
    }
    public String addOrderByQuery(String query, Pagination pagination) {
        if (pagination == null || pagination.getSortBy() == null || pagination.getOrder() == null) {
            return query + DEFAULT_ORDERBY_CLAUSE;
        } else {
            query = query + ORDERBY_CLAUSE;
        }
        return query.replace("{orderBy}", pagination.getSortBy()).replace("{sortingOrder}", pagination.getOrder().name());
    }
    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY_APP);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.application_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building document search query {}", e.getMessage());
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION, "Error occurred while building the query: " + e.getMessage());
        }
    }


    public String getStatuteSectionSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_STATUTE_SECTION_QUERY);
            query.append(FROM_STATUTE_SECTION_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE stse.application_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building statute section search query {}", e.getMessage());
            throw new CustomException(STATUTE_SEARCH_QUERY_EXCEPTION, "Error occurred while building the query: " + e.getMessage());
        }
    }
}


