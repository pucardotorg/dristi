package org.egov.sbi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.sbi.model.TransactionSearchCriteria;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TransactionDetailsQueryBuilder {


    private static final String BASE_APPLICATION_QUERY = "SELECT merchant_id, operating_mode, merchant_country, merchant_currency, posting_amount, other_details, aggregator_id, merchant_order_number, merchant_customer_id, pay_mode, access_medium, transaction_source, created_by, last_modified_by, created_time, last_modified_time, transaction_status, sbi_epay_ref_id, reason, bank_code, bank_reference_number, transaction_date, cin, total_fee_gst, row_number, ref1, ref2, ref3, ref4, ref5, ref6, ref7, ref8, ref9, tenant_id, bill_id, total_due, business_service, service_number, payer_name, paid_by, mobile_number, amount_details ";


    private static final String FROM_TABLES = " FROM transaction_details ";

    public String getTransactionDetailsQuery(TransactionSearchCriteria searchCriteria, List<String> preparedStmtList) {
        StringBuilder query = new StringBuilder(BASE_APPLICATION_QUERY);
        query.append(FROM_TABLES);
        if(searchCriteria.getMerchantOrderNumber() != null){
            addClauseIfRequired(query,preparedStmtList);
            query.append(" merchant_order_number = ? ");
            preparedStmtList.add(searchCriteria.getMerchantOrderNumber());
        }
        if(searchCriteria.getTenantId() != null){
            addClauseIfRequired(query,preparedStmtList);
            query.append(" tenant_id = ? ");
            preparedStmtList.add(searchCriteria.getTenantId());
        }
        if(searchCriteria.getBillId() != null){
            addClauseIfRequired(query,preparedStmtList);
            query.append(" bill_id = ? ");
            preparedStmtList.add(searchCriteria.getBillId());
        }
        return query.toString();
    }

    private void addClauseIfRequired(StringBuilder query, List<String> preparedStmtList) {
        if (preparedStmtList.isEmpty()) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }
}
