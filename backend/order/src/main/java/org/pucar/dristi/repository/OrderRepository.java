package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.querybuilder.OrderQueryBuilder;
import org.pucar.dristi.repository.rowmapper.*;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.StatuteSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Repository
public class OrderRepository {

    @Autowired
    private OrderQueryBuilder queryBuilder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private OrderRowMapper rowMapper;

    @Autowired
    private DocumentRowMapper documentRowMapper;

    @Autowired
    private StatuteSectionRowMapper statuteSectionRowMapper;

    public List<Order> getApplications(String applicationNumber, String cnrNumber, String filingNumber, String tenantId, String id, String status) {

        try {
            List<Order> orderList = new ArrayList<>();
            List<Object> preparedStmtListDoc = new ArrayList<>();
            String orderQuery = "";
            orderQuery = queryBuilder.getOrderSearchQuery(applicationNumber,cnrNumber,filingNumber, tenantId, id, status);
            log.info("Final order query: {}", orderQuery);
            List<Order> list = jdbcTemplate.query(orderQuery, rowMapper);
            log.info("DB order list :: {}", list);
            if (list != null) {
                orderList.addAll(list);
            }

            List<String> ids = new ArrayList<>();
            for (Order order : orderList) {
                ids.add(order.getId().toString());
            }
            if (ids.isEmpty()) {
                return orderList;
            }

            String statueAndSectionQuery = "";
            preparedStmtListDoc = new ArrayList<>();
            statueAndSectionQuery = queryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtListDoc);
            log.info("Final statue and sections query: {}", statueAndSectionQuery);
            Map<UUID, StatuteSection> statuteSectionsMap = jdbcTemplate.query(statueAndSectionQuery, preparedStmtListDoc.toArray(), statuteSectionRowMapper);
            if (statuteSectionsMap != null) {
                orderList.forEach(st -> {
                    st.setStatuteSection(statuteSectionsMap.get(st.getId()));
                });
            }

            String documentQuery = "";
            documentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
            log.info("Final document query: {}", documentQuery);
            Map<UUID, List<Document>> documentMap = jdbcTemplate.query(documentQuery, preparedStmtListDoc.toArray(), documentRowMapper);
            if (documentMap != null) {
                orderList.forEach(order -> {
                    order.setDocuments(documentMap.get(order.getId()));
                });
            }

            return orderList;
        }
        catch (CustomException e){
            log.error("Custom Exception while fetching order list");
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching order list");
            throw new CustomException("ORDER_SEARCH_EXCEPTION","Error while fetching order list: "+e.getMessage());
        }
    }

}