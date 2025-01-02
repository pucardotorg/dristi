package org.pucar.dristi.repository.rowmapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.pucar.dristi.web.models.Document;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class RepresentingDocumentRowMapper implements ResultSetExtractor<Map<UUID,List<Document>>> {
    public Map<UUID,List<Document>> extractData(ResultSet rs) {
        Map<UUID, List<Document>> documentMap = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String representingId = rs.getString("representing_id");
                UUID uuid = UUID.fromString(representingId!=null ? representingId : "00000000-0000-0000-0000-000000000000");
                Document document = Document.builder()
                        .id(rs.getString("id"))
                        .documentType(rs.getString("documenttype"))
                        .fileStore(rs.getString("filestore"))
                        .documentUid(rs.getString("documentuid"))
                        .isActive(rs.getBoolean("isactive"))
                        .build();

                PGobject pgObject = (PGobject) rs.getObject("docadditionaldetails");
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
        } catch(CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error occurred while processing document ResultSet :: {}", e.toString());
            throw new CustomException("ROW_MAPPER_EXCEPTION","Exception occurred while processing document ResultSet: "+ e.getMessage());
        }
        return documentMap;
    }
}
