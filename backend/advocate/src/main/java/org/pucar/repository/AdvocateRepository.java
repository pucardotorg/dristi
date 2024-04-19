package org.pucar.repository;

import lombok.extern.slf4j.Slf4j;
import org.pucar.repository.querybuilder.AdvocateQueryBuilder;
import org.pucar.repository.rowmapper.AdvocateRowMapper;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;
import org.pucar.web.models.AdvocateSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Repository
public class AdvocateRepository {

    @Autowired
    private AdvocateQueryBuilder queryBuilder;

    @Autowired
    private AdvocateRowMapper rowMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Advocate> getApplications(AdvocateSearchCriteria searchCriteria){
        List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getAdvocateSearchQuery(searchCriteria, preparedStmtList);
        log.info("Final query: " + query);
        List<Advocate> advocateList = jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
        return advocateList;
    }
}
