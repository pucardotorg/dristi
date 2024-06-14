package org.pucar.dristi.web.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    public void testToString() {
        Assertions.assertEquals("asc", Order.ASC.toString());
        Assertions.assertEquals("desc", Order.DESC.toString());
    }

    @Test
    public void testFromValue() {
        Assertions.assertEquals(Order.ASC, Order.fromValue("asc"));
        Assertions.assertEquals(Order.DESC, Order.fromValue("desc"));
        Assertions.assertNull(Order.fromValue("invalid"));
    }

    // Add more test cases to achieve higher coverage and edge cases

}
