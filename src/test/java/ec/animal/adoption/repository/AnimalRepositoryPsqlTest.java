/*
    Copyright © 2018 Luisa Emme

    This file is part of Adoption Service in the Rescued Animals Platform.

    Adoption Service is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Adoption Service is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with Adoption Service.  If not, see <https://www.gnu.org/licenses/>.
 */

package ec.animal.adoption.repository;

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.CharacteristicsBuilder;
import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.AnimalRepository;
import ec.animal.adoption.domain.animal.Species;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.exception.EntityAlreadyExistsException;
import ec.animal.adoption.domain.exception.EntityNotFoundException;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.repository.jpa.JpaAnimalRepository;
import ec.animal.adoption.repository.jpa.model.JpaAnimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnimalRepositoryPsqlTest {

    @Mock
    private JpaAnimalRepository jpaAnimalRepository;

    private AnimalRepositoryPsql animalRepositoryPsql;
    private Animal animal;

    @BeforeEach
    public void setUp() {
        animal = AnimalBuilder.randomWithDefaultOrganization().build();
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

        assertEquals(expectedAnimal, savedAnimal);
    }

    @Test
    public void shouldThrowEntityAlreadyExistException() {
        doAnswer((Answer<Object>) invocation -> {
            throw mock(PSQLException.class);
        }).when(jpaAnimalRepository).save(any(JpaAnimal.class));

        assertThrows(EntityAlreadyExistsException.class, () -> {
            animalRepositoryPsql.save(animal);
        });
    }

    @Test
    public void shouldGetAnimalByItsUuid() {
        JpaAnimal expectedJpaAnimal = new JpaAnimal(animal);
        UUID uuid = expectedJpaAnimal.toAnimal().getUuid();
        when(jpaAnimalRepository.findById(uuid)).thenReturn(Optional.of(expectedJpaAnimal));

        Animal animalFound = animalRepositoryPsql.getBy(uuid);

        assertEquals(expectedJpaAnimal.toAnimal(), animalFound);
    }

    @Test
    public void shouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> {
            animalRepositoryPsql.getBy(UUID.randomUUID());
        });
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
                new JpaAnimal(AnimalBuilder.random()
                                           .withState(lookingForHuman)
                                           .withSpecies(dog)
                                           .withCharacteristics(CharacteristicsBuilder.random()
                                                                                      .withPhysicalActivity(high)
                                                                                      .withSize(tiny)
                                                                                      .build())
                                           .build())
        ));
        PagedEntity<Animal> expectedPageOfAnimals = new PagedEntity<>(
                jpaAnimals.stream().map(JpaAnimal::toAnimal).collect(Collectors.toList())
        );
        when(jpaAnimalRepository.findAllByStateNameAndSpeciesOrJpaCharacteristicsPhysicalActivityOrJpaCharacteristicsSize(
                lookingForHuman.getStateName(), dog.name(), high.name(), tiny.name(), pageable)
        ).thenReturn(new PageImpl<>(jpaAnimals));

        PagedEntity<Animal> pageOfAnimals = animalRepositoryPsql.getAllBy(
                lookingForHuman.getStateName(), dog, high, tiny, pageable
        );

        assertEquals(expectedPageOfAnimals, pageOfAnimals);
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

        assertEquals(expectedPageOfAnimals, pageOfAnimals);
    }
}
