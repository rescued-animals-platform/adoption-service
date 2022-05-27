package ec.animal.adoption.api.model.state;

import ec.animal.adoption.api.model.state.StateRequestValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = {StateRequestValidator.class})
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidStateRequest {

    String message() default "Invalid state";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
