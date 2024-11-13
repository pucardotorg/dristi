package digit.repository.querybuilder;

import digit.web.models.SummonsDeliverySearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SummonsDeliveryQueryBuilderTest {

    @InjectMocks
    private SummonsDeliveryQueryBuilder queryBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSummonsQueryWithOrderId() {
        SummonsDeliverySearchCriteria searchCriteria = new SummonsDeliverySearchCriteria();
        searchCriteria.setTaskNumber("order123");
        List<String> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT summons_delivery_id, task_number, case_id, tenant_id, doc_type, doc_sub_type, party_type, channel_name, payment_fees, payment_transaction_id, payment_status, is_accepted_by_channel, channel_acknowledgement_id, delivery_request_date, delivery_status, additional_fields, created_by, last_modified_by, created_time, last_modified_time, row_version  FROM summons_delivery  WHERE  task_number = ? ";
        String actualQuery = queryBuilder.getSummonsQuery(searchCriteria, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(1, preparedStmtList.size());
        assertEquals("order123", preparedStmtList.get(0));
    }

    @Test
    void testGetSummonsQueryWithSummonsId() {
        SummonsDeliverySearchCriteria searchCriteria = new SummonsDeliverySearchCriteria();
        searchCriteria.setTaskNumber("summons123");
        List<String> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT summons_delivery_id, task_number, case_id, tenant_id, doc_type, doc_sub_type, party_type, channel_name, payment_fees, payment_transaction_id, payment_status, is_accepted_by_channel, channel_acknowledgement_id, delivery_request_date, delivery_status, additional_fields, created_by, last_modified_by, created_time, last_modified_time, row_version  FROM summons_delivery  WHERE  task_number = ? ";
        String actualQuery = queryBuilder.getSummonsQuery(searchCriteria, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(1, preparedStmtList.size());
        assertEquals("summons123", preparedStmtList.get(0));
    }

    @Test
    void testGetSummonsQueryWithOrderIdAndSummonsId() {
        SummonsDeliverySearchCriteria searchCriteria = new SummonsDeliverySearchCriteria();
        searchCriteria.setTaskNumber("order123");
        searchCriteria.setSummonsDeliveryId("summons123");
        List<String> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT summons_delivery_id, task_number, case_id, tenant_id, doc_type, doc_sub_type, party_type, channel_name, payment_fees, payment_transaction_id, payment_status, is_accepted_by_channel, channel_acknowledgement_id, delivery_request_date, delivery_status, additional_fields, created_by, last_modified_by, created_time, last_modified_time, row_version  FROM summons_delivery  WHERE  summons_delivery_id = ?  AND  task_number = ? ";
        String actualQuery = queryBuilder.getSummonsQuery(searchCriteria, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(2, preparedStmtList.size());
        assertEquals("summons123", preparedStmtList.get(0));
        assertEquals("order123", preparedStmtList.get(1));
    }

    @Test
    void testGetSummonsQueryWithoutCriteria() {
        SummonsDeliverySearchCriteria searchCriteria = new SummonsDeliverySearchCriteria();
        List<String> preparedStmtList = new ArrayList<>();

        String expectedQuery = "SELECT summons_delivery_id, task_number, case_id, tenant_id, doc_type, doc_sub_type, party_type, channel_name, payment_fees, payment_transaction_id, payment_status, is_accepted_by_channel, channel_acknowledgement_id, delivery_request_date, delivery_status, additional_fields, created_by, last_modified_by, created_time, last_modified_time, row_version  FROM summons_delivery ";
        String actualQuery = queryBuilder.getSummonsQuery(searchCriteria, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertTrue(preparedStmtList.isEmpty());
    }
}
