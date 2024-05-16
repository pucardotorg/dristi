package org.pucar.dristi.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.AdvocateClerkSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
     * @param criteriaList
     * @param preparedStmtList
     * @param statusList
     * @param applicationNumber
     * @param isIndividualLoggedInUser
     * @param limit
     * @param offset
     * @return query
     */
    public String getAdvocateClerkSearchQuery(List<AdvocateClerkSearchCriteria> criteriaList, List<Object> preparedStmtList, List<String> statusList, String applicationNumber, AtomicReference<Boolean> isIndividualLoggedInUser, Integer limit, Integer offset) {
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            query.append(FROM_CLERK_TABLES);

            if (!CollectionUtils.isEmpty(criteriaList)) {
                buildCriteriaQuery(criteriaList, query, preparedStmtList);
            } else if (applicationNumber != null && !applicationNumber.isEmpty()) {
                addApplicationNumberClause(query, preparedStmtList, applicationNumber);
            }

            addStatusClause(query, preparedStmtList, statusList);

            addOrderByClause(query, isIndividualLoggedInUser);

            addPaginationClause(query, preparedStmtList, limit, offset);

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building advocate clerk search query");
            throw new CustomException(ADVOCATE_CLERK_SEARCH_QUERY_EXCEPTION, "Error occurred while building the advocate search query: " + e.getMessage());
        }
    }

    private void buildCriteriaQuery(List<AdvocateClerkSearchCriteria> criteriaList, StringBuilder query, List<Object> preparedStmtList) {
        List<String> ids = new ArrayList<>();
        List<String> stateRegnNumbers = new ArrayList<>();
        List<String> applicationNumbers = new ArrayList<>();
        List<String> individualIds = new ArrayList<>();

        for (AdvocateClerkSearchCriteria criteria : criteriaList) {
            if (criteria.getId() != null) {
                ids.add(criteria.getId());
            } else if (criteria.getStateRegnNumber() != null) {
                stateRegnNumbers.add(criteria.getStateRegnNumber());
            } else if (criteria.getApplicationNumber() != null) {
                applicationNumbers.add(criteria.getApplicationNumber());
            } else if (criteria.getIndividualId() != null) {
                individualIds.add(criteria.getIndividualId());
            }
        }

        appendCriteriaClause(query, preparedStmtList, "advc.id", ids);
        appendCriteriaClause(query, preparedStmtList, "advc.stateregnnumber", stateRegnNumbers);
        appendCriteriaClause(query, preparedStmtList, "advc.applicationnumber", applicationNumbers);
        appendCriteriaClause(query, preparedStmtList, "advc.individualid", individualIds);
    }

    private void appendCriteriaClause(StringBuilder query, List<Object> preparedStmtList, String columnName, List<String> values) {
        if (!values.isEmpty()) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(columnName).append(" IN (").append(createQuery(values)).append(") ");
            addToPreparedStatement(preparedStmtList, values);
        }
    }

    private void addApplicationNumberClause(StringBuilder query, List<Object> preparedStmtList, String applicationNumber) {
        addClauseIfRequired(query, preparedStmtList);
        query.append("LOWER(advc.applicationNumber) LIKE LOWER(?) ");
        preparedStmtList.add("%" + applicationNumber.toLowerCase() + "%");
    }

    private void addStatusClause(StringBuilder query, List<Object> preparedStmtList, List<String> statusList) {
        if (!CollectionUtils.isEmpty(statusList)) {
            addClauseIfRequiredForStatus(query, preparedStmtList);
            query.append("advc.status IN (").append(createQuery(statusList)).append(") ");
            addToPreparedStatement(preparedStmtList, statusList);
        }
    }

    private void addOrderByClause(StringBuilder query, AtomicReference<Boolean> isIndividualLoggedInUser) {
        query.append(isIndividualLoggedInUser.get() ? ORDERBY_CREATEDTIME_DESC : ORDERBY_CREATEDTIME_ASC);
    }

    private void addPaginationClause(StringBuilder query, List<Object> preparedStmtList, Integer limit, Integer offset) {
        if (limit != null && offset != null) {
            query.append(" LIMIT ? OFFSET ? ");
            preparedStmtList.add(limit);
            preparedStmtList.add(offset);
        }
    }


    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList){
        if(preparedStmtList.isEmpty()){
            query.append(" WHERE (");
        }else{
            query.append(" OR ");
        }
    }

    private void addToPreparedStatement(List<Object> preparedStmtList, List<String> list) {
        preparedStmtList.addAll(list);
    }

    private void addClauseIfRequiredForStatus(StringBuilder query, List<Object> preparedStmtList){
        if(preparedStmtList.isEmpty()){
            query.append(" WHERE ");
        }else{
            query.append(" AND ");
        }
    }

    private String createQuery(List<String> ids) {
        StringBuilder builder = new StringBuilder();
        int length = ids.size();
        for (int i = 0; i < length; i++) {
            builder.append(" ?");
            if (i != length - 1)
                builder.append(",");
        }
        return builder.toString();
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
            log.error("Error while building clerk document search query");
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,"Error occurred while building the clerk document query: "+ e.getMessage());
        }
    }
}