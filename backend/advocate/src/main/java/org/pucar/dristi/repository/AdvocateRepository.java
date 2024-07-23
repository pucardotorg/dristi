package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.querybuilder.AdvocateQueryBuilder;
import org.pucar.dristi.repository.rowmapper.AdvocateDocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.AdvocateRowMapper;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.pucar.dristi.config.ServiceConstants.ADVOCATE_SEARCH_EXCEPTION;


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


    /** Used to get applications based on search criteria using query
     * @param searchCriteria
     * @param limit
     * @param offset
     * @return list of advocate found in the DB
     */
    public List<AdvocateSearchCriteria> getApplications(List<AdvocateSearchCriteria> searchCriteria, String tenantId, Integer limit, Integer offset ) {

        try {

            for (AdvocateSearchCriteria advocateSearchCriteria : searchCriteria) {
                List<Object> preparedStmtList = new ArrayList<>();
                List<Object> preparedStmtListDoc = new ArrayList<>();
                String advocateQuery = "";
                advocateQuery = queryBuilder.getAdvocateSearchQuery(advocateSearchCriteria, preparedStmtList, tenantId, limit, offset);
                log.info("Final advocate list query :: {}", advocateQuery);
                List<Advocate> list = jdbcTemplate.query(advocateQuery, preparedStmtList.toArray(), rowMapper);
                log.info("Application size :: {}", list);
                if (list != null) {
                    advocateSearchCriteria.setResponseList(list);
                }

                List<String> ids = new ArrayList<>();
                for (Advocate advocate : advocateSearchCriteria.getResponseList()) {
                    ids.add(advocate.getId().toString());
                }
                if (ids.isEmpty()) {
                    advocateSearchCriteria.setResponseList(new ArrayList<>());
                    continue;
                }

                String advocateDocumentQuery = "";
                advocateDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
                log.info("Final document query :: {}", advocateDocumentQuery);
                Map<UUID, List<Document>> advocateDocumentMap = jdbcTemplate.query(advocateDocumentQuery, preparedStmtListDoc.toArray(), advocateDocumentRowMapper);
                if (advocateDocumentMap != null) {
                    advocateSearchCriteria.getResponseList().forEach(advocate -> {
                        advocate.setDocuments(advocateDocumentMap.get(advocate.getId()));
                    });
                }
            }

            return searchCriteria; // Use this return validate function used by update API
        }
        catch(CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching advocate application list :: {}", e.toString());
            throw new CustomException(ADVOCATE_SEARCH_EXCEPTION,"Error while fetching advocate application list: "+e.getMessage());
        }
    }

    public List<Advocate> getListApplicationsByStatus(String status, String tenantId, Integer limit, Integer offset ) {

        try {
            List<Advocate> advocateList = new ArrayList<>();
            List<Object> preparedStmtList = new ArrayList<>();
            List<Object> preparedStmtListDoc = new ArrayList<>();
            String advocateQuery = "";
            advocateQuery = queryBuilder.getAdvocateSearchQueryByStatus(status, preparedStmtList, tenantId, limit, offset);
            log.info("Final advocate list query :: {}", advocateQuery);
            List<Advocate> list = jdbcTemplate.query(advocateQuery, preparedStmtList.toArray(), rowMapper);
            if (list != null) {
                advocateList.addAll(list);
            }

            List<String> ids = new ArrayList<>();
            for (Advocate advocate : advocateList) {
                ids.add(advocate.getId().toString());
            }
            if (ids.isEmpty()) {
                return advocateList;
            }

            String advocateDocumentQuery = "";
            advocateDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
            log.info("Final document query :: {}", advocateDocumentQuery);
            Map<UUID, List<Document>> advocateDocumentMap = jdbcTemplate.query(advocateDocumentQuery, preparedStmtListDoc.toArray(), advocateDocumentRowMapper);
            if (advocateDocumentMap != null) {
                advocateList.forEach(advocate -> {
                    advocate.setDocuments(advocateDocumentMap.get(advocate.getId()));
                });
            }

            return advocateList;
        }
        catch(CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching advocate application list :: {}", e.toString());
            throw new CustomException(ADVOCATE_SEARCH_EXCEPTION,"Error while fetching advocate application list: "+e.getMessage());
        }
    }

    public List<Advocate> getListApplicationsByApplicationNumber(String applicationNumber, String tenantId, Integer limit, Integer offset ) {

        try {
            List<Advocate> advocateList = new ArrayList<>();
            List<Object> preparedStmtList = new ArrayList<>();
            List<Object> preparedStmtListDoc = new ArrayList<>();
            String advocateQuery = "";
            advocateQuery = queryBuilder.getAdvocateSearchQueryByApplicationNumber(applicationNumber, preparedStmtList, tenantId, limit, offset);
            log.info("Final advocate list query :: {}", advocateQuery);
            List<Advocate> list = jdbcTemplate.query(advocateQuery, preparedStmtList.toArray(), rowMapper);
            if (list != null) {
                advocateList.addAll(list);
            }

            List<String> ids = new ArrayList<>();
            for (Advocate advocate : advocateList) {
                ids.add(advocate.getId().toString());
            }
            if (ids.isEmpty()) {
                return advocateList;
            }

            String advocateDocumentQuery = "";
            advocateDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
            log.info("Final document query :: {}", advocateDocumentQuery);
            Map<UUID, List<Document>> advocateDocumentMap = jdbcTemplate.query(advocateDocumentQuery, preparedStmtListDoc.toArray(), advocateDocumentRowMapper);
            if (advocateDocumentMap != null) {
                advocateList.forEach(advocate -> {
                    advocate.setDocuments(advocateDocumentMap.get(advocate.getId()));
                });
            }

            return advocateList;
        }
        catch(CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching advocate application list :: {}",e.toString());
            throw new CustomException(ADVOCATE_SEARCH_EXCEPTION,"Error while fetching advocate application list: "+e.getMessage());
        }
    }

}
