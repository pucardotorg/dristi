package org.pucar.dristi.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.OrderCriteria;
import org.pucar.dristi.web.models.Pagination;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class OrderQueryBuilder {

    private static final String BASE_ORDER_QUERY = " SELECT orders.id as id, orders.tenantid as tenantid, orders.hearingnumber as hearingnumber, " +
            "orders.filingnumber as filingnumber, orders.comments as comments, orders.cnrnumber as cnrnumber, orders.linkedordernumber as linkedordernumber, orders.ordernumber as ordernumber, orders.applicationnumber as applicationnumber," +
            "orders.createddate as createddate, orders.ordertype as ordertype, orders.orderdetails as orderdetails, orders.issuedby as issuedby, orders.ordercategory as ordercategory," +
            "orders.status as status, orders.isactive as isactive, orders.additionaldetails as additionaldetails, orders.createdby as createdby," +
            "orders.lastmodifiedby as lastmodifiedby, orders.createdtime as createdtime, orders.lastmodifiedtime as lastmodifiedtime ";

    private static final String FROM_ORDERS_TABLE = " FROM dristi_orders orders";

    private static final String DOCUMENT_SELECT_QUERY_CASE = "SELECT doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore," +
            "doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.order_id as order_id";

    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_order_document doc";

    private static final String BASE_STATUTE_SECTION_QUERY = " SELECT stse.id as id, stse.tenantid as tenantid, stse.statute as statute, stse.order_id as order_id, " +
            "stse.sections as sections, stse.subsections as subsections, stse.strsections as strsections, stse.strsubsections as strsubsections, stse.additionaldetails as additionaldetails, stse.createdby as createdby," +
            " stse.lastmodifiedby as lastmodifiedby, stse.createdtime as createdtime, stse.lastmodifiedtime as lastmodifiedtime ";

    private static final String FROM_STATUTE_SECTION_TABLE = " FROM dristi_order_statute_section stse";

    private static final String BASE_ORDER_EXIST_QUERY = "SELECT COUNT(*) FROM dristi_orders orders";

    private static final String DEFAULT_ORDERBY_CLAUSE = " ORDER BY orders.createdtime DESC ";
    private static final String ORDERBY_CLAUSE = " ORDER BY orders.{orderBy} {sortingOrder} ";
    private  static  final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM ({baseQuery}) total_result";


    public String getTotalCountQuery(String baseQuery) {
        return TOTAL_COUNT_QUERY.replace("{baseQuery}", baseQuery);
    }

    public String checkOrderExistQuery(String orderNumber, String cnrNumber, String filingNumber, String applicationNumber , UUID orderId, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_ORDER_EXIST_QUERY);
            boolean firstCriteria = true; // To check if it's the first criteria

            if (cnrNumber!=null && !cnrNumber.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.cnrNumber = ?");
                preparedStmtList.add(cnrNumber);
                firstCriteria = false;
            }

            if (filingNumber!=null && !filingNumber.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.filingNumber = ?");
                preparedStmtList.add(filingNumber);
                firstCriteria = false;
            }

            if (orderNumber!=null && !orderNumber.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.orderNumber = ?");
                preparedStmtList.add(orderNumber);
                firstCriteria = false;
            }

            if (orderId!=null) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.id = ?");
                preparedStmtList.add(orderId.toString());
                firstCriteria = false;
            }

            if (applicationNumber!=null && !applicationNumber.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.applicationNumber::text LIKE ?");
                preparedStmtList.add("%" + applicationNumber + "%");
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building order exist query :: {}",e.toString());
            throw new CustomException(ORDER_EXISTS_EXCEPTION, "Error occurred while building the order exist query : " + e.getMessage());
        }
    }


    public String getOrderSearchQuery(OrderCriteria criteria, List<Object> preparedStmtList, List<Integer> preparedStmtArgList) {
        try {

            StringBuilder query = new StringBuilder(BASE_ORDER_QUERY);
            query.append(FROM_ORDERS_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria

            firstCriteria = addCriteria(criteria.getApplicationNumber() == null? null : "%" + criteria.getApplicationNumber() + "%", query, firstCriteria, "orders.applicationNumber::text LIKE ?", preparedStmtList, preparedStmtArgList, Types.VARCHAR);
            firstCriteria = addCriteria(criteria.getCnrNumber(), query, firstCriteria, "orders.cnrNumber = ?", preparedStmtList, preparedStmtArgList, Types.VARCHAR);
            firstCriteria = addCriteria(criteria.getFilingNumber(), query, firstCriteria, "orders.filingNumber = ?", preparedStmtList, preparedStmtArgList, Types.VARCHAR);
            firstCriteria = addCriteria(criteria.getTenantId(), query, firstCriteria, "orders.tenantId = ?", preparedStmtList, preparedStmtArgList, Types.VARCHAR);
            firstCriteria = addCriteria(criteria.getOrderType(), query, firstCriteria, "orders.orderType = ?", preparedStmtList, preparedStmtArgList, Types.VARCHAR);
            firstCriteria = addCriteria(criteria.getId(), query, firstCriteria, "orders.id = ?", preparedStmtList, preparedStmtArgList, Types.VARCHAR);
            firstCriteria = addCriteria(criteria.getStatus(), query, firstCriteria, "orders.status = ?", preparedStmtList, preparedStmtArgList, Types.VARCHAR);

             addCriteria(criteria.getOrderNumber() == null? null : "%" + criteria.getOrderNumber() + "%", query, firstCriteria, "LOWER(orders.orderNumber) LIKE LOWER(?)", preparedStmtList, preparedStmtArgList, Types.VARCHAR);
            return query.toString();
        } catch (Exception e) {
            log.error("Error while building order search query :: {}",e.toString());
            throw new CustomException(ORDER_SEARCH_EXCEPTION, "Error occurred while building the order search query: " + e.getMessage());
        }
    }

    private boolean addCriteria(String criteria, StringBuilder query, boolean firstCriteria, String str, List<Object> preparedStmtList, List<Integer> preparedStmtArgList, int type ) {
        if (criteria != null && !criteria.isEmpty()) {
            addClauseIfRequired(query, firstCriteria);
            query.append(str);
            preparedStmtList.add(criteria);
            preparedStmtArgList.add(type);
            firstCriteria = false;
        }
        return firstCriteria;
    }

    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList,List<Integer> preparedStmtDocList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY_CASE);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.order_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
                ids.forEach(i->preparedStmtDocList.add(Types.VARCHAR));
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building document search query :: {}",e.toString());
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION, "Error occurred while building the query: " + e.getMessage());
        }
    }


    public String getStatuteSectionSearchQuery(List<String> ids, List<Object> preparedStmtList,List<Integer> preparedStmtStsecList) {
        try {
            StringBuilder query = new StringBuilder(BASE_STATUTE_SECTION_QUERY);
            query.append(FROM_STATUTE_SECTION_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE stse.order_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
                ids.forEach(i->preparedStmtStsecList.add(Types.VARCHAR));
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building statute search query :: {}",e.toString());
            throw new CustomException(STATUTE_SEARCH_QUERY_EXCEPTION, "Error occurred while building the query: " + e.getMessage());
        }
    }

    public String addPaginationQuery(String query, Pagination pagination, List<Object> preparedStatementList, List<Integer> preparedStatementArgList) {
        preparedStatementList.add(pagination.getLimit());
        preparedStatementArgList.add(Types.DOUBLE);

        preparedStatementList.add(pagination.getOffSet());
        preparedStatementArgList.add(Types.DOUBLE);
        return query + " LIMIT ? OFFSET ?";
    }

    public String addOrderByQuery(String query, Pagination pagination) {
        if (isPaginationInvalid(pagination) || pagination.getSortBy().contains(";")) {
            return query + DEFAULT_ORDERBY_CLAUSE;
        } else {
            query = query + ORDERBY_CLAUSE;
        }
        return query.replace("{orderBy}", pagination.getSortBy()).replace("{sortingOrder}", pagination.getOrder().name());
    }

    private static boolean isPaginationInvalid(Pagination pagination) {
        return pagination == null || pagination.getSortBy() == null || pagination.getOrder() == null;
    }

    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }
}