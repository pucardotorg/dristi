package com.egov.icops_integrationkerala.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredential {

    private Long userId;

    private String serviceName;

    private String serviceKey;

    private String authType;
}
