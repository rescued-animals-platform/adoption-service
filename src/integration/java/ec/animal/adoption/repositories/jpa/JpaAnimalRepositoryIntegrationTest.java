package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.AbstractIntegrationTest;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.AnimalSpecies;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;
import ec.animal.adoption.models.jpa.JpaAnimal;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;

public class JpaAnimalRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private JpaAnimalRepository jpaAnimalRepository;

    private JpaAnimal entity;

    @Before
    public void setUp() {
        LocalDateTime registrationDate = LocalDateTime.now();
        entity = new JpaAnimal(new Animal(
                randomAlphabetic(10),
                randomAlphabetic(10),
                registrationDate,
                AnimalSpecies.CAT,
                EstimatedAge.YOUNG,
                Sex.MALE,
                new LookingForHuman(registrationDate)
        ));
    }

    @Test
    public void shouldSaveAnAnimalLookingForHuman() {
        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);

        assertEquals(jpaAnimal, entity);
    }

    @Test
    public void shouldSaveAnAnimalWithDifferentState() {
        JpaAnimal entity = new JpaAnimal(new Animal(
                randomAlphabetic(10),
                randomAlphabetic(10),
                LocalDateTime.now(),
                AnimalSpecies.CAT,
                EstimatedAge.YOUNG_ADULT,
                Sex.FEMALE,
                new Unavailable(randomAlphabetic(10))
        ));

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);

        assertEquals(jpaAnimal, entity);
    }

    @Test
    public void shouldUpdateAnimalState() {
        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        State updatedState = new Adopted(LocalDate.now(), randomAlphabetic(10));
        jpaAnimal.setState(updatedState);

        JpaAnimal adoptedJpaAnimal = jpaAnimalRepository.save(jpaAnimal);

        assertEquals(adoptedJpaAnimal.toAnimal().getState(), updatedState);
    }
}