package ec.animal.adoption.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
    public void shouldBeSerializable() throws JsonProcessingException {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        String expectedSerializedAnimalForAdoption = "{\"uuid\":\"" + uuid +
                "\",\"name\":\"" + name + "\",\"registrationDate\":\"" + registrationDate.toString() + "\"}";
        String serializedAnimalForAdoption = objectMapper.writeValueAsString(animalForAdoption);

        assertThat(serializedAnimalForAdoption, is(expectedSerializedAnimalForAdoption));
    }

    @Test
    public void shouldBeDeserializable() throws IOException {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        String serializedAnimalForAdoption = "{\"uuid\":\"" + uuid +
                "\",\"name\":\"" + name + "\",\"registrationDate\":\"" + registrationDate.toString() + "\"}";
        AnimalForAdoption deserializedAnimalForAdoption = objectMapper.readValue(
                serializedAnimalForAdoption, AnimalForAdoption.class
        );

        assertThat(deserializedAnimalForAdoption, is(animalForAdoption));
    }
}