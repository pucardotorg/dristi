package org.pucar.repository;

import lombok.extern.slf4j.Slf4j;
import org.pucar.repository.querybuilder.AdvocateClerkQueryBuilder;

import org.pucar.repository.rowmapper.AdvocateClerkRowMapper;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkSearchCriteria;
import org.pucar.web.models.AdvocateSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Repository
public class AdvocateClerkRepository {

    @Autowired
    private AdvocateClerkQueryBuilder queryBuilder;

    @Autowired
    private AdvocateClerkRowMapper rowMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<AdvocateClerk> getApplications(AdvocateClerkSearchCriteria searchCriteria){
        List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getAdvocateClerkSearchQuery(searchCriteria, preparedStmtList);
        log.info("Final query: " + query);
        List<AdvocateClerk> advocateClerkList = jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
        return advocateClerkList;
    }
}