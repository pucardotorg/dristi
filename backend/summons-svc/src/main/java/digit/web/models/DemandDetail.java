package digit.web.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.AuditDetails;

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
