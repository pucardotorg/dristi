package org.drishti.esign.repository.rowmapper;

import org.drishti.esign.web.models.ESignParameter;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EsignRowMapper implements RowMapper<ESignParameter> {

    @Override
    public ESignParameter mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ESignParameter.builder()
                .id(rs.getString("id"))
                .authType(rs.getString("authtype"))
                .fileStoreId(rs.getString("filestoreid"))
                .tenantId(rs.getString("tenantid"))
                .pageModule(rs.getString("pagemodule"))
                .signPlaceHolder(rs.getString("signplaceholder"))
                .signedFileStoreId(rs.getString("signedfilestoreid"))
                .filePath(rs.getString("filepath"))
                .auditDetails(AuditDetails.builder()
                        .createdBy(rs.getString("createdby"))
                        .createdTime(rs.getLong("createdtime"))
                        .lastModifiedBy(rs.getString("lastmodifiedby"))
                        .lastModifiedTime(rs.getLong("lastmodifiedtime")).build())
                .build();
    }
}
