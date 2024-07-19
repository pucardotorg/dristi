package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Sorting order
 */
public enum Order {
	ASC("asc"), DESC("desc");

	private String value;

	Order(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}


}
