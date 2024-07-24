package org.pucar.dristi.web.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderType {

	private Long id;

	private String code;

	private String type;

	private Boolean isActive;
}
