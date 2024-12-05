package org.pucar.dristi.web.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * this is a summary representation of CourtCase object. A limited set of fields, formatted appropriately are returned. Since the object already exists, there are no field level rules set for this summary object
 */
@Schema(description = "this is a summary representation of CourtCase object. A limited set of fields, formatted appropriately are returned. Since the object already exists, there are no field level rules set for this summary object")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-12-03T13:11:23.212020900+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseSummary {
    @JsonProperty("cnrNumber")
    @NotNull

    @Size(min = 16, max = 16)
    private String cnrNumber = null;

    @JsonProperty("filingNumber")
    @NotNull

    @Size(min = 14, max = 14)
    private String filingNumber = null;

    @JsonProperty("filingDate")
    @NotNull

    private Long filingDate = null;

    @JsonProperty("registrationNumber")
    @NotNull

    @Size(min = 10, max = 18)
    private String registrationNumber = null;

    @JsonProperty("registrationDate")
    @NotNull

    private Long registrationDate = null;

    @JsonProperty("nextHearingDate")

    private Long nextHearingDate = null;
    @JsonProperty("caseType")
    @NotNull

    private CaseTypeEnum caseType = null;
    @JsonProperty("statutesAndSections")
    @NotNull

    private List<Object> statutesAndSections = new ArrayList<>();
    @JsonProperty("status")

    private StatusEnum status = null;
    @JsonProperty("subStage")
    @NotNull

    private String subStage = null;
    @JsonProperty("judgeName")
    @NotNull

    private String judgeName = null;
    @JsonProperty("complainant")
    @NotNull

    private String complainant = null;
    @JsonProperty("respondent")
    @NotNull

    private String respondent = null;
    @JsonProperty("advocateComplainant")
    @NotNull

    private String advocateComplainant = null;
    @JsonProperty("advocateRespondent")
    @NotNull

    private String advocateRespondent = null;

    public CaseSummary addStatutesAndSectionsItem(Object statutesAndSectionsItem) {
        this.statutesAndSections.add(statutesAndSectionsItem);
        return this;
    }

    /**
     * fixed value based on case type
     */
    public enum CaseTypeEnum {
        CRIMINAL_MISCELLANEOUS_PETITION("Criminal Miscellaneous Petition"),

        SUMMARY_TRAIL("Summary Trail");

        private String value;

        CaseTypeEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static CaseTypeEnum fromValue(String text) {
            for (CaseTypeEnum b : CaseTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }


    /**
     * CMP case not yet admitted, response will be Pending. ST Case already admitted, response will be Pending. ST case already registered, but queried using CMP, response will be Disposed.
     */
    public enum StatusEnum {
        PENDING("Pending"),

        DISPOSED("Disposed");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static StatusEnum fromValue(String text) {
            for (StatusEnum b : StatusEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }

}
