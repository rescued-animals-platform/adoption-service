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

package ec.animal.adoption.domain.characteristics.temperaments;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.builders.TemperamentsBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static ec.animal.adoption.TestUtils.getObjectMapper;
import static ec.animal.adoption.TestUtils.getRandomBalance;
import static ec.animal.adoption.TestUtils.getRandomDocility;
import static ec.animal.adoption.TestUtils.getRandomSociability;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TemperamentsTest {

    @Test
    public void shouldSetSociability() {
        Sociability sociability = getRandomSociability();
        Temperaments temperaments = TemperamentsBuilder.random().withSociability(sociability).build();

        assertThat(temperaments.getSociability(), is(sociability));
    }

    @Test
    public void shouldSetDocility() {
        Docility docility = getRandomDocility();
        Temperaments temperaments = TemperamentsBuilder.random().withDocility(docility).build();

        assertThat(temperaments.getDocility(), is(docility));
    }

    @Test
    public void shouldSetBalance() {
        Balance balance = getRandomBalance();
        Temperaments temperaments = TemperamentsBuilder.random().withBalance(balance).build();

        assertThat(temperaments.getBalance(), is(balance));
    }

    @Test
    public void shouldReturnTrueIfTemperamentsIsEmpty() {
        Temperaments temperaments = TemperamentsBuilder.empty().build();

        assertThat(temperaments.isEmpty(), is(true));
    }

    @Test
    public void shouldReturnFalseIfOnlySociabilityIsSet() {
        Temperaments temperaments = TemperamentsBuilder.empty().withSociability(getRandomSociability()).build();

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldReturnFalseIfOnlyDocilityIsSet() {
        Temperaments temperaments = TemperamentsBuilder.empty().withDocility(getRandomDocility()).build();

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldReturnFalseIfOnlyBalanceIsSet() {
        Temperaments temperaments = TemperamentsBuilder.empty().withBalance(getRandomBalance()).build();

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldReturnFalseIfSociabilityAndDocilityAreSet() {
        Temperaments temperaments = TemperamentsBuilder.random().withBalance(null).build();

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldReturnFalseIfSociabilityAndBalanceAreSet() {
        Temperaments temperaments = TemperamentsBuilder.random().withDocility(null).build();

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldReturnFalseIfDocilityAndBalanceAreSet() {
        Temperaments temperaments = TemperamentsBuilder.random().withSociability(null).build();

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Temperaments.class).usingGetClass().suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        Temperaments temperaments = TemperamentsBuilder.random().build();
        ObjectMapper objectMapper = getObjectMapper();

        String serializedTemperaments = objectMapper.writeValueAsString(temperaments);
        Temperaments deserializedTemperaments = objectMapper.readValue(
                serializedTemperaments, Temperaments.class
        );

        assertThat(deserializedTemperaments, is(temperaments));
    }
}