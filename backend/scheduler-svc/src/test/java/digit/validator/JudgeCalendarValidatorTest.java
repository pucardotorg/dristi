package digit.validator;

import digit.web.models.JudgeCalendarRule;
import digit.web.models.SearchCriteria;
import digit.web.models.enums.JudgeRuleType;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class JudgeCalendarValidatorTest {


    @InjectMocks
    private JudgeCalendarValidator validator;

    @Test
    void validateSearchRequest_throwsException_whenTenantIdIsEmpty() {
        SearchCriteria criteria = mock(SearchCriteria.class);

        when(criteria.getTenantId()).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> {
            validator.validateSearchRequest(criteria);
        });

        assertEquals("DK_SH_SEARCH_ERR", exception.getCode());
        assertEquals("tenantId is mandatory for search", exception.getMessage());
    }

    @Test
    void validateSearchRequest_throwsException_whenJudgeIdIsEmpty() {
        SearchCriteria criteria = mock(SearchCriteria.class);

        when(criteria.getTenantId()).thenReturn("someTenantId");
        when(criteria.getJudgeId()).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> {
            validator.validateSearchRequest(criteria);
        });

        assertEquals("DK_SH_SEARCH_ERR", exception.getCode());
        assertEquals("judgeId is mandatory for search", exception.getMessage());
    }

    @Test
    void validateSearchRequest_throwsException_whenCourtIdIsEmpty() {
        SearchCriteria criteria = mock(SearchCriteria.class);

        when(criteria.getTenantId()).thenReturn("someTenantId");
        when(criteria.getJudgeId()).thenReturn("someJudgeId");
        when(criteria.getCourtId()).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> {
            validator.validateSearchRequest(criteria);
        });

        assertEquals("DK_SH_SEARCH_ERR", exception.getCode());
        assertEquals("courtId is mandatory for search", exception.getMessage());
    }

    @Test
    void validateSearchRequest_doesNotThrowException_whenAllFieldsArePresent() {
        SearchCriteria criteria = mock(SearchCriteria.class);

        when(criteria.getTenantId()).thenReturn("someTenantId");
        when(criteria.getJudgeId()).thenReturn("someJudgeId");
        when(criteria.getCourtId()).thenReturn("someCourtId");

        assertDoesNotThrow(() -> validator.validateSearchRequest(criteria));
    }

    @Test
    void testValidateUpdateJudgeCalendar() {
        validator.validateUpdateJudgeCalendar(new ArrayList<>());
    }
}

