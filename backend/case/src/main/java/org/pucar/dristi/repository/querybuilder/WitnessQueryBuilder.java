package org.pucar.dristi.repository.querybuilder;

import static org.pucar.dristi.config.ServiceConstants.WITNESS_SEARCH_QUERY_EXCEPTION;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.WitnessSearchCriteria;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WitnessQueryBuilder {
    private static final String BASE_WITNESS_QUERY = " SELECT witness.id as id, witness.caseid as caseid, witness.filingnumber as filingnumber, witness.cnrnumber as cnrnumber, witness.witnessidentifier as witnessidentifier, witness.individualid as individualid, " +
            " witness.remarks as remarks, witness.isactive as isactive, witness.additionaldetails as additionaldetails, witness.createdby as createdby," +
            " witness.lastmodifiedby as lastmodifiedby, witness.createdtime as createdtime, witness.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_WITNESS_TABLE = " FROM dristi_witness witness";
    private static final String ORDERBY_CREATEDTIME = " ORDER BY witness.createdtime DESC ";

    public String getWitnessesSearchQuery(List<WitnessSearchCriteria> criteriaList, List<Object> preparedStmtList, List<Integer> preparedStmtArgsList) {
        try {
            StringBuilder query = new StringBuilder(BASE_WITNESS_QUERY);
            query.append(FROM_WITNESS_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria
            if(criteriaList != null && !criteriaList.isEmpty()) {

                List<String> ids = criteriaList.stream()
                        .map(WitnessSearchCriteria::getCaseId)
                        .filter(Objects::nonNull)
                        .toList();

                List<String> individualIds = criteriaList.stream()
                        .filter(criteria -> criteria.getCaseId() == null)
                        .map(WitnessSearchCriteria::getIndividualId)
                        .filter(Objects::nonNull)
                        .toList();

                List<Boolean> includeInactives = criteriaList.stream()
                        .filter(criteria -> criteria.getCaseId() == null && criteria.getIndividualId() == null)
                        .map(WitnessSearchCriteria::getIncludeInactive)
                        .filter(Objects::nonNull)
                        .toList();

                if (!ids.isEmpty()) {
                    addClauseIfRequired(query, firstCriteria);
                    query.append("witness.caseid IN (")
                            .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                            .append(")");
                    preparedStmtList.addAll(ids);
                    preparedStmtArgsList.addAll(ids.stream().map(id -> java.sql.Types.VARCHAR).toList());
                    firstCriteria = false; // Update firstCriteria flag
                }

                if (!individualIds.isEmpty()) {
                    addClauseIfRequired(query, firstCriteria);
                    query.append("witness.individualid IN (")
                            .append(individualIds.stream().map(reg -> "?").collect(Collectors.joining(",")))
                            .append(")");
                    preparedStmtList.addAll(individualIds);
                    preparedStmtArgsList.addAll(individualIds.stream().map(id -> java.sql.Types.VARCHAR).toList());
                    firstCriteria = false; // Update firstCriteria flag

                }

                if (!includeInactives.isEmpty()) {
                    addClauseIfRequired(query, firstCriteria);
                    query.append("witness.filingnumber IN (")
                            .append(includeInactives.stream().map(num -> "?").collect(Collectors.joining(",")))
                            .append(")");
                    preparedStmtList.addAll(includeInactives);
                    preparedStmtArgsList.addAll(includeInactives.stream().map(id -> java.sql.Types.BOOLEAN).toList());
                }

            }

            query.append(ORDERBY_CREATEDTIME);

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building witness search query :: {}",e.toString());
            throw new CustomException(WITNESS_SEARCH_QUERY_EXCEPTION,"Exception occurred while building the witness search query: "+ e.getMessage());
        }
    }

    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }
}