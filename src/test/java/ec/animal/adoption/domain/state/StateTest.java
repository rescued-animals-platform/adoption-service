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

import org.junit.jupiter.api.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StateTest {

    @Test
    public void shouldReturnTrueForValidLookingForHumanStateName() {
        assertTrue(State.isValidStateName("lookingForHuman"));
    }

    @Test
    public void shouldReturnTrueForValidAdoptedStateName() {
        assertTrue(State.isValidStateName("adopted"));
    }

    @Test
    public void shouldReturnTrueForValidUnavailableStateName() {
        assertTrue(State.isValidStateName("unavailable"));
    }

    @Test
    public void shouldReturnFalseForInvalidStateName() {
        assertFalse(State.isValidStateName(randomAlphabetic(10)));
    }
}