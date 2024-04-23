package org.pucar.repository.querybuilder;

import org.pucar.web.models.AdvocateSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AdvocateQueryBuilder {
    private static final String BASE_ATR_QUERY = " SELECT adv.id as id, adv.tenantid as tenantid, adv.applicationnumber as applicationnumber, adv.barregistrationnumber as barregistrationnumber, adv.advocateType as advocatetype, adv.organisationID as organisationid, adv.individualid as individualid, adv.isactive as isactive, adv.additionaldetails as additionaldetails, adv.createdby as createdby, adv.lastmodifiedby as lastmodifiedby, adv.createdtime as createdtime, adv.lastmodifiedtime as lastmodifiedtime, adv.status as status ";

    private static final String DOCUMENT_SELECT_QUERY = "Select doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.advocateid as advocateid ";
    private static final String FROM_ADVOCATES_TABLE = " FROM dristi_advocate adv";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_document doc";
    private final String ORDERBY_CREATEDTIME = " ORDER BY adv.createdtime DESC ";

    public String getAdvocateSearchQuery(List<AdvocateSearchCriteria> criteriaList, List<Object> preparedStmtList, List<String> statusList, String applicationNumber) {
        StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
        query.append(FROM_ADVOCATES_TABLE);

        try {
            boolean firstCriteria = true; // To check if it's the first criteria
            if(criteriaList != null) {
                // Collecting ids, application numbers, and bar registration numbers
                List<String> ids = criteriaList.stream()
                        .map(AdvocateSearchCriteria::getId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                List<String> barRegistrationNumbers = criteriaList.stream()
                        .filter(criteria -> criteria.getId() == null)
                        .map(AdvocateSearchCriteria::getBarRegistrationNumber)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                List<String> applicationNumbers = criteriaList.stream()
                        .filter(criteria -> criteria.getId() == null && criteria.getBarRegistrationNumber() == null)
                        .map(AdvocateSearchCriteria::getApplicationNumber)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                List<String> individualIds = criteriaList.stream()
                        .filter(criteria -> criteria.getId() == null && criteria.getBarRegistrationNumber() == null && criteria.getApplicationNumber() == null)
                        .map(AdvocateSearchCriteria::getIndividualId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

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
                    firstCriteria = false;
                }

                if (!individualIds.isEmpty()) {
                    addClauseIfRequired(query, firstCriteria);
                    query.append("adv.individualId IN (")
                            .append(individualIds.stream().map(num -> "?").collect(Collectors.joining(",")))
                            .append(")");
                    preparedStmtList.addAll(individualIds);
                    firstCriteria = false;
                }

                if (!CollectionUtils.isEmpty(ids) || !CollectionUtils.isEmpty(barRegistrationNumbers) || !CollectionUtils.isEmpty(applicationNumbers) || !CollectionUtils.isEmpty(individualIds)) {
                    query.append(")");
                }
            }
            else if(applicationNumber != null && !applicationNumber.isEmpty()){
                addClauseIfRequired(query, firstCriteria);
                query.append("adv.applicationNumber LIKE ?")
                     .append(")");
                preparedStmtList.add("%" + applicationNumber + "%");
                firstCriteria = false;
            }


            if (statusList!=null && !statusList.isEmpty()) {
                addClauseIfRequiredForStatus(query, firstCriteria);
                query.append("adv.status IN (")
                        .append(statusList.stream().map(num -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(statusList);
            }
            query.append(ORDERBY_CREATEDTIME);

            return query.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while building the query", e);
        }
    }

    private void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE (");
        } else {
            query.append(" OR ");
        }
    }

    private void addClauseIfRequiredForStatus(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }

    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList) {
        StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY);
        query.append(FROM_DOCUMENTS_TABLE);

        try {
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.advocateid IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while building the query", e);
        }
    }
}