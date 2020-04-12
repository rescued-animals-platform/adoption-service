package ec.animal.adoption.repository;

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.CharacteristicsBuilder;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.Species;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.exception.EntityAlreadyExistsException;
import ec.animal.adoption.exception.EntityNotFoundException;
import ec.animal.adoption.model.jpa.JpaAnimal;
import ec.animal.adoption.repository.jpa.JpaAnimalRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.postgresql.util.PSQLException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("PMD.ExcessiveImports")
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
    public void shouldReturnAllAnimalsByStateWithPagination() {
        State lookingForHuman = new LookingForHuman(LocalDateTime.now());
        Species dog = Species.DOG;
        PhysicalActivity high = PhysicalActivity.HIGH;
        Size tiny = Size.TINY;
        Pageable pageable = mock(Pageable.class);
        List<JpaAnimal> jpaAnimals = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(i -> jpaAnimals.add(
                new JpaAnimal(AnimalBuilder.random().withState(lookingForHuman).withSpecies(dog).withCharacteristics(
                        CharacteristicsBuilder.random().withPhysicalActivity(high).withSize(tiny).build()
                ).build())
        ));
        PagedEntity<Animal> expectedPageOfAnimals = new PagedEntity<>(
                jpaAnimals.stream().map(JpaAnimal::toAnimal).collect(Collectors.toList())
        );
        when(
                jpaAnimalRepository
                        .findAllByStateNameAndSpeciesOrJpaCharacteristicsPhysicalActivityOrJpaCharacteristicsSize(
                                lookingForHuman.getStateName(), dog.name(), high.name(), tiny.name(), pageable
                        )
        ).thenReturn(new PageImpl<>(jpaAnimals));

        PagedEntity<Animal> pageOfAnimals = animalRepositoryPsql.getAllBy(
                lookingForHuman.getStateName(), dog, high, tiny, pageable
        );

        assertReflectionEquals(expectedPageOfAnimals, pageOfAnimals);
    }

    @Test
    public void shouldReturnAllAnimalsWithPagination() {
        Pageable pageable = mock(Pageable.class);
        List<JpaAnimal> listOfJpaAnimals = newArrayList(
                AnimalBuilder.random().build(), AnimalBuilder.random().build(), AnimalBuilder.random().build()
        ).stream().map(JpaAnimal::new).collect(Collectors.toList());
        PagedEntity<Animal> expectedPageOfAnimals = new PagedEntity<>(
                listOfJpaAnimals.stream().map(JpaAnimal::toAnimal).collect(Collectors.toList())
        );
        when(jpaAnimalRepository.findAll(pageable)).thenReturn(new PageImpl<>(listOfJpaAnimals));

        PagedEntity<Animal> pageOfAnimals = animalRepositoryPsql.getAll(pageable);

        assertReflectionEquals(expectedPageOfAnimals, pageOfAnimals);
    }
}
