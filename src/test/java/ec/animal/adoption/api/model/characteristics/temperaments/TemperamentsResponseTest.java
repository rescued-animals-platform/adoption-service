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
import ec.animal.adoption.domain.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;
import ec.animal.adoption.domain.characteristics.temperaments.TemperamentsBuilder;
import ec.animal.adoption.domain.utils.TranslatorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.util.ReflectionTestUtils;

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
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setUseCodeAsDefaultMessage(true);
        ReflectionTestUtils.setField(TranslatorUtils.class, "messageSource", messageSource);
    }

    @Test
    public void shouldBeSerializable() throws IOException {
        Sociability sociability = getRandomSociability();
        Docility docility = getRandomDocility();
        Balance balance = getRandomBalance();
        Temperaments temperaments = TemperamentsBuilder.empty()
                                                       .withSociability(sociability)
                                                       .withDocility(docility)
                                                       .withBalance(balance)
                                                       .build();
        TemperamentsResponse temperamentsResponse = TemperamentsResponse.from(temperaments);

        String temperamentsResponseAsJson = objectMapper.writeValueAsString(temperamentsResponse);

        assertTrue(temperamentsResponseAsJson.contains(String.format("\"sociability\":\"%s\"", sociability.toTranslatedName())));
        assertTrue(temperamentsResponseAsJson.contains(String.format("\"docility\":\"%s\"", docility.toTranslatedName())));
        assertTrue(temperamentsResponseAsJson.contains(String.format("\"balance\":\"%s\"", balance.toTranslatedName())));
    }

    @Test
    public void shouldBeSerializableContainingOnlySociability() throws IOException {
        Sociability sociability = getRandomSociability();
        Temperaments temperaments = TemperamentsBuilder.empty().withSociability(sociability).build();
        TemperamentsResponse temperamentsResponse = TemperamentsResponse.from(temperaments);

        String temperamentsResponseAsJson = objectMapper.writeValueAsString(temperamentsResponse);

        assertTrue(temperamentsResponseAsJson.contains(String.format("\"sociability\":\"%s\"", sociability.toTranslatedName())));
        assertFalse(temperamentsResponseAsJson.contains("\"docility\":"));
        assertFalse(temperamentsResponseAsJson.contains("\"balance\":"));
    }

    @Test
    public void shouldBeSerializableContainingOnlyDocility() throws IOException {
        Docility docility = getRandomDocility();
        Temperaments temperaments = TemperamentsBuilder.empty().withDocility(docility).build();
        TemperamentsResponse temperamentsResponse = TemperamentsResponse.from(temperaments);

        String temperamentsResponseAsJson = objectMapper.writeValueAsString(temperamentsResponse);

        assertTrue(temperamentsResponseAsJson.contains(String.format("\"docility\":\"%s\"", docility.toTranslatedName())));
        assertFalse(temperamentsResponseAsJson.contains("\"sociability\":"));
        assertFalse(temperamentsResponseAsJson.contains("\"balance\":"));
    }

    @Test
    public void shouldBeSerializableContainingOnlyBalance() throws IOException {
        Balance balance = getRandomBalance();
        Temperaments temperaments = TemperamentsBuilder.empty().withBalance(balance).build();
        TemperamentsResponse temperamentsResponse = TemperamentsResponse.from(temperaments);

        String temperamentsResponseAsJson = objectMapper.writeValueAsString(temperamentsResponse);

        assertTrue(temperamentsResponseAsJson.contains(String.format("\"balance\":\"%s\"", balance.toTranslatedName())));
        assertFalse(temperamentsResponseAsJson.contains("\"sociability\":"));
        assertFalse(temperamentsResponseAsJson.contains("\"docility\":"));
    }
}