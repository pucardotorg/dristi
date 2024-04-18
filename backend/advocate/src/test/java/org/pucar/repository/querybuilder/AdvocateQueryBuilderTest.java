package org.pucar.repository.querybuilder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.pucar.repository.querybuilder.AdvocateQueryBuilder;
import org.pucar.web.models.AdvocateSearchCriteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class AdvocateQueryBuilderTest {

    private AdvocateQueryBuilder advocateQueryBuilder;

    @BeforeEach
    public void setUp() {
        advocateQueryBuilder = new AdvocateQueryBuilder();
    }

    @Test
    public void testGetAdvocateSearchQuery() {
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

        // Call the method to be tested
        String query = advocateQueryBuilder.getAdvocateSearchQuery(criteriaList, preparedStmtList);

        // Assert the generated query string
        String expectedQuery = " SELECT adv.id as aid, adv.tenantId as atenantId, adv.applicationNumber as aapplicationNumber, adv.barRegistrationNumber as abarRegistrationNumber, adv.organisationID as aorganisationID, adv.individualId as aindividualId, adv.isActive as aisActive, adv.additionalDetails as aadditionalDetails, adv.createdBy as acreatedBy, adv.lastmodifiedby as alastmodifiedby, adv.createdtime as acreatedtime, adv.lastmodifiedtime as alastmodifiedtime FROM dristi_advocate adv WHERE adv.id IN (?) OR adv.barRegistrationNumber IN (?) OR adv.applicationNumber IN (?)";
        assertEquals(expectedQuery, query);

        // Assert the prepared statement list
        assertEquals(3, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
        assertEquals("BR123", preparedStmtList.get(1));
        assertEquals("APP456", preparedStmtList.get(2));
    }
}
