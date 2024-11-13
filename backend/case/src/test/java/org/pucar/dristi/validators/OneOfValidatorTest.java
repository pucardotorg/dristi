package org.pucar.dristi.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.CaseSearchCriteria;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class OneOfValidatorTest {

    @InjectMocks
    private OneOfValidator oneOfValidator;

    @Test
    public void testIsValid_withCaseId() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setCaseId(Collections.singletonList("123"));
        criteria.setFilingNumber(null);
        criteria.setCnrNumber(null);

        boolean result = oneOfValidator.isValid(criteria, null);
        assertTrue(result, "Validation should pass when caseId is provided.");
    }

    @Test
    public void testIsValid_withFilingNumber() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setCaseId(null);
        criteria.setFilingNumber(Collections.singletonList("456"));
        criteria.setCnrNumber(null);

        boolean result = oneOfValidator.isValid(criteria, null);
        assertTrue(result, "Validation should pass when filingNumber is provided.");
    }

    @Test
    public void testIsValid_withCnrNumber() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setCaseId(null);
        criteria.setFilingNumber(null);
        criteria.setCnrNumber(Collections.singletonList("789"));

        boolean result = oneOfValidator.isValid(criteria, null);
        assertTrue(result, "Validation should pass when cnrNumber is provided.");
    }

    @Test
    public void testIsValid_withAllFieldsNull() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setCaseId(null);
        criteria.setFilingNumber(null);
        criteria.setCnrNumber(null);

        boolean result = oneOfValidator.isValid(criteria, null);
        assertFalse(result, "Validation should fail when all fields are null.");
    }

    @Test
    public void testIsValid_withAllFieldsEmpty() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setCaseId(new ArrayList<>());
        criteria.setFilingNumber(new ArrayList<>());
        criteria.setCnrNumber(new ArrayList<>());

        boolean result = oneOfValidator.isValid(criteria, null);
        assertFalse(result, "Validation should fail when all fields are empty.");
    }

    @Test
    public void testIsValid_withMultipleFieldsFilled() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setCaseId(Collections.singletonList("123"));
        criteria.setFilingNumber(Collections.singletonList("456"));
        criteria.setCnrNumber(Collections.singletonList("789"));

        boolean result = oneOfValidator.isValid(criteria, null);
        assertTrue(result, "Validation should pass when multiple fields are provided.");
    }
}
