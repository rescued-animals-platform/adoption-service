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
import static org.junit.jupiter.api.Assertions.*;

class PictureTypeTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource("pictureTypes")
    void shouldDeserializePictureTypeUsingEnumName(final PictureType pictureType) throws JsonProcessingException {
        String pictureTypeWithEnumNameAsJson = JSONObject.quote(pictureType.name());

        PictureType deSerializedPictureType = objectMapper.readValue(pictureTypeWithEnumNameAsJson, PictureType.class);

        assertEquals(pictureType, deSerializedPictureType);
    }

    private static Stream<Arguments> pictureTypes() {
        return Stream.of(Arguments.of(PictureType.PRIMARY), Arguments.of(PictureType.ALTERNATE));
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource("expectedNamesWithSpacesForPictureType")
    void shouldTrimSpacesInValueBeforeDeSerializing(final PictureType pictureType, final String nameWithSpaces) throws JsonProcessingException {
        String pictureTypeWithSpacesAsJson = JSONObject.quote(nameWithSpaces);

        PictureType deSerializedPictureType = objectMapper.readValue(pictureTypeWithSpacesAsJson, PictureType.class);

        assertEquals(pictureType, deSerializedPictureType);
    }

    private static Stream<Arguments> expectedNamesWithSpacesForPictureType() {
        return Stream.of(Arguments.of(PictureType.PRIMARY, " PRIMARY   "),
                         Arguments.of(PictureType.ALTERNATE, "ALTERNATE "));
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