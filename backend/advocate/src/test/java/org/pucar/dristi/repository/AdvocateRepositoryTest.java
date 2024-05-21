package org.pucar.dristi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.repository.AdvocateRepository;
import org.pucar.dristi.repository.querybuilder.AdvocateQueryBuilder;
import org.pucar.dristi.repository.rowmapper.AdvocateDocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.AdvocateRowMapper;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdvocateRepositoryTest {

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
    public void testGetListApplicationsByStatus_WhenNoAdvocatesFound() {
        // Arrange
        String status = "Pending";
        String tenantId = "tenantId";
        Integer limit = 10;
        Integer offset = 0;

        when(queryBuilder.getAdvocateSearchQueryByStatus(anyString(), anyList(), anyString(), anyInt(), anyInt())).thenReturn("SELECT * FROM advocates WHERE status = ? AND tenant_id = ?");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(AdvocateRowMapper.class))).thenReturn(null);

        // Act
        List<Advocate> result = advocateRepository.getListApplicationsByStatus(status, tenantId, limit, offset);

        // Assert
        assertEquals(0, result.size());
        verify(queryBuilder, times(1)).getAdvocateSearchQueryByStatus(anyString(), anyList(), anyString(), anyInt(), anyInt());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(AdvocateRowMapper.class));
    }

    @Test
    public void testGetListApplicationsByApplicationNumber_WhenNoAdvocatesFound() {
        // Arrange
        String applicationNumber = "APP12345";
        String tenantId = "tenantId";
        Integer limit = 10;
        Integer offset = 0;

        when(queryBuilder.getAdvocateSearchQueryByApplicationNumber(anyString(), anyList(), anyString(), anyInt(), anyInt())).thenReturn("SELECT * FROM advocates WHERE application_number = ? AND tenant_id = ?");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(AdvocateRowMapper.class))).thenReturn(null);

        // Act
        List<Advocate> result = advocateRepository.getListApplicationsByApplicationNumber(applicationNumber, tenantId, limit, offset);

        // Assert
        assertEquals(0, result.size());
        verify(queryBuilder, times(1)).getAdvocateSearchQueryByApplicationNumber(anyString(), anyList(), anyString(), anyInt(), anyInt());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(AdvocateRowMapper.class));
    }
}
