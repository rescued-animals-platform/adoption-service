package ec.animal.adoption.validator;

import ec.animal.adoption.api.model.state.CreateStateRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CreateStateRequestValidator implements ConstraintValidator<ValidCreateStateRequest, CreateStateRequest> {

    private static final String STATE_NAME_IS_REQUIRED = "State name is required";
    private static final String NOTES_ARE_REQUIRED_FOR_UNAVAILABLE_STATE = "Notes are required for unavailable state";

    @Override
    public boolean isValid(final CreateStateRequest createStateRequest, final ConstraintValidatorContext context) {
        if (!createStateRequest.hasName()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(STATE_NAME_IS_REQUIRED)
                   .addPropertyNode("name")
                   .addConstraintViolation();
            return false;
        }

        if (createStateRequest.isUnavailableAndDoesNotHaveNotes()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(NOTES_ARE_REQUIRED_FOR_UNAVAILABLE_STATE)
                   .addPropertyNode("notes")
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}
