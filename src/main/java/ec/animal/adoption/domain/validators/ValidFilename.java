package ec.animal.adoption.domain.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = {FilenameValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface ValidFilename {

    String message() default "Unsupported file extension";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
