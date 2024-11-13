package digit.repository;

import digit.repository.querybuilder.CauseListQueryBuilder;
import digit.repository.rowmapper.CaseTypeRowMapper;
import digit.repository.rowmapper.CauseListRowMapper;
import digit.web.models.CaseType;
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

    private CaseTypeRowMapper caseTypeRowMapper;

    @Autowired
    public CauseListRepository(CauseListQueryBuilder queryBuilder, CauseListRowMapper rowMapper, JdbcTemplate jdbcTemplate, CaseTypeRowMapper caseTypeRowMapper) {
        this.queryBuilder = queryBuilder;
        this.rowMapper = rowMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.caseTypeRowMapper = caseTypeRowMapper;
    }


    public List<CauseList> getCauseLists(CauseListSearchCriteria searchCriteria) {

        List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getCauseListQuery(searchCriteria, preparedStmtList);
        log.debug("Final query: " + query);
        return jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
    }

    public List<String> getCauseListFileStore(CauseListSearchCriteria searchCriteria) {
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgsList = new ArrayList<>();
        String query = queryBuilder.getCauseListFileStoreQuery(searchCriteria, preparedStmtList, preparedStmtArgsList);
        log.debug("Final query: " + query);
        return jdbcTemplate.query(query, preparedStmtList.toArray(),  preparedStmtArgsList.stream().mapToInt(Integer::intValue).toArray(),  (rs, rowNum) -> rs.getString("file_store_id"));
    }

    public List<CaseType> getCaseTypes() {
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgsList = new ArrayList<>();
        String query = queryBuilder.getCaseTypeQuery(preparedStmtList, preparedStmtArgsList);
        log.debug("Final query: " + query);
        return jdbcTemplate.query(query, preparedStmtList.toArray(),preparedStmtArgsList.stream().mapToInt(Integer::intValue).toArray(),  caseTypeRowMapper);
    }
}
