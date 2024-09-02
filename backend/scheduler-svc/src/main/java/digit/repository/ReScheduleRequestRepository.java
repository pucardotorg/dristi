package digit.repository;

import digit.repository.querybuilder.ReScheduleHearingQueryBuilder;
import digit.repository.rowmapper.ReScheduleHearingRowMapper;
import digit.web.models.ReScheduleHearing;
import digit.web.models.ReScheduleHearingReqSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class ReScheduleRequestRepository {

    @Autowired
    private ReScheduleHearingRowMapper rowMapper;

    @Autowired
    private ReScheduleHearingQueryBuilder queryBuilder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ReScheduleHearing> getReScheduleRequest(ReScheduleHearingReqSearchCriteria criteria, Integer limit, Integer offset) {

        List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getReScheduleRequestQuery(criteria, preparedStmtList, limit, offset);
        log.debug("Final query: " + query);
        return jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);


    }
}
