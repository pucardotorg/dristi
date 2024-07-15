package org.pucar.dristi.repository.querybuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.web.models.TaskCriteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.pucar.dristi.config.ServiceConstants.DOCUMENT_SEARCH_QUERY_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.TASK_SEARCH_QUERY_EXCEPTION;

public class TaskQueryBuilderTest {

    private TaskQueryBuilder taskQueryBuilder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        taskQueryBuilder = new TaskQueryBuilder();
    }

    @Test
    public void testGetTaskSearchQuery() {
        TaskCriteria criteria = new TaskCriteria();
        criteria.setTaskNumber("TASK123");
        criteria.setCnrNumber("CNR123");
        criteria.setTenantId("tenant1");
        criteria.setId("123e4567-e89b-12d3-a456-426614174000");
        criteria.setStatus("Open");
        criteria.setOrderId(UUID.randomUUID());
        List<Object> preparedStmtList = new ArrayList<>();
        String query = taskQueryBuilder.getTaskSearchQuery(criteria, preparedStmtList);

        assertNotNull(query);
        assertTrue(query.contains("SELECT task.id as id"));
        assertEquals(6, preparedStmtList.size());
    }

    @Test
    public void testGetTaskSearchQuery_Exception() {
        CustomException exception = assertThrows(CustomException.class, this::invokeSearchTaskQuery);
        assertEquals(TASK_SEARCH_QUERY_EXCEPTION, exception.getCode());
    }

    private void invokeSearchTaskQuery() {
        taskQueryBuilder.getTaskSearchQuery(null, new ArrayList<>());
    }


    @Test
    public void testGetDocumentSearchQuery_Exception() {

        CustomException exception = assertThrows(CustomException.class,this::invokeDocumentSearchTaskQuery);

        assertEquals(DOCUMENT_SEARCH_QUERY_EXCEPTION, exception.getCode());
    }

    private void invokeDocumentSearchTaskQuery() {
        taskQueryBuilder.getDocumentSearchQuery(Arrays.asList("id1", "id2"), null);
    }


    @Test
    void testCheckTaskExistQuery_withCnrNumberAndFilingNumber() {
        String cnrNumber = "123";
        String filingNumber = "456";
        String expectedQuery = "SELECT COUNT(*) FROM dristi_task task WHERE task.cnrnumber = ? AND task.filingnumber = ? AND task.id = ?";
        List<Object> preparedStmtList = new ArrayList<>();
        String taskId = "123e4567-e89b-12d3-a456-556642440000"; // Replace with a valid UUID string

        String actualQuery = taskQueryBuilder.checkTaskExistQuery(cnrNumber, filingNumber, UUID.fromString(taskId), preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
    }


    @Test
    void testCheckTaskExistQuery_withException() {
        String cnrNumber = "123";
        List<Object> preparedStmtList = new ArrayList<>();
        String taskId = "1234";

        taskQueryBuilder = new TaskQueryBuilder() {
            @Override
            public String checkTaskExistQuery(String cnrNumber, String filingNumber, UUID taskId, List<Object> preparedStmtList) {
                throw new RuntimeException("Forced exception");
            }
        };

        assertThrows(Exception.class, () -> taskQueryBuilder.checkTaskExistQuery(cnrNumber, null, UUID.fromString(taskId), preparedStmtList));
    }

    @Test
    void testGetTaskSearchQuery_withException() {
        String id = "1";
        taskQueryBuilder = new TaskQueryBuilder() {
            @Override
            public String getTaskSearchQuery(TaskCriteria criteria, List<Object> preparedStmtList) {
                throw new RuntimeException("Forced exception");
            }
        };

        assertThrows(Exception.class, () -> taskQueryBuilder.getTaskSearchQuery(new TaskCriteria(), null));
    }

    @Test
    void testGetDocumentSearchQuery_withIds() {
        List<String> ids = new ArrayList<>();
        ids.add("1");
        ids.add("2");
        List<Object> preparedStmtList = new ArrayList<>();
        String expectedQuery = "SELECT doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore," +
                " doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.task_id as task_id FROM dristi_task_document doc WHERE doc.task_id IN (?,?)";

        String actualQuery = taskQueryBuilder.getDocumentSearchQuery(ids, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(ids, preparedStmtList);
    }

    @Test
    void testGetDocumentSearchQuery_withException() {
        List<String> ids = new ArrayList<>();
        ids.add("1");
        taskQueryBuilder = new TaskQueryBuilder() {
            @Override
            public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
                throw new RuntimeException("Forced exception");
            }
        };

        assertThrows(Exception.class, () -> taskQueryBuilder.getDocumentSearchQuery(ids, new ArrayList<>()));
    }

    @Test
    void testGetAmountSearchQuery_withIds() {
        List<String> ids = new ArrayList<>();
        ids.add("1");
        ids.add("2");
        List<Object> preparedStmtList = new ArrayList<>();
        String expectedQuery = "SELECT amount.id as id, amount.type as type, amount.amount as amount," +
                " amount.paymentRefNumber as paymentRefNumber, amount.status as status, amount.additionaldetails as additionaldetails, amount.task_id as task_id FROM dristi_task_amount amount WHERE amount.task_id IN (?,?)";

        String actualQuery = taskQueryBuilder.getAmountSearchQuery(ids, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(ids, preparedStmtList);
    }

    @Test
    void testGetAmountSearchQuery_withException() {
        List<String> ids = new ArrayList<>();
        ids.add("1");
        taskQueryBuilder = new TaskQueryBuilder() {
            @Override
            public String getAmountSearchQuery(List<String> ids, List<Object> preparedStmtList) {
                throw new RuntimeException("Forced exception");
            }
        };

        assertThrows(Exception.class, () -> taskQueryBuilder.getAmountSearchQuery(ids, new ArrayList<>()));
    }
}
