package digit.validator;

import digit.web.models.JudgeCalendarRule;
import digit.web.models.SearchCriteria;
import org.apache.commons.lang3.ObjectUtils;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JudgeCalendarValidator {


    public void validateUpdateJudgeCalendar(List<JudgeCalendarRule> judgeCalendarRule) {

//        judgeCalendarRule.forEach(application -> {
//            if (ObjectUtils.isEmpty(application.getTenantId()))
//                throw new CustomException("DK_SH_APP_ERR", "tenantId is mandatory for updating judge calendar");
//
//            if (ObjectUtils.isEmpty(application.getJudgeId()))
//                throw new CustomException("DK_SH_APP_ERR", "Judge Id is mandatory for updating judge calendar");
//
//            if (ObjectUtils.isEmpty(application.getDate()))
//                throw new CustomException("DK_SH_APP_ERR", "Date is mandatory for updating judge calendar");
//
//            if (application.getDate().isBefore(LocalDate.now()))
//                throw new CustomException("DK_SH_APP_ERR", "cannot update for past dates");
//
//            if (ObjectUtils.isEmpty(application.getRuleType()))
//                throw new CustomException("DK_SH_APP_ERR", "Rule Type is mandatory for updating judge calendar");
//        });
    }

    public <T extends SearchCriteria> void validateSearchRequest(T criteria) {
        if (ObjectUtils.isEmpty(criteria.getTenantId()))
            throw new CustomException("DK_SH_SEARCH_ERR", "tenantId is mandatory for search");

        if (ObjectUtils.isEmpty(criteria.getJudgeId()))
            throw new CustomException("DK_SH_SEARCH_ERR", "judgeId is mandatory for search");

        if (ObjectUtils.isEmpty(criteria.getCourtId()))
            throw new CustomException("DK_SH_SEARCH_ERR", "courtId is mandatory for search");
    }
}
