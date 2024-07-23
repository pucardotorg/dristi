package org.pucar.dristi.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.DOCUMENT_SEARCH_QUERY_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.SEARCH_QUERY_EXCEPTION;

@Component
@Slf4j
public class HearingQueryBuilder {
    private static final String BASE_ATR_QUERY = " SELECT * FROM dristi_hearing WHERE 1=1 ";

    private static final String DOCUMENT_SELECT_QUERY = "Select doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.hearingid as hearingid ";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_hearing_document doc";

    /**   /** To build query using search criteria to search hearing
     * @param cnrNumber
     * @param preparedStmtList
     * @param hearingId
     * @param applicationNumber
     * @param filingNumber
     * @param limit
     * @param offset
     * @return
     */
    public String getHearingSearchQuery(List<Object> preparedStmtList, String cnrNumber, String applicationNumber, String hearingId, String filingNumber, String tenantId, LocalDate fromDate, LocalDate toDate, Integer limit, Integer offset, String sortBy) {
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            if (cnrNumber != null && !cnrNumber.isEmpty()) {
                query.append(" AND cnrNumbers @> ?::jsonb");
                preparedStmtList.add("[\"" + cnrNumber + "\"]");
            }

            if (applicationNumber != null && !applicationNumber.isEmpty()) {
                query.append(" AND applicationNumbers @> ?::jsonb");
                preparedStmtList.add("[\"" + applicationNumber + "\"]");
            }

            if (hearingId != null && !hearingId.isEmpty()) {
                query.append(" AND hearingid = ?");
                preparedStmtList.add(hearingId);
            }

            if (filingNumber != null && !filingNumber.isEmpty()) {
                query.append(" AND filingNumber @> ?::jsonb");
                preparedStmtList.add("[\"" + filingNumber + "\"]");
            }

            if ( tenantId!= null && !tenantId.isEmpty()) {
                query.append(" AND tenantId = ?");
                preparedStmtList.add(tenantId);
            }

            if (fromDate != null) {
                query.append(" AND createdTime >= ?");
                preparedStmtList.add(fromDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000);
            }

            if (toDate != null) {
                query.append(" AND createdTime <= ?");
                preparedStmtList.add(toDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000);
            }

            if (sortBy != null && !sortBy.isEmpty()) {
                switch (sortBy) {
                    case "startTime" -> query.append(" ORDER BY startTime DESC");
                    case "endTime" -> query.append(" ORDER BY endTime DESC");
                    default -> query.append(" ORDER BY id");
                }
            } else {
                query.append(" ORDER BY id");
            }

            if (limit != null) {
                query.append(" LIMIT ?");
                preparedStmtList.add(limit);
            }

            if (offset != null) {
                query.append(" OFFSET ?");
                preparedStmtList.add(offset);
            }

            return query.toString();
        }
         catch (Exception e) {
            log.error("Error while building hearing search query");
            throw new CustomException(SEARCH_QUERY_EXCEPTION,"Error occurred while building the hearing search query: "+ e.getMessage());
        }
    }

    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE (");
        } else {
            query.append(" OR ");
        }
    }

    private void addClauseIfRequiredForStatus(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
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
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,"Error occurred while building the query: "+ e.getMessage());
        }
    }
}