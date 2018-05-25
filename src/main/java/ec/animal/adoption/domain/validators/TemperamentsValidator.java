package ec.animal.adoption.domain.validators;

import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TemperamentsValidator implements ConstraintValidator<ValidTemperaments, Temperaments> {

    @Override
    public boolean isValid(Temperaments temperaments, ConstraintValidatorContext context) {
        return temperaments != null && !temperaments.isEmpty();
    }
}
