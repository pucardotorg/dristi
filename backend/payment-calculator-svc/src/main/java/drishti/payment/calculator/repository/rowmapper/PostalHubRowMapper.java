package drishti.payment.calculator.repository.rowmapper;

import digit.models.coremodels.AuditDetails;
import drishti.payment.calculator.web.models.PostalHub;
import drishti.payment.calculator.web.models.enums.Classification;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PostalHubRowMapper implements RowMapper<PostalHub> {
    @Override
    public PostalHub mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PostalHub.builder()
                .hubId(rs.getString("hub_id"))
                .name(rs.getString("name"))
                .pincode(rs.getString("pincode"))
                .classification(Classification.fromValue(rs.getString("classification")))
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
