package org.pucar.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.pucar.repository.querybuilder.AdvocateClerkQueryBuilder;
import org.pucar.repository.rowmapper.AdvocateClerkDocumentRowMapper;
import org.pucar.repository.rowmapper.AdvocateClerkRowMapper;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkSearchCriteria;
import org.pucar.web.models.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.pucar.config.ServiceConstants.ADVOCATE_CLERK_SEARCH_EXCEPTION;


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

    public List<AdvocateClerk> getApplications(List<AdvocateClerkSearchCriteria> searchCriteria, List<String> statusList, String applicationNumber, AtomicReference<Boolean> isIndividualLoggedInUser, Integer limit, Integer offset, Pagination pagination){
        try {
            List<AdvocateClerk> advocateList = new ArrayList<>();
            List<Object> preparedStmtList = new ArrayList<>();
            List<Object> preparedStmtListDoc = new ArrayList<>();
            String query = queryBuilder.getAdvocateClerkSearchQuery(searchCriteria, preparedStmtList, statusList, applicationNumber, isIndividualLoggedInUser, limit, offset, pagination);
            log.info("Final query: " + query);
            List<AdvocateClerk> list = jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
            Integer totalCount = (list != null) ? list.size() : 0;

            //changing query to get total count
            if (limit != null && offset != null) {
                int limitIndex = query.toUpperCase().indexOf("LIMIT");
                if (limitIndex != -1) {
                    query = query.substring(0, limitIndex).trim();
                }
                if (preparedStmtList.size() >= 2) {
                    // Remove the last element
                    preparedStmtList.remove(preparedStmtList.size() - 1);

                    // Remove the new last element (which was the second last originally)
                    preparedStmtList.remove(preparedStmtList.size() - 1);
                }
                List<AdvocateClerk> allResults = jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
                totalCount = (allResults != null) ? allResults.size() : 0;
            }
            pagination.setTotalCount(Double.valueOf(totalCount));
            pagination.setSortBy("createdtime");
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
