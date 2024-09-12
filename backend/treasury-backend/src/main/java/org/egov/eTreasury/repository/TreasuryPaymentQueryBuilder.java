package org.egov.eTreasury.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
@Slf4j
public class TreasuryPaymentQueryBuilder {

    private static final String BASE_QUERY = "SELECT department_id, grn, challan_timestamp, bank_ref_no, bank_timestamp, bank_code, status, cin, amount, party_name, remark_status, remarks, file_store_id ";
    private static final String FROM_TABLES = "FROM treasury_payment_data ";

    private static final String DEPARTMENT_ID_SUBQUERY = "SELECT department_id FROM auth_sek_session_data WHERE bill_id = ? ";

    private static final String ORDER_BY_SESSION_TIME = "ORDER BY session_time )";

    public String getTreasuryPaymentQuery(String billId, List<String> preparedStmtList) {
        StringBuilder query = new StringBuilder(BASE_QUERY);
        query.append(FROM_TABLES);

        if (StringUtils.hasText(billId)) {
            addClauseIfRequired(query, preparedStmtList);
            query.append("department_id IN (");
            query.append(DEPARTMENT_ID_SUBQUERY);
            query.append(ORDER_BY_SESSION_TIME);
            preparedStmtList.add(billId);
        }

        return query.toString();
    }

    private void addClauseIfRequired(StringBuilder query, List<String> preparedStmtList) {
        if (preparedStmtList.isEmpty()) {
            query.append("WHERE ");
        } else {
            query.append("AND ");
        }
    }
}
