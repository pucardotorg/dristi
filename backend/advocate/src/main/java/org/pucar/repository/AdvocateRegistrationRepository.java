package org.pucar.repository;

import lombok.extern.slf4j.Slf4j;
import org.pucar.repository.querybuilder.AdvocateRegistrationQueryBuilder;
import org.pucar.repository.rowmapper.AdvocateRegistrationRowMapper;
import org.pucar.web.models.Advocate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Repository
public class AdvocateRegistrationRepository {

    @Autowired
    private AdvocateRegistrationQueryBuilder queryBuilder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AdvocateRegistrationRowMapper rowMapper;

//    public List<Advocate> getApplications(BirthApplicationSearchCriteria searchCriteria){
//        List<Object> preparedStmtList = new ArrayList<>();
//        String query = queryBuilder.getBirthApplicationSearchQuery(searchCriteria, preparedStmtList);
//        log.info("Final query: " + query);
//        List<BirthRegistrationApplication> list = jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
//        return list;
//    }
}
