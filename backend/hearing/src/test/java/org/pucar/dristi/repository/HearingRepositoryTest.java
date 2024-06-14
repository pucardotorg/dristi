package org.pucar.dristi.repository;

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
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class HearingRepositoryTest {

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getHearings_Success() {
        // Arrange
        List<Hearing> listHearings = new ArrayList<>();
        Hearing hearing1 = new Hearing();
        hearing1.setId(UUID.fromString("921e3cc0-64df-490f-adc1-91c3492219e6"));
        listHearings.add(hearing1);

        String hearingQuery = "SELECT * FROM hearings";
        String documentQuery = "SELECT * FROM hearing_documents";

        when(queryBuilder.getHearingSearchQuery(anyList(), anyString(), anyString(), anyString(), anyString(), anyString(), any(LocalDate.class), any(LocalDate.class), anyInt(), anyInt(), anyString()))
                .thenReturn(hearingQuery);
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList()))
                .thenReturn(documentQuery);
        when(jdbcTemplate.query(eq(hearingQuery), any(Object[].class), any(HearingRowMapper.class)))
                .thenReturn(listHearings);
        when(jdbcTemplate.query(eq(documentQuery), any(Object[].class), any(HearingDocumentRowMapper.class)))
                .thenReturn(Collections.emptyMap());

        // Act
        List<Hearing> result = hearingRepository.getHearings("cnrNumber", "applicationNumber", "hearingId", "filingNumber", "tenantId", LocalDate.now(), LocalDate.now(), 10, 0, "sortBy");

        // Assert
        assertNotNull(result);
        assertEquals(listHearings, result);
    }

    @Test
    void getHearings_EmptySuccess() {
        // Arrange
        List<Hearing> listHearings = new ArrayList<>();
        String hearingQuery = "SELECT * FROM hearings";

        when(queryBuilder.getHearingSearchQuery(anyList(), anyString(), anyString(), anyString(), anyString(), anyString(), any(LocalDate.class), any(LocalDate.class), anyInt(), anyInt(), anyString()))
                .thenReturn(hearingQuery);
        when(jdbcTemplate.query(eq(hearingQuery), any(Object[].class), any(HearingRowMapper.class)))
                .thenReturn(listHearings);

        // Act
        List<Hearing> result = hearingRepository.getHearings("cnrNumber", "applicationNumber", "hearingId", "filingNumber", "tenantId", LocalDate.now(), LocalDate.now(), 10, 0, "sortBy");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getHearings_Exception() {
        // Arrange
        String hearingQuery = "SELECT * FROM hearings";
        when(queryBuilder.getHearingSearchQuery(anyList(), anyString(), anyString(), anyString(), anyString(), anyString(), any(LocalDate.class), any(LocalDate.class), anyInt(), anyInt(), anyString()))
                .thenReturn(hearingQuery);
        when(jdbcTemplate.query(eq(hearingQuery), any(Object[].class), any(HearingRowMapper.class)))
                .thenThrow(RuntimeException.class);

        // Assert
        assertThrows(CustomException.class, () ->
                hearingRepository.getHearings("cnrNumber", "applicationNumber", "hearingId", "filingNumber", "tenantId", LocalDate.now(), LocalDate.now(), 10, 0, "sortBy"));
    }

    @Test
    void getHearings_WithDocuments() {
        // Arrange
        List<Hearing> listHearings = new ArrayList<>();
        Hearing hearing1 = new Hearing();
        hearing1.setId(UUID.fromString("921e3cc0-64df-490f-adc1-91c3492219e6"));
        listHearings.add(hearing1);

        String hearingQuery = "SELECT * FROM hearings";
        String documentQuery = "SELECT * FROM hearing_documents";

        Map<UUID, List<Document>> documentMap = new HashMap<>();
        documentMap.put(UUID.fromString("921e3cc0-64df-490f-adc1-91c3492219e6"), new ArrayList<>());

        when(queryBuilder.getHearingSearchQuery(anyList(), anyString(), anyString(), anyString(), anyString(), anyString(), any(LocalDate.class), any(LocalDate.class), anyInt(), anyInt(), anyString()))
                .thenReturn(hearingQuery);
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList()))
                .thenReturn(documentQuery);
        when(jdbcTemplate.query(eq(hearingQuery), any(Object[].class), any(HearingRowMapper.class)))
                .thenReturn(listHearings);
        when(jdbcTemplate.query(eq(documentQuery), any(Object[].class), any(HearingDocumentRowMapper.class)))
                .thenReturn(documentMap);

        // Act
        List<Hearing> result = hearingRepository.getHearings("cnrNumber", "applicationNumber", "hearingId", "filingNumber", "tenantId", LocalDate.now(), LocalDate.now(), 10, 0, "sortBy");

        // Assert
        assertNotNull(result);
        assertEquals(listHearings, result);
        assertNotNull(result.get(0).getDocuments());
    }
    @Test
    void getHearings_ExceptionHandling() {
        // Arrange
        String cnrNumber = "cnr123";
        String applicationNumber = "app123";
        String hearingId = "hear123";
        String filingNumber = "file123";
        String tenantId = "tenant123";
        LocalDate fromDate = LocalDate.now().minusDays(10);
        LocalDate toDate = LocalDate.now();
        Integer limit = 10;
        Integer offset = 0;
        String sortBy = "date";

        when(queryBuilder.getHearingSearchQuery(anyList(), eq(cnrNumber), eq(applicationNumber), eq(hearingId), eq(filingNumber), eq(tenantId), eq(fromDate), eq(toDate), eq(limit), eq(offset), eq(sortBy))).thenReturn("testHearingQuery");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(HearingRowMapper.class))).thenThrow(new RuntimeException("Database error"));

        // Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            // Act
            hearingRepository.getHearings(cnrNumber, applicationNumber, hearingId, filingNumber, tenantId, fromDate, toDate, limit, offset, sortBy);
        });

        assertEquals("Error while fetching hearing application list: Database error", exception.getMessage());
    }

    @Test
    void getHearingsByHearingObject() {
        // Arrange
        Hearing hearing = new Hearing();
        hearing.setHearingId("hear123");
        hearing.setTenantId("tenant123");

        when(queryBuilder.getHearingSearchQuery(anyList(), isNull(), isNull(), eq("hear123"), isNull(), eq("tenant123"), isNull(), isNull(), eq(1), eq(0), isNull())).thenReturn("testHearingQuery");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(HearingRowMapper.class))).thenReturn(new ArrayList<>());

        // Act
        List<Hearing> result = hearingRepository.getHearings(hearing);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getHearings_NullDates() {
        // Arrange
        String cnrNumber = "cnr123";
        String applicationNumber = "app123";
        String hearingId = "hear123";
        String filingNumber = "file123";
        String tenantId = "tenant123";
        Integer limit = 10;
        Integer offset = 0;
        String sortBy = "date";

        when(queryBuilder.getHearingSearchQuery(anyList(), eq(cnrNumber), eq(applicationNumber), eq(hearingId), eq(filingNumber), eq(tenantId), isNull(), isNull(), eq(limit), eq(offset), eq(sortBy))).thenReturn("testHearingQuery");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(HearingRowMapper.class))).thenReturn(new ArrayList<>());

        // Act
        List<Hearing> result = hearingRepository.getHearings(cnrNumber, applicationNumber, hearingId, filingNumber, tenantId, null, null, limit, offset, sortBy);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getHearings_MultipleResults() {
        // Arrange
        String cnrNumber = "cnr123";
        String applicationNumber = "app123";
        String hearingId = "hear123";
        String filingNumber = "file123";
        String tenantId = "tenant123";
        LocalDate fromDate = LocalDate.now().minusDays(10);
        LocalDate toDate = LocalDate.now();
        Integer limit = 10;
        Integer offset = 0;
        String sortBy = "date";

        List<Hearing> listHearings = new ArrayList<>();
        Hearing hearing1 = new Hearing();
        hearing1.setId(UUID.fromString("921e3cc0-64df-490f-adc1-91c3492219e6"));
        Hearing hearing2 = new Hearing();
        hearing2.setId(UUID.fromString("d747abff-6d5d-47d7-99e2-fc70eaa856cb"));
        listHearings.add(hearing1);
        listHearings.add(hearing2);

        when(queryBuilder.getHearingSearchQuery(anyList(), eq(cnrNumber), eq(applicationNumber), eq(hearingId), eq(filingNumber), eq(tenantId), eq(fromDate), eq(toDate), eq(limit), eq(offset), eq(sortBy))).thenReturn("testHearingQuery");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(HearingRowMapper.class))).thenReturn(listHearings);
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList())).thenReturn("testDocumentQuery");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(HearingDocumentRowMapper.class))).thenReturn(new HashMap<>());

        // Act
        List<Hearing> result = hearingRepository.getHearings(cnrNumber, applicationNumber, hearingId, filingNumber, tenantId, fromDate, toDate, limit, offset, sortBy);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    void getHearings_InvalidDateRange() {
        // Arrange
        String cnrNumber = "cnr123";
        String applicationNumber = "app123";
        String hearingId = "hear123";
        String filingNumber = "file123";
        String tenantId = "tenant123";
        LocalDate fromDate = LocalDate.now().plusDays(10);  // Invalid date range: fromDate is after toDate
        LocalDate toDate = LocalDate.now();
        Integer limit = 10;
        Integer offset = 0;
        String sortBy = "date";

        when(queryBuilder.getHearingSearchQuery(anyList(), eq(cnrNumber), eq(applicationNumber), eq(hearingId), eq(filingNumber), eq(tenantId), eq(fromDate), eq(toDate), eq(limit), eq(offset), eq(sortBy))).thenReturn("testHearingQuery");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(HearingRowMapper.class))).thenReturn(new ArrayList<>());

        // Act
        List<Hearing> result = hearingRepository.getHearings(cnrNumber, applicationNumber, hearingId, filingNumber, tenantId, fromDate, toDate, limit, offset, sortBy);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
