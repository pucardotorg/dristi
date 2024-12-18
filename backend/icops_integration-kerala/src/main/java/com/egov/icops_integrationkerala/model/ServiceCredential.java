package com.egov.icops_integrationkerala.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceCredential {
    private String serviceName;
    private String serviceKey;
}
