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

package ec.animal.adoption.domain.animal;

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

class SexTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource("sexes")
    void shouldDeserializeSexUsingEnumName(final Sex sex) throws JsonProcessingException {
        String sexWithEnumNameAsJson = JSONObject.quote(sex.name());

        Sex deSerializedSex = objectMapper.readValue(sexWithEnumNameAsJson, Sex.class);

        assertEquals(sex, deSerializedSex);
    }

    private static Stream<Arguments> sexes() {
        return Stream.of(Arguments.of(Sex.FEMALE), Arguments.of(Sex.MALE));
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource("expectedNamesWithSpacesForSex")
    void shouldTrimSpacesInValueBeforeDeSerializing(final Sex sex, final String nameWithSpaces) throws JsonProcessingException {
        String sexWithSpacesAsJson = JSONObject.quote(nameWithSpaces);

        Sex deSerializedSex = objectMapper.readValue(sexWithSpacesAsJson, Sex.class);

        assertEquals(sex, deSerializedSex);
    }

    private static Stream<Arguments> expectedNamesWithSpacesForSex() {
        return Stream.of(
                Arguments.of(Sex.MALE, " MALE   "),
                Arguments.of(Sex.FEMALE, "FEMALE ")
        );
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidSexAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidSexAsJson, Sex.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }
}