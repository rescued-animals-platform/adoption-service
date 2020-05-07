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

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DocilityTest {

    public static final String EXPECTED_NAMES_FOR_DOCILITY_METHOD = "expectedNamesForDocility";

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} name is \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_DOCILITY_METHOD)
    void shouldReturnExpectedNameForDocility(final Docility docility, final String expectedName) {
        assertEquals(expectedName, docility.toString());
    }

    @ParameterizedTest(name = "{index} {0} is serialized as \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_DOCILITY_METHOD)
    void shouldSerializeDocilityUsingName(final Docility docility, final String expectedName) throws JsonProcessingException {
        String docilityAsJson = objectMapper.writeValueAsString(docility);

        assertEquals(JSONObject.quote(expectedName), docilityAsJson);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_DOCILITY_METHOD)
    void shouldDeserializeDocilityUsingName(final Docility docility, final String expectedName) throws JsonProcessingException {
        String docilityWithNameAsJson = JSONObject.quote(expectedName);

        Docility deSerializedDocility = objectMapper.readValue(docilityWithNameAsJson, Docility.class);

        assertEquals(docility, deSerializedDocility);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_DOCILITY_METHOD)
    void shouldDeserializeDocilityUsingEnumName(final Docility docility) throws JsonProcessingException {
        String docilityWithEnumNameAsJson = JSONObject.quote(docility.name());

        Docility deSerializedDocility = objectMapper.readValue(docilityWithEnumNameAsJson, Docility.class);

        assertEquals(docility, deSerializedDocility);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedNamesForDocility() {
        return Stream.of(
                Arguments.of(Docility.VERY_DOCILE, "Very docile"),
                Arguments.of(Docility.DOCILE, "Docile"),
                Arguments.of(Docility.NEITHER_DOCILE_NOR_DOMINANT, "Neither docile nor dominant"),
                Arguments.of(Docility.DOMINANT, "Dominant"),
                Arguments.of(Docility.VERY_DOMINANT, "Very dominant")
        );
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource("expectedNamesWithSpacesForDocility")
    void shouldTrimSpacesInValueBeforeDeSerializing(final Docility docility, final String nameWithSpaces) throws JsonProcessingException {
        String docilityWithSpacesAsJson = JSONObject.quote(nameWithSpaces);

        Docility deSerializedDocility = objectMapper.readValue(docilityWithSpacesAsJson, Docility.class);

        assertEquals(docility, deSerializedDocility);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedNamesWithSpacesForDocility() {
        return Stream.of(
                Arguments.of(Docility.VERY_DOCILE, " Very docile "),
                Arguments.of(Docility.VERY_DOCILE, " VERY_DOCILE   "),
                Arguments.of(Docility.DOCILE, "   Docile"),
                Arguments.of(Docility.DOCILE, "DOCILE "),
                Arguments.of(Docility.NEITHER_DOCILE_NOR_DOMINANT, "Neither docile nor dominant "),
                Arguments.of(Docility.NEITHER_DOCILE_NOR_DOMINANT, "    NEITHER_DOCILE_NOR_DOMINANT "),
                Arguments.of(Docility.DOMINANT, " Dominant "),
                Arguments.of(Docility.DOMINANT, " DOMINANT "),
                Arguments.of(Docility.VERY_DOMINANT, "     Very dominant "),
                Arguments.of(Docility.VERY_DOMINANT, " VERY_DOMINANT      ")
        );
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