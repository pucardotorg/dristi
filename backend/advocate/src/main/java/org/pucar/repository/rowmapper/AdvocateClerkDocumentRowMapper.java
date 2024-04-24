package org.pucar.repository.rowmapper;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

import static org.pucar.config.ServiceConstants.ROW_MAPPER_EXCEPTION;

@Component
@Slf4j
public class AdvocateClerkDocumentRowMapper implements ResultSetExtractor<Map<UUID,List<Document>>> {
    public Map<UUID,List<Document>> extractData(ResultSet rs) {
        Map<UUID, List<Document>> documentMap = new LinkedHashMap<>();

        try {
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("clerk_id"));
                Document document = Document.builder()
                        .id(rs.getString("aid"))
                        .documentType(rs.getString("documenttype"))
                        .fileStore(rs.getString("filestore"))
                        .documentUid(rs.getString("documentuid"))
                        .additionalDetails(rs.getString("additionaldetails"))
                        .build();

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
