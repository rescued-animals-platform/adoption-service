package ec.animal.adoption.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.Unavailable;
import org.hibernate.validator.HibernateValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class AnimalTest {

    private static final String ANIMAL_NAME_IS_REQUIRED = "Animal name is required";
    private static final String ANIMAL_UUID_IS_REQUIRED = "Animal uuid is required";
    private static final String ANIMAL_TYPE_IS_REQUIRED = "Animal type is required";
    private static final String ANIMAL_STATE_IS_REQUIRED = "Animal state is required";
    private static final String ANIMAL_REGISTRATION_DATE_IS_REQUIRED = "Animal registration date is required";
    private static final String ANIMAL_ESTIMATED_AGE_IS_REQUIRED = "Animal estimated age is required";

    private LocalDateTime registrationDate;
    private String uuid;
    private String name;
    private Type type;
    private EstimatedAge estimatedAge;
    private Animal animal;

    @Before
    public void setUp() {
        registrationDate = LocalDateTime.now();
        uuid = randomAlphabetic(10);
        name = randomAlphabetic(10);
        type = Type.DOG;
        estimatedAge = EstimatedAge.YOUNG_ADULT;
        animal = new Animal(uuid, name, registrationDate, type, estimatedAge);
    }

    @Test
    public void shouldCreateAnimalWithDefaultStateLookingForHuman() {
        assertThat(animal.getState(), is(new LookingForHuman(registrationDate)));
    }

    @Test
    public void shouldCreateAnimalWithState() {
        Unavailable unavailable = new Unavailable(randomAlphabetic(10));
        Animal animal = new Animal(uuid, name, registrationDate, type, estimatedAge, unavailable);

        assertThat(animal.getState(), is(unavailable));
    }

    @Test
    public void shouldBeEqualToAnAvailableAnimalWithSameValues() {
        Animal sameAnimal = new Animal(uuid, name, registrationDate, type, estimatedAge);

        assertEquals(animal, sameAnimal);
    }

    @Test
    public void shouldNotBeEqualToAnAvailableAnimalWithDifferentValues() {
        Animal differentAnimal = new Animal(
                randomAlphabetic(10),
                randomAlphabetic(10),
                LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault())),
                Type.CAT,
                EstimatedAge.YOUNG
        );

        assertNotEquals(animal, differentAnimal);
    }

    @Test
    public void shouldNotBeEqualToAnotherObject() {
        assertNotEquals(animal, new Object());
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertNotEquals(animal, null);
    }

    @Test
    public void shouldBeEqualToItself() {
        assertEquals(animal, animal);
    }

    @Test
    public void shouldHaveSameHashCodeWhenHavingSameValues() {
        Animal sameAnimal = new Animal(uuid, name, registrationDate, type, estimatedAge);

        assertEquals(animal.hashCode(), sameAnimal.hashCode());
    }

    @Test
    public void shouldHaveDifferentHashCodeWhenHavingDifferentValues() {
        Animal differentAnimal = new Animal(
                randomAlphabetic(10),
                randomAlphabetic(10),
                LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault())),
                type,
                EstimatedAge.SENIOR_ADULT
        );

        assertNotEquals(animal.hashCode(), differentAnimal.hashCode());
    }

    @Test
    public void shouldBeSerializableAndDeserializableWithDefaultLookingForHumanState() throws IOException {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        String serializedAnimalForAdoption = objectMapper.writeValueAsString(animal);
        Animal deserializedAnimalForAdoption = objectMapper.readValue(
                serializedAnimalForAdoption, Animal.class
        );

        assertThat(deserializedAnimalForAdoption, is(animal));
    }

    @Test
    public void shouldBeSerializableAndDeserializableWithAdoptedState() throws IOException {
        animal.setState(new Adopted(LocalDate.now(), randomAlphabetic(10)));
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        String serializedAdoptedAnimal = objectMapper.writeValueAsString(animal);
        System.err.println(serializedAdoptedAnimal);
        Animal deserializedAdoptedAnimal = objectMapper.readValue(
                serializedAdoptedAnimal, Animal.class
        );

        assertThat(deserializedAdoptedAnimal, is(animal));
    }

    @Test
    public void shouldBeSerializableAndDeserializableWithUnavailableState() throws IOException {
        animal.setState(new Unavailable(randomAlphabetic(10)));
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        String serializedUnavailableAnimal = objectMapper.writeValueAsString(animal);
        Animal deserializedUnavailableAnimal = objectMapper.readValue(
                serializedUnavailableAnimal, Animal.class
        );

        assertThat(deserializedUnavailableAnimal, is(animal));
    }

    @Test
    public void shouldValidateNonNullUuid() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal(null, name, registrationDate, type, estimatedAge);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_UUID_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("uuid"));
    }

    @Test
    public void shouldValidateNonEmptyUuid() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal("", name, registrationDate, type, estimatedAge);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_UUID_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("uuid"));
    }

    @Test
    public void shouldValidateNonNullName() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal(uuid, null, registrationDate, type, estimatedAge);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_NAME_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("name"));
    }

    @Test
    public void shouldValidateNonEmptyName() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal(uuid, "", registrationDate, type, estimatedAge);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_NAME_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("name"));
    }

    @Test
    public void shouldValidateNonNullType() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal(uuid, name, registrationDate, null, estimatedAge);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_TYPE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("type"));
    }

    @Test
    public void shouldValidateNonNullState() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal(uuid, name, registrationDate, type, estimatedAge);
        animal.setState(null);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_STATE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("state"));
    }

    @Test
    public void shouldValidateNonNullRegistrationDate() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal(uuid, name, null, type, estimatedAge);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_REGISTRATION_DATE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("registrationDate"));
    }

    @Test
    public void shouldValidateNonNullEstimatedAge() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal(uuid, name, registrationDate, type, null);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_ESTIMATED_AGE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("estimatedAge"));
    }

    private static LocalValidatorFactoryBean getLocalValidatorFactoryBean() {
        LocalValidatorFactoryBean localValidatorFactory = new LocalValidatorFactoryBean();
        localValidatorFactory.setProviderClass(HibernateValidator.class);
        localValidatorFactory.afterPropertiesSet();
        return localValidatorFactory;
    }
}