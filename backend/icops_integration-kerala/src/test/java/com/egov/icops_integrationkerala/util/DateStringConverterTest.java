package com.egov.icops_integrationkerala.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DateStringConverterTest {

    @Test
    void testConvertDate() {
        DateStringConverter dateStringConverter = new DateStringConverter();
        String originalDate = "2021-01-01";
        String convertedDate = dateStringConverter.convertDate(originalDate);
        assertEquals("01/01/2021", convertedDate);
    }
}
