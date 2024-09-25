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

import java.sql.Types;
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

    public String getHearingSearchQuery(List<Object> preparedStmtList, HearingCriteria criteria, List<Integer> preparedStmtArgList) {
        try {
            String cnrNumber = criteria.getCnrNumber();
            String applicationNumber = criteria.getApplicationNumber();
            String hearingId = criteria.getHearingId();
            String hearingType = criteria.getHearingType();
            String filingNumber = criteria.getFilingNumber();
            String tenantId = criteria.getTenantId();
            Long fromDate = criteria.getFromDate();
            Long toDate = criteria.getToDate();
            String attendeeIndividualId = criteria.getAttendeeIndividualId();
            String courtId = criteria.getCourtId();
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);

            addCriteriaString(cnrNumber, query, " AND cnrNumbers @> ?::jsonb", preparedStmtList, preparedStmtArgList,"[\"" + cnrNumber + "\"]");
            addCriteriaString(applicationNumber, query, " AND applicationNumbers @> ?::jsonb", preparedStmtList, preparedStmtArgList,"[\"" + applicationNumber + "\"]");
            addCriteriaString(hearingId, query, " AND hearingid = ?", preparedStmtList,preparedStmtArgList, hearingId);
            addCriteriaString(hearingType, query, " AND hearingtype = ?", preparedStmtList,preparedStmtArgList, hearingType);
            addCriteriaString(filingNumber, query, " AND filingNumber @> ?::jsonb", preparedStmtList, preparedStmtArgList,"[\"" + filingNumber + "\"]");
            addCriteriaString(tenantId, query, " AND tenantId = ?", preparedStmtList,preparedStmtArgList, tenantId);
            addCriteriaDate(fromDate, query, " AND startTime >= ?", preparedStmtList,preparedStmtArgList);
            addCriteriaDate(toDate, query, " AND startTime <= ?", preparedStmtList,preparedStmtArgList);
            addCriteriaString(courtId, query, " AND presidedby ->> 'courtID' = ? ", preparedStmtList, preparedStmtArgList, courtId);
            addCriteriaString(attendeeIndividualId, query," AND EXISTS (SELECT 1 FROM jsonb_array_elements(attendees) elem WHERE elem->>'individualId' = ?)", preparedStmtList,preparedStmtArgList, attendeeIndividualId);

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building hearing search query");
            throw new CustomException(SEARCH_QUERY_EXCEPTION, "Error occurred while building the hearing search query: " + e.getMessage());
        }
    }

    void addCriteriaString(String criteria, StringBuilder query, String str, List<Object> preparedStmtList, List<Integer> preparedStmtArgList, Object listItem) {
        if (criteria != null && !criteria.isEmpty()) {
            query.append(str);
            preparedStmtList.add(listItem);
            preparedStmtArgList.add(Types.VARCHAR);
        }
    }

    void addCriteriaDate(Long criteria, StringBuilder query, String str, List<Object> preparedStmtList, List<Integer> preparedStmtArgList) {
        if (criteria != null) {
            query.append(str);
            preparedStmtList.add(criteria);
            preparedStmtArgList.add(Types.BIGINT);
        }
    }
    public String addOrderByQuery(String query, Pagination pagination) {
        if (isPaginationInvalid(pagination) || pagination.getSortBy().contains(";"))  {
            return query + DEFAULT_ORDERBY_CLAUSE;
        } else {
            query = query + ORDERBY_CLAUSE;
        }
        return query.replace("{orderBy}", pagination.getSortBy()).replace("{sortingOrder}", pagination.getOrder().name());
    }

    private static boolean isPaginationInvalid(Pagination pagination) {
        return pagination == null || pagination.getSortBy() == null || pagination.getOrder() == null;
    }

    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList, List<Integer> preparedStmtArgListDoc) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.hearingid IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
                ids.forEach(i->preparedStmtArgListDoc.add(Types.VARCHAR));
            }


            return query.toString();
        } catch (Exception e) {
            log.error("Error while building document search query");
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION, "Error occurred while building the query: " + e.getMessage());
        }
    }

    public String buildUpdateTranscriptAdditionalAttendeesQuery(List<Object> preparedStmtList, Hearing hearing) throws CustomException {
        String query = "UPDATE dristi_hearing SET transcript = ?::jsonb , additionaldetails = ?::jsonb , attendees = ?::jsonb , vclink = ? , notes = ? , lastModifiedBy = ? , lastModifiedTime = ? WHERE hearingId = ? AND tenantId = ?";

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
        preparedStmtList.add(hearing.getNotes());
        preparedStmtList.add(hearing.getAuditDetails().getLastModifiedBy());
        preparedStmtList.add(hearing.getAuditDetails().getLastModifiedTime());
        preparedStmtList.add(hearing.getHearingId());
        preparedStmtList.add(hearing.getTenantId());

        return query;
    }

    public String getTotalCountQuery(String baseQuery) {
        return TOTAL_COUNT_QUERY.replace("{baseQuery}", baseQuery);
    }

    public String addPaginationQuery(String query, Pagination pagination, List<Object> preparedStatementList, List<Integer> preparedStatementArgList) {
        preparedStatementList.add(pagination.getLimit());
        preparedStatementArgList.add(Types.DOUBLE);
        preparedStatementList.add(pagination.getOffSet());
        preparedStatementArgList.add(Types.DOUBLE);
        return query + " LIMIT ? OFFSET ?";
    }
}