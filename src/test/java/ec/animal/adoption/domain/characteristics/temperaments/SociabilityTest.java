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

class SociabilityTest {

    public static final String EXPECTED_NAMES_FOR_SOCIABILITY_METHOD = "expectedNamesForSociability";

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} name is \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_SOCIABILITY_METHOD)
    void shouldReturnExpectedNameForSociability(final Sociability sociability, final String expectedName) {
        assertEquals(expectedName, sociability.getName());
    }

    @ParameterizedTest(name = "{index} {0} is serialized as \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_SOCIABILITY_METHOD)
    void shouldSerializeSociabilityUsingName(final Sociability sociability, final String expectedName) throws JsonProcessingException {
        String sociabilityAsJson = objectMapper.writeValueAsString(sociability);

        assertEquals(JSONObject.quote(expectedName), sociabilityAsJson);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_SOCIABILITY_METHOD)
    void shouldDeserializeSociabilityUsingName(final Sociability sociability, final String expectedName) throws JsonProcessingException {
        String sociabilityWithNameAsJson = JSONObject.quote(expectedName);

        Sociability deSerializedSociability = objectMapper.readValue(sociabilityWithNameAsJson, Sociability.class);

        assertEquals(sociability, deSerializedSociability);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_SOCIABILITY_METHOD)
    void shouldDeserializeSociabilityUsingEnumName(final Sociability sociability) throws JsonProcessingException {
        String sociabilityWithEnumNameAsJson = JSONObject.quote(sociability.name());

        Sociability deSerializedSociability = objectMapper.readValue(sociabilityWithEnumNameAsJson, Sociability.class);

        assertEquals(sociability, deSerializedSociability);
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidSociabilityAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidSociabilityAsJson, Sociability.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedNamesForSociability() {
        return Stream.of(
                Arguments.of(Sociability.VERY_SOCIABLE, "Very sociable"),
                Arguments.of(Sociability.SOCIABLE, "Sociable"),
                Arguments.of(Sociability.NEITHER_SOCIABLE_NOR_SHY, "Neither sociable nor shy"),
                Arguments.of(Sociability.SHY, "Shy"),
                Arguments.of(Sociability.VERY_SHY, "Very shy")
        );
    }
}