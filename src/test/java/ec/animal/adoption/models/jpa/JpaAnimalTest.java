package ec.animal.adoption.models.jpa;

import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
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

    private String clinicalRecord;
    private String name;
    private LocalDateTime registrationDate;
    private Type type;
    private EstimatedAge estimatedAge;
    private Sex sex;
    private Animal animal;

    @Before
    public void setUp() {
        clinicalRecord = randomAlphabetic(10);
        name = randomAlphabetic(10);
        registrationDate = LocalDateTime.now();
        type = TestUtils.getRandomType();
        estimatedAge = TestUtils.getRandomEstimatedAge();
        sex = TestUtils.getRandomSex();
        State state = TestUtils.getRandomState();
        animal = new Animal(clinicalRecord, name, registrationDate, type, estimatedAge, sex, state);
    }

    @Test
    public void shouldCreateAJpaAnimalFromAnAnimal() {
        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        Animal animal = jpaAnimal.toAnimal();
        assertThat(animal.getClinicalRecord(), is(this.animal.getClinicalRecord()));
        assertThat(animal.getName(), is(this.animal.getName()));
        assertThat(animal.getRegistrationDate(), is(this.animal.getRegistrationDate()));
        assertThat(animal.getType(), is(this.animal.getType()));
        assertThat(animal.getEstimatedAge(), is(this.animal.getEstimatedAge()));
        assertThat(animal.getSex(), is(this.animal.getSex()));
        assertThat(animal.getState(), is(this.animal.getState()));
    }

    @Test
    public void shouldUpdateState() {
        animal = new Animal(
                clinicalRecord, name, registrationDate, type, estimatedAge, sex, new LookingForHuman(registrationDate)
        );
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