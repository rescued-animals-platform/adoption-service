package ec.animal.adoption.repositories;

import ec.animal.adoption.builders.CharacteristicsBuilder;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaCharacteristics;
import ec.animal.adoption.repositories.jpa.JpaCharacteristicsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CharacteristicsRepositoryPsqlTest {

    @Mock
    private JpaCharacteristicsRepository jpaCharacteristicsRepository;

    @Mock
    private AnimalRepositoryPsql animalRepositoryPsql;

    private CharacteristicsRepositoryPsql characteristicsRepositoryPsql;
    private Characteristics characteristics;
    private UUID animalUuid;

    @Before
    public void setUp() {
        animalUuid = UUID.randomUUID();
        characteristics = CharacteristicsBuilder.random().build();
        when(animalRepositoryPsql.animalExists(animalUuid)).thenReturn(true);
        characteristicsRepositoryPsql = new CharacteristicsRepositoryPsql(
                jpaCharacteristicsRepository, animalRepositoryPsql
        );
    }

    @Test
    public void shouldBeAnInstanceOfCharacteristicsRepository() {
        assertThat(characteristicsRepositoryPsql, is(instanceOf(CharacteristicsRepository.class)));
    }

    @Test
    public void shouldSaveJpaCharacteristics() {
        characteristics.setAnimalUuid(animalUuid);
        JpaCharacteristics expectedJpaCharacteristics = new JpaCharacteristics(characteristics);
        when(jpaCharacteristicsRepository.save(any(JpaCharacteristics.class))).thenReturn(expectedJpaCharacteristics);

        Characteristics savedCharacteristics = characteristicsRepositoryPsql.save(this.characteristics);

        assertThat(savedCharacteristics, is(expectedJpaCharacteristics.toCharacteristics()));
        assertThat(savedCharacteristics.getAnimalUuid(), is(characteristics.getAnimalUuid()));
        assertThat(savedCharacteristics.getSize(), is(characteristics.getSize()));
        assertThat(savedCharacteristics.getPhysicalActivity(), is(characteristics.getPhysicalActivity()));
        assertThat(savedCharacteristics.getTemperaments(), is(characteristics.getTemperaments()));
        assertThat(savedCharacteristics.getFriendlyWith(), is(characteristics.getFriendlyWith()));
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void shouldThrowEntityAlreadyExistException() {
        characteristics.setAnimalUuid(animalUuid);
        doAnswer((Answer<Object>) invocation -> {
            throw mock(DataIntegrityViolationException.class);
        }).when(jpaCharacteristicsRepository).save(any(JpaCharacteristics.class));

        characteristicsRepositoryPsql.save(characteristics);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenSavingCharacteristicsForNonExistentAnimal() {
        characteristicsRepositoryPsql.save(characteristics);
    }

    @Test
    public void shouldGetJpaCharacteristicsByAnimalUuid() {
        JpaCharacteristics expectedJpaCharacteristics = new JpaCharacteristics(characteristics);
        when(jpaCharacteristicsRepository.findByAnimalUuid(animalUuid)).thenReturn(
                Optional.of(expectedJpaCharacteristics)
        );

        Characteristics characteristicsFound = characteristicsRepositoryPsql.getBy(animalUuid);

        assertThat(characteristicsFound, is(characteristics));
        assertThat(characteristicsFound, is(expectedJpaCharacteristics.toCharacteristics()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundException() {
        characteristicsRepositoryPsql.getBy(UUID.randomUUID());
    }
}