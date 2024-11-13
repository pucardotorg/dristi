package com.egov.icops_integrationkerala.repository;

import com.egov.icops_integrationkerala.model.IcopsTracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class IcopsRepository {

    private final JdbcTemplate jdbcTemplate;

    private final IcopsQueryBuilder queryBuilder;

    private final IcopsRowMapper rowMapper;

    @Autowired
    public IcopsRepository(JdbcTemplate jdbcTemplate, IcopsQueryBuilder queryBuilder, IcopsRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryBuilder = queryBuilder;
        this.rowMapper = rowMapper;
    }

    public List<IcopsTracker> getIcopsTracker(String processUniqueId){
        List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getIcopsTracker(processUniqueId, preparedStmtList);
        log.info("Final query: " + query);
        return jdbcTemplate.query(query, rowMapper, preparedStmtList.toArray());
    }
}
