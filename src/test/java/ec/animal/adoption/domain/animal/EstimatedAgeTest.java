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

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstimatedAgeTest {

    public static final String EXPECTED_NAMES_FOR_ESTIMATED_AGE_METHOD = "expectedNamesForEstimatedAge";

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} name is \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_ESTIMATED_AGE_METHOD)
    void shouldReturnExpectedNameForEstimatedAge(final EstimatedAge estimatedAge, final String expectedName) {
        assertEquals(expectedName, estimatedAge.getName());
    }

    @ParameterizedTest(name = "{index} {0} is serialized as \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_ESTIMATED_AGE_METHOD)
    void shouldSerializeEstimatedAgeUsingName(final EstimatedAge estimatedAge, final String expectedName) throws JsonProcessingException {
        String estimatedAgeAsJson = objectMapper.writeValueAsString(estimatedAge);

        assertEquals(JSONObject.quote(expectedName), estimatedAgeAsJson);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_ESTIMATED_AGE_METHOD)
    void shouldDeserializeEstimatedAgeUsingName(final EstimatedAge estimatedAge, final String expectedName) throws JsonProcessingException {
        String estimatedAgeWithNameAsJson = JSONObject.quote(expectedName);

        EstimatedAge deSerializedEstimatedAge = objectMapper.readValue(estimatedAgeWithNameAsJson, EstimatedAge.class);

        assertEquals(estimatedAge, deSerializedEstimatedAge);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_ESTIMATED_AGE_METHOD)
    void shouldDeserializeEstimatedAgeUsingEnumName(final EstimatedAge estimatedAge) throws JsonProcessingException {
        String estimatedAgeWithEnumNameAsJson = JSONObject.quote(estimatedAge.name());

        EstimatedAge deSerializedEstimatedAge = objectMapper.readValue(estimatedAgeWithEnumNameAsJson, EstimatedAge.class);

        assertEquals(estimatedAge, deSerializedEstimatedAge);
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidEstimatedAgeAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidEstimatedAgeAsJson, EstimatedAge.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedNamesForEstimatedAge() {
        return Stream.of(
                Arguments.of(EstimatedAge.YOUNG, "Young"),
                Arguments.of(EstimatedAge.YOUNG_ADULT, "Young adult"),
                Arguments.of(EstimatedAge.SENIOR_ADULT, "Senior adult")
        );
    }
}