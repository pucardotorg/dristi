package org.pucar.dristi.util.jsonmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.egov.tracer.model.CustomException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JsonMapperUtilTest {

	@Mock
	private ObjectMapper objectMapper;

	@InjectMocks
	private JsonMapperUtil jsonMapperUtil;

	private JSONObject jsonObject;
	private String jsonString;
	private Class<TestClass> clazz;
	private TestClass testClass;

	@BeforeEach
	void setUp() throws JSONException {
		jsonObject = new JSONObject();
		jsonString = "{\"name\":\"test\"}";
		clazz = TestClass.class;
		testClass = new TestClass("test");

		jsonObject.put("name", "test");
	}

	@Test
	void testMapSuccess() throws Exception {
		when(objectMapper.configure(any(SerializationFeature.class), anyBoolean())).thenReturn(objectMapper);
		when(objectMapper.readValue(jsonString, clazz)).thenReturn(testClass);

		TestClass result = jsonMapperUtil.map(jsonObject, clazz);

		assertNotNull(result);
		assertEquals(testClass.getName(), result.getName());
		verify(objectMapper).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		verify(objectMapper).readValue(jsonObject.toString(), clazz);
	}

	@Test
	void testMapNullJsonObject() throws JsonProcessingException {
		TestClass result = jsonMapperUtil.map(null, clazz);

		assertNull(result);
		verify(objectMapper, never()).readValue(anyString(), eq(clazz));
	}

	@Test
	void testMapException() throws Exception {
		when(objectMapper.configure(any(SerializationFeature.class), anyBoolean())).thenReturn(objectMapper);
		when(objectMapper.readValue(anyString(), eq(clazz))).thenThrow(new RuntimeException("Mapping error"));

		CustomException exception = assertThrows(CustomException.class, () -> {
			jsonMapperUtil.map(jsonObject, clazz);
		});

		assertEquals("JSON_MAPPING_ERROR", exception.getCode());
		assertTrue(exception.getMessage().contains("Mapping error"));
		verify(objectMapper).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		verify(objectMapper).readValue(jsonObject.toString(), clazz);
	}

	// Inner class for testing
	static class TestClass {
		private String name;

		public TestClass() {}

		public TestClass(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
