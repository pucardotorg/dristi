package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.queryBuilder.ApplicationQueryBuilder;
import org.pucar.dristi.repository.rowMapper.ApplicationDocumentRowMapper;
import org.pucar.dristi.repository.rowMapper.ApplicationRowMapper;
import org.pucar.dristi.web.models.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
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
    private ApplicationDocumentRowMapper applicationDocumentRowMapper;
    public Application getApplications(UUID id, String filingNumber, String cnrNumber, String tenantId, Integer limit, Integer offset ) {

        try {
            List<Object> preparedStmtList = new ArrayList<>();
            List<Object> preparedStmtListDoc = new ArrayList<>();
            String applicationQuery = queryBuilder.getApplicationSearchQuery(preparedStmtList, id, filingNumber, cnrNumber, tenantId, limit, offset);
            log.info("Final application search query: {}", applicationQuery);
            Application applicationResult = jdbcTemplate.query(applicationQuery, preparedStmtList.toArray(), rowMapper);

//            List<String> ids = new ArrayList<>();
//            for (Advocate advocate : advocateList) {
//                ids.add(advocate.getId().toString());
//            }
//            if (ids.isEmpty()) {
//                return advocateList;
//            }
//
//            String advocateDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
//            log.info("Final document query: {}", advocateDocumentQuery);
//            Map<UUID, List<Document>> advocateDocumentMap = jdbcTemplate.query(advocateDocumentQuery, preparedStmtListDoc.toArray(), advocateDocumentRowMapper);
//            if (advocateDocumentMap != null) {
//                advocateList.forEach(advocate -> advocate.setDocuments(advocateDocumentMap.get(advocate.getId())));
//            }

            return applicationResult;
        }
        catch (CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching advocate application list");
            throw new CustomException(APPLICATION_SEARCH_ERR,"Error while fetching advocate application list: "+e.getMessage());
        }
    }
}
