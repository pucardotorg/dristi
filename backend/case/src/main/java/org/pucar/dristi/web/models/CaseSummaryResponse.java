package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;


/**
 * Represents a summary response for a case with pagination.
 * <p>
 * This class is used to hold the summary information of a case, including pagination details.
 *
 * @since 1.0
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseSummaryResponse {

    @JsonProperty("responseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("cases")
    private List<CaseSummary> cases;

    @JsonProperty("pagination")
    private Pagination pagination;
}
