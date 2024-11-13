package org.egov.transformer.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.transformer.config.TransformerProperties;
import org.egov.transformer.models.Task;
import org.egov.transformer.models.TaskRequest;
import org.egov.transformer.producer.TransformerProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final OrderService orderService;
    private final TransformerProperties properties;
    private final TransformerProducer producer;

    @Autowired
    public TaskService(OrderService orderService, TransformerProperties properties, TransformerProducer producer) {
        this.orderService = orderService;
        this.properties = properties;
        this.producer = producer;
    }


    public void addTaskDetails(Task task, String topic) {

        orderService.updateOrder(task);
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setTask(task);
        producer.push(topic, taskRequest);
    }
}
