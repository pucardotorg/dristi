package org.egov.eTreasury.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.egov.eTreasury.model.AuthSek;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

@ExtendWith(MockitoExtension.class)
class AuthSekRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private AuthSekQueryBuilder queryBuilder;

    @Mock
    private AuthSekRowMapper rowMapper;

    @InjectMocks
    private AuthSekRepository authSekRepository;

    @BeforeEach
    void setUp() {
        // Initialization is handled by Mockito
    }

    @Test
    void testGetAuthSek() {
        // Given
        String authToken = "testAuthToken";
        List<String> preparedStmtList = new ArrayList<>();
        String query = "SELECT * FROM auth_sek_session_data WHERE auth_token = ?";
        List<AuthSek> expectedResults = new ArrayList<>();
        expectedResults.add(new AuthSek()); // Add a sample AuthSek object

        // Mock behavior
        when(queryBuilder.getAuthSekQuery(authToken, preparedStmtList)).thenReturn(query);
        when(jdbcTemplate.query(query, rowMapper, preparedStmtList.toArray())).thenReturn(expectedResults);

        // When
        List<AuthSek> actualResults = authSekRepository.getAuthSek(authToken);

        // Then
        assertThat(actualResults).isEqualTo(expectedResults);
        verify(queryBuilder).getAuthSekQuery(authToken, preparedStmtList);
        verify(jdbcTemplate).query(query, rowMapper, preparedStmtList.toArray());
    }

    @Test
    void testGetAuthSekWithNullAuthToken() {
        // Given
        List<String> preparedStmtList = new ArrayList<>();
        String query = "SELECT * FROM auth_sek_session_data";
        List<AuthSek> expectedResults = new ArrayList<>();
        expectedResults.add(new AuthSek()); // Add a sample AuthSek object

        // Mock behavior
        when(queryBuilder.getAuthSekQuery(null, preparedStmtList)).thenReturn(query);
        when(jdbcTemplate.query(query, rowMapper, preparedStmtList.toArray())).thenReturn(expectedResults);

        // When
        List<AuthSek> actualResults = authSekRepository.getAuthSek(null);

        // Then
        assertThat(actualResults).isEqualTo(expectedResults);
        verify(queryBuilder).getAuthSekQuery(null, preparedStmtList);
        verify(jdbcTemplate).query(query, rowMapper, preparedStmtList.toArray());
    }
}

