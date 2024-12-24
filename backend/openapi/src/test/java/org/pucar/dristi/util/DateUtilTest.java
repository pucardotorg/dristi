package org.pucar.dristi.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DateUtilTest {

    private final DateUtil dateUtil = new DateUtil();

    @Test
    public void shouldReturnStartAndEndOfYear2021InSeconds() {
        List<Long> result = dateUtil.getYearInSeconds(2021);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1609459200000L, result.get(0));
        assertEquals(1640995199999L, result.get(1));
    }

    @Test
    public void shouldThrowExceptionWhenYearIsNull() {
        assertThrows(RuntimeException.class, () -> dateUtil.getYearInSeconds(null));
    }

    @Test
    public void shouldHandleEdgeCaseYears() {
        List<Long> result1970 = dateUtil.getYearInSeconds(1970);
        assertEquals(0, result1970.get(0));
        // Test leap year
        List<Long> result2024 = dateUtil.getYearInSeconds(2024);
        assertNotNull(result2024);
        assertEquals(2, result2024.size());
        assertEquals(1704067200000L, result2024.get(0)); // 2024-01-01 00:00:00
        assertEquals(1735689599999L, result2024.get(1)); // 2024-12-31 23:59:59
        }
}
