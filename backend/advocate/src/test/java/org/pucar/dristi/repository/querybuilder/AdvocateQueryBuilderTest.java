package org.pucar.dristi.repository.querybuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class AdvocateQueryBuilderTest {

    @InjectMocks
    private AdvocateQueryBuilder advocateQueryBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDocumentSearchQuery() {
        List<String> ids = List.of("doc1", "doc2");
        List<Object> preparedStmtList = new ArrayList<>();

        String query = advocateQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList);

        assertNotNull(query);
        assertTrue(query.contains("WHERE doc.advocateid IN (?,?)"));
        assertEquals(2, preparedStmtList.size());
        assertEquals("doc1", preparedStmtList.get(0));
        assertEquals("doc2", preparedStmtList.get(1));
    }

    @Test
    void testGetDocumentSearchQueryThrowsCustomException() {
        List<String> ids = null; // Setting to null to force an exception
        List<Object> preparedStmtList = new ArrayList<>();

        assertThrows(CustomException.class, () -> {
            advocateQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList);
        });
    }
}
