package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.IntegrationTest;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.Type;
import ec.animal.adoption.models.jpa.JpaAnimal;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;

public class JpaAnimalRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private JpaAnimalRepository jpaAnimalRepository;

    @Test
    public void shouldSaveAnAnimalForAdoption() {
        JpaAnimal entity = new JpaAnimal(
                new Animal(
                        randomAlphabetic(10),
                        randomAlphabetic(10),
                        LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault())),
                        Type.CAT
                )
        );
        JpaAnimal animalForAdoption = jpaAnimalRepository.save(entity);

        assertEquals(animalForAdoption, entity);
    }
}