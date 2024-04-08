package org.pucar.repository;

import lombok.extern.slf4j.Slf4j;
import org.pucar.repository.querybuilder.AdvocateQueryBuilder;
import org.pucar.repository.rowmapper.AdvocateRowMapper;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AdvocateRowMapper rowMapper;

    public List<Advocate> getApplications(List<AdvocateSearchCriteria> searchCriteria) {
        List<Advocate> advocateList = new ArrayList<>();
            List<Object> preparedStmtList = new ArrayList<>();
            String query = queryBuilder.getAdvocateSearchQuery(searchCriteria, preparedStmtList);
            log.info("Final query: {}", query);
            try {
                List<Advocate> advocates = jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
                if(advocates != null){
                    advocateList.addAll(advocates);
                }
            } catch (DataAccessException e) {
                log.error("Error occurred while executing database query: {}", e.getMessage());
                throw e;
            }
        return advocateList;
    }

}
