package org.pucar.dristi.repository;

import static org.mockito.ArgumentMatchers.*;
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
import org.pucar.dristi.web.models.HearingSearchRequest;
import org.pucar.dristi.web.models.Pagination;
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
        // Mock data
        HearingCriteria criteria = HearingCriteria.builder().hearingId("sdf").build();
        Pagination pagination = Pagination.builder().build();
        HearingSearchRequest hearingSearchRequest = HearingSearchRequest.builder().criteria(criteria).pagination(pagination).build();
        List<Hearing> expectedHearings = List.of(Hearing.builder().id(UUID.randomUUID()).build());

        // Mock behavior of queryBuilder
        String hearingQuery = "SELECT * FROM hearings";
        String hearingDocumentQuery = "SELECT * FROM hearing_documents WHERE hearing_id IN (?)";
        Map<UUID, List<Document>> hearingDocumentMap = new HashMap<>();
        hearingDocumentMap.put(expectedHearings.get(0).getId(), List.of(new Document()));

        when(queryBuilder.getHearingSearchQuery(anyList(), any(HearingCriteria.class))).thenReturn(hearingQuery);
        when(queryBuilder.addOrderByQuery(anyString(), any(Pagination.class))).thenReturn(hearingQuery);
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(HearingRowMapper.class))).thenReturn(expectedHearings);
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList())).thenReturn(hearingDocumentQuery);
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(HearingDocumentRowMapper.class))).thenReturn(hearingDocumentMap);
        when(queryBuilder.getTotalCountQuery(anyString())).thenReturn(hearingQuery);
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))).thenReturn(5);
        when(queryBuilder.addPaginationQuery(anyString(), any(Pagination.class), anyList())).thenReturn(hearingQuery);
        // Test method call
        List<Hearing> result = hearingRepository.getHearings(hearingSearchRequest);

        // Assertions
        assertNotNull(result);
        assertEquals(expectedHearings, result);
        verify(queryBuilder).getHearingSearchQuery(anyList(), any(HearingCriteria.class));
        verify(jdbcTemplate).query(anyString(), any(Object[].class), any(HearingRowMapper.class));
        verify(queryBuilder).getDocumentSearchQuery(anyList(), anyList());
        verify(jdbcTemplate).query(anyString(), any(Object[].class), any(HearingDocumentRowMapper.class));
    }

    @Test
    void testGetHearings_WithPagination() {
        // Mock data
        HearingCriteria criteria = HearingCriteria.builder().hearingId("sdf").build();
        Pagination pagination = Pagination.builder().limit(10.0).offSet(0.0).build();
        HearingSearchRequest hearingSearchRequest = HearingSearchRequest.builder().criteria(criteria).pagination(pagination).build();
        List<Hearing> expectedHearings = List.of(Hearing.builder().id(UUID.randomUUID()).build());

        // Mock behavior of queryBuilder for pagination
        String hearingQuery = "SELECT * FROM hearings";
        String hearingDocumentQuery = "SELECT * FROM hearing_documents WHERE hearing_id IN (?)";
        Map<UUID, List<Document>> hearingDocumentMap = new HashMap<>();
        hearingDocumentMap.put(expectedHearings.get(0).getId(), List.of(new Document()));

        when(queryBuilder.getHearingSearchQuery(anyList(), any(HearingCriteria.class))).thenReturn(hearingQuery);
        when(queryBuilder.addOrderByQuery(anyString(), any(Pagination.class))).thenReturn(hearingQuery);
        when(queryBuilder.addPaginationQuery(anyString(),any(Pagination.class),anyList())).thenReturn(hearingQuery);
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(HearingRowMapper.class))).thenReturn(expectedHearings);
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList())).thenReturn(hearingDocumentQuery);
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(HearingDocumentRowMapper.class))).thenReturn(hearingDocumentMap);
        when(queryBuilder.getTotalCountQuery(anyString())).thenReturn("SELECT count(*) FROM hearings");

        // Mock total count query
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))).thenReturn(20);

        // Test method call
        List<Hearing> result = hearingRepository.getHearings(hearingSearchRequest);

        // Assertions
        assertEquals(expectedHearings, result);
        assertEquals(20.0, hearingSearchRequest.getPagination().getTotalCount());
        verify(queryBuilder).getHearingSearchQuery(anyList(), any(HearingCriteria.class));
        verify(queryBuilder).addPaginationQuery(anyString(), any(Pagination.class), anyList());
        verify(jdbcTemplate).query(anyString(), any(Object[].class), any(HearingRowMapper.class));
        verify(queryBuilder).getDocumentSearchQuery(anyList(), anyList());
        verify(jdbcTemplate).query(anyString(), any(Object[].class), any(HearingDocumentRowMapper.class));
        verify(queryBuilder).getTotalCountQuery(anyString());
        verify(jdbcTemplate).queryForObject(anyString(), any(Object[].class), eq(Integer.class));
    }

    @Test
    void testGetHearings_CustomException() {
        HearingCriteria criteria = HearingCriteria.builder().build();
        Pagination pagination = Pagination.builder().build();
        HearingSearchRequest hearingSearchRequest = HearingSearchRequest.builder().criteria(criteria).pagination(pagination).build();

        // Mock behavior to throw CustomException
        when(queryBuilder.getHearingSearchQuery(anyList(), any(HearingCriteria.class)))
                .thenThrow(new CustomException("ERROR", "Custom exception"));

        // Test and assert CustomException
        CustomException exception = assertThrows(CustomException.class, () -> hearingRepository.getHearings(hearingSearchRequest));
        assertEquals("ERROR", exception.getCode());
    }

    @Test
    void testGetHearings_RuntimeException() {
        HearingCriteria criteria = HearingCriteria.builder().build();
        Pagination pagination = Pagination.builder().build();
        HearingSearchRequest hearingSearchRequest = HearingSearchRequest.builder().criteria(criteria).pagination(pagination).build();

        // Mock behavior to throw RuntimeException
        when(queryBuilder.getHearingSearchQuery(anyList(), any(HearingCriteria.class)))
                .thenThrow(new RuntimeException("Runtime exception"));

        // Test and assert CustomException
        CustomException exception = assertThrows(CustomException.class, () -> hearingRepository.getHearings(hearingSearchRequest));
        assertEquals("Exception while Searching hearing", exception.getCode());
        assertTrue(exception.getMessage().contains("Error while fetching hearing application list"));
    }

    @Test
    void testCheckHearingsExist() {
        // Mock data
        Hearing hearing = new Hearing();
        hearing.setId(UUID.randomUUID());
        hearing.setHearingId(String.valueOf(UUID.randomUUID()));
        hearing.setTenantId("tenant");
        List<Hearing> expectedHearings = List.of(Hearing.builder().id(UUID.randomUUID()).build());

        // Mock behavior of queryBuilder
        String hearingQuery = "SELECT * FROM hearings";
        String hearingDocumentQuery = "SELECT * FROM hearing_documents WHERE hearing_id IN (?)";
        Map<UUID, List<Document>> hearingDocumentMap = new HashMap<>();
        hearingDocumentMap.put(expectedHearings.get(0).getId(), List.of(new Document()));

        when(queryBuilder.getHearingSearchQuery(anyList(), any(HearingCriteria.class))).thenReturn(hearingQuery);
        when(queryBuilder.addOrderByQuery(anyString(), any(Pagination.class))).thenReturn(hearingQuery);
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(HearingRowMapper.class))).thenReturn(expectedHearings);
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList())).thenReturn(hearingDocumentQuery);
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(HearingDocumentRowMapper.class))).thenReturn(hearingDocumentMap);
        when(queryBuilder.getTotalCountQuery(anyString())).thenReturn(hearingQuery);
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))).thenReturn(5);
        when(queryBuilder.addPaginationQuery(anyString(), any(Pagination.class), anyList())).thenReturn(hearingQuery);

        // Test method call
        List<Hearing> result = hearingRepository.checkHearingsExist(hearing);

        // Assertions
        assertNotNull(result);
        assertEquals(expectedHearings, result);
        verify(queryBuilder).getHearingSearchQuery(anyList(), any(HearingCriteria.class));
        verify(jdbcTemplate).query(anyString(), any(Object[].class), any(HearingRowMapper.class));
        verify(queryBuilder).getDocumentSearchQuery(anyList(), anyList());
        verify(jdbcTemplate).query(anyString(), any(Object[].class), any(HearingDocumentRowMapper.class));
    }

    @Test
    void testUpdateTranscriptAdditionalAttendees() {
        // Mock data
        Hearing hearing = new Hearing();
        String hearingUpdateQuery = "UPDATE hearings SET transcript = ? WHERE id = ?";

        // Mock behavior of updateTranscriptAdditionalAttendees method
        when(queryBuilder.buildUpdateTranscriptAdditionalAttendeesQuery(anyList(), any(Hearing.class))).thenReturn(hearingUpdateQuery);
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Test method call
        hearingRepository.updateTranscriptAdditionalAttendees(hearing);

        // Assertions
        verify(queryBuilder).buildUpdateTranscriptAdditionalAttendeesQuery(anyList(), any(Hearing.class));
        verify(jdbcTemplate).update(anyString(), any(Object[].class));
    }

    @Test
    void testUpdateTranscriptAdditionalAttendees_NoUpdate() {
        // Mock data
        Hearing hearing = new Hearing();
        String hearingUpdateQuery = "UPDATE hearings SET transcript = ? WHERE id = ?";

        // Mock behavior of updateTranscriptAdditionalAttendees method when no rows are updated
        when(queryBuilder.buildUpdateTranscriptAdditionalAttendeesQuery(anyList(), any(Hearing.class))).thenReturn(hearingUpdateQuery);
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(0);

        // Test and assert CustomException
        CustomException exception = assertThrows(CustomException.class, () -> hearingRepository.updateTranscriptAdditionalAttendees(hearing));
        assertEquals("Exception while updating hearing", exception.getCode());
        assertTrue(exception.getMessage().contains("Error while updating hearing"));
    }
}
