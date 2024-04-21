package org.pucar.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.web.models.AdvocateSearchCriteria;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdvocateQueryBuilderTest {

    @Mock
    private List<Object> mockPreparedStmtList;

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

        // Create list of AdvocateSearchCriteria
        List<AdvocateSearchCriteria> criteriaList = new ArrayList<>();
        criteriaList.add(criteria1);
        criteriaList.add(criteria2);
        criteriaList.add(criteria3);

        // Create list of prepared statements
        List<Object> preparedStmtList = new ArrayList<>();

        // Status List
        List<String> statusList = new ArrayList<>();
        // Call the method to be tested
        String query = advocateQueryBuilder.getAdvocateSearchQuery(criteriaList, preparedStmtList,statusList);

        // Assert the generated query string
        String e = " SELECT adv.id as id, adv.tenantid as tenantid, adv.applicationnumber as applicationnumber, adv.barregistrationnumber as barregistrationnumber, adv.advocateType as advocatetype, adv.organisationID as organisationid, adv.individualid as individualid, adv.isactive as isactive, adv.additionaldetails as additionaldetails, adv.createdby as createdby, adv.lastmodifiedby as lastmodifiedby, adv.createdtime as createdtime, adv.lastmodifiedtime as lastmodifiedtime, adv.status as status  FROM dristi_advocate adv WHERE (adv.id IN (?) OR adv.barRegistrationNumber IN (?) OR adv.applicationNumber IN (?)) ORDER BY adv.createdtime DESC ";
        assertEquals(e, query);

        // Assert the prepared statement list
        assertEquals(3, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
        assertEquals("BR123", preparedStmtList.get(1));
        assertEquals("APP456", preparedStmtList.get(2));
    }
}
