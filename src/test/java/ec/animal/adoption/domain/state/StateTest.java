/*
    Copyright © 2018 Luisa Emme

    This file is part of Adoption Service in the Rescued Animals Platform.

    Adoption Service is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Adoption Service is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with Adoption Service.  If not, see <https://www.gnu.org/licenses/>.
 */

package ec.animal.adoption.domain.state;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StateTest {

    private static final String ESCAPED_ADOPTED_DETAILS = "\"adoptedDetails\":";
    private static final String ESCAPED_UNAVAILABLE_DETAILS = "\"unavailableDetails\":";
    private String adoptionFormId;
    private String notes;

    @BeforeEach
    void setUp() {
        adoptionFormId = randomAlphabetic(10);
        notes = randomAlphabetic(10);
    }

    @Test
    void shouldBuildLookingForHumanState() {
        State expectedState = State.lookingForHuman();

        State state = State.from(StateName.LOOKING_FOR_HUMAN, adoptionFormId, notes);

        Assertions.assertThat(state).usingRecursiveComparison().isEqualTo(expectedState);
    }

    @Test
    void shouldBuildAdoptedState() {
        State expectedState = State.adopted(adoptionFormId);

        State state = State.from(StateName.ADOPTED, adoptionFormId, notes);

        Assertions.assertThat(state).usingRecursiveComparison().isEqualTo(expectedState);
    }

    @Test
    void shouldBuildUnavailableState() {
        State expectedState = State.unavailable(notes);

        State state = State.from(StateName.UNAVAILABLE, adoptionFormId, notes);

        Assertions.assertThat(state).usingRecursiveComparison().isEqualTo(expectedState);
    }

    @Test
    void shouldReturnEmptyAdoptionFormId() {
        assertTrue(State.lookingForHuman().getAdoptionFormId().isEmpty());
        assertTrue(State.unavailable(randomAlphabetic(10)).getAdoptionFormId().isEmpty());
        assertTrue(State.adopted(null).getAdoptionFormId().isEmpty());
    }

    @Test
    void shouldReturnEmptyNotes() {
        assertTrue(State.lookingForHuman().getNotes().isEmpty());
        assertTrue(State.adopted(randomAlphabetic(10)).getNotes().isEmpty());
    }

    @Test
    void shouldBeSerializableAsLookingForHumanState() throws JsonProcessingException {
        ObjectMapper objectMapper = TestUtils.getObjectMapper();
        State state = State.lookingForHuman();

        String stateAsJson = objectMapper.writeValueAsString(state);

        assertTrue(stateAsJson.contains("\"name\":\"Looking for human\""));
        assertFalse(stateAsJson.contains(ESCAPED_ADOPTED_DETAILS));
        assertFalse(stateAsJson.contains(ESCAPED_UNAVAILABLE_DETAILS));
        assertFalse(stateAsJson.contains("\"adoptionFormId\":"));
        assertFalse(stateAsJson.contains("\"notes\":"));
    }

    @Test
    void shouldBeSerializableAsAdoptedStateWithAdoptionFormId() throws JsonProcessingException {
        ObjectMapper objectMapper = TestUtils.getObjectMapper();
        State state = State.adopted(adoptionFormId);

        String stateAsJson = objectMapper.writeValueAsString(state);

        assertTrue(stateAsJson.contains("\"name\":\"Adopted\""));
        assertTrue(stateAsJson.contains(String.format("\"adoptionFormId\":\"%s\"", adoptionFormId)));
        assertFalse(stateAsJson.contains(ESCAPED_ADOPTED_DETAILS));
        assertFalse(stateAsJson.contains(ESCAPED_UNAVAILABLE_DETAILS));
        assertFalse(stateAsJson.contains("\"notes\":"));
    }

    @Test
    void shouldBeSerializableAsAdoptedStateWithoutAdoptionFormId() throws JsonProcessingException {
        ObjectMapper objectMapper = TestUtils.getObjectMapper();
        State state = State.adopted(null);

        String stateAsJson = objectMapper.writeValueAsString(state);

        assertTrue(stateAsJson.contains("\"name\":\"Adopted\""));
        assertFalse(stateAsJson.contains("\"adoptionFormId\":"));
        assertFalse(stateAsJson.contains(ESCAPED_ADOPTED_DETAILS));
        assertFalse(stateAsJson.contains(ESCAPED_UNAVAILABLE_DETAILS));
        assertFalse(stateAsJson.contains("\"notes\":"));
    }

    @Test
    void shouldBeSerializableAsUnavailableState() throws JsonProcessingException {
        ObjectMapper objectMapper = TestUtils.getObjectMapper();
        State state = State.unavailable(notes);

        String stateAsJson = objectMapper.writeValueAsString(state);

        assertTrue(stateAsJson.contains("\"name\":\"Unavailable\""));
        assertTrue(stateAsJson.contains(String.format("\"notes\":\"%s\"", notes)));
        assertFalse(stateAsJson.contains(ESCAPED_ADOPTED_DETAILS));
        assertFalse(stateAsJson.contains(ESCAPED_UNAVAILABLE_DETAILS));
        assertFalse(stateAsJson.contains("\"adoptionFormId\":"));
    }
}