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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static ec.animal.adoption.TestUtils.getRandomBalance;
import static ec.animal.adoption.TestUtils.getRandomDocility;
import static ec.animal.adoption.TestUtils.getRandomSociability;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TemperamentsTest {

    @Test
    public void shouldSetSociability() {
        Sociability sociability = getRandomSociability();
        Temperaments temperaments = TemperamentsFactory.random().withSociability(sociability).build();

        assertTrue(temperaments.getSociability().isPresent());
        assertEquals(sociability, temperaments.getSociability().get());
    }

    @Test
    public void shouldSetDocility() {
        Docility docility = getRandomDocility();
        Temperaments temperaments = TemperamentsFactory.random().withDocility(docility).build();

        assertTrue(temperaments.getDocility().isPresent());
        assertEquals(docility, temperaments.getDocility().get());
    }

    @Test
    public void shouldSetBalance() {
        Balance balance = getRandomBalance();
        Temperaments temperaments = TemperamentsFactory.random().withBalance(balance).build();

        assertTrue(temperaments.getBalance().isPresent());
        assertEquals(balance, temperaments.getBalance().get());
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Temperaments.class).usingGetClass().verify();
    }
}