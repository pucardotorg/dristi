package org.pucar.dristi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@Builder
public class RespondentDetails {

    @JsonProperty("name")
    private String name;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("gender")
    @NotNull
    private String gender;

    @JsonProperty("email")
    @Email(message = "Email should be valid")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("address")
    @Valid
    private Address address;

    @JsonProperty("state")
    private String state;

    @JsonProperty("district")
    private String district;

    @JsonProperty("pinCode")
    private String pinCode;

    @JsonProperty("relativeName")
    private String relativeName;

    @JsonProperty("relationWithRelative")
    private String relationWithRelative;
}
