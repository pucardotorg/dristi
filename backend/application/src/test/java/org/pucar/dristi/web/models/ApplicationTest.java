package org.pucar.dristi.web.models;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationTest {

    @Test
    public void testIsResponseRequired_True() {
        Map<String, Object> additionalDetails = new HashMap<>();
        additionalDetails.put("isResponseRequired", true);

        Application application = Application.builder()
                .additionalDetails(additionalDetails)
                .build();

        assertTrue(application.isResponseRequired());
    }

    @Test
    public void testIsResponseRequired_False() {
        Map<String, Object> additionalDetails = new HashMap<>();
        additionalDetails.put("isResponseRequired", false);

        Application application = Application.builder()
                .additionalDetails(additionalDetails)
                .build();

        assertFalse(application.isResponseRequired());
    }

    @Test
    public void testIsResponseRequired_KeyNotPresent() {
        Map<String, Object> additionalDetails = new HashMap<>();

        Application application = Application.builder()
                .additionalDetails(additionalDetails)
                .build();

        assertFalse(application.isResponseRequired());
    }

    @Test
    public void testIsResponseRequired_AdditionalDetailsNull() {
        Application application = Application.builder()
                .additionalDetails(null)
                .build();

        assertFalse(application.isResponseRequired());
    }
}
