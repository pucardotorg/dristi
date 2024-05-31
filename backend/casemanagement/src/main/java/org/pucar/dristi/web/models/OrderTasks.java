package org.pucar.dristi.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.Task;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * OrderTasks
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-17T10:19:47.222225+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderTasks   {
        @JsonProperty("order")

          @Valid
                private Order order = null;

        @JsonProperty("tasks")
          @Valid
                private List<Task> tasks = null;


        public OrderTasks addTasksItem(Task tasksItem) {
            if (this.tasks == null) {
            this.tasks = new ArrayList<>();
            }
        this.tasks.add(tasksItem);
        return this;
        }

}
