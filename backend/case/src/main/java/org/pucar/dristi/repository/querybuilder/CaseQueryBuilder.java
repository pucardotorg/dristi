package org.pucar.dristi.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.CaseCriteria;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CaseQueryBuilder {
    private static final String BASE_CASE_QUERY = " SELECT cases.id as id, cases.tenantid as tenantid, cases.casenumber as casenumber, " +
            "cases.filingnumber as filingnumber," +
            " cases.courtid as courtid, cases.benchid as benchid, cases.additionaldetails as additionaldetails, cases.createdby as createdby," +
            " cases.lastmodifiedby as lastmodifiedby, cases.createdtime as createdtime, cases.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_CASES_TABLE = " FROM dristi_cases cases";
    private static final String ORDERBY_CREATEDTIME = " ORDER BY cases.createdtime DESC ";

    private static final String DOCUMENT_SELECT_QUERY = "Select doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore," +
            " doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.advocateid as advocateid ";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_case_document doc";


    private static final String BASE_LINKED_CASE_QUERY = " SELECT lics.id as id, lics.tenantid as tenantid, lics.casenumber as casenumber, " +
            "lics.filingnumber as filingnumber," +
            " lics.courtid as courtid, lics.benchid as benchid, lics.additionaldetails as additionaldetails, lics.createdby as createdby," +
            " lics.lastmodifiedby as lastmodifiedby, lics.createdtime as createdtime, lics.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_LINKED_CASE_TABLE = " FROM dristi_linked_case lics";


    private static final String BASE_LITIGANT_QUERY = " SELECT ltg.id as id, ltg.tenantid as tenantid, ltg.casenumber as casenumber, " +
            "ltg.filingnumber as filingnumber," +
            " ltg.courtid as courtid, ltg.benchid as benchid, ltg.additionaldetails as additionaldetails, ltg.createdby as createdby," +
            " ltg.lastmodifiedby as lastmodifiedby, ltg.createdtime as createdtime, ltg.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_LITIGANT_TABLE = " FROM dristi_case_litigants ltg";


    private static final String BASE_STATUTE_SECTION_QUERY = " SELECT stse.id as id, stse.tenantid as tenantid, stse.casenumber as casenumber, " +
            "stse.filingnumber as filingnumber," +
            " stse.courtid as courtid, stse.benchid as benchid, stse.additionaldetails as additionaldetails, stse.createdby as createdby," +
            " stse.lastmodifiedby as lastmodifiedby, stse.createdtime as createdtime, stse.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_STATUTE_SECTION_TABLE = " FROM dristi_case_statutes_and_sections stse";


    private static final String BASE_REPRESENTATIVES_QUERY = " SELECT rep.id as id, rep.tenantid as tenantid, rep.casenumber as casenumber, " +
            "rep.filingnumber as filingnumber," +
            " rep.courtid as courtid, rep.benchid as benchid, rep.additionaldetails as additionaldetails, rep.createdby as createdby," +
            " rep.lastmodifiedby as lastmodifiedby, rep.createdtime as createdtime, rep.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_REPRESENTATIVES_TABLE = " FROM dristi_case_representatives rep";

    public String getCasesSearchQuery(List<CaseCriteria> criteriaList, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_CASE_QUERY);
            query.append(FROM_CASES_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria
            if(criteriaList != null && !criteriaList.isEmpty()) {

                List<String> ids = criteriaList.stream()
                        .map(CaseCriteria::getCaseId)
                        .filter(Objects::nonNull)
                        .toList();

                List<String> cnrNumbers = criteriaList.stream()
                        .filter(criteria -> criteria.getCaseId() == null)
                        .map(CaseCriteria::getCnrNumber)
                        .filter(Objects::nonNull)
                        .toList();

                List<String> filingNumbers = criteriaList.stream()
                        .filter(criteria -> criteria.getCaseId() == null && criteria.getCnrNumber() == null)
                        .map(CaseCriteria::getFilingNumber)
                        .filter(Objects::nonNull)
                        .toList();

                if (!ids.isEmpty()) {
                    addClauseIfRequired(query, firstCriteria);
                    query.append("cases.id IN (")
                            .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                            .append(")");
                    preparedStmtList.addAll(ids);
                    firstCriteria = false; // Update firstCriteria flag
                }

                if (!cnrNumbers.isEmpty()) {
                    addClauseIfRequired(query, firstCriteria);
                    query.append("cases.casenumber IN (")
                            .append(cnrNumbers.stream().map(reg -> "?").collect(Collectors.joining(",")))
                            .append(")");
                    preparedStmtList.addAll(cnrNumbers);
                    firstCriteria = false; // Update firstCriteria flag

                }

                if (!filingNumbers.isEmpty()) {
                    addClauseIfRequired(query, firstCriteria);
                    query.append("cases.filingnumber IN (")
                            .append(filingNumbers.stream().map(num -> "?").collect(Collectors.joining(",")))
                            .append(")");
                    preparedStmtList.addAll(filingNumbers);
                    firstCriteria = false;
                }

//                if (!CollectionUtils.isEmpty(ids) || !CollectionUtils.isEmpty(cnrNumbers) || !CollectionUtils.isEmpty(filingNumbers)) {
//                    query.append(")");
//                }
            }
//            else if(applicationNumber != null && !applicationNumber.isEmpty()){
//                addClauseIfRequired(query, firstCriteria);
//                query.append("adv.applicationNumber LIKE ?")
//                        .append(")");
//                preparedStmtList.add("%" + applicationNumber + "%");
//                firstCriteria = false;
//            }
//            if (statusList != null && !statusList.isEmpty()) {
//                addClauseIfRequiredForStatus(query, firstCriteria);
//                query.append("adv.status IN (")
//                        .append(statusList.stream().map(num -> "?").collect(Collectors.joining(",")))
//                        .append(")");
//                preparedStmtList.addAll(statusList);
//            }
            query.append(ORDERBY_CREATEDTIME);

            return query.toString();
        }
         catch (Exception e) {
            log.error("Error while building advocate search query");
            throw new CustomException("ADVOCATE_SEARCH_QUERY_EXCEPTION","Error occurred while building the advocate search query: "+ e.getMessage());
        }
    }

    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" OR ");
        }
    }

//    private void addClauseIfRequiredForStatus(StringBuilder query, boolean isFirstCriteria) {
//        if (isFirstCriteria) {
//            query.append(" WHERE ");
//        } else {
//            query.append(" AND ");
//        }
//    }

    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.case_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building document search query");
            throw new CustomException("DOCUMENT_SEARCH_QUERY_EXCEPTION","Error occurred while building the query: "+ e.getMessage());
        }
    }

    public String getLinkedCaseSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_LINKED_CASE_QUERY);
            query.append(FROM_LINKED_CASE_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE lics.case_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building document search query");
            throw new CustomException("DOCUMENT_SEARCH_QUERY_EXCEPTION","Error occurred while building the query: "+ e.getMessage());
        }
    }

    public String getLitigantSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_LITIGANT_QUERY);
            query.append(FROM_LITIGANT_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE ltg.case_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building document search query");
            throw new CustomException("DOCUMENT_SEARCH_QUERY_EXCEPTION","Error occurred while building the query: "+ e.getMessage());
        }
    }

    public String getStatuteSectionSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_STATUTE_SECTION_QUERY);
            query.append(FROM_STATUTE_SECTION_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE stse.case_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building document search query");
            throw new CustomException("DOCUMENT_SEARCH_QUERY_EXCEPTION","Error occurred while building the query: "+ e.getMessage());
        }
    }

    public String getRepresentativesSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_REPRESENTATIVES_QUERY);
            query.append(FROM_REPRESENTATIVES_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE rep.case_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building document search query");
            throw new CustomException("DOCUMENT_SEARCH_QUERY_EXCEPTION","Error occurred while building the query: "+ e.getMessage());
        }
    }
}