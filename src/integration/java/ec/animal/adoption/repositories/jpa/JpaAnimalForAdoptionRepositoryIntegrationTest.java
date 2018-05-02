package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.IntegrationTest;
import ec.animal.adoption.domain.AnimalForAdoption;
import ec.animal.adoption.models.jpa.JpaAnimalForAdoption;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;

public class JpaAnimalForAdoptionRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private JpaAnimalForAdoptionRepository jpaAnimalForAdoptionRepository;

    @Test
    public void shouldSaveAnAnimalForAdoption() {
        JpaAnimalForAdoption entity = new JpaAnimalForAdoption(
                new AnimalForAdoption(
                        randomAlphabetic(10),
                        randomAlphabetic(10),
                        LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault()))
                )
        );
        JpaAnimalForAdoption animalForAdoption = jpaAnimalForAdoptionRepository.save(entity);

        assertEquals(animalForAdoption, entity);
    }
}