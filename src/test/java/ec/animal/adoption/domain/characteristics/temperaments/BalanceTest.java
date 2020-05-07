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

class BalanceTest {

    public static final String EXPECTED_NAMES_FOR_BALANCE_METHOD = "expectedNamesForBalance";

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} name is \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_BALANCE_METHOD)
    void shouldReturnExpectedNameForBalance(final Balance balance, final String expectedName) {
        assertEquals(expectedName, balance.toString());
    }

    @ParameterizedTest(name = "{index} {0} is serialized as \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_BALANCE_METHOD)
    void shouldSerializeBalanceUsingName(final Balance balance, final String expectedName) throws JsonProcessingException {
        String balanceAsJson = objectMapper.writeValueAsString(balance);

        assertEquals(JSONObject.quote(expectedName), balanceAsJson);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_BALANCE_METHOD)
    void shouldDeserializeBalanceUsingName(final Balance balance, final String expectedName) throws JsonProcessingException {
        String balanceWithNameAsJson = JSONObject.quote(expectedName);

        Balance deSerializedBalance = objectMapper.readValue(balanceWithNameAsJson, Balance.class);

        assertEquals(balance, deSerializedBalance);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_BALANCE_METHOD)
    void shouldDeserializeBalanceUsingEnumName(final Balance balance) throws JsonProcessingException {
        String balanceWithEnumNameAsJson = JSONObject.quote(balance.name());

        Balance deSerializedBalance = objectMapper.readValue(balanceWithEnumNameAsJson, Balance.class);

        assertEquals(balance, deSerializedBalance);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedNamesForBalance() {
        return Stream.of(
                Arguments.of(Balance.VERY_BALANCED, "Very balanced"),
                Arguments.of(Balance.BALANCED, "Balanced"),
                Arguments.of(Balance.NEITHER_BALANCED_NOR_POSSESSIVE, "Neither balanced nor possessive"),
                Arguments.of(Balance.POSSESSIVE, "Possessive"),
                Arguments.of(Balance.VERY_POSSESSIVE, "Very possessive")
        );
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource("expectedNamesWithSpacesForBalance")
    void shouldTrimSpacesInValueBeforeDeSerializing(final Balance balance, final String nameWithSpaces) throws JsonProcessingException {
        String balanceWithSpacesAsJson = JSONObject.quote(nameWithSpaces);

        Balance deSerializedBalance = objectMapper.readValue(balanceWithSpacesAsJson, Balance.class);

        assertEquals(balance, deSerializedBalance);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedNamesWithSpacesForBalance() {
        return Stream.of(
                Arguments.of(Balance.VERY_BALANCED, " Very balanced "),
                Arguments.of(Balance.VERY_BALANCED, " VERY_BALANCED   "),
                Arguments.of(Balance.BALANCED, "   Balanced"),
                Arguments.of(Balance.BALANCED, "BALANCED "),
                Arguments.of(Balance.NEITHER_BALANCED_NOR_POSSESSIVE, "Neither balanced nor possessive "),
                Arguments.of(Balance.NEITHER_BALANCED_NOR_POSSESSIVE, "    NEITHER_BALANCED_NOR_POSSESSIVE "),
                Arguments.of(Balance.POSSESSIVE, " Possessive "),
                Arguments.of(Balance.POSSESSIVE, " POSSESSIVE "),
                Arguments.of(Balance.VERY_POSSESSIVE, "     Very possessive "),
                Arguments.of(Balance.VERY_POSSESSIVE, " VERY_POSSESSIVE      ")
        );
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidBalanceAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidBalanceAsJson, Balance.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }
}