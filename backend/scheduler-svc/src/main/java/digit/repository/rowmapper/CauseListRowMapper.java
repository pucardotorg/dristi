package digit.repository.rowmapper;

import digit.web.models.CauseList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class CauseListRowMapper implements RowMapper<CauseList> {

    @Override
    public CauseList mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        String advocateNameString = resultSet.getString("advocate_names");
        List<String> advocateNames = new ArrayList<>();

        if (advocateNameString != null) {
            advocateNames = Arrays.asList(advocateNameString.split(","));
        }
        return CauseList.builder()
                .courtId(resultSet.getString("court_id"))
                .tenantId(resultSet.getString("tenant_id"))
                .judgeId(resultSet.getString("judge_id"))
                .hearingId(resultSet.getString("hearing_id"))
                .slot(resultSet.getString("slot"))
                .startTime(resultSet.getLong("start_time"))
                .endTime(resultSet.getLong("end_time"))
                .caseId(resultSet.getString("case_id"))
                .caseType(resultSet.getString("case_type"))
                .caseNumber(resultSet.getString("case_number"))
                .caseTitle(resultSet.getString("case_title"))
                .hearingDate(resultSet.getString("hearing_date"))
                .caseRegistrationDate(resultSet.getLong("case_registration_date"))
                .advocateNames(advocateNames)
                .build();
    }
}
