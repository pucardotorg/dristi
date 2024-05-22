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
    private static final String BASE_APP_QUERY = //FIXME
            " SELECT app.id as id, app.tenantid as tenantid, adv.applicationnumber as " +
            "applicationnumber, adv.barregistrationnumber as barregistrationnumber, adv.advocateType as advocatetype, " +
            "adv.organisationID as organisationid, adv.individualid as individualid, adv.isactive as isactive, adv.additionaldetails " +
            "as additionaldetails, adv.createdby as createdby, adv.lastmodifiedby as lastmodifiedby, adv.createdtime as createdtime, adv.lastmodifiedtime as lastmodifiedtime, adv.status as status ";

    private static final String DOCUMENT_SELECT_QUERY = //FIXME
            "Select doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.advocateid as advocateid ";
    private static final String FROM_APP_TABLE = " FROM dristi_application adv"; //FIXME
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_document doc"; //FIXME
    private static final String ORDERBY_CREATEDTIME_DESC = " ORDER BY app.createdtime DESC "; //FIXME
    private static final String ORDERBY_CREATEDTIME_ASC = " ORDER BY app.createdtime ASC "; //FIXME

    public String getApplicationSearchQuery(List<Object> preparedStmtList, UUID id, String filingNumber, String cnrNumber, String tenantId, Integer limit, Integer offset) {
        try {
            StringBuilder query = new StringBuilder(BASE_APP_QUERY);
            query.append(FROM_APP_TABLE);

            boolean firstCriteria = true; // To check if it's the first criteria
            if(id != null){
                addClauseIfRequired(query, firstCriteria);
                query.append("app.id = ?")
                        .append(")");
                preparedStmtList.add(id);
                firstCriteria = false; // Update firstCriteria flag
            }
            if(filingNumber != null){
                addClauseIfRequired(query, firstCriteria);
                query.append("app.filingNumber = ?")
                        .append(")");
                preparedStmtList.add(filingNumber);
                firstCriteria = false; // Update firstCriteria flag
            }
            if(cnrNumber != null){
                addClauseIfRequired(query, firstCriteria);
                query.append("app.cnrNumber = ?")
                        .append(")");
                preparedStmtList.add(cnrNumber);
                firstCriteria = false; // Update firstCriteria flag
            }
            if(tenantId != null){
                addClauseIfRequired(query, firstCriteria);
                query.append("app.tenantId = ?")
                        .append(")");
                preparedStmtList.add(tenantId);
                firstCriteria = false; // Update firstCriteria flag
            }
            query.append(")");
            query.append(ORDERBY_CREATEDTIME_DESC);


            // Adding Pagination
            if (limit != null && offset != null) {
                query.append(" LIMIT ? OFFSET ?");
                preparedStmtList.add(limit);
                preparedStmtList.add(offset);
            }

            return query.toString();
        }
        catch (Exception e) {
            log.error("Error while building application search query");
            throw new CustomException(APPLICATION_SEARCH_QUERY_EXCEPTION,"Error occurred while building the application search query: "+ e.getMessage());
        }
    }

    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE (");
        } else {
            query.append(" OR ");
        }
    }

//    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
//        try {
//            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY);
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


}

