package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;

import org.pucar.dristi.repository.querybuilder.HearingQueryBuilder;
import org.pucar.dristi.repository.rowmapper.HearingDocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.HearingRowMapper;
import org.pucar.dristi.web.models.Hearing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;




@Slf4j
@Repository
public class HearingRepository {

    @Autowired
    private HearingQueryBuilder queryBuilder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HearingRowMapper rowMapper;
    @Autowired
    private HearingDocumentRowMapper hearingDocumentRowMapper;


//    public List<Hearing> getHearings(List<HearingSearchCriteria> searchCriteria, List<String> statusList, String applicationNumber, AtomicReference<Boolean> isIndividualLoggedInUser, Integer limit, Integer offset ) {
//
//        try {
//            List<Hearing> hearingList = new ArrayList<>();
//            List<Object> preparedStmtList = new ArrayList<>();
//            List<Object> preparedStmtListDoc = new ArrayList<>();
//            String hearingQuery = "";
//            hearingQuery = queryBuilder.getHearingSearchQuery(searchCriteria, preparedStmtList, statusList, applicationNumber, isIndividualLoggedInUser, limit, offset);
//            log.info("Final hearing list query: {}", hearingQuery);
//            List<Hearing> list = jdbcTemplate.query(hearingQuery, preparedStmtList.toArray(), rowMapper);
//            if (list != null) {
//                hearingList.addAll(list);
//            }
//
//            List<String> ids = new ArrayList<>();
//            for (Hearing hearing : hearingList) {
//                ids.add(hearing.getId().toString());
//            }
//            if (ids.isEmpty()) {
//                return hearingList;
//            }
//
//            String hearingDocumentQuery = "";
//            hearingDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
//            log.info("Final document query: {}", hearingDocumentQuery);
//            Map<UUID, List<Document>> hearingDocumentMap = jdbcTemplate.query(hearingDocumentQuery, preparedStmtListDoc.toArray(), hearingDocumentRowMapper);
//            if (hearingDocumentMap != null) {
//                hearingList.forEach(hearing -> {
//                    hearing.setDocuments(hearingDocumentMap.get(hearing.getId()));
//                });
//            }
//
//            return hearingList;
//        }
//        catch (CustomException e){
//            throw e;
//        }
//        catch (Exception e){
//            log.error("Error while fetching hearing application list");
//            throw new CustomException(ADVOCATE_SEARCH_EXCEPTION,"Error while fetching hearing application list: "+e.getMessage());
//        }
//    }

    public List<Hearing> getHearings(Hearing hearing) {
        return new ArrayList<>();
    }

    public List<Hearing> getHearings(String cnrNumber, String applicationNumber, String id, String fightingNumber, String tenentId, LocalDate fromDate, LocalDate toDate, Integer limit, Integer offset, String sortBy) {
        return new ArrayList<>();
    }
}
