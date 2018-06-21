package ec.animal.adoption.repositories;

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.repositories.jpa.JpaAnimalRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.postgresql.util.PSQLException;

import java.util.Optional;
import java.util.UUID;

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
        animal = AnimalBuilder.random().build();
        animalRepositoryPsql = new AnimalRepositoryPsql(jpaAnimalRepository);
    }

    @Test
    public void shouldBeAnInstanceOfAnimalRepository() {
        assertThat(animalRepositoryPsql, is(instanceOf(AnimalRepository.class)));
    }

    @Test
    public void shouldSaveJpaAnimal() {
        JpaAnimal expectedJpaAnimal = new JpaAnimal(animal);
        when(jpaAnimalRepository.save(any(JpaAnimal.class))).thenReturn(expectedJpaAnimal);

        Animal savedAnimal = animalRepositoryPsql.save(animal);

        assertThat(expectedJpaAnimal.toAnimal(), is(savedAnimal));
        assertThat(savedAnimal.getClinicalRecord(), is(animal.getClinicalRecord()));
        assertThat(savedAnimal.getName(), is(animal.getName()));
        assertThat(savedAnimal.getRegistrationDate(), is(animal.getRegistrationDate()));
        assertThat(savedAnimal.getSpecies(), is(animal.getSpecies()));
        assertThat(savedAnimal.getEstimatedAge(), is(animal.getEstimatedAge()));
        assertThat(savedAnimal.getSex(), is(animal.getSex()));
        assertThat(savedAnimal.getState(), is(animal.getState()));
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
