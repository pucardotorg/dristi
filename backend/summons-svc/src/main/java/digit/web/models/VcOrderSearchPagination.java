package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VcOrderSearchPagination {
    @JsonProperty("limit")

    @DecimalMax("100")
    private Double limit = 10d;

    @JsonProperty("offSet")
    private Double offSet = 0d;
}
