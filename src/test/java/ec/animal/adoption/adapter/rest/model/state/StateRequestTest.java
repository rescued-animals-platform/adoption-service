package ec.animal.adoption.adapter.rest.model.state;

import org.junit.jupiter.api.Test;

import static ec.animal.adoption.domain.model.state.StateName.ADOPTED;
import static ec.animal.adoption.domain.model.state.StateName.LOOKING_FOR_HUMAN;
import static ec.animal.adoption.domain.model.state.StateName.UNAVAILABLE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StateRequestTest {

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