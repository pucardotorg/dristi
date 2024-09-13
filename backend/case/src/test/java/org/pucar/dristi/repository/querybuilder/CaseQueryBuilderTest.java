package org.pucar.dristi.repository.querybuilder;

import java.util.ArrayList;
import java.util.List;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.CaseExists;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.Pagination;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CaseQueryBuilderTest {

    private CaseQueryBuilder queryBuilder;

    @Mock
    private Logger mockedLogger;

    @BeforeEach
    void setUp() {
        queryBuilder = new CaseQueryBuilder();
    }

    @Test
    void checkCaseExistQuery_ShouldGenerateCorrectQuery_WhenAllFieldsAreNotNull() {
        // Arrange
        String caseId = "111";
        String courtCaseNumber = "123";
        String cnrNumber = "456";
        String filingNumber = "789";
        CaseExists caseExists = new CaseExists();
        caseExists.setCaseId(caseId);
        caseExists.setCourtCaseNumber(courtCaseNumber);
        caseExists.setCnrNumber(cnrNumber);
        caseExists.setFilingNumber(filingNumber);
        // Act
        String query = queryBuilder.checkCaseExistQuery(caseExists, new ArrayList<>(), new ArrayList<>());

        // Assert
        String expectedQuery = " SELECT COUNT(*) FROM dristi_cases cases  WHERE cases.id = ? AND cases.cnrNumber = ? AND cases.filingnumber = ? AND cases.courtcasenumber = ?;";
        assertEquals(expectedQuery, query);
    }

    @Test
    void checkCaseExistQuery_ShouldGenerateCorrectQuery_WhenAllFieldsAreNotNull2() {
        // Arrange
        String courtCaseNumber = "123";
        String cnrNumber = "456";
        String filingNumber = null;
        String caseId = null;
        CaseExists caseExists = new CaseExists();
        caseExists.setCaseId(caseId);
        caseExists.setCourtCaseNumber(courtCaseNumber);
        caseExists.setCnrNumber(cnrNumber);
        caseExists.setFilingNumber(filingNumber);
        // Act
        String query = queryBuilder.checkCaseExistQuery(caseExists, new ArrayList<>(), new ArrayList<>());

        // Assert
        String expectedQuery =" SELECT COUNT(*) FROM dristi_cases cases  WHERE cases.cnrNumber = ? AND cases.courtcasenumber = ?;";
        assertEquals(expectedQuery, query);
    }

    @Test
    void checkCaseExistQuery_ShouldGenerateCorrectQuery_WhenAllFieldsAreNotNull3() {
        // Arrange
        String courtCaseNumber = "123";
        String cnrNumber = null;
        String filingNumber = "789";
        String caseId = null;
        CaseExists caseExists = new CaseExists();
        caseExists.setCaseId(caseId);
        caseExists.setCourtCaseNumber(courtCaseNumber);
        caseExists.setCnrNumber(cnrNumber);
        caseExists.setFilingNumber(filingNumber);
        // Act
        String query = queryBuilder.checkCaseExistQuery(caseExists, new ArrayList<>(), new ArrayList<>());

        // Assert
        String expectedQuery = " SELECT COUNT(*) FROM dristi_cases cases  WHERE cases.filingnumber = ? AND cases.courtcasenumber = ?;";
        assertEquals(expectedQuery, query);
    }

    @Test
    void checkCaseExistQuery_ShouldGenerateCorrectQuery_WhenAllFieldsAreNotNull4() {
        // Arrange
        String courtCaseNumber = null;
        String cnrNumber = "456";
        String filingNumber = "789";
        String caseId = null;
        CaseExists caseExists = new CaseExists();
        caseExists.setCaseId(caseId);
        caseExists.setCourtCaseNumber(courtCaseNumber);
        caseExists.setCnrNumber(cnrNumber);
        caseExists.setFilingNumber(filingNumber);
        // Act
        String query = queryBuilder.checkCaseExistQuery(caseExists, new ArrayList<>(), new ArrayList<>());


        // Assert
        String expectedQuery = " SELECT COUNT(*) FROM dristi_cases cases  WHERE cases.cnrNumber = ? AND cases.filingnumber = ?;";
        assertEquals(expectedQuery, query);
    }

    @Test
    void checkCaseExistQuery_ShouldGenerateCorrectQuery_WhenAllFieldsAreNotNull5() {
        // Arrange
        String courtCaseNumber = "123";
        CaseExists caseExists = new CaseExists();
        caseExists.setCourtCaseNumber(courtCaseNumber);
        // Act
        String query = queryBuilder.checkCaseExistQuery(caseExists, new ArrayList<>(), new ArrayList<>());


        // Assert
        String expectedQuery = " SELECT COUNT(*) FROM dristi_cases cases  WHERE cases.courtcasenumber = ?;";
        assertEquals(expectedQuery, query);
    }

    @Test
    void checkCaseExistQuery_ShouldGenerateCorrectQuery_WhenAllFieldsAreNotNull6() {
        // Arrange
        String cnrNumber = "456";
        CaseExists caseExists = new CaseExists();
        caseExists.setCnrNumber(cnrNumber);
        // Act
        String query = queryBuilder.checkCaseExistQuery(caseExists, new ArrayList<>(), new ArrayList<>());

        // Assert
        String expectedQuery = " SELECT COUNT(*) FROM dristi_cases cases  WHERE cases.cnrNumber = ?;";
        assertEquals(expectedQuery, query);
    }

    @Test
    void checkCaseExistQuery_ShouldGenerateCorrectQuery_WhenAllFieldsAreNotNull7() {
        // Arrange
        String filingNumber = "123";

        CaseExists caseExists = new CaseExists();
        caseExists.setFilingNumber(filingNumber);
        // Act
        String query = queryBuilder.checkCaseExistQuery(caseExists, new ArrayList<>(), new ArrayList<>());


        // Assert
        String expectedQuery = " SELECT COUNT(*) FROM dristi_cases cases  WHERE cases.filingnumber = ?;";
        assertEquals(expectedQuery, query);
    }

    @Test
    void testGetCasesSearchQuery_SingleCriteria() {
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId("12345");
        criteria.setCnrNumber("123");
        criteria.setFilingNumber("9876");
        criteria.setCourtCaseNumber("456");

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String expectedQuery = " SELECT cases.id as id, cases.tenantid as tenantid, cases.casenumber as casenumber, cases.resolutionmechanism as resolutionmechanism, cases.casetitle as casetitle, cases.casedescription as casedescription, cases.filingnumber as filingnumber, cases.casenumber as casenumber, cases.accesscode as accesscode, cases.courtcasenumber as courtcasenumber, cases.cnrNumber as cnrNumber,  cases.outcome as outcome, cases.courtid as courtid, cases.benchid as benchid, cases.judgeid as judgeid, cases.stage as stage, cases.substage as substage, cases.filingdate as filingdate, cases.judgementdate as judgementdate, cases.registrationdate as registrationdate, cases.natureofpleading as natureofpleading, cases.status as status, cases.remarks as remarks, cases.isactive as isactive, cases.casedetails as casedetails, cases.additionaldetails as additionaldetails, cases.casecategory as casecategory, cases.createdby as createdby, cases.lastmodifiedby as lastmodifiedby, cases.createdtime as createdtime, cases.lastmodifiedtime as lastmodifiedtime  FROM dristi_cases cases WHERE cases.id = ? AND cases.cnrNumber = ? AND LOWER(cases.filingnumber) LIKE LOWER(?) AND cases.courtcasenumber = ?";
        String actualQuery = queryBuilder.getCasesSearchQuery(criteria, preparedStmtList, preparedStmtArgList,null);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(4, preparedStmtList.size());
        assertEquals("12345", preparedStmtList.get(0));
    }

    @Test()
    public void testGetCasesSearchQuery_Exception() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        ids.add("1");
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId("12345");
        criteria.setCnrNumber("123");
        criteria.setFilingNumber("9876");
        criteria.setCourtCaseNumber("456");
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Assert
        assertThrows(Exception.class, () -> queryBuilder.getCasesSearchQuery(criteria, preparedStmtList, preparedStmtArgList,null));
    }

    @Test()
    public void testGetCasesSearchQuery_CustomException() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        ids.add("1");
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId("12345");
        criteria.setCnrNumber("123");
        criteria.setFilingNumber("9876");
        criteria.setCourtCaseNumber("456");
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getCasesSearchQuery(criteria, preparedStmtList, preparedStmtArgList,null));
    }

    @Test
    void testGetCasesSearchQuery_SingleCriteria2() {
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId(null);
        criteria.setCnrNumber("123");
        criteria.setFilingNumber("9876");
        criteria.setCourtCaseNumber("456");

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String expectedQuery = " SELECT cases.id as id, cases.tenantid as tenantid, cases.casenumber as casenumber, cases.resolutionmechanism as resolutionmechanism, cases.casetitle as casetitle, cases.casedescription as casedescription, cases.filingnumber as filingnumber, cases.casenumber as casenumber, cases.accesscode as accesscode, cases.courtcasenumber as courtcasenumber, cases.cnrNumber as cnrNumber,  cases.outcome as outcome, cases.courtid as courtid, cases.benchid as benchid, cases.judgeid as judgeid, cases.stage as stage, cases.substage as substage, cases.filingdate as filingdate, cases.judgementdate as judgementdate, cases.registrationdate as registrationdate, cases.natureofpleading as natureofpleading, cases.status as status, cases.remarks as remarks, cases.isactive as isactive, cases.casedetails as casedetails, cases.additionaldetails as additionaldetails, cases.casecategory as casecategory, cases.createdby as createdby, cases.lastmodifiedby as lastmodifiedby, cases.createdtime as createdtime, cases.lastmodifiedtime as lastmodifiedtime  FROM dristi_cases cases WHERE cases.cnrNumber = ? AND LOWER(cases.filingnumber) LIKE LOWER(?) AND cases.courtcasenumber = ?";
        String actualQuery = queryBuilder.getCasesSearchQuery(criteria, preparedStmtList, preparedStmtArgList,null);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(3, preparedStmtList.size());
        assertEquals("123", preparedStmtList.get(0));
    }

    @Test()
    public void testGetCasesSearchQuery2_Exception() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        ids.add("1");
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId("12345");
        criteria.setCnrNumber("123");
        criteria.setFilingNumber("9876");
        criteria.setCourtCaseNumber("456");
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getCasesSearchQuery(criteria, preparedStmtList, preparedStmtArgList,null));
    }

    @Test
    void testGetCasesSearchQuery_SingleCriteria3() {
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId(null);
        criteria.setCnrNumber(null);
        criteria.setFilingNumber("9876");
        criteria.setCourtCaseNumber("456");

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String expectedQuery = " SELECT cases.id as id, cases.tenantid as tenantid, cases.casenumber as casenumber, cases.resolutionmechanism as resolutionmechanism, cases.casetitle as casetitle, cases.casedescription as casedescription, cases.filingnumber as filingnumber, cases.casenumber as casenumber, cases.accesscode as accesscode, cases.courtcasenumber as courtcasenumber, cases.cnrNumber as cnrNumber,  cases.outcome as outcome, cases.courtid as courtid, cases.benchid as benchid, cases.judgeid as judgeid, cases.stage as stage, cases.substage as substage, cases.filingdate as filingdate, cases.judgementdate as judgementdate, cases.registrationdate as registrationdate, cases.natureofpleading as natureofpleading, cases.status as status, cases.remarks as remarks, cases.isactive as isactive, cases.casedetails as casedetails, cases.additionaldetails as additionaldetails, cases.casecategory as casecategory, cases.createdby as createdby, cases.lastmodifiedby as lastmodifiedby, cases.createdtime as createdtime, cases.lastmodifiedtime as lastmodifiedtime  FROM dristi_cases cases WHERE LOWER(cases.filingnumber) LIKE LOWER(?) AND cases.courtcasenumber = ?";
        String actualQuery = queryBuilder.getCasesSearchQuery(criteria, preparedStmtList, preparedStmtArgList,null);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(2, preparedStmtList.size());
        assertEquals("%9876%", preparedStmtList.get(0));
    }

    @Test()
    public void testGetCasesSearchQuery3_Exception() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        ids.add("1");
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId("12345");
        criteria.setCnrNumber("123");
        criteria.setFilingNumber("9876");
        criteria.setCourtCaseNumber("456");
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getCasesSearchQuery(criteria, preparedStmtList, preparedStmtArgList,null));
    }

    @Test
    void testGetCasesSearchQuery_SingleCriteria4() {
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId(null);
        criteria.setCnrNumber(null);
        criteria.setFilingNumber(null);
        criteria.setCourtCaseNumber("456");

        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String expectedQuery = " SELECT cases.id as id, cases.tenantid as tenantid, cases.casenumber as casenumber, cases.resolutionmechanism as resolutionmechanism, cases.casetitle as casetitle, cases.casedescription as casedescription, cases.filingnumber as filingnumber, cases.casenumber as casenumber, cases.accesscode as accesscode, cases.courtcasenumber as courtcasenumber, cases.cnrNumber as cnrNumber,  cases.outcome as outcome, cases.courtid as courtid, cases.benchid as benchid, cases.judgeid as judgeid, cases.stage as stage, cases.substage as substage, cases.filingdate as filingdate, cases.judgementdate as judgementdate, cases.registrationdate as registrationdate, cases.natureofpleading as natureofpleading, cases.status as status, cases.remarks as remarks, cases.isactive as isactive, cases.casedetails as casedetails, cases.additionaldetails as additionaldetails, cases.casecategory as casecategory, cases.createdby as createdby, cases.lastmodifiedby as lastmodifiedby, cases.createdtime as createdtime, cases.lastmodifiedtime as lastmodifiedtime  FROM dristi_cases cases WHERE cases.courtcasenumber = ?";
        String actualQuery = queryBuilder.getCasesSearchQuery(criteria, preparedStmtList, preparedStmtArgList,null);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(1, preparedStmtList.size());
        assertEquals("456", preparedStmtList.get(0));
    }

    @Test()
    public void testGetCasesSearchQuery4_Exception() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        ids.add("1");
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId(null);
        criteria.setCnrNumber(null);
        criteria.setFilingNumber(null);
        criteria.setCourtCaseNumber("456");
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getCasesSearchQuery(criteria, preparedStmtList, preparedStmtArgList, null));
    }

    @Test
    void testGetCasesSearchQuery_SingleCriteria5() {
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId(null);
        criteria.setCnrNumber(null);
        criteria.setFilingNumber(null);
        criteria.setCourtCaseNumber("456");
        criteria.setFilingFromDate(1223235235l);
        criteria.setFilingToDate(1223235236l);
        criteria.setRegistrationFromDate(1223235238l);
        criteria.setRegistrationToDate(1223235239l);
        List<Integer> preparedStmtArgList = new ArrayList<>();

        List<Object> preparedStmtList = new ArrayList<>();

        String actualQuery = queryBuilder.getCasesSearchQuery(criteria, preparedStmtList, preparedStmtArgList, null);

        assertNotNull(actualQuery);
        assertEquals(5, preparedStmtList.size());
        assertEquals("456", preparedStmtList.get(0));
    }

    @Test()
    public void testGetCasesSearchQuery5_Exception() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        List<Integer> preparedStmtArgList = new ArrayList<>();

        ids.add("1");
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId(null);
        criteria.setCnrNumber(null);
        criteria.setFilingNumber(null);
        criteria.setCourtCaseNumber("456");

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getCasesSearchQuery(criteria, preparedStmtList, preparedStmtArgList, null));
    }

    @Test
    void getDocumentSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Act
        String query = queryBuilder.getDocumentSearchQuery(ids, preparedStmtList, preparedStmtArgList);

        // Assert
        String expectedQuery = "Select doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, " +
                "doc.additionaldetails as docadditionaldetails, doc.case_id as case_id, doc.linked_case_id as linked_case_id, doc.litigant_id as litigant_id, " +
                "doc.representative_id as representative_id, doc.representing_id as representing_id  FROM dristi_case_document doc " +
                "WHERE doc.case_id IN (?,?,?)";

        assertEquals(expectedQuery, query);
        assertEquals(3, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
        assertEquals("2", preparedStmtList.get(1));
        assertEquals("3", preparedStmtList.get(2));
    }

    @Test()
    public void testetDocumentSearchQuery_ShouldGenerateCorrectQuery_Exception() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        List<Integer> preparedStmtArgList = null;
        ids.add("1");

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getDocumentSearchQuery(ids, preparedStmtList, preparedStmtArgList));
    }

    @Test
    void getLinkedCaseSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Act
        String query = queryBuilder.getLinkedCaseSearchQuery(ids, preparedStmtList, preparedStmtArgList);

        // Assert
        String expectedQuery = " SELECT lics.id as id, lics.casenumbers as casenumbers, lics.case_id as case_id,lics.relationshiptype as relationshiptype, " +
                "lics.isactive as isactive, lics.additionaldetails as additionaldetails, lics.createdby as createdby, lics.lastmodifiedby as lastmodifiedby, " +
                "lics.createdtime as createdtime, lics.lastmodifiedtime as lastmodifiedtime  FROM dristi_linked_case lics WHERE lics.case_id IN (?,?,?)";
        assertEquals(expectedQuery, query);
        assertEquals(3, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
        assertEquals("2", preparedStmtList.get(1));
        assertEquals("3", preparedStmtList.get(2));
    }

    @Test()
    public void testGetLinkedCaseSearchQuery_ShouldGenerateCorrectQuery_Exception() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        List<Integer> preparedStmtArgList = new ArrayList<>();

        ids.add("1");

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getLinkedCaseSearchQuery(ids, preparedStmtList, preparedStmtArgList));
    }

    @Test
    void getLitigantSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Act
        String query = queryBuilder.getLitigantSearchQuery(ids, preparedStmtList,preparedStmtArgList);

        // Assert
        String expectedQuery = " SELECT ltg.id as id, ltg.tenantid as tenantid, ltg.partycategory as partycategory, ltg.case_id as case_id, ltg.individualid as individualid,  ltg.organisationid as organisationid, ltg.partytype as partytype, ltg.isactive as isactive, ltg.additionaldetails as additionaldetails, ltg.createdby as createdby, ltg.lastmodifiedby as lastmodifiedby, ltg.createdtime as createdtime, ltg.lastmodifiedtime as lastmodifiedtime  FROM dristi_case_litigants ltg WHERE ltg.case_id IN (?,?,?) AND ltg.isactive = true";
        assertEquals(expectedQuery, query);
        assertEquals(3, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
        assertEquals("2", preparedStmtList.get(1));
        assertEquals("3", preparedStmtList.get(2));
    }

    @Test()
    public void testGetLitigantSearchQuery_ShouldGenerateCorrectQuery_Exception() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        ids.add("1");
        List<Integer> preparedStmtArgList = new ArrayList<>();


        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getLitigantSearchQuery(ids, preparedStmtList,preparedStmtArgList));
    }

    @Test
    void getStatuteSectionSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Act
        String query = queryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtList,preparedStmtArgList);

        // Assert
        String expectedQuery = " SELECT stse.id as id, stse.tenantid as tenantid, stse.statutes as statutes, stse.case_id as case_id, stse.sections as sections, stse.subsections as subsections, stse.additionaldetails as additionaldetails, stse.createdby as createdby, stse.lastmodifiedby as lastmodifiedby, stse.createdtime as createdtime, stse.lastmodifiedtime as lastmodifiedtime  FROM dristi_case_statutes_and_sections stse WHERE stse.case_id IN (?,?,?)";
        assertEquals(expectedQuery, query);
        assertEquals(3, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
        assertEquals("2", preparedStmtList.get(1));
        assertEquals("3", preparedStmtList.get(2));
    }

    @Test()
    public void testGetStatuteSectionSearchQuery_Exception() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        ids.add("1");
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtList,preparedStmtArgList));
    }

    @Test
    void getRepresentativesSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Act
        String query = queryBuilder.getRepresentativesSearchQuery(ids, preparedStmtList,preparedStmtArgList);

        // Assert
        String expectedQuery = " SELECT rep.id as id, rep.tenantid as tenantid, rep.advocateid as advocateid, rep.case_id as case_id,  rep.isactive as isactive, rep.additionaldetails as additionaldetails, rep.createdby as createdby, rep.lastmodifiedby as lastmodifiedby, rep.createdtime as createdtime, rep.lastmodifiedtime as lastmodifiedtime  FROM dristi_case_representatives rep WHERE rep.case_id IN (?,?,?) AND rep.isactive = true";
        assertEquals(expectedQuery, query);
        assertEquals(3, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
        assertEquals("2", preparedStmtList.get(1));
        assertEquals("3", preparedStmtList.get(2));
    }

    @Test()
    public void testGetRepresentativesSearchQuery_Exception() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        ids.add("1");
        List<Integer> preparedStmtArgList = new ArrayList<>();


        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getRepresentativesSearchQuery(ids, preparedStmtList,preparedStmtArgList));
    }

    @Test
    void getRepresentingSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Act
        String query = queryBuilder.getRepresentingSearchQuery(ids, preparedStmtList,preparedStmtArgList);

        // Assert
        String expectedQuery = " SELECT rpst.id as id, rpst.tenantid as tenantid, rpst.partycategory as partycategory, rpst.representative_id as representative_id, rpst.individualid as individualid, rpst.case_id as case_id,  rpst.organisationid as organisationid, rpst.partytype as partytype, rpst.isactive as isactive, rpst.additionaldetails as additionaldetails, rpst.createdby as createdby, rpst.lastmodifiedby as lastmodifiedby, rpst.createdtime as createdtime, rpst.lastmodifiedtime as lastmodifiedtime  FROM dristi_case_representing rpst WHERE rpst.representative_id IN (?,?,?) AND rpst.isactive = true";
        assertEquals(expectedQuery, query);
        assertEquals(3, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
        assertEquals("2", preparedStmtList.get(1));
        assertEquals("3", preparedStmtList.get(2));
    }

    @Test()
    public void testGetRepresentingSearchQuery_Exception() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        ids.add("1");
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getRepresentingSearchQuery(ids, preparedStmtList,preparedStmtArgList));
    }

    @Test
    void getLinkedCaseDocumentSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Act
        String query = queryBuilder.getLinkedCaseDocumentSearchQuery(ids, preparedStmtList,preparedStmtArgList);

        // Assert
        String expectedQuery = "Select doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.case_id as case_id, doc.linked_case_id as linked_case_id, doc.litigant_id as litigant_id, doc.representative_id as representative_id, doc.representing_id as representing_id  FROM dristi_case_document doc WHERE doc.linked_case_id IN (?,?,?)";
        assertEquals(expectedQuery, query);
        assertEquals(3, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
        assertEquals("2", preparedStmtList.get(1));
        assertEquals("3", preparedStmtList.get(2));
    }

    @Test()
    public void testGetLinkedCaseDocumentSearchQuery_Exception() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        ids.add("1");
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getLinkedCaseDocumentSearchQuery(ids, preparedStmtList,preparedStmtArgList));
    }

    @Test
    void getLitigantDocumentSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Act
        String query = queryBuilder.getLitigantDocumentSearchQuery(ids, preparedStmtList,preparedStmtArgList);

        // Assert
        String expectedQuery = "Select doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.case_id as case_id, doc.linked_case_id as linked_case_id, doc.litigant_id as litigant_id, doc.representative_id as representative_id, doc.representing_id as representing_id  FROM dristi_case_document doc WHERE doc.litigant_id IN (?,?,?)";
        assertEquals(expectedQuery, query);
        assertEquals(3, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
        assertEquals("2", preparedStmtList.get(1));
        assertEquals("3", preparedStmtList.get(2));
    }

    @Test()
    public void testGetLitigantDocumentSearchQuery_Exception() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        ids.add("1");
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getLitigantDocumentSearchQuery(ids, preparedStmtList,preparedStmtArgList));
    }

    @Test
    void getRepresentativeDocumentSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Act
        String query = queryBuilder.getRepresentativeDocumentSearchQuery(ids, preparedStmtList,preparedStmtArgList);

        // Assert
        String expectedQuery = "Select doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.case_id as case_id, doc.linked_case_id as linked_case_id, doc.litigant_id as litigant_id, doc.representative_id as representative_id, doc.representing_id as representing_id  FROM dristi_case_document doc WHERE doc.representative_id IN (?,?,?)";
        assertEquals(expectedQuery, query);
        assertEquals(3, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
        assertEquals("2", preparedStmtList.get(1));
        assertEquals("3", preparedStmtList.get(2));
    }

    @Test()
    public void testGetRepresentativeDocumentSearchQuery_Exception() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        ids.add("1");
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getRepresentativeDocumentSearchQuery(ids, preparedStmtList,preparedStmtArgList));
    }

    @Test
    void getRepresentingDocumentSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Act
        String query = queryBuilder.getRepresentingDocumentSearchQuery(ids, preparedStmtList,preparedStmtArgList);

        // Assert
        String expectedQuery = "Select doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.case_id as case_id, doc.linked_case_id as linked_case_id, doc.litigant_id as litigant_id, doc.representative_id as representative_id, doc.representing_id as representing_id  FROM dristi_case_document doc WHERE doc.representing_id IN (?,?,?)";
        assertEquals(expectedQuery, query);
        assertEquals(3, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
        assertEquals("2", preparedStmtList.get(1));
        assertEquals("3", preparedStmtList.get(2));
    }

    @Test()
    public void testGetRepresentingDocumentSearchQuery_Exception() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        ids.add("1");
        List<Integer> preparedStmtArgList = new ArrayList<>();

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getRepresentingDocumentSearchQuery(ids,preparedStmtList,preparedStmtArgList));
    }


    @Test
    void getTotalCountQuery_ShouldReturnCorrectQuery_WhenBaseQueryIsNotNull() {
        String baseQuery = "SELECT * FROM dristi_cases cases WHERE cases.id = '111'";

        String query = queryBuilder.getTotalCountQuery(baseQuery);

        String expectedQuery = "SELECT COUNT(*) FROM (SELECT * FROM dristi_cases cases WHERE cases.id = '111') total_result";

        assertEquals(expectedQuery, query);
    }

    @Test
    void addPagination_Query_ShouldReturnCorrectQuery_WhenPageSizeAndPageNumberAreNotNull() {
        String query = "SELECT * FROM dristi_cases cases WHERE cases.id = '111'";
        Pagination pagination = new Pagination();
        pagination.setLimit(2d);
        pagination.setOffSet(0d);
        List<Object> prepareList = new ArrayList<>();

        String paginatedQuery = queryBuilder.addPaginationQuery(query,prepareList, pagination, new ArrayList<>());

        String expectedQuery = "SELECT * FROM dristi_cases cases WHERE cases.id = '111' LIMIT ? OFFSET ?";

        assertEquals(expectedQuery, paginatedQuery);
        assertEquals(2, prepareList.size());
        assertEquals(2d, prepareList.get(0));
        assertEquals(0d, prepareList.get(1));
    }

    @Test
    void addOrderByQuery_ShouldAppendDefaultOrderBy_WhenPaginationIsNull() {
        // Arrange
        String baseQuery = "SELECT * FROM dristi_cases";
        Pagination pagination = null;

        // Act
        String resultQuery = queryBuilder.addOrderByQuery(baseQuery, pagination);

        // Assert
        assertTrue(resultQuery.endsWith(" ORDER BY cases.createdtime DESC "));
    }

    @Test
    void addOrderByQuery_ShouldAppendDefaultOrderBy_WhenSortByIsNull() {
        // Arrange
        String baseQuery = "SELECT * FROM dristi_cases";
        Pagination pagination = new Pagination();
        pagination.setSortBy(";");
        pagination.setOrder(Order.ASC);

        // Act
        String resultQuery = queryBuilder.addOrderByQuery(baseQuery, pagination);

        // Assert
        assertTrue(resultQuery.endsWith(" ORDER BY cases.createdtime DESC "));
    }

    @Test
    void addOrderByQuery_ShouldAppendDefaultOrderBy_WhenOrderIsNull() {
        // Arrange
        String baseQuery = "SELECT * FROM dristi_cases";
        Pagination pagination = new Pagination();
        pagination.setSortBy("casenumber");
        pagination.setOrder(null);

        // Act
        String resultQuery = queryBuilder.addOrderByQuery(baseQuery, pagination);

        // Assert
        assertTrue(resultQuery.endsWith(" ORDER BY cases.createdtime DESC "));
    }

    @Test
    void addOrderByQuery_ShouldAppendCustomOrderBy_WhenPaginationIsNotNull() {
        // Arrange
        String baseQuery = "SELECT * FROM dristi_cases";
        Pagination pagination = new Pagination();
        pagination.setSortBy("casenumber");
        pagination.setOrder(Order.ASC);

        // Act
        String resultQuery = queryBuilder.addOrderByQuery(baseQuery, pagination);

        // Assert
        assertTrue(resultQuery.contains(" ORDER BY cases.casenumber ASC "));
    }
}
