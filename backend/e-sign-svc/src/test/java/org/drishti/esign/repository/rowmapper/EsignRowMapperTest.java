package org.drishti.esign.repository.rowmapper;

import org.drishti.esign.web.models.ESignParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EsignRowMapperTest {


    private ResultSet resultSet;
    private EsignRowMapper rowMapper;

    @BeforeEach
    public void setUp() {
        resultSet = Mockito.mock(ResultSet.class);
        rowMapper = new EsignRowMapper();
    }

    @Test
    @DisplayName("should return ESignParameter Object")
    public void shouldReturnESignParameterObject() throws SQLException {

        Mockito.when(resultSet.getString("id")).thenReturn("123");
        Mockito.when(resultSet.getString("authtype")).thenReturn("OTP");
        Mockito.when(resultSet.getString("filestoreid")).thenReturn("file-456");
        Mockito.when(resultSet.getString("tenantid")).thenReturn("tenant-k");
        Mockito.when(resultSet.getString("pagemodule")).thenReturn("module-name");
        Mockito.when(resultSet.getString("signplaceholder")).thenReturn("sign-placeholder");
        Mockito.when(resultSet.getString("signedfilestoreid")).thenReturn("signed-file-101");
        Mockito.when(resultSet.getString("filepath")).thenReturn("/path/to/file");

        Mockito.when(resultSet.getString("createdby")).thenReturn("creator-user");
        Mockito.when(resultSet.getLong("createdtime")).thenReturn(1627382934000L);
        Mockito.when(resultSet.getString("lastmodifiedby")).thenReturn("modifier-user");
        Mockito.when(resultSet.getLong("lastmodifiedtime")).thenReturn(1627383934000L);

        ESignParameter result = rowMapper.mapRow(resultSet, 1);

        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals("OTP", result.getAuthType());
        assertEquals("file-456", result.getFileStoreId());
        assertEquals("tenant-k", result.getTenantId());
        assertEquals("module-name", result.getPageModule());
        assertEquals("sign-placeholder", result.getSignPlaceHolder());
        assertEquals("signed-file-101", result.getSignedFileStoreId());
        assertEquals("/path/to/file", result.getFilePath());

        assertNotNull(result.getAuditDetails());
        assertEquals("creator-user", result.getAuditDetails().getCreatedBy());
        assertEquals(1627382934000L, result.getAuditDetails().getCreatedTime());
        assertEquals("modifier-user", result.getAuditDetails().getLastModifiedBy());
        assertEquals(1627383934000L, result.getAuditDetails().getLastModifiedTime());
    }

    @Test
    @DisplayName("should handle Sql Exception")
    public void shouldHandleSqlException() {
        try {
            Mockito.when(resultSet.getString("id")).thenThrow(new SQLException("Exception Occur at RowMapper"));

            assertThrows(SQLException.class, () -> rowMapper.mapRow(resultSet, 1));
        } catch (SQLException e) {
            fail("SQLException should be thrown and caught by assertThrows.");
        }
    }
}
