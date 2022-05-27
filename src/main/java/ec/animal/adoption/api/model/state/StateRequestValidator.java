package ec.animal.adoption.api.model.state;

import org.springframework.lang.NonNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StateRequestValidator implements ConstraintValidator<ValidStateRequest, StateRequest> {

    private static final String STATE_NAME_IS_REQUIRED = "State name is required";
    private static final String NOTES_ARE_REQUIRED_FOR_UNAVAILABLE_STATE = "Notes are required for unavailable state";

    @Override
    public boolean isValid(@NonNull final StateRequest stateRequest, final ConstraintValidatorContext context) {
        if (!stateRequest.hasName()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(STATE_NAME_IS_REQUIRED)
                   .addPropertyNode("name")
                   .addConstraintViolation();
            return false;
        }

        if (stateRequest.isUnavailableAndDoesNotHaveNotes()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(NOTES_ARE_REQUIRED_FOR_UNAVAILABLE_STATE)
                   .addPropertyNode("notes")
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}
