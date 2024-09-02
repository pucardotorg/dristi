package drishti.payment.calculator.repository.rowmapper;

import digit.models.coremodels.AuditDetails;
import drishti.payment.calculator.web.models.PostalService;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PostalServiceRowMapper implements RowMapper<PostalService> {
    @Override
    public PostalService mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PostalService.builder()
                .postalHubId(rs.getString("postal_hub_id"))
                .postalServiceId(rs.getString("postal_service_id"))
                .pincode(rs.getString("pincode"))
                .distanceKM(rs.getDouble("distance_km"))
                .tenantId(rs.getString("tenant_id"))
                .auditDetails(AuditDetails.builder()
                        .createdBy(rs.getString("created_by"))
                        .createdTime(rs.getLong("created_time"))
                        .lastModifiedBy(rs.getString("last_modified_by"))
                        .lastModifiedTime(rs.getLong("last_modified_time"))
                        .build())
                .rowVersion(rs.getInt("row_version"))
                .build();
    }
}
