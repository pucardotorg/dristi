package com.egov.icops_integrationkerala.repository;

import com.egov.icops_integrationkerala.model.IcopsTracker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IcopsRepositoryTest {

    @InjectMocks
    private IcopsRepository icopsRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private IcopsRowMapper rowMapper;

    @Mock
    private IcopsQueryBuilder queryBuilder;

    @Test
    void testGetIcopsTracker() {
        // Arrange
        String processUniqueId = "processUniqueId";

        ArrayList<Object> preparedStmtList = new ArrayList<>();

        when(queryBuilder.getIcopsTracker(processUniqueId, preparedStmtList)).thenReturn("query");

        // Act
        List<IcopsTracker> list =  icopsRepository.getIcopsTracker(processUniqueId);

        // Assert
        assertNotNull(list);
    }
}
