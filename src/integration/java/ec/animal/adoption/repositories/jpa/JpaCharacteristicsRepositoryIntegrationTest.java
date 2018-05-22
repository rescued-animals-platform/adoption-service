package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.IntegrationTest;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.Type;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.FriendlyWith;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.characteristics.temperament.Balance;
import ec.animal.adoption.domain.characteristics.temperament.Docility;
import ec.animal.adoption.domain.characteristics.temperament.Sociability;
import ec.animal.adoption.domain.state.Unavailable;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.models.jpa.JpaCharacteristics;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class JpaCharacteristicsRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private JpaAnimalRepository jpaAnimalRepository;

    @Autowired
    private JpaCharacteristicsRepository jpaCharacteristicsRepository;

    private JpaCharacteristics entity;

    @Test
    public void shouldSaveCharacteristics() {
        JpaAnimal jpaAnimal = saveJpaAnimal();

        Characteristics characteristics = new Characteristics(
                Size.BIG,
                PhysicalActivity.HIGH,
                Arrays.asList(Sociability.EXTREMELY_SOCIABLE, Balance.BALANCED),
                FriendlyWith.CHILDREN,
                FriendlyWith.DOGS,
                FriendlyWith.ADULTS
        );
        characteristics.setAnimalUuid(jpaAnimal.toAnimal().getUuid());

        entity = new JpaCharacteristics(characteristics);

        JpaCharacteristics jpaCharacteristics = jpaCharacteristicsRepository.save(entity);

        assertEquals(jpaCharacteristics, entity);
    }

    @Test
    public void shouldGetCharacteristicsByAnimalUuid() {
        JpaAnimal jpaAnimal = saveJpaAnimal();
        Characteristics characteristics = new Characteristics(
                Size.TINY,
                PhysicalActivity.LOW,
                Arrays.asList(Sociability.SHY, Balance.POSSESSIVE, Docility.NEITHER_DOCILE_NOR_DOMINANT),
                FriendlyWith.OTHER_ANIMALS,
                FriendlyWith.ADULTS
        );
        UUID animalUuid = jpaAnimal.toAnimal().getUuid();
        characteristics.setAnimalUuid(animalUuid);
        entity = new JpaCharacteristics(characteristics);
        JpaCharacteristics jpaCharacteristicsSaved = jpaCharacteristicsRepository.save(entity);

        JpaCharacteristics jpaCharacteristicsFound = jpaCharacteristicsRepository.findByAnimalUuid(animalUuid);

        assertReflectionEquals(jpaCharacteristicsSaved, jpaCharacteristicsFound);
    }

    private JpaAnimal saveJpaAnimal() {
        return jpaAnimalRepository.save(new JpaAnimal(new Animal(
                    randomAlphabetic(10),
                    randomAlphabetic(10),
                    LocalDateTime.now(),
                    Type.CAT,
                    EstimatedAge.YOUNG_ADULT,
                    Sex.MALE,
                    new Unavailable(randomAlphabetic(10))
            )));
    }
}