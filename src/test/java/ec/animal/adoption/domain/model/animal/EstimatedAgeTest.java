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

package ec.animal.adoption.domain.model.animal;

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

class EstimatedAgeTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource("estimatedAges")
    void shouldDeserializeEstimatedAgeUsingEnumName(final EstimatedAge estimatedAge) throws JsonProcessingException {
        String estimatedAgeWithEnumNameAsJson = JSONObject.quote(estimatedAge.name());

        EstimatedAge deSerializedEstimatedAge = objectMapper.readValue(estimatedAgeWithEnumNameAsJson, EstimatedAge.class);

        assertEquals(estimatedAge, deSerializedEstimatedAge);
    }

    private static Stream<Arguments> estimatedAges() {
        return Stream.of(
                Arguments.of(EstimatedAge.YOUNG),
                Arguments.of(EstimatedAge.YOUNG_ADULT),
                Arguments.of(EstimatedAge.SENIOR_ADULT)
        );
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource("expectedNamesWithSpacesForEstimatedAge")
    void shouldTrimSpacesInValueBeforeDeSerializing(final EstimatedAge estimatedAge, final String nameWithSpaces) throws JsonProcessingException {
        String estimatedAgeWithSpacesAsJson = JSONObject.quote(nameWithSpaces);

        EstimatedAge deSerializedEstimatedAge = objectMapper.readValue(estimatedAgeWithSpacesAsJson, EstimatedAge.class);

        assertEquals(estimatedAge, deSerializedEstimatedAge);
    }

    private static Stream<Arguments> expectedNamesWithSpacesForEstimatedAge() {
        return Stream.of(
                Arguments.of(EstimatedAge.YOUNG, " YOUNG   "),
                Arguments.of(EstimatedAge.YOUNG_ADULT, "YOUNG_ADULT "),
                Arguments.of(EstimatedAge.SENIOR_ADULT, " SENIOR_ADULT    ")
        );
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidEstimatedAgeAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidEstimatedAgeAsJson, EstimatedAge.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }
}