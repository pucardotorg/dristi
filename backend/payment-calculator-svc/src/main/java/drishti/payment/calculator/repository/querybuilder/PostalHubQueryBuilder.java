package drishti.payment.calculator.repository.querybuilder;

import drishti.payment.calculator.web.models.HubSearchCriteria;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Types;
import java.util.List;

@Component
public class PostalHubQueryBuilder {

    private static final String BASE_APPLICATION_QUERY = "SELECT * ";
    private static final String FROM_TABLES = " FROM postal_hub ph ";

    public String getPostalHubQuery(HubSearchCriteria criteria, List<Object> preparedStmtList, List<Integer> preparedStmtArgsList) {

        StringBuilder query = new StringBuilder(BASE_APPLICATION_QUERY);
        query.append(FROM_TABLES);
        if (!CollectionUtils.isEmpty(criteria.getHubId())) {

            addClauseIfRequired(query, preparedStmtList);
            query.append(" ph.hub_id IN ( ").append(createQuery(criteria.getHubId())).append(" ) ");
            addToPreparedStatement(preparedStmtList, criteria.getHubId());
            preparedStmtArgsList.add(Types.VARCHAR);
        }

        if (!ObjectUtils.isEmpty(criteria.getName())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" ph.name = ? ");
            preparedStmtList.add(criteria.getName());
            preparedStmtArgsList.add(Types.VARCHAR);
        }
        if (!CollectionUtils.isEmpty(criteria.getPincode())) {

            addClauseIfRequired(query, preparedStmtList);
            query.append(" ph.pincode IN ( ").append(createQuery(criteria.getPincode())).append(" ) ");
            addToPreparedStatement(preparedStmtList, criteria.getPincode());
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

    public String createQuery(List<String> ids) {
        StringBuilder builder = new StringBuilder();
        int length = ids.size();
        for (int i = 0; i < length; i++) {
            builder.append(" ?");
            if (i != length - 1)
                builder.append(",");
        }
        return builder.toString();
    }

    public void addToPreparedStatement(List<Object> preparedStmtList, List<String> ids) {
        ids.forEach(id -> {
            preparedStmtList.add(id);
        });
    }
}
