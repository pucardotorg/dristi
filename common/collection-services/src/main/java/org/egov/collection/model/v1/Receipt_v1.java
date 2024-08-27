package org.egov.collection.model.v1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.collection.model.AuditDetails;
import org.egov.collection.model.Instrument;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = {"receiptNumber"})
public class Receipt_v1 {

    @NotNull
    private String tenantId;

    private String transactionId;

    // Read only, populated during search
    private String receiptNumber;

    // Read only, populated during search
    private String consumerCode;

    // Read only, populated during search
    private Long receiptDate;

    @NotNull
    @Size(min = 1, max = 1)
    @Valid
    @JsonProperty("Bill")
    private List<Bill_v1> bill = new ArrayList<>();

    private AuditDetails_v1 auditDetails;

    @Valid
    private Instrument instrument;

}