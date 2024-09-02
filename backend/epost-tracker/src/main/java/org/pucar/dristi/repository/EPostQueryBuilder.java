package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.pucar.dristi.model.EPostTrackerSearchCriteria;
import org.pucar.dristi.model.Pagination;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
@Slf4j
public class EPostQueryBuilder {

    private static final String BASE_APPLICATION_QUERY = "SELECT process_number, tenant_id, file_store_id, task_number, tracking_number, pincode, address, delivery_status, remarks, additional_details, row_version, booking_date, received_date, createdBy, lastModifiedBy, createdTime, lastModifiedTime ";

    private static final String FROM_TABLES = " FROM dristi_epost_tracker ";

    private static final String ORDER_BY_CLAUSE = " ORDER BY {sortBy} ";

    private static final String DEFAULT_ORDER_BY_CLAUSE = " ORDER BY createdtime ";

    private static final String DEFAULT_SORTING_ORDER = "DESC";

    private static final String LIMIT_OFFSET = " LIMIT ? OFFSET ?";

    private  static  final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM ({baseQuery}) total_result";

    public String getEPostTrackerSearchQuery(EPostTrackerSearchCriteria searchCriteria, List<Object> preparedStmtList) {
        StringBuilder query = new StringBuilder(BASE_APPLICATION_QUERY);
        query.append(FROM_TABLES);
        if(!ObjectUtils.isEmpty(searchCriteria.getDeliveryStatus())){
            addClauseIfRequired(query,preparedStmtList);
            query.append(" delivery_status = ? ");
            preparedStmtList.add(searchCriteria.getDeliveryStatus());
        }
        if(!CollectionUtils.isEmpty(searchCriteria.getDeliveryStatusList())){
            addClauseIfRequired(query,preparedStmtList);
            query.append(" delivery_status in ( ").append(createQuery(searchCriteria.getDeliveryStatusList())).append(" ) ");
            addToPreparedStatement(preparedStmtList, searchCriteria.getDeliveryStatusList());
        }
        if(!ObjectUtils.isEmpty(searchCriteria.getProcessNumber())){
            addClauseIfRequired(query,preparedStmtList);
            query.append(" process_number = ? ");
            preparedStmtList.add(searchCriteria.getProcessNumber());
        }
        if(!ObjectUtils.isEmpty(searchCriteria.getTrackingNumber())){
            addClauseIfRequired(query,preparedStmtList);
            query.append(" tracking_number = ? ");
            preparedStmtList.add(searchCriteria.getTrackingNumber());
        }
        if(!ObjectUtils.isEmpty(searchCriteria.getBookingDate())){
            addClauseIfRequired(query,preparedStmtList);
            query.append(" booking_date = ? ");
            preparedStmtList.add(searchCriteria.getTrackingNumber());
        }
        if(!ObjectUtils.isEmpty(searchCriteria.getReceivedDate())){
            addClauseIfRequired(query,preparedStmtList);
            query.append(" received_date = ? ");
            preparedStmtList.add(searchCriteria.getTrackingNumber());
        }
        return query.toString();
    }

    public String getTotalCountQuery(String baseQuery) {
        return TOTAL_COUNT_QUERY.replace("{baseQuery}", baseQuery);
    }

    public String addPaginationQuery(String query, List<Object> preparedStmtList, Pagination pagination,int limit,int offset) {
        if (pagination != null && !ObjectUtils.isEmpty(pagination.getSortBy())) {
            query += ORDER_BY_CLAUSE.replace("{sortBy}", pagination.getSortBy().name());
        } else {
            query += DEFAULT_ORDER_BY_CLAUSE;
        }

        if (pagination != null && !ObjectUtils.isEmpty(pagination.getOrderBy())) {
            query += pagination.getOrderBy().name();
        } else {
            query += DEFAULT_SORTING_ORDER;
        }

        preparedStmtList.add(limit);
        preparedStmtList.add(offset);
        return query + LIMIT_OFFSET;
    }

    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
        if (preparedStmtList.isEmpty()) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }

    private String createQuery(List<String> statusList) {
        StringBuilder builder = new StringBuilder();
        int length = statusList.size();
        for (int i = 0; i < length; i++) {
            builder.append(" ?");
            if (i != length - 1)
                builder.append(",");
        }
        return builder.toString();
    }

    private void addToPreparedStatement(List<Object> preparedStmtList, List<String> ids) {
        preparedStmtList.addAll(ids);
    }
}
