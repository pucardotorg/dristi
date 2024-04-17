package org.pucar.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.web.models.AdvocateClerkSearchCriteria;
import org.pucar.web.models.AdvocateSearchCriteria;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
@Slf4j
class AdvocateQueryBuilderTest {

    @Mock
    private List<Object> mockPreparedStmtList = new ArrayList<>();

    private AdvocateClerkQueryBuilder queryBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        queryBuilder = new AdvocateClerkQueryBuilder();
    }

    @Test
    void testGetAdvocateSearchQuery() {
        List<AdvocateClerkSearchCriteria> criteriaList = new ArrayList<>();
        criteriaList.add(new AdvocateClerkSearchCriteria("1", "2", "3"));

        List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getAdvocateClerkSearchQuery(criteriaList, preparedStmtList);

        // Verify the generated query
        String expectedQuery = " SELECT advc.id as aid, advc.tenantid as atenantid, advc.applicationnumber as aapplicationnumber, advc.stateregnnumber as stateregnnumber, " +
                "advc.individualid as aindividualid, advc.isactive as aisactive, advc.additionaldetails as aadditionaldetails, advc.createdby as acreatedby, " +
                "advc.lastmodifiedby as alastmodifiedby, advc.createdtime as acreatedtime, advc.lastmodifiedtime as alastmodifiedtime,  doc.id as aaid, " +
                "doc.documenttype as adocumenttype, doc.filestore as afilestore, doc.documentuid as adocumentuid, doc.additionaldetails as aadditionaldetails  " +
                "FROM dristi_advocate_clerk advc LEFT JOIN distri_documents doc ON advc.id = doc.advocateclerkid WHERE  advc.id IN (  ? )  AND  advc.stateregnnumber IN (  ? )  AND  advc.applicationnumber IN (  ? )  " +
                "ORDER BY advc.createdtime DESC ";
//        log.info("BOOOl",query.contains(expectedQuery));
//        assertEquals(expectedQuery.trim(), query.trim());
    }
}
