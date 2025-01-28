package org.drishti.esign.repository;

import org.drishti.esign.repository.rowmapper.EsignRowMapper;
import org.drishti.esign.web.models.ESignParameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class EsignRequestRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private EsignRowMapper rowMapper;

    @InjectMocks
    private EsignRequestRepository repository;


    @Test
    @DisplayName("should return Esign parameter object for id")
    public void shouldReturnESignParameterObjectForID() {
        String eSignQuery = "SELECT * FROM dristi_esign_pdf de WHERE id = ? LIMIT ? OFFSET ? ;";
        String id = "123654";

        ESignParameter mockedESign = new ESignParameter();

        List<ESignParameter> expectedList = Collections.singletonList(mockedESign);

        when(jdbcTemplate.query(eq(eSignQuery), eq(new Object[]{"123654", 1, 0}), eq(new int[]{12, 4, 4}), eq(rowMapper))).thenReturn(expectedList);

        ESignParameter recievedESignParameter = repository.getESignDetails(id);

        assertNotNull(recievedESignParameter);
        assertEquals(mockedESign.getId(), recievedESignParameter.getId());
        assertEquals(mockedESign.getFileStoreId(), recievedESignParameter.getFileStoreId());

    }
}
