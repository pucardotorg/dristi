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


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PdfResponseRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    private final static String tableQuery = "SELECT referenceId, jsonResponse FROM referenceid_filestore_mapper";
    private final static String selectQuery = "SELECT jsonResponse FROM referenceid_filestore_mapper WHERE referenceId = ?";
    private final static String insertQuery = "INSERT INTO referenceid_filestore_mapper (referenceId, jsonResponse) VALUES (?, ?::jsonb) " +
            "ON CONFLICT (referenceId) DO UPDATE SET jsonResponse = EXCLUDED.jsonResponse";

    public List<Map<String, Object>> findAll() {
        return jdbcTemplate.queryForList(tableQuery);
    }

    public Optional<String> findJsonResponseByReferenceId(String referenceId) {
        try {
            List<String> results = jdbcTemplate.query(selectQuery, (rs, rowNum) -> {
                PGobject pgObject = (PGobject) rs.getObject("jsonResponse");
                return pgObject.getValue();
            }, new Object[]{referenceId});

            if (results.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.ofNullable(results.get(0));
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void saveJsonResponse(String referenceId, String jsonResponse) {
        jdbcTemplate.update(insertQuery, referenceId, jsonResponse);
    }
}

