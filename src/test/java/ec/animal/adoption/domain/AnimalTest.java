package ec.animal.adoption.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.hibernate.validator.HibernateValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.is;
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
    private State state;
    private Animal animal;

    @Before
    public void setUp() {
        registrationDate = LocalDateTime.now();
        uuid = randomAlphabetic(10);
        name = randomAlphabetic(10);
        type = Type.DOG;
        estimatedAge = EstimatedAge.YOUNG_ADULT;
        state = getRandomState();
        animal = new Animal(uuid, name, registrationDate, type, estimatedAge, state);
    }

    @Test
    public void shouldCreateAnimalWithState() {
        assertThat(animal.getState(), is(state));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Animal.class).usingGetClass().verify();
    }

    @Test
    public void shouldBeSerializableAndDeserializableWithLookingForHumanState() throws IOException {
        State lookingForHumanState = new LookingForHuman(registrationDate);
        animal = new Animal(uuid, name, registrationDate, type, estimatedAge, lookingForHumanState);
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        String serializedAnimalForAdoption = objectMapper.writeValueAsString(animal);
        System.err.println(serializedAnimalForAdoption);
        Animal deserializedAnimalForAdoption = objectMapper.readValue(
                serializedAnimalForAdoption, Animal.class
        );

        assertThat(deserializedAnimalForAdoption, is(animal));
    }

    @Test
    public void shouldBeSerializableAndDeserializableWithAdoptedState() throws IOException {
        State adoptedState = new Adopted(LocalDate.now(), randomAlphabetic(10));
        animal = new Animal(uuid, name, registrationDate, type, estimatedAge, adoptedState);
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        String serializedAdoptedAnimal = objectMapper.writeValueAsString(animal);
        Animal deserializedAdoptedAnimal = objectMapper.readValue(
                serializedAdoptedAnimal, Animal.class
        );

        assertThat(deserializedAdoptedAnimal, is(animal));
    }

    @Test
    public void shouldBeSerializableAndDeserializableWithUnavailableState() throws IOException {
        Unavailable unavailableState = new Unavailable(randomAlphabetic(10));
        animal = new Animal(uuid, name, registrationDate, type, estimatedAge, unavailableState);
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
        Animal animal = new Animal(null, name, registrationDate, type, estimatedAge, state);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_UUID_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("uuid"));
    }

    @Test
    public void shouldValidateNonEmptyUuid() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal("", name, registrationDate, type, estimatedAge, state);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_UUID_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("uuid"));
    }

    @Test
    public void shouldValidateNonNullName() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal(uuid, null, registrationDate, type, estimatedAge, state);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_NAME_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("name"));
    }

    @Test
    public void shouldValidateNonEmptyName() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal(uuid, "", registrationDate, type, estimatedAge, state);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_NAME_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("name"));
    }

    @Test
    public void shouldValidateNonNullType() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal(uuid, name, registrationDate, null, estimatedAge, state);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_TYPE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("type"));
    }

    @Test
    public void shouldValidateNonNullState() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal(uuid, name, registrationDate, type, estimatedAge, null);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_STATE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("state"));
    }

    @Test
    public void shouldValidateNonNullRegistrationDate() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal(uuid, name, null, type, estimatedAge, state);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_REGISTRATION_DATE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("registrationDate"));
    }

    @Test
    public void shouldValidateNonNullEstimatedAge() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal(uuid, name, registrationDate, type, null, state);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_ESTIMATED_AGE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("estimatedAge"));
    }

    private State getRandomState() {
        Random random = new Random();
        List<State> states = Arrays.asList(
                new LookingForHuman(registrationDate),
                new Adopted(LocalDate.now(), randomAlphabetic(10)),
                new Unavailable(randomAlphabetic(10))
        );
        int randomStateIndex = random.nextInt(states.size());
        return states.get(randomStateIndex);
    }

    private static LocalValidatorFactoryBean getLocalValidatorFactoryBean() {
        LocalValidatorFactoryBean localValidatorFactory = new LocalValidatorFactoryBean();
        localValidatorFactory.setProviderClass(HibernateValidator.class);
        localValidatorFactory.afterPropertiesSet();
        return localValidatorFactory;
    }
}