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

class FriendlyWithTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource("friendlyWiths")
    void shouldDeserializeFriendlyWithUsingEnumName(final FriendlyWith friendlyWith) throws JsonProcessingException {
        String friendlyWithWithEnumNameAsJson = JSONObject.quote(friendlyWith.name());

        FriendlyWith deSerializedFriendlyWith = objectMapper.readValue(friendlyWithWithEnumNameAsJson, FriendlyWith.class);

        assertEquals(friendlyWith, deSerializedFriendlyWith);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> friendlyWiths() {
        return Stream.of(Arguments.of(FriendlyWith.ADULTS),
                         Arguments.of(FriendlyWith.CATS),
                         Arguments.of(FriendlyWith.CHILDREN),
                         Arguments.of(FriendlyWith.DOGS),
                         Arguments.of(FriendlyWith.OTHER_ANIMALS));
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource("expectedNamesWithSpacesForFriendlyWith")
    void shouldTrimSpacesInValueBeforeDeSerializing(final FriendlyWith friendlyWith, final String nameWithSpaces) throws JsonProcessingException {
        String friendlyWithWithSpacesAsJson = JSONObject.quote(nameWithSpaces);

        FriendlyWith deSerializedFriendlyWith = objectMapper.readValue(friendlyWithWithSpacesAsJson, FriendlyWith.class);

        assertEquals(friendlyWith, deSerializedFriendlyWith);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedNamesWithSpacesForFriendlyWith() {
        return Stream.of(Arguments.of(FriendlyWith.DOGS, " DOGS   "),
                         Arguments.of(FriendlyWith.CATS, "CATS "),
                         Arguments.of(FriendlyWith.OTHER_ANIMALS, "    OTHER_ANIMALS "),
                         Arguments.of(FriendlyWith.ADULTS, " ADULTS "),
                         Arguments.of(FriendlyWith.CHILDREN, " CHILDREN      "));
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidFriendlyWithAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidFriendlyWithAsJson, FriendlyWith.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }
}