package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Type;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;
import org.junit.Before;
import org.junit.Test;

import java.time.*;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class JpaAnimalTest {

    private Animal animal;

    @Before
    public void setUp() {
        LocalDateTime registrationDate = LocalDateTime.now();
        String uuid = randomAlphabetic(10);
        String name = randomAlphabetic(10);
        Type type = Type.DOG;
        EstimatedAge estimatedAge = EstimatedAge.SENIOR_ADULT;
        animal = new Animal(uuid, name, registrationDate, type, estimatedAge);
    }

    @Test
    public void shouldCreateAJpaAnimalFromAnAnimalWithDefaultState() {
        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        assertThat(jpaAnimal.toAnimal(), is(animal));
    }

    @Test
    public void shouldCreateAJpaAnimalFromAnAnimalWithAnotherState() {
        animal.setState(new Unavailable(randomAlphabetic(10)));
        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        assertThat(jpaAnimal.toAnimal(), is(animal));
    }

    @Test
    public void shouldUpdateState() {
        JpaAnimal jpaAnimal = new JpaAnimal(animal);
        State newState = new Adopted(LocalDate.now(), randomAlphabetic(10));
        jpaAnimal.setState(newState);

        assertThat(jpaAnimal.toAnimal().getState(), is(newState));
    }

    @Test
    public void shouldBeEqualToAJpaAnimalWithSameValues() {
        JpaAnimal jpaAnimal = new JpaAnimal(animal);
        JpaAnimal sameJpaAnimal = new JpaAnimal(animal);

        assertEquals(jpaAnimal, sameJpaAnimal);
    }

    @Test
    public void shouldNotBeEqualToAJpaAnimalWithDifferentValues() {
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