package org.pucar.dristi.repository.querybuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.AdvocateClerkSearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AdvocateClerkQueryBuilderTest {

    private AdvocateClerkQueryBuilder queryBuilder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        queryBuilder = new AdvocateClerkQueryBuilder();
    }
    @Test
    public void testGetAdvocateClerkSearchQuery_withNullCriteria() {
        List<Object> preparedStmtList = new ArrayList<>();
        String tenantId = "tenant1";
        AtomicReference<Boolean> isIndividualLoggedInUser = new AtomicReference<>(true);
        Integer limit = 10;
        Integer offset = 0;

        String expectedQuery = "SELECT advc.id as id, advc.tenantid as tenantid, advc.applicationnumber as applicationnumber, advc.stateregnnumber as stateregnnumber, advc.individualid as individualid, advc.isactive as isactive, advc.additionaldetails as additionaldetails, advc.createdby as createdby, advc.lastmodifiedby as lastmodifiedby, advc.createdtime as createdtime, advc.lastmodifiedtime as lastmodifiedtime, advc.status as status  FROM dristi_advocate_clerk advc ORDER BY advc.createdtime DESC  LIMIT ? OFFSET ?";

        String actualQuery = queryBuilder.getAdvocateClerkSearchQuery(null, preparedStmtList, tenantId, isIndividualLoggedInUser, limit, offset);

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
}