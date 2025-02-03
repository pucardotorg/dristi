package digit.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.web.models.CaseDiary;
import digit.web.models.CaseDiaryDocument;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

import static digit.config.ServiceConstants.ROW_MAPPER_EXCEPTION;

@Component
@Slf4j
public class DiaryWithDocumentRowMapper implements ResultSetExtractor<List<CaseDiary>> {

    private final ObjectMapper objectMapper;

    public DiaryWithDocumentRowMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<CaseDiary> extractData(ResultSet rs) {

        Map<UUID, CaseDiary> caseDiaryMap = new HashMap<>();

        try {
            while (rs.next()) {
                UUID diaryId = UUID.fromString(rs.getString("id"));

                CaseDiary caseDiary = caseDiaryMap.computeIfAbsent(diaryId, id -> {
                    try {
                        CaseDiary newCaseDiary = CaseDiary.builder()
                                .id(id)
                                .tenantId(rs.getString("tenantId"))
                                .caseNumber(rs.getString("caseNumber"))
                                .diaryDate(rs.getLong("diaryDate"))
                                .diaryType(rs.getString("diaryType"))
                                .judgeId(rs.getString("judgeId"))
                                .documents(new ArrayList<>())
                                .auditDetails(AuditDetails.builder()
                                        .createdBy(rs.getString("diaryCreateBy"))
                                        .createdTime(rs.getLong("diaryCreatedTime"))
                                        .lastModifiedBy(rs.getString("diaryLastModifiedBy"))
                                        .lastModifiedTime(rs.getLong("diaryLastModifiedTime"))
                                        .build())
                                .build();

                        PGobject pGobject = (PGobject) rs.getObject("additionalDetails");
                        if (pGobject != null) {
                            newCaseDiary.setAdditionalDetails(objectMapper.readTree(pGobject.getValue()));
                        }
                        return newCaseDiary;
                    } catch (Exception e) {
                        throw new RuntimeException("Error mapping case diary", e);
                    }
                });

                caseDiary.getDocuments().add(
                        CaseDiaryDocument.builder()
                                .id(UUID.fromString(rs.getString("documentId")))
                                .tenantId(rs.getString("tenantId"))
                                .fileStoreId(rs.getString("fileStoreId"))
                                .documentUid(rs.getString("documentUid"))
                                .documentName(rs.getString("documentName"))
                                .documentType(rs.getString("documentType"))
                                .caseDiaryId(rs.getString("caseDiaryId"))
                                .isActive(rs.getBoolean("documentIsActive"))
                                .auditDetails(AuditDetails.builder()
                                        .createdBy(rs.getString("documentCreatedBy"))
                                        .createdTime(rs.getLong("documentCreatedTime"))
                                        .lastModifiedBy(rs.getString("documentLastModifiedBy"))
                                        .lastModifiedTime(rs.getLong("documentLastModifiedTime"))
                                        .build())
                                .build()
                );
            }
            return new ArrayList<>(caseDiaryMap.values());

        } catch (Exception e) {
            log.error("Error occurred while processing document ResultSet: {}", e.getMessage());
            throw new CustomException(ROW_MAPPER_EXCEPTION, "Error occurred while processing document ResultSet: " + e.getMessage());
        }
    }
}
