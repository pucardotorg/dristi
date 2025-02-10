package digit.validators;

import digit.web.models.CaseDiarySearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OneOfValidatorTest {

    @InjectMocks
    private OneOfValidator oneOfValidator;

    private CaseDiarySearchCriteria searchCriteria;

    @BeforeEach
    public void setup() {
        searchCriteria = new CaseDiarySearchCriteria();
    }

    @Test
    public void testIsValid_WhenDateIsPresent_ShouldReturnTrue() {
        searchCriteria.setDate(1L);
        boolean result = oneOfValidator.isValid(searchCriteria, null);
        assertTrue(result, "Validator should return true when date is present");
    }

    @Test
    public void testIsValid_WhenCaseIdIsPresent_ShouldReturnTrue() {
        searchCriteria.setCaseId("CASE123");
        boolean result = oneOfValidator.isValid(searchCriteria, null);
        assertTrue(result, "Validator should return true when caseId is present");
    }

    @Test
    public void testIsValid_WhenBothDateAndCaseIdArePresent_ShouldReturnTrue() {
        searchCriteria.setDate(1L);
        searchCriteria.setCaseId("CASE123");
        boolean result = oneOfValidator.isValid(searchCriteria, null);
        assertTrue(result, "Validator should return true when both date and caseId are present");
    }

    @Test
    public void testIsValid_WhenNeitherDateNorCaseIdIsPresent_ShouldReturnFalse() {
        boolean result = oneOfValidator.isValid(searchCriteria, null);
        assertFalse(result, "Validator should return false when neither date nor caseId is present");
    }
}