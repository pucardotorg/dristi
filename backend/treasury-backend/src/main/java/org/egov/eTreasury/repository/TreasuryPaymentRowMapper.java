package org.egov.eTreasury.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.eTreasury.model.TreasuryPaymentData;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Slf4j
public class TreasuryPaymentRowMapper implements RowMapper<TreasuryPaymentData> {
    @Override
    public TreasuryPaymentData mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TreasuryPaymentData.builder()
                .departmentId(rs.getString("department_id"))
                .grn(rs.getString("grn"))
                .challanTimestamp(rs.getString("challan_timestamp"))
                .bankRefNo(rs.getString("bank_ref_no"))
                .bankTimestamp(rs.getString("bank_timestamp"))
                .bankCode(rs.getString("bank_code"))
                .status(rs.getString("status").charAt(0))
                .cin(rs.getString("cin"))
                .amount(rs.getBigDecimal("amount"))
                .partyName(rs.getString("party_name"))
                .remarkStatus(rs.getString("remark_status"))
                .remarks(rs.getString("remarks"))
                .fileStoreId(rs.getString("file_store_id"))
                .build();
    }
}
