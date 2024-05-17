package org.pucar.dristi.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.WitnessSearchCriteria;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class WitnessQueryBuilder {
    private static final String BASE_WITNESS_QUERY = " SELECT witness.id as id, witness.caseid as caseid, witness.filingnumber as filingnumber, witness.cnrnumber as cnrnumber, witness.witnessidentifier as witnessidentifier, witness.individualid as individualid, " +
            " witness.remarks as remarks, witness.isactive as isactive, witness.additionaldetails as additionaldetails, witness.createdby as createdby," +
            " witness.lastmodifiedby as lastmodifiedby, witness.createdtime as createdtime, witness.lastmodifiedtime as lastmodifiedtime ";
    private static final String FROM_WITNESS_TABLE = " FROM dristi_witness witness";
    private static final String ORDERBY_CREATEDTIME = " ORDER BY witness.createdtime DESC ";

    public String getWitnessesSearchQuery(List<WitnessSearchCriteria> criteriaList, List<Object> preparedStmtList) {
        try {
            StringBuilder query = new StringBuilder(BASE_WITNESS_QUERY);
            query.append(FROM_WITNESS_TABLE);

            if(criteriaList != null && !criteriaList.isEmpty()) {
                if(criteriaList.stream().anyMatch(criteria -> criteria.getCaseId() != null)) {
                    addClauseIfRequired(query, true);
                    query.append("witness.caseid IN (")
                            .append(criteriaList.stream()
                                    .map(WitnessSearchCriteria::getCaseId)
                                    .filter(Objects::nonNull)
                                    .map(id -> "?")
                                    .collect(Collectors.joining(",")))
                            .append(")");
                    preparedStmtList.addAll(criteriaList.stream()
                            .map(WitnessSearchCriteria::getCaseId)
                            .filter(Objects::nonNull)
                            .toList());
                }

                if(criteriaList.stream().anyMatch(criteria -> criteria.getIndividualId() != null)) {
                    addClauseIfRequired(query, true);
                    query.append("witness.individualid IN (")
                            .append(criteriaList.stream()
                                    .filter(criteria -> criteria.getCaseId() == null)
                                    .map(WitnessSearchCriteria::getIndividualId)
                                    .filter(Objects::nonNull)
                                    .map(reg -> "?")
                                    .collect(Collectors.joining(",")))
                            .append(")");
                    preparedStmtList.addAll(criteriaList.stream()
                            .filter(criteria -> criteria.getCaseId() == null)
                            .map(WitnessSearchCriteria::getIndividualId)
                            .filter(Objects::nonNull)
                            .toList());
                }

                if(criteriaList.stream().anyMatch(criteria -> criteria.getCaseId() == null && criteria.getIndividualId() == null)) {
                    addClauseIfRequired(query, true);
                    query.append("witness.filingnumber IN (")
                            .append(criteriaList.stream()
                                    .filter(criteria -> criteria.getCaseId() == null && criteria.getIndividualId() == null)
                                    .map(WitnessSearchCriteria::getIncludeInactive)
                                    .filter(Objects::nonNull)
                                    .map(num -> "?")
                                    .collect(Collectors.joining(",")))
                            .append(")");
                    preparedStmtList.addAll(criteriaList.stream()
                            .filter(criteria -> criteria.getCaseId() == null && criteria.getIndividualId() == null)
                            .map(WitnessSearchCriteria::getIncludeInactive)
                            .filter(Objects::nonNull)
                            .toList());
                }
            }

            query.append(ORDERBY_CREATEDTIME);

            return query.toString();
        }
        catch (Exception e) {
            log.error(WITNESS_QUERY_ERROR);
            throw new CustomException(WITNESS_SEARCH_QUERY_EXCEPTION,WITNESS_QUERY_ERROR+ e.getMessage());
        }
    }


    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" OR ");
        }
    }
}