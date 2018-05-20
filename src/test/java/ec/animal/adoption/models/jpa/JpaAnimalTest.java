package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Type;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
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
    private String uuid;
    private String name;
    private LocalDateTime registrationDate;
    private Type type;
    private EstimatedAge estimatedAge;
    private State lookingForHumanState;

    @Before
    public void setUp() {
        uuid = randomAlphabetic(10);
        name = randomAlphabetic(10);
        registrationDate = LocalDateTime.now();
        type = Type.DOG;
        estimatedAge = EstimatedAge.SENIOR_ADULT;
        lookingForHumanState = new LookingForHuman(registrationDate);
        animal = new Animal(uuid, name, registrationDate, type, estimatedAge, lookingForHumanState);
    }

    @Test
    public void shouldCreateAJpaAnimalFromAnAnimalWithDefaultState() {
        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        assertThat(jpaAnimal.toAnimal(), is(animal));
    }

    @Test
    public void shouldCreateAJpaAnimalFromAnAnimalWithAnotherState() {
        State state = new Unavailable(randomAlphabetic(10));
        animal = new Animal(uuid, name, registrationDate, type, estimatedAge, state);
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
                EstimatedAge.YOUNG,
                new Unavailable(randomAlphabetic(10))
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
                EstimatedAge.YOUNG_ADULT,
                new Unavailable(randomAlphabetic(10))
        );
        JpaAnimal anotherJpaAnimal = new JpaAnimal(anotherAnimal);

        assertNotEquals(jpaAnimal.hashCode(), anotherJpaAnimal.hashCode());
    }
}