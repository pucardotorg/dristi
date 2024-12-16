package org.pucar.dristi.repository.querybuilder;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.pucar.dristi.web.OpenApiCaseSummary;
import org.pucar.dristi.web.models.OpenApiCaseSummaryRequest;
import org.pucar.dristi.web.models.Pagination;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class OpenApiCaseSummaryQueryBuilder {

    private static final String CASE_BASE_QUERY = "SELECT cases.id, cases.tenantid, cases.casenumber, cases.casetitle, cases.filingnumber, " +
            "         cases.cnrNumber, cases.outcome, cases.cmpnumber, cases.courtcasenumber, cases.courtid, cases.benchid, " +
            "         cases.casetype, cases.judgeid, cases.stage, cases.substage, cases.filingdate, " +
            "         cases.registrationdate, cases.status, cases.isactive, cases.casecategory, " +
            "         cases.createdby, cases.lastmodifiedby, cases.createdtime, cases.lastmodifiedtime ";

    private static final String CASE_SUMMARY_QUERY = "SELECT uc.id as id, uc.tenantid as tenantid, uc.casenumber as casenumber, uc.casetitle as casetitle, " +
            "       uc.filingnumber as filingnumber, uc.cnrNumber as cnrNumber, uc.outcome as outcome, " +
            "       uc.cmpnumber, uc.courtid as courtid, uc.benchid as benchid, uc.casetype, " +
            "       uc.judgeid as judgeid, uc.stage as stage, uc.substage as substage, uc.filingdate as filingdate, " +
            "       uc.registrationdate as registrationdate, uc.status as status, uc.isactive as isactive, " +
            "       uc.casecategory as casecategory, uc.createdby as createdby, uc.lastmodifiedby as lastmodifiedby, " +
            "       uc.createdtime as createdtime, uc.lastmodifiedtime as lastmodifiedtime," +

            //       -- Litigants table columns
            "       ltg.id as litigant_id, ltg.tenantid as litigant_tenantid, ltg.partycategory as litigant_partycategory, " +
            "       ltg.case_id as litigant_case_id, ltg.individualid as litigant_individualid, ltg.organisationid as litigant_organisationid, " +
            "       ltg.partytype as litigant_partytype, ltg.isactive as litigant_isactive, ltg.additionaldetails as litigant_additionaldetails, " +
            "       ltg.createdby as litigant_createdby, ltg.lastmodifiedby as litigant_lastmodifiedby, " +
            "       ltg.createdtime as litigant_createdtime, ltg.lastmodifiedtime as litigant_lastmodifiedtime," +

            //       -- Statutes and sections table columns"
            "       stse.id as statute_section_id, stse.tenantid as statute_section_tenantid, stse.statutes as statute_section_statutes, " +
            "       stse.case_id as statute_section_case_id, stse.sections as statute_section_sections, " +
            "       stse.subsections as statute_section_subsections, stse.additionaldetails as statute_section_additionaldetails, " +
            "       stse.createdby as statute_section_createdby, stse.lastmodifiedby as statute_section_lastmodifiedby, " +
            "       stse.createdtime as statute_section_createdtime, stse.lastmodifiedtime as statute_section_lastmodifiedtime," +

            //   -- Representatives table columns"
            "       rep.id as representative_id, rep.tenantid as representative_tenantid, rep.advocateid as representative_advocateid, " +
            "       rep.case_id as representative_case_id, rep.isactive as representative_isactive, " +
            "       rep.additionaldetails as representative_additionaldetails, rep.createdby as representative_createdby, " +
            "       rep.lastmodifiedby as representative_lastmodifiedby, rep.createdtime as representative_createdtime, " +
            "       rep.lastmodifiedtime as representative_lastmodifiedtime," +

            //       -- Representing table columns"
            "       rpst.id as representing_id, rpst.tenantid as representing_tenantid, rpst.partycategory as representing_partycategory, " +
            "       rpst.representative_id as representing_representative_id, rpst.individualid as representing_individualid, " +
            "       rpst.case_id as representing_case_id, rpst.organisationid as representing_organisationid, " +
            "       rpst.partytype as representing_partytype, rpst.isactive as representing_isactive, " +
            "       rpst.additionaldetails as representing_additionaldetails, rpst.createdby as representing_createdby, " +
            "       rpst.lastmodifiedby as representing_lastmodifiedby, rpst.createdtime as representing_createdtime, " +
            "       rpst.lastmodifiedtime as representing_lastmodifiedtime";

    private static final String CASE_TABLE_QUERY = " FROM dristi_cases cases";

    private static final String FROM_UNIQUE_CASE_TABLE = " FROM unique_cases uc ";

    public static final String LEFT_JOIN = " LEFT JOIN ";

    private static final String FROM_LITIGANT_TABLE = "  dristi_case_litigants ltg";
    private static final String FROM_STATUTE_SECTION_TABLE = "  dristi_case_statutes_and_sections stse";
    private static final String FROM_REPRESENTATIVES_TABLE = "  dristi_case_representatives rep";
    private static final String FROM_REPRESENTING_TABLE = "  dristi_case_representing rpst";

    private static final String LEFT_JOIN_QUERY =
            LEFT_JOIN + FROM_LITIGANT_TABLE + " ON uc.id = ltg.case_id " +
                    LEFT_JOIN + FROM_STATUTE_SECTION_TABLE + " ON uc.id = stse.case_id " +
                    " LEFT JOIN ( " +
                    FROM_REPRESENTATIVES_TABLE +
                    LEFT_JOIN + FROM_REPRESENTING_TABLE + " ON rep.id = rpst.representative_id " +
                    " ) ON uc.id = rep.case_id ";

    private static final String ORDERBY_CLAUSE = " ORDER BY cases.{orderBy} {sortingOrder} ";
    private static final String DEFAULT_ORDERBY_CLAUSE = " ORDER BY cases.registrationDate DESC ";



    public String getCaseBaseQuery(OpenApiCaseSummaryRequest searchCriteria, List<Object> preparedStatementValues, List<Integer> preparedStatementValueTypes) {
        boolean firstCriteria = true;
        StringBuilder query = new StringBuilder(CASE_BASE_QUERY);
        query.append(CASE_TABLE_QUERY);

        if (searchCriteria.getTenantId() != null) {
            addWhereClause(query, firstCriteria);
            query.append("cases.tenantId = ?");
            preparedStatementValues.add(searchCriteria.getTenantId());
            preparedStatementValueTypes.add(Types.VARCHAR);
            firstCriteria = false;
        }

        if (searchCriteria.getCnrNumber() != null) {
            addWhereClause(query, firstCriteria);
            query.append("cases.cnrNumber = ?");
            preparedStatementValues.add(searchCriteria.getCnrNumber());
            preparedStatementValueTypes.add(Types.VARCHAR);
            firstCriteria = false;
        }

        if (searchCriteria.getCaseType() != null) {
            addWhereClause(query, firstCriteria);
            query.append("cases.caseType = ?");
            preparedStatementValues.add(searchCriteria.getCaseType());
            preparedStatementValueTypes.add(Types.VARCHAR);
            firstCriteria = false;
        }

        if (Objects.equals(searchCriteria.getCaseType(), OpenApiCaseSummary.CaseTypeEnum.CMP.name())) {
            if (searchCriteria.getYear() != null && searchCriteria.getStartYear() != null && searchCriteria.getEndYear() != null) {
                addWhereClause(query, firstCriteria);
                query.append("cases.registrationdate BETWEEN ? AND ?");
                preparedStatementValues.add(searchCriteria.getStartYear());
                preparedStatementValueTypes.add(Types.BIGINT);
                preparedStatementValues.add(searchCriteria.getEndYear());
                preparedStatementValueTypes.add(Types.BIGINT);
                firstCriteria = false;
            }
            if (searchCriteria.getCaseNumber() != null) {
                addWhereClause(query, firstCriteria);
                query.append("cases.cmpnumber LIKE 'CMP/%/").append(searchCriteria.getCaseNumber()).append("'");
                preparedStatementValues.add(searchCriteria.getCaseNumber());
                preparedStatementValueTypes.add(Types.INTEGER);
                firstCriteria = false;
            }
        }
        else if (Objects.equals(searchCriteria.getCaseType(), OpenApiCaseSummary.CaseTypeEnum.ST.name())) {
            if (searchCriteria.getYear() != null) {
                addWhereClause(query, firstCriteria);
                query.append("cases.courtcasenumber LIKE 'ST/%/").append(searchCriteria.getYear()).append("'");
                firstCriteria = false;
            }
            if (searchCriteria.getCaseNumber() != null) {
                addWhereClause(query, firstCriteria);
                query.append("cases.courtcasenumber LIKE 'ST/").append(searchCriteria.getCaseNumber()).append("/").append(searchCriteria.getYear()).append("'");
                firstCriteria = false;
            }
        }

        return query.toString();
    }

    private void addWhereClause(StringBuilder query, boolean firstCriteria) {
        if (firstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }

    public String getCaseSummarySearchQuery(String baseQuery) {

        StringBuilder query = new StringBuilder("WITH unique_cases AS ( ").append(baseQuery)
                .append(" ) ").append(CASE_SUMMARY_QUERY)
                .append(FROM_UNIQUE_CASE_TABLE).append(LEFT_JOIN_QUERY);

        return query.toString();

    }

    public String addOrderByQuery(String caseSummaryQuery, @Valid Pagination pagination) {

        if (isEmptyPagination(pagination) || pagination.getSortBy().contains(";")) {
            return caseSummaryQuery + DEFAULT_ORDERBY_CLAUSE;
        } else {
            caseSummaryQuery = caseSummaryQuery + ORDERBY_CLAUSE;
        }
        return caseSummaryQuery.replace("{orderBy}", pagination.getSortBy()).replace("{sortingOrder}", pagination.getOrder().name());
    }

    private boolean isEmptyPagination(Pagination pagination) {
        return pagination == null || pagination.getSortBy() == null || pagination.getOrder() == null;
    }

    public String addPaginationQuery(String caseSummaryQuery, List<Object> preparedStmtList, @Valid Pagination pagination, List<Integer> preparedStmtArgList) {

        preparedStmtList.add(pagination.getLimit());
        preparedStmtArgList.add(Types.INTEGER);

        preparedStmtList.add(pagination.getOffSet());
        preparedStmtArgList.add(Types.INTEGER);
        return caseSummaryQuery + " LIMIT ? OFFSET ?";
    }

}
