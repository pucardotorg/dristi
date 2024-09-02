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
public class ESignXmlForm {

    @JsonProperty("id")
    public String id;

    @JsonProperty("type")
    public String type;

    @JsonProperty("description")
    public String description;

    @JsonProperty("eSignRequest")
    public String eSignRequest;

    @JsonProperty("aspTxnID")
    public String aspTxnID;

    @JsonProperty("contentType")
    public String contentType;
}
