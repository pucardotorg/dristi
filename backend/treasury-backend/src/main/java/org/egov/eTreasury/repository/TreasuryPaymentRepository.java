package org.egov.eTreasury.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.eTreasury.model.TreasuryPaymentData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class TreasuryPaymentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TreasuryPaymentQueryBuilder treasuryPaymentQueryBuilder;
    private final TreasuryPaymentRowMapper treasuryPaymentRowMapper;

    public TreasuryPaymentRepository(JdbcTemplate jdbcTemplate, TreasuryPaymentQueryBuilder treasuryPaymentQueryBuilder, TreasuryPaymentRowMapper treasuryPaymentRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.treasuryPaymentQueryBuilder = treasuryPaymentQueryBuilder;
        this.treasuryPaymentRowMapper = treasuryPaymentRowMapper;
    }
    public List<TreasuryPaymentData> getTreasuryPaymentData(String billId){
        List<String> preparedStmtList = new ArrayList<>();
        String query = treasuryPaymentQueryBuilder.getTreasuryPaymentQuery(billId, preparedStmtList);
        log.debug("Final query: {}", query);
        return jdbcTemplate.query(query, treasuryPaymentRowMapper, preparedStmtList.toArray());
    }
}
