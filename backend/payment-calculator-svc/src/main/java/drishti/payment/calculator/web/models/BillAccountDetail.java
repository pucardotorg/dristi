package drishti.payment.calculator.web.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

/**
 * BillAccountDetail
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-06-10T14:05:42.847785340+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillAccountDetail {
    @JsonProperty("id")

    private String id = null;

    @JsonProperty("tenantId")

    private String tenantId = null;

    @JsonProperty("billDetail")

    private String billDetail = null;

    @JsonProperty("demandDetailId")

    private String demandDetailId = null;

    @JsonProperty("order")

    private Integer order = null;

    @JsonProperty("amount")

    @Valid
    private BigDecimal amount = null;

    @JsonProperty("adjustedAmount")

    @Valid
    private BigDecimal adjustedAmount = null;

    @JsonProperty("isActualDemand")

    private Boolean isActualDemand = null;

    @JsonProperty("glcode")

    private String glcode = null;

    @JsonProperty("taxHeadCode")

    private String taxHeadCode = null;

    @JsonProperty("additionalDetails")

    private Object additionalDetails = null;
    @JsonProperty("purpose")

    private PurposeEnum purpose = null;

    /**
     * Gets or Sets purpose
     */
    public enum PurposeEnum {
        ARREAR("ARREAR"),

        CURRENT("CURRENT"),

        ADVANCE("ADVANCE");

        private final String value;

        PurposeEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static PurposeEnum fromValue(String text) {
            for (PurposeEnum b : PurposeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }


}
