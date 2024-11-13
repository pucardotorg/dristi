package org.egov.sbi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.sbi.model.TransactionDetails;
import org.egov.sbi.model.TransactionSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class TransactionDetailsRepository {

    private final JdbcTemplate jdbcTemplate;

    private final TransactionDetailsQueryBuilder queryBuilder;

    private final TransactionDetailsRowMapper rowMapper;

    @Autowired
    public TransactionDetailsRepository(JdbcTemplate jdbcTemplate, TransactionDetailsQueryBuilder queryBuilder, TransactionDetailsRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryBuilder = queryBuilder;
        this.rowMapper = rowMapper;
    }

    public List<TransactionDetails> getTransactionDetails(TransactionSearchCriteria searchCriteria) {
        List<String> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getTransactionDetailsQuery(searchCriteria, preparedStmtList);
        log.debug("Final query: {}", query);
        return jdbcTemplate.query(query, rowMapper, preparedStmtList.toArray());
    }
}
