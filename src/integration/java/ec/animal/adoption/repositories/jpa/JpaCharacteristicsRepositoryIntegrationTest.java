package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.AbstractIntegrationTest;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.FriendlyWith;
import ec.animal.adoption.domain.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.models.jpa.JpaCharacteristics;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomPhysicalActivity;
import static ec.animal.adoption.TestUtils.getRandomSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class JpaCharacteristicsRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private JpaCharacteristicsRepository jpaCharacteristicsRepository;

    private JpaAnimal jpaAnimal;
    private JpaCharacteristics entity;

    @Before
    public void setUp() {
        jpaAnimal = saveJpaAnimal();
    }

    @Test
    public void shouldSaveCharacteristics() {
        Characteristics characteristics = new Characteristics(
                getRandomSize(),
                getRandomPhysicalActivity(),
                new Temperaments(Sociability.VERY_SOCIABLE, Docility.DOCILE, Balance.BALANCED),
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
        Characteristics characteristics = new Characteristics(
                getRandomSize(),
                getRandomPhysicalActivity(),
                new Temperaments(Sociability.SHY, Docility.NEITHER_DOCILE_NOR_DOMINANT, Balance.POSSESSIVE),
                FriendlyWith.OTHER_ANIMALS,
                FriendlyWith.ADULTS
        );
        UUID animalUuid = jpaAnimal.toAnimal().getUuid();
        characteristics.setAnimalUuid(animalUuid);
        entity = new JpaCharacteristics(characteristics);
        JpaCharacteristics jpaCharacteristicsSaved = jpaCharacteristicsRepository.save(entity);

        Optional<JpaCharacteristics> jpaCharacteristicsFound = jpaCharacteristicsRepository.findByAnimalUuid(animalUuid);

        assertThat(jpaCharacteristicsFound.isPresent(), is(true));
        assertReflectionEquals(jpaCharacteristicsSaved, jpaCharacteristicsFound.get());
    }
}