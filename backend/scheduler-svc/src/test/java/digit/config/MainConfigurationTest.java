package digit.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainConfigurationTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MainConfiguration mainConfiguration;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mainConfiguration, "timeZone", "UTC");
        mainConfiguration.initialize();
    }

    @Test
    void testInitialize() {
        TimeZone expectedTimeZone = TimeZone.getTimeZone("UTC");
        assertEquals(expectedTimeZone, TimeZone.getDefault());
    }

    @Test
    void testObjectMapper() {
        ObjectMapper mapper = mainConfiguration.objectMapper();
        assertNotNull(mapper);
        assertEquals(false, mapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
    }

    @Test
    void testJacksonConverter() {
        MappingJackson2HttpMessageConverter converter = mainConfiguration.jacksonConverter(objectMapper);
        assertNotNull(converter);
        assertEquals(objectMapper, converter.getObjectMapper());
    }
}
