package org.pucar.dristi.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.AdvocateClerkSearchCriteria;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class AdvocateClerkQueryBuilder {

    private static final String BASE_ATR_QUERY = "SELECT advc.id as id, advc.tenantid as tenantid, advc.applicationnumber as applicationnumber, advc.stateregnnumber as stateregnnumber, advc.individualid as individualid, advc.isactive as isactive, advc.additionaldetails as additionaldetails, advc.createdby as createdby, advc.lastmodifiedby as lastmodifiedby, advc.createdtime as createdtime, advc.lastmodifiedtime as lastmodifiedtime, advc.status as status ";
    private static final String ADV_TENANT_ID_QUERY = "LOWER(advc.tenantid) = LOWER(?)";

    private static final String DOCUMENT_SELECT_QUERY = "SELECT doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.clerk_id as clerk_id ";
    private static final String FROM_CLERK_TABLES = " FROM dristi_advocate_clerk advc";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_document doc";
    private static final String ORDERBY_CREATEDTIME_DESC = " ORDER BY advc.createdtime DESC ";
    private static final String LIMIT_OFFSET = " LIMIT ? OFFSET ?";

    /** To build query using search criteria to search clerks
     * @param criteria
     * @param preparedStmtList
     * @param tenantId
     * @param limit
     * @param offset
     * @return query
     */
    public String getAdvocateClerkSearchQuery(AdvocateClerkSearchCriteria criteria, List<Object> preparedStmtList, String tenantId, Integer limit, Integer offset) {
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            query.append(FROM_CLERK_TABLES);

            if (criteria != null) {
                addCriteriaToQuery(criteria, query, preparedStmtList);

                if (tenantId != null && !tenantId.isEmpty()) {
                    addClauseIfRequiredForTenantId(query, preparedStmtList);
                    query.append(ADV_TENANT_ID_QUERY);
                    preparedStmtList.add(tenantId.toLowerCase());
                }
            }

            query.append(ORDERBY_CREATEDTIME_DESC);

            // Adding Pagination
            if (limit != null && offset != null) {
                query.append(LIMIT_OFFSET);
                preparedStmtList.add(limit);
                preparedStmtList.add(offset);
            }

            return query.toString();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error(ADVOCATE_CLERK_SEARCH_QUERY_BUILD_EXCEPTION, e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_QUERY_EXCEPTION, ADVOCATE_CLERK_SEARCH_QUERY_BUILD_EXCEPTION + e.getMessage());
        }
    }

    private void addCriteriaToQuery(AdvocateClerkSearchCriteria criteria, StringBuilder query, List<Object> preparedStmtList) {
        boolean hasPreviousClause = false;

        hasPreviousClause = addSingleCriteria(criteria.getId(), "advc.id", query, preparedStmtList, hasPreviousClause);
        hasPreviousClause = addSingleCriteria(criteria.getStateRegnNumber(), "advc.stateregnnumber", query, preparedStmtList, hasPreviousClause);
        hasPreviousClause = addSingleCriteria(criteria.getApplicationNumber(), "advc.applicationNumber", query, preparedStmtList, hasPreviousClause);
        hasPreviousClause = addSingleCriteria(criteria.getIndividualId(), "advc.individualId", query, preparedStmtList, hasPreviousClause);
    }

    private boolean addSingleCriteria(String value, String column, StringBuilder query, List<Object> preparedStmtList, boolean hasPreviousClause) {
        if (value != null && !value.isEmpty()) {
            if (hasPreviousClause) {
                query.append(" AND ");
            } else {
                addClauseIfRequired(query, preparedStmtList);
                hasPreviousClause = true;
            }
            query.append(column).append(" = ? ");
            preparedStmtList.add(value);
        }
        return hasPreviousClause;
    }


    public String getAdvocateClerkSearchQueryByStatus(String status, List<Object> preparedStmtList, String tenantId, Integer limit, Integer offset){
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            query.append(FROM_CLERK_TABLES);

            if(status != null && !status.isEmpty()){
                addClauseIfRequiredForStatus(query, preparedStmtList);
                query.append("LOWER(advc.status) = LOWER(?)")
                        .append(")");
                preparedStmtList.add(status.toLowerCase());
            }

            if(tenantId != null && !tenantId.isEmpty()){
                addClauseIfRequiredForTenantId(query, preparedStmtList);
                query.append(ADV_TENANT_ID_QUERY);
                preparedStmtList.add(tenantId.toLowerCase());
            }

            query.append(ORDERBY_CREATEDTIME_DESC);

            // Adding Pagination
            if (limit != null && offset != null) {
                query.append(LIMIT_OFFSET);
                preparedStmtList.add(limit);
                preparedStmtList.add(offset);
            }

            return query.toString();
        }
        catch (Exception e) {
            log.error(ADVOCATE_CLERK_SEARCH_QUERY_BUILD_EXCEPTION, e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_QUERY_EXCEPTION,ADVOCATE_CLERK_SEARCH_QUERY_BUILD_EXCEPTION+ e.getMessage());
        }
    }

    public String getAdvocateClerkSearchQueryByAppNumber(String applicationNumber, List<Object> preparedStmtList, String tenantId, Integer limit, Integer offset){
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            query.append(FROM_CLERK_TABLES);

            if(applicationNumber != null && !applicationNumber.isEmpty()){
                addClauseIfRequiredForStatus(query, preparedStmtList);
                query.append("LOWER(advc.applicationnumber) = LOWER(?)")
                        .append(")");
                preparedStmtList.add(applicationNumber.toLowerCase());
            }

            if(tenantId != null && !tenantId.isEmpty()){
                addClauseIfRequiredForTenantId(query, preparedStmtList);
                query.append(ADV_TENANT_ID_QUERY);
                preparedStmtList.add(tenantId.toLowerCase());
            }

            query.append(ORDERBY_CREATEDTIME_DESC);

            // Adding Pagination
            if (limit != null && offset != null) {
                query.append(LIMIT_OFFSET);
                preparedStmtList.add(limit);
                preparedStmtList.add(offset);
            }

            return query.toString();
        }
        catch (Exception e) {
            log.error(ADVOCATE_CLERK_SEARCH_QUERY_BUILD_EXCEPTION,e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_QUERY_EXCEPTION,ADVOCATE_CLERK_SEARCH_QUERY_BUILD_EXCEPTION+ e.getMessage());
        }
    }

    void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList){
        if(preparedStmtList.isEmpty()){
            query.append(" WHERE ");
        }else{
            query.append(AND);
        }
    }

    void addClauseIfRequiredForStatus(StringBuilder query, List<Object> preparedStmtList){
        if(preparedStmtList.isEmpty()){
            query.append(" WHERE ( ");
        }else{
            query.append(AND);
        }
    }

    void addClauseIfRequiredForTenantId(StringBuilder query, List<Object> preparedStmtList){
        if(preparedStmtList.isEmpty()){
            query.append(" WHERE ");
        }else{
            query.append(AND);
        }
    }

    /** To add condition for fetching documents
     * @param ids
     * @param preparedStmtList
     * @return
     */
    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.clerk_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building clerk document search query :: {}",e.toString());
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,"Exception occurred while building the clerk document query: "+ e.getMessage());
        }
    }
}