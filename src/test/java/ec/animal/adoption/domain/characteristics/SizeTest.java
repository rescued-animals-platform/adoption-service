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

package ec.animal.adoption.domain.characteristics;

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

class SizeTest {

    private static final String EXPECTED_NAMES_FOR_SIZE_METHOD = "expectedNamesForSize";

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} name is \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_SIZE_METHOD)
    void shouldReturnExpectedNameForSize(final Size size, final String expectedName) {
        assertEquals(expectedName, size.toString());
    }

    @ParameterizedTest(name = "{index} {0} is serialized as \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_SIZE_METHOD)
    void shouldSerializeSizeUsingName(final Size size, final String expectedName) throws JsonProcessingException {
        String sizeAsJson = objectMapper.writeValueAsString(size);

        assertEquals(JSONObject.quote(expectedName), sizeAsJson);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_SIZE_METHOD)
    void shouldDeserializeSizeUsingName(final Size size, final String expectedName) throws JsonProcessingException {
        String sizeWithNameAsJson = JSONObject.quote(expectedName);

        Size deSerializedSize = objectMapper.readValue(sizeWithNameAsJson, Size.class);

        assertEquals(size, deSerializedSize);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_SIZE_METHOD)
    void shouldDeserializeSizeUsingEnumName(final Size size) throws JsonProcessingException {
        String sizeWithEnumNameAsJson = JSONObject.quote(size.name());

        Size deSerializedSize = objectMapper.readValue(sizeWithEnumNameAsJson, Size.class);

        assertEquals(size, deSerializedSize);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedNamesForSize() {
        return Stream.of(
                Arguments.of(Size.TINY, "Tiny"),
                Arguments.of(Size.SMALL, "Small"),
                Arguments.of(Size.MEDIUM, "Medium"),
                Arguments.of(Size.BIG, "Big"),
                Arguments.of(Size.OUTSIZE, "Outsize")
        );
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource("expectedNamesWithSpacesForSize")
    void shouldTrimSpacesInValueBeforeDeSerializing(final Size size, final String nameWithSpaces) throws JsonProcessingException {
        String sizeWithSpacesAsJson = JSONObject.quote(nameWithSpaces);

        Size deSerializedSize = objectMapper.readValue(sizeWithSpacesAsJson, Size.class);

        assertEquals(size, deSerializedSize);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedNamesWithSpacesForSize() {
        return Stream.of(
                Arguments.of(Size.TINY, " Tiny "),
                Arguments.of(Size.TINY, " TINY   "),
                Arguments.of(Size.SMALL, "   Small"),
                Arguments.of(Size.SMALL, "SMALL "),
                Arguments.of(Size.MEDIUM, "Medium "),
                Arguments.of(Size.MEDIUM, "    MEDIUM "),
                Arguments.of(Size.BIG, " Big "),
                Arguments.of(Size.BIG, " BIG "),
                Arguments.of(Size.OUTSIZE, "     Outsize "),
                Arguments.of(Size.OUTSIZE, " OUTSIZE      ")
        );
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidSizeAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidSizeAsJson, Size.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }
}