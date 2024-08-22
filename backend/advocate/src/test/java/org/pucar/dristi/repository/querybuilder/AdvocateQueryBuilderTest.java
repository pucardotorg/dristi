package org.pucar.dristi.repository.querybuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdvocateQueryBuilderTest {

    @InjectMocks
    private AdvocateQueryBuilder advocateQueryBuilder;

    Integer limit;
    Integer offset;
    private AdvocateSearchCriteria criteria;
    @BeforeEach
    void setUp() {
         limit = 10;
         offset = 0;
        criteria = new AdvocateSearchCriteria();
        criteria.setId("123");
        criteria.setBarRegistrationNumber("BAR123");
        criteria.setApplicationNumber("APP456");
        criteria.setIndividualId("IND789");

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAdvocateSearchQuery() {
        testGetAdvocateSearchQuery_NoCriteria();
        testGetAdvocateSearchQuery_WithCriteria();
    }

    private void testGetAdvocateSearchQuery_NoCriteria() {
        // Arrange
        AdvocateSearchCriteria criteria = null;
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = "tenant1";
        // Act
        String query = advocateQueryBuilder.getAdvocateSearchQuery(criteria, preparedStmtList, new ArrayList<>(), tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("FROM dristi_advocate adv"));
        assertTrue(query.contains("LIMIT ? OFFSET ?"));
        assertEquals(2, preparedStmtList.size());
        assertEquals(10, preparedStmtList.get(0));
        assertEquals(0, preparedStmtList.get(1));
    }

    private void testGetAdvocateSearchQuery_WithCriteria() {
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = "tenant1";
        // Act
        String query = advocateQueryBuilder.getAdvocateSearchQuery(criteria, preparedStmtList, new ArrayList<>(), tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("adv.id = ?"));
        assertTrue(query.contains("adv.barRegistrationNumber = ?"));
        assertTrue(query.contains("adv.applicationNumber = ?"));
        assertTrue(query.contains("adv.individualId = ?"));
    }

    @Test
    void testGetAdvocateSearchQueryByStatus() {
        testGetAdvocateSearchQueryByStatus_NoStatusOrTenantId();
        testGetAdvocateSearchQueryByStatus_WithStatusAndTenantId();
    }

    private void testGetAdvocateSearchQueryByStatus_NoStatusOrTenantId() {
        // Arrange
        String status = null;
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = null;

        // Act
        String query = advocateQueryBuilder.getAdvocateSearchQueryByStatus(status, preparedStmtList, new ArrayList<>(), tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("FROM dristi_advocate adv"));
        assertEquals(2, preparedStmtList.size());
    }

    private void testGetAdvocateSearchQueryByStatus_WithStatusAndTenantId() {
        // Arrange
        String status = "active";
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = "tenant1";

        // Act
        String query = advocateQueryBuilder.getAdvocateSearchQueryByStatus(status, preparedStmtList, new ArrayList<>(), tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("LOWER(adv.status) LIKE LOWER(?)"));
        assertTrue(query.contains("LOWER(adv.tenantid) LIKE LOWER(?)"));
        assertEquals(4, preparedStmtList.size());
        assertEquals("active", preparedStmtList.get(0));
        assertEquals("tenant1", preparedStmtList.get(1));
    }

    @Test
    void testGetAdvocateSearchQueryByApplicationNumber() {
        testGetAdvocateSearchQueryByApplicationNumber_NoApplicationNumberOrTenantId();
        testGetAdvocateSearchQueryByApplicationNumber_WithApplicationNumberAndTenantId();
    }

    private void testGetAdvocateSearchQueryByApplicationNumber_NoApplicationNumberOrTenantId() {
        // Arrange
        String applicationNumber = null;
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = null;

        // Act
        String query = advocateQueryBuilder.getAdvocateSearchQueryByApplicationNumber(applicationNumber, preparedStmtList, new ArrayList<>(), tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("FROM dristi_advocate adv"));
        assertEquals(2, preparedStmtList.size());
    }

    private void testGetAdvocateSearchQueryByApplicationNumber_WithApplicationNumberAndTenantId() {
        // Arrange
        String applicationNumber = "123456";
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = "tenant1";

        // Act
        String query = advocateQueryBuilder.getAdvocateSearchQueryByApplicationNumber(applicationNumber, preparedStmtList, new ArrayList<>(), tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("LOWER(adv.applicationnumber) LIKE LOWER(?)"));
        assertTrue(query.contains("LOWER(adv.tenantid) LIKE LOWER(?)"));
        assertEquals(4, preparedStmtList.size());
        assertEquals("%123456%", preparedStmtList.get(0));
        assertEquals("%tenant1%", preparedStmtList.get(1));
    }

    @Test
    void testGetDocumentSearchQuery() {
        List<String> ids = List.of("doc1", "doc2");
        List<Object> preparedStmtList = new ArrayList<>();

        String query = advocateQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList, new ArrayList<>());

        assertNotNull(query);
        assertTrue(query.contains("WHERE doc.advocateid IN (?,?)"));
        assertEquals(2, preparedStmtList.size());
        assertEquals("doc1", preparedStmtList.get(0));
        assertEquals("doc2", preparedStmtList.get(1));
    }

    @Test
    void testGetDocumentSearchQueryThrowsCustomException() {
        assertThrows(CustomException.class, this::invokeSearchException);
    }

    private void invokeSearchException() {
        List<String> ids = null; // Setting to null to force an exception
        List<Object> preparedStmtList = new ArrayList<>();
        advocateQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList, new ArrayList<>());
    }

    @Test
    void testGetAdvocateSearchQuery_Exception() {
        assertThrows(CustomException.class, this::invokeSearch);
    }

    private void invokeSearch() {
        List<Object> preparedStmtList = null;
        String tenantId = "tenant1";
        advocateQueryBuilder.getAdvocateSearchQuery(criteria, preparedStmtList, new ArrayList<>(), tenantId, limit, offset);
    }

    @Test
    void testGetAdvocateSearchQueryByStatus_Exception() {
        assertThrows(CustomException.class, this::invokeStatusSearch);
    }

    private void invokeStatusSearch() {
        String status = "active";
        List<Object> preparedStmtList = null;
        String tenantId = "tenant1";
        Integer limit = 10;
        Integer offset = 0;
        advocateQueryBuilder.getAdvocateSearchQueryByStatus(status, preparedStmtList, new ArrayList<>(), tenantId, limit, offset);
    }

    @Test
    void testGetAdvocateSearchQueryByApplicationNumber_Exception() {
        assertThrows(CustomException.class, this::invokeAppSearch);
    }

    private void invokeAppSearch() {
        String appNumber = "appNumber";
        List<Object> preparedStmtList = null;
        String tenantId = "tenant1";
        advocateQueryBuilder.getAdvocateSearchQueryByApplicationNumber(appNumber, preparedStmtList, new ArrayList<>(), tenantId, limit, offset);
    }

    @Test
    void testAddClauseIfRequired() {
        testAddClauseIfRequired_FirstCriteria();
        testAddClauseIfRequired_NotFirstCriteria();
    }

    private void testAddClauseIfRequired_FirstCriteria() {
        // Arrange
        StringBuilder query = new StringBuilder();
        boolean isFirstCriteria = true;

        // Act
        advocateQueryBuilder.addClauseIfRequired(query, isFirstCriteria);

        // Assert
        assertEquals(" WHERE ", query.toString());
    }

    private void testAddClauseIfRequired_NotFirstCriteria() {
        // Arrange
        StringBuilder query = new StringBuilder("InitialQuery ");
        boolean isFirstCriteria = false;

        // Act
        advocateQueryBuilder.addClauseIfRequired(query, isFirstCriteria);

        // Assert
        assertEquals("InitialQuery  AND ", query.toString());
    }
}
