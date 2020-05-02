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

class SizeTest {

    public static final String EXPECTED_NAMES_FOR_SIZE_METHOD = "expectedNamesForSize";

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} name is \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_SIZE_METHOD)
    void shouldReturnExpectedNameForSize(final Size size, final String expectedName) {
        assertEquals(expectedName, size.getName());
    }

    @ParameterizedTest(name = "{index} {0} is serialized as \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_SIZE_METHOD)
    void shouldSerializeSizeUsingName(final Size size, final String expectedName) throws JsonProcessingException {
        String sizeAsJson = objectMapper.writeValueAsString(size);

        assertEquals(JSONObject.quote(expectedName), sizeAsJson);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_SIZE_METHOD)
    void shouldDeserializeSizeUsingName(final Size size, final String expectedName) throws JsonProcessingException {
        String sizeWithNameAsJson = JSONObject.quote(expectedName);

        Size deSerializedSize = objectMapper.readValue(sizeWithNameAsJson, Size.class);

        assertEquals(size, deSerializedSize);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_SIZE_METHOD)
    void shouldDeserializeSizeUsingEnumName(final Size size) throws JsonProcessingException {
        String sizeWithEnumNameAsJson = JSONObject.quote(size.name());

        Size deSerializedSize = objectMapper.readValue(sizeWithEnumNameAsJson, Size.class);

        assertEquals(size, deSerializedSize);
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidSizeAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidSizeAsJson, Size.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedNamesForSize() {
        return Stream.of(
                Arguments.of(Size.TINY, "Tiny"),
                Arguments.of(Size.SMALL, "Small"),
                Arguments.of(Size.MEDIUM, "Medium"),
                Arguments.of(Size.BIG, "Big"),
                Arguments.of(Size.OUTSIZE, "Outsize")
        );
    }
}