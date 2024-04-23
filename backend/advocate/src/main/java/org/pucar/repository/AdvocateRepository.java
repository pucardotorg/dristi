package org.pucar.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.pucar.repository.querybuilder.AdvocateQueryBuilder;
import org.pucar.repository.rowmapper.AdvocateDocumentRowMapper;
import org.pucar.repository.rowmapper.AdvocateRowMapper;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateSearchCriteria;
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
public class AdvocateRepository {

    @Autowired
    private AdvocateQueryBuilder queryBuilder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AdvocateRowMapper rowMapper;
    @Autowired
    private AdvocateDocumentRowMapper advocateDocumentRowMapper;

    public List<Advocate> getApplications(List<AdvocateSearchCriteria> searchCriteria, List<String> statusList, String applicationNumber) {
        List<Advocate> advocateList = new ArrayList<>();
        List<Object> preparedStmtList = new ArrayList<>();
        List<Object> preparedStmtListDoc = new ArrayList<>();
        String advocateQuery = queryBuilder.getAdvocateSearchQuery(searchCriteria, preparedStmtList, statusList, applicationNumber);
        log.info("Final query: {}", advocateQuery);
        try {
            List<Advocate> list = jdbcTemplate.query(advocateQuery, preparedStmtList.toArray(), rowMapper);
            if(list != null){
                advocateList.addAll(list);
            }
        } catch (DataAccessException e) {
            log.error("Error occurred while executing database query: {}", e.getMessage());
            throw e;
        }
        List<String> ids = new ArrayList<>();
        for (Advocate advocate : advocateList) {
            ids.add(advocate.getId().toString());
        }
        if(ids.isEmpty()) {
            return advocateList;
        }

        String advocateDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
        log.info("Final query: {}", advocateDocumentQuery);
        try {
            Map<UUID,List<Document>> advocateDocumentMap= jdbcTemplate.query(advocateDocumentQuery, preparedStmtListDoc.toArray(), advocateDocumentRowMapper);
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
