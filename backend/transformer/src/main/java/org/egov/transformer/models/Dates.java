package org.egov.transformer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dates {

    private String filingDate;

    private String registrationDate;

    private String judgementDate;

}
