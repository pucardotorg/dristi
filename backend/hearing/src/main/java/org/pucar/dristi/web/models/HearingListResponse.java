package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import hearing.src.main.java.org.pucar.dristi.web.models.Hearing;
import org.pucar.dristi.web.models.ResponseInfo;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * This object holds information about the hearingList response
 */
@Schema(description = "This object holds information about the hearingList response")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:11.072458+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HearingListResponse   {
        @JsonProperty("responseInfo")

          @Valid
                private ResponseInfo responseInfo = null;

        @JsonProperty("TotalCount")

                private Integer totalCount = null;

        @JsonProperty("HearingList")
          @Valid
                private List<Hearing> hearingList = null;


        public HearingListResponse addHearingListItem(Hearing hearingListItem) {
            if (this.hearingList == null) {
            this.hearingList = new ArrayList<>();
            }
        this.hearingList.add(hearingListItem);
        return this;
        }

}
