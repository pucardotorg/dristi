package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * This object holds information about the aapplication List response
 */
@Schema(description = "This object holds information about the aapplication List response")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:12:15.132164900+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationListResponse   {
        @JsonProperty("ResponseInfo")

          @Valid
                private ResponseInfo responseInfo = null;

        @JsonProperty("TotalCount")

                private Integer totalCount = null;

        @JsonProperty("applicationList")
          @Valid
                private List<Application> applicationList = null;


        public ApplicationListResponse addApplicationListItem(Application applicationListItem) {
            if (this.applicationList == null) {
            this.applicationList = new ArrayList<>();
            }
        this.applicationList.add(applicationListItem);
        return this;
        }

}
