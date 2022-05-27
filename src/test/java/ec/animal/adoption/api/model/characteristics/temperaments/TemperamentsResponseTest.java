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

package ec.animal.adoption.api.model.characteristics.temperaments;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.animal.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.animal.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.animal.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.animal.characteristics.temperaments.Temperaments;
import ec.animal.adoption.domain.animal.characteristics.temperaments.TemperamentsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static ec.animal.adoption.TestUtils.getRandomBalance;
import static ec.animal.adoption.TestUtils.getRandomDocility;
import static ec.animal.adoption.TestUtils.getRandomSociability;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemperamentsResponseTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @Test
    void shouldBeSerializable() throws IOException {
        Sociability sociability = getRandomSociability();
        Docility docility = getRandomDocility();
        Balance balance = getRandomBalance();
        Temperaments temperaments = TemperamentsFactory.empty()
                                                       .withSociability(sociability)
                                                       .withDocility(docility)
                                                       .withBalance(balance)
                                                       .build();
        TemperamentsResponse temperamentsResponse = TemperamentsResponse.from(temperaments);

        String temperamentsResponseAsJson = objectMapper.writeValueAsString(temperamentsResponse);

        assertTrue(temperamentsResponseAsJson.contains(String.format("\"sociability\":\"%s\"", sociability.name())));
        assertTrue(temperamentsResponseAsJson.contains(String.format("\"docility\":\"%s\"", docility.name())));
        assertTrue(temperamentsResponseAsJson.contains(String.format("\"balance\":\"%s\"", balance.name())));
    }

    @Test
    void shouldBeSerializableContainingOnlySociability() throws IOException {
        Sociability sociability = getRandomSociability();
        Temperaments temperaments = TemperamentsFactory.empty().withSociability(sociability).build();
        TemperamentsResponse temperamentsResponse = TemperamentsResponse.from(temperaments);

        String temperamentsResponseAsJson = objectMapper.writeValueAsString(temperamentsResponse);

        assertTrue(temperamentsResponseAsJson.contains(String.format("\"sociability\":\"%s\"", sociability.name())));
        assertFalse(temperamentsResponseAsJson.contains("\"docility\":"));
        assertFalse(temperamentsResponseAsJson.contains("\"balance\":"));
    }

    @Test
    void shouldBeSerializableContainingOnlyDocility() throws IOException {
        Docility docility = getRandomDocility();
        Temperaments temperaments = TemperamentsFactory.empty().withDocility(docility).build();
        TemperamentsResponse temperamentsResponse = TemperamentsResponse.from(temperaments);

        String temperamentsResponseAsJson = objectMapper.writeValueAsString(temperamentsResponse);

        assertTrue(temperamentsResponseAsJson.contains(String.format("\"docility\":\"%s\"", docility.name())));
        assertFalse(temperamentsResponseAsJson.contains("\"sociability\":"));
        assertFalse(temperamentsResponseAsJson.contains("\"balance\":"));
    }

    @Test
    void shouldBeSerializableContainingOnlyBalance() throws IOException {
        Balance balance = getRandomBalance();
        Temperaments temperaments = TemperamentsFactory.empty().withBalance(balance).build();
        TemperamentsResponse temperamentsResponse = TemperamentsResponse.from(temperaments);

        String temperamentsResponseAsJson = objectMapper.writeValueAsString(temperamentsResponse);

        assertTrue(temperamentsResponseAsJson.contains(String.format("\"balance\":\"%s\"", balance.name())));
        assertFalse(temperamentsResponseAsJson.contains("\"sociability\":"));
        assertFalse(temperamentsResponseAsJson.contains("\"docility\":"));
    }
}