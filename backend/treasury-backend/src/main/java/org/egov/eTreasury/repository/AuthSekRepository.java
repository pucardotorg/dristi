package org.egov.eTreasury.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.eTreasury.model.AuthSek;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AuthSekRepository {

    private final JdbcTemplate jdbcTemplate;
    
    private final AuthSekQueryBuilder queryBuilder;

    private final AuthSekRowMapper rowMapper;

    public AuthSekRepository(JdbcTemplate jdbcTemplate, AuthSekQueryBuilder queryBuilder, AuthSekRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryBuilder = queryBuilder;
        this.rowMapper = rowMapper;
    }

    public List<AuthSek> getAuthSek(String authToken) {
        List<String> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getAuthSekQuery(authToken, preparedStmtList);
        log.debug("Final query: {}", query);
        return jdbcTemplate.query(query, rowMapper, preparedStmtList.toArray());
    }
}
