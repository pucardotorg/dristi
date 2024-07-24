package org.pucar.dristi.web.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCategory {
	private Long id;

	private String category;

	private Boolean isActive;
}
