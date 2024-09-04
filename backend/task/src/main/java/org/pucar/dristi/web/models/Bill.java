package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.egov.common.contract.models.AuditDetails;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Bill {

    @JsonProperty("id")
    private String id;

    @JsonProperty("mobileNumber")
    private String mobileNumber;

    @JsonProperty("paidBy")
    private String paidBy;

    @JsonProperty("payerName")
    private String payerName;

    @JsonProperty("payerAddress")
    private String payerAddress;

    @JsonProperty("payerEmail")
    private String payerEmail;

    @JsonProperty("payerId")
    private String payerId;

    @JsonProperty("status")
    private StatusEnum status;

    @JsonProperty("reasonForCancellation")
    private String reasonForCancellation;

    @JsonProperty("isCancelled")
    private Boolean isCancelled;

    @JsonProperty("additionalDetails")
    private JsonNode additionalDetails;

    @JsonProperty("billDetails")
    @Valid
    private List<BillDetail> billDetails;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails;

    @JsonProperty("collectionModesNotAllowed")
    private List<String> collectionModesNotAllowed;

    @JsonProperty("partPaymentAllowed")
    private Boolean partPaymentAllowed;

    @JsonProperty("isAdvanceAllowed")
    private Boolean isAdvanceAllowed;

    @JsonProperty("minimumAmountToBePaid")
    private BigDecimal minimumAmountToBePaid;

    @JsonProperty("businessService")
    private String businessService;

    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("consumerCode")
    private String consumerCode;

    @JsonProperty("billNumber")
    private String billNumber;

    @JsonProperty("billDate")
    private Long billDate;

    @JsonProperty("amountPaid")
    private BigDecimal amountPaid;



    public enum StatusEnum {
        ACTIVE("ACTIVE"),

        CANCELLED("CANCELLED"),

        PAID("PAID"),

        EXPIRED("EXPIRED");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }


        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }



    }




}