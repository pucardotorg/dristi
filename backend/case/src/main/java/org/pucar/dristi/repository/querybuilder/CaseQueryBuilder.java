package org.pucar.dristi.repository.querybuilder;

import static org.pucar.dristi.config.ServiceConstants.CASE_SEARCH_QUERY_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.DOCUMENT_SEARCH_QUERY_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.LINKED_CASE_SEARCH_QUERY_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.LITIGANT_SEARCH_QUERY_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.REPRESENTATIVES_SEARCH_QUERY_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.REPRESENTING_SEARCH_QUERY_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.STATUTE_SECTION_SEARCH_QUERY_EXCEPTION;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.CaseExists;
import org.pucar.dristi.web.models.Pagination;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CaseQueryBuilder {
    private static final String BASE_CASE_QUERY = "   WITH PaginatedCases AS (\n" +
            "     SELECT id  \n" +
            "     FROM dristi_cases \n" +
            "      ORDER BY status \n" +
            "       limit 50\n" +
            "        offset 0\n" +
            ")\n" +
            "SELECT \n" +
            "    dc.id,\n" +
            "    dc.tenantid,\n" +
            "    dc.resolutionmechanism,\n" +
            "    dc.casetitle,\n" +
            "    dc.casedescription,\n" +
            "    dc.filingnumber,\n" +
            "    dc.casenumber,\n" +
            "    dc.cnrnumber,\n" +
            "    dc.courtcasenumber,\n" +
            "    dc.accesscode,\n" +
            "    dc.courtid,\n" +
            "    dc.benchid,\n" +
            "    dc.casecategory,\n" +
            "    dc.natureofpleading,\n" +
            "    dc.status,\n" +
            "    dc.remarks,\n" +
            "    dc.isactive,\n" +
            "    dc.casedetails,\n" +
            "    dc.additionaldetails,\n" +
            "    dc.createdby,\n" +
            "    dc.lastmodifiedby,\n" +
            "    dc.createdtime,\n" +
            "    dc.lastmodifiedtime,\n" +
            "    dc.judgeid,\n" +
            "    dc.stage,\n" +
            "    dc.substage,\n" +
            "    dc.filingdate,\n" +
            "    dc.registrationdate,\n" +
            "    dc.judgementdate,\n" +
            "    dc.outcome,\n" +
            "    dcl.id AS litigant_id,\n" +
            "    dcl.tenantid AS litigant_tenantid,\n" +
            "    dcl.partycategory,\n" +
            "    dcl.individualid AS litigant_individualid,\n" +
            "    dcl.organisationid AS litigant_organisationid,\n" +
            "    dcl.partytype,\n" +
            "    dcl.isactive AS litigant_isactive,\n" +
            "    dcl.additionaldetails AS litigant_additionaldetails,\n" +
            "    dcr.id AS representative_id,\n" +
            "    dcr.tenantid AS representative_tenantid,\n" +
            "    dcr.advocateid,\n" +
            "    dcr.isactive AS representative_isactive,\n" +
            "    dcr.additionaldetails AS representative_additionaldetails,\n" +
            "    dcrp.id AS representing_id,\n" +
            "    dcrp.tenantid AS representing_tenantid,\n" +
            "    dcrp.partycategory AS representing_partycategory,\n" +
            "    dcrp.individualid AS representing_individualid,\n" +
            "    dcrp.organisationid AS representing_organisationid,\n" +
            "    dcrp.partytype AS representing_partytype,\n" +
            "    dcrp.isactive AS representing_isactive,\n" +
            "    dcrp.representative_id AS representing_representative_id,\n" +
            "    dcrp.additionaldetails AS representing_additionaldetails,\n" +
            "    dcss.id AS statute_section_id,\n" +
            "    dcss.tenantid AS statute_section_tenantid,\n" +
            "    dcss.statutes,\n" +
            "    dcss.sections,\n" +
            "    dcss.subsections,\n" +
            "    dcss.additionaldetails AS statute_section_additionaldetails,\n" +
            "    dw.id AS witness_id,\n" +
            "    dw.caseid AS witness_caseid,\n" +
            "    dw.filingnumber AS witness_filingnumber,\n" +
            "    dw.cnrnumber AS witness_cnrnumber,\n" +
            "    dw.witnessidentifier,\n" +
            "    dw.individualid AS witness_individualid,\n" +
            "    dw.remarks AS witness_remarks,\n" +
            "    dw.isactive AS witness_isactive,\n" +
            "    dw.additionaldetails AS witness_additionaldetails\n" +
            "\n" +
            "FROM \n" +
            "    public.dristi_cases dc\n" +
            "LEFT JOIN \n" +
            "    public.dristi_case_litigants dcl ON dc.id = dcl.case_id\n" +
            "LEFT JOIN \n" +
            "    public.dristi_case_representatives dcr ON dc.id = dcr.case_id\n" +
            "LEFT JOIN \n" +
            "    public.dristi_case_representing dcrp ON dc.id = dcrp.case_id\n" +
            "LEFT JOIN \n" +
            "    public.dristi_case_statutes_and_sections dcss ON dc.id = dcss.case_id\n" +
            "LEFT JOIN \n" +
            "    public.dristi_linked_case dlc ON dc.id = dlc.case_id\n" +
            "LEFT JOIN \n" +
            "    public.dristi_witness dw ON dc.id = dw.caseid\n" +
            "    WHERE dc.id IN (SELECT id FROM PaginatedCases)";
    private static final String FROM_CASES_TABLE = " FROM dristi_cases dc";
    private static final String ORDERBY_CLAUSE = " ORDER BY dc.{orderBy} {sortingOrder} ";
    private static final String DEFAULT_ORDERBY_CLAUSE = " ORDER BY dc.createdtime DESC ";

    private static final String DOCUMENT_SELECT_QUERY_CASE = "Select doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore," +
            " doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.case_id as case_id, doc.linked_case_id as linked_case_id, doc.litigant_id as litigant_id, doc.representative_id as representative_id, doc.representing_id as representing_id ";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_case_document doc";

    private  static  final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM ({baseQuery}) total_result";


    private static final String BASE_LINKED_CASE_QUERY = " SELECT dlc.id as id, dlc.casenumbers as casenumbers, dlc.case_id as case_id," +
            "dlc.relationshiptype as relationshiptype," +
            " dlc.isactive as isactive, dlc.additionaldetails as additionaldetails, dlc.createdby as createdby," +
            " dlc.lastmodifiedby as lastmodifiedby, dlc.createdtime as createdtime, dlc.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_LINKED_CASE_TABLE = " FROM dristi_linked_case dlc";


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
            "rpst.individualid as individualid, rpst.case_id as case_id, " +
            " rpst.organisationid as organisationid, rpst.partytype as partytype, rpst.isactive as isactive, rpst.additionaldetails as additionaldetails, rpst.createdby as createdby," +
            " rpst.lastmodifiedby as lastmodifiedby, rpst.createdtime as createdtime, rpst.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_REPRESENTING_TABLE = " FROM dristi_case_representing rpst";

    private static final String BASE_CASE_EXIST_QUERY = " SELECT COUNT(*) FROM dristi_cases cases ";

    public static final String AND = " AND ";

    public String checkCaseExistQuery(CaseExists caseExists, List<Object> preparedStmtList, List<Integer> preparedStmtListArgs) {
        try {
            StringBuilder query = new StringBuilder(BASE_CASE_EXIST_QUERY);
            boolean firstCriteria = true;

            if(caseExists != null){
                firstCriteria = addCriteria(caseExists.getCaseId(), query, firstCriteria, "cases.id = ?", preparedStmtList, preparedStmtListArgs, Types.VARCHAR);

                firstCriteria = addCriteria(caseExists.getCnrNumber(), query, firstCriteria, "cases.cnrNumber = ?", preparedStmtList, preparedStmtListArgs, Types.VARCHAR);

                firstCriteria = addCriteria(caseExists.getFilingNumber(), query, firstCriteria, "cases.filingnumber = ?", preparedStmtList, preparedStmtListArgs, Types.VARCHAR);

                addCriteria(caseExists.getCourtCaseNumber(), query, firstCriteria, "cases.courtcasenumber = ?", preparedStmtList, preparedStmtListArgs, Types.VARCHAR);

                query.append(";");
            }
            return query.toString();
        } catch (Exception e) {
            log.error("Error while building case exist query", e);
            throw new CustomException(CASE_SEARCH_QUERY_EXCEPTION, "Error occurred while building the case exist query: " + e.getMessage());
        }
    }

    public String getCasesSearchQuery(CaseCriteria criteria, List<Object> preparedStmtList, List<Integer> preparedStmtArgList,RequestInfo requestInfo) {
        try {
            StringBuilder query = new StringBuilder(BASE_CASE_QUERY);
//            query.append(FROM_CASES_TABLE);
            boolean firstCriteria = false; // To check if it's the first criteria
            if (criteria != null) {

                firstCriteria = addCriteria(criteria.getCaseId(), query, firstCriteria, "dc.id = ?", preparedStmtList, preparedStmtArgList, Types.VARCHAR);

                firstCriteria = addCriteria(criteria.getCnrNumber(), query, firstCriteria, "dc.cnrNumber = ?", preparedStmtList,preparedStmtArgList, Types.VARCHAR);

                firstCriteria = addCriteria(criteria.getFilingNumber() == null? null : "%" + criteria.getFilingNumber() + "%", query, firstCriteria, "LOWER(dc.filingnumber) LIKE LOWER(?)", preparedStmtList,preparedStmtArgList, Types.VARCHAR);

                firstCriteria = addCriteria(criteria.getCourtCaseNumber(), query, firstCriteria, "dc.courtcasenumber = ?", preparedStmtList,preparedStmtArgList, Types.VARCHAR);

                firstCriteria = addCriteria(criteria.getJudgeId(), query, firstCriteria, "dc.judgeid = ?", preparedStmtList,preparedStmtArgList, Types.VARCHAR);

                firstCriteria = addListCriteria(criteria.getStage(), query, firstCriteria, "dc.stage", preparedStmtList, preparedStmtArgList, Types.VARCHAR);

                firstCriteria = addListCriteria(criteria.getOutcome(), query, firstCriteria, "dc.outcome", preparedStmtList, preparedStmtArgList, Types.VARCHAR);

                firstCriteria = addCriteria(criteria.getSubstage(), query, firstCriteria, "dc.substage = ?",preparedStmtList, preparedStmtArgList, Types.VARCHAR);

                firstCriteria = addLitigantCriteria(criteria,preparedStmtList, preparedStmtArgList, requestInfo, query, firstCriteria);

                firstCriteria = addAdvocateCriteria(criteria,preparedStmtList, preparedStmtArgList, requestInfo, query, firstCriteria);

                firstCriteria = addListCriteria(criteria.getStatus(), query, firstCriteria, "dc.status", preparedStmtList,preparedStmtArgList, Types.VARCHAR);

                firstCriteria = addFilingDateCriteria(criteria, firstCriteria, query, preparedStmtList, preparedStmtArgList);

                addRegistrationDateCriteria(criteria, firstCriteria, query, preparedStmtList, preparedStmtArgList);
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building case search query :: {}",e.toString());
            throw new CustomException(CASE_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the case search query: " + e.getMessage());
        }
    }

    private boolean addListCriteria(List<String> itemList, StringBuilder query, boolean firstCriteria, String str, List<Object> preparedStmtList, List<Integer> preparedStmtArgList, int varchar) {
        if (itemList != null && !itemList.isEmpty()) {
            addClauseIfRequired(query, firstCriteria);
            prepareStatementAndArgumentForListCriteria(itemList, query, str , preparedStmtList, preparedStmtArgList, varchar);
            firstCriteria = false;
        }
        return firstCriteria;
    }

    private static void prepareStatementAndArgumentForListCriteria(List<String> itemList, StringBuilder query, String str, List<Object> preparedStmtList, List<Integer> preparedStmtArgList, int varchar) {
        if (!itemList.isEmpty()) {
            query.append(str).append(" IN (")
                    .append(itemList.stream().map(id -> "?").collect(Collectors.joining(",")))
                    .append(")");
            preparedStmtList.addAll(itemList);
            itemList.forEach(i->preparedStmtArgList.add(varchar));
        }
    }

    private static void addRegistrationDateCriteria(CaseCriteria criteria, boolean firstCriteria, StringBuilder query, List<Object> preparedStmtList, List<Integer> preparedStmtListArgs) {
        if (criteria.getRegistrationFromDate() != null && criteria.getRegistrationToDate() != null) {
            if (!firstCriteria)
                query.append(" OR cases.registrationdate>= ? AND cases.registrationdate <= ? ").append(" ");
            else {
                query.append(" AND cases.registrationdate>= ? AND cases.registrationdate <= ? ").append(" ");
            }
            preparedStmtList.add(criteria.getRegistrationFromDate());
            preparedStmtListArgs.add(Types.TIMESTAMP);
            preparedStmtList.add(criteria.getRegistrationToDate());
            preparedStmtListArgs.add(Types.TIMESTAMP);
        }
    }

    private static boolean addFilingDateCriteria(CaseCriteria criteria, boolean firstCriteria, StringBuilder query, List<Object> preparedStmtList, List<Integer> preparedStmtListArgs) {
        if (criteria.getFilingFromDate() != null && criteria.getFilingToDate() != null) {
            if (!firstCriteria)
                query.append(" OR cases.filingdate >= ? AND cases.filingdate <= ? ").append(" ");
            else {
                query.append(" AND cases.filingdate >= ? AND cases.filingdate <= ? ").append(" ");
            }
            preparedStmtList.add(criteria.getFilingFromDate());
            preparedStmtListArgs.add(Types.TIMESTAMP);
            preparedStmtList.add(criteria.getFilingToDate());
            preparedStmtListArgs.add(Types.TIMESTAMP);
            firstCriteria = false;
        }
        return firstCriteria;
    }

    private boolean addAdvocateCriteria(CaseCriteria criteria, List<Object> preparedStmtList, List<Integer> preparedStmtArgList, RequestInfo requestInfo, StringBuilder query, boolean firstCriteria) {
        if (criteria.getAdvocateId() != null && !criteria.getAdvocateId().isEmpty()) {
            addClauseIfRequired(query, firstCriteria);
            query.append("((cases.id IN ( SELECT advocate.case_id from dristi_case_representatives advocate WHERE advocate.advocateId = ? AND advocate.isactive = true) AND cases.status not in ('DRAFT_IN_PROGRESS')) OR cases.status ='DRAFT_IN_PROGRESS' AND cases.createdby = ?) AND (cases.status NOT IN ('DELETED_DRAFT'))");
            preparedStmtList.add(criteria.getAdvocateId());
            preparedStmtArgList.add(Types.VARCHAR);
            preparedStmtList.add(requestInfo.getUserInfo().getUuid());
            preparedStmtArgList.add(Types.VARCHAR);
            firstCriteria = false;
        }
        return firstCriteria;
    }

    private boolean addLitigantCriteria(CaseCriteria criteria, List<Object> preparedStmtList,List<Integer> preparedStmtArgList, RequestInfo requestInfo, StringBuilder query, boolean firstCriteria) {
        if (criteria.getLitigantId() != null && !criteria.getLitigantId().isEmpty()) {
            addClauseIfRequired(query, firstCriteria);
            query.append("((cases.id IN ( SELECT litigant.case_id from dristi_case_litigants litigant WHERE litigant.individualId = ? AND litigant.isactive = true) AND cases.status not in ('DRAFT_IN_PROGRESS')) OR cases.status ='DRAFT_IN_PROGRESS' AND cases.createdby = ?) AND (cases.status NOT IN ('DELETED_DRAFT'))");
            preparedStmtList.add(criteria.getLitigantId());
            preparedStmtArgList.add(Types.VARCHAR);
            preparedStmtList.add(requestInfo.getUserInfo().getUuid());
            preparedStmtArgList.add(Types.VARCHAR);
            firstCriteria = false;
        }
        return firstCriteria;
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

    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(AND);
        }
    }

    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList,List<Integer> preparedStmtArgList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY_CASE);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.case_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
                ids.forEach(i->preparedStmtArgList.add(Types.VARCHAR));
            }

            return query.toString();
        }  catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error while building document search query :: {}",e.toString());
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the document query: " + e.getMessage());
        }
    }

    public String getLinkedCaseSearchQuery(List<String> ids, List<Object> preparedStmtList, List<Integer> preparedStmtArgList) {
        try {
            StringBuilder query = new StringBuilder(BASE_LINKED_CASE_QUERY);
            query.append(FROM_LINKED_CASE_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE dlc.case_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
                ids.forEach(i->preparedStmtArgList.add(Types.VARCHAR));
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building linked case search query :: {}",e.toString());
            throw new CustomException(LINKED_CASE_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the linked case search query: " + e.getMessage());
        }
    }

    public String getLitigantSearchQuery(List<String> ids, List<Object> preparedStmtList, List<Integer> preparedStmtArgList) {
        try {
            StringBuilder query = new StringBuilder(BASE_LITIGANT_QUERY);
            query.append(FROM_LITIGANT_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE ltg.case_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")")
                        .append(AND)
                        .append("ltg.isactive = true");
                preparedStmtList.addAll(ids);
                ids.forEach(i->preparedStmtArgList.add(Types.VARCHAR));
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building litigant search query :: {}",e.toString());
            throw new CustomException(LITIGANT_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the litigant query: " + e.getMessage());
        }
    }

    public String getStatuteSectionSearchQuery(List<String> ids, List<Object> preparedStmtList, List<Integer> preparedStmtArgList) {
        try {
            StringBuilder query = new StringBuilder(BASE_STATUTE_SECTION_QUERY);
            query.append(FROM_STATUTE_SECTION_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE stse.case_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
                ids.forEach(i->preparedStmtArgList.add(Types.VARCHAR));
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building statute section search query :: {}",e.toString());
            throw new CustomException(STATUTE_SECTION_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the statue section query: " + e.getMessage());
        }
    }

    public String getRepresentativesSearchQuery(List<String> ids, List<Object> preparedStmtList, List<Integer> preparedStmtArgList) {
        try {
            StringBuilder query = new StringBuilder(BASE_REPRESENTATIVES_QUERY);
            query.append(FROM_REPRESENTATIVES_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE rep.case_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")")
                        .append(AND)
                        .append("rep.isactive = true");
                preparedStmtList.addAll(ids);
                ids.forEach(i->preparedStmtArgList.add(Types.VARCHAR));
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building representatives search query :: {}",e.toString());
            throw new CustomException(REPRESENTATIVES_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the representative search query: " + e.getMessage());
        }
    }

    public String getRepresentingSearchQuery(List<String> ids, List<Object> preparedStmtList, List<Integer> preparedStmtArgList) {
        try {
            StringBuilder query = new StringBuilder(BASE_REPRESENTING_QUERY);
            query.append(FROM_REPRESENTING_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE rpst.representative_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")")
                        .append(AND)
                        .append("rpst.isactive = true");
                preparedStmtList.addAll(ids);
                ids.forEach(i->preparedStmtArgList.add(Types.VARCHAR));
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building representing search query :: {}",e.toString());
            throw new CustomException(REPRESENTING_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the representing search query: " + e.getMessage());
        }
    }

    public String getLinkedCaseDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList, List<Integer> preparedStmtArgList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY_CASE);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.linked_case_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
                ids.forEach(i->preparedStmtArgList.add(Types.VARCHAR));
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building linked case document search query :: {}",e.toString());
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the linked case document search query: " + e.getMessage());
        }
    }

    public String getLitigantDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList, List<Integer> preparedStmtArgList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY_CASE);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.litigant_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
                ids.forEach(i->preparedStmtArgList.add(Types.VARCHAR));
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building litigant document search query :: {}",e.toString());
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the litigant document search query: " + e.getMessage());
        }
    }

    public String getRepresentativeDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList, List<Integer> preparedStmtArgList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY_CASE);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.representative_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
                ids.forEach(i->preparedStmtArgList.add(Types.VARCHAR));
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building representative document search query :: {}",e.toString());
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the representative document search query: " + e.getMessage());
        }
    }

    public String getRepresentingDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList, List<Integer> preparedStmtArgList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY_CASE);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.representing_id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
                ids.forEach(i->preparedStmtArgList.add(Types.VARCHAR));
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building representing document search query :: {}",e.toString());
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION, "Exception occurred while building the representing document search query: " + e.getMessage());
        }
    }

    public String getTotalCountQuery(String baseQuery) {
        return TOTAL_COUNT_QUERY.replace("{baseQuery}", baseQuery);
    }
    public String addPaginationQuery(String query, List<Object> preparedStatementList, Pagination pagination, List<Integer> preparedStmtArgList) {
        preparedStatementList.add(pagination.getLimit());
        preparedStmtArgList.add(Types.DOUBLE);

        preparedStatementList.add(pagination.getOffSet());
        preparedStmtArgList.add(Types.DOUBLE);
        return query + " LIMIT ? OFFSET ?";

    }
    public String addOrderByQuery(String query, Pagination pagination) {
        if (isEmptyPagination(pagination) || pagination.getSortBy().contains(";")) {
            return query + DEFAULT_ORDERBY_CLAUSE;
        } else {
            query = query + ORDERBY_CLAUSE;
        }
        return query.replace("{orderBy}", pagination.getSortBy()).replace("{sortingOrder}", pagination.getOrder().name());
    }

    private boolean isEmptyPagination(Pagination pagination) {
        return pagination == null || pagination.getSortBy()==null || pagination.getOrder() == null;
    }
}