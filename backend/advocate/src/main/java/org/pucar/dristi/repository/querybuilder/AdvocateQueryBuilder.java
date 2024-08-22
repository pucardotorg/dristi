package org.pucar.dristi.repository.querybuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class AdvocateQueryBuilder {
    private static final String BASE_ATR_QUERY = " SELECT adv.id as id, adv.tenantid as tenantid, adv.applicationnumber as applicationnumber, adv.barregistrationnumber as barregistrationnumber, adv.advocateType as advocatetype, adv.organisationID as organisationid, adv.individualid as individualid, adv.isactive as isactive, adv.additionaldetails as additionaldetails, adv.createdby as createdby, adv.lastmodifiedby as lastmodifiedby, adv.createdtime as createdtime, adv.lastmodifiedtime as lastmodifiedtime, adv.status as status ";

    private static final String DOCUMENT_SELECT_QUERY = "Select doc.id as aid, doc.documenttype as documenttype, doc.filestore as filestore, doc.documentuid as documentuid, doc.additionaldetails as docadditionaldetails, doc.advocateid as advocateid ";
    private static final String FROM_ADVOCATES_TABLE = " FROM dristi_advocate adv";
    private static final String FROM_DOCUMENTS_TABLE = " FROM dristi_document doc";
    private static final String ORDERBY_CREATEDTIME_DESC = " ORDER BY adv.createdtime DESC ";
    private static final String LIMIT_OFFSET = " LIMIT ? OFFSET ?";

    /**   /** To build query using search criteria to search advocate
     * @param preparedStmtList
     * @param limit
     * @param offset
     * @return
     */
    public String getAdvocateSearchQuery(AdvocateSearchCriteria criteria, List<Object> preparedStmtList,List<Integer> preparedStmtArgList, String tenantId, Integer limit, Integer offset) {
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            query.append(FROM_ADVOCATES_TABLE);
            boolean firstCriteria = true;

            if (criteria != null) {
                firstCriteria = addCriteriaToQuery(criteria, query, preparedStmtList,preparedStmtArgList, firstCriteria);

                if(tenantId != null && !tenantId.isEmpty()){
                    addClauseIfRequiredForTenantId(query, firstCriteria);
                    query.append("LOWER(adv.tenantid) = LOWER(?)");
                    preparedStmtList.add(tenantId.toLowerCase());
                    preparedStmtArgList.add(Types.VARCHAR);
                }
            }

            query.append(ORDERBY_CREATEDTIME_DESC);

            // Adding Pagination
            addPagination(preparedStmtList,preparedStmtArgList,limit,offset,query);

            return query.toString();
        }
        catch (Exception e) {
            log.error(ADVOCATE_SEARCH_QUERY_BUILD_EXCEPTION, e.toString());
            throw new CustomException(ADVOCATE_SEARCH_QUERY_EXCEPTION, ADVOCATE_SEARCH_QUERY_BUILD_EXCEPTION + e.getMessage());
        }
    }

    private boolean addCriteriaToQuery(AdvocateSearchCriteria criteria, StringBuilder query, List<Object> preparedStmtList, List<Integer> preparedStmtArgList,boolean firstCriteria) {
        firstCriteria = addSingleCriteria(criteria.getId(), "adv.id = ?", query, preparedStmtList,preparedStmtArgList, firstCriteria);
        firstCriteria = addSingleCriteria(criteria.getBarRegistrationNumber(), "adv.barRegistrationNumber = ?", query, preparedStmtList,preparedStmtArgList, firstCriteria);
        firstCriteria = addSingleCriteria(criteria.getApplicationNumber(), "adv.applicationNumber = ?", query, preparedStmtList, preparedStmtArgList,firstCriteria);
        firstCriteria = addSingleCriteria(criteria.getIndividualId(), "adv.individualId = ?", query, preparedStmtList,preparedStmtArgList, firstCriteria);

        return firstCriteria;
    }

    private boolean addSingleCriteria(String value, String column, StringBuilder query, List<Object> preparedStmtList, List<Integer> preparedStmtArgsList, boolean firstCriteria) {
        if (value != null && !value.isEmpty()) {
            addClauseIfRequired(query, firstCriteria);
            query.append(column);
            preparedStmtList.add(value);
            preparedStmtArgsList.add(Types.VARCHAR);
            firstCriteria = false;
        }
        return firstCriteria;
    }

    public String getAdvocateSearchQueryByStatus(String status, List<Object> preparedStmtList, List<Integer> preparedStmtArguList, String tenantId, Integer limit, Integer offset){
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            query.append(FROM_ADVOCATES_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria

            if(status != null && !status.isEmpty()){
                addClauseIfRequiredForStatus(query, firstCriteria);
                query.append("LOWER(adv.status) LIKE LOWER(?)")
                        .append(")");
                preparedStmtList.add(status.toLowerCase());
                preparedStmtArguList.add(Types.VARCHAR);
                firstCriteria = false;
            }
            if(tenantId != null && !tenantId.isEmpty()){
                addClauseIfRequiredForTenantId(query, firstCriteria);
                query.append("LOWER(adv.tenantid) LIKE LOWER(?)");
                preparedStmtList.add(tenantId.toLowerCase());
                preparedStmtArguList.add(Types.VARCHAR);
            }

            query.append(ORDERBY_CREATEDTIME_DESC);

            // Adding Pagination
            addPagination(preparedStmtList,preparedStmtArguList,limit,offset,query);

            return query.toString();
        }
        catch (Exception e) {
            log.error("Error while building advocate search by status query :: {}",e.toString());
            throw new CustomException(ADVOCATE_SEARCH_QUERY_EXCEPTION,ADVOCATE_SEARCH_QUERY_BUILD_EXCEPTION+ e.getMessage());
        }
    }

    public String getAdvocateSearchQueryByApplicationNumber(String applicationNumber, List<Object> preparedStmtList,List<Integer> preparedStmtArgssList, String tenantId, Integer limit, Integer offset){
        try {
            StringBuilder query = new StringBuilder(BASE_ATR_QUERY);
            query.append(FROM_ADVOCATES_TABLE);
            boolean firstCriteria = true; // To check if it's the first criteria

            if(applicationNumber != null && !applicationNumber.isEmpty()){
                addClauseIfRequiredForStatus(query, firstCriteria);
                query.append("LOWER(adv.applicationnumber) LIKE LOWER(?)")
                        .append(")");
                preparedStmtList.add("%" + applicationNumber.toLowerCase() + "%");
                preparedStmtArgssList.add(Types.VARCHAR);
                firstCriteria = false;
            }
            if(tenantId != null && !tenantId.isEmpty()){
                addClauseIfRequiredForTenantId(query, firstCriteria);
                query.append("LOWER(adv.tenantid) LIKE LOWER(?)");
                preparedStmtList.add("%" + tenantId.toLowerCase() + "%");
                preparedStmtArgssList.add(Types.VARCHAR);
            }

            query.append(ORDERBY_CREATEDTIME_DESC);

            // Adding Pagination
            addPagination(preparedStmtList,preparedStmtArgssList,limit,offset,query);

            return query.toString();
        }
        catch (Exception e) {
            log.error("Error while building advocate search by app num query :: {}",e.toString());
            throw new CustomException(ADVOCATE_SEARCH_QUERY_EXCEPTION,ADVOCATE_SEARCH_QUERY_BUILD_EXCEPTION+ e.getMessage());
        }
    }

    void addClauseIfRequired(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(AND);
        }
    }

    void addClauseIfRequiredForStatus(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ( ");
        } else {
            query.append(AND);
        }
    }

    void addClauseIfRequiredForTenantId(StringBuilder query, boolean isFirstCriteria) {
        if (isFirstCriteria) {
            query.append(" WHERE ");
        } else {
            query.append(AND);
        }
    }

    private void addPagination(List<Object> preparedStmtList,List<Integer> preparedStmtArgumentList, Integer limit, Integer offset, StringBuilder query){
        if (limit != null && offset != null) {
            query.append(LIMIT_OFFSET);
            preparedStmtList.add(limit);
            preparedStmtArgumentList.add(Types.INTEGER);

            preparedStmtList.add(offset);
            preparedStmtArgumentList.add(Types.INTEGER);

        }
    }

    public String getDocumentSearchQuery(List<String> ids, List<Object> preparedStmtList,List<Integer> preparedStmtArgDocList) {
        try {
            StringBuilder query = new StringBuilder(DOCUMENT_SELECT_QUERY);
            query.append(FROM_DOCUMENTS_TABLE);
            if (!ids.isEmpty()) {
                query.append(" WHERE doc.advocateid IN (")
                        .append(ids.stream().map(id -> "?").collect(Collectors.joining(",")))
                        .append(")");
                preparedStmtList.addAll(ids);
                ids.forEach(i->preparedStmtArgDocList.add(Types.VARCHAR));
            }

            return query.toString();
        } catch (Exception e) {
            log.error("Error while building document search query :: {}",e.toString());
            throw new CustomException(DOCUMENT_SEARCH_QUERY_EXCEPTION,"Exception occurred while building the query: "+ e.getMessage());
        }
    }
}