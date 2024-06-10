//package org.pucar.dristi.repository.querybuilder;
//
//import lombok.extern.slf4j.Slf4j;
//import org.egov.tracer.model.CustomException;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//@Slf4j
//public class TaskQueryBuilder {
//    private static final String BASE_CASE_QUERY = " SELECT cases.id as id, cases.tenantid as tenantid, cases.casenumber as casenumber, cases.resolutionmechanism as resolutionmechanism, cases.casetitle as casetitle, cases.casedescription as casedescription, " +
//            "cases.filingnumber as filingnumber, cases.casenumber as casenumber, cases.accesscode as accesscode, cases.courtcasenumber as courtcasenumber, cases.cnrNumber as cnrNumber, " +
//            " cases.courtid as courtid, cases.benchid as benchid, cases.filingdate as filingdate, cases.registrationdate as registrationdate, cases.natureofpleading as natureofpleading, cases.status as status, cases.remarks as remarks, cases.isactive as isactive, cases.casedetails as casedetails, cases.additionaldetails as additionaldetails, cases.casecategory as casecategory, cases.createdby as createdby," +
//            " cases.lastmodifiedby as lastmodifiedby, cases.createdtime as createdtime, cases.lastmodifiedtime as lastmodifiedtime ";
//    private static final String FROM_CASES_TABLE = " FROM dristi_cases cases";
//    private static final String ORDERBY_CREATEDTIME = " ORDER BY cases.createdtime DESC ";
//
//    private static final String DOCUMENT_SELECT_QUERY_CASE = "Select doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore," +
//            " doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.case_id as case_id, doc.linked_case_id as linked_case_id, doc.litigant_id as litigant_id, doc.representative_id as representative_id, doc.representing_id as representing_id ";
//    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_case_document doc";
//
//    private static final String BASE_CASE_EXIST_QUERY = "SELECT COUNT(*) FROM dristi_cases cases WHERE ";
//
//    public String checkCaseExistQuery(String courtCaseNumber, String cnrNumber, String filingNumber) {
//        try {
//            StringBuilder query = new StringBuilder(BASE_CASE_EXIST_QUERY);
//
//            if (courtCaseNumber != null && cnrNumber != null && filingNumber == null) {
//                // courtCaseNumber and cnrNumber are not null, filingNumber is null
//                query.append("cases.courtcasenumber = ").append("'").append(courtCaseNumber).append("'").append(" AND ");
//                query.append("cases.cnrnumber = ").append("'").append(cnrNumber).append("'").append(";");
//
//            } else if (courtCaseNumber != null && cnrNumber == null && filingNumber != null) {
//                // courtCaseNumber and filingNumber are not null, cnrNumber is null
//                query.append("cases.courtcasenumber = ").append("'").append(courtCaseNumber).append("'").append(" AND ");
//                query.append("cases.filingnumber = ").append("'").append(filingNumber).append("'").append(";");
//
//            } else if (courtCaseNumber == null && cnrNumber != null && filingNumber != null) {
//                // cnrNumber and filingNumber are not null, courtCaseNumber is null
//                query.append("cases.cnrnumber = ").append("'").append(cnrNumber).append("'").append(" AND ");
//                query.append("cases.filingnumber = ").append("'").append(filingNumber).append("'").append(";");
//
//            } else if (courtCaseNumber != null && cnrNumber == null) {
//                // Only courtCaseNumber is not null, cnrNumber and filingNumber are null
//                query.append("cases.courtcasenumber = ").append("'").append(courtCaseNumber).append("'").append(";");
//
//            } else if (courtCaseNumber == null && cnrNumber != null) {
//                // Only cnrNumber is not null, courtCaseNumber and filingNumber are null
//                query.append("cases.cnrnumber = ").append("'").append(cnrNumber).append("'").append(";");
//
//            } else if (courtCaseNumber == null && filingNumber != null) {
//                // Only filingNumber is not null, courtCaseNumber and cnrNumber are null
//                query.append("cases.filingnumber = ").append("'").append(filingNumber).append("'").append(";");
//
//            } else if (courtCaseNumber != null) {
//                // All fields are not null
//                query.append("cases.courtcasenumber = ").append("'").append(courtCaseNumber).append("'").append(" AND ");
//                query.append("cases.cnrnumber = ").append("'").append(cnrNumber).append("'").append(" AND ");
//                query.append("cases.filingnumber = ").append("'").append(filingNumber).append("'").append(";");
//            }
//
//            return query.toString();
//        } catch (Exception e) {
//            log.error("Error while building case exist query");
//            throw new CustomException(CASE_SEARCH_QUERY_EXCEPTION, "Error occurred while building the case exist query : " + e.getMessage());
//        }
//    }
//
//    public String getCasesSearchQuery(CaseCriteria criteria, List<Object> preparedStmtList) {
//        try {
//            StringBuilder query = new StringBuilder(BASE_CASE_QUERY);
//            query.append(FROM_CASES_TABLE);
//            boolean firstCriteria = true; // To check if it's the first criteria
//            if (criteria != null) {
//
//                if (criteria.getCaseId() != null && !criteria.getCaseId().isEmpty()) {
//                    addClauseIfRequired(query, firstCriteria);
//                    query.append("cases.id = ?");
//                    preparedStmtList.add(criteria.getCaseId());
//                    firstCriteria = false;
//                }
//
//                if (criteria.getCnrNumber() != null && !criteria.getCnrNumber().isEmpty()) {
//                    addClauseIfRequired(query, firstCriteria);
//                    query.append("cases.cnrNumber = ?");
//                    preparedStmtList.add(criteria.getCnrNumber());
//                    firstCriteria = false;
//                }
//
//                if (criteria.getFilingNumber() != null && !criteria.getFilingNumber().isEmpty()) {
//                    addClauseIfRequired(query, firstCriteria);
//                    query.append("cases.filingnumber = ?");
//                    preparedStmtList.add(criteria.getFilingNumber());
//                    firstCriteria = false;
//                }
//
//                if (criteria.getCourtCaseNumber() != null && !criteria.getCourtCaseNumber().isEmpty()) {
//                    addClauseIfRequired(query, firstCriteria);
//                    query.append("cases.courtcasenumber = ?");
//                    preparedStmtList.add(criteria.getCourtCaseNumber());
//                    firstCriteria = false;
//                }
//
//                if (criteria.getFilingFromDate() != null && criteria.getFilingToDate() != null) {
//                    if (!firstCriteria)
//                        query.append("OR cases.filingdate BETWEEN ").append(criteria.getFilingFromDate()).append(" AND ").append(criteria.getFilingToDate()).append(" ");
//                    else {
//                        query.append("WHERE cases.filingdate BETWEEN ").append(criteria.getFilingFromDate()).append(" AND ").append(criteria.getFilingToDate()).append(" ");
//                    }
//                    firstCriteria = false;
//                }
//
//                if (criteria.getRegistrationFromDate() != null && criteria.getRegistrationToDate() != null) {
//                    if (!firstCriteria)
//                        query.append("OR cases.registrationdate BETWEEN ").append(criteria.getRegistrationFromDate()).append(" AND ").append(criteria.getRegistrationToDate()).append(" ");
//                    else {
//                        query.append("WHERE cases.registrationdate BETWEEN ").append(criteria.getRegistrationFromDate()).append(" AND ").append(criteria.getRegistrationToDate()).append(" ");
//                    }
//                    firstCriteria = false;
//                }
//            }
//            query.append(ORDERBY_CREATEDTIME);
//
//            return query.toString();
//        } catch (Exception e) {
//            log.error("Error while building case search query");
//            throw new CustomException(CASE_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the case search query: " + e.getMessage());
//        }
//    }
//
//    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
//        if (isFirstCriteria) {
//            query.append(" WHERE ");
//        } else {
//            query.append(" AND ");
//        }
//    }
//
//    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
//        try {
//            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY_CASE);
//            query.append(FROM_DOCUMENTS_TABLE);
//            if (!ids.isEmpty()) {
//                query.append(" WHERE doc.case_id IN (")
//                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
//                        .append(")");
//                preparedStmtList.addAll(ids);
//            }
//
//            return query.toString();
//        }  catch(CustomException e){
//            throw e;
//        } catch (Exception e) {
//            log.error("Error while building document search query");
//            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the query: " + e.getMessage());
//        }
//    }
//
//    public String getLinkedCaseSearchQuery(List<String> ids, List<Object> preparedStmtList) {
//        try {
//            StringBuilder query = new StringBuilder(BASE_LINKED_CASE_QUERY);
//            query.append(FROM_LINKED_CASE_TABLE);
//            if (!ids.isEmpty()) {
//                query.append(" WHERE lics.case_id IN (")
//                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
//                        .append(")");
//                preparedStmtList.addAll(ids);
//            }
//
//            return query.toString();
//        } catch (Exception e) {
//            log.error("Error while building linked case search query");
//            throw new CustomException(LINKED_CASE_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the query: " + e.getMessage());
//        }
//    }
//
//    public String getLitigantSearchQuery(List<String> ids, List<Object> preparedStmtList) {
//        try {
//            StringBuilder query = new StringBuilder(BASE_LITIGANT_QUERY);
//            query.append(FROM_LITIGANT_TABLE);
//            if (!ids.isEmpty()) {
//                query.append(" WHERE ltg.case_id IN (")
//                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
//                        .append(")");
//                preparedStmtList.addAll(ids);
//            }
//
//            return query.toString();
//        } catch (Exception e) {
//            log.error("Error while building litigant search query");
//            throw new CustomException(LITIGANT_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the query: " + e.getMessage());
//        }
//    }
//}