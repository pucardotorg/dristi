package org.pucar.repository.querybuilder;

import org.pucar.web.models.AdvocateClerkSearchCriteria;
import org.pucar.web.models.AdvocateSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class AdvocateClerkQueryBuilder {

    private static final String BASE_ADV_QUERY = " SELECT advClerk.id as id, advClerk.tenantId as tenantId, advClerk.applicationNumber as applicationNumber" +
            ", advClerk.individualId as individualId, advClerk.isActive as isActive " +
            " , advClerk.createdBy as createdBy, advClerk.lastModifiedBy as lastModifiedBy, advClerk.createdTime as createdTime, advClerk.lastModifiedTime as lastModifiedTime  FROM dristi_advocate_clerk advClerk ";

    //  private static final String ORDERBY_CREATEDTIME = " ORDER BY adv.createdTime DESC ;";

    public String getAdvocateClerkSearchQuery(AdvocateClerkSearchCriteria criteria, List<Object> preparedStmtList){
        StringBuilder query = new StringBuilder(BASE_ADV_QUERY);

//        if(!ObjectUtils.isEmpty(criteria.getBarRegistrationNumber())){
//            addClauseIfRequired(query, preparedStmtList);
//            query.append(" adv.barRegistrationNumber = ? ");
//            preparedStmtList.add(criteria.getBarRegistrationNumber());
//        }
        if(!ObjectUtils.isEmpty(criteria.getApplicationNumber())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" advClerk.applicationNumber = ? ");
            preparedStmtList.add(criteria.getApplicationNumber());
        }
        //  query.append(ORDERBY_CREATEDTIME);

        return query.toString();
    }

    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList){
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

    private void addToPreparedStatement(List<Object> preparedStmtList, List<String> ids) {
        ids.forEach(id -> {
            preparedStmtList.add(id);
        });
    }

}