package org.pucar.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.pucar.repository.querybuilder.AdvocateQueryBuilder;
import org.pucar.repository.rowmapper.AdvocateDocumentRowMapper;
import org.pucar.repository.rowmapper.AdvocateRowMapper;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.pucar.config.ServiceConstants.ADVOCATE_SEARCH_EXCEPTION;


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

    public List<Advocate> getApplications(List<AdvocateSearchCriteria> searchCriteria, List<String> statusList, String applicationNumber, AtomicReference<Boolean> isIndividualLoggedInUser) {

        try {
            List<Advocate> advocateList = new ArrayList<>();
            List<Object> preparedStmtList = new ArrayList<>();
            List<Object> preparedStmtListDoc = new ArrayList<>();
            String advocateQuery = "";
            advocateQuery = queryBuilder.getAdvocateSearchQuery(searchCriteria, preparedStmtList, statusList, applicationNumber, isIndividualLoggedInUser);
            log.info("Final advocate list query: {}", advocateQuery);
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
            log.info("Final document query: {}", advocateDocumentQuery);
            Map<UUID, List<Document>> advocateDocumentMap = jdbcTemplate.query(advocateDocumentQuery, preparedStmtListDoc.toArray(), advocateDocumentRowMapper);
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
            log.error("Error while fetching advocate application list");
            throw new CustomException(ADVOCATE_SEARCH_EXCEPTION,"Error while fetching advocate application list: "+e.getMessage());
        }
    }

}
