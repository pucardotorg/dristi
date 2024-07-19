package org.pucar.dristi.repository;

import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PdfResponseRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String tableQuery = "SELECT referenceId, jsonResponse FROM referenceid_filestore_mapper";
    private final String selectQuery = "SELECT jsonResponse FROM referenceid_filestore_mapper WHERE referenceId = ?";
    private final String insertQuery = "INSERT INTO referenceid_filestore_mapper (referenceId, jsonResponse) VALUES (?, ?::jsonb) " +
            "ON CONFLICT (referenceId) DO UPDATE SET jsonResponse = EXCLUDED.jsonResponse";

    public List<Map<String, Object>> findAll() {
        return jdbcTemplate.queryForList(tableQuery);
    }

    public Optional<String> findJsonResponseByReferenceId(String referenceId) {
        try {
            return jdbcTemplate.queryForObject(selectQuery, new Object[]{referenceId}, (rs, rowNum) -> {
                PGobject pgObject = (PGobject) rs.getObject("jsonResponse");
                return Optional.ofNullable(pgObject.getValue());
            });
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void saveJsonResponse(String referenceId, String jsonResponse) {
        jdbcTemplate.update(insertQuery, referenceId, jsonResponse);
    }
}

