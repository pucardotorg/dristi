package org.pucar.dristi.repository.querybuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.AdvocateClerkRequest;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

class AdvocateQueryBuilderTest {

    @InjectMocks
    private AdvocateQueryBuilder advocateQueryBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAdvocateSearchQuery_NoCriteria() {
        // Arrange
//        YourClass yourClass = new YourClass();
        AdvocateSearchCriteria criteria = null;

        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = "tenant1";
        Integer limit = 10;
        Integer offset = 0;

        // Act
        String query = advocateQueryBuilder.getAdvocateSearchQuery(criteria, preparedStmtList, tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("FROM dristi_advocate adv"));
        assertTrue(query.contains("LIMIT ? OFFSET ?"));
        assertEquals(2, preparedStmtList.size());
        assertEquals(10, preparedStmtList.get(0));
        assertEquals(0, preparedStmtList.get(1));
    }

    @Test
    void getAdvocateSearchQuery_WithCriteria() {
        // Arrange
//        YourClass yourClass = new YourClass();
        AdvocateSearchCriteria criteria = new AdvocateSearchCriteria();
        criteria.setId("123");
        criteria.setBarRegistrationNumber("BAR123");
        criteria.setApplicationNumber("APP456");
        criteria.setIndividualId("IND789");
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = "tenant1";
        Integer limit = 10;
        Integer offset = 0;

        // Act
        String query = advocateQueryBuilder.getAdvocateSearchQuery(criteria, preparedStmtList, tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("adv.id = ?"));
        assertTrue(query.contains("adv.barRegistrationNumber = ?"));
        assertTrue(query.contains("adv.applicationNumber = ?"));
        assertTrue(query.contains("adv.individualId = ?"));
    }

    @Test
    void getAdvocateSearchQueryByStatus_NoStatusOrTenantId() {
        // Arrange
        String status = null;
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = null;
        Integer limit = 10;
        Integer offset = 0;

        // Act
        String query = advocateQueryBuilder.getAdvocateSearchQueryByStatus(status, preparedStmtList, tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("FROM dristi_advocate adv"));
        assertEquals(2, preparedStmtList.size());
    }

    @Test
    void getAdvocateSearchQueryByStatus_WithStatusAndTenantId() {
        // Arrange
        String status = "active";
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = "tenant1";
        Integer limit = 10;
        Integer offset = 0;

        // Act
        String query = advocateQueryBuilder.getAdvocateSearchQueryByStatus(status, preparedStmtList, tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("LOWER(adv.status) LIKE LOWER(?)"));
        assertTrue(query.contains("LOWER(adv.tenantid) LIKE LOWER(?)"));
        assertEquals(4, preparedStmtList.size());
        assertEquals("active", preparedStmtList.get(0));
        assertEquals("tenant1", preparedStmtList.get(1));
    }

    @Test
    void getAdvocateSearchQueryByApplicationNumber_NoApplicationNumberOrTenantId() {
        // Arrange
        String applicationNumber = null;
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = null;
        Integer limit = 10;
        Integer offset = 0;

        // Act
        String query = advocateQueryBuilder.getAdvocateSearchQueryByApplicationNumber(applicationNumber, preparedStmtList, tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("FROM dristi_advocate adv"));
        assertEquals(2, preparedStmtList.size());
    }

    @Test
    void getAdvocateSearchQueryByApplicationNumber_WithApplicationNumberAndTenantId() {
        // Arrange
        String applicationNumber = "123456";
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = "tenant1";
        Integer limit = 10;
        Integer offset = 0;

        // Act
        String query = advocateQueryBuilder.getAdvocateSearchQueryByApplicationNumber(applicationNumber, preparedStmtList, tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("LOWER(adv.applicationnumber) LIKE LOWER(?)"));
        assertTrue(query.contains("LOWER(adv.tenantid) LIKE LOWER(?)"));
//        assertTrue(query.contains("ORDER BY createdtime DESC"));
        assertEquals(4, preparedStmtList.size());
        assertEquals("%123456%", preparedStmtList.get(0));
        assertEquals("%tenant1%", preparedStmtList.get(1));
    }

    @Test
    void testGetDocumentSearchQuery() {
        List<String> ids = List.of("doc1", "doc2");
        List<Object> preparedStmtList = new ArrayList<>();

        String query = advocateQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList);

        assertNotNull(query);
        assertTrue(query.contains("WHERE doc.advocateid IN (?,?)"));
        assertEquals(2, preparedStmtList.size());
        assertEquals("doc1", preparedStmtList.get(0));
        assertEquals("doc2", preparedStmtList.get(1));
    }

    @Test
    void testGetDocumentSearchQueryThrowsCustomException() {
        List<String> ids = null; // Setting to null to force an exception
        List<Object> preparedStmtList = new ArrayList<>();

        assertThrows(CustomException.class, () -> {
            advocateQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList);
        });
    }

    @Test
    void getAdvocateSearchQuery_Exception() {
        AdvocateSearchCriteria criteria = new AdvocateSearchCriteria();
        criteria.setId("123");
        criteria.setBarRegistrationNumber("BAR123");
        criteria.setApplicationNumber("APP456");
        criteria.setIndividualId("IND789");
        List<Object> preparedStmtList = null;
        String tenantId = "tenant1";
        Integer limit = 10;
        Integer offset = 0;

        // Act and Assert
        assertThrows(CustomException.class, () -> {
            advocateQueryBuilder.getAdvocateSearchQuery(criteria, preparedStmtList, tenantId, limit, offset);
        });
    }

    @Test
    void getAdvocateSearchQueryByStatus_Exception() {
        String status = "active";
        List<Object> preparedStmtList = null;
        String tenantId = "tenant1";
        Integer limit = 10;
        Integer offset = 0;

        // Act and Assert
        assertThrows(CustomException.class, () -> {
            advocateQueryBuilder.getAdvocateSearchQueryByStatus(status, preparedStmtList, tenantId, limit, offset);
        });
    }

    @Test
    void getAdvocateSearchQueryByAppMumber_Exception() {
        String appNumber = "appNumber";
        List<Object> preparedStmtList = null;
        String tenantId = "tenant1";
        Integer limit = 10;
        Integer offset = 0;

        // Act and Assert
        assertThrows(CustomException.class, () -> {
            advocateQueryBuilder.getAdvocateSearchQueryByApplicationNumber(appNumber, preparedStmtList, tenantId, limit, offset);
        });
    }
}
