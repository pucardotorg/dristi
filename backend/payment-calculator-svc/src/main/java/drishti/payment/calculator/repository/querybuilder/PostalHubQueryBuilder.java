package drishti.payment.calculator.repository.querybuilder;

import drishti.payment.calculator.web.models.HubSearchCriteria;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;

@Component
public class PostalHubQueryBuilder {


    private static final String BASE_APPLICATION_QUERY = "SELECT * ";
    private static final String FROM_TABLES = " FROM postal_hub ph ";
    private static final String LEFT_JOIN = " LEFT JOIN address a ON ph.addressid = a.id ";

    public String getPostalHubQuery(HubSearchCriteria criteria, List<Object> preparedStmtList, List<Integer> preparedStmtArgsList) {

        StringBuilder query = new StringBuilder(BASE_APPLICATION_QUERY);
        query.append(FROM_TABLES);
        query.append(LEFT_JOIN);
        if(!ObjectUtils.isEmpty(criteria.getHubId())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" ph.hub_id = ? ");
            preparedStmtList.add(criteria.getHubId());
            preparedStmtArgsList.add(Types.VARCHAR);
        }
        if(!ObjectUtils.isEmpty(criteria.getName())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" ph.name = ? ");
            preparedStmtList.add(criteria.getName());
            preparedStmtArgsList.add(Types.VARCHAR);
        }
        if(!ObjectUtils.isEmpty(criteria.getPincode())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" ph.pincode = ? ");
            preparedStmtList.add(criteria.getPincode());
            preparedStmtArgsList.add(Types.VARCHAR);
        }
        return query.toString();

    }


    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
        if (preparedStmtList.isEmpty()) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }
}
