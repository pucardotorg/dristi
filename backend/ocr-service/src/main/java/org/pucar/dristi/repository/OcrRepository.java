package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.pucar.dristi.repository.queryBuilder.OcrQueryBuilder;
import org.pucar.dristi.repository.rowMapper.OcrRowMapper;
import org.pucar.dristi.web.model.Ocr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class OcrRepository {

    private final OcrQueryBuilder ocrQueryBuilder;
    private final JdbcTemplate jdbcTemplate;
    private final OcrRowMapper ocrRowMapper;

    @Autowired
    public OcrRepository(OcrQueryBuilder ocrQueryBuilder, JdbcTemplate jdbcTemplate, OcrRowMapper ocrRowMapper) {
        this.ocrQueryBuilder = ocrQueryBuilder;
        this.jdbcTemplate = jdbcTemplate;
        this.ocrRowMapper = ocrRowMapper;
    }

    public List<Ocr> findByFilingNumber(String filingNumber) {

        String query = ocrQueryBuilder.getOcrSearchByFilingNumberQuery();
        return jdbcTemplate.query(query, new String[]{filingNumber}, ocrRowMapper);
    }
}
