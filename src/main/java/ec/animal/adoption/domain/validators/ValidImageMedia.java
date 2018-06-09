package ec.animal.adoption.domain.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = {ImageMediaValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface ValidImageMedia {

    String message() default "Unsupported file extension";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
