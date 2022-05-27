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

package ec.animal.adoption.domain.model.characteristics.temperaments;

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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DocilityTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource("docilityValues")
    void shouldDeserializeDocilityUsingEnumName(final Docility docility) throws JsonProcessingException {
        String docilityWithEnumNameAsJson = JSONObject.quote(docility.name());

        Docility deSerializedDocility = objectMapper.readValue(docilityWithEnumNameAsJson, Docility.class);

        assertEquals(docility, deSerializedDocility);
    }

    private static Stream<Arguments> docilityValues() {
        return Stream.of(Arguments.of(Docility.VERY_DOCILE),
                         Arguments.of(Docility.DOCILE),
                         Arguments.of(Docility.NEITHER_DOCILE_NOR_DOMINANT),
                         Arguments.of(Docility.DOMINANT),
                         Arguments.of(Docility.VERY_DOMINANT));
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource("expectedNamesWithSpacesForDocility")
    void shouldTrimSpacesInValueBeforeDeSerializing(final Docility docility, final String nameWithSpaces) throws JsonProcessingException {
        String docilityWithSpacesAsJson = JSONObject.quote(nameWithSpaces);

        Docility deSerializedDocility = objectMapper.readValue(docilityWithSpacesAsJson, Docility.class);

        assertEquals(docility, deSerializedDocility);
    }

    private static Stream<Arguments> expectedNamesWithSpacesForDocility() {
        return Stream.of(Arguments.of(Docility.VERY_DOCILE, " VERY_DOCILE   "),
                         Arguments.of(Docility.DOCILE, "DOCILE "),
                         Arguments.of(Docility.NEITHER_DOCILE_NOR_DOMINANT, "    NEITHER_DOCILE_NOR_DOMINANT "),
                         Arguments.of(Docility.DOMINANT, " DOMINANT "),
                         Arguments.of(Docility.VERY_DOMINANT, " VERY_DOMINANT      "));
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidDocilityAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidDocilityAsJson, Docility.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }
}