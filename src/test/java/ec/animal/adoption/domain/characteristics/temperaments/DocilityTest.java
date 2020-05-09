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
import ec.animal.adoption.domain.utils.TranslatorUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.stream.Stream;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DocilityTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setUseCodeAsDefaultMessage(true);
        ReflectionTestUtils.setField(TranslatorUtils.class, "messageSource", messageSource);
    }

    @ParameterizedTest(name = "{index} {0} name is \"{1}\"")
    @MethodSource("expectedTranslatedNameForDocility")
    void shouldReturnExpectedNameForDocility(final Docility docility, final String expectedTranslatedName) {
        assertEquals(expectedTranslatedName, docility.toTranslatedName());
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedTranslatedNameForDocility() {
        return Stream.of(Arguments.of(Docility.VERY_DOCILE, "DOCILITY.VERY_DOCILE"),
                         Arguments.of(Docility.DOCILE, "DOCILITY.DOCILE"),
                         Arguments.of(Docility.NEITHER_DOCILE_NOR_DOMINANT, "DOCILITY.NEITHER_DOCILE_NOR_DOMINANT"),
                         Arguments.of(Docility.DOMINANT, "DOCILITY.DOMINANT"),
                         Arguments.of(Docility.VERY_DOMINANT, "DOCILITY.VERY_DOMINANT"));
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource("docilityValues")
    void shouldDeserializeDocilityUsingEnumName(final Docility docility) throws JsonProcessingException {
        String docilityWithEnumNameAsJson = JSONObject.quote(docility.name());

        Docility deSerializedDocility = objectMapper.readValue(docilityWithEnumNameAsJson, Docility.class);

        assertEquals(docility, deSerializedDocility);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
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

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
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