package digit.web.models;

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
import java.util.ArrayList;
import java.util.List;

/**
 * BillDetail
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-06-10T14:05:42.847785340+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillDetail {
    @JsonProperty("id")

    private String id = null;

    @JsonProperty("tenantId")

    private String tenantId = null;

    @JsonProperty("demandId")

    private String demandId = null;

    @JsonProperty("bill")

    private String bill = null;

    @JsonProperty("businessService")

    private String businessService = null;

    @JsonProperty("billNumber")

    private String billNumber = null;

    @JsonProperty("billDate")

    @Valid
    private BigDecimal billDate = null;

    @JsonProperty("consumerCode")

    private String consumerCode = null;

    @JsonProperty("consumerType")

    private String consumerType = null;

    @JsonProperty("minimumAmount")

    @Valid
    private BigDecimal minimumAmount = null;

    @JsonProperty("totalAmount")

    @Valid
    private BigDecimal totalAmount = null;

    @JsonProperty("fromPeriod")

    @Valid
    private BigDecimal fromPeriod = null;

    @JsonProperty("toPeriod")

    @Valid
    private BigDecimal toPeriod = null;

    @JsonProperty("collectedAmount")

    @Valid
    private BigDecimal collectedAmount = null;

    @JsonProperty("collectionModesNotAllowed")

    private List<String> collectionModesNotAllowed = null;

    @JsonProperty("partPaymentAllowed")

    private Boolean partPaymentAllowed = null;

    @JsonProperty("additionalDetails")

    private Object additionalDetails = null;

    @JsonProperty("billAccountDetails")
    @Valid
    private List<BillAccountDetail> billAccountDetails = null;
    @JsonProperty("status")

    private StatusEnum status = null;

    public BillDetail addCollectionModesNotAllowedItem(String collectionModesNotAllowedItem) {
        if (this.collectionModesNotAllowed == null) {
            this.collectionModesNotAllowed = new ArrayList<>();
        }
        this.collectionModesNotAllowed.add(collectionModesNotAllowedItem);
        return this;
    }

    public BillDetail addBillAccountDetailsItem(BillAccountDetail billAccountDetailsItem) {
        if (this.billAccountDetails == null) {
            this.billAccountDetails = new ArrayList<>();
        }
        this.billAccountDetails.add(billAccountDetailsItem);
        return this;
    }

    /**
     * Gets or Sets status
     */
    public enum StatusEnum {
        CREATED("CREATED"),

        CANCELLED("CANCELLED"),

        INSTRUMENT_BOUNCED("INSTRUMENT_BOUNCED");

        private final String value;

        StatusEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static StatusEnum fromValue(String text) {
            for (StatusEnum b : StatusEnum.values()) {
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
