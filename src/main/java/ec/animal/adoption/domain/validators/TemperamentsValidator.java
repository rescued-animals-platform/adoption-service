package ec.animal.adoption.domain.validators;

import ec.animal.adoption.domain.characteristics.temperament.Balance;
import ec.animal.adoption.domain.characteristics.temperament.Docility;
import ec.animal.adoption.domain.characteristics.temperament.Sociability;
import ec.animal.adoption.domain.characteristics.temperament.Temperament;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class TemperamentsValidator implements ConstraintValidator<Temperaments, Set<Temperament>> {

    @Override
    public boolean isValid(Set<Temperament> temperaments, ConstraintValidatorContext context) {
        Set<Temperament> nullSafeTemperaments = Optional.ofNullable(temperaments).orElse(Collections.emptySet());

        long numberOfSociabilityInstances = nullSafeTemperaments.stream()
                .filter(temperament -> temperament instanceof Sociability).count();
        long numberOfDocilityInstances = nullSafeTemperaments.stream()
                .filter(temperament -> temperament instanceof Docility).count();
        long numberOfBalanceInstances = nullSafeTemperaments.stream()
                .filter(temperament -> temperament instanceof Balance).count();

        return numberOfSociabilityInstances <= 1 && numberOfDocilityInstances <= 1 && numberOfBalanceInstances <= 1;
    }
}
