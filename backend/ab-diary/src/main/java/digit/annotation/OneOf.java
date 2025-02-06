package digit.annotation;

import digit.validators.OneOfValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Constraint annotation to ensure that at least one of the specified fields is provided.
 * This annotation is used to validate that one of the caseId or date fields is present.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OneOfValidator.class)
@Documented
public @interface OneOf {

    String message() default "One of the required fields must be provided";

    Class<?>[] groups() default {};

    String[] fields() default {};

    Class<? extends Payload>[] payload() default {};

}
