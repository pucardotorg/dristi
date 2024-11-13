package org.pucar.dristi.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.repository.querybuilder.WitnessQueryBuilder;
import org.pucar.dristi.repository.rowmapper.WitnessRowMapper;
import org.pucar.dristi.web.models.Witness;
import org.pucar.dristi.web.models.WitnessSearchCriteria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class WitnessRepositoryTest {

    @Mock
    private WitnessQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private WitnessRowMapper rowMapper;

    @InjectMocks
    private WitnessRepository witnessRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetApplicationsEmptySuccess() {
        // Mock the behavior of getWitnessesSearchQuery
        List<WitnessSearchCriteria> searchCriteria = new ArrayList<>();
        when(queryBuilder.getWitnessesSearchQuery(any(), any(), any())).thenReturn("SELECT * FROM witnesses");

        // Mock the behavior of JdbcTemplate to return an empty list
        when(jdbcTemplate.query(anyString(), (PreparedStatementSetter) any(), any(WitnessRowMapper.class)))
                .thenReturn(Collections.emptyList());

        // Call the method under test
        List<Witness> result = witnessRepository.getApplications(searchCriteria);

        // Assert
        assertTrue(result.isEmpty(), "Returned witnesses should be empty");
    }

    @Test
    public void testGetApplicationsSuccess() {
        // Mock the behavior of getWitnessesSearchQuery
        Witness witness = Witness.builder().cnrNumber("cnrNumber").build();
        List<Witness> list = new ArrayList<>();
        list.add(witness);
        List<WitnessSearchCriteria> searchCriteria = new ArrayList<>();
        when(queryBuilder.getWitnessesSearchQuery(any(), any(), any())).thenReturn("SELECT * FROM witnesses");

        // Mock the behavior of JdbcTemplate to return an empty list
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(WitnessRowMapper.class)))
                .thenReturn(list);

        // Call the method under test
        List<Witness> result = witnessRepository.getApplications(searchCriteria);

        // Assert
        assertTrue(!result.isEmpty(), "Returned witnesses should not be empty");
    }

    @Test
    public void testGetApplicationsException() {
        // Mock the behavior of getWitnessesSearchQuery
        Witness witness = Witness.builder().cnrNumber("cnrNumber").build();
        List<Witness> list = new ArrayList<>();
        list.add(witness);
        List<WitnessSearchCriteria> searchCriteria = new ArrayList<>();
        when(queryBuilder.getWitnessesSearchQuery(any(), any(), any())).thenReturn("SELECT * FROM witnesses");

        // Mock the behavior of JdbcTemplate to return an empty list
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(),  any(WitnessRowMapper.class)))
                .thenThrow(new RuntimeException("Error"));

        // Assert
        assertThrows(Exception.class, () -> witnessRepository.getApplications(searchCriteria));
    }

    @Test
    public void testGetApplicationsCustomException() {
        // Mock the behavior of getWitnessesSearchQuery
        Witness witness = Witness.builder().cnrNumber("cnrNumber").build();
        List<Witness> list = new ArrayList<>();
        list.add(witness);
        List<WitnessSearchCriteria> searchCriteria = new ArrayList<>();
        when(queryBuilder.getWitnessesSearchQuery(any(), any(), any())).thenReturn("SELECT * FROM witnesses");

        // Mock the behavior of JdbcTemplate to return an empty list
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(WitnessRowMapper.class)))
                .thenThrow(new CustomException());

        // Assert
        assertThrows(CustomException.class, () -> witnessRepository.getApplications(searchCriteria));
    }

}


