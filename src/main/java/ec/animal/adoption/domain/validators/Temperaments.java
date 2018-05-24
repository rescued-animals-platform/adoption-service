package ec.animal.adoption.domain.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = {TemperamentsValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Temperaments {

    String message() default "Temperaments are invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
