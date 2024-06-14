package org.pucar.dristi.repository.rowMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

import static org.pucar.dristi.config.ServiceConstants.DOCUMENT_ROW_MAPPER_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.ROW_MAPPER_EXCEPTION;


@Component
@Slf4j
public class DocumentRowMapper implements ResultSetExtractor<Map<UUID,List<Document>>> {
    public Map<UUID,List<Document>> extractData(ResultSet rs) {
        Map<UUID, List<Document>> documentMap = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String applicationId = rs.getString("application_id");
                UUID uuid = UUID.fromString(applicationId !=null ? applicationId : "00000000-0000-0000-0000-000000000000");
                Document document = Document.builder()
                        .id(rs.getString("id"))
                        .documentType(rs.getString("documenttype"))
                        .fileStore(rs.getString("filestore"))
                        .documentUid(rs.getString("documentuid"))
                        .build();

                PGobject pgObject = (PGobject) rs.getObject("additionaldetails");
                if(pgObject!=null)
                    document.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));

                if (documentMap.containsKey(uuid) ) {
                    documentMap.get(uuid).add(document);
                }
                else{
                    List<Document> documents = new ArrayList<>();
                    documents.add(document);
                    documentMap.put(uuid,documents);
                }
            }
        }
        catch (Exception e){
            log.error("Error occurred while processing document ResultSet: {}", e.getMessage());
            throw new CustomException(DOCUMENT_ROW_MAPPER_EXCEPTION,"Error occurred while processing document ResultSet: "+ e.getMessage());
        }
        return documentMap;
    }
}