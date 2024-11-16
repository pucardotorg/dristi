package org.pucar.dristi.repository.querybuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.Pagination;
import org.pucar.dristi.web.models.TaskCriteria;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskQueryBuilderTest {

    @InjectMocks
    private TaskQueryBuilder taskQueryBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTotalCountQuery() {
        String baseQuery = "SELECT * FROM dristi_task";
        String expectedQuery = "SELECT COUNT(*) FROM (SELECT * FROM dristi_task) total_result";
        String result = taskQueryBuilder.getTotalCountQuery(baseQuery);
        assertEquals(expectedQuery, result);
    }

    @Test
    void testAddPaginationQuery() {
        String query = "SELECT * FROM dristi_task";
        Pagination pagination = new Pagination();
        List<Object> preparedStatementList = new ArrayList<>();
        List<Integer> preparedStatementArgList = new ArrayList<>();

        String result = taskQueryBuilder.addPaginationQuery(query, pagination, preparedStatementList, preparedStatementArgList);
        assertEquals("SELECT * FROM dristi_task LIMIT ? OFFSET ?", result);
        assertEquals(2, preparedStatementList.size());
        assertEquals(10.0, preparedStatementList.get(0));
        assertEquals(0.0, preparedStatementList.get(1));
        assertEquals(Arrays.asList(Types.DOUBLE, Types.DOUBLE), preparedStatementArgList);
    }

    @Test
    void testAddOrderByQuery() {
        String query = "SELECT * FROM dristi_task";
        Pagination pagination = new Pagination();

        String result = taskQueryBuilder.addOrderByQuery(query, pagination);
        assertEquals("SELECT * FROM dristi_task ORDER BY task.createdtime DESC ", result);

        // Test with null pagination
        result = taskQueryBuilder.addOrderByQuery(query, null);
        assertEquals("SELECT * FROM dristi_task ORDER BY task.createdtime DESC ", result);
    }

    @Test
    void testCheckTaskExistQuery() {
        List<Object> preparedStmtList = new ArrayList<>();
        String cnrNumber = "CNR123";
        String filingNumber = "FL123";
        UUID taskId = UUID.randomUUID();
        String referenceId = "APP-001";
        String state = "PENDING";

        String result = taskQueryBuilder.checkTaskExistQuery(cnrNumber, filingNumber, taskId, referenceId, state, preparedStmtList);

        assertTrue(result.contains("task.cnrnumber = ?"));
        assertTrue(result.contains("task.filingnumber = ?"));
        assertTrue(result.contains("task.id = ?"));
        assertEquals(5, preparedStmtList.size());
        assertEquals(cnrNumber, preparedStmtList.get(0));
        assertEquals(filingNumber, preparedStmtList.get(1));
        assertEquals(referenceId, preparedStmtList.get(2));
        assertEquals(state, preparedStmtList.get(3));
        assertEquals(taskId.toString(), preparedStmtList.get(4));

    }

    @Test
    void testGetTaskSearchQuery() {
        TaskCriteria criteria = TaskCriteria.builder()
                .taskNumber("TN123")
                .cnrNumber("CNR123")
                .tenantId("tenant123")
                .id("ID123")
                .status("InProgress")
                .orderId(UUID.randomUUID())
                .build();


        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        String result = taskQueryBuilder.getTaskSearchQuery(criteria, preparedStmtList, preparedStmtArgList);

        assertTrue(result.contains("task.id = ?"));
        assertTrue(result.contains("task.tenantid = ?"));
        assertTrue(result.contains("task.status = ?"));
        assertTrue(result.contains("task.orderid = ?"));
        assertTrue(result.contains("task.cnrnumber = ?"));
        assertTrue(result.contains("task.tasknumber = ?"));

        assertEquals(6, preparedStmtList.size());
        assertEquals(6, preparedStmtArgList.size());
    }

    @Test
    void testGetDocumentSearchQuery() {
        List<String> ids = Arrays.asList("ID1", "ID2");
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgListDoc = new ArrayList<>();

        String result = taskQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList, preparedStmtArgListDoc);

        assertTrue(result.contains("doc.task_id IN (?,?)"));
        assertEquals(2, preparedStmtList.size());
        assertEquals(2, preparedStmtArgListDoc.size());
    }

    @Test
    void testGetAmountSearchQuery() {
        List<String> ids = Arrays.asList("ID1", "ID2");
        List<Object> preparedStmtList = new ArrayList<>();
        List<Integer> preparedStmtArgListAm = new ArrayList<>();

        String result = taskQueryBuilder.getAmountSearchQuery(ids, preparedStmtList, preparedStmtArgListAm);

        assertTrue(result.contains("amount.task_id IN (?,?)"));
        assertEquals(2, preparedStmtList.size());
        assertEquals(2, preparedStmtArgListAm.size());
    }
}
