package ec.animal.adoption.api.model.state;

import ec.animal.adoption.domain.state.State;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static ec.animal.adoption.domain.state.StateName.ADOPTED;
import static ec.animal.adoption.domain.state.StateName.LOOKING_FOR_HUMAN;
import static ec.animal.adoption.domain.state.StateName.UNAVAILABLE;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateStateRequestTest {

    @Test
    void shouldReturnCreateStateDtoWithLookingForHumanState() {
        CreateStateRequest createStateRequest = new CreateStateRequest(LOOKING_FOR_HUMAN,
                                                                       null,
                                                                       null);
        State expectedState = State.lookingForHuman();

        State state = createStateRequest.toCreateStateDtoDomain();

        Assertions.assertThat(state).usingRecursiveComparison().isEqualTo(expectedState);
    }

    @Test
    void shouldReturnCreateStateDtoWithAdoptedStateAndAdoptionFormId() {
        String adoptionFormId = randomAlphabetic(10);
        CreateStateRequest createStateRequest = new CreateStateRequest(ADOPTED,
                                                                       adoptionFormId,
                                                                       null);
        State expectedState = State.adopted(adoptionFormId);

        State state = createStateRequest.toCreateStateDtoDomain();

        Assertions.assertThat(state).usingRecursiveComparison().isEqualTo(expectedState);
    }

    @Test
    void shouldReturnCreateStateDtoWithAdoptedStateAndNoAdoptionFormId() {
        CreateStateRequest createStateRequest = new CreateStateRequest(ADOPTED,
                                                                       null,
                                                                       null);
        State expectedState = State.adopted(null);

        State state = createStateRequest.toCreateStateDtoDomain();

        Assertions.assertThat(state).usingRecursiveComparison().isEqualTo(expectedState);
    }

    @Test
    void shouldReturnCreateStateDtoWithUnavailableStateAndMandatoryNotes() {
        String notes = randomAlphabetic(10);
        CreateStateRequest createStateRequest = new CreateStateRequest(UNAVAILABLE,
                                                                       null,
                                                                       notes);
        State expectedState = State.unavailable(notes);

        State state = createStateRequest.toCreateStateDtoDomain();

        Assertions.assertThat(state).usingRecursiveComparison().isEqualTo(expectedState);
    }

    @Test
    void shouldReturnTrueWhenNameIsPresent() {
        CreateStateRequest createStateRequest = new CreateStateRequest(ADOPTED, null, null);

        assertTrue(createStateRequest.hasName());
    }

    @Test
    void shouldReturnFalseWhenNameIsNotPresent() {
        CreateStateRequest createStateRequest = new CreateStateRequest(null, null, null);

        assertFalse(createStateRequest.hasName());
    }

    @Test
    void shouldReturnTrueWhenNameIsUnavailableAndNotesAreNotPresent() {
        CreateStateRequest createStateRequest = new CreateStateRequest(UNAVAILABLE, null, null);

        assertTrue(createStateRequest.isUnavailableAndDoesNotHaveNotes());
    }

    @Test
    void shouldReturnFalseWhenNameIsUnavailableAndNotesArePresent() {
        CreateStateRequest createStateRequest = new CreateStateRequest(UNAVAILABLE, null, randomAlphabetic(10));

        assertFalse(createStateRequest.isUnavailableAndDoesNotHaveNotes());
    }

    @Test
    void shouldReturnFalseWhenNameIsOtherThanUnavailableAndNotesAreNotPresent() {
        CreateStateRequest createStateRequest = new CreateStateRequest(ADOPTED, null, null);

        assertFalse(createStateRequest.isUnavailableAndDoesNotHaveNotes());
    }

    @Test
    void shouldReturnFalseWhenNameIsOtherThanUnavailableAndNotesArePresent() {
        CreateStateRequest createStateRequest = new CreateStateRequest(LOOKING_FOR_HUMAN, null, randomAlphabetic(10));

        assertFalse(createStateRequest.isUnavailableAndDoesNotHaveNotes());
    }
}