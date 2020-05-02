package ec.animal.adoption.domain.animal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import ec.animal.adoption.TestUtils;
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

class SexTest {

    public static final String EXPECTED_NAMES_FOR_SEX_METHOD = "expectedNamesForSex";

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} name is \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_SEX_METHOD)
    void shouldReturnExpectedNameForSex(final Sex sex, final String expectedName) {
        assertEquals(expectedName, sex.getName());
    }

    @ParameterizedTest(name = "{index} {0} is serialized as \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_SEX_METHOD)
    void shouldSerializeSexUsingName(final Sex sex, final String expectedName) throws JsonProcessingException {
        String sexAsJson = objectMapper.writeValueAsString(sex);

        assertEquals(String.format("\"%s\"", expectedName), sexAsJson);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_SEX_METHOD)
    void shouldDeserializeSexUsingName(final Sex sex, final String expectedName) throws JsonProcessingException {
        String sexWithNameAsJson = String.format("\"%s\"", expectedName);

        Sex deSerializedSex = objectMapper.readValue(sexWithNameAsJson, Sex.class);

        assertEquals(sex, deSerializedSex);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_SEX_METHOD)
    void shouldDeserializeSexUsingEnumName(final Sex sex) throws JsonProcessingException {
        String sexWithEnumNameAsJson = String.format("\"%s\"", sex.name());

        Sex deSerializedSex = objectMapper.readValue(sexWithEnumNameAsJson, Sex.class);

        assertEquals(sex, deSerializedSex);
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidSexAsJson = String.format("\"%s\"", randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidSexAsJson, Sex.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedNamesForSex() {
        return Stream.of(
                Arguments.of(Sex.MALE, "Male"),
                Arguments.of(Sex.FEMALE, "Female")
        );
    }
}