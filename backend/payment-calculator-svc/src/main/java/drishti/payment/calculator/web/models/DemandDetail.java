package drishti.payment.calculator.web.models;

import digit.models.coremodels.AuditDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandDetail {

    private String id;

    private String demandId;

    @NotNull
    private String taxHeadMasterCode;

    @NotNull
    private BigDecimal taxAmount;

    @NotNull
    @Builder.Default
    private BigDecimal collectionAmount = BigDecimal.valueOf(0d);

    private AuditDetails auditDetails;

    private String tenantId;
}
