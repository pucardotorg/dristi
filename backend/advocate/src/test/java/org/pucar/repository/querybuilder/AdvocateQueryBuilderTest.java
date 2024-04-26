package org.pucar.repository.querybuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.web.models.AdvocateSearchCriteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdvocateQueryBuilderTest {

    @Mock
    private List<Object> mockPreparedStmtList;

    @InjectMocks
    private AdvocateQueryBuilder advocateQueryBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        advocateQueryBuilder = new AdvocateQueryBuilder();
    }

    @Test
    void testGetAdvocateSearchQuery() {
        // Mock AdvocateSearchCriteria objects
        AdvocateSearchCriteria criteria1 = mock(AdvocateSearchCriteria.class);
        when(criteria1.getId()).thenReturn("1");
        when(criteria1.getBarRegistrationNumber()).thenReturn(null);
        when(criteria1.getApplicationNumber()).thenReturn(null);

        AdvocateSearchCriteria criteria2 = mock(AdvocateSearchCriteria.class);
        when(criteria2.getId()).thenReturn(null);
        when(criteria2.getBarRegistrationNumber()).thenReturn("BR123");
        when(criteria2.getApplicationNumber()).thenReturn(null);

        AdvocateSearchCriteria criteria3 = mock(AdvocateSearchCriteria.class);
        when(criteria3.getId()).thenReturn(null);
        when(criteria3.getBarRegistrationNumber()).thenReturn(null);
        when(criteria3.getApplicationNumber()).thenReturn("APP456");

        AdvocateSearchCriteria criteria4 = mock(AdvocateSearchCriteria.class);
        when(criteria4.getId()).thenReturn(null);
        when(criteria4.getBarRegistrationNumber()).thenReturn(null);
        when(criteria4.getApplicationNumber()).thenReturn(null);
        when(criteria4.getIndividualId()).thenReturn("Ind123");

        // Create list of AdvocateSearchCriteria
        List<AdvocateSearchCriteria> criteriaList = new ArrayList<>();
        criteriaList.add(criteria1);
        criteriaList.add(criteria2);
        criteriaList.add(criteria3);
        criteriaList.add(criteria4);

        // Create list of prepared statements
        List<Object> preparedStmtList = new ArrayList<>();

        // Status List
        List<String> statusList = new ArrayList<>();
        String applicationNumber = "";

        // Call the method to be tested
        String query = advocateQueryBuilder.getAdvocateSearchQuery(criteriaList, preparedStmtList,statusList, applicationNumber);

        // Assert the generated query string
        String e = " SELECT adv.id as id, adv.tenantid as tenantid, adv.applicationnumber as applicationnumber, adv.barregistrationnumber as barregistrationnumber, adv.advocateType as advocatetype, adv.organisationID as organisationid, adv.individualid as individualid, adv.isactive as isactive, adv.additionaldetails as additionaldetails, adv.createdby as createdby, adv.lastmodifiedby as lastmodifiedby, adv.createdtime as createdtime, adv.lastmodifiedtime as lastmodifiedtime, adv.status as status  FROM dristi_advocate adv WHERE (adv.id IN (?) OR adv.barRegistrationNumber IN (?) OR adv.applicationNumber IN (?) OR adv.individualId IN (?)) ORDER BY adv.createdtime DESC ";
        assertEquals(e, query);

        // Assert the prepared statement list
        assertEquals(4, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
        assertEquals("BR123", preparedStmtList.get(1));
        assertEquals("APP456", preparedStmtList.get(2));
    }

    @Test
    void testGetAdvocateSearchQueryByApplicationNumber() {

        // Create list of prepared statements
        List<Object> preparedStmtList = new ArrayList<>();

        // Status List
        List<String> statusList = new ArrayList<>();
        String applicationNumber = "123APP";

        // Call the method to be tested
        String query = advocateQueryBuilder.getAdvocateSearchQuery(null, preparedStmtList,statusList, applicationNumber);

        // Assert the generated query string
        String e = " SELECT adv.id as id, adv.tenantid as tenantid, adv.applicationnumber as applicationnumber, adv.barregistrationnumber as barregistrationnumber, adv.advocateType as advocatetype, adv.organisationID as organisationid, adv.individualid as individualid, adv.isactive as isactive, adv.additionaldetails as additionaldetails, adv.createdby as createdby, adv.lastmodifiedby as lastmodifiedby, adv.createdtime as createdtime, adv.lastmodifiedtime as lastmodifiedtime, adv.status as status  FROM dristi_advocate adv WHERE (LOWER(adv.applicationNumber) LIKE LOWER(?)) ORDER BY adv.createdtime DESC ";
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
        String query = advocateQueryBuilder.getDocumentSearchQuery(ids, mockPreparedStmtList);

        // Assert the generated query string
        String expectedQuery = "Select doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.advocateid as advocateid  FROM dristi_document doc";
        assertEquals(expectedQuery, query);

        // Assert the prepared statement list is empty
        verify(mockPreparedStmtList, never()).add(any());
    }

    @Test
    void testGetDocumentSearchQuery_WithIds() {
        // Setup test case with non-empty ids list
        List<String> ids = Arrays.asList("1", "2", "3");

        // Call the method
        String query = advocateQueryBuilder.getDocumentSearchQuery(ids, mockPreparedStmtList);

        // Assert the generated query string
        String expectedQuery = "Select doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.advocateid as advocateid  FROM dristi_document doc WHERE doc.advocateid IN (?,?,?)";
        assertEquals(expectedQuery, query);

        // Assert the prepared statement list contains ids
        verify(mockPreparedStmtList, times(1)).addAll(ids);

    }

}
