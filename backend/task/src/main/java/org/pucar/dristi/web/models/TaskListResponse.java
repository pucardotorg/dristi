package org.pucar.dristi.web.models;

<<<<<<< HEAD

import com.fasterxml.jackson.annotation.JsonProperty;
=======
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
>>>>>>> main
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

<<<<<<< HEAD
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
=======
import org.pucar.dristi.web.models.Task;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
>>>>>>> main
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * TaskListResponse
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:50.003326400+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
<<<<<<< HEAD
public class TaskListResponse {

    @JsonProperty("ResponseInfo")
    @Valid
    private ResponseInfo responseInfo = null;

    @JsonProperty("TotalCount")
    private Integer totalCount = null;

    @JsonProperty("list")
    @Valid
    private List<Task> list = null;

    @JsonProperty("pagination")
    @Valid
    private Pagination pagination = null;


=======
public class TaskListResponse   {
        @JsonProperty("responseInfo")

          @Valid
                private ResponseInfo responseInfo = null;

        @JsonProperty("TotalCount")

                private Integer totalCount = null;

        @JsonProperty("list")
          @Valid
                private List<Task> list = null;


        public TaskListResponse addListItem(Task listItem) {
            if (this.list == null) {
            this.list = new ArrayList<>();
            }
        this.list.add(listItem);
        return this;
        }

>>>>>>> main
}
