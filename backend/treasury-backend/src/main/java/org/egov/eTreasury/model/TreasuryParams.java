package org.egov.eTreasury.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TreasuryParams {

    private String authToken;
    private Boolean status;
    private String rek;
    private String hmac;
    private String data;
}
