package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.queryBuilder.ApplicationQueryBuilder;
import org.pucar.dristi.repository.rowMapper.ApplicationRowMapper;
import org.pucar.dristi.repository.rowMapper.DocumentRowMapper;
import org.pucar.dristi.repository.rowMapper.StatuteSectionRowMapper;
import org.pucar.dristi.web.models.Application;
import org.pucar.dristi.web.models.StatuteSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.APPLICATION_SEARCH_ERR;

@Slf4j
@Repository
public class ApplicationRepository {
    @Autowired
    private ApplicationQueryBuilder queryBuilder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ApplicationRowMapper rowMapper;

    @Autowired
    private DocumentRowMapper documentRowMapper;

    @Autowired
    private StatuteSectionRowMapper statuteSectionRowMapper;

    public List<Application> getApplications(String id, String filingNumber, String cnrNumber, String tenantId, Integer limit, Integer offset ) {

        try {
            List<Application> applicationList = new ArrayList<>();
            List<Object> preparedStmtList = new ArrayList<>();
            List<Object> preparedStmtListSt = new ArrayList<>();
            List<Object> preparedStmtListDoc = new ArrayList<>();
            String applicationQuery = queryBuilder.getApplicationSearchQuery(id, filingNumber, cnrNumber, tenantId, limit, offset);
            log.info("Final application search query: {}", applicationQuery);
            List<Application> list = jdbcTemplate.query(applicationQuery, rowMapper);
            log.info("DB application list :: {}", list);
            if (list != null) {
                applicationList.addAll(list);
            }

            List<String> ids = new ArrayList<>();
            for (Application application : applicationList) {
                ids.add(application.getId().toString());
            }
            if (ids.isEmpty()) {
                return applicationList;
            }

            String statueAndSectionQuery = "";
            preparedStmtListSt = new ArrayList<>();
            statueAndSectionQuery = queryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtListSt);
            log.info("Final statue and sections query: {}", statueAndSectionQuery);
            Map<UUID, StatuteSection> statuteSectionsMap = jdbcTemplate.query(statueAndSectionQuery, preparedStmtListSt.toArray(), statuteSectionRowMapper);
            log.info("DB statute sections map :: {}", statuteSectionsMap);
            if (statuteSectionsMap != null) {
                applicationList.forEach(application -> {
                    application.setStatuteSection(statuteSectionsMap.get(application.getId()));
                });
            }

            String documentQuery = "";
            preparedStmtListDoc = new ArrayList<>();
            documentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
            log.info("Final document query: {}", documentQuery);
            Map<UUID, List<Document>> documentMap = jdbcTemplate.query(documentQuery, preparedStmtListDoc.toArray(), documentRowMapper);
            log.info("DB document map :: {}", documentMap);
            if (documentMap != null) {
                applicationList.forEach(application -> {
                    application.setDocuments(documentMap.get(application.getId()));
                });
            }
            return applicationList;
        }
        catch (CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching application list");
            throw new CustomException(APPLICATION_SEARCH_ERR,"Error while fetching application list: "+e.getMessage());
        }
    }
}
