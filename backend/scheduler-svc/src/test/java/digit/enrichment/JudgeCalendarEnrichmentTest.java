package digit.enrichment;

import digit.models.coremodels.AuditDetails;
import digit.web.models.JudgeCalendarRule;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JudgeCalendarEnrichmentTest {

    private JudgeCalendarEnrichment judgeCalendarEnrichment;
    private RequestInfo requestInfo;

    @BeforeEach
    public void setUp() {
        judgeCalendarEnrichment = new JudgeCalendarEnrichment();

        User user = mock(User.class);
        Mockito.lenient().when(user.getUuid()).thenReturn("test-uuid");

        requestInfo = mock(RequestInfo.class);
        Mockito.lenient().when(requestInfo.getUserInfo()).thenReturn(user);
    }

    @Test
    public void testEnrichUpdateJudgeCalendar() {
        List<JudgeCalendarRule> judgeCalendarRules = new ArrayList<>();
        JudgeCalendarRule rule = new JudgeCalendarRule();
        judgeCalendarRules.add(rule);

        judgeCalendarEnrichment.enrichUpdateJudgeCalendar(requestInfo, judgeCalendarRules);

        assertNotNull(rule.getId());
        assertNotNull(rule.getAuditDetails());
        assertEquals("test-uuid", rule.getAuditDetails().getCreatedBy());
        assertEquals("test-uuid", rule.getAuditDetails().getLastModifiedBy());
        assertEquals(1, rule.getRowVersion());
    }

    @Test
    public void testEnrichUpdateJudgeCalendar_WithMultipleRules() {
        List<JudgeCalendarRule> judgeCalendarRules = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            judgeCalendarRules.add(new JudgeCalendarRule());
        }

        judgeCalendarEnrichment.enrichUpdateJudgeCalendar(requestInfo, judgeCalendarRules);

        for (JudgeCalendarRule rule : judgeCalendarRules) {
            assertNotNull(rule.getId());
            assertNotNull(rule.getAuditDetails());
            assertEquals("test-uuid", rule.getAuditDetails().getCreatedBy());
            assertEquals("test-uuid", rule.getAuditDetails().getLastModifiedBy());
            assertEquals(1, rule.getRowVersion());
        }
    }

    @Test
    public void testEnrichUpdateJudgeCalendar_WithEmptyRules() {
        List<JudgeCalendarRule> judgeCalendarRules = new ArrayList<>();

        judgeCalendarEnrichment.enrichUpdateJudgeCalendar(requestInfo, judgeCalendarRules);

        assertTrue(judgeCalendarRules.isEmpty());
    }

    @Test
    public void testEnrichUpdateJudgeCalendar_WithNullRequestInfo() {
        List<JudgeCalendarRule> judgeCalendarRules = new ArrayList<>();
        JudgeCalendarRule rule = new JudgeCalendarRule();
        judgeCalendarRules.add(rule);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            judgeCalendarEnrichment.enrichUpdateJudgeCalendar(null, judgeCalendarRules);
        });

        assertNotNull(exception);
    }

    @Test
    public void testEnrichUpdateJudgeCalendar_WithNullUserInfo() {
        when(requestInfo.getUserInfo()).thenReturn(null);
        List<JudgeCalendarRule> judgeCalendarRules = new ArrayList<>();
        JudgeCalendarRule rule = new JudgeCalendarRule();
        judgeCalendarRules.add(rule);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            judgeCalendarEnrichment.enrichUpdateJudgeCalendar(requestInfo, judgeCalendarRules);
        });

        assertNotNull(exception);
    }
}

