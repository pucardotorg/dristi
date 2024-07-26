package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.querybuilder.EvidenceQueryBuilder;
import org.pucar.dristi.repository.rowmapper.*;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.EvidenceSearchCriteria;
import org.pucar.dristi.web.models.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class    EvidenceRepository {

    private final EvidenceQueryBuilder queryBuilder;
    private final JdbcTemplate jdbcTemplate;
    private final EvidenceRowMapper evidenceRowMapper;

    @Autowired
    public EvidenceRepository(
            EvidenceQueryBuilder queryBuilder,
            JdbcTemplate jdbcTemplate,
            EvidenceRowMapper evidenceRowMapper

    ) {
        this.queryBuilder = queryBuilder;
        this.jdbcTemplate = jdbcTemplate;
        this.evidenceRowMapper = evidenceRowMapper;
    }

    public List<Artifact> getArtifacts(EvidenceSearchCriteria evidenceSearchCriteria, Pagination pagination) {
        try {
            List<Object> preparedStmtList = new ArrayList<>();

            // Artifact query building
            String artifactQuery = queryBuilder.getArtifactSearchQuery(
                    preparedStmtList,evidenceSearchCriteria
            );
            artifactQuery = queryBuilder.addOrderByQuery(artifactQuery, pagination);
            log.info("Final artifact query: {}", artifactQuery);

            if (pagination != null) {
                Integer totalRecords = getTotalCountArtifact(artifactQuery, preparedStmtList);
                log.info("Total count without pagination :: {}", totalRecords);
                pagination.setTotalCount(Double.valueOf(totalRecords));
                artifactQuery = queryBuilder.addPaginationQuery(artifactQuery, pagination, preparedStmtList);
            }

            List<Artifact> artifactList = jdbcTemplate.query(artifactQuery, preparedStmtList.toArray(), evidenceRowMapper);
            log.info("DB artifact list :: {}", artifactList);

            // Fetch associated comments
            List<String> artifactIds = new ArrayList<>();
            for (Artifact artifact : artifactList) {
                artifactIds.add(artifact.getId().toString());
            }
            if (artifactIds.isEmpty()) {
                return artifactList;
            }

            return artifactList;
        } catch (CustomException e) {
            log.error("Custom Exception while fetching artifact list");
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching artifact list");
            throw new CustomException("ARTIFACT_SEARCH_EXCEPTION", "Error while fetching artifact list: " + e.toString());
        }
    }


    public Integer getTotalCountArtifact(String baseQuery, List<Object> preparedStmtList) {
        String countQuery = queryBuilder.getTotalCountQuery(baseQuery);
        log.info("Final count query :: {}", countQuery);
        return jdbcTemplate.queryForObject(countQuery, preparedStmtList.toArray(), Integer.class);
    }
}

