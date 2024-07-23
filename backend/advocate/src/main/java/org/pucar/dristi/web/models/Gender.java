package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE"),
    OTHER("OTHER"),
    TRANSGENDER("TRANSGENDER");

    private String value;

    private Gender(String value) {
        this.value = value;
    }

    @JsonValue
    public String toString() {
        return String.valueOf(this.value);
    }

    @JsonCreator
    public static Gender fromValue(String text) {
        Gender[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
           Gender b = var1[var3];
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }

        return null;
    }
}

