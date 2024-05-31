package org.pucar.dristi.repository.querybuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.AdvocateClerkSearchCriteria;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class AdvocateClerkQueryBuilderTest {

    private AdvocateClerkQueryBuilder queryBuilder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        queryBuilder = new AdvocateClerkQueryBuilder();
    }

    @Test
    void getAdvocateClerkSearchQuery_NoCriteriaOrTenantId() {
        // Arrange
        AdvocateClerkSearchCriteria criteria = null;
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = null;
        Integer limit = 10;
        Integer offset = 0;

        // Act
        String query = queryBuilder.getAdvocateClerkSearchQuery(criteria, preparedStmtList, tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("FROM dristi_advocate_clerk advc"));
        assertEquals(2, preparedStmtList.size());
    }

    @Test
    void getAdvocateClerkSearchQuery_WithCriteriaAndTenantId() {
        // Arrange
        AdvocateClerkSearchCriteria criteria = new AdvocateClerkSearchCriteria();
        criteria.setId("123");
        criteria.setStateRegnNumber("456");
        criteria.setApplicationNumber("4567");
        criteria.setIndividualId("indivID");
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = "tenant1";
        Integer limit = 10;
        Integer offset = 0;

        // Act
        String query = queryBuilder.getAdvocateClerkSearchQuery(criteria, preparedStmtList, tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("SELECT advc.id as id"));
        assertTrue(query.contains("FROM dristi_advocate_clerk advc"));
        assertTrue(query.contains("LOWER(advc.tenantid) = LOWER(?)"));
        assertEquals(7, preparedStmtList.size());
        assertEquals("123", preparedStmtList.get(0));
        assertEquals("456", preparedStmtList.get(1));
        assertEquals("4567", preparedStmtList.get(2));
        assertEquals("indivID", preparedStmtList.get(3));
        assertEquals("tenant1", preparedStmtList.get(4));
        assertEquals(10, preparedStmtList.get(5));
        assertEquals(0, preparedStmtList.get(6));
    }

    @Test
    void getAdvocateClerkSearchQueryByStatus_NoStatusOrTenantId() {
        // Arrange
        String status = null;
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = null;
        Integer limit = 10;
        Integer offset = 0;

        // Act
        String query = queryBuilder.getAdvocateClerkSearchQueryByStatus(status, preparedStmtList, tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("FROM dristi_advocate_clerk advc"));
        assertEquals(2, preparedStmtList.size());
    }

    @Test
    void getAdvocateClerkSearchQueryByStatus_WithStatusAndTenantId() {
        // Arrange
        String status = "Active";
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = "tenant1";
        Integer limit = 10;
        Integer offset = 0;

        // Act
        String query = queryBuilder.getAdvocateClerkSearchQueryByStatus(status, preparedStmtList, tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("SELECT advc.id as id"));
        assertTrue(query.contains("FROM dristi_advocate_clerk advc"));
        assertTrue(query.contains("LOWER(advc.status) = LOWER(?)"));
        assertTrue(query.contains("LOWER(advc.tenantid) = LOWER(?)"));
        assertEquals(4, preparedStmtList.size());
        assertEquals("active", preparedStmtList.get(0));
        assertEquals("tenant1", preparedStmtList.get(1));
    }

    @Test
    void getAdvocateClerkSearchQueryByAppNumber_NoAppNumberOrTenantId() {
        // Arrange
        String applicationNumber = null;
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = null;
        Integer limit = 10;
        Integer offset = 0;

        // Act
        String query = queryBuilder.getAdvocateClerkSearchQueryByAppNumber(applicationNumber, preparedStmtList, tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("FROM dristi_advocate_clerk advc"));
        assertEquals(2, preparedStmtList.size());
    }

    @Test
    void getAdvocateClerkSearchQueryByAppNumber_WithAppNumberAndTenantId() {
        // Arrange
        String applicationNumber = "APP001";
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = "tenant1";
        Integer limit = 10;
        Integer offset = 0;

        // Act
        String query = queryBuilder.getAdvocateClerkSearchQueryByAppNumber(applicationNumber, preparedStmtList, tenantId, limit, offset);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("SELECT advc.id as id"));
        assertTrue(query.contains("FROM dristi_advocate_clerk advc"));
        assertTrue(query.contains("LOWER(advc.applicationnumber) = LOWER(?)"));
        assertTrue(query.contains("LOWER(advc.tenantid) = LOWER(?)"));
        assertEquals(4, preparedStmtList.size());
        assertEquals("app001", preparedStmtList.get(0));
        assertEquals("tenant1", preparedStmtList.get(1));
    }

    @Test
    public void testGetAdvocateClerkSearchQuery_withNullCriteria() {
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = "tenant1";
        Integer limit = 10;
        Integer offset = 0;

        String expectedQuery = "SELECT advc.id as id, advc.tenantid as tenantid, advc.applicationnumber as applicationnumber, advc.stateregnnumber as stateregnnumber, advc.individualid as individualid, advc.isactive as isactive, advc.additionaldetails as additionaldetails, advc.createdby as createdby, advc.lastmodifiedby as lastmodifiedby, advc.createdtime as createdtime, advc.lastmodifiedtime as lastmodifiedtime, advc.status as status  FROM dristi_advocate_clerk advc ORDER BY advc.createdtime DESC  LIMIT ? OFFSET ?";

        String actualQuery = queryBuilder.getAdvocateClerkSearchQuery(null, preparedStmtList, tenantId, limit, offset);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(List.of(10, 0), preparedStmtList);
    }

    @Test
    public void testGetDocumentSearchQuery_withValidIds() {
        List<String> ids = List.of("clerk1", "clerk2");
        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.clerk_id as clerk_id  FROM dristi_document doc WHERE doc.clerk_id IN (?,?)";

        String actualQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(List.of("clerk1", "clerk2"), preparedStmtList);
    }
    @Test
    public void testGetDocumentSearchQuery_withEmptyIds() {
        List<String> ids = new ArrayList<>();
        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.clerk_id as clerk_id  FROM dristi_document doc";

        String actualQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(new ArrayList<>(), preparedStmtList);
    }

    @Test
    void getAdvocateClerkSearchQuery_Exception() {
        AdvocateClerkSearchCriteria criteria = new AdvocateClerkSearchCriteria();
        criteria.setId("123");
        criteria.setStateRegnNumber("456");
        criteria.setApplicationNumber("4567");
        criteria.setIndividualId("indivID");
        List<Object> preparedStmtList = null;
        String tenantId = "tenant1";
        Integer limit = 10;
        Integer offset = 0;

        // Act and Assert
        assertThrows(CustomException.class, () -> {
            queryBuilder.getAdvocateClerkSearchQuery(criteria, preparedStmtList, tenantId, limit, offset);
        });
    }

    @Test
    void getAdvocateClerkSearchQueryByStatus_Exception() {
        String status = "active";
        List<Object> preparedStmtList = null;
        String tenantId = "tenant1";
        Integer limit = 10;
        Integer offset = 0;

        // Act and Assert
        assertThrows(CustomException.class, () -> {
            queryBuilder.getAdvocateClerkSearchQueryByStatus(status, preparedStmtList, tenantId, limit, offset);
        });
    }

    @Test
    void getAdvocateClerkSearchQueryByAppMumber_Exception() {
        String appNumber = "appNumber";
        List<Object> preparedStmtList = null;
        String tenantId = "tenant1";
        Integer limit = 10;
        Integer offset = 0;

        // Act and Assert
        assertThrows(CustomException.class, () -> {
            queryBuilder.getAdvocateClerkSearchQueryByAppNumber(appNumber, preparedStmtList, tenantId, limit, offset);
        });
    }

    @Test
    void getDocumentSearchQuery_Exception() {
        List<String> ids = List.of("clerk1", "clerk2");
        List<Object> preparedStmtList = null;

        // Act and Assert
        assertThrows(CustomException.class, () -> {
            queryBuilder.getDocumentSearchQuery(ids, preparedStmtList);
        });
    }
}
