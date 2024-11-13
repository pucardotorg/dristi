package org.egov.transformer.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderData {
    @JsonProperty("orderDetails")
    @Valid
    private Order orderDetails = null;

    @JsonProperty("history")
    private Object history = null;
}
