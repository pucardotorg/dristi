package org.pucar.dristi.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.model.EPostTrackerSearchCriteria;
import org.pucar.dristi.model.Pagination;
import org.pucar.dristi.model.Sort;
import org.pucar.dristi.model.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EPostQueryBuilderTest {

    private final EPostQueryBuilder queryBuilder = new EPostQueryBuilder();

    @Test
    void testGetEPostTrackerSearchQueryWithAllCriteria() {
        // Arrange
        EPostTrackerSearchCriteria searchCriteria = new EPostTrackerSearchCriteria();
        searchCriteria.setDeliveryStatus("DELIVERED");
        searchCriteria.setProcessNumber("PN123");
        searchCriteria.setTrackingNumber("TN123");
        searchCriteria.setBookingDate("2024-01-01");
        searchCriteria.setReceivedDate("2024-01-02");

        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getEPostTrackerSearchQuery(searchCriteria, preparedStmtList);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains(" delivery_status = ? "));
        assertTrue(query.contains(" process_number = ? "));
        assertTrue(query.contains(" tracking_number = ? "));
        assertTrue(query.contains(" booking_date = ? "));
        assertTrue(query.contains(" received_date = ? "));
        assertEquals(5, preparedStmtList.size());  // 1 for deliveryStatus, 2 for deliveryStatusList, 1 for processNumber, 1 for trackingNumber, 1 for bookingDate, 1 for receivedDate
    }

    @Test
    void testGetEPostTrackerSearchQueryWithDeliveryStatusCriteria() {
        // Arrange
        EPostTrackerSearchCriteria searchCriteria = new EPostTrackerSearchCriteria();
        searchCriteria.setDeliveryStatusList(Arrays.asList("Delivered", "Pending"));


        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String query = queryBuilder.getEPostTrackerSearchQuery(searchCriteria, preparedStmtList);

        // Assert
        assertNotNull(query);
        assertTrue(query.contains("delivery_status in (  ?, ? )"));
        assertEquals(2, preparedStmtList.size());  // 1 for deliveryStatus, 2 for deliveryStatusList, 1 for processNumber, 1 for trackingNumber, 1 for bookingDate, 1 for receivedDate
    }

    @Test
    void testGetEPostTrackerSearchQueryWithNoCriteria() {
        // Arrange
        EPostTrackerSearchCriteria searchCriteria = EPostTrackerSearchCriteria.builder().build();
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        String actualQuery = queryBuilder.getEPostTrackerSearchQuery(searchCriteria, preparedStmtList);

        // Define expected query
        String expectedQuery = "SELECT process_number, tenant_id, file_store_id, task_number, tracking_number, pincode, address, delivery_status, remarks, additional_details, row_version, booking_date, received_date, createdBy, lastModifiedBy, createdTime, lastModifiedTime FROM dristi_epost_tracker ";

        // Normalize whitespace before assertion
        String normalizedExpected = expectedQuery.trim().replaceAll("\\s+", " ");
        String normalizedActual = actualQuery.trim().replaceAll("\\s+", " ");


        // Assert
        assertNotNull(actualQuery);
        assertEquals(normalizedExpected, normalizedActual);
        assertTrue(preparedStmtList.isEmpty());
    }


    @Test
    void testAddPaginationQueryWithSortingAndOrdering() {
        // Arrange
        String baseQuery = "SELECT * FROM dristi_epost_tracker";
        Pagination pagination = Pagination.builder()
                .sortBy(Sort.PROCESS_NUMBER)
                .orderBy(Order.ASC)
                .build();

        List<Object> preparedStmtList = new ArrayList<>();
        int limit = 10;
        int offset = 0;

        // Act
        String queryWithPagination = queryBuilder.addPaginationQuery(baseQuery, preparedStmtList, pagination, limit, offset);

        // Assert
        assertNotNull(queryWithPagination);
        assertTrue(queryWithPagination.contains(" ORDER BY PROCESS_NUMBER ASC "));
        assertTrue(queryWithPagination.contains(" LIMIT ? OFFSET ?"));
        assertEquals(2, preparedStmtList.size()); // 1 for limit, 1 for offset
        assertEquals(10, preparedStmtList.get(0));
        assertEquals(0, preparedStmtList.get(1));
    }

    @Test
    void testAddPaginationQueryWithDefaultValues() {
        // Arrange
        String baseQuery = "SELECT * FROM dristi_epost_tracker";
        Pagination pagination = Pagination.builder() // No sortBy or orderBy set
                .build();

        List<Object> preparedStmtList = new ArrayList<>();
        int limit = 10;
        int offset = 0;

        // Act
        String queryWithPagination = queryBuilder.addPaginationQuery(baseQuery, preparedStmtList, pagination, limit, offset);

        // Assert
        assertNotNull(queryWithPagination);
        assertTrue(queryWithPagination.contains(" ORDER BY createdtime DESC "));
        assertTrue(queryWithPagination.contains(" LIMIT ? OFFSET ?"));
        assertEquals(2, preparedStmtList.size()); // 1 for limit, 1 for offset
        assertEquals(10, preparedStmtList.get(0));
        assertEquals(0, preparedStmtList.get(1));
    }

    @Test
    void testGetTotalCountQuery() {
        // Arrange
        String baseQuery = "SELECT * FROM dristi_epost_tracker WHERE process_number = ?";

        // Act
        String countQuery = queryBuilder.getTotalCountQuery(baseQuery);

        // Assert
        assertNotNull(countQuery);
        assertEquals("SELECT COUNT(*) FROM (SELECT * FROM dristi_epost_tracker WHERE process_number = ?) total_result", countQuery);
    }
}
