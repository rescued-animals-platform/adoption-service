package ec.animal.adoption.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

public class AnimalTest {

    private static final String ANIMAL_NAME_IS_REQUIRED = "Animal name is required";
    private static final String ANIMAL_CLINICAL_RECORD_IS_REQUIRED = "Animal clinical record is required";
    private static final String ANIMAL_TYPE_IS_REQUIRED = "Animal type is required";
    private static final String ANIMAL_REGISTRATION_DATE_IS_REQUIRED = "Animal registration date is required";
    private static final String ANIMAL_ESTIMATED_AGE_IS_REQUIRED = "Animal estimated age is required";
    private static final String ANIMAL_SEX_IS_REQUIRED = "Animal sex is required";

    private String clinicalRecord;
    private String name;
    private LocalDateTime registrationDate;
    private Type type;
    private EstimatedAge estimatedAge;
    private Sex sex;
    private State state;
    private Animal animal;

    @Before
    public void setUp() {
        clinicalRecord = randomAlphabetic(10);
        name = randomAlphabetic(10);
        registrationDate = LocalDateTime.now();
        type = getRandomType();
        estimatedAge = getRandomEstimatedAge();
        sex = getRandomSex();
        state = getRandomState();
        animal = new Animal(clinicalRecord, name, registrationDate, type, estimatedAge, sex, state);
    }

    @Test
    public void shouldCreateAnimalWithNullUuidWhenUuidIsNotSet() {
        animal = new Animal(clinicalRecord, name, registrationDate, type, estimatedAge, sex, state);

        assertNull(animal.getUuid());
    }

    @Test
    public void shouldSetUuid() {
        UUID uuid = UUID.randomUUID();
        animal = new Animal(clinicalRecord, name, registrationDate, type, estimatedAge, sex, state);
        animal.setUuid(uuid);

        assertThat(animal.getUuid(), is(uuid));
    }

    @Test
    public void shouldCreateAnimalWithDefaultLookingForHumanStateWhenStateIsNull() {
        animal = new Animal(clinicalRecord, name, registrationDate, type, estimatedAge, sex, null);
        LookingForHuman expectedLookingForHumanState = new LookingForHuman(registrationDate);

        assertThat(animal.getState(), is(expectedLookingForHumanState));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Animal.class).usingGetClass().withNonnullFields("state")
                .suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void shouldBeSerializableAndDeserializableWithDefaultLookingForHumanState() throws IOException {
        animal = new Animal(clinicalRecord, name, registrationDate, type, estimatedAge, sex, null);
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        String serializedAnimalForAdoption = objectMapper.writeValueAsString(animal);
        Animal deserializedAnimalForAdoption = objectMapper.readValue(
                serializedAnimalForAdoption, Animal.class
        );

        assertThat(deserializedAnimalForAdoption, is(animal));
        assertThat(deserializedAnimalForAdoption.getState(), is(notNullValue()));
        assertThat(deserializedAnimalForAdoption.getState(), is(instanceOf(LookingForHuman.class)));
        assertThat(deserializedAnimalForAdoption.getState(), is(animal.getState()));
    }

    @Test
    public void shouldBeSerializableAndDeserializableWithAdoptedState() throws IOException {
        State adoptedState = new Adopted(LocalDate.now(), randomAlphabetic(10));
        animal = new Animal(clinicalRecord, name, registrationDate, type, estimatedAge, sex, adoptedState);
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
        animal = new Animal(clinicalRecord, name, registrationDate, type, estimatedAge, sex, unavailableState);
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
    public void shouldValidateNonNullClinicalRecord() {
        Animal animal = new Animal(null, name, registrationDate, type, estimatedAge, sex, state);

        Set<ConstraintViolation<Animal>> constraintViolations = getValidator().validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_CLINICAL_RECORD_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("clinicalRecord"));
    }

    @Test
    public void shouldValidateNonEmptyClinicalRecord() {
        Animal animal = new Animal("", name, registrationDate, type, estimatedAge, sex, state);

        Set<ConstraintViolation<Animal>> constraintViolations = getValidator().validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_CLINICAL_RECORD_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("clinicalRecord"));
    }

    @Test
    public void shouldValidateNonNullName() {
        Animal animal = new Animal(clinicalRecord, null, registrationDate, type, estimatedAge, sex, state);

        Set<ConstraintViolation<Animal>> constraintViolations = getValidator().validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_NAME_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("name"));
    }

    @Test
    public void shouldValidateNonEmptyName() {
        Animal animal = new Animal(clinicalRecord, "", registrationDate, type, estimatedAge, sex, state);

        Set<ConstraintViolation<Animal>> constraintViolations = getValidator().validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_NAME_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("name"));
    }

    @Test
    public void shouldValidateNonNullType() {
        Animal animal = new Animal(clinicalRecord, name, registrationDate, null, estimatedAge, sex, state);

        Set<ConstraintViolation<Animal>> constraintViolations = getValidator().validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_TYPE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("type"));
    }

    @Test
    public void shouldValidateNonNullRegistrationDate() {
        Animal animal = new Animal(clinicalRecord, name, null, type, estimatedAge, sex, state);

        Set<ConstraintViolation<Animal>> constraintViolations = getValidator().validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_REGISTRATION_DATE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("registrationDate"));
    }

    @Test
    public void shouldValidateNonNullEstimatedAge() {
        Animal animal = new Animal(clinicalRecord, name, registrationDate, type, null, sex, state);

        Set<ConstraintViolation<Animal>> constraintViolations = getValidator().validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_ESTIMATED_AGE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("estimatedAge"));
    }

    @Test
    public void shouldValidateNonNullSex() {
        Animal animal = new Animal(clinicalRecord, name, registrationDate, type, estimatedAge, null, state);

        Set<ConstraintViolation<Animal>> constraintViolations = getValidator().validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_SEX_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("sex"));
    }
}