package org.pucar.dristi.repository.querybuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.Hearing;
import org.pucar.dristi.web.models.HearingCriteria;
import org.pucar.dristi.web.models.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class HearingQueryBuilder {
    private static final String BASE_ATR_QUERY = " SELECT * FROM dristi_hearing WHERE 1=1 ";

    private static final String DOCUMENT_SELECT_QUERY = "Select doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.hearingid as hearingid ";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_hearing_document doc";
    private  static  final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM ({baseQuery}) total_result";
    private static final String DEFAULT_ORDERBY_CLAUSE = " ORDER BY createdtime DESC ";
    private static final String ORDERBY_CLAUSE = " ORDER BY {orderBy} {sortingOrder} ";

    private final ObjectMapper mapper;

    @Autowired
    public HearingQueryBuilder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String getHearingSearchQuery(List<Object> preparedStmtList, HearingCriteria criteria) {
        try {
            String cnrNumber = criteria.getCnrNumber();
            String applicationNumber = criteria.getApplicationNumber();
            String hearingId = criteria.getHearingId();
            String hearingType = criteria.getHearingType();
            String filingNumber = criteria.getFilingNumber();
            String tenantId = criteria.getTenantId();
            LocalDate fromDate = criteria.getFromDate();
            LocalDate toDate = criteria.getToDate();
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);

            addCriteriaString(cnrNumber, query, " AND cnrNumbers @> ?::jsonb", preparedStmtList, "[\"" + cnrNumber + "\"]");
            addCriteriaString(applicationNumber, query, " AND applicationNumbers @> ?::jsonb", preparedStmtList, "[\"" + applicationNumber + "\"]");
            addCriteriaString(hearingId, query, " AND hearingid = ?", preparedStmtList, hearingId);
            addCriteriaString(hearingType, query, " AND hearingtype = ?", preparedStmtList, hearingType);
            addCriteriaString(filingNumber, query, " AND filingNumber @> ?::jsonb", preparedStmtList, "[\"" + filingNumber + "\"]");
            addCriteriaString(tenantId, query, " AND tenantId = ?", preparedStmtList, tenantId);
            addCriteriaDate(fromDate, query, " AND startTime >= ?", preparedStmtList);
            addCriteriaDate(toDate, query, " AND startTime <= ?", preparedStmtList);
            return query.toString();
        } catch (Exception e) {
            log.error("Error while building hearing search query");
            throw new CustomException(SEARCH_QUERY_EXCEPTION, "Error occurred while building the hearing search query: " + e.getMessage());
        }
    }

    void addCriteriaString(String criteria, StringBuilder query, String str, List<Object> preparedStmtList, Object listItem) {
        if (criteria != null && !criteria.isEmpty()) {
            query.append(str);
            preparedStmtList.add(listItem);
        }
    }

    void addCriteriaDate(LocalDate criteria, StringBuilder query, String str, List<Object> preparedStmtList) {
        if (criteria != null) {
            query.append(str);
            preparedStmtList.add(criteria.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000);
        }
    }
    public String addOrderByQuery(String query, Pagination pagination) {
        if (pagination == null || pagination.getSortBy() == null || pagination.getOrder() == null) {
            return query + DEFAULT_ORDERBY_CLAUSE;
        } else {
            query = query + ORDERBY_CLAUSE;
        }
        return query.replace("{orderBy}", pagination.getSortBy()).replace("{sortingOrder}", pagination.getOrder().name());
    }

    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.hearingid IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }


            return query.toString();
        } catch (Exception e) {
            log.error("Error while building document search query");
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION, "Error occurred while building the query: " + e.getMessage());
        }
    }

    public String buildUpdateTranscriptAdditionalAttendeesQuery(List<Object> preparedStmtList, Hearing hearing) throws CustomException {
        String query = "UPDATE dristi_hearing SET transcript = ?::jsonb , additionaldetails = ?::jsonb , attendees = ?::jsonb , vclink = ? , lastModifiedBy = ? , lastModifiedTime = ? WHERE hearingId = ? AND tenantId = ?";

        // Convert the objects to JSON
        try {
            String transcriptJson = mapper.writeValueAsString(hearing.getTranscript());
            String additionalDetailsJson = mapper.writeValueAsString(hearing.getAdditionalDetails());
            String attendeesJson = mapper.writeValueAsString(hearing.getAttendees());
            preparedStmtList.add(transcriptJson);
            preparedStmtList.add(additionalDetailsJson);
            preparedStmtList.add(attendeesJson);
        } catch (JsonProcessingException e) {
            throw new CustomException(PARSING_ERROR, "Error parsing data to JSON : " + e.getMessage());
        }

        // Add other parameters to preparedStmtList
        preparedStmtList.add(hearing.getVcLink());
        preparedStmtList.add(hearing.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(hearing.getAuditDetails().getLastModifiedTime());
        preparedStmtList.add(hearing.getHearingId());
        preparedStmtList.add(hearing.getTenantId());

        return query;
    }

    public String getTotalCountQuery(String baseQuery) {
        return TOTAL_COUNT_QUERY.replace("{baseQuery}", baseQuery);
    }

    public String addPaginationQuery(String query, Pagination pagination, List<Object> preparedStatementList) {
        preparedStatementList.add(pagination.getLimit());
        preparedStatementList.add(pagination.getOffSet());
        return query + " LIMIT ? OFFSET ?";
    }
}