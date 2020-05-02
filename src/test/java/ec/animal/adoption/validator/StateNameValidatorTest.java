package ec.animal.adoption.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class StateNameValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    private StateNameValidator stateNameValidator;

    @BeforeEach
    void setUp() {
        stateNameValidator = new StateNameValidator();
    }

    @Test
    public void shouldBeAnInstanceOfConstraintValidator() {
        assertThat(stateNameValidator, is(instanceOf(ConstraintValidator.class)));
    }

    @ParameterizedTest(name = "{index} \"{0}\" is a valid state name")
    @ValueSource(strings = {"lookingForHuman", "adopted", "unavailable"})
    public void shouldBeValid(final String stateName) {
        assertTrue(stateNameValidator.isValid(stateName, context));
    }

    @ParameterizedTest(name = "{index} \"{0}\" is an invalid state name")
    @ValueSource(strings = {"anyOtherState", "ADOPTED", "Un-available"})
    public void shouldBeInvalid(final String stateName) {
        assertFalse(stateNameValidator.isValid(stateName, context));
    }
}