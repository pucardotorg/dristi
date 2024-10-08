package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.querybuilder.TaskCaseQueryBuilder;
import org.pucar.dristi.repository.querybuilder.TaskQueryBuilder;
import org.pucar.dristi.repository.rowmapper.AmountRowMapper;
import org.pucar.dristi.repository.rowmapper.DocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.TaskCaseRowMapper;
import org.pucar.dristi.repository.rowmapper.TaskRowMapper;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.*;

@Slf4j
@Repository
public class TaskRepository {

    private final TaskQueryBuilder queryBuilder;
    private final JdbcTemplate jdbcTemplate;
    private final TaskRowMapper rowMapper;
    private final AmountRowMapper amountRowMapper;
    private final DocumentRowMapper documentRowMapper;
    private final TaskCaseQueryBuilder taskCaseQueryBuilder;
    private final TaskCaseRowMapper taskCaseRowMapper;


    @Autowired
    public TaskRepository(TaskQueryBuilder queryBuilder,
                          JdbcTemplate jdbcTemplate,
                          TaskRowMapper rowMapper,
                          AmountRowMapper amountRowMapper,
                          DocumentRowMapper documentRowMapper, TaskCaseQueryBuilder taskCaseQueryBuilder, TaskCaseRowMapper taskCaseRowMapper) {
        this.queryBuilder = queryBuilder;
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
        this.amountRowMapper = amountRowMapper;
        this.documentRowMapper = documentRowMapper;
        this.taskCaseQueryBuilder = taskCaseQueryBuilder;
        this.taskCaseRowMapper = taskCaseRowMapper;
    }


    public List<Task> getTasks(TaskCriteria criteria, Pagination pagination) {
        try {
            List<Task> taskList = new ArrayList<>();
            List<Object> preparedStmtList = new ArrayList<>();
            List<Integer> preparedStmtArgList = new ArrayList<>();

            List<Object> preparedStmtAm = new ArrayList<>();
            List<Integer> preparedStmtArgAm = new ArrayList<>();

            List<Object> preparedStmtDc = new ArrayList<>();
            List<Integer> preparedStmtArgDc = new ArrayList<>();

            String taskQuery = "";
            taskQuery = queryBuilder.getTaskSearchQuery(criteria, preparedStmtList, preparedStmtArgList);
            if (preparedStmtList.size() != preparedStmtArgList.size()) {
                log.info("Arg size :: {}, and ArgType size :: {}", preparedStmtList.size(), preparedStmtArgList.size());
                throw new CustomException(SEARCH_TASK_ERR, "Args and ArgTypes size mismatch");
            }
            taskQuery = queryBuilder.addOrderByQuery(taskQuery, pagination);
            log.info("Final Task query :: {}", taskQuery);

            if (pagination != null) {
                Integer totalRecords = getTotalCountOrders(taskQuery, preparedStmtList);
                log.info("Total count without pagination :: {}", totalRecords);
                pagination.setTotalCount(Double.valueOf(totalRecords));
                taskQuery = queryBuilder.addPaginationQuery(taskQuery, pagination, preparedStmtList, preparedStmtArgList);
            }

            List<Task> list = jdbcTemplate.query(taskQuery, preparedStmtList.toArray(), preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(), rowMapper);
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
            amountQuery = queryBuilder.getAmountSearchQuery(ids, preparedStmtAm, preparedStmtArgAm);
            log.info("Final Amount query :: {}", amountQuery);
            if (preparedStmtAm.size() != preparedStmtArgAm.size()) {
                log.info("Amount Arg size :: {}, and ArgType size :: {}", preparedStmtAm.size(), preparedStmtArgAm.size());
                throw new CustomException(TASK_SEARCH_QUERY_EXCEPTION, "Arg and ArgType size mismatch for amount search");
            }
            Map<UUID, Amount> amountMap = jdbcTemplate.query(amountQuery, preparedStmtAm.toArray(), preparedStmtArgAm.stream().mapToInt(Integer::intValue).toArray(), amountRowMapper);
            log.info("DB Amount map :: {}", amountMap);
            if (amountMap != null) {
                taskList.forEach(order -> order.setAmount(amountMap.get(order.getId())));
            }

            String documentQuery = "";
            documentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtDc, preparedStmtArgDc);
            log.info("Final document query :: {}", documentQuery);
            if (preparedStmtDc.size() != preparedStmtArgDc.size()) {
                log.info("Doc Arg size :: {}, and ArgType size :: {}", preparedStmtDc.size(), preparedStmtArgDc.size());
                throw new CustomException(TASK_SEARCH_QUERY_EXCEPTION, "Arg and ArgType size mismatch for document search");
            }
            Map<UUID, List<Document>> documentMap = jdbcTemplate.query(documentQuery, preparedStmtDc.toArray(), preparedStmtArgDc.stream().mapToInt(Integer::intValue).toArray(), documentRowMapper);
            log.info("DB document map :: {}", documentMap);
            if (documentMap != null) {
                taskList.forEach(order -> order.setDocuments(documentMap.get(order.getId())));
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
            if (taskExists.getCnrNumber() == null && taskExists.getFilingNumber() == null && taskExists.getTaskId() == null) {
                taskExists.setExists(false);
            } else {
                String taskExistQuery = queryBuilder.checkTaskExistQuery(taskExists.getCnrNumber(), taskExists.getFilingNumber(), taskExists.getTaskId(), preparedStmtList);
                log.info("Final task exist query :: {}", taskExistQuery);
                Integer count = jdbcTemplate.queryForObject(taskExistQuery, Integer.class, preparedStmtList.toArray());
                taskExists.setExists(count != null && count > 0);
            }
            return taskExists;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while checking task exist :: {} ", e.toString());
            throw new CustomException(EXIST_TASK_ERR, "Custom exception while checking task exist : " + e.getMessage());
        }
    }

    public Integer getTotalCountOrders(String baseQuery, List<Object> preparedStmtList) {
        String countQuery = queryBuilder.getTotalCountQuery(baseQuery);
        log.info("Final count query :: {}", countQuery);
        return jdbcTemplate.queryForObject(countQuery, Integer.class, preparedStmtList.toArray());
    }

    public Integer getTotalCountTaskCase(String baseQuery, List<Object> preparedStmtList) {
        String countQuery = taskCaseQueryBuilder.getTotalCountQuery(baseQuery);
        log.info("Final count query :: {}", countQuery);
        return jdbcTemplate.queryForObject(countQuery, Integer.class, preparedStmtList.toArray());
    }

    public List<TaskCase> getTaskWithCaseDetails(TaskCaseSearchRequest request) {

        List<Object> preparedStmtList = new ArrayList<>();

        List<Integer> preparedStmtArgDc = new ArrayList<>();

        String taskQuery = taskCaseQueryBuilder.getTaskTableSearchQuery(request.getCriteria(), preparedStmtList);
        taskQuery = taskCaseQueryBuilder.addOrderByQuery(taskQuery, request.getPagination());
        taskQuery = taskCaseQueryBuilder.addApplicationStatusQuery(request.getCriteria(), taskQuery, preparedStmtList);
        log.debug("Final query: " + taskQuery);

        if (request.getPagination() != null) {
            Integer totalRecords = getTotalCountTaskCase(taskQuery, preparedStmtList);
            log.info("Total count without pagination :: {}", totalRecords);
            request.getPagination().setTotalCount(Double.valueOf(totalRecords));
            taskQuery = taskCaseQueryBuilder.addPaginationQuery(taskQuery, request.getPagination(), preparedStmtList);
        }
        List<TaskCase> list = jdbcTemplate.query(taskQuery, preparedStmtList.toArray(), taskCaseRowMapper);
        List<Object> preparedStmtDc = new ArrayList<>();

        List<String> ids = new ArrayList<>();
        if (list == null) {
            return new ArrayList<>();
        }
        for (TaskCase task : list)
            ids.add(task.getId().toString());

        String documentQuery = "";
        documentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtDc,preparedStmtArgDc);
        log.info("Final document query in summon table :: {}", documentQuery);
        Map<UUID, List<Document>> documentMap = jdbcTemplate.query(documentQuery, preparedStmtDc.toArray(), documentRowMapper);
        log.info("DB document map in summon table :: {}", documentMap);
        if (documentMap != null) {
            list.forEach(order -> {
                order.setDocuments(documentMap.get(order.getId()));
            });
        }

        return list;

    }

}