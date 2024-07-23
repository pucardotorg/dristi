package org.pucar.dristi.repository.querybuilder;

import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class AdvocateQueryBuilder {
    private static final String BASE_ATR_QUERY = " SELECT adv.id as id, adv.tenantid as tenantid, adv.applicationnumber as applicationnumber, adv.barregistrationnumber as barregistrationnumber, adv.advocateType as advocatetype, adv.organisationID as organisationid, adv.individualid as individualid, adv.isactive as isactive, adv.additionaldetails as additionaldetails, adv.createdby as createdby, adv.lastmodifiedby as lastmodifiedby, adv.createdtime as createdtime, adv.lastmodifiedtime as lastmodifiedtime, adv.status as status ";

    private static final String DOCUMENT_SELECT_QUERY = "Select doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.advocateid as advocateid ";
    private static final String FROM_ADVOCATES_TABLE = " FROM dristi_advocate adv";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_document doc";
    private static final String ORDERBY_CREATEDTIME_DESC = " ORDER BY adv.createdtime DESC ";
    private static final String ORDERBY_CREATEDTIME_ASC = " ORDER BY adv.createdtime ASC ";

    /**   /** To build query using search criteria to search advocate
     * @param preparedStmtList
     * @param limit
     * @param offset
     * @return
     */
    public String getAdvocateSearchQuery(AdvocateSearchCriteria criteria, List<Object> preparedStmtList, String tenantId, Integer limit, Integer offset) {
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            query.append(FROM_ADVOCATES_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria
            if(criteria != null) {
                if (criteria.getId()!=null && !criteria.getId().isEmpty()) {
                    addClauseIfRequired(query, firstCriteria);
                    query.append("adv.id = ?");
                    preparedStmtList.add(criteria.getId());
                    firstCriteria = false; // Update firstCriteria flag
                }

                if (criteria.getBarRegistrationNumber()!=null && !criteria.getBarRegistrationNumber().isEmpty()) {
                    addClauseIfRequired(query, firstCriteria);
                    query.append("adv.barRegistrationNumber = ?");
                    preparedStmtList.add(criteria.getBarRegistrationNumber());
                    firstCriteria = false; // Update firstCriteria flag

                }

                if (criteria.getApplicationNumber()!=null && !criteria.getApplicationNumber().isEmpty()) {
                    addClauseIfRequired(query, firstCriteria);
                    query.append("adv.applicationNumber = ?");
                    preparedStmtList.add(criteria.getApplicationNumber());
                    firstCriteria = false;
                }

                if (criteria.getIndividualId()!=null && !criteria.getIndividualId().isEmpty()) {
                    addClauseIfRequired(query, firstCriteria);
                    query.append("adv.individualId = ?");
                    preparedStmtList.add(criteria.getIndividualId());
                    firstCriteria = false;
                }

                if ((criteria.getId()!=null && !criteria.getId().isEmpty()) || (criteria.getBarRegistrationNumber()!=null && !criteria.getBarRegistrationNumber().isEmpty()) || criteria.getApplicationNumber()!=null && !criteria.getApplicationNumber().isEmpty()
                        || criteria.getIndividualId()!=null && !criteria.getIndividualId().isEmpty()) {
                    query.append(")");
                }

                if(tenantId != null && !tenantId.isEmpty()){
                    addClauseIfRequiredForTenantId(query, firstCriteria);
                    query.append("LOWER(adv.tenantid) = LOWER(?)");
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
         catch (Exception e) {
            log.error("Error while building advocate search query :: {}",e.toString());
            throw new CustomException(ADVOCATE_SEARCH_QUERY_EXCEPTION,"Exception occurred while building the advocate search query: "+ e.getMessage());
        }
    }

    public String getAdvocateSearchQueryByStatus(String status, List<Object> preparedStmtList, String tenantId, Integer limit, Integer offset){
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            query.append(FROM_ADVOCATES_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria

            if(status != null && !status.isEmpty()){
                addClauseIfRequiredForStatus(query, firstCriteria);
                query.append("LOWER(adv.status) LIKE LOWER(?)")
                        .append(")");
                preparedStmtList.add(status.toLowerCase());
                firstCriteria = false;
            }
            if(tenantId != null && !tenantId.isEmpty()){
                addClauseIfRequiredForTenantId(query, firstCriteria);
                query.append("LOWER(adv.tenantid) LIKE LOWER(?)");
                preparedStmtList.add(tenantId.toLowerCase());
                firstCriteria = false;
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
            log.error("Error while building advocate search query :: {}",e.toString());
            throw new CustomException(ADVOCATE_SEARCH_QUERY_EXCEPTION,"Exception occurred while building the advocate search query: "+ e.getMessage());
        }
    }

    public String getAdvocateSearchQueryByApplicationNumber(String applicationNumber, List<Object> preparedStmtList, String tenantId, Integer limit, Integer offset){
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            query.append(FROM_ADVOCATES_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria

            if(applicationNumber != null && !applicationNumber.isEmpty()){
                addClauseIfRequiredForStatus(query, firstCriteria);
                query.append("LOWER(adv.applicationnumber) LIKE LOWER(?)")
                        .append(")");
                preparedStmtList.add("%" + applicationNumber.toLowerCase() + "%");
                firstCriteria = false;
            }
            if(tenantId != null && !tenantId.isEmpty()){
                addClauseIfRequiredForTenantId(query, firstCriteria);
                query.append("LOWER(adv.tenantid) LIKE LOWER(?)");
                preparedStmtList.add("%" + tenantId.toLowerCase() + "%");
                firstCriteria = false;
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
            log.error("Error while building advocate search query :: {}",e.toString());
            throw new CustomException(ADVOCATE_SEARCH_QUERY_EXCEPTION,"Exception occurred while building the advocate search query: "+ e.getMessage());
        }
    }

    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE (");
        } else {
            query.append(" OR ");
        }
    }

    private void addClauseIfRequiredForStatus(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE (");
        } else {
            query.append(" AND ");
        }
    }

    private void addClauseIfRequiredForTenantId(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }

    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.advocateid IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building document search query :: {}",e.toString());
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,"Exception occurred while building the query: "+ e.getMessage());
        }
    }
}