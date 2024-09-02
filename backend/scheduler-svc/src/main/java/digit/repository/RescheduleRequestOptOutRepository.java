package digit.repository;


import digit.repository.querybuilder.RescheduleRequestOptOutQueryBuilder;
import digit.repository.rowmapper.RescheduleRequestOptOutRowMapper;
import digit.web.models.OptOut;
import digit.web.models.OptOutSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class RescheduleRequestOptOutRepository {

    @Autowired
    private RescheduleRequestOptOutRowMapper rescheduleRequestOptOutRowMapper;

    @Autowired
    private RescheduleRequestOptOutQueryBuilder optOutQueryBuilder;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<OptOut> getOptOut(OptOutSearchCriteria optOutSearchCriteria, Integer limit, Integer offset) {

        List<Object> preparedStmtList = new ArrayList<>();
        String query = optOutQueryBuilder.getOptOutQuery(optOutSearchCriteria, preparedStmtList, limit, offset);
        log.debug("Final query: " + query);
        return jdbcTemplate.query(query, preparedStmtList.toArray(), rescheduleRequestOptOutRowMapper);
    }
}
