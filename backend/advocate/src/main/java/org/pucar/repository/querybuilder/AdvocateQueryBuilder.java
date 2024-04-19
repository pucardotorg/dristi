package org.pucar.repository.querybuilder;

import org.pucar.web.models.AdvocateSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.sql.SQLOutput;
import java.util.List;

@Component
public class AdvocateQueryBuilder {

    private static final String BASE_ADV_QUERY = " SELECT adv.id as id, adv.tenantId as tenantId, adv.applicationNumber as applicationNumber, adv.barRegistrationNumber as barRegistrationNumber" +
            ", adv.organisationID as organisationID, adv.individualId as individualId, adv.isActive as isActive " +
            " , adv.createdBy as createdBy, adv.lastModifiedBy as lastModifiedBy, adv.createdTime as createdTime, adv.lastModifiedTime as lastModifiedTime  FROM dristi_advocate adv ";

  //  private static final String ORDERBY_CREATEDTIME = " ORDER BY adv.createdTime DESC ;";

    public String getAdvocateSearchQuery(AdvocateSearchCriteria criteria, List<Object> preparedStmtList){
        StringBuilder query = new StringBuilder(BASE_ADV_QUERY);

//        if(!ObjectUtils.isEmpty(criteria.getBarRegistrationNumber())){
//            addClauseIfRequired(query, preparedStmtList);
//            query.append(" adv.barRegistrationNumber = ? ");
//            preparedStmtList.add(criteria.getBarRegistrationNumber());
//        }
        if(!ObjectUtils.isEmpty(criteria.getApplicationNumber())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" adv.applicationNumber = ? ");
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