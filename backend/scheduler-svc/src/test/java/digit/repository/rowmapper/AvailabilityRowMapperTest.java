package digit.repository.rowmapper;

import digit.web.models.AvailabilityDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvailabilityRowMapperTest {

    @InjectMocks
    private AvailabilityRowMapper mapper;

    @Mock
    private ResultSet resultSet;

    @Test
    public void testMapRow() throws SQLException {
        when(resultSet.getString("date")).thenReturn("2024-07-04");
        when(resultSet.getDouble("hours")).thenReturn(8.0);

        AvailabilityDTO availabilityDTO = mapper.mapRow(resultSet, 1);

        assertEquals("2024-07-04", availabilityDTO.getDate());
        assertEquals(8.0, availabilityDTO.getOccupiedBandwidth());
    }

}