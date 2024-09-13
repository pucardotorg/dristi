package org.pucar.dristi.repository;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.repository.querybuilder.AdvocateQueryBuilder;
import org.pucar.dristi.repository.rowmapper.AdvocateDocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.AdvocateRowMapper;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

 class AdvocateRepositoryTest {

    @Mock
    private AdvocateQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private AdvocateRowMapper rowMapper;

    @Mock
    private AdvocateDocumentRowMapper advocateDocumentRowMapper;

    @InjectMocks
    private AdvocateRepository advocateRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void getApplications_Success() {
        // Arrange
        List<AdvocateSearchCriteria> searchCriteria = new ArrayList<>();
        // Populate searchCriteria with test data
        AdvocateSearchCriteria advocateSearchCriteria = new AdvocateSearchCriteria();
        advocateSearchCriteria.setId("id");
        advocateSearchCriteria.setApplicationNumber("appNumber");
        advocateSearchCriteria.setIndividualId("individualID");
        searchCriteria.add(advocateSearchCriteria);

        List<Advocate> listAdvocates = new ArrayList<Advocate>();
        Advocate advocate1 = new Advocate();
        advocate1.setId(UUID.fromString("921e3cc0-64df-490f-adc1-91c3492219e6"));
        Advocate advocate2 = new Advocate();
        advocate2.setId(UUID.fromString("d747abff-6d5d-47d7-99e2-fc70eaa856cb"));
        Advocate advocate3 = new Advocate();
        advocate3.setId(UUID.fromString("681230cd-702d-4add-b5e4-f97e71d9b622"));
        listAdvocates.add(advocate1);
        listAdvocates.add(advocate2);
        listAdvocates.add(advocate3);

        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        // Mock queryBuilder methods
        when(queryBuilder.getAdvocateSearchQuery(any(), anyList(),any(), any(), any(), any())).thenReturn("testAdvocateQuery");
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList(),any())).thenReturn("testDocumentQuery");

        // Mock jdbcTemplate methods
        when(jdbcTemplate.query(anyString(), any(Object[].class),any(), any(AdvocateRowMapper.class))).thenReturn(listAdvocates);
        when(jdbcTemplate.query(anyString(), any(Object[].class),any(), any(AdvocateDocumentRowMapper.class))).thenReturn(Collections.emptyMap());

        // Act
        List<AdvocateSearchCriteria> result = advocateRepository.getAdvocates(searchCriteria, tenantId, limit, offset);

        // Assert
        assertNotNull(result);
        assertTrue(!result.isEmpty()); // Since we're returning an empty list from jdbcTemplate
    }

    @Test
    void getApplications_Exception() {
        // Arrange
        List<AdvocateSearchCriteria> searchCriteria = null;

        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        // Mock queryBuilder methods
        when(queryBuilder.getAdvocateSearchQuery(any(), anyList(),any(), any(), any(), any())).thenReturn("testAdvocateQuery");
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList(),any())).thenReturn("testDocumentQuery");

        // Mock jdbcTemplate methods
        when(jdbcTemplate.query(anyString(), any(Object[].class),any(), any(AdvocateRowMapper.class))).thenReturn(new ArrayList<Advocate>());
        when(jdbcTemplate.query(anyString(), any(Object[].class),any(), any(AdvocateDocumentRowMapper.class))).thenThrow(RuntimeException.class);

        // Assert
        assertThrows(Exception.class, () -> advocateRepository.getAdvocates(searchCriteria, tenantId, limit, offset));
    }

    @Test
    void getApplications_CustomException() {
        // Arrange
        List<AdvocateSearchCriteria> searchCriteria = null;

        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        // Mock queryBuilder methods
        when(queryBuilder.getAdvocateSearchQuery(any(), anyList(),any(), any(), any(), any())).thenReturn("testAdvocateQuery");
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList(),any())).thenReturn("testDocumentQuery");

        // Mock jdbcTemplate methods
        when(jdbcTemplate.query(anyString(), any(Object[].class),any(), any(AdvocateRowMapper.class))).thenReturn(new ArrayList<Advocate>());
        when(jdbcTemplate.query(anyString(), any(Object[].class),any(), any(AdvocateDocumentRowMapper.class))).thenThrow(RuntimeException.class);

        // Assert
        assertThrows(CustomException.class, () -> advocateRepository.getAdvocates(searchCriteria, tenantId, limit, offset));
    }

    @Test
    void getListApplicationsByStatus_Success() {
        // Arrange
        String status = "testStatus";
        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        List<AdvocateSearchCriteria> searchCriteria = new ArrayList<>();
        // Populate searchCriteria with test data
        AdvocateSearchCriteria advocateSearchCriteria = new AdvocateSearchCriteria();
        advocateSearchCriteria.setId("id");
        advocateSearchCriteria.setApplicationNumber("appNumber");
        advocateSearchCriteria.setIndividualId("individualID");
        searchCriteria.add(advocateSearchCriteria);

        List<Advocate> listAdvocates = new ArrayList<Advocate>();
        Advocate advocate1 = new Advocate();
        advocate1.setId(UUID.fromString("921e3cc0-64df-490f-adc1-91c3492219e6"));
        Advocate advocate2 = new Advocate();
        advocate2.setId(UUID.fromString("d747abff-6d5d-47d7-99e2-fc70eaa856cb"));
        Advocate advocate3 = new Advocate();
        advocate3.setId(UUID.fromString("681230cd-702d-4add-b5e4-f97e71d9b622"));
        listAdvocates.add(advocate1);
        listAdvocates.add(advocate2);
        listAdvocates.add(advocate3);

        // Mock queryBuilder methods
        when(queryBuilder.getAdvocateSearchQueryByStatus(anyString(), anyList(), anyList(),anyString(), anyInt(), anyInt())).thenReturn("testAdvocateQuery");
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList(),anyList())).thenReturn("testDocumentQuery");

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(),any(AdvocateRowMapper.class))).thenReturn(listAdvocates);
        when(jdbcTemplate.query(anyString(), any(Object[].class),any(), any(AdvocateDocumentRowMapper.class))).thenReturn(Collections.emptyMap());

        // Act
        List<Advocate> result = advocateRepository.getListApplicationsByStatus(status, tenantId, limit, offset);

        // Assert
        assertNotNull(result);
        assertEquals(listAdvocates, result);
    }

    @Test
    void getListApplicationsByStatus_EmptySuccess() {
        // Arrange
        String status = "testStatus";
        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        List<AdvocateSearchCriteria> searchCriteria = new ArrayList<>();
        // Populate searchCriteria with test data
        AdvocateSearchCriteria advocateSearchCriteria = new AdvocateSearchCriteria();
        advocateSearchCriteria.setId("id");
        advocateSearchCriteria.setApplicationNumber("appNumber");
        advocateSearchCriteria.setIndividualId("individualID");
        searchCriteria.add(advocateSearchCriteria);

        List<Advocate> listAdvocates = new ArrayList<Advocate>();
        Advocate advocate1 = new Advocate();
        advocate1.setId(UUID.fromString("921e3cc0-64df-490f-adc1-91c3492219e6"));
        Advocate advocate2 = new Advocate();
        advocate2.setId(UUID.fromString("d747abff-6d5d-47d7-99e2-fc70eaa856cb"));
        Advocate advocate3 = new Advocate();
        advocate3.setId(UUID.fromString("681230cd-702d-4add-b5e4-f97e71d9b622"));

        // Mock queryBuilder methods
        when(queryBuilder.getAdvocateSearchQueryByStatus(anyString(), anyList(),anyList(), anyString(), anyInt(), anyInt())).thenReturn("testAdvocateQuery");
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList(),any())).thenReturn("testDocumentQuery");

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(),any(AdvocateRowMapper.class))).thenReturn(listAdvocates);
        when(jdbcTemplate.query(anyString(), any(Object[].class),any(), any(AdvocateDocumentRowMapper.class))).thenReturn(Collections.emptyMap());

        // Act
        List<Advocate> result = advocateRepository.getListApplicationsByStatus(status, tenantId, limit, offset);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty()); // Since we're returning an empty list from jdbcTemplate
    }

    @Test
    void getListApplicationsByApplicationNumber_Success() {
        // Arrange
        String applicationNumber = "APP12345";
        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        List<Advocate> listAdvocates = new ArrayList<Advocate>();
        Advocate advocate1 = new Advocate();
        advocate1.setId(UUID.fromString("921e3cc0-64df-490f-adc1-91c3492219e6"));
        Advocate advocate2 = new Advocate();
        advocate2.setId(UUID.fromString("d747abff-6d5d-47d7-99e2-fc70eaa856cb"));
        Advocate advocate3 = new Advocate();
        advocate3.setId(UUID.fromString("681230cd-702d-4add-b5e4-f97e71d9b622"));
        listAdvocates.add(advocate1);
        listAdvocates.add(advocate2);
        listAdvocates.add(advocate3);

        // Mock queryBuilder methods
        when(queryBuilder.getAdvocateSearchQueryByApplicationNumber(anyString(), anyList(),anyList(), anyString(), anyInt(), anyInt())).thenReturn("testAdvocateQuery");
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList(),any())).thenReturn("testDocumentQuery");

        // Populate advocateList with test data
        when(jdbcTemplate.query(anyString(), any(Object[].class),any(), any(AdvocateRowMapper.class))).thenReturn(listAdvocates);
        when(jdbcTemplate.query(anyString(), any(Object[].class),any(), any(AdvocateDocumentRowMapper.class))).thenReturn(Collections.emptyMap());

        // Act
        List<Advocate> result = advocateRepository.getListApplicationsByApplicationNumber(applicationNumber, tenantId, limit, offset);

        // Assert
        assertNotNull(result);
        assertEquals(listAdvocates, result);
    }

    @Test
    void getListApplicationsByApplicationNumber_EmptySuccess() {
        // Arrange
        String applicationNumber = "APP12345";
        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        List<Advocate> listAdvocates = new ArrayList<Advocate>();
        Advocate advocate1 = new Advocate();
        advocate1.setId(UUID.fromString("921e3cc0-64df-490f-adc1-91c3492219e6"));
        Advocate advocate2 = new Advocate();
        advocate2.setId(UUID.fromString("d747abff-6d5d-47d7-99e2-fc70eaa856cb"));
        Advocate advocate3 = new Advocate();
        advocate3.setId(UUID.fromString("681230cd-702d-4add-b5e4-f97e71d9b622"));

        // Mock queryBuilder methods
        when(queryBuilder.getAdvocateSearchQueryByApplicationNumber(anyString(), anyList(), anyList(),anyString(), anyInt(), anyInt())).thenReturn("testAdvocateQuery");
        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList(),any())).thenReturn("testDocumentQuery");

        when(jdbcTemplate.query(anyString(), any(Object[].class),any(), any(AdvocateRowMapper.class))).thenReturn(listAdvocates);
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(),any(AdvocateDocumentRowMapper.class))).thenReturn(Collections.emptyMap());

        // Act
        List<Advocate> result = advocateRepository.getListApplicationsByApplicationNumber(applicationNumber, tenantId, limit, offset);

        // Assert
        assertNotNull(result);
        assertEquals(listAdvocates, result);
    }

    @Test
     void testGetListApplicationsByApplicationNumber_WhenNoAdvocatesFound() {
        // Arrange
        String applicationNumber = "APP12345";
        String tenantId = "tenantId";
        Integer limit = 10;
        Integer offset = 0;

        when(queryBuilder.getAdvocateSearchQueryByApplicationNumber(anyString(), anyList(), anyList(),anyString(), anyInt(), anyInt())).thenReturn("SELECT * FROM advocates WHERE application_number = ? AND tenant_id = ?");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(),any(AdvocateRowMapper.class))).thenReturn(Collections.emptyList());
        // Act
        List<Advocate> result = advocateRepository.getListApplicationsByApplicationNumber(applicationNumber, tenantId, limit, offset);

        // Assert
        assertEquals(0, result.size());
        verify(queryBuilder, times(1)).getAdvocateSearchQueryByApplicationNumber(anyString(), anyList(), anyList(),anyString(), anyInt(), anyInt());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(),any(AdvocateRowMapper.class));
    }

    @Test
    void getListApplicationsByStatus_Exception() {
        // Arrange
        String status = "testStatus";
        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        List<AdvocateSearchCriteria> searchCriteria = new ArrayList<>();
        // Populate searchCriteria with test data
        AdvocateSearchCriteria advocateSearchCriteria = new AdvocateSearchCriteria();
        advocateSearchCriteria.setId("id");
        advocateSearchCriteria.setApplicationNumber("appNumber");
        advocateSearchCriteria.setIndividualId("individualID");
        searchCriteria.add(advocateSearchCriteria);

        List<Advocate> listAdvocates = new ArrayList<Advocate>();
        Advocate advocate1 = new Advocate();
        advocate1.setId(UUID.fromString("921e3cc0-64df-490f-adc1-91c3492219e6"));
        Advocate advocate2 = new Advocate();
        advocate2.setId(UUID.fromString("d747abff-6d5d-47d7-99e2-fc70eaa856cb"));
        Advocate advocate3 = new Advocate();
        advocate3.setId(UUID.fromString("681230cd-702d-4add-b5e4-f97e71d9b622"));
        listAdvocates.add(advocate1);
        listAdvocates.add(advocate2);
        listAdvocates.add(advocate3);

        // Mock queryBuilder methods
        when(queryBuilder.getAdvocateSearchQueryByStatus(anyString(), anyList(), any(),anyString(), anyInt(), anyInt())).thenThrow(new RuntimeException());

        assertThrows(Exception.class, () -> advocateRepository.getListApplicationsByStatus(status, tenantId, limit, offset));
    }

    @Test
    void getListApplicationsByStatus_CustomException() {
        // Arrange
        String status = "testStatus";
        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        List<AdvocateSearchCriteria> searchCriteria = new ArrayList<>();
        // Populate searchCriteria with test data
        AdvocateSearchCriteria advocateSearchCriteria = new AdvocateSearchCriteria();
        advocateSearchCriteria.setId("id");
        advocateSearchCriteria.setApplicationNumber("appNumber");
        advocateSearchCriteria.setIndividualId("individualID");
        searchCriteria.add(advocateSearchCriteria);

        List<Advocate> listAdvocates = new ArrayList<Advocate>();
        Advocate advocate1 = new Advocate();
        advocate1.setId(UUID.fromString("921e3cc0-64df-490f-adc1-91c3492219e6"));
        Advocate advocate2 = new Advocate();
        advocate2.setId(UUID.fromString("d747abff-6d5d-47d7-99e2-fc70eaa856cb"));
        Advocate advocate3 = new Advocate();
        advocate3.setId(UUID.fromString("681230cd-702d-4add-b5e4-f97e71d9b622"));
        listAdvocates.add(advocate1);
        listAdvocates.add(advocate2);
        listAdvocates.add(advocate3);

        // Mock queryBuilder methods
        when(queryBuilder.getAdvocateSearchQueryByStatus(anyString(), anyList(),any(), anyString(), anyInt(), anyInt())).thenThrow(new CustomException());

        assertThrows(CustomException.class, () -> advocateRepository.getListApplicationsByStatus(status, tenantId, limit, offset));
    }

    @Test
    void getListApplicationsByApplicationNumber_Exception() {
        // Arrange
        String applicationNumber = "APP12345";
        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        List<Advocate> listAdvocates = new ArrayList<Advocate>();
        Advocate advocate1 = new Advocate();
        advocate1.setId(UUID.fromString("921e3cc0-64df-490f-adc1-91c3492219e6"));
        Advocate advocate2 = new Advocate();
        advocate2.setId(UUID.fromString("d747abff-6d5d-47d7-99e2-fc70eaa856cb"));
        Advocate advocate3 = new Advocate();
        advocate3.setId(UUID.fromString("681230cd-702d-4add-b5e4-f97e71d9b622"));
        listAdvocates.add(advocate1);
        listAdvocates.add(advocate2);
        listAdvocates.add(advocate3);

        // Mock queryBuilder methods
        when(queryBuilder.getAdvocateSearchQueryByApplicationNumber(anyString(), anyList(), any(),anyString(), anyInt(), anyInt())).thenThrow(new RuntimeException());

        assertThrows(Exception.class, () -> advocateRepository.getListApplicationsByApplicationNumber(applicationNumber, tenantId, limit, offset));
    }

    @Test
    void getListApplicationsByApplicationNumber_CustomException() {
        // Arrange
        String applicationNumber = "APP12345";
        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        List<Advocate> listAdvocates = new ArrayList<Advocate>();
        Advocate advocate1 = new Advocate();
        advocate1.setId(UUID.fromString("921e3cc0-64df-490f-adc1-91c3492219e6"));
        Advocate advocate2 = new Advocate();
        advocate2.setId(UUID.fromString("d747abff-6d5d-47d7-99e2-fc70eaa856cb"));
        Advocate advocate3 = new Advocate();
        advocate3.setId(UUID.fromString("681230cd-702d-4add-b5e4-f97e71d9b622"));
        listAdvocates.add(advocate1);
        listAdvocates.add(advocate2);
        listAdvocates.add(advocate3);

        // Mock queryBuilder methods
        when(queryBuilder.getAdvocateSearchQueryByApplicationNumber(anyString(), anyList(),any(), anyString(), anyInt(), anyInt())).thenThrow(new CustomException());

        assertThrows(CustomException.class, () -> advocateRepository.getListApplicationsByApplicationNumber(applicationNumber, tenantId, limit, offset));
    }
}
