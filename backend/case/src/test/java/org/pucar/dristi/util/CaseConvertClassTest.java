package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CaseConvertClassTest {

    @Mock
    private ObjectMapper objectMapper;
    private MockedStatic<SpringContext> mockedSpringContext;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockedSpringContext = Mockito.mockStatic(SpringContext.class);
        mockedSpringContext.when(() -> SpringContext.getBean(ObjectMapper.class)).thenReturn(objectMapper);
    }

    @AfterEach
    void tearDown() {
        mockedSpringContext.close();
    }

    @Test
    void testConvertTo_Object_Success() throws IOException {
        // Prepare mock data
        String jsonString = "{\"name\":\"test\"}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        // Call the method
        TestObject result = CaseConvertClass.convertTo(jsonNode, TestObject.class);

        // Verify the result
        assertNotNull(result);
        assertEquals("test", result.getName());
    }

    @Test
    void testConvertTo_List_Success() throws IOException {
        // Prepare mock data
        String jsonString = "[{\"name\":\"test1\"}, {\"name\":\"test2\"}]";
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        // Call the method
        List<TestObject> result = CaseConvertClass.convertTo(jsonNode, TestObject.class);

        // Verify the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test1", result.get(0).getName());
        assertEquals("test2", result.get(1).getName());
    }

    @Test
    void testConvertTo_EmptyArray() throws IOException {
        // Prepare mock data
        String jsonString = "[]";
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        // Call the method
        List<TestObject> result = CaseConvertClass.convertTo(jsonNode, TestObject.class);

        // Verify the result
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testConvertTo_InvalidJson() {
        // Prepare invalid JSON
        String invalidJson = "{name:\"test\"";

        assertThrows(IOException.class, () -> {
            objectMapper.readTree(invalidJson);
        });
    }

    @Test
    void testConvertTo_NullJsonNode() throws IOException {
        // Prepare null JsonNode
        JsonNode jsonNode = null;

        // Call the method and expect a NullPointerException
        assertThrows(NullPointerException.class, () -> CaseConvertClass.convertTo(jsonNode, TestObject.class));
    }

    // Helper class for testing
    static class TestObject {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
