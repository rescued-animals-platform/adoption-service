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

class SpeciesTest {

    public static final String EXPECTED_NAMES_FOR_SPECIES_METHOD = "expectedNamesForSpecies";

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} {0} name is \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_SPECIES_METHOD)
    void shouldReturnExpectedNameForSpecies(final Species species, final String expectedName) {
        assertEquals(expectedName, species.getName());
    }

    @ParameterizedTest(name = "{index} {0} is serialized as \"{1}\"")
    @MethodSource(EXPECTED_NAMES_FOR_SPECIES_METHOD)
    void shouldSerializeSpeciesUsingName(final Species species, final String expectedName) throws JsonProcessingException {
        String speciesAsJson = objectMapper.writeValueAsString(species);

        assertEquals(JSONObject.quote(expectedName), speciesAsJson);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{1}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_SPECIES_METHOD)
    void shouldDeserializeSpeciesUsingName(final Species species, final String expectedName) throws JsonProcessingException {
        String speciesWithNameAsJson = JSONObject.quote(expectedName);

        Species deSerializedSpecies = objectMapper.readValue(speciesWithNameAsJson, Species.class);

        assertEquals(species, deSerializedSpecies);
    }

    @ParameterizedTest(name = "{index} {0} is de-serialized from \"{0}\" value")
    @MethodSource(EXPECTED_NAMES_FOR_SPECIES_METHOD)
    void shouldDeserializeSpeciesUsingEnumName(final Species species) throws JsonProcessingException {
        String speciesWithEnumNameAsJson = JSONObject.quote(species.name());

        Species deSerializedSpecies = objectMapper.readValue(speciesWithEnumNameAsJson, Species.class);

        assertEquals(species, deSerializedSpecies);
    }

    @Test
    void shouldThrowValueInstantiationExceptionCausedByIllegalArgumentExceptionWhenCanNotDeserializeFromValue() {
        String invalidSpeciesAsJson = JSONObject.quote(randomAlphabetic(10));

        ValueInstantiationException exception = assertThrows(ValueInstantiationException.class, () -> {
            objectMapper.readValue(invalidSpeciesAsJson, Species.class);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Stream<Arguments> expectedNamesForSpecies() {
        return Stream.of(
                Arguments.of(Species.DOG, "Dog"),
                Arguments.of(Species.CAT, "Cat")
        );
    }
}