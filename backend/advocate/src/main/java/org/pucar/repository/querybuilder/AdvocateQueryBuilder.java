package org.pucar.repository.querybuilder;

import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.repository.rowmapper.AdvocateRowMapper;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateSearchCriteria;
import org.pucar.web.models.Order;
import org.pucar.web.models.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.pucar.config.ServiceConstants.ADVOCATE_SEARCH_QUERY_EXCEPTION;
import static org.pucar.config.ServiceConstants.DOCUMENT_SEARCH_QUERY_EXCEPTION;

@Component
@Slf4j
public class AdvocateQueryBuilder {
    private static final String BASE_ATR_QUERY = " SELECT adv.id as id, adv.tenantid as tenantid, adv.applicationnumber as applicationnumber, adv.barregistrationnumber as barregistrationnumber, adv.advocateType as advocatetype, adv.organisationID as organisationid, adv.individualid as individualid, adv.isactive as isactive, adv.additionaldetails as additionaldetails, adv.createdby as createdby, adv.lastmodifiedby as lastmodifiedby, adv.createdtime as createdtime, adv.lastmodifiedtime as lastmodifiedtime, adv.status as status ";

    private static final String DOCUMENT_SELECT_QUERY = "Select doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.advocateid as advocateid ";
    private static final String FROM_ADVOCATES_TABLE = " FROM dristi_advocate adv";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_document doc";
    private static final String ORDERBY_CREATEDTIME_DESC = " ORDER BY adv.createdtime DESC ";
    private static final String ORDERBY_CREATEDTIME_ASC = " ORDER BY adv.createdtime ASC ";

    public String getAdvocateSearchQuery(List<AdvocateSearchCriteria> criteriaList, List<Object> preparedStmtList, List<String> statusList, String applicationNumber, AtomicReference<Boolean> isIndividualLoggedInUser, Integer limit, Integer offset, Pagination pagination) {
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            query.append(FROM_ADVOCATES_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria
            if(criteriaList != null && !criteriaList.isEmpty()) {

                // Collecting ids, application numbers, and bar registration numbers
                List<String> ids = criteriaList.stream()
                        .map(AdvocateSearchCriteria::getId)
                        .filter(Objects::nonNull)
                        .toList();

                List<String> barRegistrationNumbers = criteriaList.stream()
                        .filter(criteria -> criteria.getId() == null)
                        .map(AdvocateSearchCriteria::getBarRegistrationNumber)
                        .filter(Objects::nonNull)
                        .toList();

                List<String> applicationNumbers = criteriaList.stream()
                        .filter(criteria -> criteria.getId() == null && criteria.getBarRegistrationNumber() == null)
                        .map(AdvocateSearchCriteria::getApplicationNumber)
                        .filter(Objects::nonNull)
                        .toList();

                List<String> individualIds = criteriaList.stream()
                        .filter(criteria -> criteria.getId() == null && criteria.getBarRegistrationNumber() == null && criteria.getApplicationNumber() == null)
                        .map(AdvocateSearchCriteria::getIndividualId)
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
                query.append("LOWER(adv.applicationNumber) LIKE LOWER(?)")
                        .append(")");
                preparedStmtList.add("%" + applicationNumber.toLowerCase() + "%");
                firstCriteria = false;
            }
            if (statusList != null && !statusList.isEmpty()) {
                addClauseIfRequiredForStatus(query, firstCriteria);
                query.append("adv.status IN (")
                        .append(statusList.stream().map(num -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(statusList);
            }
            if(isIndividualLoggedInUser.get()){
                query.append(ORDERBY_CREATEDTIME_DESC);
                pagination.setOrder(Order.DESC);
            }
            else {
                query.append(ORDERBY_CREATEDTIME_ASC);
                pagination.setOrder(Order.ASC);
            }
//            List<Advocate> list = jdbcTemplate.query(query.toString(), preparedStmtList.toArray(), rowMapper);
//            Integer totalCount = (list != null) ? list.size() : 0;
//            pagination.setTotalCount(Double.valueOf(totalCount));
//            pagination.setSortBy("createdtime");

            // Adding Pagination
            if (limit != null && offset != null) {
                query.append(" LIMIT ? OFFSET ?");
                preparedStmtList.add(limit);
                preparedStmtList.add(offset);
                pagination.setLimit(Double.valueOf(limit));
                pagination.setOffSet(Double.valueOf(offset));
            }

            return query.toString();
        }
         catch (Exception e) {
            log.error("Error while building advocate search query");
            throw new CustomException(ADVOCATE_SEARCH_QUERY_EXCEPTION,"Error occurred while building the advocate search query: "+ e.getMessage());
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
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.advocateid IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building document search query");
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,"Error occurred while building the query: "+ e.getMessage());
        }
    }
}