package com.egov.icops_integrationkerala.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartyData {

    @JsonProperty("spartyAge")
    private String spartyAge;

    @JsonProperty("spartyName")
    private String spartyName;

    @JsonProperty("spartyType")
    private String spartyType;

    @JsonProperty("spartyEmail")
    private String spartyEmail;

    @JsonProperty("spartyState")
    private String spartyState;

    @JsonProperty("spartyGender")
    private String spartyGender;

    @JsonProperty("spartyMobile")
    private String spartyMobile;

    @JsonProperty("spartyAddress")
    private String spartyAddress;

    @JsonProperty("spartyDistrict")
    private String spartyDistrict;

    @JsonProperty("spartyRelationName")
    private String spartyRelationName;

    @JsonProperty("spartyRelationType")
    private String spartyRelationType;
}
