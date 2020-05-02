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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StateTest {

    @ParameterizedTest(name = "{index} \"{0}\" is a valid state name")
    @ValueSource(strings = {"lookingForHuman",
                            "LookingForHuman",
                            "Looking for human",
                            "looking for human",
                            "LOOKING_FOR_HUMAN",
                            "looking_for_human",
                            "adopted",
                            "Adopted",
                            "ADOPTED",
                            "unavailable",
                            "Unavailable",
                            "UNAVAILABLE"})
    public void shouldReturnTrueForValidStateNames(final String stateName) {
        assertTrue(State.isStateNameValid(stateName));
    }

    @ParameterizedTest(name = "{index} \"{0}\" is an invalid state name")
    @ValueSource(strings = {"LoOking_For_HuMan",
                            "Un-available",
                            "akshudbjw27367",
                            "anyOther",
                            ""})
    public void shouldReturnFalseForInvalidStateNames(final String stateName) {
        assertFalse(State.isStateNameValid(stateName));
    }

    @Test
    void shouldReturnNullForNullStateName() {
        assertNull(State.normalize(null));
    }

    @ParameterizedTest(name = "{index} \"{0}\" is normalized to \"lookingForHuman\"")
    @ValueSource(strings = {"lookingForHuman",
                            "LookingForHuman",
                            "Looking for human",
                            "looking for human",
                            "LOOKING_FOR_HUMAN",
                            "looking_for_human"})
    void shouldReturnLookingForHumanNormalizedName(final String stateName) {
        assertEquals("lookingForHuman", State.normalize(stateName));
    }

    @ParameterizedTest(name = "{index} \"{0}\" is normalized to \"adopted\"")
    @ValueSource(strings = {"adopted", "Adopted", "ADOPTED"})
    void shouldReturnAdoptedNormalizedName(final String stateName) {
        assertEquals("adopted", State.normalize(stateName));
    }

    @ParameterizedTest(name = "{index} \"{0}\" is normalized to \"unavailable\"")
    @ValueSource(strings = {"unavailable", "Unavailable", "UNAVAILABLE"})
    void shouldReturnUnavailableNormalizedName(final String stateName) {
        assertEquals("unavailable", State.normalize(stateName));
    }

    @ParameterizedTest(name = "{index} \"{0}\" is normalized to \"null\"")
    @ValueSource(strings = {"LoOking_For_HuMan",
                            "Un-available",
                            "akshudbjw27367",
                            "anyOther",
                            ""})
    void shouldReturnNullForUnrecognizedStateName(final String stateName) {
        assertNull(State.normalize(stateName));
    }
}