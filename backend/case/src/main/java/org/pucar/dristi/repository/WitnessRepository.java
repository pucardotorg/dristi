package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.querybuilder.CaseQueryBuilder;
import org.pucar.dristi.repository.querybuilder.WitnessQueryBuilder;
import org.pucar.dristi.repository.rowmapper.*;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Repository
public class WitnessRepository {

    @Autowired
    private WitnessQueryBuilder queryBuilder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private WitnessRowMapper rowMapper;

    public List<Witness> getApplications(List<WitnessSearchCriteria> searchCriteria) {

        try {
            List<Witness> witnessList = new ArrayList<>();
            List<Object> preparedStmtList = new ArrayList<>();
            String casesQuery = "";
            casesQuery = queryBuilder.getWitnessesSearchQuery(searchCriteria, preparedStmtList);
            log.info("Final case query: {}", casesQuery);
            List<Witness> list = jdbcTemplate.query(casesQuery, preparedStmtList.toArray(), rowMapper);
            if (list != null) {
                witnessList.addAll(list);
            }

            return witnessList;
        }
        catch (CustomException e){
            throw e;
        }
        catch (Exception e){
            e.printStackTrace();
            log.error("Error while fetching advocate application list");
            throw new CustomException("ADVOCATE_SEARCH_EXCEPTION","Error while fetching advocate application list: "+e.getMessage());
        }
    }

}