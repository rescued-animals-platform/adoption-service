package ec.animal.adoption.domain.characteristics;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import ec.animal.adoption.builders.CharacteristicsBuilder;
import ec.animal.adoption.builders.TemperamentsBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.Set;

import static ec.animal.adoption.TestUtils.getValidator;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CharacteristicsTest {

    private static final String SIZE_IS_REQUIRED = "Size is required";
    private static final String PHYSICAL_ACTIVITY_IS_REQUIRED = "Physical activity is required";
    private static final String AT_LEAST_ONE_TEMPERAMENT_IS_REQUIRED = "At least one temperaments is required";

    @Test
    public void shouldEliminateDuplicatesOnFriendlyWith() {
        CharacteristicsBuilder characteristicsBuilder = CharacteristicsBuilder.random();
        Characteristics expectedCharacteristics = characteristicsBuilder.withFriendlyWith(
                FriendlyWith.CATS, FriendlyWith.CHILDREN
        ).build();

        Characteristics characteristics = characteristicsBuilder.withFriendlyWith(
                FriendlyWith.CATS, FriendlyWith.CATS, FriendlyWith.CHILDREN
        ).build();

        assertThat(characteristics, is(expectedCharacteristics));
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Characteristics.class).usingGetClass().suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        ObjectMapper objectMapper = TestUtils.getObjectMapper();
        Characteristics characteristics = CharacteristicsBuilder.random().build();

        String serializedCharacteristics = objectMapper.writeValueAsString(characteristics);
        Characteristics deserializedCharacteristics = objectMapper.readValue(
                serializedCharacteristics, Characteristics.class
        );

        assertThat(deserializedCharacteristics, is(characteristics));
    }

    @Test
    public void shouldValidateNonNullSize() {
        Characteristics characteristics = CharacteristicsBuilder.random().withSize(null).build();

        Set<ConstraintViolation<Characteristics>> constraintViolations = getValidator().validate(characteristics);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Characteristics> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(SIZE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("size"));
    }

    @Test
    public void shouldValidateNonNullPhysicalActivity() {
        Characteristics characteristics = CharacteristicsBuilder.random().withPhysicalActivity(null).build();

        Set<ConstraintViolation<Characteristics>> constraintViolations = getValidator().validate(characteristics);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Characteristics> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(PHYSICAL_ACTIVITY_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("physicalActivity"));
    }

    @Test
    public void shouldValidateNonNullTemperaments() {
        Characteristics characteristics = CharacteristicsBuilder.random().withTemperaments(null).build();
        Set<ConstraintViolation<Characteristics>> constraintViolations = getValidator().validate(characteristics);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Characteristics> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(AT_LEAST_ONE_TEMPERAMENT_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("temperaments"));
    }

    @Test
    public void shouldValidateNonEmptyTemperaments() {
        Characteristics characteristics = CharacteristicsBuilder.random().
                withTemperaments(TemperamentsBuilder.empty().build()).build();

        Set<ConstraintViolation<Characteristics>> constraintViolations = getValidator().validate(characteristics);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Characteristics> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(AT_LEAST_ONE_TEMPERAMENT_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("temperaments"));
    }
}