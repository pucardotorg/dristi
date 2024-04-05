package org.pucar.repository.querybuilder;

import org.pucar.web.models.AdvocateSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class AdvocateRegistrationQueryBuilder {
    private static final String ADV_QUERY = " SELECT adv.id as aid, adv.tenantId as atenantId, adv.applicationNumber as aapplicationNumber, adv.barRegistrationNumber as abarRegistrationNumber, adv.organisationID as aorganisationID, adv.individualId as aindividualId, adv.isActive as aisActive, adv.additionalDetails as aadditionalDetails, adv.createdBy as acreatedBy, adv.lastmodifiedby as alastmodifiedby, adv.createdtime as acreatedtime, adv.lastmodifiedtime as alastmodifiedtime ";
    private static final String FROM_TABLES = " FROM dristi_advocate adv ";

    public String getAdvocateSearchQuery(AdvocateSearchCriteria criteria, List<Object> preparedStmtList) {
        StringBuilder query = new StringBuilder(ADV_QUERY);
        query.append(FROM_TABLES);

        try {
            if (!ObjectUtils.isEmpty(criteria.getApplicationNumber())) {
                addClauseIfRequired(query, preparedStmtList);
                query.append(" adv.id = ? ");
                preparedStmtList.add(criteria.getId());
            }
            if (!ObjectUtils.isEmpty(criteria.getApplicationNumber())) {
                addClauseIfRequired(query, preparedStmtList);
                query.append(" adv.applicationNumber = ? ");
                preparedStmtList.add(criteria.getApplicationNumber());
            }
            if (!ObjectUtils.isEmpty(criteria.getBarRegistrationNumber())) {
                addClauseIfRequired(query, preparedStmtList);
                query.append(" adv.barRegistrationNumber = ? ");
                preparedStmtList.add(criteria.getBarRegistrationNumber());
            }

            return query.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while building the query", e);
        }
    }

    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
        if (preparedStmtList.isEmpty()) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }
}