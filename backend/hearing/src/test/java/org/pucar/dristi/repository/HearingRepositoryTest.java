package org.pucar.dristi.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.repository.querybuilder.HearingQueryBuilder;
import org.pucar.dristi.repository.rowmapper.HearingDocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.HearingRowMapper;
import org.pucar.dristi.web.models.Hearing;
import org.pucar.dristi.web.models.HearingCriteria;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

class HearingRepositoryTest {

    @Mock
    private HearingQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private HearingRowMapper rowMapper;

    @Mock
    private HearingDocumentRowMapper hearingDocumentRowMapper;

    @InjectMocks
    private HearingRepository hearingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetHearings() {
        HearingCriteria criteria =  HearingCriteria.builder().hearingId("sdf").build();
        Hearing hearing = new Hearing();
        hearing.setId(UUID.randomUUID());
        List<Hearing> expectedHearings = Arrays.asList();
        String hearingQuery = "SELECT * FROM hearings";
        String hearingDocumentQuery = "SELECT * FROM hearing_documents WHERE hearing_id IN (?)";
        Map<UUID, List<Document>> hearingDocumentMap = new HashMap<>();
        hearingDocumentMap.put(UUID.randomUUID(), Arrays.asList(new Document()));

        when(queryBuilder.getHearingSearchQuery(anyList(), any(HearingCriteria.class))).thenReturn(hearingQuery);
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(HearingRowMapper.class))).thenReturn(expectedHearings);
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList())).thenReturn(hearingDocumentQuery);
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(HearingDocumentRowMapper.class))).thenReturn(hearingDocumentMap);

        List<Hearing> result = hearingRepository.getHearings(criteria);

        assertEquals(expectedHearings, result);
        verify(queryBuilder).getHearingSearchQuery(anyList(), any(HearingCriteria.class));
        verify(jdbcTemplate).query(anyString(), any(Object[].class), any(HearingRowMapper.class));
    }

    @Test
    void testGetHearings_CustomException() {
        HearingCriteria criteria = HearingCriteria.builder().build();
        when(queryBuilder.getHearingSearchQuery(anyList(), any(HearingCriteria.class))).thenThrow(new CustomException("ERROR", "Custom exception"));

        CustomException exception = assertThrows(CustomException.class, () -> hearingRepository.getHearings(criteria));

        assertEquals("ERROR", exception.getCode());
    }

    @Test
    void testGetHearings_Exception() {
        HearingCriteria criteria = HearingCriteria.builder().build();
        when(queryBuilder.getHearingSearchQuery(anyList(), any(HearingCriteria.class))).thenThrow(new RuntimeException("Runtime exception"));

        CustomException exception = assertThrows(CustomException.class, () -> hearingRepository.getHearings(criteria));

        assertEquals("Exception while Searching hearing", exception.getCode());
        assertTrue(exception.getMessage().contains("Error while fetching hearing application list"));
    }

    @Test
    void testCheckHearingsExist() {
        Hearing hearing = new Hearing();
        hearing.setId(UUID.randomUUID());
        hearing.setHearingId(String.valueOf(UUID.randomUUID()));
        hearing.setTenantId("tenant");
        List<Hearing> expectedHearings = Arrays.asList(hearing);

        when(hearingRepository.checkHearingsExist(hearing)).thenReturn(expectedHearings);

        List<Hearing> result = hearingRepository.checkHearingsExist(hearing);

        assertEquals(expectedHearings, result);
    }

    @Test
    void testUpdateTranscriptAdditionalAttendees() {
        Hearing hearing = new Hearing();
        List<Object> preparedStmtList = new ArrayList<>();
        String hearingUpdateQuery = "UPDATE hearings SET transcript = ? WHERE id = ?";

        when(queryBuilder.buildUpdateTranscriptAdditionalAttendeesQuery(anyList(), any(Hearing.class))).thenReturn(hearingUpdateQuery);
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        hearingRepository.updateTranscriptAdditionalAttendees(hearing);

        verify(queryBuilder).buildUpdateTranscriptAdditionalAttendeesQuery(anyList(), any(Hearing.class));
        verify(jdbcTemplate).update(anyString(), any(Object[].class));
    }

    @Test
    void testUpdateTranscriptAdditionalAttendees_CustomException() {
        Hearing hearing = new Hearing();
        List<Object> preparedStmtList = new ArrayList<>();
        String hearingUpdateQuery = "UPDATE hearings SET transcript = ? WHERE id = ?";

        when(queryBuilder.buildUpdateTranscriptAdditionalAttendeesQuery(anyList(), any(Hearing.class))).thenReturn(hearingUpdateQuery);
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(0);

        CustomException exception = assertThrows(CustomException.class, () -> hearingRepository.updateTranscriptAdditionalAttendees(hearing));

        assertEquals("Exception while updating hearing", exception.getCode());
        assertTrue(exception.getMessage().contains("Error while updating hearing"));
    }
}
