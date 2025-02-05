package digit.repository.rowmapper;

import digit.web.models.CaseDiaryListItem;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static digit.config.ServiceConstants.ROW_MAPPER_EXCEPTION;

@Component
@Slf4j
public class DiaryRowMapper implements ResultSetExtractor<List<CaseDiaryListItem>> {

    @Override
    public List<CaseDiaryListItem> extractData(ResultSet rs) {

        List<CaseDiaryListItem> caseDiaryListItems = new ArrayList<>();

        try {

            while (rs.next()) {
                CaseDiaryListItem caseDiaryListItem = CaseDiaryListItem.builder()
                        .diaryId(UUID.fromString(rs.getString("id")))
                        .tenantId(rs.getString("tenantId"))
                        .date(rs.getLong("diaryDate"))
                        .diaryType(rs.getString("diaryType"))
                        .fileStoreID(rs.getString("fileStoreId"))
                        .build();
                caseDiaryListItems.add(caseDiaryListItem);
            }
            return caseDiaryListItems;

        } catch (Exception e) {
            log.error("Error occurred while processing document ResultSet: {}", e.getMessage());
            throw new CustomException(ROW_MAPPER_EXCEPTION, "Error occurred while processing document ResultSet: " + e.getMessage());
        }
    }
}
