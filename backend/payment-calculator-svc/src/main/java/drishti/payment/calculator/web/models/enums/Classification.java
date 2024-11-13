package drishti.payment.calculator.web.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Classification {

    LTD("ltd"), ROC("roc");

    private final String value;

    Classification(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Classification fromValue(String text) {
        for (Classification b : Classification.values()) {
            if (String.valueOf(b.value).equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }
}
