package ec.animal.adoption.domain.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = {TemperamentsValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface ValidTemperaments {

    String message() default "At least one temperaments is required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
