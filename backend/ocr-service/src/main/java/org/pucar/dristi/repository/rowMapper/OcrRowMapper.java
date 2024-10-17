package org.pucar.dristi.repository.rowMapper;

import org.pucar.dristi.web.model.Ocr;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class OcrRowMapper implements RowMapper<Ocr> {
    @Override
    public Ocr mapRow(ResultSet rs, int rowNum) throws SQLException {
        Ocr ocr = new Ocr();
        ocr.setId(UUID.fromString(rs.getString("id")));
        ocr.setTenantId(rs.getString("tenantid"));
        ocr.setFilingNumber(rs.getString("filingnumber"));
        ocr.setFileStoreId(rs.getString("filestoreid"));
        ocr.setDocumentType(rs.getString("documenttype"));
        ocr.setMessage(rs.getString("message"));
        ocr.setCode(rs.getString("code"));
        ocr.setExtractedData(rs.getObject("extracteddata"));
        return ocr;
    }
}
