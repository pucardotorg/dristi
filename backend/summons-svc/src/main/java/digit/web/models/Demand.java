package digit.web.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.AuditDetails;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Demand {

    @JsonProperty("id")
    private String id;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("consumerCode")
    private String consumerCode;

    @JsonProperty("consumerType")
    private String consumerType;

    @JsonProperty("businessService")
    private String businessService;

    @Valid
    @JsonProperty("payer")
    private User payer;

    @JsonProperty("taxPeriodFrom")
    private Long taxPeriodFrom;

    @JsonProperty("taxPeriodTo")
    private Long taxPeriodTo;

    @Builder.Default
    @JsonProperty("demandDetails")
    @Valid
    private List<DemandDetail> demandDetails = new ArrayList<>();

    @JsonProperty("fixedBillExpiryDate")
    private Long fixedBillExpiryDate;

    @JsonProperty("billExpiryTime")
    private Long billExpiryTime;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails;

    @JsonProperty("additionalDetails")
    private Object additionalDetails;

    @Builder.Default
    @JsonProperty("minimumAmountPayable")
    private BigDecimal minimumAmountPayable = BigDecimal.ZERO;

    /**
     * Gets or Sets status
     */
    public enum DemandStatusEnum {

        ACTIVE("ACTIVE"),

        CANCELLED("CANCELLED"),

        ADJUSTED("ADJUSTED");

        private String value;

        DemandStatusEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static DemandStatusEnum fromValue(String text) {
            for (DemandStatusEnum b : DemandStatusEnum.values()) {
                if (String.valueOf(b.value).equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("status")
    private DemandStatusEnum status;


    public Demand addDemandDetailsItem(DemandDetail demandDetailsItem) {
        this.demandDetails.add(demandDetailsItem);
        return this;
    }

}
