package ec.animal.adoption.domain;

import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
}