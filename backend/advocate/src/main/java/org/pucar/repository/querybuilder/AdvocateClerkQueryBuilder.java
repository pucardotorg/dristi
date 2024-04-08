package org.pucar.repository.querybuilder;

import org.pucar.web.models.AdvocateClerkSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class AdvocateClerkQueryBuilder {

    private static final String BASE_ATR_QUERY = " SELECT advc.id as aid, advc.tenantid as atenantid, advc.applicationnumber as aapplicationnumber, advc.stateregnnumber as stateregnnumber, advc.individualid as aindividualid, advc.isactive as aisactive, advc.additionaldetails as aadditionaldetails, advc.createdby as acreatedby, advc.lastmodifiedby as alastmodifiedby, advc.createdtime as acreatedtime, advc.lastmodifiedtime as alastmodifiedtime, ";

    private static final String DOCUMENT_SELECT_QUERY = " doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as additionaldetails ";
    private static final String FROM_TABLES = " FROM dristi_advocate advc LEFT JOIN distri_document doc ON advc.id = doc.registrationid";
    private final String ORDERBY_CREATEDTIME = " ORDER BY advc.createdtime DESC ";

    public String getAdvocateClerkSearchQuery(AdvocateClerkSearchCriteria criteria, List<Object> preparedStmtList){
        StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
        query.append(DOCUMENT_SELECT_QUERY);
        query.append(FROM_TABLES);

        if(!ObjectUtils.isEmpty(criteria.getId())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" advc.id = ? ");
            preparedStmtList.add(criteria.getId());
        }
        if(!ObjectUtils.isEmpty(criteria.getStateRegnNumber())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" advc.stateregnnumber = ? ");
            preparedStmtList.add(criteria.getStateRegnNumber());
        }
        if(!ObjectUtils.isEmpty(criteria.getApplicationNumber())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" advc.applicationnumber = ? ");
            preparedStmtList.add(criteria.getApplicationNumber());
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

    private void addToPreparedStatement(List<Object> preparedStmtList, List<String> ids) {
        ids.forEach(id -> {
            preparedStmtList.add(id);
        });
    }
}