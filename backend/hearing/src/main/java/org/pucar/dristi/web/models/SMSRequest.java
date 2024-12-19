package org.pucar.dristi.web.models;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SMSRequest {

    @Pattern(regexp = "^[0-9]{10}$", message = "MobileNumber should be 10 digit number")
    private String mobileNumber;

    @Size(max = 1000)
    private String message;

    private String category;
    private Long expiryTime;
    private String templateId;

    //Unused for future upgrades
    private String locale;
    private String tenantId;
    private String email;
    private String[] users;

    private String contentType;

}