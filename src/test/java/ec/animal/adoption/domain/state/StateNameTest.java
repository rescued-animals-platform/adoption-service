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
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import ec.animal.adoption.TestUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.*;

class StateNameTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource("stateNames")
    void shouldDeserializeStateNameUsingEnumName(final StateName stateName) throws JsonProcessingException {
        String stateNameWithEnumNameAsJson = JSONObject.quote(stateName.name());

        StateName deSerializedStateName = objectMapper.readValue(stateNameWithEnumNameAsJson, StateName.class);

        assertEquals(stateName, deSerializedStateName);
    }

    private static Stream<Arguments> stateNames() {
        return Stream.of(Arguments.of(StateName.LOOKING_FOR_HUMAN),
                         Arguments.of(StateName.ADOPTED),
                         Arguments.of(StateName.UNAVAILABLE));
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource("expectedNamesWithSpacesForStateName")
    void shouldTrimSpacesInValueBeforeDeSerializing(final StateName stateName, final String nameWithSpaces) throws JsonProcessingException {
        String stateNameWithSpacesAsJson = JSONObject.quote(nameWithSpaces);

        StateName deSerializedStateName = objectMapper.readValue(stateNameWithSpacesAsJson, StateName.class);

        assertEquals(stateName, deSerializedStateName);
    }

    private static Stream<Arguments> expectedNamesWithSpacesForStateName() {
        return Stream.of(Arguments.of(StateName.LOOKING_FOR_HUMAN, " LOOKING_FOR_HUMAN   "),
                         Arguments.of(StateName.ADOPTED, "ADOPTED "),
                         Arguments.of(StateName.UNAVAILABLE, "    UNAVAILABLE "));
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidStateNameAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidStateNameAsJson, StateName.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }
}