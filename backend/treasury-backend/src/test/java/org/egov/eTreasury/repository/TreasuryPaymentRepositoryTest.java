package org.egov.eTreasury.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.egov.eTreasury.model.TreasuryPaymentData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

@ExtendWith(MockitoExtension.class)
class TreasuryPaymentRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private TreasuryPaymentQueryBuilder treasuryPaymentQueryBuilder;

    @Mock
    private TreasuryPaymentRowMapper treasuryPaymentRowMapper;

    @InjectMocks
    private TreasuryPaymentRepository treasuryPaymentRepository;

    @BeforeEach
    void setUp() {
        // Initialization is handled by Mockito
    }

    @Test
    void testGetTreasuryPaymentData() {
        // Given
        String billId = "testBillId";
        List<String> preparedStmtList = new ArrayList<>();
        String query = "SELECT department_id, grn, challan_timestamp, bank_ref_no, bank_timestamp, bank_code, status, cin, amount, party_name, remark_status, remarks, file_store_id FROM treasury_payment_data WHERE department_id IN (SELECT department_id FROM auth_sek_session_data WHERE bill_id = ? ORDER BY session_time )";
        List<TreasuryPaymentData> expectedResults = new ArrayList<>();
        expectedResults.add(new TreasuryPaymentData()); // Add a sample TreasuryPaymentData object

        // Mock behavior
        when(treasuryPaymentQueryBuilder.getTreasuryPaymentQuery(billId, preparedStmtList)).thenReturn(query);
        when(jdbcTemplate.query(query, treasuryPaymentRowMapper, preparedStmtList.toArray())).thenReturn(expectedResults);

        // When
        List<TreasuryPaymentData> actualResults = treasuryPaymentRepository.getTreasuryPaymentData(billId);

        // Then
        assertThat(actualResults).isEqualTo(expectedResults);
        verify(treasuryPaymentQueryBuilder).getTreasuryPaymentQuery(billId, preparedStmtList);
        verify(jdbcTemplate).query(query, treasuryPaymentRowMapper, preparedStmtList.toArray());
    }

    @Test
    void testGetTreasuryPaymentDataWithNullBillId() {
        // Given
        List<String> preparedStmtList = new ArrayList<>();
        String query = "SELECT department_id, grn, challan_timestamp, bank_ref_no, bank_timestamp, bank_code, status, cin, amount, party_name, remark_status, remarks, file_store_id FROM treasury_payment_data";
        List<TreasuryPaymentData> expectedResults = new ArrayList<>();
        expectedResults.add(new TreasuryPaymentData()); // Add a sample TreasuryPaymentData object

        // Mock behavior
        when(treasuryPaymentQueryBuilder.getTreasuryPaymentQuery(null, preparedStmtList)).thenReturn(query);
        when(jdbcTemplate.query(query, treasuryPaymentRowMapper, preparedStmtList.toArray())).thenReturn(expectedResults);

        // When
        List<TreasuryPaymentData> actualResults = treasuryPaymentRepository.getTreasuryPaymentData(null);

        // Then
        assertThat(actualResults).isEqualTo(expectedResults);
        verify(treasuryPaymentQueryBuilder).getTreasuryPaymentQuery(null, preparedStmtList);
        verify(jdbcTemplate).query(query, treasuryPaymentRowMapper, preparedStmtList.toArray());
    }
}

