package ec.animal.adoption.adapter.rest.service;

import ec.animal.adoption.adapter.rest.model.state.StateRequest;
import ec.animal.adoption.domain.model.state.State;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static ec.animal.adoption.domain.model.state.StateName.ADOPTED;
import static ec.animal.adoption.domain.model.state.StateName.LOOKING_FOR_HUMAN;
import static ec.animal.adoption.domain.model.state.StateName.UNAVAILABLE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertNull;

class StateRequestMapperTest {

    @Test
    void shouldReturnLookingForHumanState() {
        StateRequest stateRequest = new StateRequest(LOOKING_FOR_HUMAN,
                                                     null,
                                                     null);
        State expectedState = State.lookingForHuman();

        State state = StateRequestMapper.MAPPER.toState(stateRequest);

        Assertions.assertThat(state).usingRecursiveComparison().isEqualTo(expectedState);
    }

    @Test
    void shouldReturnAdoptedStateWithAdoptionFormId() {
        String adoptionFormId = randomAlphabetic(10);
        StateRequest stateRequest = new StateRequest(ADOPTED,
                                                     adoptionFormId,
                                                     null);
        State expectedState = State.adopted(adoptionFormId);

        State state = StateRequestMapper.MAPPER.toState(stateRequest);

        Assertions.assertThat(state).usingRecursiveComparison().isEqualTo(expectedState);
    }

    @Test
    void shouldReturnAdoptedStateWithoutAdoptionFormId() {
        StateRequest stateRequest = new StateRequest(ADOPTED,
                                                     null,
                                                     null);
        State expectedState = State.adopted(null);

        State state = StateRequestMapper.MAPPER.toState(stateRequest);

        Assertions.assertThat(state).usingRecursiveComparison().isEqualTo(expectedState);
    }

    @Test
    void shouldReturnUnavailableStateWithMandatoryNotes() {
        String notes = randomAlphabetic(10);
        StateRequest stateRequest = new StateRequest(UNAVAILABLE,
                                                     null,
                                                     notes);
        State expectedState = State.unavailable(notes);

        State state = StateRequestMapper.MAPPER.toState(stateRequest);

        Assertions.assertThat(state).usingRecursiveComparison().isEqualTo(expectedState);
    }

    @Test
    void shouldReturnNullWhenStateRequestIsNull() {
        assertNull(StateRequestMapper.MAPPER.toState(null));
    }
}