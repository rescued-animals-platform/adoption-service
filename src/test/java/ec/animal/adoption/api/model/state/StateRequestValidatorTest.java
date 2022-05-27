package ec.animal.adoption.api.model.state;

import ec.animal.adoption.api.model.state.StateRequest;
import ec.animal.adoption.api.model.state.StateRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

import static ec.animal.adoption.domain.state.StateName.*;
import static javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import static javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StateRequestValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintViolationBuilder constraintViolationBuilder;

    @Mock
    private NodeBuilderCustomizableContext nodeBuilderCustomizableContext;

    private StateRequestValidator stateRequestValidator;

    @BeforeEach
    void setUp() {
        stateRequestValidator = new StateRequestValidator();
    }

    @Test
    void shouldReturnFalseWithCustomPathAndMessageWhenStateNameIsNull() {
        StateRequest stateRequest = new StateRequest(null, null, null);
        when(context.buildConstraintViolationWithTemplate("State name is required")).thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addPropertyNode("name")).thenReturn(nodeBuilderCustomizableContext);

        boolean isValid = stateRequestValidator.isValid(stateRequest, context);

        assertFalse(isValid);
        verify(context).disableDefaultConstraintViolation();
        verify(nodeBuilderCustomizableContext).addConstraintViolation();
    }

    @Test
    void shouldReturnFalseWithCustomPathAndMessageWhenStateNameIsUnavailableAndNotesAreNull() {
        StateRequest stateRequest = new StateRequest(UNAVAILABLE, null, null);
        when(context.buildConstraintViolationWithTemplate("Notes are required for unavailable state"))
                .thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addPropertyNode("notes")).thenReturn(nodeBuilderCustomizableContext);

        boolean isValid = stateRequestValidator.isValid(stateRequest, context);

        assertFalse(isValid);
        verify(context).disableDefaultConstraintViolation();
        verify(nodeBuilderCustomizableContext).addConstraintViolation();
    }

    @ParameterizedTest(name = "{index} {0} is valid")
    @MethodSource("validCreateStateRequests")
    void shouldReturnTrueForValidCreateStateRequest(final StateRequest stateRequest) {
        assertTrue(stateRequestValidator.isValid(stateRequest, context));
        verifyNoInteractions(context);
    }

    private static Stream<Arguments> validCreateStateRequests() {
        return Stream.of(
                Arguments.of(new StateRequest(LOOKING_FOR_HUMAN, null, null)),
                Arguments.of(new StateRequest(LOOKING_FOR_HUMAN, randomAlphabetic(10), null)),
                Arguments.of(new StateRequest(LOOKING_FOR_HUMAN, null, randomAlphabetic(10))),
                Arguments.of(new StateRequest(LOOKING_FOR_HUMAN, randomAlphabetic(10), randomAlphabetic(10))),
                Arguments.of(new StateRequest(ADOPTED, null, null)),
                Arguments.of(new StateRequest(ADOPTED, randomAlphabetic(10), null)),
                Arguments.of(new StateRequest(ADOPTED, null, randomAlphabetic(10))),
                Arguments.of(new StateRequest(ADOPTED, randomAlphabetic(10), randomAlphabetic(10))),
                Arguments.of(new StateRequest(UNAVAILABLE, null, randomAlphabetic(10))),
                Arguments.of(new StateRequest(UNAVAILABLE, randomAlphabetic(10), randomAlphabetic(10)))
        );
    }
}