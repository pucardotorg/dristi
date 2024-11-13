package org.pucar.dristi.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;


/**
 * Represents a request for retrieving case summaries.
 * This class encapsulates the necessary information for querying case summaries,
 * including request metadata, pagination details, and search criteria.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CaseSummaryRequest {

    @JsonProperty("RequestInfo")
    @Valid
    private RequestInfo requestInfo ;

    @JsonProperty("pagination")
    @Valid
    private Pagination pagination ;

    @JsonProperty("criteria")
    @Valid
    private CaseSearchCriteria criteria ;


}
