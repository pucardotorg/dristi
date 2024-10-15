package org.pucar.dristi.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.pucar.dristi.annotation.OneOf;
import org.pucar.dristi.web.models.CaseSearchCriteria;


public class OneOfValidator implements ConstraintValidator<OneOf, CaseSearchCriteria> {


    @Override
    public boolean isValid(CaseSearchCriteria criteria, ConstraintValidatorContext constraintValidatorContext) {
        return (criteria.getCaseId() != null && !criteria.getCaseId().isEmpty()) ||
                (criteria.getFilingNumber() != null && !criteria.getFilingNumber().isEmpty()) ||
                (criteria.getCnrNumber() != null && !criteria.getCnrNumber().isEmpty());
    }
}
