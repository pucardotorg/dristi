package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.util.Util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UtilTest {

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private Util util;

    private String kafkaJson;
    private String jsonPath;
    private JSONArray expectedArray;

    @BeforeEach
    void setUp() throws JSONException {
        kafkaJson = "{ \"data\": [ { \"id\": \"task1\" }, { \"id\": \"task2\" } ] }";
        jsonPath = "$.data";
        expectedArray = new JSONArray();
        expectedArray.put(new JSONObject().put("id", "task1"));
        expectedArray.put(new JSONObject().put("id", "task2"));
    }

    @Test
    void testConstructArray_ValidJsonArray() throws Exception {
        // Mocking JsonPath.read() and ObjectMapper.writeValueAsString()
        String jsonArrayString = "[ { \"id\": \"task1\" }, { \"id\": \"task2\" } ]";
        when(mapper.writeValueAsString(any(net.minidev.json.JSONArray.class))).thenReturn(jsonArrayString);

        // Call the method
        org.json.JSONArray result = util.constructArray(kafkaJson, jsonPath);

        // Verify
        assertEquals(expectedArray.toString(), result.toString());
    }

    @Test
    void testConstructArray_InvalidJsonArray() throws Exception {
        // Invalid json array
        kafkaJson = "{ \"data\": { \"id\": \"task1\" } }";

        // Call the method
        org.json.JSONArray result = util.constructArray(kafkaJson, jsonPath);

        // Verify
        assertNull(result);
    }

    @Test
    void testConstructArray_NoJsonPath() throws Exception {
        // JSON string without json path
        kafkaJson = "{ \"array\":[ { \"id\": \"task1\" }, { \"id\": \"task2\" } ]}";
        String kafkaJsonResult = "[{\"id\":\"task1\"},{\"id\":\"task2\"}]";
        // Call the method
        org.json.JSONArray result = util.constructArray(kafkaJson, null);

        // Verify
        assertEquals(new org.json.JSONArray(kafkaJsonResult).toString(), result.toString());
    }

    @Test
    void testPullArrayOutOfString_ValidJsonString() {
        // JSON string with array
        String jsonString = "{ \"array\":[ { \"id\": \"task1\" }, { \"id\": \"task2\" } ]}";

        // Call the method
        String result = util.pullArrayOutOfString(jsonString);

        // Verify
        assertEquals("[ { \"id\": \"task1\" }, { \"id\": \"task2\" } ]", result);
    }

    @Test
    void testPullArrayOutOfString_InvalidJsonString() {
        // Invalid JSON string
        String jsonString = "{ \"array\":{ \"id\": \"task1\" }}";

        // Call the method
        String result = util.pullArrayOutOfString(jsonString);

        // Verify
        assertEquals("{ \"id\": \"task1\" }", result);
    }
}
