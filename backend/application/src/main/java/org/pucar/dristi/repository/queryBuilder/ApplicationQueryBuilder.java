package org.pucar.dristi.repository.queryBuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.APPLICATION_SEARCH_QUERY_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.DOCUMENT_SEARCH_QUERY_EXCEPTION;

@Component
@Slf4j
public class ApplicationQueryBuilder {

    //TODO WRITE APPLICATION GET QUERY

//    private static final String BASE_APP_QUERY = "SELECT * ";
    private static final String BASE_APP_QUERY = //FIXME
            " SELECT app.id as id, app.tenantid as tenantid, app.filingnumber as filingnumber, app.cnrnumber as cnrnumber," +
                    " app.referenceid as referenceid, app.createddate as createddate, app.applicationcreatedby as applicationcreatedby," +
                    " app.onbehalfof as onbehalfof, app.applicationtype as applicationtype, app.applicationnumber as applicationnumber," +
                    " app.issuedby as issuedby, app.status as status, app.comment as comment, app.isactive as isactive," +
                    " app.additionaldetails as additionaldetails,"+
                    "app.createdby as createdby," +
                    " app.lastmodifiedby as lastmodifiedby, app.createdtime as createdtime, app.lastmodifiedtime as lastmodifiedtime," +
                    " app.status as status ";
    private static final String DOCUMENT_SELECT_QUERY_APP = "SELECT doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore," +
            "doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.application_id as application_id";

    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_application_document doc";

    private static final String BASE_STATUTE_SECTION_QUERY = " SELECT stse.id as id, stse.tenantid as tenantid" +
            ", stse.statute as statute, stse.application_id as application_id, " +
            "stse.sections as sections, stse.subsections as subsections, stse.strsections as strsections, stse.strsubsections as strsubsections, stse.additionaldetails as additionaldetails, stse.createdby as createdby," +
            " stse.lastmodifiedby as lastmodifiedby, stse.createdtime as createdtime, stse.lastmodifiedtime as lastmodifiedtime";

    private static final String FROM_STATUTE_SECTION_TABLE = " FROM dristi_application_statute_section stse";

//    private static final String DOCUMENT_SELECT_QUERY = //FIXME
//            "Select doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.application_id as applicationid ";
    private static final String FROM_APP_TABLE = " FROM dristi_application app"; //FIXME
//    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_document doc"; //FIXME
    private static final String ORDERBY_CREATEDTIME_DESC = " ORDER BY app.createdtime DESC "; //FIXME
    private static final String ORDERBY_CREATEDTIME_ASC = " ORDER BY app.createdtime ASC "; //FIXME

    public String getApplicationSearchQuery(String id, String filingNumber, String cnrNumber, String tenantId, Integer limit, Integer offset) {
        try {
            StringBuilder query = new StringBuilder(BASE_APP_QUERY);
            query.append(FROM_APP_TABLE);

            boolean firstCriteria = true; // To check if it's the first criteria
            if(id != null){
                addClauseIfRequired(query, firstCriteria);
                query.append("app.id =").append("'").append(id).append("'");
                firstCriteria = false; // Update firstCriteria flag
            }
            if(filingNumber != null){
                addClauseIfRequired(query, firstCriteria);
                query.append("app.filingNumber =").append("'").append(filingNumber).append("'");
                firstCriteria = false; // Update firstCriteria flag
             }
            if(cnrNumber != null){
                addClauseIfRequired(query, firstCriteria);
                query.append("app.cnrNumber =").append("'").append(cnrNumber).append("'");
                firstCriteria = false;
            }
            if(tenantId != null){
                addClauseIfRequired(query, firstCriteria);
                query.append("app.tenantId =").append("'").append(tenantId).append("'");
                firstCriteria = false;
            }
            query.append(ORDERBY_CREATEDTIME_DESC);


            // Adding Pagination
//            if (limit != null && offset != null) {
//                query.append(" LIMIT ? OFFSET ?");
//                preparedStmtList.add(limit);
//                preparedStmtList.add(offset);
//            }

            return query.toString();
        }
        catch (Exception e) {
            log.error("Error while building application search query");
            throw new CustomException(APPLICATION_SEARCH_QUERY_EXCEPTION,"Error occurred while building the application search query: "+ e.getMessage());
        }
    }

    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" OR ");
        }
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
            log.error("Error while building document search query");
            throw new CustomException("DOCUMENT_SEARCH_QUERY_EXCEPTION", "Error occurred while building the query: " + e.getMessage());
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
            log.error("Error while building document search query");
            throw new CustomException("DOCUMENT_SEARCH_QUERY_EXCEPTION", "Error occurred while building the query: " + e.getMessage());
        }
    }
}

//    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
//        try {
//            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY_APP);
//            query.append(FROM_DOCUMENTS_TABLE);
//            if (!ids.isEmpty()) {
//                query.append(" WHERE doc.advocateid IN (")
//                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
//                        .append(")");
//                preparedStmtList.addAll(ids);
//            }
//
//            return query.toString();
//        } catch (Exception e) {
//            log.error("Error while building document search query");
//            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,"Error occurred while building the query: "+ e.getMessage());
//        }
//    }


//}

