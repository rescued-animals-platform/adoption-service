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

class SociabilityTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource("sociabilityValues")
    void shouldDeserializeSociabilityUsingEnumName(final Sociability sociability) throws JsonProcessingException {
        String sociabilityWithEnumNameAsJson = JSONObject.quote(sociability.name());

        Sociability deSerializedSociability = objectMapper.readValue(sociabilityWithEnumNameAsJson, Sociability.class);

        assertEquals(sociability, deSerializedSociability);
    }

    private static Stream<Arguments> sociabilityValues() {
        return Stream.of(Arguments.of(Sociability.VERY_SOCIABLE),
                         Arguments.of(Sociability.SOCIABLE),
                         Arguments.of(Sociability.NEITHER_SOCIABLE_NOR_SHY),
                         Arguments.of(Sociability.SHY),
                         Arguments.of(Sociability.VERY_SHY));
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource("expectedNamesWithSpacesForSociability")
    void shouldTrimSpacesInValueBeforeDeSerializing(final Sociability sociability, final String nameWithSpaces) throws JsonProcessingException {
        String sociabilityWithSpacesAsJson = JSONObject.quote(nameWithSpaces);

        Sociability deSerializedSociability = objectMapper.readValue(sociabilityWithSpacesAsJson, Sociability.class);

        assertEquals(sociability, deSerializedSociability);
    }

    private static Stream<Arguments> expectedNamesWithSpacesForSociability() {
        return Stream.of(Arguments.of(Sociability.VERY_SOCIABLE, " VERY_SOCIABLE   "),
                         Arguments.of(Sociability.SOCIABLE, "SOCIABLE "),
                         Arguments.of(Sociability.NEITHER_SOCIABLE_NOR_SHY, "    NEITHER_SOCIABLE_NOR_SHY "),
                         Arguments.of(Sociability.SHY, " SHY "),
                         Arguments.of(Sociability.VERY_SHY, " VERY_SHY      "));
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidSociabilityAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidSociabilityAsJson, Sociability.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }
}