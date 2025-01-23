package pucar.web.models.individual;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IndividualSearch {

    @JsonProperty("id")
    private List<String> id = null;

    @JsonProperty("individualId")
    private String individualId = null;

    @JsonProperty("clientReferenceId")
    private List<String> clientReferenceId = null;


    @JsonProperty("dateOfBirth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dateOfBirth = null;

    @JsonProperty("mobileNumber")
    private String mobileNumber = null;


    @JsonProperty("wardCode")

    private String wardCode = null;

    @JsonProperty("individualName")

    private String individualName = null;

    @JsonProperty("createdFrom")

    private BigDecimal createdFrom = null;

    @JsonProperty("createdTo")

    private BigDecimal createdTo = null;


    @JsonProperty("boundaryCode")

    private String boundaryCode = null;

    @JsonProperty("roleCodes")

    private List<String> roleCodes = null;


    @JsonProperty("username")
    private String username;


    @JsonProperty("userId")
    private Long userId;


    @JsonProperty("userUuid")
    @Size(min = 1)
    private List<String> userUuid;


    @JsonProperty("latitude")
    @DecimalMin("-90")
    @DecimalMax("90")
    private Double latitude;


    @JsonProperty("longitude")
    @DecimalMin("-180")
    @DecimalMax("180")
    private Double longitude;

    /*
     * @value unit of measurement in Kilometer
     * */

    @JsonProperty("searchRadius")
    @DecimalMin("0")
    private Double searchRadius;
}
