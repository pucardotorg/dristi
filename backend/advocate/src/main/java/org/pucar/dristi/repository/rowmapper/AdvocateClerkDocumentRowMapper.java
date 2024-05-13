package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

import static org.pucar.dristi.config.ServiceConstants.ROW_MAPPER_EXCEPTION;

@Component
@Slf4j
public class AdvocateClerkDocumentRowMapper implements ResultSetExtractor<Map<UUID,List<Document>>> {


    /** To map query result to a map
     * @param rs
     * @return map of uuid along with its corresponding document lists
     */
    public Map<UUID,List<Document>> extractData(ResultSet rs) {
        Map<UUID, List<Document>> documentMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("clerk_id"));
                Document document = Document.builder()
                        .id(rs.getString("aid"))
                        .documentType(rs.getString("documenttype"))
                        .fileStore(rs.getString("filestore"))
                        .documentUid(rs.getString("documentuid"))
                        .additionalDetails(rs.getString("additionaldetails"))
                        .build();

                PGobject pgObject = (PGobject) rs.getObject("additionaldetails");
                if(pgObject!=null)
                    document.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));

                log.info("Document Map Data :: {}",documentMap);
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
            throw new CustomException(ROW_MAPPER_EXCEPTION,"Error occurred while processing document ResultSet: "+ e.getMessage());
        }
        return documentMap;
    }
}
