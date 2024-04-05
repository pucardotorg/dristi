package org.pucar.repository.querybuilder;

import org.junit.jupiter.api.Assertions;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.mockito.Mockito;
import org.pucar.web.models.AdvocateSearchCriteria;

import java.util.ArrayList;
        import java.util.List;

public class AdvocateRegistrationQueryBuilderTest {

    private AdvocateRegistrationQueryBuilder advocateRegistrationQueryBuilder;
    private List<Object> preparedStmtList;

    @BeforeEach
    public void setUp() {
        advocateRegistrationQueryBuilder = new AdvocateRegistrationQueryBuilder();
        preparedStmtList = new ArrayList<>();
    }

    @Test
    public void testAdvocateSearchQuery() {
        AdvocateSearchCriteria criteria = new AdvocateSearchCriteria();
        criteria.setId("1");
        criteria.setApplicationNumber("APP123");
        criteria.setBarRegistrationNumber("BAR456");

        String expectedQuery = " SELECT adv.id as aid, adv.tenantId as atenantId, adv.applicationNumber as aapplicationNumber, adv.barRegistrationNumber as abarRegistrationNumber, adv.organisationID as aorganisationID, adv.individualId as aindividualId, adv.isActive as aisActive, adv.additionalDetails as aadditionalDetails, adv.createdBy as acreatedBy, adv.lastmodifiedby as alastmodifiedby, adv.createdtime as acreatedtime, adv.lastmodifiedtime as alastmodifiedtime  FROM dristi_advocate adv WHERE  adv.id = ?  AND  adv.applicationNumber = ?  AND  adv.barRegistrationNumber = ? ";
        String actualQuery = advocateRegistrationQueryBuilder.getAdvocateSearchQuery(criteria, preparedStmtList);

        Assertions.assertEquals(expectedQuery, actualQuery);
        Assertions.assertEquals(3, preparedStmtList.size());
        Assertions.assertEquals(1, preparedStmtList.get(0));
        Assertions.assertEquals("APP123", preparedStmtList.get(1));
        Assertions.assertEquals("BAR456", preparedStmtList.get(2));
    }

}

