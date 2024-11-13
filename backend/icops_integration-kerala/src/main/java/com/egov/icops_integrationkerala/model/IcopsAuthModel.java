package com.egov.icops_integrationkerala.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class IcopsAuthModel {

    private Long id;

    private Long userId;

    private String token;

    private Date createdDate;

    private Date expiryDate;
}
