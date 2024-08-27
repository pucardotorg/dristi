package org.egov.demand.repository.rowmapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.node.MissingNode;

import java.math.BigDecimal;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.demand.util.Util;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {BillRowMapperV2.class})
@ExtendWith(SpringExtension.class)
class BillRowMapperV2Test {
    @Autowired
    private BillRowMapperV2 billRowMapperV2;

    @MockBean
    private Util util;

    @Test
    void testExtractData() throws SQLException {
        when(this.util.getJsonValue((org.postgresql.util.PGobject) any())).thenReturn(MissingNode.getInstance());
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getObject((String) any())).thenThrow(new EmptyResultDataAccessException(3));
        when(resultSet.getString((String) any())).thenThrow(new EmptyResultDataAccessException(3));
        when(resultSet.getBigDecimal((String) any())).thenThrow(new EmptyResultDataAccessException(3));
        when(resultSet.getLong((String) any())).thenThrow(new EmptyResultDataAccessException(3));
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        assertThrows(EmptyResultDataAccessException.class, () -> this.billRowMapperV2.extractData(resultSet));
        verify(resultSet).next();
        verify(resultSet).getString((String) any());
    }


    @Test
    void testExtractData2() throws SQLException {
        when(this.util.getJsonValue((org.postgresql.util.PGobject) any())).thenReturn(MissingNode.getInstance());
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt((String) any())).thenReturn(1);
        when(resultSet.getObject((String) any())).thenReturn(null);
        when(resultSet.getString((String) any())).thenReturn("String");
        when(resultSet.getBigDecimal((String) any())).thenReturn(BigDecimal.valueOf(42L));
        when(resultSet.getLong((String) any())).thenReturn(1L);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        assertEquals(1, this.billRowMapperV2.extractData(resultSet).size());
        verify(this.util, atLeast(1)).getJsonValue((org.postgresql.util.PGobject) any());
        verify(resultSet, atLeast(1)).next();
        verify(resultSet, atLeast(1)).getInt((String) any());
        verify(resultSet, atLeast(1)).getObject((String) any());
        verify(resultSet, atLeast(1)).getString((String) any());
        verify(resultSet, atLeast(1)).getBigDecimal((String) any());
        verify(resultSet, atLeast(1)).getLong((String) any());
    }


}

