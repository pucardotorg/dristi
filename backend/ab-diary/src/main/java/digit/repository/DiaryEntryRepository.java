package digit.repository;

import digit.repository.querybuilder.DiaryEntryQueryBuilder;
import digit.repository.rowmapper.DiaryEntryRowMapper;
import digit.web.models.CaseDiaryEntry;
import digit.web.models.CaseDiaryExistCriteria;
import digit.web.models.CaseDiarySearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static digit.config.ServiceConstants.*;

@Repository
@Slf4j
public class DiaryEntryRepository {

    private final DiaryEntryQueryBuilder queryBuilder;

    private final JdbcTemplate jdbcTemplate;

    private final DiaryEntryRowMapper diaryEntryRowMapper;

    public DiaryEntryRepository(DiaryEntryQueryBuilder queryBuilder, JdbcTemplate jdbcTemplate, DiaryEntryRowMapper diaryEntryRowMapper) {
        this.queryBuilder = queryBuilder;
        this.jdbcTemplate = jdbcTemplate;
        this.diaryEntryRowMapper = diaryEntryRowMapper;
    }

    public List<CaseDiaryEntry> getCaseDiaryEntries(CaseDiarySearchRequest searchRequest) {

        try {

            List<Object> preparedStmtList = new ArrayList<>();
            List<Integer> preparedStmtArgList = new ArrayList<>();

            String diaryEntryQuery = queryBuilder.getDiaryEntryQuery(searchRequest.getCriteria(), preparedStmtList, preparedStmtArgList);
            diaryEntryQuery = queryBuilder.addOrderByQuery(diaryEntryQuery, searchRequest.getPagination());
            log.info("Diary Entry query : {} ", diaryEntryQuery);

            if (searchRequest.getPagination() != null) {

                Integer totalRecords = getTotalCount(diaryEntryQuery, preparedStmtList);
                log.info("Total count without pagination :: {}", totalRecords);
                searchRequest.getPagination().setTotalCount(Double.valueOf(totalRecords));
                diaryEntryQuery = queryBuilder.addPaginationQuery(diaryEntryQuery, preparedStmtList, searchRequest.getPagination(), preparedStmtArgList);
                log.info("Post Pagination Query :: {}", diaryEntryQuery);

            }

            if (preparedStmtList.size() != preparedStmtArgList.size()) {
                log.info("Arg size :: {}, and ArgType size :: {}", preparedStmtList.size(), preparedStmtArgList.size());
                throw new CustomException(DIARY_ENTRY_QUERY_EXCEPTION, "Arg and ArgType size mismatch ");
            }

            return jdbcTemplate.query(diaryEntryQuery, preparedStmtList.toArray(), preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(), diaryEntryRowMapper);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching diary entries");
            throw new CustomException(DIARY_ENTRY_SEARCH_EXCEPTION, "Error occurred while retrieving data from the database");
        }


    }

    public Integer getTotalCount(String baseQuery, List<Object> preparedStmtList) {
        String countQuery = queryBuilder.getTotalCountQuery(baseQuery);
        log.info("Final count query :: {}", countQuery);
        return jdbcTemplate.queryForObject(countQuery, Integer.class, preparedStmtList.toArray());
    }

    public List<CaseDiaryEntry> getExistingDiaryEntry(CaseDiaryExistCriteria caseDiaryExistCriteria) {

        try {

            List<Object> preparedStmtList = new ArrayList<>();
            List<Integer> preparedStmtArgList = new ArrayList<>();

            String diaryEntryExistQuery = queryBuilder.getExistingDiaryEntry(caseDiaryExistCriteria, preparedStmtList, preparedStmtArgList);

            log.info("Diary Entry Exist query : {} ", diaryEntryExistQuery);

            if (preparedStmtList.size() != preparedStmtArgList.size()) {
                log.info("Arg size :: {}, and ArgType size :: {}", preparedStmtList.size(), preparedStmtArgList.size());
                throw new CustomException(DIARY_ENTRY_QUERY_EXCEPTION, "Arg and ArgType size mismatch ");
            }

            return jdbcTemplate.query(diaryEntryExistQuery, preparedStmtList.toArray(), preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(), diaryEntryRowMapper);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching diary entries");
            throw new CustomException(DIARY_ENTRY_SEARCH_EXCEPTION, "Error occurred while retrieving data from the database");
        }

    }

}
