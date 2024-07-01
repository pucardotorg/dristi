package org.pucar.dristi.repository.queryBuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.Pagination;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
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
    private static final String ORDERBY_CREATEDTIME_DESC = " ORDER BY app.createdtime DESC ";
    private static final String ORDERBY_CREATEDTIME_ASC = " ORDER BY app.createdtime ASC ";

    private static final String BASE_APPLICATION_EXIST_QUERY = "SELECT COUNT(*) FROM dristi_application app WHERE ";

    public String checkApplicationExistQuery(String filingNumber, String cnrNumber, String applicationNumber) {
        try {
            StringBuilder query = new StringBuilder(BASE_APPLICATION_EXIST_QUERY);
            boolean hasPreviousCondition = false;

            if (filingNumber != null && !filingNumber.isEmpty()) {
                query.append("app.filingnumber = '").append(filingNumber).append("'");
                hasPreviousCondition = true;
            }

            if (cnrNumber != null && !cnrNumber.isEmpty()) {
                if (hasPreviousCondition) {
                    query.append(" AND ");
                }
                query.append("app.cnrnumber = '").append(cnrNumber).append("'");
                hasPreviousCondition = true;
            }

            if (applicationNumber != null && !applicationNumber.isEmpty()) {
                if (hasPreviousCondition) {
                    query.append(" AND ");
                }
                query.append("app.applicationnumber = '").append(applicationNumber).append("'");
            }

            query.append(";");
            return query.toString();
        } catch (Exception e) {
            log.error("Error while building application exist query");
            throw new CustomException(APPLICATION_EXIST_EXCEPTION, "Error occurred while building the application exist query : " + e.getMessage());
        }
    }

    public String getApplicationSearchQuery(String id, String filingNumber, String cnrNumber, String tenantId, String status,String applicationNumber) {
        try {
            StringBuilder query = new StringBuilder(BASE_APP_QUERY);
            query.append(FROM_APP_TABLE);

            boolean firstCriteria = true; // To check if it's the first criteria
            if(id != null && !id.isEmpty()){
                addClauseIfRequired(query, firstCriteria);
                query.append("app.id =").append("'").append(id).append("'");
                firstCriteria = false; // Update firstCriteria flag
            }
            if(filingNumber != null && !filingNumber.isEmpty()){
                addClauseIfRequired(query, firstCriteria);
                query.append("app.filingNumber =").append("'").append(filingNumber).append("'");
                firstCriteria = false; // Update firstCriteria flag
             }
            if(cnrNumber != null && !cnrNumber.isEmpty()){
                addClauseIfRequired(query, firstCriteria);
                query.append("app.cnrNumber =").append("'").append(cnrNumber).append("'");
                firstCriteria = false;
            }
            if(tenantId != null && !tenantId.isEmpty()){
                addClauseIfRequired(query, firstCriteria);
                query.append("app.tenantId =").append("'").append(tenantId).append("'");
                firstCriteria = false;
            }
            if (status!=null && !status.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("app.status =").append("'").append(status).append("'");
                firstCriteria = false;
            }
            if (applicationNumber!=null && !applicationNumber.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("app.applicationNumber =").append("'").append(applicationNumber).append("'");
                firstCriteria = false;
            }
            query.append(ORDERBY_CREATEDTIME_DESC);
            return query.toString();
        }
        catch (Exception e) {
            log.error("Error while building application search query {}", e.getMessage());
            throw new CustomException(APPLICATION_SEARCH_QUERY_EXCEPTION,"Error occurred while building the application search query: "+ e.getMessage());
        }
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
    public String addPaginationQuery(String query, Pagination pagination) {
        String paginationQuery = query + " LIMIT " + (pagination.getLimit().intValue()) +
                " OFFSET " + pagination.getOffSet().intValue();
        log.info("pagination search query : {}", paginationQuery);
        return paginationQuery;
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


