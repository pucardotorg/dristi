package org.egov.sbi.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.sbi.model.AmountDetails;
import org.egov.sbi.model.TransactionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TransactionDetailsRowMapper implements RowMapper<TransactionDetails> {

    private final ObjectMapper objectMapper;

    @Autowired
    public TransactionDetailsRowMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public TransactionDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

        List<AmountDetails> amountDetails = new ArrayList<>();
        String amountDetailsString = rs.getString("amount_details");
        try {
            if (amountDetailsString != null) {
                amountDetails = objectMapper.readValue(amountDetailsString, new TypeReference<List<AmountDetails>>() {});
            }
        } catch (Exception e) {
            log.error("Error deserializing 'amount_details' field: {}", amountDetailsString, e);
            throw new SQLException("Error deserializing 'amount_details' field: " + amountDetailsString, e);
        }

        AuditDetails auditdetails = AuditDetails.builder()
                .createdBy(rs.getString("created_by"))
                .createdTime(rs.getLong("created_time"))
                .lastModifiedBy(rs.getString("last_modified_by"))
                .lastModifiedTime(rs.getLong("last_modified_time"))
                .build();

        return TransactionDetails.builder()
                .merchantId(rs.getString("merchant_id"))
                .operatingMode(rs.getString("operating_mode"))
                .merchantCountry(rs.getString("merchant_country"))
                .merchantCurrency(rs.getString("merchant_currency"))
                .postingAmount(rs.getDouble("posting_amount"))
                .otherDetails(rs.getString("other_details"))
                .aggregatorId(rs.getString("aggregator_id"))
                .merchantOrderNumber(rs.getString("merchant_order_number"))
                .merchantCustomerId(rs.getString("merchant_customer_id"))
                .payMode(rs.getString("pay_mode"))
                .accessMedium(rs.getString("access_medium"))
                .transactionSource(rs.getString("transaction_source"))
                .auditDetails(auditdetails)
                .sbiEpayRefId(rs.getString("sbi_epay_ref_id"))
                .transactionStatus(rs.getString("transaction_status"))
                .reason(rs.getString("reason"))
                .bankCode(rs.getString("bank_code"))
                .bankReferenceNumber(rs.getString("bank_reference_number"))
                .transactionDate(rs.getString("transaction_date"))
                .cin(rs.getString("cin"))
                .totalFeeGst(rs.getDouble("total_fee_gst"))
                .rowNumber(rs.getInt("row_number"))
                .ref1(rs.getString("ref1"))
                .ref2(rs.getString("ref2"))
                .ref3(rs.getString("ref3"))
                .ref4(rs.getString("ref4"))
                .ref5(rs.getString("ref5"))
                .ref6(rs.getString("ref6"))
                .ref7(rs.getString("ref7"))
                .ref8(rs.getString("ref8"))
                .ref9(rs.getString("ref9"))
                .tenantId(rs.getString("tenant_id"))
                .billId(rs.getString("bill_id"))
                .totalDue(rs.getDouble("total_due"))
                .businessService(rs.getString("business_service"))
                .serviceNumber(rs.getString("service_number"))
                .payerName(rs.getString("payer_name"))
                .paidBy(rs.getString("paid_by"))
                .mobileNumber(rs.getString("mobile_number"))
                .amountDetails(amountDetails)
                .build();
    }
}
