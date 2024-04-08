package org.pucar.repository.querybuilder;

import org.pucar.web.models.AdvocateSearchCriteria;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AdvocateRegistrationQueryBuilder {
    private static final String ADV_QUERY = " SELECT adv.id as aid, adv.tenantId as atenantId, adv.applicationNumber as aapplicationNumber, adv.barRegistrationNumber as abarRegistrationNumber, adv.organisationID as aorganisationID, adv.individualId as aindividualId, adv.isActive as aisActive, adv.additionalDetails as aadditionalDetails, adv.createdBy as acreatedBy, adv.lastmodifiedby as alastmodifiedby, adv.createdtime as acreatedtime, adv.lastmodifiedtime as alastmodifiedtime";
    private static final String FROM_TABLES = " FROM dristi_advocate adv";

    public String getAdvocateSearchQuery(List<AdvocateSearchCriteria> criteriaList, List<Object> preparedStmtList) {
        StringBuilder query = new StringBuilder(ADV_QUERY);
        query.append(FROM_TABLES);

        try {
            boolean firstCriteria = true; // To check if it's the first criteria

            // Collecting ids, application numbers, and bar registration numbers
            List<String> ids = criteriaList.stream()
                    .map(AdvocateSearchCriteria::getId)
                    .filter(Objects::nonNull)
                    .toList();

            List<String> applicationNumbers = criteriaList.stream()
                    .map(AdvocateSearchCriteria::getApplicationNumber)
                    .filter(Objects::nonNull)
                    .toList();

            List<String> barRegistrationNumbers = criteriaList.stream()
                    .map(AdvocateSearchCriteria::getBarRegistrationNumber)
                    .filter(Objects::nonNull)
                    .toList();

            if (!ids.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("adv.id IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
                firstCriteria = false; // Update firstCriteria flag
            }

            if (!barRegistrationNumbers.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("adv.barRegistrationNumber IN (")
                        .append(barRegistrationNumbers.stream().map(reg -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(barRegistrationNumbers);
                firstCriteria = false; // Update firstCriteria flag

            }

            if (!applicationNumbers.isEmpty()) {
                addClauseIfRequired(query, firstCriteria);
                query.append("adv.applicationNumber IN (")
                        .append(applicationNumbers.stream().map(num -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(applicationNumbers);
            }



            return query.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while building the query", e);
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