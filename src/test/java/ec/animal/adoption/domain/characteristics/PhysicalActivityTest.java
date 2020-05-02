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

class PhysicalActivityTest {

    public static final String EXPECTED_NAMES_FOR_PHYSICAL_ACTIVITY_METHOD = "expectedNamesForPhysicalActivity";

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} name is \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_PHYSICAL_ACTIVITY_METHOD)
    void shouldReturnExpectedNameForPhysicalActivity(final PhysicalActivity physicalActivity, final String expectedName) {
        assertEquals(expectedName, physicalActivity.getName());
    }

    @ParameterizedTest(name = "{index} {0} is serialized as \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_PHYSICAL_ACTIVITY_METHOD)
    void shouldSerializePhysicalActivityUsingName(final PhysicalActivity physicalActivity, final String expectedName) throws JsonProcessingException {
        String physicalActivityAsJson = objectMapper.writeValueAsString(physicalActivity);

        assertEquals(JSONObject.quote(expectedName), physicalActivityAsJson);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_PHYSICAL_ACTIVITY_METHOD)
    void shouldDeserializePhysicalActivityUsingName(final PhysicalActivity physicalActivity, final String expectedName) throws JsonProcessingException {
        String physicalActivityWithNameAsJson = JSONObject.quote(expectedName);

        PhysicalActivity deSerializedPhysicalActivity = objectMapper.readValue(physicalActivityWithNameAsJson, PhysicalActivity.class);

        assertEquals(physicalActivity, deSerializedPhysicalActivity);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_PHYSICAL_ACTIVITY_METHOD)
    void shouldDeserializePhysicalActivityUsingEnumName(final PhysicalActivity physicalActivity) throws JsonProcessingException {
        String physicalActivityWithEnumNameAsJson = JSONObject.quote(physicalActivity.name());

        PhysicalActivity deSerializedPhysicalActivity = objectMapper.readValue(physicalActivityWithEnumNameAsJson, PhysicalActivity.class);

        assertEquals(physicalActivity, deSerializedPhysicalActivity);
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeSerializeFromValue() {
        String invalidPhysicalActivityAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidPhysicalActivityAsJson, PhysicalActivity.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedNamesForPhysicalActivity() {
        return Stream.of(
                Arguments.of(PhysicalActivity.HIGH, "High"),
                Arguments.of(PhysicalActivity.MEDIUM, "Medium"),
                Arguments.of(PhysicalActivity.LOW, "Low")
        );
    }
}