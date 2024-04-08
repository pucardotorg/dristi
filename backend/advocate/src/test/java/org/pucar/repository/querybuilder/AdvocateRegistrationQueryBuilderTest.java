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
import static org.mockito.Mockito.verify;
@Slf4j
class AdvocateRegistrationQueryBuilderTest {

    @Mock
    private List<Object> mockPreparedStmtList;

    private AdvocateRegistrationQueryBuilder queryBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        queryBuilder = new AdvocateRegistrationQueryBuilder();
    }

    @Test
    void testGetAdvocateSearchQuery() {
        List<AdvocateSearchCriteria> criteriaList = new ArrayList<>();
        criteriaList.add(new AdvocateSearchCriteria("1", null, "APP1"));
        criteriaList.add(new AdvocateSearchCriteria(null, "REG2","APP2" ));

        // Execute the method under test
        String query = queryBuilder.getAdvocateSearchQuery(criteriaList, mockPreparedStmtList);

        // Verify the generated query
        String expectedQuery = " SELECT adv.id as aid, adv.tenantId as atenantId, adv.applicationNumber as aapplicationNumber, adv.barRegistrationNumber as abarRegistrationNumber, adv.organisationID as aorganisationID, adv.individualId as aindividualId, adv.isActive as aisActive, adv.additionalDetails as aadditionalDetails, adv.createdBy as acreatedBy, adv.lastmodifiedby as alastmodifiedby, adv.createdtime as acreatedtime, adv.lastmodifiedtime as alastmodifiedtime FROM dristi_advocate adv WHERE adv.id IN (?) AND adv.barRegistrationNumber IN (?) AND adv.applicationNumber IN (?,?)";
        log.info("BOOOl",query.contains(expectedQuery));
        assertEquals(expectedQuery, query);

// Verify that preparedStmtList is populated correctly
        verify(mockPreparedStmtList).addAll(List.of("1"));
        verify(mockPreparedStmtList).addAll(List.of("REG2"));
        verify(mockPreparedStmtList).addAll(List.of("APP1", "APP2"));
    }
}
