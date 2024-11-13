package drishti.payment.calculator.repository;


import drishti.payment.calculator.repository.querybuilder.PostalHubQueryBuilder;
import drishti.payment.calculator.repository.rowmapper.PostalHubRowMapper;
import drishti.payment.calculator.web.models.HubSearchCriteria;
import drishti.payment.calculator.web.models.PostalHub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class PostalHubRepository {

    private final PostalHubRowMapper rowMapper;
    private final PostalHubQueryBuilder queryBuilder;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostalHubRepository(PostalHubRowMapper rowMapper, PostalHubQueryBuilder queryBuilder, JdbcTemplate jdbcTemplate) {
        this.rowMapper = rowMapper;
        this.queryBuilder = queryBuilder;
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<PostalHub> getPostalHub(HubSearchCriteria criteria) {
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgsList = new ArrayList<>();
        String query = queryBuilder.getPostalHubQuery(criteria, preparedStmtList, preparedStmtArgsList);
        log.debug("Final query: " + query);
        return jdbcTemplate.query(query, preparedStmtList.toArray(), preparedStmtArgsList.stream().mapToInt(Integer::intValue).toArray(), rowMapper);
    }
}
