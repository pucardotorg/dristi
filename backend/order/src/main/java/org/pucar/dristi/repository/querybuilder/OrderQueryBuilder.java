package org.pucar.dristi.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrderQueryBuilder {

    private static final String BASE_ORDER_QUERY = " SELECT orders.id as id, orders.tenantid as tenantid, orders.hearingnumber as hearingnumber, " +
            "orders.filingnumber as filingnumber, orders.cnrnumber as cnrnumber, orders.ordernumber as ordernumber, orders.applicationnumber as applicationnumber," +
            "orders.status as status, orders.isactive as isactive, orders.additionaldetails as additionaldetails, orders.createdby as createdby," +
            "orders.lastmodifiedby as lastmodifiedby, orders.createdtime as createdtime, orders.lastmodifiedtime as lastmodifiedtime ";

    private static final String FROM_ORDERS_TABLE = " FROM dristi_orders orders";

    private static final String ORDERBY_CREATEDTIME = " ORDER BY orders.createdtime DESC ";

    private static final String DOCUMENT_SELECT_QUERY_CASE = "SELECT doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore," +
            "doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.order_id as order_id";

    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_order_document doc";

    private static final String BASE_STATUTE_SECTION_QUERY = " SELECT stse.id as id, stse.tenantid as tenantid, stse.statute as statute, stse.order_id as order_id, " +
            "stse.sections as sections, stse.subsections as subsections, stse.strsections as strsections, stse.strsubsections as strsubsections, stse.additionaldetails as additionaldetails, stse.createdby as createdby," +
            " stse.lastmodifiedby as lastmodifiedby, stse.createdtime as createdtime, stse.lastmodifiedtime as lastmodifiedtime ";

    private static final String FROM_STATUTE_SECTION_TABLE = " FROM dristi_order_statute_section stse";

    private static final String BASE_ORDER_EXIST_QUERY = "SELECT COUNT(*) FROM dristi_orders orders WHERE ";

    public String checkOrderExistQuery(String orderNumber, String cnrNumber, String filingNumber) {
        try {
            StringBuilder query = new StringBuilder(BASE_ORDER_EXIST_QUERY);

            if (orderNumber != null && cnrNumber != null && filingNumber == null) {
                // orderNumber and cnrNumber are not null, filingNumber is null
                query.append("orders.ordernumber = ").append("'").append(orderNumber).append("'").append(" AND ");
                query.append("orders.cnrnumber = ").append("'").append(cnrNumber).append("'").append(";");

            } else if (orderNumber != null && cnrNumber == null && filingNumber != null) {
                // orderNumber and filingNumber are not null, cnrNumber is null
                query.append("orders.ordernumber = ").append("'").append(orderNumber).append("'").append(" AND ");
                query.append("orders.filingnumber = ").append("'").append(filingNumber).append("'").append(";");

            } else if (orderNumber == null && cnrNumber != null && filingNumber != null) {
                // cnrNumber and filingNumber are not null, orderNumber is null
                query.append("orders.cnrnumber = ").append("'").append(cnrNumber).append("'").append(" AND ");
                query.append("orders.filingnumber = ").append("'").append(filingNumber).append("'").append(";");

            } else if (orderNumber != null && cnrNumber == null) {
                // Only orderNumber is not null, cnrNumber and filingNumber are null
                query.append("orders.ordernumber = ").append("'").append(orderNumber).append("'").append(";");

            } else if (orderNumber == null && cnrNumber != null) {
                // Only cnrNumber is not null, orderNumber and filingNumber are null
                query.append("orders.cnrnumber = ").append("'").append(cnrNumber).append("'").append(";");

            } else if (orderNumber == null && filingNumber != null) {
                // Only filingNumber is not null, orderNumber and cnrNumber are null
                query.append("orders.filingnumber = ").append("'").append(filingNumber).append("'").append(";");

            } else if (orderNumber != null) {
                // All fields are not null
                query.append("orders.ordernumber = ").append("'").append(orderNumber).append("'").append(" AND ");
                query.append("orders.cnrnumber = ").append("'").append(cnrNumber).append("'").append(" AND ");
                query.append("orders.filingnumber = ").append("'").append(filingNumber).append("'").append(";");
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building order exist query");
            throw new CustomException("ORDER_EXISTS_ERROR", "Error occurred while building the order exist query : " + e.getMessage());
        }
    }


    public String getOrderSearchQuery(String cnrNumber, String filingNumber, String tenantId, String id, String status) {
        try {
            StringBuilder query = new StringBuilder(BASE_ORDER_QUERY);
            query.append(FROM_ORDERS_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria

            if (cnrNumber!=null) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.cnrNumber = ").append("'").append(cnrNumber).append("'");
                firstCriteria = false;

            }

            if (filingNumber!=null) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.filingnumber =").append("'").append(filingNumber).append("'");
                firstCriteria = false;
            }

            if (tenantId!=null) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.tenantid =").append("'").append(tenantId).append("'");
                firstCriteria = false;
            }

            if (id!=null) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.id = ").append("'").append(id).append("'");
                firstCriteria = false;

            }

            if (status!=null) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.status =").append("'").append(status).append("'");
                firstCriteria = false;
            }
            query.append(ORDERBY_CREATEDTIME);

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building order search query");
            throw new CustomException("ORDER_SEARCH_QUERY_EXCEPTION", "Error occurred while building the order search query: " + e.getMessage());
        }
    }

    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" OR ");
        }
    }

    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY_CASE);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.order_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building document search query");
            throw new CustomException("DOCUMENT_SEARCH_QUERY_EXCEPTION", "Error occurred while building the query: " + e.getMessage());
        }
    }


    public String getStatuteSectionSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_STATUTE_SECTION_QUERY);
            query.append(FROM_STATUTE_SECTION_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE stse.order_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building document search query");
            throw new CustomException("DOCUMENT_SEARCH_QUERY_EXCEPTION", "Error occurred while building the query: " + e.getMessage());
        }
    }
}