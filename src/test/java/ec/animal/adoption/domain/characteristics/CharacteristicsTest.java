package ec.animal.adoption.domain.characteristics;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CharacteristicsTest {

    private static final String SIZE_IS_REQUIRED = "Size is required";
    private static final String PHYSICAL_ACTIVITY_IS_REQUIRED = "Physical activity is required";
    private static final String AT_LEAST_ONE_TEMPERAMENT_IS_REQUIRED = "At least one temperaments is required";
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private Size size;
    private PhysicalActivity physicalActivity;
    private Temperaments temperaments;

    @Before
    public void setUp() {
        size = TestUtils.getRandomSize();
        physicalActivity = TestUtils.getRandomPhysicalActivity();
        temperaments = new Temperaments(Sociability.SHY, Docility.DOCILE, Balance.VERY_POSSESSIVE);
    }

    @Test
    public void shouldEliminateDuplicatesOnFriendlyWith() {
        Characteristics expectedCharacteristics = new Characteristics(
                size, physicalActivity, temperaments, FriendlyWith.CATS, FriendlyWith.CHILDREN
        );

        Characteristics characteristics = new Characteristics(
                size, physicalActivity, temperaments, FriendlyWith.CATS, FriendlyWith.CATS, FriendlyWith.CHILDREN
        );

        assertThat(characteristics, is(expectedCharacteristics));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Characteristics.class).usingGetClass().suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        Characteristics characteristics = new Characteristics(
                size, physicalActivity, temperaments, FriendlyWith.CATS, FriendlyWith.CHILDREN
        );
        ObjectMapper objectMapper = new ObjectMapper();

        String serializedCharacteristics = objectMapper.writeValueAsString(characteristics);
        Characteristics deserializedCharacteristics = objectMapper.readValue(
                serializedCharacteristics, Characteristics.class
        );

        assertThat(deserializedCharacteristics, is(characteristics));
    }

    @Test
    public void shouldValidateNonNullSize() {
        Characteristics characteristics = new Characteristics(
                null, physicalActivity, temperaments, TestUtils.getRandomFriendlyWith()
        );

        Set<ConstraintViolation<Characteristics>> constraintViolations = VALIDATOR.validate(characteristics);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Characteristics> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(SIZE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("size"));
    }

    @Test
    public void shouldValidateNonNullPhysicalActivity() {
        Characteristics characteristics = new Characteristics(
                size, null, temperaments, TestUtils.getRandomFriendlyWith()
        );

        Set<ConstraintViolation<Characteristics>> constraintViolations = VALIDATOR.validate(characteristics);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Characteristics> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(PHYSICAL_ACTIVITY_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("physicalActivity"));
    }

    @Test
    public void shouldValidateNonNullTemperaments() {
        Characteristics characteristics = new Characteristics(
                size, physicalActivity, null, TestUtils.getRandomFriendlyWith()
        );

        Set<ConstraintViolation<Characteristics>> constraintViolations = VALIDATOR.validate(characteristics);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Characteristics> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(AT_LEAST_ONE_TEMPERAMENT_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("temperaments"));
    }

    @Test
    public void shouldValidateNonEmptyTemperaments() {
        Characteristics characteristics = new Characteristics(
                size,
                physicalActivity,
                new Temperaments(null, null, null),
                TestUtils.getRandomFriendlyWith()
        );

        Set<ConstraintViolation<Characteristics>> constraintViolations = VALIDATOR.validate(characteristics);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Characteristics> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(AT_LEAST_ONE_TEMPERAMENT_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("temperaments"));
    }
}