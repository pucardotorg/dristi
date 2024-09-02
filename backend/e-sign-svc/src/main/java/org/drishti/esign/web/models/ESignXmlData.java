package org.drishti.esign.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ESignXmlData {

    @JsonProperty("ver")
    private String ver;

    @JsonProperty("sc")
    private String sc;

    @JsonProperty("ts")
    private String ts;

    @JsonProperty("txn")
    private String txn;

    @JsonProperty("ekycId")
    private String ekycId;

    @JsonProperty("ekycIdType")
    private String ekycIdType;

    @JsonProperty("aspId")
    private String aspId;

    @JsonProperty("authMode")
    private String authMode;

    @JsonProperty("responseSigType")
    private String responseSigType;

    @JsonProperty("responseUrl")
    private String responseUrl;

    @JsonProperty("id")
    private String id;

    @JsonProperty("hashAlgorithm")
    private String hashAlgorithm;

    @JsonProperty("docInfo")
    private String docInfo;

    @JsonProperty("docHashHex")
    private String docHashHex;

    @JsonProperty("digSigAsp")
    private String digSigAsp;
}
