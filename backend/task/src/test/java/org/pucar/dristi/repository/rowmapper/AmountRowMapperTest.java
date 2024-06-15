package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Amount;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AmountRowMapperTest {

    @InjectMocks
    private AmountRowMapper amountRowMapper;

    @Mock
    private ResultSet rs;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

//    @Test
//    void testExtractDataSuccess() throws Exception {
//        UUID taskId = UUID.randomUUID();
//        UUID amountId = UUID.randomUUID();
//        String type = "testType";
//        String status = "testStatus";
//        String amountValue = "100";
//        String paymentRefNumber = "testPaymentRefNumber";
//
//        when(rs.next()).thenReturn(true).thenReturn(false);
//        when(rs.getString("task_id")).thenReturn(taskId.toString());
//        when(rs.getString("id")).thenReturn(amountId.toString());
//        when(rs.getString("type")).thenReturn(type);
//        when(rs.getString("status")).thenReturn(status);
//        when(rs.getString("amount")).thenReturn(amountValue);
//        when(rs.getString("paymentrefnumber")).thenReturn(paymentRefNumber);
//
//        PGobject pgObject = new PGobject();
//        pgObject.setType("json");
//        pgObject.setValue("{\"key\":\"value\"}");
//        when(rs.getObject("additionaldetails")).thenReturn(pgObject);
//
//        Map<UUID, Amount> result = amountRowMapper.extractData(rs);
//
//        assertNotNull(result);
//        assertTrue(result.containsKey(taskId));
//        Amount amount = result.get(taskId);
//        assertNotNull(amount);
//        assertEquals(amountId, amount.getId());
//        assertEquals(type, amount.getType());
//        assertEquals(status, amount.getStatus());
//        assertEquals(amountValue, amount.getAmount());
//        assertEquals(paymentRefNumber, amount.getPaymentRefNumber());
//        assertNotNull(amount.getAdditionalDetails());
//
//        verify(rs, times(1)).getString("task_id");
//        verify(rs, times(1)).getString("id");
//        verify(rs, times(1)).getString("type");
//        verify(rs, times(1)).getString("status");
//        verify(rs, times(1)).getString("amount");
//        verify(rs, times(1)).getString("paymentrefnumber");
//        verify(rs, times(1)).getObject("additionaldetails");
//    }
//
//    @Test
//    void testExtractDataNoAdditionalDetails() throws Exception {
//        UUID taskId = UUID.randomUUID();
//        UUID amountId = UUID.randomUUID();
//        String type = "testType";
//        String status = "testStatus";
//        String amountValue = "100";
//        String paymentRefNumber = "testPaymentRefNumber";
//
//        when(rs.next()).thenReturn(true).thenReturn(false);
//        when(rs.getString("task_id")).thenReturn(taskId.toString());
//        when(rs.getString("id")).thenReturn(amountId.toString());
//        when(rs.getString("type")).thenReturn(type);
//        when(rs.getString("status")).thenReturn(status);
//        when(rs.getString("amount")).thenReturn(amountValue);
//        when(rs.getString("paymentrefnumber")).thenReturn(paymentRefNumber);
//        when(rs.getObject("additionaldetails")).thenReturn(null);
//
//        Map<UUID, Amount> result = amountRowMapper.extractData(rs);
//
//        assertNotNull(result);
//        assertTrue(result.containsKey(taskId));
//        Amount amount = result.get(taskId);
//        assertNotNull(amount);
//        assertEquals(amountId, amount.getId());
//        assertEquals(type, amount.getType());
//        assertEquals(status, amount.getStatus());
//        assertEquals(amountValue, amount.getAmount());
//        assertEquals(paymentRefNumber, amount.getPaymentRefNumber());
//        assertNull(amount.getAdditionalDetails());
//
//        verify(rs, times(1)).getString("task_id");
//        verify(rs, times(1)).getString("id");
//        verify(rs, times(1)).getString("type");
//        verify(rs, times(1)).getString("status");
//        verify(rs, times(1)).getString("amount");
//        verify(rs, times(1)).getString("paymentrefnumber");
//        verify(rs, times(1)).getObject("additionaldetails");
//    }

    @Test
    void testExtractDataSQLException() throws Exception {
        when(rs.next()).thenThrow(new SQLException("DB Error"));

        CustomException exception = assertThrows(CustomException.class, () -> amountRowMapper.extractData(rs));
        assertEquals("Error in row mapper", exception.getCode());
        assertEquals("Error occurred while processing Task amount ResultSet: DB Error", exception.getMessage());

        verify(rs, times(1)).next();
    }

    @Test
    void testExtractDataCustomException() throws Exception {
        when(rs.next()).thenReturn(true);
        when(rs.getString("task_id")).thenThrow(new CustomException("CUSTOM_ERROR", "Custom error"));

        CustomException exception = assertThrows(CustomException.class, () -> amountRowMapper.extractData(rs));
        assertEquals("CUSTOM_ERROR", exception.getCode());
        assertEquals("Custom error", exception.getMessage());

        verify(rs, times(1)).next();
        verify(rs, times(1)).getString("task_id");
    }
}
