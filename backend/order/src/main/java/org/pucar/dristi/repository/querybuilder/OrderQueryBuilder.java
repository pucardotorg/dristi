package org.pucar.dristi.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class OrderQueryBuilder {

    private static final String BASE_ORDER_QUERY = " SELECT orders.id as id, orders.tenantid as tenantid, orders.hearingnumber as hearingnumber, " +
            "orders.filingnumber as filingnumber, orders.comments as comments, orders.cnrnumber as cnrnumber, orders.linkedordernumber as linkedordernumber, orders.ordernumber as ordernumber, orders.applicationnumber as applicationnumber," +
            "orders.createddate as createddate, orders.ordertype as ordertype, orders.issuedby as issuedby, orders.ordercategory as ordercategory," +
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

    private static final String BASE_ORDER_EXIST_QUERY = "SELECT COUNT(*) FROM dristi_orders orders";

    public String checkOrderExistQuery(String orderNumber, String cnrNumber, String filingNumber, String applicationNumber , UUID orderId) {
        try {
            StringBuilder query = new StringBuilder(BASE_ORDER_EXIST_QUERY);
            boolean firstCriteria = true; // To check if it's the first criteria

            if (cnrNumber!=null && !cnrNumber.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.cnrNumber = ").append("'").append(cnrNumber).append("'");
                firstCriteria = false;
            }

            if (filingNumber!=null && !filingNumber.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.filingnumber =").append("'").append(filingNumber).append("'");
                firstCriteria = false;
            }

            if (orderNumber!=null && !orderNumber.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.ordernumber =").append("'").append(orderNumber).append("'");
                firstCriteria = false;
            }

            if (orderId!=null) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.id =").append("'").append(orderId).append("'");
                firstCriteria = false;
            }

            if (applicationNumber!=null && !applicationNumber.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.applicationNumber::text LIKE '%\"").append(applicationNumber).append("\"%'");
                firstCriteria = false;
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building order exist query :: {}",e.toString());
            throw new CustomException(ORDER_EXISTS_EXCEPTION, "Error occurred while building the order exist query : " + e.getMessage());
        }
    }


    public String getOrderSearchQuery(String orderNumber,String applicationNumber, String cnrNumber, String filingNumber, String tenantId, String id, String status) {
        try {
            StringBuilder query = new StringBuilder(BASE_ORDER_QUERY);
            query.append(FROM_ORDERS_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria

            if (applicationNumber!=null && !applicationNumber.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.applicationNumber::text LIKE '%\"").append(applicationNumber).append("\"%'");
                firstCriteria = false;
            }

            if (cnrNumber!=null && !cnrNumber.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.cnrNumber = ").append("'").append(cnrNumber).append("'");
                firstCriteria = false;
            }

            if (filingNumber!=null && !filingNumber.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.filingnumber =").append("'").append(filingNumber).append("'");
                firstCriteria = false;
            }

            if (tenantId!=null && !tenantId.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.tenantid =").append("'").append(tenantId).append("'");
                firstCriteria = false;
            }

            if (id!=null && !id.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.id = ").append("'").append(id).append("'");
                firstCriteria = false;

            }

            if (status!=null && !status.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.status =").append("'").append(status).append("'");
                firstCriteria = false;
            }
            if (orderNumber!=null && !orderNumber.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.ordernumber =").append("'").append(orderNumber).append("'");
                firstCriteria = false;
            }
            query.append(ORDERBY_CREATEDTIME);

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building order search query :: {}",e.toString());
            throw new CustomException(ORDER_SEARCH_EXCEPTION, "Error occurred while building the order search query: " + e.getMessage());
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
            log.error("Error while building document search query :: {}",e.toString());
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION, "Error occurred while building the query: " + e.getMessage());
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
            log.error("Error while building statute search query :: {}",e.toString());
            throw new CustomException(STATUTE_SEARCH_QUERY_EXCEPTION, "Error occurred while building the query: " + e.getMessage());
        }
    }

    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }
}