package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;

import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.querybuilder.HearingQueryBuilder;
import org.pucar.dristi.repository.rowmapper.HearingDocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.HearingRowMapper;
import org.pucar.dristi.web.models.Hearing;
import org.pucar.dristi.web.models.HearingCriteria;
import org.pucar.dristi.web.models.HearingSearchRequest;
import org.pucar.dristi.web.models.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.HEARING_SEARCH_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.HEARING_UPDATE_EXCEPTION;


@Slf4j
@Repository
public class HearingRepository {

    private final HearingQueryBuilder queryBuilder;

    private final JdbcTemplate jdbcTemplate;

    private final HearingRowMapper rowMapper;

    private final HearingDocumentRowMapper hearingDocumentRowMapper;

    @Autowired
    public HearingRepository(HearingQueryBuilder queryBuilder, JdbcTemplate jdbcTemplate, HearingRowMapper rowMapper, HearingDocumentRowMapper hearingDocumentRowMapper) {
        this.queryBuilder = queryBuilder;
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
        this.hearingDocumentRowMapper = hearingDocumentRowMapper;
    }


    public List<Hearing> getHearings(HearingSearchRequest hearingSearchRequest) {

        try {
            List<Hearing> hearingList = new ArrayList<>();
            List<Object> preparedStmtList = new ArrayList<>();
            List<Object> preparedStmtListDoc = new ArrayList<>();
            String hearingQuery;
            hearingQuery = queryBuilder.getHearingSearchQuery(preparedStmtList, hearingSearchRequest.getCriteria());
            hearingQuery = queryBuilder.addOrderByQuery(hearingQuery, hearingSearchRequest.getPagination());
            log.info("Hearing list query: {}", hearingQuery);

            if(hearingSearchRequest.getPagination() !=  null) {
                Integer totalRecords = getTotalCountHearing(hearingQuery, preparedStmtList);
                log.info("Total count without pagination :: {}", totalRecords);
                hearingSearchRequest.getPagination().setTotalCount(Double.valueOf(totalRecords));
                hearingQuery = queryBuilder.addPaginationQuery(hearingQuery, hearingSearchRequest.getPagination(), preparedStmtList);
                log.info("Post Pagination Query :: {}", hearingQuery);
            }
            List<Hearing> list = jdbcTemplate.query(hearingQuery, preparedStmtList.toArray(), rowMapper);
            if (list != null) {
                hearingList.addAll(list);
            }

            List<String> ids = new ArrayList<>();
            for (Hearing hearing : hearingList) {
                ids.add(hearing.getId().toString());
            }
            if (ids.isEmpty()) {
                return hearingList;
            }

            String hearingDocumentQuery;
            hearingDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
            log.info("Final document query: {}", hearingDocumentQuery);
            Map<UUID, List<Document>> hearingDocumentMap = jdbcTemplate.query(hearingDocumentQuery, preparedStmtListDoc.toArray(), hearingDocumentRowMapper);
            if (hearingDocumentMap != null) {
                hearingList.forEach(hearing -> hearing.setDocuments(hearingDocumentMap.get(hearing.getId())));
            }

            return hearingList;
        }
        catch (CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching hearing application list");
            throw new CustomException(HEARING_SEARCH_EXCEPTION,"Error while fetching hearing application list: "+e.getMessage());
        }
    }

    public Integer getTotalCountHearing(String baseQuery, List<Object> preparedStmtList) {
        String countQuery = queryBuilder.getTotalCountQuery(baseQuery);
        log.info("Final count query :: {}", countQuery);
        return jdbcTemplate.queryForObject(countQuery, preparedStmtList.toArray(), Integer.class);
    }

    public List<Hearing> checkHearingsExist(Hearing hearing) {
        HearingCriteria criteria = HearingCriteria.builder().hearingId(hearing.getHearingId()).tenantId(hearing.getTenantId()).build();
        Pagination pagination = Pagination.builder().limit(1.0).offSet((double) 0).build();
        HearingSearchRequest hearingSearchRequest = HearingSearchRequest.builder().criteria(criteria).pagination(pagination).build();
        return getHearings(hearingSearchRequest);
    }

    public void updateTranscriptAdditionalAttendees(Hearing hearing) {
        List<Object> preparedStmtList = new ArrayList<>();
        String hearingUpdateQuery = queryBuilder.buildUpdateTranscriptAdditionalAttendeesQuery(preparedStmtList, hearing);
        log.info("Final update query: {}", hearingUpdateQuery);
        int check = jdbcTemplate.update(hearingUpdateQuery, preparedStmtList.toArray());
        if(check==0) throw new CustomException(HEARING_UPDATE_EXCEPTION,"Error while updating hearing");
    }
}
