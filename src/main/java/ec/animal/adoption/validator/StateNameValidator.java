package ec.animal.adoption.validator;

import ec.animal.adoption.domain.state.State;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StateNameValidator implements ConstraintValidator<ValidStateName, String> {

    @Override
    public boolean isValid(final String stateName, final ConstraintValidatorContext context) {
        return stateName == null || State.isStateNameValid(stateName);
    }
}
