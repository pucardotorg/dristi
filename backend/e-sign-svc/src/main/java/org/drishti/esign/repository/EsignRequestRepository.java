package org.drishti.esign.repository;


import lombok.extern.slf4j.Slf4j;
import org.drishti.esign.repository.rowmapper.EsignRowMapper;
import org.drishti.esign.web.models.ESignParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class EsignRequestRepository {


    private static final String BASE_QUERY = "SELECT * ";
    private static final String FROM_TABLE = "FROM dristi_esign_pdf de ";
    private static final String WHERE = "WHERE ";
    private static final String LIMIT = "LIMIT ? ";
    private static final String OFFSET = "OFFSET ? ";

    private final JdbcTemplate jdbcTemplate;
    private final EsignRowMapper rowMapper;

    @Autowired
    public EsignRequestRepository(JdbcTemplate jdbcTemplate, EsignRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }


    private String getSignQuery() {

        StringBuilder query = new StringBuilder(BASE_QUERY);
        query.append(FROM_TABLE).append(WHERE);
        query.append("id = ? ").append(LIMIT).append(OFFSET).append(";");
        return query.toString();

    }

    //
    private String getSignQuery(boolean includePagination, String... conditions) {
        StringBuilder query = new StringBuilder(BASE_QUERY);
        query.append(FROM_TABLE);

        if (conditions.length > 0) {
            query.append(WHERE).append(String.join(" AND ", conditions));
        }

        if (includePagination) {
            query.append(" ").append(LIMIT).append(OFFSET);
        }

        return query.toString();
    }

    public ESignParameter getESignDetails(String id) {
        log.info("Method=getESignDetails, Result=InProgress,id={}", id);

        List<Integer> preparedStmtArgList = new ArrayList<>(List.of(Types.VARCHAR, Types.INTEGER, Types.INTEGER));
        List<Object> preparedStmtList = new ArrayList<>(List.of(id, 1, 0));
        String query = getSignQuery();
        List<ESignParameter> parameter = jdbcTemplate.query(query, preparedStmtList.toArray(), preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(), rowMapper);
        log.info("Method=getESignDetails, Result=Success,id={}", id);

        return parameter.get(0);

    }
}
