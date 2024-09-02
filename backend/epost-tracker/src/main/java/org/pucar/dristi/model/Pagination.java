package org.pucar.dristi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * Pagination details
 */
@Schema(description = "Pagination details")
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pagination {
	@JsonProperty("totalCount")
	private Integer totalCount = null;

	@JsonProperty("sortBy")
	private Sort sortBy = null;

	@JsonProperty("orderBy")
	private Order orderBy = null;
}
