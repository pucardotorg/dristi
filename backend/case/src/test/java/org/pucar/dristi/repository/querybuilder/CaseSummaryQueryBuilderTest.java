package org.pucar.dristi.repository.querybuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.CaseSearchCriteria;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.Pagination;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CaseSummaryQueryBuilderTest {

    @InjectMocks
    private CaseSummaryQueryBuilder caseSummaryQueryBuilder;

    private List<Object> preparedStmtList;
    private List<Integer> preparedStmtArgList;

    @BeforeEach
    public void setup() {
        preparedStmtList = new ArrayList<>();
        preparedStmtArgList = new ArrayList<>();
    }

    @Test
    public void testGetCaseSummarySearchQuery_withCaseId() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setCaseId(List.of("123"));

        String query = caseSummaryQueryBuilder.getCaseSummarySearchQuery("cases");

        assertEquals("WITH unique_cases AS ( cases ) SELECT uc.id as id, uc.tenantid as tenantid, uc.casenumber as casenumber, uc.casetitle as casetitle,        uc.filingnumber as filingnumber, uc.cnrNumber as cnrNumber, uc.outcome as outcome,        uc.cmpnumber, uc.courtid as courtid, uc.benchid as benchid, uc.casetype,        uc.judgeid as judgeid, uc.stage as stage, uc.substage as substage, uc.filingdate as filingdate,        uc.registrationdate as registrationdate, uc.status as status, uc.isactive as isactive,        uc.casecategory as casecategory, uc.createdby as createdby, uc.lastmodifiedby as lastmodifiedby,        uc.createdtime as createdtime, uc.lastmodifiedtime as lastmodifiedtime,       ltg.id as litigant_id, ltg.tenantid as litigant_tenantid, ltg.partycategory as litigant_partycategory,        ltg.case_id as litigant_case_id, ltg.individualid as litigant_individualid, ltg.organisationid as litigant_organisationid,        ltg.partytype as litigant_partytype, ltg.isactive as litigant_isactive, ltg.additionaldetails as litigant_additionaldetails,        ltg.createdby as litigant_createdby, ltg.lastmodifiedby as litigant_lastmodifiedby,        ltg.createdtime as litigant_createdtime, ltg.lastmodifiedtime as litigant_lastmodifiedtime,       stse.id as statute_section_id, stse.tenantid as statute_section_tenantid, stse.statutes as statute_section_statutes,        stse.case_id as statute_section_case_id, stse.sections as statute_section_sections,        stse.subsections as statute_section_subsections, stse.additionaldetails as statute_section_additionaldetails,        stse.createdby as statute_section_createdby, stse.lastmodifiedby as statute_section_lastmodifiedby,        stse.createdtime as statute_section_createdtime, stse.lastmodifiedtime as statute_section_lastmodifiedtime,       rep.id as representative_id, rep.tenantid as representative_tenantid, rep.advocateid as representative_advocateid,        rep.case_id as representative_case_id, rep.isactive as representative_isactive,        rep.additionaldetails as representative_additionaldetails, rep.createdby as representative_createdby,        rep.lastmodifiedby as representative_lastmodifiedby, rep.createdtime as representative_createdtime,        rep.lastmodifiedtime as representative_lastmodifiedtime,       rpst.id as representing_id, rpst.tenantid as representing_tenantid, rpst.partycategory as representing_partycategory,        rpst.representative_id as representing_representative_id, rpst.individualid as representing_individualid,        rpst.case_id as representing_case_id, rpst.organisationid as representing_organisationid,        rpst.partytype as representing_partytype, rpst.isactive as representing_isactive,        rpst.additionaldetails as representing_additionaldetails, rpst.createdby as representing_createdby,        rpst.lastmodifiedby as representing_lastmodifiedby, rpst.createdtime as representing_createdtime,        rpst.lastmodifiedtime as representing_lastmodifiedtime FROM unique_cases uc  LEFT JOIN   dristi_case_litigants ltg ON uc.id = ltg.case_id  LEFT JOIN   dristi_case_statutes_and_sections stse ON uc.id = stse.case_id  LEFT JOIN (   dristi_case_representatives rep LEFT JOIN   dristi_case_representing rpst ON rep.id = rpst.representative_id  ) ON uc.id = rep.case_id ", query);
        assertEquals(0, preparedStmtList.size());
        assertEquals(0, preparedStmtArgList.size());
    }

    @Test
    public void testGetCaseSummarySearchQuery_withFilingNumber() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setFilingNumber(List.of("FN123"));

        String query = caseSummaryQueryBuilder.getCaseSummarySearchQuery("cases");

        assertTrue(query.contains("cases"));
    }

    @Test
    public void testGetCaseSummarySearchQuery_withCnrNumber() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setCnrNumber(List.of("CNR123"));

        String query = caseSummaryQueryBuilder.getCaseSummarySearchQuery("cases");

        assertTrue(query.contains("WITH unique_cases AS ( "));
        assertEquals(0, preparedStmtList.size());
        assertEquals(0, preparedStmtArgList.size());
    }

    @Test
    public void testGetCaseSummarySearchQuery_withMultipleCriteria() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setCaseId(List.of("123"));
        criteria.setFilingNumber(List.of("FN123"));
        criteria.setCnrNumber(List.of("CNR123"));

        String query = caseSummaryQueryBuilder.getCaseSummarySearchQuery("cases");

        assertNotNull(query);
        assertTrue(query.contains("cases"));
    }

    @Test
    public void testAddOrderByQuery_withValidPagination() {
        String baseQuery = "SELECT * FROM cases";
        Pagination pagination = new Pagination();
        pagination.setSortBy("casenumber");

        String result = caseSummaryQueryBuilder.addOrderByQuery(baseQuery, pagination);

        assertTrue(result.contains("SELECT * FROM cases ORDER BY cases.createdtime DESC"));
    }

    @Test
    public void testAddOrderByQuery_withEmptyPagination() {
        String baseQuery = "SELECT * FROM cases";
        Pagination pagination = new Pagination();
        pagination.setSortBy(null);

        String result = caseSummaryQueryBuilder.addOrderByQuery(baseQuery, pagination);

        assertTrue(result.contains("ORDER BY cases.createdtime DESC"));
    }

    @Test
    public void testAddPaginationQuery_withValidPagination() {
        String baseQuery = "SELECT * FROM cases";
        Pagination pagination = new Pagination();
        pagination.setLimit(10);
        pagination.setOffSet(0);

        String result = caseSummaryQueryBuilder.addPaginationQuery(baseQuery, preparedStmtList, pagination, preparedStmtArgList);

        assertTrue(result.contains("LIMIT ? OFFSET ?"));
        assertEquals(2, preparedStmtList.size());
        assertEquals(Types.INTEGER, preparedStmtArgList.get(0));
        assertEquals(Types.INTEGER, preparedStmtArgList.get(1));
    }

    @Test
    public void testCreateQuery() {
        List<String> ids = Arrays.asList("1", "2", "3");

        String result = caseSummaryQueryBuilder.createQuery(ids);

        assertEquals(" ?, ?, ?", result, "The query should contain placeholders for each id.");
    }

    @Test
    public void testAddToPreparedStatement() {
        List<Object> stmtList = new ArrayList<>();
        List<String> ids = Arrays.asList("1", "2", "3");

        caseSummaryQueryBuilder.addToPreparedStatement(stmtList, ids);

        assertEquals(3, stmtList.size());
        assertEquals("1", stmtList.get(0));
        assertEquals("2", stmtList.get(1));
        assertEquals("3", stmtList.get(2));
    }

    @Test
    public void testAddToPreparedStatementArgs() {
        List<Integer> stmtArgList = new ArrayList<>();
        List<String> ids = Arrays.asList("1", "2", "3");

        caseSummaryQueryBuilder.addToPreparedStatementArgs(stmtArgList, Types.VARCHAR, ids);

        assertEquals(3, stmtArgList.size());
        assertEquals(Types.VARCHAR, stmtArgList.get(0));
        assertEquals(Types.VARCHAR, stmtArgList.get(1));
        assertEquals(Types.VARCHAR, stmtArgList.get(2));
    }

    @Test
    void testAddOrderByQuery_withInvalidSortBy() {
        String baseQuery = "SELECT * FROM cases";
        Pagination pagination = new Pagination();
        pagination.setSortBy("DROP TABLE cases");
        pagination.setOrder(Order.ASC);

        String result = caseSummaryQueryBuilder.addOrderByQuery(baseQuery, pagination);

        assertTrue(result.contains("SELECT * FROM cases ORDER BY cases.DROP TABLE cases ASC"));
    }

    @Test
    public void testAddClauseIfRequired_withEmptyPreparedStmtList() {
        StringBuilder query = new StringBuilder("SELECT * FROM cases");
        List<Object> preparedStmtList = new ArrayList<>();

        caseSummaryQueryBuilder.addClauseIfRequired(query, preparedStmtList);

        assertTrue(query.toString().contains(" WHERE "));
    }

    @Test
    public void testAddClauseIfRequired_withNonEmptyPreparedStmtList() {
        StringBuilder query = new StringBuilder("SELECT * FROM cases");
        List<Object> preparedStmtList = new ArrayList<>();
        preparedStmtList.add("123");

        caseSummaryQueryBuilder.addClauseIfRequired(query, preparedStmtList);

        assertTrue(query.toString().contains(" OR "));
    }

    @Test
    public void testGetCaseBaseQuery_withCaseId() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setCaseId(List.of("123"));

        String query = caseSummaryQueryBuilder.getCaseBaseQuery(criteria, preparedStmtList, preparedStmtArgList);

        assertTrue(query.contains("SELECT cases.id, cases.tenantid, cases.casenumber"));
        assertTrue(query.contains("cases.id IN (  ? )"));
        assertEquals(1, preparedStmtList.size());
        assertEquals("123", preparedStmtList.get(0));
        assertEquals(Types.VARCHAR, preparedStmtArgList.get(0));
    }

    @Test
    public void testGetCaseBaseQuery_withFilingNumber() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setFilingNumber(List.of("FN123"));

        String query = caseSummaryQueryBuilder.getCaseBaseQuery(criteria, preparedStmtList, preparedStmtArgList);

        assertTrue(query.contains("cases.filingnumber IN (  ? )"));
        assertEquals(1, preparedStmtList.size());
        assertEquals("FN123", preparedStmtList.get(0));
        assertEquals(Types.VARCHAR, preparedStmtArgList.get(0));
    }

    @Test
    public void testGetCaseBaseQuery_withCnrNumber() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setCnrNumber(List.of("CNR123"));

        String query = caseSummaryQueryBuilder.getCaseBaseQuery(criteria, preparedStmtList, preparedStmtArgList);

        assertTrue(query.contains("cases.cnrNumber IN (  ? )"));
        assertEquals(1, preparedStmtList.size());
        assertEquals("CNR123", preparedStmtList.get(0));
        assertEquals(Types.VARCHAR, preparedStmtArgList.get(0));
    }

    @Test
    public void testGetCaseBaseQuery_withMultipleCriteria() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setCaseId(List.of("123"));
        criteria.setFilingNumber(List.of("FN123"));
        criteria.setCnrNumber(List.of("CNR123"));

        String query = caseSummaryQueryBuilder.getCaseBaseQuery(criteria, preparedStmtList, preparedStmtArgList);

        assertTrue(query.contains("cases.id IN (  ? )"));
        assertTrue(query.contains("cases.filingnumber IN (  ? )"));
        assertTrue(query.contains("cases.cnrNumber IN (  ? )"));
        assertEquals(3, preparedStmtList.size());
        assertEquals("123", preparedStmtList.get(0));
        assertEquals("FN123", preparedStmtList.get(1));
        assertEquals("CNR123", preparedStmtList.get(2));
        assertEquals(3, preparedStmtArgList.size());
        assertEquals(Types.VARCHAR, preparedStmtArgList.get(0));
        assertEquals(Types.VARCHAR, preparedStmtArgList.get(1));
        assertEquals(Types.VARCHAR, preparedStmtArgList.get(2));
    }

}
