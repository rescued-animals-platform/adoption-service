package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.AnimalForAdoption;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class JpaAnimalForAdoptionTest {

    private AnimalForAdoption animalForAdoption;

    @Before
    public void setUp() {
        LocalDateTime registrationDate = LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault()));
        String uuid = randomAlphabetic(10);
        String name = randomAlphabetic(10);
        animalForAdoption = new AnimalForAdoption(uuid, name, registrationDate);
    }

    @Test
    public void shouldCreateAJpaAvailableAnimalFromAnAvailableAnimal() {
        JpaAnimalForAdoption jpaAnimalForAdoption = new JpaAnimalForAdoption(animalForAdoption);

        assertThat(jpaAnimalForAdoption.toAvailableAnimal(), is(animalForAdoption));
    }

    @Test
    public void shouldBeEqualToAJpaAvailableAnimalWithSameValues() {
        JpaAnimalForAdoption jpaAnimalForAdoption = new JpaAnimalForAdoption(animalForAdoption);
        JpaAnimalForAdoption sameJpaAnimalForAdoption = new JpaAnimalForAdoption(animalForAdoption);

        assertEquals(jpaAnimalForAdoption, sameJpaAnimalForAdoption);
    }

    @Test
    public void shouldNotBeEqualToAJpaAvailableAnimalWithDifferentValues() {
        JpaAnimalForAdoption jpaAnimalForAdoption = new JpaAnimalForAdoption(animalForAdoption);
        AnimalForAdoption anotherAnimalForAdoption = new AnimalForAdoption(
                randomAlphabetic(10),
                randomAlphabetic(10),
                LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault()))
        );
        JpaAnimalForAdoption anotherJpaAnimalForAdoption = new JpaAnimalForAdoption(anotherAnimalForAdoption);

        assertNotEquals(jpaAnimalForAdoption, anotherJpaAnimalForAdoption);
    }

    @Test
    public void shouldNotBeEqualToAnotherObject() {
        JpaAnimalForAdoption jpaAnimalForAdoption = new JpaAnimalForAdoption(animalForAdoption);

        assertNotEquals(jpaAnimalForAdoption, new Object());
    }

    @Test
    public void shouldNotBeEqualToNull() {
        JpaAnimalForAdoption jpaAnimalForAdoption = new JpaAnimalForAdoption(animalForAdoption);

        assertNotEquals(jpaAnimalForAdoption, null);
    }

    @Test
    public void shouldBeEqualToItself() {
        JpaAnimalForAdoption jpaAnimalForAdoption = new JpaAnimalForAdoption(animalForAdoption);

        assertEquals(jpaAnimalForAdoption, jpaAnimalForAdoption);
    }

    @Test
    public void shouldBeEqual() {
        JpaAnimalForAdoption jpaAnimalForAdoption = new JpaAnimalForAdoption();
        JpaAnimalForAdoption sameJpaAnimalForAdoption = new JpaAnimalForAdoption();

        assertEquals(jpaAnimalForAdoption, sameJpaAnimalForAdoption);
    }

    @Test
    public void shouldHaveSameHashCodeWhenHavingSameValues() {
        JpaAnimalForAdoption jpaAnimalForAdoption = new JpaAnimalForAdoption(animalForAdoption);
        JpaAnimalForAdoption sameJpaAnimalForAdoption = new JpaAnimalForAdoption(animalForAdoption);

        assertEquals(jpaAnimalForAdoption.hashCode(), sameJpaAnimalForAdoption.hashCode());
    }

    @Test
    public void shouldHaveDifferentHashCodeWhenHavingDifferentValues() {
        JpaAnimalForAdoption jpaAnimalForAdoption = new JpaAnimalForAdoption(animalForAdoption);
        AnimalForAdoption anotherAnimalForAdoption = new AnimalForAdoption(
                randomAlphabetic(10),
                randomAlphabetic(10),
                LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault()))
        );
        JpaAnimalForAdoption anotherJpaAnimalForAdoption = new JpaAnimalForAdoption(anotherAnimalForAdoption);

        assertNotEquals(jpaAnimalForAdoption.hashCode(), anotherJpaAnimalForAdoption.hashCode());
    }
}