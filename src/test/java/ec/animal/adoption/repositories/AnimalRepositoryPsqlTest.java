package ec.animal.adoption.repositories;

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.repositories.jpa.JpaAnimalRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.postgresql.util.PSQLException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

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
    public void shouldSaveAnimal() {
        JpaAnimal expectedJpaAnimal = new JpaAnimal(animal);
        Animal expectedAnimal = expectedJpaAnimal.toAnimal();
        when(jpaAnimalRepository.save(any(JpaAnimal.class))).thenReturn(expectedJpaAnimal);

        Animal savedAnimal = animalRepositoryPsql.save(animal);

        assertReflectionEquals(expectedAnimal, savedAnimal);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void shouldThrowEntityAlreadyExistException() {
        doAnswer((Answer<Object>) invocation -> {
            throw mock(PSQLException.class);
        }).when(jpaAnimalRepository).save(any(JpaAnimal.class));

        animalRepositoryPsql.save(animal);
    }

    @Test
    public void shouldGetAnimalByItsUuid() {
        JpaAnimal expectedJpaAnimal = new JpaAnimal(animal);
        UUID uuid = expectedJpaAnimal.toAnimal().getUuid();
        when(jpaAnimalRepository.findById(uuid)).thenReturn(Optional.of(expectedJpaAnimal));

        Animal animalFound = animalRepositoryPsql.getBy(uuid);

        assertReflectionEquals(expectedJpaAnimal.toAnimal(), animalFound);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundException() {
        animalRepositoryPsql.getBy(UUID.randomUUID());
    }

    @Test
    public void shouldGetListOfAnimals() {
        List<JpaAnimal> listOfJpaAnimals = newArrayList(
                AnimalBuilder.random().build(),
                AnimalBuilder.random().build(),
                AnimalBuilder.random().build()
        ).stream().map(JpaAnimal::new).collect(Collectors.toList());
        List<Animal> expectedListOfAnimals = listOfJpaAnimals.stream().map(JpaAnimal::toAnimal)
                .collect(Collectors.toList());
        when(jpaAnimalRepository.findAll()).thenReturn(listOfJpaAnimals);

        List<Animal> listOfAnimals = animalRepositoryPsql.getAll();

        assertReflectionEquals(expectedListOfAnimals, listOfAnimals);
    }

    @Test
    public void shouldGetEmptyList() {
        when(jpaAnimalRepository.findAll()).thenReturn(newArrayList());

        assertThat(animalRepositoryPsql.getAll().isEmpty(), is(true));
    }

    @Test
    public void shouldReturnTrueIfThereIsAnAnimalWithCorrespondingUuid() {
        UUID animalUuid = UUID.randomUUID();
        when(jpaAnimalRepository.findById(animalUuid)).thenReturn(Optional.of(mock(JpaAnimal.class)));

        assertThat(animalRepositoryPsql.animalExists(animalUuid), is(true));
    }

    @Test
    public void shouldReturnFalseIfThereIsNoAnimalWithUuid() {
        UUID animalUuid = UUID.randomUUID();
        when(jpaAnimalRepository.findById(animalUuid)).thenReturn(Optional.empty());

        assertThat(animalRepositoryPsql.animalExists(animalUuid), is(false));
    }
}
