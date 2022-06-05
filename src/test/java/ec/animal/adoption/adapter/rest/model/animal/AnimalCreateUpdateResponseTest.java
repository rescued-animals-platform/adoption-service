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

package ec.animal.adoption.adapter.rest.model.animal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import ec.animal.adoption.adapter.rest.service.AnimalCreateUpdateResponseMapper;
import ec.animal.adoption.domain.model.animal.Animal;
import ec.animal.adoption.domain.model.animal.AnimalFactory;
import ec.animal.adoption.domain.model.state.State;
import ec.animal.adoption.domain.model.state.StateName;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnimalCreateUpdateResponseTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @Test
    void shouldBeSerializable() throws JsonProcessingException {
        Animal animal = AnimalFactory.random().withState(State.adopted(null)).build();
        String expectedRegistrationDateAsJson = objectMapper.writeValueAsString(animal.getRegistrationDate());
        AnimalCreateUpdateResponse animalCreateUpdateResponse = AnimalCreateUpdateResponseMapper.MAPPER.toAnimalCreateUpdateResponse(animal);

        String createAnimalResponseAsJson = objectMapper.writeValueAsString(animalCreateUpdateResponse);

        assertAll(
                () -> assertTrue(createAnimalResponseAsJson.contains(String.format("\"id\":\"%s\"", animal.getIdentifier()))),
                () -> assertTrue(createAnimalResponseAsJson.contains(String.format("\"registrationDate\":%s", expectedRegistrationDateAsJson))),
                () -> assertTrue(createAnimalResponseAsJson.contains(String.format("\"clinicalRecord\":\"%s\"", animal.getClinicalRecord()))),
                () -> assertTrue(createAnimalResponseAsJson.contains(String.format("\"name\":\"%s\"", animal.getName()))),
                () -> assertTrue(createAnimalResponseAsJson.contains(String.format("\"species\":\"%s\"", animal.getSpecies().name()))),
                () -> assertTrue(createAnimalResponseAsJson.contains(String.format("\"estimatedAge\":\"%s\"", animal.getEstimatedAge().name()))),
                () -> assertTrue(createAnimalResponseAsJson.contains(String.format("\"sex\":\"%s\"", animal.getSex().name()))),
                () -> assertTrue(createAnimalResponseAsJson.contains(String.format("\"state\":{\"name\":\"%s\"}", StateName.ADOPTED.name())))
        );
    }

    @Test
    void shouldBeDeSerializable() throws JsonProcessingException, JSONException {
        String notes = randomAlphabetic(10);
        Animal animal = AnimalFactory.random().withState(State.unavailable(notes)).build();
        AnimalCreateUpdateResponse expectedAnimalCreateUpdateResponse = AnimalCreateUpdateResponseMapper.MAPPER.toAnimalCreateUpdateResponse(animal);
        String createAnimalResponseAsJson = new JSONObject()
                .put("id", animal.getIdentifier())
                .put("registrationDate", animal.getRegistrationDate())
                .put("clinicalRecord", animal.getClinicalRecord())
                .put("name", animal.getName())
                .put("species", animal.getSpecies().name())
                .put("estimatedAge", animal.getEstimatedAge().name())
                .put("sex", animal.getSex().name())
                .put("state", new JSONObject()
                        .put("name", StateName.UNAVAILABLE.name())
                        .put("notes", notes)
                ).toString();

        AnimalCreateUpdateResponse animalCreateUpdateResponse = objectMapper.readValue(createAnimalResponseAsJson, AnimalCreateUpdateResponse.class);

        Assertions.assertThat(animalCreateUpdateResponse).usingRecursiveComparison().isEqualTo(expectedAnimalCreateUpdateResponse);
    }
}