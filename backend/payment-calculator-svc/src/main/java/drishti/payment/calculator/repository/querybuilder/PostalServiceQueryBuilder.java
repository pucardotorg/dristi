package drishti.payment.calculator.repository.querybuilder;


import drishti.payment.calculator.web.models.PostalServiceSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.sql.Types;
import java.util.List;

@Component
public class PostalServiceQueryBuilder {

    private static final String BASE_APPLICATION_QUERY = "SELECT * ";
    private static final String FROM_TABLES = " FROM postal_service ps ";


    public String getPostalServiceQuery(PostalServiceSearchCriteria criteria, List<Object> preparedStmtList, List<Integer> preparedStmtArgsList) {

            StringBuilder query = new StringBuilder(BASE_APPLICATION_QUERY);
            query.append(FROM_TABLES);

            if(!ObjectUtils.isEmpty(criteria.getId())){
                addClauseIfRequired(query, preparedStmtList);
                query.append(" ps.postal_service_id = ? ");
                preparedStmtList.add(criteria.getId());
                preparedStmtArgsList.add(Types.VARCHAR);
            }
            if(!ObjectUtils.isEmpty(criteria.getPincode())){
                addClauseIfRequired(query, preparedStmtList);
                query.append(" ps.pincode = ? ");
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
