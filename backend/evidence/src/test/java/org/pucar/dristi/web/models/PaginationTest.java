package org.pucar.dristi.web.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaginationTest {

    private Pagination pagination;

    @BeforeEach
    public void setup() {
        pagination = new Pagination();
    }

    @Test
    public void testDefaultValues() {
        Assertions.assertEquals(10.0, pagination.getLimit());
        Assertions.assertEquals(0.0, pagination.getOffSet());
        Assertions.assertNull(pagination.getTotalCount());
        Assertions.assertNull(pagination.getSortBy());
        Assertions.assertNull(pagination.getOrder());
    }

    @Test
    public void testGettersAndSetters() {
        Double limit = 20.0;
        Double offSet = 5.0;
        Double totalCount = 100.0;
        String sortBy = "name";
        Order order = Order.ASC;

        pagination.setLimit(limit);
        pagination.setOffSet(offSet);
        pagination.setTotalCount(totalCount);
        pagination.setSortBy(sortBy);
        pagination.setOrder(order);

        Assertions.assertEquals(limit, pagination.getLimit());
        Assertions.assertEquals(offSet, pagination.getOffSet());
        Assertions.assertEquals(totalCount, pagination.getTotalCount());
        Assertions.assertEquals(sortBy, pagination.getSortBy());
        Assertions.assertEquals(order, pagination.getOrder());
    }

    // Add more test cases to achieve higher coverage and edge cases

}
