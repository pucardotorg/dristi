package org.pucar.dristi.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class MainConfigurationTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MainConfiguration mainConfiguration;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void initialize_shouldSetDefaultTimeZone() {

        String timeZone = "GMT";
        mainConfiguration.setTimeZone(timeZone);
        mainConfiguration.initialize();

        assertEquals(TimeZone.getDefault(), TimeZone.getTimeZone(timeZone));
    }

    @Test
    void objectMapper_shouldReturnConfiguredObjectMapper() {
        String timeZone = "GMT";
        mainConfiguration.setTimeZone(timeZone);
        ObjectMapper returnedObjectMapper = mainConfiguration.objectMapper();
        when(objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)).thenReturn(objectMapper);
        when(objectMapper.setTimeZone(TimeZone.getTimeZone("GMT"))).thenReturn(objectMapper);

        assertNotNull(returnedObjectMapper);
    }

    @Test
    void jacksonConverter_shouldReturnConfiguredJacksonConverter() {
        MappingJackson2HttpMessageConverter converter = mainConfiguration.jacksonConverter(objectMapper);

        assertEquals(converter.getObjectMapper(), objectMapper);
    }
}