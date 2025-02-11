package org.egov.hrms.model;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Email {

    private Set<String> emailTo;
    private String subject;

    //need to be json string of with key value pair of data that need to be sent in email
    private String body;
    Map<String, String> fileStoreId;
    private String tenantId;
    @JsonProperty("isHTML")
    private boolean isHTML;

    //template code in mdms and messageCode for localization
    private String templateCode;
}

