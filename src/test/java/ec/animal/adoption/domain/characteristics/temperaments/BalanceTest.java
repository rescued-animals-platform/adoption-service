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
        assertEquals(expectedName, balance.getName());
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

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidBalanceAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidBalanceAsJson, Balance.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
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
}