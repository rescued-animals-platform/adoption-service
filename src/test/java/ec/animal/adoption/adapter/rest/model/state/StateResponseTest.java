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

package ec.animal.adoption.adapter.rest.model.state;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.model.state.State;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ec.animal.adoption.TestUtils.getRandomState;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StateResponseTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @Test
    void shouldBeSerializableForLookingForHumanStateResponse() throws JsonProcessingException {
        State state = State.lookingForHuman();
        StateResponse stateResponse = StateResponse.from(state);

        String createStateResponseAsJson = objectMapper.writeValueAsString(stateResponse);

        assertTrue(createStateResponseAsJson.contains(String.format("\"name\":\"%s\"", state.getName().name())));
    }

    @Test
    void shouldBeSerializableForAdoptedStateResponse() throws JsonProcessingException {
        String adoptionFormId = randomAlphabetic(10);
        State state = State.adopted(adoptionFormId);
        StateResponse stateResponse = StateResponse.from(state);

        String createStateResponseAsJson = objectMapper.writeValueAsString(stateResponse);

        assertTrue(createStateResponseAsJson.contains(String.format("\"name\":\"%s\"", state.getName().name())));
        assertTrue(createStateResponseAsJson.contains(String.format("\"adoptionFormId\":\"%s\"", adoptionFormId)));
    }

    @Test
    void shouldBeSerializableForUnavailableStateResponse() throws JsonProcessingException {
        String notes = randomAlphabetic(10);
        State state = State.unavailable(notes);
        StateResponse stateResponse = StateResponse.from(state);

        String createStateResponseAsJson = objectMapper.writeValueAsString(stateResponse);

        assertTrue(createStateResponseAsJson.contains(String.format("\"name\":\"%s\"", state.getName().name())));
        assertTrue(createStateResponseAsJson.contains(String.format("\"notes\":\"%s\"", notes)));
    }

    @Test
    void shouldBeDeSerializable() throws JsonProcessingException, JSONException {
        State state = getRandomState();
        StateResponse expectedStateResponse = StateResponse.from(state);
        String createStateResponseAsJson = new JSONObject()
                .put("name", state.getName().name())
                .put("adoptionFormId", state.getAdoptionFormId().orElse(null))
                .put("notes", state.getNotes().orElse(null))
                .toString();

        StateResponse stateResponse = objectMapper.readValue(createStateResponseAsJson, StateResponse.class);

        Assertions.assertThat(stateResponse).usingRecursiveComparison().isEqualTo(expectedStateResponse);
    }
}