package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.querybuilder.TaskQueryBuilder;
import org.pucar.dristi.repository.rowmapper.*;
import org.pucar.dristi.web.models.Amount;
import org.pucar.dristi.web.models.Task;
import org.pucar.dristi.web.models.TaskExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.EXIST_TASK_ERR;
import static org.pucar.dristi.config.ServiceConstants.SEARCH_TASK_ERR;

@Slf4j
@Repository
public class TaskRepository {

    private final TaskQueryBuilder queryBuilder;
    private final JdbcTemplate jdbcTemplate;
    private final TaskRowMapper rowMapper;
    private final AmountRowMapper amountRowMapper;
    private final DocumentRowMapper documentRowMapper;

    @Autowired
    public TaskRepository(TaskQueryBuilder queryBuilder,
                          JdbcTemplate jdbcTemplate,
                          TaskRowMapper rowMapper,
                          AmountRowMapper amountRowMapper,
                          DocumentRowMapper documentRowMapper) {
        this.queryBuilder = queryBuilder;
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
        this.amountRowMapper = amountRowMapper;
        this.documentRowMapper = documentRowMapper;
    }
    

    public List<Task> getApplications(String id, String tenantId, String status, UUID orderId, String cnrNumber, String taskNumber) {
        try {
            List<Task> taskList = new ArrayList<>();
            List<Object> preparedStmtList = new ArrayList<>();
            List<Object> preparedStmtAm = new ArrayList<>();
            List<Object> preparedStmtDc = new ArrayList<>();
            String casesQuery = "";
            casesQuery = queryBuilder.getTaskSearchQuery(id, tenantId, status, orderId, cnrNumber,taskNumber,preparedStmtList);             log.info("Final case query :: {}", casesQuery);
            List<Task> list = jdbcTemplate.query(casesQuery, preparedStmtList.toArray(), rowMapper);
            log.info("DB task list :: {}", list);
            if (list != null) {
                taskList.addAll(list);
            }

            List<String> ids = new ArrayList<>();
            for (Task task : taskList)
                ids.add(task.getId().toString());

            if (ids.isEmpty())
                return taskList;

            String amountQuery = "";
            amountQuery = queryBuilder.getAmountSearchQuery(ids, preparedStmtAm);
            log.info("Final Amount query :: {}", amountQuery);
            Map<UUID, Amount> amountMap = jdbcTemplate.query(amountQuery, preparedStmtAm.toArray(), amountRowMapper);
            log.info("DB Amount map :: {}", amountMap);
            if (amountMap != null) {
                taskList.forEach(order -> {
                    order.setAmount(amountMap.get(order.getId()));
                });
            }

            String documentQuery = "";
            documentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtDc);
            log.info("Final document query :: {}", documentQuery);
            Map<UUID, List<Document>> documentMap = jdbcTemplate.query(documentQuery, preparedStmtDc.toArray(), documentRowMapper);
            log.info("DB document map :: {}", documentMap);
            if (documentMap != null) {
                taskList.forEach(order -> {
                    order.setDocuments(documentMap.get(order.getId()));
                });
            }
            return taskList;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching task application list :: {}", e.toString());
            throw new CustomException(SEARCH_TASK_ERR, "Exception while fetching task application list: " + e.getMessage());
        }
    }

    public TaskExists checkTaskExists(TaskExists taskExists) {
        try {
            List<Object> preparedStmtList = new ArrayList<>();
            if (taskExists.getCnrNumber() == null && taskExists.getFilingNumber() == null &&taskExists.getTaskId()==null) {                taskExists.setExists(false);
            } else {
                String taskExistQuery = queryBuilder.checkTaskExistQuery(taskExists.getCnrNumber(), taskExists.getFilingNumber(), taskExists.getTaskId(),preparedStmtList);
                log.info("Final task exist query :: {}", taskExistQuery);
                Integer count = jdbcTemplate.queryForObject(taskExistQuery, preparedStmtList.toArray(), Integer.class);                taskExists.setExists(count != null && count > 0);
            }
            return taskExists;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while checking task exist :: {} ", e.toString());
            throw new CustomException(EXIST_TASK_ERR, "Custom exception while checking task exist : " + e.getMessage());
        }
    }
}