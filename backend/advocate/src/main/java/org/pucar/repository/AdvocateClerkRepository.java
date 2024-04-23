package org.pucar.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.pucar.repository.querybuilder.AdvocateClerkQueryBuilder;
import org.pucar.repository.rowmapper.AdvocateClerkDocumentRowMapper;
import org.pucar.repository.rowmapper.AdvocateClerkRowMapper;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Repository
public class AdvocateClerkRepository {

    @Autowired
    private AdvocateClerkQueryBuilder queryBuilder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AdvocateClerkRowMapper rowMapper;

    @Autowired
    private AdvocateClerkDocumentRowMapper documentRowMapper;

    public List<AdvocateClerk> getApplications(List<AdvocateClerkSearchCriteria> searchCriteria, List<String> statusList, String applicationNumber){
        List<AdvocateClerk> advocateList = new ArrayList<>();
        List<Object> preparedStmtList = new ArrayList<>();
        List<Object> preparedStmtListDoc = new ArrayList<>();
        String query = queryBuilder.getAdvocateClerkSearchQuery(searchCriteria, preparedStmtList, statusList, applicationNumber);
        log.info("Final query: " + query);
        try {
            List<AdvocateClerk> list = jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
            if(list != null){
                advocateList.addAll(list);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        List<String> ids = new ArrayList<>();
        for (AdvocateClerk advocate : advocateList) {
            ids.add(advocate.getId().toString());
        }
        if (ids.isEmpty()) {
            return advocateList;
        }

        String advocateDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
        log.info("Final query Document: {}", advocateDocumentQuery);
        try {
            Map<UUID,List<Document>> advocateDocumentMap= jdbcTemplate.query(advocateDocumentQuery, preparedStmtListDoc.toArray(), documentRowMapper);
            if(advocateDocumentMap != null){
                advocateList.forEach(advocate -> {
                    advocate.setDocuments(advocateDocumentMap.get(advocate.getId()));
                });
            }
        } catch (DataAccessException e) {
            log.error("Error occurred while executing database query: {}", e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return advocateList;
    }
}
