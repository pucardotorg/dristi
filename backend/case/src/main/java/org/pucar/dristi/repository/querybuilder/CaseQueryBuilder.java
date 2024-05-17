package org.pucar.dristi.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.CaseCriteria;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class CaseQueryBuilder {
    private static final String BASE_CASE_QUERY = " SELECT cases.id as id, cases.tenantid as tenantid, cases.casenumber as casenumber, cases.resolutionmechanism as resolutionmechanism, cases.casetitle as casetitle, cases.casedescription as casedescription, " +
            "cases.filingnumber as filingnumber, cases.casenumber as casenumber, cases.accesscode as accesscode, cases.courtcasenumber as courtcasenumber, cases.cnrNumber as cnrNumber, " +
            " cases.courtid as courtid, cases.benchid as benchid, cases.filingdate as filingdate, cases.registrationdate as registrationdate, cases.natureofpleading as natureofpleading, cases.status as status, cases.remarks as remarks, cases.isactive as isactive, cases.casedetails as casedetails, cases.additionaldetails as additionaldetails, cases.casecategory as casecategory, cases.createdby as createdby," +
            " cases.lastmodifiedby as lastmodifiedby, cases.createdtime as createdtime, cases.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_CASES_TABLE = " FROM dristi_cases cases";
    private static final String ORDERBY_CREATEDTIME = " ORDER BY cases.createdtime DESC ";

    private static final String DOCUMENT_SELECT_QUERY_CASE = "Select doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore," +
            " doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.case_id as case_id, doc.linked_case_id as linked_case_id, doc.litigant_id as litigant_id, doc.representative_id as representative_id, doc.representing_id as representing_id ";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_case_document doc";


    private static final String BASE_LINKED_CASE_QUERY = " SELECT lics.id as id, lics.casenumbers as casenumbers, lics.case_id as case_id," +
            "lics.relationshiptype as relationshiptype," +
            " lics.isactive as isactive, lics.additionaldetails as additionaldetails, lics.createdby as createdby," +
            " lics.lastmodifiedby as lastmodifiedby, lics.createdtime as createdtime, lics.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_LINKED_CASE_TABLE = " FROM dristi_linked_case lics";


    private static final String BASE_LITIGANT_QUERY = " SELECT ltg.id as id, ltg.tenantid as tenantid, ltg.partycategory as partycategory, ltg.case_id as case_id, " +
            "ltg.individualid as individualid, " +
            " ltg.organisationid as organisationid, ltg.partytype as partytype, ltg.isactive as isactive, ltg.additionaldetails as additionaldetails, ltg.createdby as createdby," +
            " ltg.lastmodifiedby as lastmodifiedby, ltg.createdtime as createdtime, ltg.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_LITIGANT_TABLE = " FROM dristi_case_litigants ltg";


    private static final String BASE_STATUTE_SECTION_QUERY = " SELECT stse.id as id, stse.tenantid as tenantid, stse.statutes as statutes, stse.case_id as case_id, " +
            "stse.sections as sections," +
            " stse.subsections as subsections, stse.additionaldetails as additionaldetails, stse.createdby as createdby," +
            " stse.lastmodifiedby as lastmodifiedby, stse.createdtime as createdtime, stse.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_STATUTE_SECTION_TABLE = " FROM dristi_case_statutes_and_sections stse";


    private static final String BASE_REPRESENTATIVES_QUERY = " SELECT rep.id as id, rep.tenantid as tenantid, rep.advocateid as advocateid, rep.case_id as case_id, " +
            " rep.isactive as isactive, rep.additionaldetails as additionaldetails, rep.createdby as createdby," +
            " rep.lastmodifiedby as lastmodifiedby, rep.createdtime as createdtime, rep.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_REPRESENTATIVES_TABLE = " FROM dristi_case_representatives rep";

    private static final String BASE_REPRESENTING_QUERY = " SELECT rpst.id as id, rpst.tenantid as tenantid, rpst.partycategory as partycategory, rpst.representative_id as representative_id, " +
            "rpst.individualid as individualid, " +
            " rpst.organisationid as organisationid, rpst.partytype as partytype, rpst.isactive as isactive, rpst.additionaldetails as additionaldetails, rpst.createdby as createdby," +
            " rpst.lastmodifiedby as lastmodifiedby, rpst.createdtime as createdtime, rpst.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_REPRESENTING_TABLE = " FROM dristi_case_representing rpst";

    public String getCasesSearchQuery(List<CaseCriteria> criteriaList, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_CASE_QUERY);
            query.append(FROM_CASES_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria
            if(criteriaList != null && !criteriaList.isEmpty()) {
                addCriteriaClauses(criteriaList, query, preparedStmtList, firstCriteria);
                addDateRangeClauses(criteriaList, query, firstCriteria);
            }
            query.append(ORDERBY_CREATEDTIME);
            return query.toString();
        } catch (Exception e) {
            log.error(CASE_QUERY_ERROR);
            throw new CustomException(CASE_SEARCH_QUERY_EXCEPTION,"Error occurred while building the case search query: "+ e.getMessage());
        }
    }

    private void addCriteriaClauses(List<CaseCriteria> criteriaList, StringBuilder query, List<Object> preparedStmtList, boolean firstCriteria) {
        List<String> ids = new ArrayList<>();
        List<String> cnrNumbers = new ArrayList<>();
        List<String> filingNumbers = new ArrayList<>();
        List<String> courtCaseNumbers = new ArrayList<>();

        for (CaseCriteria criteria : criteriaList) {
            if (criteria.getCaseId() != null) {
                ids.add(criteria.getCaseId());
            } else if (criteria.getCnrNumber() != null) {
                cnrNumbers.add(criteria.getCnrNumber());
            } else if (criteria.getFilingNumber() != null) {
                filingNumbers.add(criteria.getFilingNumber());
            } else if (criteria.getCourtCaseNumber() != null) {
                courtCaseNumbers.add(criteria.getCourtCaseNumber());
            }
        }

        addClauseAndValues(query, "cases.id", ids, preparedStmtList, firstCriteria);
        addClauseAndValues(query, "cases.cnrNumber", cnrNumbers, preparedStmtList, firstCriteria);
        addClauseAndValues(query, "cases.filingnumber", filingNumbers, preparedStmtList, firstCriteria);
        addClauseAndValues(query, "cases.courtcasenumber", courtCaseNumbers, preparedStmtList, firstCriteria);
    }

    private void addDateRangeClauses(List<CaseCriteria> criteriaList, StringBuilder query, boolean firstCriteria) {
        for (CaseCriteria caseCriteria : criteriaList) {
            if (caseCriteria.getFilingFromDate() != null && caseCriteria.getFilingToDate() != null) {
                addDateRangeClause(query, "cases.filingdate", caseCriteria.getFilingFromDate(), caseCriteria.getFilingToDate(), firstCriteria);
            }

            if (caseCriteria.getRegistrationFromDate() != null && caseCriteria.getRegistrationToDate() != null) {
                addDateRangeClause(query, "cases.registrationdate", caseCriteria.getRegistrationFromDate(), caseCriteria.getRegistrationToDate(), firstCriteria);
            }
        }
    }


    private void addClauseAndValues(StringBuilder query, String fieldName, List<String> values, List<Object> preparedStmtList, boolean firstCriteria) {
        if (!values.isEmpty()) {
            addClauseIfRequired(query, firstCriteria);
            query.append(fieldName).append(" IN (")
                    .append(values.stream().map(reg -> "?").collect(Collectors.joining(",")))
                    .append(")");
            preparedStmtList.addAll(values);
        }
    }

    private void addDateRangeClause(StringBuilder query, String fieldName, LocalDate fromDate, LocalDate toDate, boolean firstCriteria) {
        if (firstCriteria) {
            query.append("WHERE ");
        } else {
            query.append("OR ");
        }
        query.append(fieldName).append(" BETWEEN ").append(fromDate).append(" AND ").append(toDate).append(" ");
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
                query.append(" WHERE doc.case_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error(DOCUMENT_QUERY_ERROR);
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,QUERY_BUILD_ERROR+ e.getMessage());
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
            log.error(LINKED_CASE_QUERY_ERROR);
            throw new CustomException(LINKED_CASE_SEARCH_QUERY_EXCEPTION,QUERY_BUILD_ERROR+ e.getMessage());
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
            log.error(LITIGANT_QUERY_ERROR);
            throw new CustomException(LITIGANT_SEARCH_QUERY_EXCEPTION,QUERY_BUILD_ERROR+ e.getMessage());
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
            log.error(STATUTE_QUERY_ERROR);
            throw new CustomException(STATUTE_SECTION_SEARCH_QUERY_EXCEPTION,QUERY_BUILD_ERROR+ e.getMessage());
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
            log.error(REPRESENTATIVE_QUERY_ERROR);
            throw new CustomException(REPRESENTATIVES_SEARCH_QUERY_EXCEPTION,QUERY_BUILD_ERROR+ e.getMessage());
        }
    }

    public String getRepresentingSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_REPRESENTING_QUERY);
            query.append(FROM_REPRESENTING_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE rpst.representative_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error(REPRESENTING_QUERY_ERROR);
            throw new CustomException(REPRESENTING_SEARCH_QUERY_EXCEPTION,QUERY_BUILD_ERROR+ e.getMessage());
        }
    }

    public String getLinkedCaseDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY_CASE);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.linked_case_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error(DOCUMENT_QUERY_ERROR);
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,QUERY_BUILD_ERROR+ e.getMessage());
        }
    }

    public String getLitigantDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY_CASE);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.litigant_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error(DOCUMENT_QUERY_ERROR);
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,QUERY_BUILD_ERROR+ e.getMessage());
        }
    }

    public String getRepresentativeDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY_CASE);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.representative_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error(DOCUMENT_QUERY_ERROR);
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,QUERY_BUILD_ERROR+ e.getMessage());
        }
    }

    public String getRepresentingDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY_CASE);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.representing_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error(DOCUMENT_QUERY_ERROR);
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,QUERY_BUILD_ERROR+ e.getMessage());
        }
    }
}