package org.pucar.repository.querybuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.web.models.AdvocateClerkSearchCriteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AdvocateClerkQueryBuilderTest {

    @Mock
    private List<Object> mockPreparedStmtList;

    private AdvocateClerkQueryBuilder advocateClerkQueryBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        advocateClerkQueryBuilder = new AdvocateClerkQueryBuilder();
    }

    @Test
    void testGetAdvocateSearchQuery() {
        // Mock AdvocateSearchCriteria objects
        AdvocateClerkSearchCriteria criteria1 = mock(AdvocateClerkSearchCriteria.class);
        when(criteria1.getId()).thenReturn("1");
        when(criteria1.getStateRegnNumber()).thenReturn(null);
        when(criteria1.getApplicationNumber()).thenReturn(null);

        AdvocateClerkSearchCriteria criteria2 = mock(AdvocateClerkSearchCriteria.class);
        when(criteria2.getId()).thenReturn(null);
        when(criteria2.getStateRegnNumber()).thenReturn("BR123");
        when(criteria2.getApplicationNumber()).thenReturn(null);

        AdvocateClerkSearchCriteria criteria3 = mock(AdvocateClerkSearchCriteria.class);
        when(criteria3.getId()).thenReturn(null);
        when(criteria3.getStateRegnNumber()).thenReturn(null);
        when(criteria3.getApplicationNumber()).thenReturn("APP456");

        AdvocateClerkSearchCriteria criteria4 = mock(AdvocateClerkSearchCriteria.class);
        when(criteria4.getId()).thenReturn(null);
        when(criteria4.getStateRegnNumber()).thenReturn(null);
        when(criteria4.getApplicationNumber()).thenReturn(null);
        when(criteria4.getIndividualId()).thenReturn("Ind123");

        // Create list of AdvocateSearchCriteria
        List<AdvocateClerkSearchCriteria> criteriaList = new ArrayList<>();
        criteriaList.add(criteria1);
        criteriaList.add(criteria2);
        criteriaList.add(criteria3);
        criteriaList.add(criteria4);

        // Create list of prepared statements
        List<Object> preparedStmtList = new ArrayList<>();

        // List of status
        List<String> statusList = new ArrayList<>();

        String applicationNumber = "";

        // Call the method to be tested
        String query = advocateClerkQueryBuilder.getAdvocateClerkSearchQuery(criteriaList, preparedStmtList,statusList, applicationNumber, new AtomicReference<>(true));

        // Assert the generated query string
        String e = "SELECT advc.id as id, advc.tenantid as tenantid, advc.applicationnumber as applicationnumber, advc.stateregnnumber as stateregnnumber, advc.individualid as individualid, advc.isactive as isactive, advc.additionaldetails as additionaldetails, advc.createdby as createdby, advc.lastmodifiedby as lastmodifiedby, advc.createdtime as createdtime, advc.lastmodifiedtime as lastmodifiedtime, advc.status as status  FROM dristi_advocate_clerk advc WHERE ( advc.id IN (  ? )  OR  advc.stateregnnumber IN (  ? )  OR  advc.applicationnumber IN (  ? )  OR  advc.individualid IN (  ? ) ) ORDER BY advc.createdtime DESC ";
        assertEquals(e, query);

        // Assert the prepared statement list
        assertEquals(4, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
        assertEquals("BR123", preparedStmtList.get(1));
    }

    @Test
    void testGetAdvocateClerkSearchQueryByApplicationNumber() {

        // Create list of prepared statements
        List<Object> preparedStmtList = new ArrayList<>();

        // Status List
        List<String> statusList = new ArrayList<>();
        String applicationNumber = "123APP";

        // Call the method to be tested
        String query = advocateClerkQueryBuilder.getAdvocateClerkSearchQuery(null, preparedStmtList,statusList, applicationNumber, new AtomicReference<>(true));

        // Assert the generated query string
        String e = "SELECT advc.id as id, advc.tenantid as tenantid, advc.applicationnumber as applicationnumber, advc.stateregnnumber as stateregnnumber, advc.individualid as individualid, advc.isactive as isactive, advc.additionaldetails as additionaldetails, advc.createdby as createdby, advc.lastmodifiedby as lastmodifiedby, advc.createdtime as createdtime, advc.lastmodifiedtime as lastmodifiedtime, advc.status as status  FROM dristi_advocate_clerk advc WHERE (LOWER(advc.applicationNumber) LIKE LOWER(?)) ORDER BY advc.createdtime DESC ";
        assertEquals(e, query);

        // Assert the prepared statement list
        assertEquals(1, preparedStmtList.size());
        assertEquals("%123app%", preparedStmtList.get(0));
    }


    @Test
    void testGetDocumentSearchQuery_EmptyIds() {
        // Setup test case with empty ids list
        List<String> ids = new ArrayList<>();

        // Call the method
        String query = advocateClerkQueryBuilder.getDocumentSearchQuery(ids, mockPreparedStmtList);

        // Assert the generated query string
        String expectedQuery = "SELECT doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.clerk_id as clerk_id  FROM dristi_document doc";
        assertEquals(expectedQuery, query);

        // Assert the prepared statement list is empty
        verify(mockPreparedStmtList, never()).add(any());
    }

    @Test
    void testGetDocumentSearchQuery_WithIds() {
        // Setup test case with non-empty ids list
        List<String> ids = Arrays.asList("1", "2", "3");

        // Call the method
        String query = advocateClerkQueryBuilder.getDocumentSearchQuery(ids, mockPreparedStmtList);

        // Assert the generated query string
        String expectedQuery = "SELECT doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.clerk_id as clerk_id  FROM dristi_document doc WHERE doc.clerk_id IN (?,?,?)";
        assertEquals(expectedQuery, query);

        // Assert the prepared statement list contains ids
        verify(mockPreparedStmtList, times(1)).addAll(ids);

    }

}
