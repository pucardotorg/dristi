package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.querybuilder.AdvocateClerkQueryBuilder;
import org.pucar.dristi.repository.rowmapper.AdvocateClerkDocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.AdvocateClerkRowMapper;
import org.pucar.dristi.web.models.AdvocateClerk;
import org.pucar.dristi.web.models.AdvocateClerkSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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
     * @param statusList
     * @param applicationNumber
     * @param isIndividualLoggedInUser
     * @param limit
     * @param offset
     * @return list of clerks found in the DB
     */
    public List<AdvocateClerk> getApplications(List<AdvocateClerkSearchCriteria> searchCriteria, List<String> statusList, String applicationNumber, AtomicReference<Boolean> isIndividualLoggedInUser, Integer limit, Integer offset){
        try {
            List<AdvocateClerk> advocateList = new ArrayList<>();
            List<Object> preparedStmtList = new ArrayList<>();
            List<Object> preparedStmtListDoc = new ArrayList<>();
            String query = queryBuilder.getAdvocateClerkSearchQuery(searchCriteria, preparedStmtList, statusList, applicationNumber, isIndividualLoggedInUser, limit, offset);
            log.info("Final query: " + query);
            List<AdvocateClerk> list = jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
            if (list != null) {
                advocateList.addAll(list);
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
            Map<UUID, List<Document>> advocateDocumentMap = jdbcTemplate.query(advocateDocumentQuery, preparedStmtListDoc.toArray(), documentRowMapper);
            if (advocateDocumentMap != null) {
                advocateList.forEach(advocate -> {
                    advocate.setDocuments(advocateDocumentMap.get(advocate.getId()));
                });
            }
            return advocateList;
        }
        catch (CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching advocate clerk application list");
            throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION,"Error while fetching advocate clerk application list: "+e.getMessage());
        }
    }
}
