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

public class AnimalForAdoptionTest {

    private LocalDateTime registrationDate;
    private String uuid;
    private String name;
    private AnimalForAdoption animalForAdoption;

    @Before
    public void setUp() {
        registrationDate = LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault()));
        uuid = randomAlphabetic(10);
        name = randomAlphabetic(10);
        animalForAdoption = new AnimalForAdoption(uuid, name, registrationDate);
    }

    @Test
    public void shouldBeEqualToAnAvailableAnimalWithSameValues() {
        AnimalForAdoption sameAnimalForAdoption = new AnimalForAdoption(uuid, name, registrationDate);

        assertEquals(animalForAdoption, sameAnimalForAdoption);
    }

    @Test
    public void shouldNotBeEqualToAnAvailableAnimalWithDifferentValues() {
        AnimalForAdoption differentAnimalForAdoption = new AnimalForAdoption(
                randomAlphabetic(10),
                randomAlphabetic(10),
                LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault()))
        );

        assertNotEquals(animalForAdoption, differentAnimalForAdoption);
    }

    @Test
    public void shouldNotBeEqualToAnotherObject() {
        assertNotEquals(animalForAdoption, new Object());
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertNotEquals(animalForAdoption, null);
    }

    @Test
    public void shouldBeEqualToItself() {
        assertEquals(animalForAdoption, animalForAdoption);
    }

    @Test
    public void shouldHaveSameHashCodeWhenHavingSameValues() {
        AnimalForAdoption sameAnimalForAdoption = new AnimalForAdoption(uuid, name, registrationDate);

        assertEquals(animalForAdoption.hashCode(), sameAnimalForAdoption.hashCode());
    }

    @Test
    public void shouldHaveDifferentHashCodeWhenHavingDifferentValues() {
        AnimalForAdoption differentAnimalForAdoption = new AnimalForAdoption(
                randomAlphabetic(10),
                randomAlphabetic(10),
                LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault()))
        );

        assertNotEquals(animalForAdoption.hashCode(), differentAnimalForAdoption.hashCode());
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        String serializedAnimalForAdoption = objectMapper.writeValueAsString(animalForAdoption);

        AnimalForAdoption deserializedAnimalForAdoption = objectMapper.readValue(
                serializedAnimalForAdoption, AnimalForAdoption.class
        );

        assertThat(deserializedAnimalForAdoption, is(animalForAdoption));
    }

    @Test
    public void shouldValidateNonNullUuid() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        AnimalForAdoption animalForAdoption = new AnimalForAdoption(null, randomAlphabetic(10), LocalDateTime.now());

        Set<ConstraintViolation<AnimalForAdoption>> constraintViolations = localValidatorFactory.validate(animalForAdoption);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<AnimalForAdoption> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is("must not be empty"));
        assertThat(constraintViolation.getPropertyPath().toString(), is("uuid"));
    }

    @Test
    public void shouldValidateNonEmptyUuid() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        AnimalForAdoption animalForAdoption = new AnimalForAdoption("", randomAlphabetic(10), LocalDateTime.now());

        Set<ConstraintViolation<AnimalForAdoption>> constraintViolations = localValidatorFactory.validate(animalForAdoption);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<AnimalForAdoption> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is("must not be empty"));
        assertThat(constraintViolation.getPropertyPath().toString(), is("uuid"));
    }

    @Test
    public void shouldValidateNonNullName() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        AnimalForAdoption animalForAdoption = new AnimalForAdoption(randomAlphabetic(10), null, LocalDateTime.now());

        Set<ConstraintViolation<AnimalForAdoption>> constraintViolations = localValidatorFactory.validate(animalForAdoption);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<AnimalForAdoption> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is("must not be empty"));
        assertThat(constraintViolation.getPropertyPath().toString(), is("name"));
    }

    @Test
    public void shouldValidateNonEmptyName() {
        LocalValidatorFactoryBean localValidatorFactory = getLocalValidatorFactoryBean();
        AnimalForAdoption animalForAdoption = new AnimalForAdoption(randomAlphabetic(10), "", LocalDateTime.now());

        Set<ConstraintViolation<AnimalForAdoption>> constraintViolations = localValidatorFactory.validate(animalForAdoption);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<AnimalForAdoption> constraintViolation = constraintViolations.iterator().next();
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