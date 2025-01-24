package digit.web.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * this entity is only for use by get API to return a list of items. it is not stored in DB, but is filled by getting data from DB. This will mostly be used for A Diary to show a list of A diaries across dates
 */
@Schema(description = "this entity is only for use by get API to return a list of items. it is not stored in DB, but is filled by getting data from DB. This will mostly be used for A Diary to show a list of A diaries across dates")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-01-15T12:45:29.792404900+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseDiaryListItem {
    @JsonProperty("diaryId")

    @Valid
    private UUID diaryId = null;

    @JsonProperty("tenantId")

    private String tenantId = null;

    @JsonProperty("date")

    private Long date = null;

    @JsonProperty("diaryType")

    private String diaryType = null;

    @JsonProperty("fileStoreID")

    private String fileStoreID = null;


}
