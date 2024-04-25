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
    private static final String BASE_ATR_QUERY = " SELECT cases.id as id, cases.tenantid as tenantid, cases.casenumber as casenumber, " +
            "cases.filingnumber as filingnumber," +
            " cases.courtid as courtid, cases.benchid as benchid, cases.additionaldetails as additionaldetails, cases.createdby as createdby," +
            " cases.lastmodifiedby as lastmodifiedby, cases.createdtime as createdtime, cases.lastmodifiedtime as lastmodifiedtime ";

    private static final String DOCUMENT_SELECT_QUERY = "Select doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore," +
            " doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.advocateid as advocateid ";
    private static final String FROM_CASES_TABLE = " FROM dristi_cases cases";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_case_document doc";
    private static final String ORDERBY_CREATEDTIME = " ORDER BY cases.createdtime DESC ";

    public String getCasesSearchQuery(List<CaseCriteria> criteriaList, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            query.append(FROM_CASES_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria
            if(criteriaList != null && !criteriaList.isEmpty()) {

                // Collecting ids, application numbers, and bar registration numbers
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
                    query.append("cases.filingNumber IN (")
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

    private void addClauseIfRequiredForStatus(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }

    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.advocateid IN (")
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