package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.Species;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.repositories.jpa.JpaAnimalRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.postgresql.util.PSQLException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AnimalRepositoryPsqlTest {

    @Mock
    private JpaAnimalRepository jpaAnimalRepository;

    private AnimalRepositoryPsql animalRepositoryPsql;
    private Animal animal;

    @Before
    public void setUp() {
        LocalDateTime registrationDate = LocalDateTime.now();
        animal = new Animal(
                randomAlphabetic(10),
                randomAlphabetic(10),
                registrationDate,
                Species.DOG,
                EstimatedAge.YOUNG_ADULT,
                Sex.FEMALE,
                new LookingForHuman(registrationDate)
        );

        animalRepositoryPsql = new AnimalRepositoryPsql(jpaAnimalRepository);
    }

    @Test
    public void shouldBeAnInstanceOfAnimalRepository() {
        assertThat(animalRepositoryPsql, is(instanceOf(AnimalRepository.class)));
    }

    @Test
    public void shouldSaveJpaAnimal() {
        ArgumentCaptor<JpaAnimal> jpaAnimalArgumentCaptor = ArgumentCaptor.forClass(JpaAnimal.class);
        JpaAnimal expectedJpaAnimal = new JpaAnimal(animal);
        when(jpaAnimalRepository.save(any(JpaAnimal.class))).thenReturn(expectedJpaAnimal);

        Animal savedAnimal = animalRepositoryPsql.save(animal);

        verify(jpaAnimalRepository).save(jpaAnimalArgumentCaptor.capture());
        JpaAnimal jpaAnimal = jpaAnimalArgumentCaptor.getValue();
        Animal animal = jpaAnimal.toAnimal();

        assertThat(animal.getClinicalRecord(), is(this.animal.getClinicalRecord()));
        assertThat(animal.getName(), is(this.animal.getName()));
        assertThat(animal.getRegistrationDate(), is(this.animal.getRegistrationDate()));
        assertThat(animal.getSpecies(), is(this.animal.getSpecies()));
        assertThat(animal.getEstimatedAge(), is(this.animal.getEstimatedAge()));
        assertThat(animal.getSex(), is(this.animal.getSex()));
        assertThat(animal.getState(), is(this.animal.getState()));
        assertThat(expectedJpaAnimal.toAnimal(), is(savedAnimal));
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void shouldThrowEntityAlreadyExistException() {
        doAnswer((Answer<Object>) invocation -> {
            throw mock(PSQLException.class);
        }).when(jpaAnimalRepository).save(any(JpaAnimal.class));

        animalRepositoryPsql.save(animal);
    }

    @Test
    public void shouldReturnTrueIfThereIsAnAnimalWithCorrespondingUuid() {
        UUID animalUuid = UUID.randomUUID();
        when(jpaAnimalRepository.findById(animalUuid)).thenReturn(Optional.of(mock(JpaAnimal.class)));

        assertThat(animalRepositoryPsql.animalExists(animalUuid), is(true));
    }

    @Test
    public void shouldFalseIfThereIsNoAnimalWithUuid() {
        UUID animalUuid = UUID.randomUUID();
        when(jpaAnimalRepository.findById(animalUuid)).thenReturn(Optional.empty());

        assertThat(animalRepositoryPsql.animalExists(animalUuid), is(false));
    }
}
