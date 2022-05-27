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

package ec.animal.adoption.domain.animal.characteristics;

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

class SizeTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource("sizes")
    void shouldDeserializeSizeUsingEnumName(final Size size) throws JsonProcessingException {
        String sizeWithEnumNameAsJson = JSONObject.quote(size.name());

        Size deSerializedSize = objectMapper.readValue(sizeWithEnumNameAsJson, Size.class);

        assertEquals(size, deSerializedSize);
    }

    private static Stream<Arguments> sizes() {
        return Stream.of(Arguments.of(Size.TINY),
                         Arguments.of(Size.SMALL),
                         Arguments.of(Size.MEDIUM),
                         Arguments.of(Size.BIG),
                         Arguments.of(Size.OUTSIZE));
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource("expectedNamesWithSpacesForSize")
    void shouldTrimSpacesInValueBeforeDeSerializing(final Size size, final String nameWithSpaces) throws JsonProcessingException {
        String sizeWithSpacesAsJson = JSONObject.quote(nameWithSpaces);

        Size deSerializedSize = objectMapper.readValue(sizeWithSpacesAsJson, Size.class);

        assertEquals(size, deSerializedSize);
    }

    private static Stream<Arguments> expectedNamesWithSpacesForSize() {
        return Stream.of(Arguments.of(Size.TINY, " TINY   "),
                         Arguments.of(Size.SMALL, "SMALL "),
                         Arguments.of(Size.MEDIUM, "    MEDIUM "),
                         Arguments.of(Size.BIG, " BIG "),
                         Arguments.of(Size.OUTSIZE, " OUTSIZE      "));
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