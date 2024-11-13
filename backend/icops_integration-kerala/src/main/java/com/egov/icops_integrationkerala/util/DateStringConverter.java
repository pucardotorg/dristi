package com.egov.icops_integrationkerala.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class DateStringConverter {

    public String convertDate(String originalDate) {
        // Define the original date format
        DateTimeFormatter originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Define the new date format
        DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Parse the original date string to a LocalDate object
        LocalDate date = LocalDate.parse(originalDate, originalFormat);
        // Format the LocalDate object to the new date string
        return date.format(newFormat);
    }

    public String convertLongToDate(Long timestamp) {
        // Define the new date format
        DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Instant instant = null;
        if(timestamp == null){
            instant = Instant.now();
        } else {
            instant = Instant.ofEpochMilli(timestamp);
        }
        // Convert Instant to LocalDate using UTC time zone
        LocalDate localDate = instant.atZone(ZoneId.of("UTC")).toLocalDate();

        // Format the LocalDate object to the new date string
        return localDate.format(newFormat);
    }
}
