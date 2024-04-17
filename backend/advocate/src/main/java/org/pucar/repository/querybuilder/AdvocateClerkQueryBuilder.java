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

    private static final String BASE_ATR_QUERY = " SELECT advc.id as aid, advc.tenantid as atenantid, advc.applicationnumber as aapplicationnumber, advc.stateregnnumber as stateregnnumber, advc.individualid as aindividualid, advc.isactive as aisactive, advc.additionaldetails as aadditionaldetails, advc.createdby as acreatedby, advc.lastmodifiedby as alastmodifiedby, advc.createdtime as acreatedtime, advc.lastmodifiedtime as alastmodifiedtime, ";

    private static final String DOCUMENT_SELECT_QUERY = " doc.id as aaid, doc.documenttype as adocumenttype, doc.filestore as afilestore, doc.documentuid as adocumentuid, doc.additionaldetails as aadditionaldetails ";
    private static final String FROM_TABLES = " FROM dristi_advocate_clerk advc LEFT JOIN distri_documents doc ON advc.id = doc.advocateclerkid";
    private final String ORDERBY_CREATEDTIME = " ORDER BY advc.createdtime DESC ";

    public String getAdvocateClerkSearchQuery(List<AdvocateClerkSearchCriteria> criteria, List<Object> preparedStmtList){
        StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
        query.append(DOCUMENT_SELECT_QUERY);
        query.append(FROM_TABLES);

        List<String> ids = criteria.stream()
                .map(AdvocateClerkSearchCriteria::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<String> applicationNumbers = criteria.stream()
                .map(AdvocateClerkSearchCriteria::getApplicationNumber)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<String> stateRegnNumber = criteria.stream()
                .map(AdvocateClerkSearchCriteria::getStateRegnNumber)
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

        query.append(ORDERBY_CREATEDTIME);

        return query.toString();
    }

    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList){
        if(preparedStmtList.isEmpty()){
            query.append(" WHERE ");
        }else{
            query.append(" AND ");
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
}