package ec.animal.adoption.domain.validators;

import ec.animal.adoption.domain.characteristics.temperament.Balance;
import ec.animal.adoption.domain.characteristics.temperament.Docility;
import ec.animal.adoption.domain.characteristics.temperament.Sociability;
import ec.animal.adoption.domain.characteristics.temperament.Temperament;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TemperamentsValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    private TemperamentsValidator temperamentsValidator;

    @Before
    public void setUp() {
        temperamentsValidator = new TemperamentsValidator();
    }

    @Test
    public void shouldBeAnInstanceOfConstraintValidator() {
        assertThat(temperamentsValidator, is(instanceOf(ConstraintValidator.class)));
    }

    @Test
    public void shouldBeValidIfTemperamentsIsNull() {
        boolean areTemperamentsValid = temperamentsValidator.isValid(null, context);

        assertThat(areTemperamentsValid, is(true));
    }

    @Test
    public void shouldBeValid() {
        Set<Temperament> temperaments = new HashSet<>(Arrays.asList(
                Sociability.SHY, Docility.NEITHER_DOCILE_NOR_DOMINANT, Balance.POSSESSIVE
        ));

        boolean areTemperamentsValid = temperamentsValidator.isValid(temperaments, context);

        assertThat(areTemperamentsValid, is(true));
    }

    @Test
    public void shouldBeInvalidIfTemperamentsHaveMoreThanOneSociabilityDefinition() {
        Set<Temperament> temperaments = new HashSet<>(Arrays.asList(
                Sociability.SHY, Sociability.VERY_SOCIABLE, Docility.DOCILE
        ));

        boolean areTemperamentsValid = temperamentsValidator.isValid(temperaments, context);

        assertThat(areTemperamentsValid, is(false));
    }

    @Test
    public void shouldBeInvalidIfTemperamentsHaveMoreThanOneDocilityDefinition() {
        Set<Temperament> temperaments = new HashSet<>(Arrays.asList(
                Sociability.SHY, Docility.DOCILE, Docility.NEITHER_DOCILE_NOR_DOMINANT, Docility.DOMINANT
        ));

        boolean areTemperamentsValid = temperamentsValidator.isValid(temperaments, context);

        assertThat(areTemperamentsValid, is(false));
    }

    @Test
    public void shouldBeInvalidIfTemperamentsHaveMoreThanOneBalanceDefinition() {
        Set<Temperament> temperaments = new HashSet<>(Arrays.asList(
                Sociability.SHY, Docility.DOCILE, Balance.POSSESSIVE, Balance.BALANCED
        ));

        boolean areTemperamentsValid = temperamentsValidator.isValid(temperaments, context);

        assertThat(areTemperamentsValid, is(false));
    }
}