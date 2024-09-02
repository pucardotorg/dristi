package drishti.payment.calculator.repository;


import drishti.payment.calculator.repository.querybuilder.PostalServiceQueryBuilder;
import drishti.payment.calculator.repository.rowmapper.PostalServiceRowMapper;
import drishti.payment.calculator.web.models.PostalService;
import drishti.payment.calculator.web.models.PostalServiceSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class PostalServiceRepository {

    private final PostalServiceRowMapper rowMapper;
    private final PostalServiceQueryBuilder queryBuilder;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostalServiceRepository(PostalServiceRowMapper rowMapper, PostalServiceQueryBuilder queryBuilder, JdbcTemplate jdbcTemplate) {
        this.rowMapper = rowMapper;
        this.queryBuilder = queryBuilder;
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<PostalService> getPostalService(PostalServiceSearchCriteria criteria) {
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgsList = new ArrayList<>();
        String query = queryBuilder.getPostalServiceQuery(criteria, preparedStmtList, preparedStmtArgsList);
        log.debug("Final query: " + query);
        return jdbcTemplate.query(query, preparedStmtList.toArray(), preparedStmtArgsList.stream().mapToInt(Integer::intValue).toArray() ,  rowMapper);
    }


}
