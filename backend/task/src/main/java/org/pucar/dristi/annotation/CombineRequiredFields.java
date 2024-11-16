package org.pucar.dristi.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.pucar.dristi.validators.CombineValidator;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CombineValidator.class)
@Documented
public @interface CombineRequiredFields {


    String message() default "combination of field is required";

    Class<?>[] groups() default {};

    String[] fields() default {};

    Class<? extends Payload>[] payload() default {};

}
