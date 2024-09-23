package org.pucar.dristi.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.AdvocateClerkSearchCriteria;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class AdvocateClerkQueryBuilder {

    private static final String BASE_ATR_QUERY = "SELECT advc.id as id, advc.tenant_id as tenantid, advc.application_number as applicationnumber, advc.state_regn_number as stateregnnumber, advc.individual_id as individualid, advc.is_active as isactive, advc.additional_details as additionaldetails, advc.created_by as createdby, advc.last_modified_by as lastmodifiedby, advc.created_time as createdtime, advc.last_modified_time as lastmodifiedtime, advc.wf_status as status ";
    private static final String ADV_TENANT_ID_QUERY = "LOWER(advc.tenant_id) = LOWER(?)";

    private static final String DOCUMENT_SELECT_QUERY = "SELECT doc.id as aid, doc.document_type as documenttype, doc.filestore_id as filestore, doc.document_uid as documentuid, doc.additional_details as additionaldetails, doc.clerk_id as clerk_id ";
    private static final String FROM_CLERK_TABLES = " FROM dristi_advocate_clerk advc";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_advocate_clerk_document doc";
    private static final String ORDERBY_CREATEDTIME_DESC = " ORDER BY advc.created_time DESC ";
    private static final String LIMIT_OFFSET = " LIMIT ? OFFSET ?";

    /** To build query using search criteria to search clerks
     * @param criteria
     * @param preparedStmtList
     * @param tenantId
     * @param limit
     * @param offset
     * @return query
     */
    public String getAdvocateClerkSearchQuery(AdvocateClerkSearchCriteria criteria, List<Object> preparedStmtList, List<Integer> preparedStmtArgList,String tenantId, Integer limit, Integer offset) {
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            query.append(FROM_CLERK_TABLES);

            if (criteria != null) {
                addCriteriaToQuery(criteria, query, preparedStmtList,preparedStmtArgList);

                if (tenantId != null && !tenantId.isEmpty()) {
                    addClauseIfRequiredForTenantId(query, preparedStmtList);
                    query.append(ADV_TENANT_ID_QUERY);
                    preparedStmtList.add(tenantId.toLowerCase());
                    preparedStmtArgList.add(Types.VARCHAR);
                }
            }

            query.append(ORDERBY_CREATEDTIME_DESC);

            // Adding Pagination
            addPagination(preparedStmtList,preparedStmtArgList,limit,offset,query);

            return query.toString();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while building advocate clerk search query :: {}", e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_QUERY_EXCEPTION, ADVOCATE_CLERK_SEARCH_QUERY_BUILD_EXCEPTION + e.getMessage());
        }
    }

    private void addCriteriaToQuery(AdvocateClerkSearchCriteria criteria, StringBuilder query, List<Object> preparedStmtList,List<Integer> preparedStmtArgList) {
        boolean hasPreviousClause = false;

        hasPreviousClause = addSingleCriteria(criteria.getId(), "advc.id", query, preparedStmtList, preparedStmtArgList,hasPreviousClause);
        hasPreviousClause = addSingleCriteria(criteria.getStateRegnNumber(), "advc.state_regn_number", query, preparedStmtList, preparedStmtArgList,hasPreviousClause);
        hasPreviousClause = addSingleCriteria(criteria.getApplicationNumber(), "advc.application_number", query, preparedStmtList,preparedStmtArgList, hasPreviousClause);
        addSingleCriteria(criteria.getIndividualId(), "advc.individual_id", query, preparedStmtList, preparedStmtArgList,hasPreviousClause);
    }

    private boolean addSingleCriteria(String value, String column, StringBuilder query, List<Object> preparedStmtList,List<Integer> preparedStmtArgsList, boolean hasPreviousClause) {
        if (value != null && !value.isEmpty()) {
            if (hasPreviousClause) {
                query.append(" AND ");
            } else {
                addClauseIfRequired(query, preparedStmtList);
                hasPreviousClause = true;
            }
            query.append(column).append(" = ? ");
            preparedStmtList.add(value);
            preparedStmtArgsList.add(Types.VARCHAR);
        }
        return hasPreviousClause;
    }


    public String getAdvocateClerkSearchQueryByStatus(String status, List<Object> preparedStmtList,List<Integer> preparedStmtArguList, String tenantId, Integer limit, Integer offset){
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            query.append(FROM_CLERK_TABLES);

            if(status != null && !status.isEmpty()){
                addClauseIfRequiredForStatus(query, preparedStmtList);
                query.append("LOWER(advc.wf_status) = LOWER(?)")
                        .append(")");
                preparedStmtList.add(status.toLowerCase());
                preparedStmtArguList.add(Types.VARCHAR);
            }

            if(tenantId != null && !tenantId.isEmpty()){
                addClauseIfRequiredForTenantId(query, preparedStmtList);
                query.append(ADV_TENANT_ID_QUERY);
                preparedStmtList.add(tenantId.toLowerCase());
                preparedStmtArguList.add(Types.VARCHAR);
            }

            query.append(ORDERBY_CREATEDTIME_DESC);

            // Adding Pagination
            addPagination(preparedStmtList,preparedStmtArguList,limit,offset,query);

            return query.toString();
        }
        catch (Exception e) {
            log.error("Error while building advocate clerk search by status query :: {}", e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_QUERY_EXCEPTION,ADVOCATE_CLERK_SEARCH_QUERY_BUILD_EXCEPTION+ e.getMessage());
        }
    }

    public String getAdvocateClerkSearchQueryByAppNumber(String applicationNumber, List<Object> preparedStmtList, List<Integer> preparedStmtArgumentList, String tenantId, Integer limit, Integer offset){
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            query.append(FROM_CLERK_TABLES);

            if(applicationNumber != null && !applicationNumber.isEmpty()){
                addClauseIfRequiredForStatus(query, preparedStmtList);
                query.append("LOWER(advc.application_number) = LOWER(?)")
                        .append(")");
                preparedStmtList.add(applicationNumber.toLowerCase());
                preparedStmtArgumentList.add(Types.VARCHAR);
            }

            if(tenantId != null && !tenantId.isEmpty()){
                addClauseIfRequiredForTenantId(query, preparedStmtList);
                query.append(ADV_TENANT_ID_QUERY);
                preparedStmtList.add(tenantId.toLowerCase());
                preparedStmtArgumentList.add(Types.VARCHAR);
            }

            query.append(ORDERBY_CREATEDTIME_DESC);

            // Adding Pagination
            addPagination(preparedStmtList,preparedStmtArgumentList,limit,offset,query);

            return query.toString();
        }
        catch (Exception e) {
            log.error("Error while building advocate clerk search by app num query :: {}",e.toString());
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

    private void addPagination(List<Object> preparedStmtList,List<Integer> preparedStmtArgssList, Integer limit, Integer offset, StringBuilder query){
        if (limit != null && offset != null) {
            query.append(LIMIT_OFFSET);
            preparedStmtList.add(limit);
            preparedStmtArgssList.add(Types.INTEGER);

            preparedStmtList.add(offset);
            preparedStmtArgssList.add(Types.INTEGER);

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
    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList,List<Integer> preparedStmtArgDocList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.clerk_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
                ids.forEach(i->preparedStmtArgDocList.add(Types.VARCHAR));
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building clerk document search query :: {}",e.toString());
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,"Exception occurred while building the clerk document query: "+ e.getMessage());
        }
    }
}