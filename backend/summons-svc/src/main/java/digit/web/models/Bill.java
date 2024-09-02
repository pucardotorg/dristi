package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * Bill
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-06-10T14:05:42.847785340+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bill {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("mobileNumber")
    private String mobileNumber = null;

    @JsonProperty("payerName")
    private String payerName = null;

    @JsonProperty("payerAddress")
    private String payerAddress = null;

    @JsonProperty("payerEmail")
    private String payerEmail = null;

    @JsonProperty("isActive")
    private Boolean isActive = null;

    @JsonProperty("isCancelled")
    private Boolean isCancelled = null;

    @JsonProperty("additionalDetails")
    private Object additionalDetails = null;

    @JsonProperty("taxAndPayments")
    @Valid
    private List<TaxAndPayment> taxAndPayments = null;

    @JsonProperty("billDetails")
    @Valid
    private List<BillDetail> billDetails = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("auditDetails")
    @Valid
    private AuditDetails auditDetails = null;


    public Bill addTaxAndPaymentsItem(TaxAndPayment taxAndPaymentsItem) {
        if (this.taxAndPayments == null) {
            this.taxAndPayments = new ArrayList<>();
        }
        this.taxAndPayments.add(taxAndPaymentsItem);
        return this;
    }

    public Bill addBillDetailsItem(BillDetail billDetailsItem) {
        if (this.billDetails == null) {
            this.billDetails = new ArrayList<>();
        }
        this.billDetails.add(billDetailsItem);
        return this;
    }

}
