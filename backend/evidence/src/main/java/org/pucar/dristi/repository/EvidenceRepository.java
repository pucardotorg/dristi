    package org.pucar.dristi.repository;

    import lombok.extern.slf4j.Slf4j;
    import org.egov.common.contract.models.Document;
    import org.egov.tracer.model.CustomException;
    import org.pucar.dristi.repository.querybuilder.EvidenceQueryBuilder;
    import org.pucar.dristi.repository.rowmapper.*;
    import org.pucar.dristi.web.models.Artifact;
    import org.pucar.dristi.web.models.Comment;
    import org.pucar.dristi.web.models.EvidenceSearchCriteria;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.jdbc.core.JdbcTemplate;
    import org.springframework.stereotype.Repository;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;
    import java.util.UUID;

    @Slf4j
    @Repository
    public class EvidenceRepository {

        private final EvidenceQueryBuilder queryBuilder;
        private final JdbcTemplate jdbcTemplate;
        private final EvidenceRowMapper evidenceRowMapper;
        private final DocumentRowMapper documentRowMapper;
        private final CommentRowMapper commentRowMapper;

        @Autowired
        public EvidenceRepository(
                EvidenceQueryBuilder queryBuilder,
                JdbcTemplate jdbcTemplate,
                EvidenceRowMapper evidenceRowMapper,
                DocumentRowMapper documentRowMapper,
                CommentRowMapper commentRowMapper
        ) {
            this.queryBuilder = queryBuilder;
            this.jdbcTemplate = jdbcTemplate;
            this.evidenceRowMapper = evidenceRowMapper;
            this.documentRowMapper = documentRowMapper;
            this.commentRowMapper = commentRowMapper;
        }

        public List<Artifact> getArtifacts(EvidenceSearchCriteria evidenceSearchCriteria) {
            try {
                List<Object> preparedStmtListDoc = new ArrayList<>();
                List<Object> preparedStmtListCom = new ArrayList<>();
                List<Object> preparedStmtList=new ArrayList<>();
                String artifactQuery = queryBuilder.getArtifactSearchQuery(
                        preparedStmtList,
                        evidenceSearchCriteria.getId(),
                        evidenceSearchCriteria.getCaseId(),
                        evidenceSearchCriteria.getApplicationNumber(),
                        evidenceSearchCriteria.getHearing(),
                        evidenceSearchCriteria.getOrder(),
                        evidenceSearchCriteria.getSourceId(),
                        evidenceSearchCriteria.getSourceName(),
                        evidenceSearchCriteria.getArtifactNumber()
                );
                log.info("Final artifact query: {}", artifactQuery);

                List<Artifact> artifactList = jdbcTemplate.query(artifactQuery,preparedStmtList.toArray(), evidenceRowMapper);
                log.info("DB artifact list :: {}", artifactList);

                List<String> artifactIds = new ArrayList<>();
                for (Artifact artifact : artifactList) {
                    artifactIds.add(String.valueOf(artifact.getId()));
                }

                if (!artifactIds.isEmpty()) {
                    String documentQuery = queryBuilder.getDocumentSearchQuery(artifactIds, preparedStmtListDoc);
                    log.info("Final document query: {}", documentQuery);
                    Document documentMap = jdbcTemplate.query(documentQuery, preparedStmtListDoc.toArray(), documentRowMapper);
                    log.info("DB document map :: {}", documentMap);

                    String commentQuery = queryBuilder.getCommentSearchQuery(artifactIds, preparedStmtListCom);
                    log.info("Final comment query: {}", commentQuery);
                    List<Comment> commentMap = jdbcTemplate.query(commentQuery, preparedStmtListCom.toArray(), commentRowMapper);
                    log.info("DB comment map :: {}", commentMap);
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

    }

