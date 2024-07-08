package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Amount;

import java.sql.ResultSet;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.ROW_MAPPER_EXCEPTION;

class AmountRowMapperTest {

    private AmountRowMapper amountRowMapper;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        amountRowMapper = new AmountRowMapper();
    }

    @Test
    void testExtractData_Success() throws Exception {
        // Mock ResultSet behavior
        UUID taskId = UUID.randomUUID();
        UUID amountId = UUID.randomUUID();

        when(resultSet.next()).thenReturn(true, false); // Simulate single row
        when(resultSet.getString("task_id")).thenReturn(taskId.toString());
        when(resultSet.getString("id")).thenReturn(amountId.toString());
        when(resultSet.getString("type")).thenReturn("Type");
        when(resultSet.getString("status")).thenReturn("Status");
        when(resultSet.getString("amount")).thenReturn("1000");
        when(resultSet.getString("paymentrefnumber")).thenReturn("Ref123");

        PGobject pgObject = new PGobject();
        pgObject.setValue("{}"); // Mock JSON string
        when(resultSet.getObject("additionaldetails")).thenReturn(pgObject);

        // Mock ObjectMapper behavior
        when(objectMapper.readTree("{}")).thenReturn(null); // Mock reading JSON

        // Invoke method
        Map<UUID, Amount> amountMap = amountRowMapper.extractData(resultSet);

        // Assertions
        assertEquals(1, amountMap.size());
        Amount amount = amountMap.values().iterator().next();
        assertEquals(amountId, amount.getId());
        assertEquals("Type", amount.getType());
        assertEquals("Status", amount.getStatus());
        assertEquals("1000", amount.getAmount());
        assertEquals("Ref123", amount.getPaymentRefNumber());
    }

    @Test
    void testExtractData_CustomException() throws Exception {
        // Simulate CustomException being thrown
        when(resultSet.next()).thenThrow(new CustomException("ERROR_CODE", "Error Message"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            amountRowMapper.extractData(resultSet);
        });

        assertEquals("ERROR_CODE", exception.getCode());
        assertEquals("Error Message", exception.getMessage());
    }

    @Test
    void testExtractData_GeneralException() throws Exception {
        // Simulate general exception being thrown
        when(resultSet.next()).thenThrow(new RuntimeException("General error"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            amountRowMapper.extractData(resultSet);
        });

        assertEquals(ROW_MAPPER_EXCEPTION, exception.getCode());
        assertEquals("Error occurred while processing Task amount ResultSet: General error", exception.getMessage());
    }
}
