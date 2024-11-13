package org.pucar.dristi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Sort {
    PROCESS_NUMBER("processNumber"),
    TASK_NUMBER("taskNumber"),
    TRACKING_NUMBER("trackingNumber"),
    DELIVERY_STATUS("deliveryStatus"),
    BOOKING_DATE("bookingDate"),
    RECEIVED_DATE("receivedDate");

    private String value;

    Sort(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static Sort fromValue(String text) {
        for (Sort b : Sort.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
