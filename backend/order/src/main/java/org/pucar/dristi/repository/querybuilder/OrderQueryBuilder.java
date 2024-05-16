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
    private static final String BASE_ORDER_QUERY = " SELECT orders.id as id, orders.tenantid as tenantid, orders.casenumber as casenumber, orders.resolutionmechanism as resolutionmechanism, orders.casetitle as casetitle, orders.casedescription as casedescription, " +
            "orders.filingnumber as filingnumber, orders.cnrNumber as cnrNumber, " +
            " orders.courtid as courtid, orders.benchid as benchid, orders.filingdate as filingdate, orders.registrationdate as registrationdate, orders.natureofpleading as natureofpleading, orders.status as status, orders.remarks as remarks, orders.isactive as isactive, orders.casedetails as casedetails, orders.additionaldetails as additionaldetails, orders.casecategory as casecategory, orders.createdby as createdby," +
            " orders.lastmodifiedby as lastmodifiedby, orders.createdtime as createdtime, orders.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_ORDERS_TABLE = " FROM dristi_orders orders";
    private static final String ORDERBY_CREATEDTIME = " ORDER BY orders.createdtime DESC ";

    private static final String DOCUMENT_SELECT_QUERY_CASE = "SELECT doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore," +
            " doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.case_id as case_id, doc.linked_case_id as linked_case_id, doc.litigant_id as litigant_id, doc.representative_id as representative_id, doc.representing_id as representing_id ";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_order_document doc";

    private static final String BASE_STATUTE_SECTION_QUERY = " SELECT stse.id as id, stse.tenantid as tenantid, stse.statutes as statutes, stse.case_id as case_id, " +
            "stse.sections as sections," +
            " stse.subsections as subsections, stse.additionaldetails as additionaldetails, stse.createdby as createdby," +
            " stse.lastmodifiedby as lastmodifiedby, stse.createdtime as createdtime, stse.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_STATUTE_SECTION_TABLE = " FROM dristi_order_statute_section stse";

    public String getOrderSearchQuery(String applicationNumber, String cnrNumber, String filingNumber, String tenantId, String id, String status, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_ORDER_QUERY);
            query.append(FROM_ORDERS_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria

            List<String> applicationNumbers = List.of(applicationNumber);
            List<String> cnrNumbers = List.of(cnrNumber);
            List<String> filingNumbers = List.of(filingNumber);
            List<String> tenantIds = List.of(tenantId);
            List<String> ids = List.of(id);
            List<String> statusList = List.of(status);


            if (!ids.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.id IN (")
                        .append(ids.stream().map(i -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
                firstCriteria = false;
            }

            if (!cnrNumbers.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.cnrNumber IN (")
                        .append(cnrNumbers.stream().map(reg -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(cnrNumbers);
                firstCriteria = false;

            }

            if (!filingNumbers.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("orders.filingnumber IN (")
                        .append(filingNumbers.stream().map(num -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(filingNumbers);
                firstCriteria = false;
            }
//
//                for(CaseCriteria caseCriteria:criteriaList){
//                    if(caseCriteria.getFilingFromDate() != null && caseCriteria.getFilingToDate()!=null){
//                        if(!firstCriteria)
//                          query.append("OR orders.filingdate BETWEEN "+caseCriteria.getFilingFromDate()+" AND "+caseCriteria.getFilingToDate()+" ");
//                        else{
//                            query.append("WHERE orders.filingdate BETWEEN "+caseCriteria.getFilingFromDate()+" AND "+caseCriteria.getFilingToDate()+" ");
//                        }
//                        firstCriteria =false;
//                    }
//
//                    if(caseCriteria.getRegistrationFromDate() != null && caseCriteria.getRegistrationToDate()!=null){
//                        if(!firstCriteria)
//                            query.append("OR orders.registrationdate BETWEEN "+caseCriteria.getRegistrationFromDate()+" AND "+caseCriteria.getRegistrationToDate()+" ");
//                        else{
//                            query.append("WHERE orders.registrationdate BETWEEN "+caseCriteria.getRegistrationFromDate()+" AND "+caseCriteria.getRegistrationToDate()+" ");
//                        }
//                        firstCriteria =false;
//                    }
//                }
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