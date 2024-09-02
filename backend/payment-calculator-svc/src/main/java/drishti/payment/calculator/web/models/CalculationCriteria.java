package drishti.payment.calculator.web.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * CalculationCriteria
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-06-10T14:05:42.847785340+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalculationCriteria {
    @JsonProperty("applicationId")

    private String applicationId = null;

    @JsonProperty("tenantId")

    private String tenantId = null;
    @JsonProperty("formType")

    private FormTypeEnum formType = null;
    @JsonProperty("request")

    private Object request = null;

    /**
     * Gets or Sets formType
     */
    public enum FormTypeEnum {
        SUMMONS("SUMMONS"),

        E_FILLING("E-FILLING");

        private final String value;

        FormTypeEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static FormTypeEnum fromValue(String text) {
            for (FormTypeEnum b : FormTypeEnum.values()) {
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
