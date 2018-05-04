package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Type;
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

public class JpaAnimalTest {

    private Animal animal;

    @Before
    public void setUp() {
        LocalDateTime registrationDate = LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault()));
        String uuid = randomAlphabetic(10);
        String name = randomAlphabetic(10);
        Type type = Type.DOG;
        EstimatedAge estimatedAge = EstimatedAge.SENIOR_ADULT;
        animal = new Animal(uuid, name, registrationDate, type, estimatedAge);
    }

    @Test
    public void shouldCreateAJpaAvailableAnimalFromAnAvailableAnimal() {
        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        assertThat(jpaAnimal.toAnimal(), is(animal));
    }

    @Test
    public void shouldBeEqualToAJpaAvailableAnimalWithSameValues() {
        JpaAnimal jpaAnimal = new JpaAnimal(animal);
        JpaAnimal sameJpaAnimal = new JpaAnimal(animal);

        assertEquals(jpaAnimal, sameJpaAnimal);
    }

    @Test
    public void shouldNotBeEqualToAJpaAvailableAnimalWithDifferentValues() {
        JpaAnimal jpaAnimal = new JpaAnimal(animal);
        Animal anotherAnimal = new Animal(
                randomAlphabetic(10),
                randomAlphabetic(10),
                LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault())),
                Type.CAT,
                EstimatedAge.YOUNG
        );
        JpaAnimal anotherJpaAnimal = new JpaAnimal(anotherAnimal);

        assertNotEquals(jpaAnimal, anotherJpaAnimal);
    }

    @Test
    public void shouldNotBeEqualToAnotherObject() {
        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        assertNotEquals(jpaAnimal, new Object());
    }

    @Test
    public void shouldNotBeEqualToNull() {
        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        assertNotEquals(jpaAnimal, null);
    }

    @Test
    public void shouldBeEqualToItself() {
        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        assertEquals(jpaAnimal, jpaAnimal);
    }

    @Test
    public void shouldBeEqual() {
        JpaAnimal jpaAnimal = new JpaAnimal();
        JpaAnimal sameJpaAnimal = new JpaAnimal();

        assertEquals(jpaAnimal, sameJpaAnimal);
    }

    @Test
    public void shouldHaveSameHashCodeWhenHavingSameValues() {
        JpaAnimal jpaAnimal = new JpaAnimal(animal);
        JpaAnimal sameJpaAnimal = new JpaAnimal(animal);

        assertEquals(jpaAnimal.hashCode(), sameJpaAnimal.hashCode());
    }

    @Test
    public void shouldHaveDifferentHashCodeWhenHavingDifferentValues() {
        JpaAnimal jpaAnimal = new JpaAnimal(animal);
        Animal anotherAnimal = new Animal(
                randomAlphabetic(10),
                randomAlphabetic(10),
                LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault())),
                Type.CAT,
                EstimatedAge.YOUNG_ADULT
        );
        JpaAnimal anotherJpaAnimal = new JpaAnimal(anotherAnimal);

        assertNotEquals(jpaAnimal.hashCode(), anotherJpaAnimal.hashCode());
    }
}