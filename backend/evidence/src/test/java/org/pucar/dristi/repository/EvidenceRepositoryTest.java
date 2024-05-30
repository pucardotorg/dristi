package org.pucar.dristi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.repository.querybuilder.EvidenceQueryBuilder;
import org.pucar.dristi.repository.rowmapper.CommentRowMapper;
import org.pucar.dristi.repository.rowmapper.DocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.EvidenceRowMapper;
import org.pucar.dristi.web.models.Artifact;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EvidenceRepositoryTest {

    @Mock
    private EvidenceQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private EvidenceRowMapper evidenceRowMapper;

    @Mock
    private DocumentRowMapper documentRowMapper;

    @Mock
    private CommentRowMapper commentRowMapper;

    @InjectMocks
    private EvidenceRepository evidenceRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetArtifacts() {
        // Mock query result from the query builder
        when(queryBuilder.getArtifactSearchQuery(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("SELECT * FROM artifacts WHERE ...");

        // Mock jdbcTemplate behavior
        List<Artifact> expectedArtifacts = Collections.singletonList(new Artifact(/* initialize with necessary values */));
        when(jdbcTemplate.query(anyString(), any(EvidenceRowMapper.class))).thenReturn(expectedArtifacts);

        // Invoke the method under test
        List<Artifact> actualArtifacts = evidenceRepository.getArtifacts(anyString(), anyString(), anyString(), anyString(),anyString(), anyString(), anyString());

        // Verify behavior
        assertEquals(expectedArtifacts, actualArtifacts);
        verify(queryBuilder, times(1)).getArtifactSearchQuery(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
        verify(jdbcTemplate, times(1)).query(anyString(), any(EvidenceRowMapper.class));
    }
}

