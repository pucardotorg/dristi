package org.pucar.repository;

import lombok.extern.slf4j.Slf4j;
import org.pucar.repository.querybuilder.AdvocateClerkQueryBuilder;
import org.pucar.repository.rowmapper.AdvocateClerkRowMapper;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkSearchCriteria;
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
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AdvocateClerkRowMapper rowMapper;

    public List<AdvocateClerk> getApplications(List<AdvocateClerkSearchCriteria> searchCriteria){
        List<AdvocateClerk> advocateList = null;
        for (AdvocateClerkSearchCriteria advocateClerkSearchCriteria : searchCriteria
        ) {
            List<Object> preparedStmtList = new ArrayList<>();
            String query = queryBuilder.getAdvocateClerkSearchQuery(advocateClerkSearchCriteria, preparedStmtList);
            log.info("Final query: " + query);
            List<AdvocateClerk> list = jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
            if(list != null){
                advocateList.addAll(list);
            }
        }

        return advocateList;
    }
}
