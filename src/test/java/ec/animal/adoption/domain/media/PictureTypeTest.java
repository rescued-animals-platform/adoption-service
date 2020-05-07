package ec.animal.adoption.domain.media;

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

class PictureTypeTest {

    public static final String EXPECTED_NAMES_FOR_PICTURE_TYPE_METHOD = "expectedNamesForPictureType";

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} name is \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_PICTURE_TYPE_METHOD)
    void shouldReturnExpectedNameForPictureType(final PictureType pictureType, final String expectedName) {
        assertEquals(expectedName, pictureType.toString());
    }

    @ParameterizedTest(name = "{index} {0} is serialized as \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_PICTURE_TYPE_METHOD)
    void shouldSerializePictureTypeUsingName(final PictureType pictureType, final String expectedName) throws JsonProcessingException {
        String pictureTypeAsJson = objectMapper.writeValueAsString(pictureType);

        assertEquals(JSONObject.quote(expectedName), pictureTypeAsJson);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_PICTURE_TYPE_METHOD)
    void shouldDeserializePictureTypeUsingName(final PictureType pictureType, final String expectedName) throws JsonProcessingException {
        String pictureTypeWithNameAsJson = JSONObject.quote(expectedName);

        PictureType deSerializedPictureType = objectMapper.readValue(pictureTypeWithNameAsJson, PictureType.class);

        assertEquals(pictureType, deSerializedPictureType);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_PICTURE_TYPE_METHOD)
    void shouldDeserializePictureTypeUsingEnumName(final PictureType pictureType) throws JsonProcessingException {
        String pictureTypeWithEnumNameAsJson = JSONObject.quote(pictureType.name());

        PictureType deSerializedPictureType = objectMapper.readValue(pictureTypeWithEnumNameAsJson, PictureType.class);

        assertEquals(pictureType, deSerializedPictureType);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedNamesForPictureType() {
        return Stream.of(
                Arguments.of(PictureType.PRIMARY, "Primary"),
                Arguments.of(PictureType.ALTERNATE, "Alternate")
        );
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource("expectedNamesWithSpacesForPictureType")
    void shouldTrimSpacesInValueBeforeDeSerializing(final PictureType pictureType, final String nameWithSpaces) throws JsonProcessingException {
        String pictureTypeWithSpacesAsJson = JSONObject.quote(nameWithSpaces);

        PictureType deSerializedPictureType = objectMapper.readValue(pictureTypeWithSpacesAsJson, PictureType.class);

        assertEquals(pictureType, deSerializedPictureType);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedNamesWithSpacesForPictureType() {
        return Stream.of(
                Arguments.of(PictureType.PRIMARY, " Primary "),
                Arguments.of(PictureType.PRIMARY, " PRIMARY   "),
                Arguments.of(PictureType.ALTERNATE, "   Alternate"),
                Arguments.of(PictureType.ALTERNATE, "ALTERNATE ")
        );
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidPictureTypeAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidPictureTypeAsJson, PictureType.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }
}