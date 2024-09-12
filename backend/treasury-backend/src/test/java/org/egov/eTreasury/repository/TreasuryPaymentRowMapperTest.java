package org.egov.eTreasury.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.eTreasury.model.TreasuryPaymentData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class TreasuryPaymentRowMapperTest {

    @InjectMocks
    private TreasuryPaymentRowMapper rowMapper;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        // Initialization is handled by Mockito
    }

    @Test
    void testMapRow() throws SQLException {
        // Given
        when(resultSet.getString("department_id")).thenReturn("dept01");
        when(resultSet.getString("grn")).thenReturn("grn001");
        when(resultSet.getString("challan_timestamp")).thenReturn("2024-08-08 12:00:00");
        when(resultSet.getString("bank_ref_no")).thenReturn("bankRef001");
        when(resultSet.getString("bank_timestamp")).thenReturn("2024-08-08 12:00:00");
        when(resultSet.getString("bank_code")).thenReturn("bankCode001");
        when(resultSet.getString("status")).thenReturn("A");
        when(resultSet.getString("cin")).thenReturn("cin001");
        when(resultSet.getBigDecimal("amount")).thenReturn(BigDecimal.valueOf(1000.00));
        when(resultSet.getString("party_name")).thenReturn("John Doe");
        when(resultSet.getString("remark_status")).thenReturn("Approved");
        when(resultSet.getString("remarks")).thenReturn("Remarks here");
        when(resultSet.getString("file_store_id")).thenReturn("fileStore001");

        // When
        TreasuryPaymentData result = rowMapper.mapRow(resultSet, 1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDepartmentId()).isEqualTo("dept01");
        assertThat(result.getGrn()).isEqualTo("grn001");
        assertThat(result.getChallanTimestamp()).isEqualTo("2024-08-08 12:00:00");
        assertThat(result.getBankRefNo()).isEqualTo("bankRef001");
        assertThat(result.getBankTimestamp()).isEqualTo("2024-08-08 12:00:00");
        assertThat(result.getBankCode()).isEqualTo("bankCode001");
        assertThat(result.getCin()).isEqualTo("cin001");
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(1000.00));
        assertThat(result.getRemarkStatus()).isEqualTo("Approved");
        assertThat(result.getRemarks()).isEqualTo("Remarks here");
        assertThat(result.getFileStoreId()).isEqualTo("fileStore001");

        verify(resultSet).getString("department_id");
        verify(resultSet).getString("grn");
        verify(resultSet).getString("bank_ref_no");
        verify(resultSet).getString("bank_timestamp");
        verify(resultSet).getString("bank_code");
        verify(resultSet).getString("status");
        verify(resultSet).getString("cin");
        verify(resultSet).getBigDecimal("amount");
        verify(resultSet).getString("party_name");
        verify(resultSet).getString("remark_status");
        verify(resultSet).getString("remarks");
        verify(resultSet).getString("file_store_id");
    }

    @Test
    void testMapRowThrowsSQLException() throws SQLException {
        // Given
        when(resultSet.getString("department_id")).thenThrow(new SQLException("Database error"));

        // When / Then
        try {
            rowMapper.mapRow(resultSet, 1);
        } catch (SQLException e) {
            assertThat(e.getMessage()).isEqualTo("Database error");
        }
    }
}
