package org.pucar.dristi.repository.querybuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskQueryBuilderTest {

    private TaskQueryBuilder queryBuilder;

    @BeforeEach
    void setUp() {
        queryBuilder = new TaskQueryBuilder();
    }

    @Test
    void testCheckTaskExistQuery() {
        // Arrange
        String cnrNumber = "CNR123";
        String filingNumber = "FILING123";
        UUID taskId = UUID.randomUUID();
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.checkTaskExistQuery(cnrNumber, filingNumber, taskId, preparedStmtList);

        // Assert
        String expectedQuery = "SELECT COUNT(*) FROM dristi_task task WHERE task.id = ? AND task.cnrNumber = ? AND task.filingNumber = ?";
        assertEquals(expectedQuery, query);
        assertEquals(3, preparedStmtList.size());
        assertEquals(taskId.toString(), preparedStmtList.get(0));
        assertEquals(cnrNumber, preparedStmtList.get(1));
        assertEquals(filingNumber, preparedStmtList.get(2));
    }

    @Test
    void testGetTaskSearchQuery() {
        // Arrange
        String id = "1";
        String tenantId = "tenant1";
        String status = "active";
        UUID orderId = UUID.randomUUID();
        String cnrNumber = "CNR123";
        String taskNumber = "TASK123";
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getTaskSearchQuery(id, tenantId, status, orderId, cnrNumber, taskNumber, preparedStmtList);

        // Assert
        String expectedQuery = "SELECT task.id as id, task.tenantid as tenantid, task.orderid as orderid, task.createddate as createddate, task.filingnumber as filingnumber, task.tasknumber as tasknumber, task.datecloseby as datecloseby, task.dateclosed as dateclosed, task.taskdescription as taskdescription, task.cnrnumber as cnrnumber, task.taskdetails as taskdetails, task.tasktype as tasktype, task.assignedto as assignedto, task.status as status, task.isactive as isactive,task.additionaldetails as additionaldetails, task.createdby as createdby, task.lastmodifiedby as lastmodifiedby, task.createdtime as createdtime, task.lastmodifiedtime as lastmodifiedtime FROM dristi_task task WHERE task.id = ? AND task.tenantId = ? AND task.status = ? AND task.orderId = ? AND task.cnrNumber = ? AND task.taskNumber = ? ORDER BY task.createdtime DESC ";
        assertEquals(expectedQuery, query);
        assertEquals(6, preparedStmtList.size());
        assertEquals(id, preparedStmtList.get(0));
        assertEquals(tenantId, preparedStmtList.get(1));
        assertEquals(status, preparedStmtList.get(2));
        assertEquals(orderId.toString(), preparedStmtList.get(3));
        assertEquals(cnrNumber, preparedStmtList.get(4));
        assertEquals(taskNumber, preparedStmtList.get(5));
    }

    @Test
    void testGetDocumentSearchQuery() {
        // Arrange
        List<String> ids = new ArrayList<>();
        ids.add("1");
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getDocumentSearchQuery(ids, preparedStmtList);

        // Assert
        String expectedQuery = "SELECT doc.id as id, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as additionaldetails, doc.task_id as task_id FROM dristi_task_document doc WHERE doc.task_id IN (?)";
        assertEquals(expectedQuery, query);
        assertEquals(1, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
    }

    @Test
    void testGetAmountSearchQuery() {
        // Arrange
        List<String> ids = new ArrayList<>();
        ids.add("1");
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getAmountSearchQuery(ids, preparedStmtList);

        // Assert
        String expectedQuery = "SELECT amount.id as id, amount.type as type, amount.amount as amount, amount.paymentRefNumber as paymentRefNumber, amount.status as status, amount.additionaldetails as additionaldetails, amount.task_id as task_id FROM dristi_task_amount amount WHERE amount.task_id IN (?)";
        assertEquals(expectedQuery, query);
        assertEquals(1, preparedStmtList.size());
        assertEquals("1", preparedStmtList.get(0));
    }
}
