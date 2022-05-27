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

class PhysicalActivityTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource("physicalActivities")
    void shouldDeserializePhysicalActivityUsingEnumName(final PhysicalActivity physicalActivity) throws JsonProcessingException {
        String physicalActivityWithEnumNameAsJson = JSONObject.quote(physicalActivity.name());

        PhysicalActivity deSerializedPhysicalActivity = objectMapper.readValue(physicalActivityWithEnumNameAsJson, PhysicalActivity.class);

        assertEquals(physicalActivity, deSerializedPhysicalActivity);
    }

    private static Stream<Arguments> physicalActivities() {
        return Stream.of(Arguments.of(PhysicalActivity.HIGH),
                         Arguments.of(PhysicalActivity.MEDIUM),
                         Arguments.of(PhysicalActivity.LOW));
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource("expectedNamesWithSpacesForPhysicalActivity")
    void shouldTrimSpacesInValueBeforeDeSerializing(final PhysicalActivity physicalActivity, final String nameWithSpaces) throws JsonProcessingException {
        String physicalActivityWithSpacesAsJson = JSONObject.quote(nameWithSpaces);

        PhysicalActivity deSerializedPhysicalActivity = objectMapper.readValue(physicalActivityWithSpacesAsJson, PhysicalActivity.class);

        assertEquals(physicalActivity, deSerializedPhysicalActivity);
    }

    private static Stream<Arguments> expectedNamesWithSpacesForPhysicalActivity() {
        return Stream.of(Arguments.of(PhysicalActivity.LOW, " LOW   "),
                         Arguments.of(PhysicalActivity.MEDIUM, "MEDIUM "),
                         Arguments.of(PhysicalActivity.HIGH, "HIGH "));
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidPhysicalActivityAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidPhysicalActivityAsJson, PhysicalActivity.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }
}