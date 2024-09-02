package drishti.payment.calculator.repository.rowmapper;

import digit.models.coremodels.AuditDetails;
import drishti.payment.calculator.web.models.Address;
import drishti.payment.calculator.web.models.PostalHub;
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
                .address(Address.builder()
                        .id(rs.getString("addressid"))
                        .tenantId(rs.getString("tenantid"))
                        .doorNo(rs.getString("doorno"))
                        .latitude(rs.getDouble("latitude"))
                        .longitude(rs.getDouble("longitude"))
                        .locationAccuracy(rs.getDouble("locationaccuracy"))
                        .type((rs.getString("type")))
                        .addressLine1(rs.getString("addressline1"))
                        .addressLine2(rs.getString("addressline2"))
                        .landmark(rs.getString("landmark"))
                        .city(rs.getString("city"))
                        .pincode(rs.getString("pincode"))
                        .buildingName(rs.getString("buildingname"))
                        .street(rs.getString("street"))
                        .build())
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
