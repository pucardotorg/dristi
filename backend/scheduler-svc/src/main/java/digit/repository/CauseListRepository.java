package digit.repository;

import digit.repository.querybuilder.CauseListQueryBuilder;
import digit.repository.rowmapper.CauseListRowMapper;
import digit.web.models.CauseList;
import digit.web.models.CauseListSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class CauseListRepository {

    private CauseListQueryBuilder queryBuilder;

    private CauseListRowMapper rowMapper;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CauseListRepository(CauseListQueryBuilder queryBuilder, CauseListRowMapper rowMapper, JdbcTemplate jdbcTemplate) {
        this.queryBuilder = queryBuilder;
        this.rowMapper = rowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<CauseList> getCauseLists(CauseListSearchCriteria searchCriteria) {

        List<String> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getCauseListQuery(searchCriteria, preparedStmtList);
        log.debug("Final query: " + query);
        return jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
    }
}
