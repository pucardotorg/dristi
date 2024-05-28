package org.pucar.dristi.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.TimeZone;

public class MainConfigurationTest {

    private ObjectMapper objectMapper;
    private MappingJackson2HttpMessageConverter jacksonConverter;
    private String timeZone = "UTC"; // Assuming you want to test with UTC as the timezone

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));

        jacksonConverter = new MappingJackson2HttpMessageConverter();
        jacksonConverter.setObjectMapper(objectMapper);
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(objectMapper);
        assertEquals(TimeZone.getTimeZone(timeZone), objectMapper.getSerializationConfig().getTimeZone());
        assertEquals(TimeZone.getTimeZone(timeZone), objectMapper.getDeserializationConfig().getTimeZone());
    }

    @Test
    public void testJacksonConverter() {
        assertNotNull(jacksonConverter);
        assertEquals(objectMapper, jacksonConverter.getObjectMapper());
    }

    @Test
    public void testTimeZoneInitialization() {
        assertEquals(TimeZone.getTimeZone(timeZone), TimeZone.getTimeZone("UTC"));
    }
}