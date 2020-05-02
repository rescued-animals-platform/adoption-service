package ec.animal.adoption.domain.animal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EstimatedAgeTest {

    public static final String EXPECTED_NAMES_FOR_ESTIMATED_AGE_METHOD = "expectedNamesForEstimatedAge";

    @ParameterizedTest(name = "{index} {0} name is \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_ESTIMATED_AGE_METHOD)
    void shouldReturnExpectedNameForEstimatedAge(final EstimatedAge estimatedAge, final String expectedName) {
        assertEquals(expectedName, estimatedAge.getName());
    }

    @ParameterizedTest(name = "{index} {0} is serialized as \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_ESTIMATED_AGE_METHOD)
    void shouldSerializeEstimatedAgeUsingName(final EstimatedAge estimatedAge, final String expectedName) throws JsonProcessingException {
        ObjectMapper objectMapper = TestUtils.getObjectMapper();

        String estimatedAgeAsJson = objectMapper.writeValueAsString(estimatedAge);

        assertEquals(String.format("\"%s\"", expectedName), estimatedAgeAsJson);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_ESTIMATED_AGE_METHOD)
    void shouldDeserializeEstimatedAgeUsingName(final EstimatedAge estimatedAge, final String expectedName) throws JsonProcessingException {
        String estimatedAgeWithNameAsJson = String.format("\"%s\"", expectedName);
        ObjectMapper objectMapper = TestUtils.getObjectMapper();

        EstimatedAge deSerializedEstimatedAge = objectMapper.readValue(estimatedAgeWithNameAsJson, EstimatedAge.class);

        assertEquals(estimatedAge, deSerializedEstimatedAge);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_ESTIMATED_AGE_METHOD)
    void shouldDeserializeEstimatedAgeUsingEnumName(final EstimatedAge estimatedAge) throws JsonProcessingException {
        String estimatedAgeWithEnumNameAsJson = String.format("\"%s\"", estimatedAge.name());
        ObjectMapper objectMapper = TestUtils.getObjectMapper();

        EstimatedAge deSerializedEstimatedAge = objectMapper.readValue(estimatedAgeWithEnumNameAsJson, EstimatedAge.class);

        assertEquals(estimatedAge, deSerializedEstimatedAge);
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