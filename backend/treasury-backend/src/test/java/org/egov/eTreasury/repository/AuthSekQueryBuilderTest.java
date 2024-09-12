package org.egov.eTreasury.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class AuthSekQueryBuilderTest {

    private AuthSekQueryBuilder queryBuilder;

    @BeforeEach
    void setUp() {
        queryBuilder = new AuthSekQueryBuilder();
    }

    @Test
    void testGetAuthSekQueryWithAuthToken() {
        // Given
        String authToken = "testAuthToken";
        List<String> preparedStmtList = new ArrayList<>();

        // When
        String query = queryBuilder.getAuthSekQuery(authToken, preparedStmtList);

        // Then
        assertThat(preparedStmtList).containsExactly("testAuthToken");
        assertThat(query).isEqualTo(
                "SELECT auth_token, decrypted_sek, bill_id, business_service, service_number, total_due, mobile_number, paid_by, session_time, department_id  FROM auth_sek_session_data  WHERE  auth_token = ? ORDER BY session_time "
        );
    }

    @Test
    void testGetAuthSekQueryWithoutAuthToken() {
        // Given
        String authToken = null;
        List<String> preparedStmtList = new ArrayList<>();

        // When
        String query = queryBuilder.getAuthSekQuery(authToken, preparedStmtList);

        // Then
        assertThat(query).isEqualTo("SELECT auth_token, decrypted_sek, bill_id, business_service, service_number, total_due, mobile_number, paid_by, session_time, department_id  FROM auth_sek_session_data ORDER BY session_time ");
        Assertions.assertNotNull(preparedStmtList);
    }

    @Test
    void testGetAuthSekQueryWithAuthToken_1() {
        // Given
        String authToken = "testAuthToken";
        List<String> preparedStmtList = new ArrayList<>();
        preparedStmtList.add("testAuthToken");

        // When
        String query = queryBuilder.getAuthSekQuery(authToken, preparedStmtList);

        // Then
        assertThat(query).isEqualTo(
                "SELECT auth_token, decrypted_sek, bill_id, business_service, service_number, total_due, mobile_number, paid_by, session_time, department_id  FROM auth_sek_session_data  AND  auth_token = ? ORDER BY session_time "
        );
        Assertions.assertNotNull(preparedStmtList);
    }
}
