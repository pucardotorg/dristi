package org.pucar.dristi.repository.rowMapper;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pucar.dristi.web.models.Application;
import org.pucar.dristi.web.models.IssuedBy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@Slf4j

public class ApplicationRowMapperTest {
    private ResultSet resultSet;
    private ApplicationRowMapper applicationRowMapper;

    @BeforeEach
    public void setUp() {
        resultSet = Mockito.mock(ResultSet.class);
        applicationRowMapper = new ApplicationRowMapper();
    }
    @Test
    public void testExtractData() throws SQLException {
        String onBehalfOfUuidArray = "[\"550e8400-e29b-41d4-a716-446655440000\", \"550e8400-e29b-41d4-a716-446655440001\"]";
        String issuedByJson = "{\"benchId\": \"benchId\", \"courtId\": \"courtId\", \"judgeId\": [\"e7f39394-5b04-4f25-a901-8f369e73c758\", \"85b10177-cce5-4db1-bbe5-875a03f8a24c\"]}";
        String commentJson1 = "[{\"id\":\"123e4567-e89b-12d3-a456-556642440000\", \"tenantId\":\"tenant1\", \"individualId\":\"indiv1\", \"comment\":\"Hello\", \"isActive\":true, \"auditdetails\":null}]";
        String commentJson2 = "[{\"id\":\"123e4567-e89b-12d3-a456-556642440001\", \"tenantId\":\"tenant2\", \"individualId\":\"indiv2\", \"comment\":\"Another comment\", \"isActive\":true, \"auditdetails\":null}]";

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("id")).thenReturn("123e4567-e89b-12d3-a456-556642440000", "123e4567-e89b-12d3-a456-556642440001");
        when(resultSet.getLong("lastmodifiedtime")).thenReturn(123456789L, 987654321L);
        when(resultSet.getString("createdby")).thenReturn("John", "Alice");
        when(resultSet.getLong("createdtime")).thenReturn(123456789L, 987654321L);
        when(resultSet.getString("lastmodifiedby")).thenReturn("Doe", "Smith");
        when(resultSet.getString("applicationnumber")).thenReturn("APP-001", "APP-002");
        when(resultSet.getString("applicationtype")).thenReturn("1");
        when(resultSet.getString("cnrnumber")).thenReturn("CN-001", "CN-002");
        when(resultSet.getString("filingnumber")).thenReturn("F-001", "F-002");
        when(resultSet.getString("caseid")).thenReturn("Case-001", "Case-002");
        when(resultSet.getString("referenceid")).thenReturn("123e4567-e89b-12d3-a456-556642440000", "123e4567-e89b-12d3-a456-556642440001");
        when(resultSet.getString("createddate")).thenReturn("2024-05-27", "2024-05-28");
        when(resultSet.getString("applicationcreatedby")).thenReturn("123e4567-e89b-12d3-a456-556642440002", "123e4567-e89b-12d3-a456-556642440003");
        when(resultSet.getString("tenantid")).thenReturn("tenant1", "tenant2");
        when(resultSet.getBoolean("isactive")).thenReturn(true, false);
        when(resultSet.getString("issuedby")).thenReturn(issuedByJson);
        when(resultSet.getString("status")).thenReturn("Pending", "Approved");
        when(resultSet.getString("comment")).thenReturn(commentJson1, commentJson2);
        when(resultSet.getString("onbehalfof")).thenReturn(onBehalfOfUuidArray);

        // Creating ApplicationRowMapper instance
        ApplicationRowMapper rowMapper = new ApplicationRowMapper();

        // Invoking the extractData method
        List<Application> applications = rowMapper.extractData(resultSet);

        // Assertions
        assertEquals(2, applications.size());
        assertEquals("APP-001", applications.get(0).getApplicationNumber());
        assertEquals("CN-002", applications.get(1).getCnrNumber());
        assertEquals("benchId", applications.get(0).getIssuedBy().getBenchId());
        assertEquals("courtId", applications.get(0).getIssuedBy().getCourtId());
        assertEquals("e7f39394-5b04-4f25-a901-8f369e73c758", applications.get(0).getIssuedBy().getJudgeId().get(0).toString());

        // Asserting comment fields
        assertEquals("Hello", applications.get(0).getComment().get(0).getComment());
        assertEquals("tenant1", applications.get(0).getComment().get(0).getTenantId());
        assertEquals("indiv1", applications.get(0).getComment().get(0).getIndividualId());

        assertEquals("Another comment", applications.get(1).getComment().get(0).getComment());
        assertEquals("tenant2", applications.get(1).getComment().get(0).getTenantId());
        assertEquals("indiv2", applications.get(1).getComment().get(0).getIndividualId());

        // assert onBehalfOf field
        assertEquals(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), applications.get(0).getOnBehalfOf().get(0));
        assertEquals(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"), applications.get(0).getOnBehalfOf().get(1));
    }

    @Test
    public void testExtractDataWithSQLException() throws SQLException {
        // Simulate SQLException
        when(resultSet.next()).thenThrow(new SQLException("SQL error"));

        assertThrows(CustomException.class, () -> {
            applicationRowMapper.extractData(resultSet);
        });
    }

    @Test
    public void testExtractDataWithRuntimeException() throws SQLException {
        // Simulate RuntimeException (e.g., NullPointerException)
        when(resultSet.next()).thenThrow(new RuntimeException("Runtime error"));

        assertThrows(CustomException.class, () -> {
            applicationRowMapper.extractData(resultSet);
        });
    }

        @Test
        public void testStringToList_NormalCase() {
            String str = "[1, 2, 3]";
            List<Integer> expected = Arrays.asList(1, 2, 3);

            List<Integer> actual = applicationRowMapper.stringToList(str);

            assertEquals(expected, actual);
        }

        @Test
        public void testStringToList_NullString() {
            String str = null;
            List<Integer> expected = new ArrayList<>();

            List<Integer> actual = applicationRowMapper.stringToList(str);

            assertEquals(expected, actual);
        }

        @Test
        public void testStringToList_InvalidJson() {
            String str = "[1, 2, 'three']";

            assertThrows(CustomException.class, () -> applicationRowMapper.stringToList(str));
        }

    @Test
    public void testGetObjectFromJsonWithValidJson() {
        String json = "{\"benchId\":\"123\",\"judgeId\":[\"550e8400-e29b-41d4-a716-446655440000\"],\"courtId\":\"456\"}";

        IssuedBy expected = IssuedBy.builder()
                .benchId("123")
                .judgeId(Collections.singletonList(UUID.fromString("550e8400-e29b-41d4-a716-446655440000")))
                .courtId("456")
                .build();

        IssuedBy result = applicationRowMapper.getObjectFromJson(json, new TypeReference<IssuedBy>() {});
        assertEquals(expected, result);
    }

    @Test
    public void testGetObjectFromJsonWithNullJson() {
        IssuedBy expected = new IssuedBy(); // Expecting an empty IssuedBy object

        IssuedBy result = applicationRowMapper.getObjectFromJson(null, new TypeReference<IssuedBy>() {});
        assertEquals(expected, result);
    }

    @Test
    public void testGetObjectFromJsonWithEmptyJson() {
        IssuedBy expected = new IssuedBy(); // Expecting an empty IssuedBy object

        IssuedBy result = applicationRowMapper.getObjectFromJson("", new TypeReference<IssuedBy>() {});
        assertEquals(expected, result);
    }

    @Test
    public void testGetObjectFromJsonWithInvalidJson() {
        String json = "{\"benchId\":\"123\",\"judgeId\":[\"invalid-uuid\"],\"courtId\":\"456\"}"; // Invalid UUID
        TypeReference<IssuedBy> tRef = new TypeReference<IssuedBy>() {};
        assertThrows(CustomException.class, () -> {
            applicationRowMapper.getObjectFromJson(json, tRef);
        });
    }
}
