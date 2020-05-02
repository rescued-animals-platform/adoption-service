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

    public static final String EXPECTED_NAMES_FOR_FRIENDLY_WITH_METHOD = "expectedNamesForFriendlyWith";

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} name is \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_FRIENDLY_WITH_METHOD)
    void shouldReturnExpectedNameForFriendlyWith(final FriendlyWith friendlyWith, final String expectedName) {
        assertEquals(expectedName, friendlyWith.getName());
    }

    @ParameterizedTest(name = "{index} {0} is serialized as \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_FRIENDLY_WITH_METHOD)
    void shouldSerializeFriendlyWithUsingName(final FriendlyWith friendlyWith, final String expectedName) throws JsonProcessingException {
        String friendlyWithAsJson = objectMapper.writeValueAsString(friendlyWith);

        assertEquals(JSONObject.quote(expectedName), friendlyWithAsJson);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_FRIENDLY_WITH_METHOD)
    void shouldDeserializeFriendlyWithUsingName(final FriendlyWith friendlyWith, final String expectedName) throws JsonProcessingException {
        String friendlyWithWithNameAsJson = JSONObject.quote(expectedName);

        FriendlyWith deSerializedFriendlyWith = objectMapper.readValue(friendlyWithWithNameAsJson, FriendlyWith.class);

        assertEquals(friendlyWith, deSerializedFriendlyWith);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_FRIENDLY_WITH_METHOD)
    void shouldDeserializeFriendlyWithUsingEnumName(final FriendlyWith friendlyWith) throws JsonProcessingException {
        String friendlyWithWithEnumNameAsJson = JSONObject.quote(friendlyWith.name());

        FriendlyWith deSerializedFriendlyWith = objectMapper.readValue(friendlyWithWithEnumNameAsJson, FriendlyWith.class);

        assertEquals(friendlyWith, deSerializedFriendlyWith);
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidFriendlyWithAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidFriendlyWithAsJson, FriendlyWith.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedNamesForFriendlyWith() {
        return Stream.of(
                Arguments.of(FriendlyWith.ADULTS, "Adults"),
                Arguments.of(FriendlyWith.CATS, "Cats"),
                Arguments.of(FriendlyWith.CHILDREN, "Children"),
                Arguments.of(FriendlyWith.DOGS, "Dogs"),
                Arguments.of(FriendlyWith.OTHER_ANIMALS, "Other animals")
        );
    }
}