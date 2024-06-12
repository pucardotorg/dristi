package org.pucar.dristi.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.AdvocateClerkSearchCriteria;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.ADVOCATE_CLERK_SEARCH_QUERY_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.DOCUMENT_SEARCH_QUERY_EXCEPTION;

@Component
@Slf4j
public class AdvocateClerkQueryBuilder {

    private static final String BASE_ATR_QUERY = "SELECT advc.id as id, advc.tenantid as tenantid, advc.applicationnumber as applicationnumber, advc.stateregnnumber as stateregnnumber, advc.individualid as individualid, advc.isactive as isactive, advc.additionaldetails as additionaldetails, advc.createdby as createdby, advc.lastmodifiedby as lastmodifiedby, advc.createdtime as createdtime, advc.lastmodifiedtime as lastmodifiedtime, advc.status as status ";

    private static final String DOCUMENT_SELECT_QUERY = "SELECT doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.clerk_id as clerk_id ";
    private static final String FROM_CLERK_TABLES = " FROM dristi_advocate_clerk advc";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_document doc";
    private static final String ORDERBY_CREATEDTIME_DESC = " ORDER BY advc.createdtime DESC ";
    private static final String ORDERBY_CREATEDTIME_ASC = " ORDER BY advc.createdtime ASC ";

    /** To build query using search criteria to search clerks
     * @param criteria
     * @param preparedStmtList
     * @param tenantId
     * @param limit
     * @param offset
     * @return query
     */
    public String getAdvocateClerkSearchQuery(AdvocateClerkSearchCriteria criteria, List<Object> preparedStmtList, String tenantId, Integer limit, Integer offset){
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            query.append(FROM_CLERK_TABLES);
            if(criteria != null) {

                if (criteria.getId()!=null && !criteria.getId().isEmpty()) {
                    addClauseIfRequired(query, preparedStmtList);
                    query.append(" advc.id = ? ");
                    preparedStmtList.add(criteria.getId());
                }

                if (criteria.getStateRegnNumber()!=null && !criteria.getStateRegnNumber().isEmpty()) {
                    addClauseIfRequired(query, preparedStmtList);
                    query.append("advc.stateregnnumber = ?");
                    preparedStmtList.add(criteria.getStateRegnNumber());
                }

                if (criteria.getApplicationNumber()!=null && !criteria.getApplicationNumber().isEmpty()) {
                    addClauseIfRequired(query, preparedStmtList);
                    query.append("advc.applicationNumber = ?");
                    preparedStmtList.add(criteria.getApplicationNumber());
                }

                if (criteria.getIndividualId()!=null && !criteria.getIndividualId().isEmpty()) {
                    addClauseIfRequired(query, preparedStmtList);
                    query.append("advc.individualId = ?");
                    preparedStmtList.add(criteria.getIndividualId());
                }

                if ((criteria.getId()!=null && !criteria.getId().isEmpty()) || (criteria.getStateRegnNumber()!=null && !criteria.getStateRegnNumber().isEmpty()) || criteria.getApplicationNumber()!=null && !criteria.getApplicationNumber().isEmpty()
                        || criteria.getIndividualId()!=null && !criteria.getIndividualId().isEmpty()) {
                    query.append(")");
                }

                if(tenantId != null && !tenantId.isEmpty()){
                    addClauseIfRequiredForTenantId(query, preparedStmtList);
                    query.append("LOWER(advc.tenantid) = LOWER(?)");
                    preparedStmtList.add(tenantId.toLowerCase());
                }
            }

            query.append(ORDERBY_CREATEDTIME_DESC);

            // Adding Pagination
            if (limit != null && offset != null) {
                query.append(" LIMIT ? OFFSET ?");
                preparedStmtList.add(limit);
                preparedStmtList.add(offset);
            }

            return query.toString();
        }
        catch(CustomException e){
            throw e;
        }
        catch (Exception e) {
            log.error("Error while building advocate clerk search query :: {}",e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_QUERY_EXCEPTION,"Exception occurred while building the advocate search query: "+ e.getMessage());
        }
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
                query.append("LOWER(advc.tenantid) = LOWER(?)");
                preparedStmtList.add(tenantId.toLowerCase());
            }

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
            log.error("Error while building advocate clerk search query :: {}", e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_QUERY_EXCEPTION,"Exception occurred while building the advocate search query: "+ e.getMessage());
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
                query.append("LOWER(advc.tenantid) = LOWER(?)");
                preparedStmtList.add(tenantId.toLowerCase());
            }

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
            log.error("Error while building advocate clerk search query :: {}",e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_QUERY_EXCEPTION,"Exception occurred while building the advocate search query: "+ e.getMessage());
        }
    }

    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList){
        if(preparedStmtList.isEmpty()){
            query.append(" WHERE (");
        }else{
            query.append(" OR ");
        }
    }

    private void addClauseIfRequiredForStatus(StringBuilder query, List<Object> preparedStmtList){
        if(preparedStmtList.isEmpty()){
            query.append(" WHERE (");
        }else{
            query.append(" AND ");
        }
    }

    private void addClauseIfRequiredForTenantId(StringBuilder query, List<Object> preparedStmtList){
        if(preparedStmtList.isEmpty()){
            query.append(" WHERE ");
        }else{
            query.append(" AND ");
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