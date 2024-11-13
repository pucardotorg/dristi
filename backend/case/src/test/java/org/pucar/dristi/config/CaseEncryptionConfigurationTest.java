package org.pucar.dristi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import com.fasterxml.jackson.databind.DeserializationFeature;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CaseEncryptionConfigurationTest {

    @InjectMocks
    private CaseEncryptionConfiguration config;

    @Test
    void testObjectMapper() {
        ObjectMapper objectMapper = config.objectMapper();

        assertNotNull(objectMapper, "ObjectMapper should not be null");
        assertFalse(objectMapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES), "FAIL_ON_UNKNOWN_PROPERTIES should be disabled");
        assertTrue(objectMapper.getRegisteredModuleIds().stream()
                .anyMatch(id -> id.equals(new JavaTimeModule().getTypeId())), "JavaTimeModule should be registered");
    }

    @Test
    void testJacksonConverter() {
        MappingJackson2HttpMessageConverter converter = config.jacksonConverter();

        assertNotNull(converter, "MappingJackson2HttpMessageConverter should not be null");
        ObjectMapper objectMapper = converter.getObjectMapper();
        assertNotNull(objectMapper, "ObjectMapper in converter should not be null");
        assertFalse(objectMapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES),
                "FAIL_ON_UNKNOWN_PROPERTIES should be disabled in the ObjectMapper of the converter");
        assertTrue(objectMapper.getRegisteredModuleIds().stream()
                .anyMatch(id -> id.equals(new JavaTimeModule().getTypeId())), "JavaTimeModule should be registered");
    }
}