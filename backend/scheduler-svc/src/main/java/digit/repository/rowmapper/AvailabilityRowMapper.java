package digit.repository.rowmapper;

import digit.web.models.AvailabilityDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Slf4j
public class AvailabilityRowMapper implements RowMapper<AvailabilityDTO> {

    @Override
    public AvailabilityDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return AvailabilityDTO.builder()
                .date(rs.getString("date"))
                .occupiedBandwidth(rs.getDouble("hours"))
                .build();
    }
}
