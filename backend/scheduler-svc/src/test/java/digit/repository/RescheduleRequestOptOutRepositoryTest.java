package digit.repository;

import digit.repository.querybuilder.RescheduleRequestOptOutQueryBuilder;
import digit.repository.rowmapper.RescheduleRequestOptOutRowMapper;
import digit.web.models.OptOut;
import digit.web.models.OptOutSearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RescheduleRequestOptOutRepositoryTest {

    @Mock
    private RescheduleRequestOptOutRowMapper rescheduleRequestOptOutRowMapper;

    @Mock
    private RescheduleRequestOptOutQueryBuilder optOutQueryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private RescheduleRequestOptOutRepository rescheduleRequestOptOutRepository;

    private OptOutSearchCriteria optOutSearchCriteria;
    private List<OptOut> optOuts;

    @BeforeEach
    public void setUp() {
        optOutSearchCriteria = new OptOutSearchCriteria();
        optOuts = new ArrayList<>();
        OptOut optOut = new OptOut();
        optOuts.add(optOut);
    }

    @Test
    public void testGetOptOut_Success() {
        List<Object> preparedStmtList = new ArrayList<>();
        when(optOutQueryBuilder.getOptOutQuery(any(OptOutSearchCriteria.class), any(List.class), anyInt(), anyInt())).thenReturn("SELECT * FROM opt_outs");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RescheduleRequestOptOutRowMapper.class))).thenReturn(optOuts);

        List<OptOut> result = rescheduleRequestOptOutRepository.getOptOut(optOutSearchCriteria, 10, 0);

        assertEquals(1, result.size());
        verify(optOutQueryBuilder, times(1)).getOptOutQuery(any(OptOutSearchCriteria.class), any(List.class), anyInt(), anyInt());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RescheduleRequestOptOutRowMapper.class));
    }

    @Test
    public void testGetOptOut_EmptyResult() {
        List<Object> preparedStmtList = new ArrayList<>();
        when(optOutQueryBuilder.getOptOutQuery(any(OptOutSearchCriteria.class), any(List.class), anyInt(), anyInt())).thenReturn("SELECT * FROM opt_outs");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RescheduleRequestOptOutRowMapper.class))).thenReturn(Collections.emptyList());

        List<OptOut> result = rescheduleRequestOptOutRepository.getOptOut(optOutSearchCriteria, 10, 0);

        assertEquals(0, result.size());
        verify(optOutQueryBuilder, times(1)).getOptOutQuery(any(OptOutSearchCriteria.class), any(List.class), anyInt(), anyInt());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RescheduleRequestOptOutRowMapper.class));
    }

    @Test
    public void testGetOptOut_Exception() {
        List<Object> preparedStmtList = new ArrayList<>();
        when(optOutQueryBuilder.getOptOutQuery(any(OptOutSearchCriteria.class), any(List.class), anyInt(), anyInt())).thenReturn("SELECT * FROM opt_outs");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RescheduleRequestOptOutRowMapper.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            rescheduleRequestOptOutRepository.getOptOut(optOutSearchCriteria, 10, 0);
        });

        assertEquals("Database error", exception.getMessage());
        verify(optOutQueryBuilder, times(1)).getOptOutQuery(any(OptOutSearchCriteria.class), any(List.class), anyInt(), anyInt());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RescheduleRequestOptOutRowMapper.class));
    }
}
