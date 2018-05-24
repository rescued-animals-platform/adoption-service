package ec.animal.adoption.domain.characteristics;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.characteristics.temperament.Docility;
import ec.animal.adoption.domain.characteristics.temperament.Sociability;
import ec.animal.adoption.domain.characteristics.temperament.Temperament;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.hibernate.validator.HibernateValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class CharacteristicsTest {

    private static final String SIZE_IS_REQUIRED = "Size is required";
    private static final String PHYSICAL_ACTIVITY_IS_REQUIRED = "Physical activity is required";
    private static final String AT_LEAST_ONE_TEMPERAMENT_IS_REQUIRED = "At least one temperament is required";
    private static final String TEMPERAMENTS_ARE_INVALID = "Temperaments are invalid";

    private Temperament temperament;
    private Size size;
    private PhysicalActivity physicalActivity;
    private List<Temperament> temperaments;

    @Before
    public void setUp() {
        size = TestUtils.getRandomSize();
        physicalActivity = TestUtils.getRandomPhysicalActivity();
        temperament = TestUtils.getRandomTemperament();
        temperaments = newArrayList(temperament);
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
    public void shouldEliminateDuplicatesOnTemperaments() {
        Temperament anotherTemperament = mock(Temperament.class);
        List<Temperament> temperaments = newArrayList(temperament, anotherTemperament, anotherTemperament);
        Characteristics expectedCharacteristics = new Characteristics(
                size, physicalActivity, newArrayList(temperament, anotherTemperament)
        );

        Characteristics characteristics = new Characteristics(size, physicalActivity, temperaments);

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
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Characteristics characteristics = new Characteristics(
                null, physicalActivity, temperaments, TestUtils.getRandomFriendlyWith()
        );

        Set<ConstraintViolation<Characteristics>> constraintViolations = localValidatorFactory.validate(characteristics);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Characteristics> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(SIZE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("size"));
    }

    @Test
    public void shouldValidateNonNullPhysicalActivity() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Characteristics characteristics = new Characteristics(
                size, null, temperaments, TestUtils.getRandomFriendlyWith()
        );

        Set<ConstraintViolation<Characteristics>> constraintViolations = localValidatorFactory.validate(characteristics);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Characteristics> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(PHYSICAL_ACTIVITY_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("physicalActivity"));
    }

    @Test
    public void shouldValidateNonNullTemperaments() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Characteristics characteristics = new Characteristics(
                size, physicalActivity, null, TestUtils.getRandomFriendlyWith()
        );

        Set<ConstraintViolation<Characteristics>> constraintViolations = localValidatorFactory.validate(characteristics);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Characteristics> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(AT_LEAST_ONE_TEMPERAMENT_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("temperaments"));
    }

    @Test
    public void shouldValidateNonEmptyTemperaments() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Characteristics characteristics = new Characteristics(
                size, physicalActivity, Collections.emptyList(), TestUtils.getRandomFriendlyWith()
        );

        Set<ConstraintViolation<Characteristics>> constraintViolations = localValidatorFactory.validate(characteristics);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Characteristics> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(AT_LEAST_ONE_TEMPERAMENT_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("temperaments"));
    }

    @Test
    public void shouldValidateTemperamentsDoNotHaveMoreThanOneTemperamentInstanceOfTheSameType() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        List<Temperament> invalidTemperaments = Arrays.asList(Sociability.SHY, Sociability.SOCIABLE, Docility.DOCILE);
        Characteristics characteristics = new Characteristics(
                size, physicalActivity, invalidTemperaments, TestUtils.getRandomFriendlyWith()
        );

        Set<ConstraintViolation<Characteristics>> constraintViolations = localValidatorFactory.validate(characteristics);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Characteristics> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(TEMPERAMENTS_ARE_INVALID));
        assertThat(constraintViolation.getPropertyPath().toString(), is("temperaments"));    }

    private static LocalValidatorFactoryBean getLocalValidatorFactoryBean() {
        LocalValidatorFactoryBean localValidatorFactory = new LocalValidatorFactoryBean();
        localValidatorFactory.setProviderClass(HibernateValidator.class);
        localValidatorFactory.afterPropertiesSet();
        return localValidatorFactory;
    }
}