package ec.animal.adoption.models.jpa;

import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Type;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JpaAnimalTest {

    private LocalDateTime registrationDate;
    private Animal animal;
    private String uuid;
    private String name;
    private Type type;
    private EstimatedAge estimatedAge;

    @Before
    public void setUp() {
        uuid = randomAlphabetic(10);
        name = randomAlphabetic(10);
        registrationDate = LocalDateTime.now();
        type = Type.DOG;
        estimatedAge = EstimatedAge.SENIOR_ADULT;
        State state = TestUtils.getRandomState();
        animal = new Animal(uuid, name, registrationDate, type, estimatedAge, state);
    }

    @Test
    public void shouldCreateAJpaAnimalFromAnAnimal() {
        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        assertThat(jpaAnimal.toAnimal(), is(animal));
    }

    @Test
    public void shouldUpdateState() {
        animal = new Animal(uuid, name, registrationDate, type, estimatedAge, new LookingForHuman(registrationDate));
        JpaAnimal jpaAnimal = new JpaAnimal(animal);
        State newState = new Adopted(LocalDate.now(), randomAlphabetic(10));
        jpaAnimal.setState(newState);

        assertThat(jpaAnimal.toAnimal().getState(), is(newState));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaAnimal.class).usingGetClass().withPrefabValues(
                Timestamp.class,
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusDays(2))
        ).verify();
    }
}