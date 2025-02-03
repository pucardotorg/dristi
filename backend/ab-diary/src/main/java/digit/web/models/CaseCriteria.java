package digit.web.models;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CaseCriteria
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseCriteria {

    @JsonProperty("caseSearchText")
    private String caseSearchText = null;

    @JsonProperty("responseList")
    @Valid
    private List<CourtCase> responseList = null;


    @JsonProperty("pagination")

    @Valid
    private Pagination pagination = null;

}
