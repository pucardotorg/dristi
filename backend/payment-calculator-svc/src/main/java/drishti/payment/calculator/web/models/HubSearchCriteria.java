package drishti.payment.calculator.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * the fields specified will be used in a logical AND condition
 */
@Schema(description = "the fields specified will be used in a logical AND condition")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-06-10T14:05:42.847785340+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HubSearchCriteria {

    @JsonProperty("hubId")
    private List<String> hubId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("pincode")
    private List<String> pincode;

}
