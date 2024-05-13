package org.pucar.dristi.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.CaseCriteria;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
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

                List<String> courtCaseNumbers = criteriaList.stream()
                        .filter(criteria -> criteria.getCaseId() == null && criteria.getCnrNumber() == null && criteria.getFilingNumber() == null)
                        .map(CaseCriteria::getCourtCaseNumber)
                        .filter(Objects::nonNull)
                        .toList();


                if (!ids.isEmpty()) {
                    addClauseIfRequired(query, firstCriteria);
                    query.append("cases.id IN (")
                            .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                            .append(")");
                    preparedStmtList.addAll(ids);
                    firstCriteria = false;
                }

                if (!cnrNumbers.isEmpty()) {
                    addClauseIfRequired(query, firstCriteria);
                    query.append("cases.cnrNumber IN (")
                            .append(cnrNumbers.stream().map(reg -> "?").collect(Collectors.joining(",")))
                            .append(")");
                    preparedStmtList.addAll(cnrNumbers);
                    firstCriteria = false;

                }

                if (!filingNumbers.isEmpty()) {
                    addClauseIfRequired(query, firstCriteria);
                    query.append("cases.filingnumber IN (")
                            .append(filingNumbers.stream().map(num -> "?").collect(Collectors.joining(",")))
                            .append(")");
                    preparedStmtList.addAll(filingNumbers);
                    firstCriteria = false;
                }

                if (!courtCaseNumbers.isEmpty()) {
                    addClauseIfRequired(query, firstCriteria);
                    query.append("cases.courtcasenumber IN (")
                            .append(courtCaseNumbers.stream().map(num -> "?").collect(Collectors.joining(",")))
                            .append(")");
                    preparedStmtList.addAll(courtCaseNumbers);
                    firstCriteria = false;
                }

                for(CaseCriteria caseCriteria:criteriaList){
                    if(caseCriteria.getFilingFromDate() != null && caseCriteria.getFilingToDate()!=null){
                        if(firstCriteria==false)
                          query.append("OR cases.filingdate BETWEEN "+caseCriteria.getFilingFromDate()+" AND "+caseCriteria.getFilingToDate()+" ");
                        else{
                            query.append("WHERE cases.filingdate BETWEEN "+caseCriteria.getFilingFromDate()+" AND "+caseCriteria.getFilingToDate()+" ");
                        }
                        firstCriteria =false;
                    }

                    if(caseCriteria.getRegistrationFromDate() != null && caseCriteria.getRegistrationToDate()!=null){
                        if(firstCriteria==false)
                            query.append("OR cases.registrationdate BETWEEN "+caseCriteria.getRegistrationFromDate()+" AND "+caseCriteria.getRegistrationToDate()+" ");
                        else{
                            query.append("WHERE cases.registrationdate BETWEEN "+caseCriteria.getRegistrationFromDate()+" AND "+caseCriteria.getRegistrationToDate()+" ");
                        }
                        firstCriteria =false;
                    }
                }

            }
            query.append(ORDERBY_CREATEDTIME);

            return query.toString();
        }
         catch (Exception e) {
            log.error("Error while building case search query");
            throw new CustomException(CASE_SEARCH_QUERY_EXCEPTION,"Error occurred while building the case search query: "+ e.getMessage());
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
                query.append(" WHERE doc.case_id IN (")
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
            log.error("Error while building linked case search query");
            throw new CustomException(LINKED_CASE_SEARCH_QUERY_EXCEPTION,"Error occurred while building the query: "+ e.getMessage());
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
            log.error("Error while building litigant search query");
            throw new CustomException(LITIGANT_SEARCH_QUERY_EXCEPTION,"Error occurred while building the query: "+ e.getMessage());
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
            log.error("Error while building statute section search query");
            throw new CustomException(STATUTE_SECTION_SEARCH_QUERY_EXCEPTION,"Error occurred while building the query: "+ e.getMessage());
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
            log.error("Error while building representatives search query");
            throw new CustomException(REPRESENTATIVES_SEARCH_QUERY_EXCEPTION,"Error occurred while building the query: "+ e.getMessage());
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
            log.error("Error while building representing search query");
            throw new CustomException(REPRESENTING_SEARCH_QUERY_EXCEPTION,"Error occurred while building the query: "+ e.getMessage());
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
            log.error("Error while building document search query");
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,"Error occurred while building the query: "+ e.getMessage());
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
            log.error("Error while building document search query");
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,"Error occurred while building the query: "+ e.getMessage());
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
            log.error("Error while building document search query");
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,"Error occurred while building the query: "+ e.getMessage());
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
            log.error("Error while building document search query");
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,"Error occurred while building the query: "+ e.getMessage());
        }
    }
}