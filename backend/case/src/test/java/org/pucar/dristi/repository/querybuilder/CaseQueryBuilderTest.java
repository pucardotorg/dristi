package org.pucar.dristi.repository.querybuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.CaseCriteria;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        // Act
        String query = queryBuilder.checkCaseExistQuery(caseId, courtCaseNumber, cnrNumber, filingNumber);

        // Assert
        String expectedQuery = "SELECT COUNT(*) FROM dristi_cases cases WHERE " +
                "cases.id = '111' AND " +
                "cases.courtcasenumber = '123' AND " +
                "cases.cnrnumber = '456' AND " +
                "cases.filingnumber = '789';";
        assertEquals(expectedQuery, query);
    }

    @Test
    void checkCaseExistQuery_ShouldGenerateCorrectQuery_WhenAllFieldsAreNotNull2() {
        // Arrange
        String courtCaseNumber = "123";
        String cnrNumber = "456";
        String filingNumber = null;
        String caseId = null;

        // Act
        String query = queryBuilder.checkCaseExistQuery(caseId, courtCaseNumber, cnrNumber, filingNumber);

        // Assert
        String expectedQuery = "SELECT COUNT(*) FROM dristi_cases cases WHERE " +
                "cases.courtcasenumber = '123' AND " +
                "cases.cnrnumber = '456';";
        assertEquals(expectedQuery, query);
    }

    @Test
    void checkCaseExistQuery_ShouldGenerateCorrectQuery_WhenAllFieldsAreNotNull3() {
        // Arrange
        String courtCaseNumber = "123";
        String cnrNumber = null;
        String filingNumber = "789";
        String caseId = null;

        // Act
        String query = queryBuilder.checkCaseExistQuery(caseId, courtCaseNumber, cnrNumber, filingNumber);

        // Assert
        String expectedQuery = "SELECT COUNT(*) FROM dristi_cases cases " +
                "WHERE cases.courtcasenumber = '123' AND cases.filingnumber = '789';";
        assertEquals(expectedQuery, query);
    }

    @Test
    void checkCaseExistQuery_ShouldGenerateCorrectQuery_WhenAllFieldsAreNotNull4() {
        // Arrange
        String courtCaseNumber = null;
        String cnrNumber = "456";
        String filingNumber = "789";
        String caseId = null;

        // Act
        String query = queryBuilder.checkCaseExistQuery(caseId, courtCaseNumber, cnrNumber, filingNumber);


        // Assert
        String expectedQuery = "SELECT COUNT(*) FROM dristi_cases cases WHERE cases.cnrnumber = '456' AND cases.filingnumber = '789';";
        assertEquals(expectedQuery, query);
    }

    @Test
    void checkCaseExistQuery_ShouldGenerateCorrectQuery_WhenAllFieldsAreNotNull5() {
        // Arrange
        String courtCaseNumber = "123";
        String cnrNumber = null;
        String filingNumber = null;
        String caseId = null;

        // Act
        String query = queryBuilder.checkCaseExistQuery(caseId, courtCaseNumber, cnrNumber, filingNumber);


        // Assert
        String expectedQuery = "SELECT COUNT(*) FROM dristi_cases cases WHERE cases.courtcasenumber = '123';";
        assertEquals(expectedQuery, query);
    }

    @Test
    void checkCaseExistQuery_ShouldGenerateCorrectQuery_WhenAllFieldsAreNotNull6() {
        // Arrange
        String courtCaseNumber = null;
        String cnrNumber = "456";
        String filingNumber = null;
        String caseId = null;

        // Act
        String query = queryBuilder.checkCaseExistQuery(caseId, courtCaseNumber, cnrNumber, filingNumber);

        // Assert
        String expectedQuery = "SELECT COUNT(*) FROM dristi_cases cases WHERE cases.cnrnumber = '456';";
        assertEquals(expectedQuery, query);
    }

    @Test
    void checkCaseExistQuery_ShouldGenerateCorrectQuery_WhenAllFieldsAreNotNull7() {
        // Arrange
        String courtCaseNumber = null;
        String cnrNumber = null;
        String filingNumber = "123";
        String caseId = null;

        // Act
        String query = queryBuilder.checkCaseExistQuery(caseId, courtCaseNumber, cnrNumber, filingNumber);


        // Assert
        String expectedQuery = "SELECT COUNT(*) FROM dristi_cases cases WHERE cases.filingnumber = '123';";
        assertEquals(expectedQuery, query);
    }

//    @Test
//    void checkCaseExistQuery_Exception() {
//        // Arrange
//        String courtCaseNumber = null;
//        String cnrNumber = null;
//        String filingNumber = "123";
//
//        Logger mockedLogger = mock(Logger.class);
//        doThrow(new RuntimeException()).when(mockedLogger).error(any(String.class));
//
//        // Assert
//        assertThrows(Exception.class, () -> {
//            queryBuilder.checkCaseExistQuery(courtCaseNumber, cnrNumber, filingNumber);
//        });
//    }

    @Test
    void testGetCasesSearchQuery_SingleCriteria() {
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId("12345");
        criteria.setCnrNumber("123");
        criteria.setFilingNumber("9876");
        criteria.setCourtCaseNumber("456");

        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = " SELECT cases.id as id, cases.tenantid as tenantid, cases.casenumber as casenumber, cases.resolutionmechanism as resolutionmechanism, cases.casetitle as casetitle, cases.casedescription as casedescription, cases.filingnumber as filingnumber, cases.casenumber as casenumber, cases.accesscode as accesscode, cases.courtcasenumber as courtcasenumber, cases.cnrNumber as cnrNumber,  cases.courtid as courtid, cases.benchid as benchid, cases.judgeid as judgeid, cases.stage as stage, cases.substage as substage, cases.filingdate as filingdate, cases.registrationdate as registrationdate, cases.natureofpleading as natureofpleading, cases.status as status, cases.remarks as remarks, cases.isactive as isactive, cases.casedetails as casedetails, cases.additionaldetails as additionaldetails, cases.casecategory as casecategory, cases.createdby as createdby, cases.lastmodifiedby as lastmodifiedby, cases.createdtime as createdtime, cases.lastmodifiedtime as lastmodifiedtime  FROM dristi_cases cases WHERE cases.id = ? AND cases.cnrNumber = ? AND cases.filingnumber = ? AND cases.courtcasenumber = ? ORDER BY cases.createdtime DESC ";
        String actualQuery = queryBuilder.getCasesSearchQuery(criteria, preparedStmtList);

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

        // Assert
        assertThrows(Exception.class, () -> queryBuilder.getCasesSearchQuery(criteria, preparedStmtList));
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

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getCasesSearchQuery(criteria, preparedStmtList));
    }

    @Test
    void testGetCasesSearchQuery_SingleCriteria2() {
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId(null);
        criteria.setCnrNumber("123");
        criteria.setFilingNumber("9876");
        criteria.setCourtCaseNumber("456");

        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = " SELECT cases.id as id, cases.tenantid as tenantid, cases.casenumber as casenumber, cases.resolutionmechanism as resolutionmechanism, cases.casetitle as casetitle, cases.casedescription as casedescription, cases.filingnumber as filingnumber, cases.casenumber as casenumber, cases.accesscode as accesscode, cases.courtcasenumber as courtcasenumber, cases.cnrNumber as cnrNumber,  cases.courtid as courtid, cases.benchid as benchid, cases.judgeid as judgeid, cases.stage as stage, cases.substage as substage, cases.filingdate as filingdate, cases.registrationdate as registrationdate, cases.natureofpleading as natureofpleading, cases.status as status, cases.remarks as remarks, cases.isactive as isactive, cases.casedetails as casedetails, cases.additionaldetails as additionaldetails, cases.casecategory as casecategory, cases.createdby as createdby, cases.lastmodifiedby as lastmodifiedby, cases.createdtime as createdtime, cases.lastmodifiedtime as lastmodifiedtime  FROM dristi_cases cases WHERE cases.cnrNumber = ? AND cases.filingnumber = ? AND cases.courtcasenumber = ? ORDER BY cases.createdtime DESC ";
        String actualQuery = queryBuilder.getCasesSearchQuery(criteria, preparedStmtList);

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

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getCasesSearchQuery(criteria, preparedStmtList));
    }

    @Test
    void testGetCasesSearchQuery_SingleCriteria3() {
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId(null);
        criteria.setCnrNumber(null);
        criteria.setFilingNumber("9876");
        criteria.setCourtCaseNumber("456");

        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = " SELECT cases.id as id, cases.tenantid as tenantid, cases.casenumber as casenumber, cases.resolutionmechanism as resolutionmechanism, cases.casetitle as casetitle, cases.casedescription as casedescription, cases.filingnumber as filingnumber, cases.casenumber as casenumber, cases.accesscode as accesscode, cases.courtcasenumber as courtcasenumber, cases.cnrNumber as cnrNumber,  cases.courtid as courtid, cases.benchid as benchid, cases.judgeid as judgeid, cases.stage as stage, cases.substage as substage, cases.filingdate as filingdate, cases.registrationdate as registrationdate, cases.natureofpleading as natureofpleading, cases.status as status, cases.remarks as remarks, cases.isactive as isactive, cases.casedetails as casedetails, cases.additionaldetails as additionaldetails, cases.casecategory as casecategory, cases.createdby as createdby, cases.lastmodifiedby as lastmodifiedby, cases.createdtime as createdtime, cases.lastmodifiedtime as lastmodifiedtime  FROM dristi_cases cases WHERE cases.filingnumber = ? AND cases.courtcasenumber = ? ORDER BY cases.createdtime DESC ";
        String actualQuery = queryBuilder.getCasesSearchQuery(criteria, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(2, preparedStmtList.size());
        assertEquals("9876", preparedStmtList.get(0));
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

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getCasesSearchQuery(criteria, preparedStmtList));
    }

    @Test
    void testGetCasesSearchQuery_SingleCriteria4() {
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId(null);
        criteria.setCnrNumber(null);
        criteria.setFilingNumber(null);
        criteria.setCourtCaseNumber("456");

        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = " SELECT cases.id as id, cases.tenantid as tenantid, cases.casenumber as casenumber, cases.resolutionmechanism as resolutionmechanism, cases.casetitle as casetitle, cases.casedescription as casedescription, cases.filingnumber as filingnumber, cases.casenumber as casenumber, cases.accesscode as accesscode, cases.courtcasenumber as courtcasenumber, cases.cnrNumber as cnrNumber,  cases.courtid as courtid, cases.benchid as benchid, cases.judgeid as judgeid, cases.stage as stage, cases.substage as substage, cases.filingdate as filingdate, cases.registrationdate as registrationdate, cases.natureofpleading as natureofpleading, cases.status as status, cases.remarks as remarks, cases.isactive as isactive, cases.casedetails as casedetails, cases.additionaldetails as additionaldetails, cases.casecategory as casecategory, cases.createdby as createdby, cases.lastmodifiedby as lastmodifiedby, cases.createdtime as createdtime, cases.lastmodifiedtime as lastmodifiedtime  FROM dristi_cases cases WHERE cases.courtcasenumber = ? ORDER BY cases.createdtime DESC ";
        String actualQuery = queryBuilder.getCasesSearchQuery(criteria, preparedStmtList);

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

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getCasesSearchQuery(criteria, preparedStmtList));
    }

    @Test
    void testGetCasesSearchQuery_SingleCriteria5() {
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId(null);
        criteria.setCnrNumber(null);
        criteria.setFilingNumber(null);
        criteria.setCourtCaseNumber("456");
        criteria.setFilingFromDate(LocalDate.parse("2024-05-28"));
        criteria.setFilingToDate(LocalDate.parse("2024-05-28"));
        criteria.setRegistrationFromDate(LocalDate.parse("2024-05-28"));
        criteria.setRegistrationToDate(LocalDate.parse("2024-05-28"));

        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = " SELECT cases.id as id, cases.tenantid as tenantid, cases.casenumber as casenumber, cases.resolutionmechanism as resolutionmechanism, cases.casetitle as casetitle, cases.casedescription as casedescription, cases.filingnumber as filingnumber, cases.casenumber as casenumber, cases.accesscode as accesscode, cases.courtcasenumber as courtcasenumber, cases.cnrNumber as cnrNumber,  cases.courtid as courtid, cases.benchid as benchid, cases.judgeid as judgeid, cases.stage as stage, cases.substage as substage, cases.filingdate as filingdate, cases.registrationdate as registrationdate, cases.natureofpleading as natureofpleading, cases.status as status, cases.remarks as remarks, cases.isactive as isactive, cases.casedetails as casedetails, cases.additionaldetails as additionaldetails, cases.casecategory as casecategory, cases.createdby as createdby, cases.lastmodifiedby as lastmodifiedby, cases.createdtime as createdtime, cases.lastmodifiedtime as lastmodifiedtime  FROM dristi_cases cases WHERE cases.courtcasenumber = ?OR cases.filingdate BETWEEN 2024-05-28 AND 2024-05-28 OR cases.registrationdate BETWEEN 2024-05-28 AND 2024-05-28  ORDER BY cases.createdtime DESC ";
        String actualQuery = queryBuilder.getCasesSearchQuery(criteria, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(1, preparedStmtList.size());
        assertEquals("456", preparedStmtList.get(0));
    }

    @Test()
    public void testGetCasesSearchQuery5_Exception() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = null;
        ids.add("1");
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId(null);
        criteria.setCnrNumber(null);
        criteria.setFilingNumber(null);
        criteria.setCourtCaseNumber("456");

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getCasesSearchQuery(criteria, preparedStmtList));
    }

    @Test
    void getDocumentSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getDocumentSearchQuery(ids, preparedStmtList);

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
        ids.add("1");

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getDocumentSearchQuery(ids, preparedStmtList));
    }

    @Test
    void getLinkedCaseSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getLinkedCaseSearchQuery(ids, preparedStmtList);

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
        ids.add("1");

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getLinkedCaseSearchQuery(ids, preparedStmtList));
    }

    @Test
    void getLitigantSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getLitigantSearchQuery(ids, preparedStmtList);

        // Assert
        String expectedQuery = " SELECT ltg.id as id, ltg.tenantid as tenantid, ltg.partycategory as partycategory, ltg.case_id as case_id, ltg.individualid as individualid,  ltg.organisationid as organisationid, ltg.partytype as partytype, ltg.isactive as isactive, ltg.additionaldetails as additionaldetails, ltg.createdby as createdby, ltg.lastmodifiedby as lastmodifiedby, ltg.createdtime as createdtime, ltg.lastmodifiedtime as lastmodifiedtime  FROM dristi_case_litigants ltg WHERE ltg.case_id IN (?,?,?)";
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

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getLitigantSearchQuery(ids, preparedStmtList));
    }

    @Test
    void getStatuteSectionSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtList);

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

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtList));
    }

    @Test
    void getRepresentativesSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getRepresentativesSearchQuery(ids, preparedStmtList);

        // Assert
        String expectedQuery = " SELECT rep.id as id, rep.tenantid as tenantid, rep.advocateid as advocateid, rep.case_id as case_id,  rep.isactive as isactive, rep.additionaldetails as additionaldetails, rep.createdby as createdby, rep.lastmodifiedby as lastmodifiedby, rep.createdtime as createdtime, rep.lastmodifiedtime as lastmodifiedtime  FROM dristi_case_representatives rep WHERE rep.case_id IN (?,?,?)";
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

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getRepresentativesSearchQuery(ids, preparedStmtList));
    }

    @Test
    void getRepresentingSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getRepresentingSearchQuery(ids, preparedStmtList);

        // Assert
        String expectedQuery = " SELECT rpst.id as id, rpst.tenantid as tenantid, rpst.partycategory as partycategory, rpst.representative_id as representative_id, rpst.individualid as individualid, rpst.case_id as case_id,  rpst.organisationid as organisationid, rpst.partytype as partytype, rpst.isactive as isactive, rpst.additionaldetails as additionaldetails, rpst.createdby as createdby, rpst.lastmodifiedby as lastmodifiedby, rpst.createdtime as createdtime, rpst.lastmodifiedtime as lastmodifiedtime  FROM dristi_case_representing rpst WHERE rpst.representative_id IN (?,?,?)";
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

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getRepresentingSearchQuery(ids, preparedStmtList));
    }

    @Test
    void getLinkedCaseDocumentSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getLinkedCaseDocumentSearchQuery(ids, preparedStmtList);

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

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getLinkedCaseDocumentSearchQuery(ids, preparedStmtList));
    }

    @Test
    void getLitigantDocumentSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getLitigantDocumentSearchQuery(ids, preparedStmtList);

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

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getLitigantDocumentSearchQuery(ids, preparedStmtList));
    }

    @Test
    void getRepresentativeDocumentSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getRepresentativeDocumentSearchQuery(ids, preparedStmtList);

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

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getRepresentativeDocumentSearchQuery(ids, preparedStmtList));
    }

    @Test
    void getRepresentingDocumentSearchQuery_ShouldGenerateCorrectQuery_WhenIdsNotEmpty() {
        // Arrange
        List<String> ids = List.of("1", "2", "3");
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getRepresentingDocumentSearchQuery(ids, preparedStmtList);

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

        // Assert
        assertThrows(CustomException.class, () -> queryBuilder.getRepresentingDocumentSearchQuery(ids, preparedStmtList));
    }

}
