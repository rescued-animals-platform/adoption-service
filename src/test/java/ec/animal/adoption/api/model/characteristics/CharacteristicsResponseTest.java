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

package ec.animal.adoption.api.model.characteristics;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import ec.animal.adoption.api.model.characteristics.temperaments.TemperamentsResponse;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.CharacteristicsFactory;
import ec.animal.adoption.domain.characteristics.FriendlyWith;
import ec.animal.adoption.domain.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;
import ec.animal.adoption.domain.characteristics.temperaments.TemperamentsFactory;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CharacteristicsResponseTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @Test
    void shouldBeSerializable() throws IOException {
        Characteristics characteristics = CharacteristicsFactory.random().withFriendlyWith(FriendlyWith.ADULTS).build();
        String expectedTemperamentsResponseAsJson = objectMapper.writeValueAsString(
                TemperamentsResponse.from(characteristics.getTemperaments())
        );
        CharacteristicsResponse characteristicsResponse = CharacteristicsResponse.from(characteristics);

        String characteristicsResponseAsJson = objectMapper.writeValueAsString(characteristicsResponse);

        assertTrue(characteristicsResponseAsJson.contains(String.format("\"size\":\"%s\"", characteristics.getSize().name())));
        assertTrue(characteristicsResponseAsJson.contains(
                String.format("\"physicalActivity\":\"%s\"", characteristics.getPhysicalActivity().name())
        ));
        assertTrue(characteristicsResponseAsJson.contains(String.format("\"temperaments\":%s", expectedTemperamentsResponseAsJson)));
        assertTrue(characteristicsResponseAsJson.contains(String.format("\"friendlyWith\":[\"%s\"]", FriendlyWith.ADULTS.name())));
    }

    @Test
    void shouldBeDeSerializable() throws IOException, JSONException {
        Temperaments temperaments = TemperamentsFactory.empty().withBalance(Balance.POSSESSIVE).build();
        Characteristics characteristics = CharacteristicsFactory.random()
                                                                .withTemperaments(temperaments)
                                                                .withFriendlyWith(FriendlyWith.DOGS)
                                                                .build();
        CharacteristicsResponse expectedCharacteristicsResponse = CharacteristicsResponse.from(characteristics);
        String characteristicsResponseAsJson = new JSONObject()
                .put("size", characteristics.getSize().name())
                .put("physicalActivity", characteristics.getPhysicalActivity().name())
                .put("temperaments", new JSONObject().put("balance", Balance.POSSESSIVE.name()))
                .put("friendlyWith", new JSONArray().put(FriendlyWith.DOGS.name()))
                .toString();

        CharacteristicsResponse characteristicsResponse = objectMapper.readValue(characteristicsResponseAsJson,
                                                                                 CharacteristicsResponse.class);

        Assertions.assertThat(characteristicsResponse)
                  .usingRecursiveComparison()
                  .isEqualTo(expectedCharacteristicsResponse);
    }
}