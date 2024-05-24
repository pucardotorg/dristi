package org.pucar.dristi.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;

import java.util.TimeZone;

@SpringBootTest
@ActiveProfiles("test") // Use the test profile
public class MainConfigurationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonConverter;

    @Value("${app.timezone}")
    private String timeZone;

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
