package org.pucar.repository.querybuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.web.models.AdvocateClerkSearchCriteria;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        String applicationNumber = new String();

        // Call the method to be tested
        String query = advocateClerkQueryBuilder.getAdvocateClerkSearchQuery(criteriaList, preparedStmtList,statusList, applicationNumber);

        // Assert the generated query string
        String e = "SELECT advc.id as id, advc.tenantid as tenantid, advc.applicationnumber as applicationnumber, advc.stateregnnumber as stateregnnumber, advc.individualid as individualid, advc.isactive as isactive, advc.additionaldetails as additionaldetails, advc.createdby as createdby, advc.lastmodifiedby as lastmodifiedby, advc.createdtime as createdtime, advc.lastmodifiedtime as lastmodifiedtime, advc.status as status  FROM dristi_advocate_clerk advc WHERE ( advc.id IN (  ? )  OR  advc.stateregnnumber IN (  ? )  OR  advc.applicationnumber IN (  ? )  OR  advc.individualid IN (  ? ) ) ORDER BY advc.createdtime DESC ";
        assertEquals(e, query);

        // Assert the prepared statement list
        assertEquals(4, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
        assertEquals("BR123", preparedStmtList.get(1));
    }
}
