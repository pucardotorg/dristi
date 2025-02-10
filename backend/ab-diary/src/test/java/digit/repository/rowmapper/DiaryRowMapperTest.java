package digit.repository.rowmapper;

import digit.web.models.CaseDiaryListItem;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static digit.config.ServiceConstants.ROW_MAPPER_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiaryRowMapperTest {

    @InjectMocks
    private DiaryRowMapper diaryRowMapper;

    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        diaryRowMapper = new DiaryRowMapper();
        resultSet = mock(ResultSet.class);
    }

    @Test
    void extractData_Success() throws Exception {
        // Mock ResultSet behavior
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("id")).thenReturn(UUID.randomUUID().toString());
        when(resultSet.getString("tenantId")).thenReturn("tenant-123");
        when(resultSet.getLong("diaryDate")).thenReturn(1672348800000L);
        when(resultSet.getString("diaryType")).thenReturn("TypeA");
        when(resultSet.getString("fileStoreId")).thenReturn("file-store-id-123");

        // Execute the method
        List<CaseDiaryListItem> result = diaryRowMapper.extractData(resultSet);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        CaseDiaryListItem item = result.get(0);
        assertNotNull(item.getDiaryId());
        assertEquals("tenant-123", item.getTenantId());
        assertEquals(1672348800000L, item.getDate());
        assertEquals("TypeA", item.getDiaryType());
        assertEquals("file-store-id-123", item.getFileStoreID());

        // Verify ResultSet interactions
        verify(resultSet, times(2)).next();
        verify(resultSet).getString("id");
        verify(resultSet).getString("tenantId");
        verify(resultSet).getLong("diaryDate");
        verify(resultSet).getString("diaryType");
        verify(resultSet).getString("fileStoreId");
    }

    @Test
    void extractData_EmptyResultSet() throws Exception {
        // Mock empty ResultSet
        when(resultSet.next()).thenReturn(false);

        // Execute the method
        List<CaseDiaryListItem> result = diaryRowMapper.extractData(resultSet);

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify ResultSet interactions
        verify(resultSet).next();
    }

    @Test
    void extractData_Exception() throws Exception {
        // Mock ResultSet behavior to throw an exception
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("id")).thenThrow(new SQLException("Database error"));

        // Execute and assert
        CustomException exception = assertThrows(CustomException.class, () -> diaryRowMapper.extractData(resultSet));
        assertEquals(ROW_MAPPER_EXCEPTION, exception.getCode());
        assertTrue(exception.getMessage().contains("Database error"));

        // Verify interactions
        verify(resultSet).next();
        verify(resultSet).getString("id");
    }
}
