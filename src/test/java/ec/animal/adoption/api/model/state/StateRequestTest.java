package ec.animal.adoption.api.model.state;

import ec.animal.adoption.domain.state.State;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static ec.animal.adoption.domain.state.StateName.*;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StateRequestTest {

    @Test
    void shouldReturnLookingForHumanState() {
        StateRequest stateRequest = new StateRequest(LOOKING_FOR_HUMAN,
                                                     null,
                                                     null);
        State expectedState = State.lookingForHuman();

        State state = stateRequest.toDomain();

        Assertions.assertThat(state).usingRecursiveComparison().isEqualTo(expectedState);
    }

    @Test
    void shouldReturnAdoptedStateWithAdoptionFormId() {
        String adoptionFormId = randomAlphabetic(10);
        StateRequest stateRequest = new StateRequest(ADOPTED,
                                                     adoptionFormId,
                                                     null);
        State expectedState = State.adopted(adoptionFormId);

        State state = stateRequest.toDomain();

        Assertions.assertThat(state).usingRecursiveComparison().isEqualTo(expectedState);
    }

    @Test
    void shouldReturnAdoptedStateWithoutAdoptionFormId() {
        StateRequest stateRequest = new StateRequest(ADOPTED,
                                                     null,
                                                     null);
        State expectedState = State.adopted(null);

        State state = stateRequest.toDomain();

        Assertions.assertThat(state).usingRecursiveComparison().isEqualTo(expectedState);
    }

    @Test
    void shouldReturnUnavailableStateWithMandatoryNotes() {
        String notes = randomAlphabetic(10);
        StateRequest stateRequest = new StateRequest(UNAVAILABLE,
                                                     null,
                                                     notes);
        State expectedState = State.unavailable(notes);

        State state = stateRequest.toDomain();

        Assertions.assertThat(state).usingRecursiveComparison().isEqualTo(expectedState);
    }

    @Test
    void shouldReturnTrueWhenNameIsPresent() {
        StateRequest stateRequest = new StateRequest(ADOPTED, null, null);

        assertTrue(stateRequest.hasName());
    }

    @Test
    void shouldReturnFalseWhenNameIsNotPresent() {
        StateRequest stateRequest = new StateRequest(null, null, null);

        assertFalse(stateRequest.hasName());
    }

    @Test
    void shouldReturnTrueWhenNameIsUnavailableAndNotesAreNotPresent() {
        StateRequest stateRequest = new StateRequest(UNAVAILABLE, null, null);

        assertTrue(stateRequest.isUnavailableAndDoesNotHaveNotes());
    }

    @Test
    void shouldReturnFalseWhenNameIsUnavailableAndNotesArePresent() {
        StateRequest stateRequest = new StateRequest(UNAVAILABLE, null, randomAlphabetic(10));

        assertFalse(stateRequest.isUnavailableAndDoesNotHaveNotes());
    }

    @Test
    void shouldReturnFalseWhenNameIsOtherThanUnavailableAndNotesAreNotPresent() {
        StateRequest stateRequest = new StateRequest(ADOPTED, null, null);

        assertFalse(stateRequest.isUnavailableAndDoesNotHaveNotes());
    }

    @Test
    void shouldReturnFalseWhenNameIsOtherThanUnavailableAndNotesArePresent() {
        StateRequest stateRequest = new StateRequest(LOOKING_FOR_HUMAN, null, randomAlphabetic(10));

        assertFalse(stateRequest.isUnavailableAndDoesNotHaveNotes());
    }
}