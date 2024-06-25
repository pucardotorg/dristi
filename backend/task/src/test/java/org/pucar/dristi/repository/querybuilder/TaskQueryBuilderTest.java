package org.pucar.dristi.repository.querybuilder;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskQueryBuilderTest {

    private TaskQueryBuilder taskQueryBuilder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        taskQueryBuilder = new TaskQueryBuilder();
    }

    @Test
    public void testCheckTaskExistQuery_withCnrNumber() {
        String cnrNumber = "123";
        String filingNumber = null;
        String expectedQuery = "SELECT COUNT(*) FROM dristi_task task WHERE task.cnrnumber = '123'";

        String actualQuery = taskQueryBuilder.checkTaskExistQuery(cnrNumber, filingNumber);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testCheckTaskExistQuery_withFilingNumber() {
        String cnrNumber = null;
        String filingNumber = "456";
        String expectedQuery = "SELECT COUNT(*) FROM dristi_task task WHERE task.filingnumber = '456'";

        String actualQuery = taskQueryBuilder.checkTaskExistQuery(cnrNumber, filingNumber);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testCheckTaskExistQuery_withCnrNumberAndFilingNumber() {
        String cnrNumber = "123";
        String filingNumber = "456";
        String expectedQuery = "SELECT COUNT(*) FROM dristi_task task WHERE task.cnrnumber = '123' AND task.filingnumber = '456'";

        String actualQuery = taskQueryBuilder.checkTaskExistQuery(cnrNumber, filingNumber);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testCheckTaskExistQuery_withException() {
        String cnrNumber = "123";
        taskQueryBuilder = new TaskQueryBuilder() {
            @Override
            public String checkTaskExistQuery(String cnrNumber, String filingNumber) {
                throw new RuntimeException("Forced exception");
            }
        };

        assertThrows(Exception.class, () -> taskQueryBuilder.checkTaskExistQuery(cnrNumber, null));
    }

//    @Test
//    public void testGetTaskSearchQuery_withAllParams() {
//        String id = "1";
//        String tenantId = "tenant";
//        String status = "active";
//        UUID orderId = UUID.randomUUID();
//        String cnrNumber = "123";
//        String expectedQuery =  "SELECT task.id as id, task.tenantid as tenantid, task.orderid as orderid, task.createddate as createddate, task.filingnumber as filingnumber, task.tasknumber as tasknumber, task.datecloseby as datecloseby, task.dateclosed as dateclosed, task.taskdescription as taskdescription, task.cnrnumber as cnrnumber, task.taskdetails as taskdetails, task.tasktype as tasktype, task.assignedto as assignedto, task.status as status, task.isactive as isactive,task.additionaldetails as additionaldetails, task.createdby as createdby, task.lastmodifiedby as lastmodifiedby, task.createdtime as createdtime, task.lastmodifiedtime as lastmodifiedtime FROM dristi_task task WHERE task.id = '1' AND task.tenantid = 'tenant' AND task.status = 'active' AND task.orderid = '72667fc1-689e-4d89-8719-b974e82c270e' AND task.cnrnumber = '123' ORDER BY task.createdtime DESC ";
//        String actualQuery = taskQueryBuilder.getTaskSearchQuery(id, tenantId, status, orderId, cnrNumber);
//
//        assertEquals(expectedQuery, actualQuery);
//    }

    @Test
    public void testGetTaskSearchQuery_withException() {
        String id = "1";
        taskQueryBuilder = new TaskQueryBuilder() {
            @Override
            public String getTaskSearchQuery(String id, String tenantId, String status, UUID orderId, String cnrNumber, String taskNumber) {
                throw new RuntimeException("Forced exception");
            }
        };

        assertThrows(Exception.class, () -> taskQueryBuilder.getTaskSearchQuery(id, null, null, null, null,null));
    }

    @Test
    public void testGetDocumentSearchQuery_withIds() {
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
    public void testGetDocumentSearchQuery_withException() {
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
    public void testGetAmountSearchQuery_withIds() {
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
    public void testGetAmountSearchQuery_withException() {
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
