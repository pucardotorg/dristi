package org.pucar.dristi.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.pucar.dristi.web.models.CaseSummary;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpenApiCaseSummary {

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

    public OpenApiCaseSummary addStatutesAndSectionsItem(Object statutesAndSectionsItem) {
        this.statutesAndSections.add(statutesAndSectionsItem);
        return this;
    }

    /**
     * fixed value based on case type
     */
    public enum CaseTypeEnum {
        CMP("Criminal Miscellaneous Petition"),

        ST("Summary Trail");

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