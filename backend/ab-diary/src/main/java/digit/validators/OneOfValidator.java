package digit.validators;

import digit.annotation.OneOf;
import digit.web.models.CaseDiarySearchCriteria;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OneOfValidator implements ConstraintValidator<OneOf, CaseDiarySearchCriteria> {

    @Override
    public boolean isValid(CaseDiarySearchCriteria searchCriteria, ConstraintValidatorContext constraintValidatorContext) {
        return ((searchCriteria.getDate() != null) || (searchCriteria.getCaseId() != null));
    }

}
