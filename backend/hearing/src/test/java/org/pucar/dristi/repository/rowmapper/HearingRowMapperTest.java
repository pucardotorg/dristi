package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Attendee;
import org.pucar.dristi.web.models.Hearing;
import org.pucar.dristi.web.models.PresidedBy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HearingRowMapperTest {

    @Mock
    private ResultSet rs;

    private ObjectMapper objectMapper;
    private HearingRowMapper rowMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        rowMapper = new HearingRowMapper(objectMapper);
    }

    @Test
    void shouldExtractDataSuccessfully() throws SQLException, JsonProcessingException {
        // Arrange
        String uuid = "921e3cc0-64df-490f-adc1-91c3492219e6";
        String cnrNumbersJson = "[\"cnr1\"]";
        String filingNumberJson = "[\"file1\"]";
        String applicationNumbersJson = "[\"app1\"]";
        String presidedByJson = "{\"benchID\":\"bench1\"}";
        String attendeesJson = "[{\"name\":\"Attendee1\"}]";
        String transcriptJson = "[\"transcript1\"]";
        String additionalDetailsJson = "{\"key\":\"value\"}";

        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("id")).thenReturn(uuid);
        when(rs.getString("tenantid")).thenReturn("tenant1");
        when(rs.getString("hearingid")).thenReturn("hearing1");
        when(rs.getString("hearingtype")).thenReturn("type1");
        when(rs.getString("status")).thenReturn("status1");
        when(rs.getLong("starttime")).thenReturn(1625140800000L);
        when(rs.getLong("endtime")).thenReturn(1625144400000L);
        when(rs.getString("vclink")).thenReturn("link1");
        when(rs.getBoolean("isactive")).thenReturn(true);
        when(rs.getString("notes")).thenReturn("note1");
        when(rs.getString("createdby")).thenReturn("user1");
        when(rs.getLong("createdtime")).thenReturn(1625140800000L);
        when(rs.getString("lastmodifiedby")).thenReturn("user2");
        when(rs.getLong("lastmodifiedtime")).thenReturn(1625144400000L);
        when(rs.getString("cnrnumbers")).thenReturn(cnrNumbersJson);
        when(rs.getString("filingnumber")).thenReturn(filingNumberJson);
        when(rs.getString("courtcasenumber")).thenReturn(" ");
        when(rs.getString("casereferencenumber")).thenReturn(" ");
        when(rs.getString("cmpNumber")).thenReturn(" ");
        when(rs.getString("applicationnumbers")).thenReturn(applicationNumbersJson);
        when(rs.getString("presidedby")).thenReturn(presidedByJson);
        when(rs.getString("attendees")).thenReturn(attendeesJson);
        when(rs.getString("transcript")).thenReturn(transcriptJson);
        PGobject pgObject = new PGobject();
        pgObject.setValue(additionalDetailsJson);
        when(rs.getObject("additionalDetails")).thenReturn(pgObject);

        Hearing hearing = Hearing.builder()
                .tenantId("tenant1")
                .id(UUID.fromString(uuid))
                .hearingId("hearing1")
                .hearingType("type1")
                .status("status1")
                .startTime(1625140800000L)
                .endTime(1625144400000L)
                .vcLink("link1")
                .isActive(true)
                .notes("note1")
                .auditDetails(AuditDetails.builder().createdBy("user1").createdTime(1625140800000L).lastModifiedBy("user2").lastModifiedTime(1625144400000L).build())
                .cnrNumbers(Collections.singletonList("cnr1"))
                .filingNumber(Collections.singletonList("file1"))
                .applicationNumbers(Collections.singletonList("app1"))
                .presidedBy(PresidedBy.builder().benchID("bench1").build())
                .attendees(Collections.singletonList(Attendee.builder().name("Attendee1").build()))
                .transcript(Collections.singletonList("transcript1"))
                .additionalDetails(objectMapper.readTree(pgObject.getValue()))
                .build();

        // Act
        List<Hearing> results = rowMapper.extractData(rs);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(compareHearing(hearing, results.get(0)));
    }



    @Test
    void shouldHandleNullAndEmptyJsonGracefully() throws SQLException, JsonProcessingException {
        // Arrange
        String uuid = "921e3cc0-64df-490f-adc1-91c3492219e6";
        String additionalDetailsJson = "{\"key\":\"value\"}";
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("id")).thenReturn(uuid);
        when(rs.getString("tenantid")).thenReturn("tenant1");
        when(rs.getString("hearingid")).thenReturn("hearing1");
        when(rs.getString("hearingtype")).thenReturn("type1");
        when(rs.getString("status")).thenReturn("status1");
        when(rs.getLong("starttime")).thenReturn(0L);
        when(rs.getLong("endtime")).thenReturn(1625140800000L);
        when(rs.getString("vclink")).thenReturn("link1");
        when(rs.getBoolean("isactive")).thenReturn(true);
        when(rs.getString("notes")).thenReturn("note1");
        when(rs.getString("createdby")).thenReturn("user1");
        when(rs.getLong("createdtime")).thenReturn(1625140800000L);
        when(rs.getString("lastmodifiedby")).thenReturn("user2");
        when(rs.getLong("lastmodifiedtime")).thenReturn(1625144400000L);
        when(rs.getString("cnrnumbers")).thenReturn(null);
        when(rs.getString("filingnumber")).thenReturn("");
        when(rs.getString("applicationnumbers")).thenReturn(" ");
        when(rs.getString("courtcasenumber")).thenReturn(" ");
        when(rs.getString("casereferencenumber")).thenReturn(" ");
        when(rs.getString("cmpNumber")).thenReturn(" ");
        when(rs.getString("presidedby")).thenReturn(null);
        when(rs.getString("attendees")).thenReturn("");
        when(rs.getString("transcript")).thenReturn(null);
        PGobject pgObject = new PGobject();
        pgObject.setValue(additionalDetailsJson);
        when(rs.getObject("additionalDetails")).thenReturn(pgObject);

        Hearing hearing = Hearing.builder()
                .tenantId("tenant1")
                .id(UUID.fromString(uuid))
                .hearingId("hearing1")
                .hearingType("type1")
                .courtCaseNumber("courtcasenumber")
                .caseReferenceNumber("casereferencenumber")
                .cmpNumber("cmpNumber")
                .status("status1")
                .startTime(0L)
                .endTime(1625140800000L)
                .vcLink("link1")
                .isActive(true)
                .notes("note1")
                .auditDetails(AuditDetails.builder().createdBy("user1").createdTime(1625140800000L).lastModifiedBy("user2").lastModifiedTime(1625144400000L).build())
                .cnrNumbers(Collections.emptyList())
                .filingNumber(Collections.emptyList())
                .applicationNumbers(Collections.emptyList())
                .presidedBy(PresidedBy.builder().build())
                .attendees(Collections.emptyList())
                .transcript(Collections.emptyList())
                .additionalDetails(objectMapper.readTree(additionalDetailsJson))
                .build();

        // Act
        List<Hearing> results = rowMapper.extractData(rs);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(compareHearing(hearing, results.get(0)));
    }

    @Test
    void shouldThrowExceptionForInvalidJson() throws SQLException {
        // Arrange
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("id")).thenReturn(UUID.randomUUID().toString());
        when(rs.getString("tenantid")).thenReturn("tenant1");
        when(rs.getString("hearingid")).thenReturn("hearing1");
        when(rs.getString("hearingtype")).thenReturn("type1");
        when(rs.getString("status")).thenReturn("status1");
        when(rs.getString("vclink")).thenReturn("link1");
        when(rs.getBoolean("isactive")).thenReturn(true);
        when(rs.getString("notes")).thenReturn("note1");
        when(rs.getString("createdby")).thenReturn("user1");
        when(rs.getLong("createdtime")).thenReturn(1625140800000L);
        when(rs.getString("lastmodifiedby")).thenReturn("user2");
        when(rs.getLong("lastmodifiedtime")).thenReturn(1625144400000L);
        when(rs.getString("cnrnumbers")).thenReturn("[invalid_json");

        // Act & Assert
        assertThrows(CustomException.class, () -> rowMapper.extractData(rs));
    }

    @Test
    void shouldHandleSQLExceptionGracefully() throws SQLException {
        // Arrange
        when(rs.next()).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(CustomException.class, () -> rowMapper.extractData(rs));
    }

    public boolean compareHearing(Hearing expected, Hearing actual) {
        if (expected == null || actual == null) return false;

        // Compare tenantId
        if (!Objects.equals(expected.getTenantId(), actual.getTenantId())) return false;

        // Compare hearingId
        if (!Objects.equals(expected.getHearingId(), actual.getHearingId())) return false;

        // Compare filingNumber
        if (!Objects.equals(expected.getFilingNumber(), actual.getFilingNumber())) return false;

        // Compare cnrNumbers
        if (!Objects.equals(expected.getCnrNumbers(), actual.getCnrNumbers())) return false;

        // Compare applicationNumbers
        if (!Objects.equals(expected.getApplicationNumbers(), actual.getApplicationNumbers())) return false;

        // Compare hearingType
        if (!Objects.equals(expected.getHearingType(), actual.getHearingType())) return false;

        // Compare status
        if (!Objects.equals(expected.getStatus(), actual.getStatus())) return false;

        // Compare startTime
        if (!Objects.equals(expected.getStartTime(), actual.getStartTime())) return false;

        // Compare endTime
        if (!Objects.equals(expected.getEndTime(), actual.getEndTime())) return false;

        // Compare presidedBy
        if (!Objects.equals(expected.getPresidedBy(), actual.getPresidedBy())) return false;

        // Compare attendees
        if (!Objects.equals(expected.getAttendees(), actual.getAttendees())) return false;

        // Compare transcript
        if (!Objects.equals(expected.getTranscript(), actual.getTranscript())) return false;

        // Compare vcLink
        if (!Objects.equals(expected.getVcLink(), actual.getVcLink())) return false;

        // Compare isActive
        if (!Objects.equals(expected.getIsActive(), actual.getIsActive())) return false;

        // Compare documents
        if (!Objects.equals(expected.getDocuments(), actual.getDocuments())) return false;

        // Compare additionalDetails
        if (!Objects.equals(expected.getAdditionalDetails(), actual.getAdditionalDetails())) return false;

        // Compare auditDetails
        if (!Objects.equals(expected.getAuditDetails().getCreatedBy(), actual.getAuditDetails().getCreatedBy())) return false;
        if (!Objects.equals(expected.getAuditDetails().getLastModifiedBy(), actual.getAuditDetails().getLastModifiedBy())) return false;
        if (!Objects.equals(expected.getAuditDetails().getCreatedTime(), actual.getAuditDetails().getCreatedTime())) return false;
        if (!Objects.equals(expected.getAuditDetails().getLastModifiedTime(), actual.getAuditDetails().getLastModifiedTime())) return false;

        // Compare workflow
        if (!Objects.equals(expected.getWorkflow(), actual.getWorkflow())) return false;

        // Compare notes
        if (!Objects.equals(expected.getNotes(), actual.getNotes())) return false;

        return true;
    }

    @Test
    void testGetListFromJson_NullJson() {
        String json = null;
        List<String> result = rowMapper.getListFromJson(json);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetListFromJson_EmptyJson() {
        String json = "  ";
        List<String> result = rowMapper.getListFromJson(json);
        assertTrue(result.isEmpty());
    }
}

