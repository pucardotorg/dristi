package org.pucar.dristi.repository;

import static org.pucar.dristi.config.ServiceConstants.SEARCH_WITNESS_ERR;

import java.util.ArrayList;
import java.util.List;

import io.swagger.models.auth.In;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.querybuilder.WitnessQueryBuilder;
import org.pucar.dristi.repository.rowmapper.WitnessRowMapper;
import org.pucar.dristi.web.models.Witness;
import org.pucar.dristi.web.models.WitnessSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Repository
public class WitnessRepository {

    private WitnessQueryBuilder queryBuilder;

    private JdbcTemplate jdbcTemplate;

    private WitnessRowMapper rowMapper;

    @Autowired
    public WitnessRepository(WitnessQueryBuilder queryBuilder, JdbcTemplate jdbcTemplate, WitnessRowMapper rowMapper) {
        this.queryBuilder = queryBuilder;
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    public List<Witness> getApplications(List<WitnessSearchCriteria> searchCriteria) {

        try {
            List<Witness> witnessList = new ArrayList<>();
            List<Object> preparedStmtList = new ArrayList<>();
            List<Integer> preparedStmtArgsList= new ArrayList<>();
            String casesQuery = "";
            casesQuery = queryBuilder.getWitnessesSearchQuery(searchCriteria, preparedStmtList, preparedStmtArgsList);
            log.info("Final case query :: {}", casesQuery);
            List<Witness> list = jdbcTemplate.query(casesQuery, preparedStmtList.toArray(), preparedStmtArgsList.stream().mapToInt(Integer::intValue).toArray(), rowMapper);
            if (list != null) {
                witnessList.addAll(list);
            }

            return witnessList;
        } catch(CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching witness application list :: {}", e.toString());
            throw new CustomException(SEARCH_WITNESS_ERR,"Exception while fetching witness application list: "+e.getMessage());
        }
    }

}