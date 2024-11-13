package com.egov.icops_integrationkerala.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class IcopsQueryBuilder {

    private static final String BASE_APPLICATION_QUERY = "SELECT process_number, tenant_id, task_number, task_type, file_store_id, task_details, delivery_status, remarks, additional_details, booking_date, received_date, row_version, acknowledgement_id ";

    private static final String FROM_TABLES = " FROM dristi_kerala_icops ";

    public String getIcopsTracker(String processUniqueId, List<Object> preparedStmtList) {
        StringBuilder query = new StringBuilder(BASE_APPLICATION_QUERY);
        query.append(FROM_TABLES);
        if(processUniqueId != null) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" process_number = ? ");
            preparedStmtList.add(processUniqueId);
        }
        return query.toString();
    }

    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
        if (preparedStmtList.isEmpty()) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }
}
