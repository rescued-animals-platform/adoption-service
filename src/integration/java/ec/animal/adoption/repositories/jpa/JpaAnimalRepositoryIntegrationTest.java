package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.IntegrationTest;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Type;
import ec.animal.adoption.models.jpa.JpaAnimal;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;

public class JpaAnimalRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private JpaAnimalRepository jpaAnimalRepository;

    @Test
    public void shouldSaveAnAnimal() {
        JpaAnimal entity = new JpaAnimal(
                new Animal(
                        randomAlphabetic(10),
                        randomAlphabetic(10),
                        LocalDateTime.now(),
                        Type.CAT,
                        EstimatedAge.YOUNG
                )
        );
        JpaAnimal animalForAdoption = jpaAnimalRepository.save(entity);

        assertEquals(animalForAdoption, entity);
    }
}