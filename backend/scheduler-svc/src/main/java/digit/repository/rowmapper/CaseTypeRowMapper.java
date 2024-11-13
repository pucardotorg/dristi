package digit.repository.rowmapper;

import digit.web.models.CaseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Slf4j
public class CaseTypeRowMapper implements RowMapper<CaseType> {

    @Override
    public CaseType mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return CaseType.builder()
                .id(resultSet.getInt("id"))
                .caseType(resultSet.getString("casetype"))
                .description(resultSet.getString("description"))
                .priority(resultSet.getInt("priority"))
                .isActive(resultSet.getBoolean("isactive"))
                .build();
    }
}
