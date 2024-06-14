package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.querybuilder.AdvocateClerkQueryBuilder;
import org.pucar.dristi.repository.rowmapper.AdvocateClerkDocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.AdvocateClerkRowMapper;
import org.pucar.dristi.web.models.AdvocateClerk;
import org.pucar.dristi.web.models.AdvocateClerkSearchCriteria;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.ADVOCATE_CLERK_SEARCH_EXCEPTION;


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

    /** Used to get applications based on search criteria using query
     * @param searchCriteria
     * @param limit
     * @param offset
     * @return list of clerks found in the DB
     */
    public List<AdvocateClerk> getApplications(List<AdvocateClerkSearchCriteria> searchCriteria, String tenantId, Integer limit, Integer offset){
        try {
            for (AdvocateClerkSearchCriteria advocateSearchCriteria : searchCriteria) {
                List<Object> preparedStmtList = new ArrayList<>();
                List<Object> preparedStmtListDoc = new ArrayList<>();
                String query = queryBuilder.getAdvocateClerkSearchQuery(advocateSearchCriteria, preparedStmtList, tenantId, limit, offset);
                log.info("Final query :: {} ", query);
                List<AdvocateClerk> advocateClerkList = jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
                if (advocateClerkList != null) {
                    advocateSearchCriteria.setResponseList(advocateClerkList);
                }

                List<String> ids = new ArrayList<>();
                for (AdvocateClerk advocate : advocateSearchCriteria.getResponseList()) {
                    ids.add(advocate.getId().toString());
                }
                if (ids.isEmpty()) {
                    advocateSearchCriteria.setResponseList(new ArrayList<>());
                    continue;
                }

                String advocateDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
                log.info("Final query Document :: {}", advocateDocumentQuery);
                Map<UUID, List<Document>> advocateDocumentMap = jdbcTemplate.query(advocateDocumentQuery, preparedStmtListDoc.toArray(), documentRowMapper);
                if (advocateDocumentMap != null) {
                    advocateSearchCriteria.getResponseList().forEach(advocate -> {
                        advocate.setDocuments(advocateDocumentMap.get(advocate.getId()));
                    });
                }
            }
            return searchCriteria.get(0).getResponseList();
        } catch(CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching advocate clerk application list :: {}", e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION,"Error while fetching advocate clerk application list: "+e.getMessage());
        }
    }

    public List<AdvocateClerk> getApplicationsByStatus(String status, String tenantId, Integer limit, Integer offset){
        try {
            List<AdvocateClerk> advocateClerkList = new ArrayList<>();
            List<Object> preparedStmtList = new ArrayList<>();
            List<Object> preparedStmtListDoc = new ArrayList<>();
            String query = queryBuilder.getAdvocateClerkSearchQueryByStatus(status, preparedStmtList, tenantId, limit, offset);
            log.info("Final query ::  {}", query);
            List<AdvocateClerk> list = jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
            if (list != null) {
                advocateClerkList.addAll(list);
            }

            List<String> ids = new ArrayList<>();
            for (AdvocateClerk advocate : advocateClerkList) {
                ids.add(advocate.getId().toString());
            }
            if (ids.isEmpty()) {
                return advocateClerkList;
            }

            String advocateDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
            log.info("Final query Document :: {}", advocateDocumentQuery);
            Map<UUID, List<Document>> advocateDocumentMap = jdbcTemplate.query(advocateDocumentQuery, preparedStmtListDoc.toArray(), documentRowMapper);
            if (advocateDocumentMap != null) {
                advocateClerkList.forEach(advocate -> {
                    advocate.setDocuments(advocateDocumentMap.get(advocate.getId()));
                });
            }
            return advocateClerkList;
        } catch(CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching advocate clerk application list :: {}", e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION,"Error while fetching advocate clerk application list: "+e.getMessage());
        }
    }

    public List<AdvocateClerk> getApplicationsByAppNumber(String applicationNumber, String tenantId, Integer limit, Integer offset){
        try {
            List<AdvocateClerk> advocateClerkList = new ArrayList<>();
            List<Object> preparedStmtList = new ArrayList<>();
            List<Object> preparedStmtListDoc = new ArrayList<>();
            String query = queryBuilder.getAdvocateClerkSearchQueryByAppNumber(applicationNumber, preparedStmtList, tenantId, limit, offset);
            log.info("Final query :: {}", query);
            List<AdvocateClerk> list = jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
            if (list != null) {
                advocateClerkList.addAll(list);
            }

            List<String> ids = new ArrayList<>();
            for (AdvocateClerk advocate : advocateClerkList) {
                ids.add(advocate.getId().toString());
            }
            if (ids.isEmpty()) {
                return advocateClerkList;
            }

            String advocateDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
            log.info("Final query Document :: {}", advocateDocumentQuery);
            Map<UUID, List<Document>> advocateDocumentMap = jdbcTemplate.query(advocateDocumentQuery, preparedStmtListDoc.toArray(), documentRowMapper);
            if (advocateDocumentMap != null) {
                advocateClerkList.forEach(advocate -> {
                    advocate.setDocuments(advocateDocumentMap.get(advocate.getId()));
                });
            }
            return advocateClerkList;
        } catch(CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching advocate clerk application list :: {}", e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION,"Error while fetching advocate clerk application list: "+e.getMessage());
        }
    }
}
