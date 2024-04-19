package org.pucar.repository.querybuilder;

import org.pucar.web.models.AdvocateClerkSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AdvocateClerkQueryBuilder {

    private static final String BASE_ATR_QUERY = "SELECT advc.id as id, advc.tenantid as tenantid, advc.applicationnumber as applicationnumber, advc.stateregnnumber as stateregnnumber, advc.individualid as individualid, advc.isactive as isactive, advc.additionaldetails as additionaldetails, advc.createdby as createdby, advc.lastmodifiedby as lastmodifiedby, advc.createdtime as createdtime, advc.lastmodifiedtime as lastmodifiedtime, advc.status as status ";

    private static final String DOCUMENT_SELECT_QUERY = "SELECT doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.clerk_id as clerk_id ";
    private static final String FROM_CLERK_TABLES = " FROM dristi_advocate_clerk advc";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_document doc";
    private final String ORDERBY_CREATEDTIME = " ORDER BY advc.createdtime DESC ";

    public String getAdvocateClerkSearchQuery(List<AdvocateClerkSearchCriteria> criteriaList, List<Object> preparedStmtList, List<String> statusList){
        StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
        query.append(FROM_CLERK_TABLES);

        List<String> ids = criteriaList.stream()
                .map(AdvocateClerkSearchCriteria::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<String> stateRegnNumber = criteriaList.stream()
                .filter(criteria -> criteria.getId() == null)
                .map(AdvocateClerkSearchCriteria::getStateRegnNumber)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<String> applicationNumbers = criteriaList.stream()
                .filter(criteria -> criteria.getId() == null && criteria.getApplicationNumber() == null)
                .map(AdvocateClerkSearchCriteria::getApplicationNumber)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(ids)) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" advc.id IN ( ").append(createQuery(ids)).append(" ) ");
            addToPreparedStatement(preparedStmtList, ids);
        }

        if (!CollectionUtils.isEmpty(stateRegnNumber)) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" advc.stateregnnumber IN ( ").append(createQuery(stateRegnNumber)).append(" ) ");
            addToPreparedStatement(preparedStmtList, stateRegnNumber);
        }

        if (!CollectionUtils.isEmpty(applicationNumbers)) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" advc.applicationnumber IN ( ").append(createQuery(applicationNumbers)).append(" ) ");
            addToPreparedStatement(preparedStmtList, applicationNumbers);
        }

        if (statusList!=null && !CollectionUtils.isEmpty(statusList)) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" advc.status IN ( ").append(createQuery(statusList)).append(" ) ");
            addToPreparedStatement(preparedStmtList, statusList);
        }

        query.append(ORDERBY_CREATEDTIME);

        return query.toString();
    }

    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList){
        if(preparedStmtList.isEmpty()){
            query.append(" WHERE ");
        }else{
            query.append(" OR ");
        }
    }

    private void addToPreparedStatement(List<Object> preparedStmtList, List<String> list) {
        preparedStmtList.addAll(list);
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

    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY);
        query.append(FROM_DOCUMENTS_TABLE);

        try {
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.clerk_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while building the query", e);
        }
    }
}