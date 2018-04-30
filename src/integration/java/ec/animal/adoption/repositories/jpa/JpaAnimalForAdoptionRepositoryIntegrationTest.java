package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.models.JpaAnimalForAdoption;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaAnimalForAdoptionRepositoryIntegrationTest {

    @Autowired
    private JpaAnimalForAdoptionRepository jpaAnimalForAdoptionRepository;

    @Test
    public void shouldSaveAnAnimalForAdoption() {
        JpaAnimalForAdoption entity = new JpaAnimalForAdoption(
                new ec.animal.adoption.domain.AnimalForAdoption(
                        randomAlphabetic(10),
                        randomAlphabetic(10),
                        LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault()))
                )
        );
        JpaAnimalForAdoption animalForAdoption = jpaAnimalForAdoptionRepository.save(entity);

        assertEquals(animalForAdoption, entity);
    }
}