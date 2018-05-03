package ec.animal.adoption.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hibernate.validator.HibernateValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class AnimalTest {

    private LocalDateTime registrationDate;
    private String uuid;
    private String name;
    private Type type;
    private Animal animal;

    @Before
    public void setUp() {
        registrationDate = LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault()));
        uuid = randomAlphabetic(10);
        name = randomAlphabetic(10);
        type = Type.DOG;
        animal = new Animal(uuid, name, registrationDate, type);
    }

    @Test
    public void shouldBeEqualToAnAvailableAnimalWithSameValues() {
        Animal sameAnimal = new Animal(uuid, name, registrationDate, type);

        assertEquals(animal, sameAnimal);
    }

    @Test
    public void shouldNotBeEqualToAnAvailableAnimalWithDifferentValues() {
        Animal differentAnimal = new Animal(
                randomAlphabetic(10),
                randomAlphabetic(10),
                LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault())),
                Type.CAT
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
        Animal sameAnimal = new Animal(uuid, name, registrationDate, type);

        assertEquals(animal.hashCode(), sameAnimal.hashCode());
    }

    @Test
    public void shouldHaveDifferentHashCodeWhenHavingDifferentValues() {
        Animal differentAnimal = new Animal(
                randomAlphabetic(10),
                randomAlphabetic(10),
                LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault())),
                type
        );

        assertNotEquals(animal.hashCode(), differentAnimal.hashCode());
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        String serializedAnimalForAdoption = objectMapper.writeValueAsString(animal);

        Animal deserializedAnimal = objectMapper.readValue(
                serializedAnimalForAdoption, Animal.class
        );

        assertThat(deserializedAnimal, is(animal));
    }

    @Test
    public void shouldValidateNonNullUuid() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal(null, randomAlphabetic(10), LocalDateTime.now(), type);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is("must not be empty"));
        assertThat(constraintViolation.getPropertyPath().toString(), is("uuid"));
    }

    @Test
    public void shouldValidateNonEmptyUuid() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal("", randomAlphabetic(10), LocalDateTime.now(), type);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is("must not be empty"));
        assertThat(constraintViolation.getPropertyPath().toString(), is("uuid"));
    }

    @Test
    public void shouldValidateNonNullName() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal(randomAlphabetic(10), null, LocalDateTime.now(), type);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is("must not be empty"));
        assertThat(constraintViolation.getPropertyPath().toString(), is("name"));
    }

    @Test
    public void shouldValidateNonEmptyName() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        Animal animal = new Animal(randomAlphabetic(10), "", LocalDateTime.now(), type);

        Set<ConstraintViolation<Animal>> constraintViolations = localValidatorFactory.validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is("must not be empty"));
        assertThat(constraintViolation.getPropertyPath().toString(), is("name"));

    }

    private static LocalValidatorFactoryBean getLocalValidatorFactoryBean() {
        LocalValidatorFactoryBean localValidatorFactory = new LocalValidatorFactoryBean();
        localValidatorFactory.setProviderClass(HibernateValidator.class);
        localValidatorFactory.afterPropertiesSet();
        return localValidatorFactory;
    }
}