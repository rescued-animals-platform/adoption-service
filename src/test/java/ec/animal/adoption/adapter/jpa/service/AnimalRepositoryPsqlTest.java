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

package ec.animal.adoption.adapter.jpa.service;

import ec.animal.adoption.adapter.jpa.model.JpaAnimal;
import ec.animal.adoption.domain.model.animal.Animal;
import ec.animal.adoption.domain.model.animal.AnimalFactory;
import ec.animal.adoption.domain.model.animal.Species;
import ec.animal.adoption.domain.model.animal.dto.AnimalDto;
import ec.animal.adoption.domain.model.animal.dto.AnimalDtoFactory;
import ec.animal.adoption.domain.model.characteristics.Characteristics;
import ec.animal.adoption.domain.model.characteristics.CharacteristicsFactory;
import ec.animal.adoption.domain.model.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.model.characteristics.Size;
import ec.animal.adoption.domain.model.exception.EntityAlreadyExistsException;
import ec.animal.adoption.domain.model.exception.EntityNotFoundException;
import ec.animal.adoption.domain.model.organization.Organization;
import ec.animal.adoption.domain.model.organization.OrganizationFactory;
import ec.animal.adoption.domain.model.state.State;
import ec.animal.adoption.domain.service.AnimalRepository;
import ec.animal.adoption.domain.model.PagedEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.postgresql.util.PSQLException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@ExtendWith(MockitoExtension.class)
class AnimalRepositoryPsqlTest {

    @Mock
    private JpaAnimalRepository jpaAnimalRepository;

    private AnimalRepositoryPsql animalRepositoryPsql;
    private Animal animal;

    @BeforeEach
    public void setUp() {
        animal = AnimalFactory.randomWithDefaultOrganization().build();
        animalRepositoryPsql = new AnimalRepositoryPsql(jpaAnimalRepository);
    }

    @Test
    void shouldBeAnInstanceOfAnimalRepository() {
        assertThat(animalRepositoryPsql, is(instanceOf(AnimalRepository.class)));
    }

    @Test
    void shouldCreateAnimal() {
        AnimalDto animalDto = AnimalDtoFactory.random().build();
        JpaAnimal jpaAnimal = JpaAnimalMapper.MAPPER.toJpaAnimal(animalDto);
        when(jpaAnimalRepository.save(any(JpaAnimal.class))).thenReturn(jpaAnimal);
        Animal expectedAnimal = JpaAnimalMapper.MAPPER.toAnimal(jpaAnimal);

        Animal createdAnimal = animalRepositoryPsql.create(animalDto);

        assertReflectionEquals(expectedAnimal, createdAnimal);
    }

    @Test
    void shouldThrowEntityAlreadyExistExceptionIfAnyExceptionIsThrownByJpaAnimalRepositoryWhenCreatingAnimal() {
        doAnswer((Answer<Object>) invocation -> {
            throw mock(PSQLException.class);
        }).when(jpaAnimalRepository).save(any(JpaAnimal.class));
        AnimalDto animalDto = AnimalDtoFactory.random().build();

        assertThrows(EntityAlreadyExistsException.class, () -> animalRepositoryPsql.create(animalDto));
    }

    @Test
    void shouldSaveAnimal() {
        JpaAnimal jpaAnimal = JpaAnimalMapper.MAPPER.toJpaAnimal(animal);
        when(jpaAnimalRepository.save(any(JpaAnimal.class))).thenReturn(jpaAnimal);

        Animal savedAnimal = animalRepositoryPsql.save(animal);

        assertReflectionEquals(animal, savedAnimal);
    }

    @Test
    void shouldThrowEntityAlreadyExistExceptionIfAnyExceptionIsThrownByJpaAnimalRepositoryWhenSavingAnimal() {
        doAnswer((Answer<Object>) invocation -> {
            throw mock(PSQLException.class);
        }).when(jpaAnimalRepository).save(any(JpaAnimal.class));

        assertThrows(EntityAlreadyExistsException.class, () -> animalRepositoryPsql.save(animal));
    }

    @Test
    void shouldGetAnimalByClinicalRecordAndOrganization() {
        JpaAnimal expectedJpaAnimal = JpaAnimalMapper.MAPPER.toJpaAnimal(animal);
        when(jpaAnimalRepository.findByClinicalRecordAndJpaOrganizationId(
                animal.getClinicalRecord(), animal.getOrganizationId())
        ).thenReturn(Optional.of(expectedJpaAnimal));

        Optional<Animal> animalFound = animalRepositoryPsql.getBy(animal.getClinicalRecord(), animal.getOrganization());

        assertTrue(animalFound.isPresent());
        assertReflectionEquals(animal, animalFound.get());
    }

    @Test
    void shouldReturnEmptyWhenAnimalByClinicalRecordAndOrganizationDoesNotExist() {
        String clinicalRecord = randomAlphabetic(10);
        Organization organization = OrganizationFactory.random().build();
        when(jpaAnimalRepository.findByClinicalRecordAndJpaOrganizationId(clinicalRecord, organization.getOrganizationId()))
                .thenReturn(Optional.empty());

        Optional<Animal> animalFound = animalRepositoryPsql.getBy(clinicalRecord, organization);

        assertTrue(animalFound.isEmpty());
    }

    @Test
    void shouldReturnTrueIfAnimalWithClinicalRecordAndOrganizationIdIsFound() {
        String clinicalRecord = randomAlphabetic(10);
        UUID organizationId = UUID.randomUUID();
        when(jpaAnimalRepository.existsByClinicalRecordAndJpaOrganizationId(clinicalRecord, organizationId))
                .thenReturn(true);

        boolean exists = animalRepositoryPsql.exists(clinicalRecord, organizationId);

        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseIfAnimalWithClinicalRecordAndOrganizationIdIsNotFound() {
        String clinicalRecord = randomAlphabetic(10);
        UUID organizationId = UUID.randomUUID();
        when(jpaAnimalRepository.existsByClinicalRecordAndJpaOrganizationId(clinicalRecord, organizationId))
                .thenReturn(false);

        boolean exists = animalRepositoryPsql.exists(clinicalRecord, organizationId);

        assertFalse(exists);
    }

    @Test
    void shouldGetAnimalByItsIdentifier() {
        JpaAnimal expectedJpaAnimal = JpaAnimalMapper.MAPPER.toJpaAnimal(animal);
        when(jpaAnimalRepository.findById(animal.getIdentifier())).thenReturn(Optional.of(expectedJpaAnimal));

        Animal animalFound = animalRepositoryPsql.getBy(animal.getIdentifier());

        assertReflectionEquals(animal, animalFound);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenAnimalByItsIdentifierIsNotFound() {
        UUID animalId = UUID.randomUUID();

        assertThrows(EntityNotFoundException.class, () -> animalRepositoryPsql.getBy(animalId));
    }

    @Test
    void shouldGetAnimalByItsIdentifierAndOrganization() {
        JpaAnimal expectedJpaAnimal = JpaAnimalMapper.MAPPER.toJpaAnimal(animal);
        when(jpaAnimalRepository.findByIdAndJpaOrganizationId(animal.getIdentifier(), animal.getOrganizationId()))
                .thenReturn(Optional.of(expectedJpaAnimal));

        Animal animalFound = animalRepositoryPsql.getBy(animal.getIdentifier(), animal.getOrganization());

        assertReflectionEquals(animal, animalFound);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenAnimalByItsIdentifierAndOrganizationIsNotFound() {
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationFactory.random().build();

        assertThrows(EntityNotFoundException.class, () -> animalRepositoryPsql.getBy(animalId, organization));
    }

    @Test
    void shouldReturnAllAnimalsForOrganizationWithPagination() {
        Pageable pageable = mock(Pageable.class);
        List<JpaAnimal> listOfJpaAnimals = Stream.of(AnimalFactory.random().build(),
                                                     AnimalFactory.random().build(),
                                                     AnimalFactory.random().build())
                                                 .map(JpaAnimalMapper.MAPPER::toJpaAnimal)
                                                 .toList();
        PagedEntity<Animal> expectedPageOfAnimals = new PagedEntity<>(
                listOfJpaAnimals.stream().map(JpaAnimalMapper.MAPPER::toAnimal).toList()
        );
        Organization organization = OrganizationFactory.random().build();
        when(jpaAnimalRepository.findAllByJpaOrganizationId(organization.getOrganizationId(), pageable))
                .thenReturn(new PageImpl<>(listOfJpaAnimals));

        PagedEntity<Animal> pageOfAnimals = animalRepositoryPsql.getAllFor(organization, pageable);

        Assertions.assertThat(pageOfAnimals).usingRecursiveFieldByFieldElementComparator().isEqualTo(expectedPageOfAnimals);
    }

    @Test
    void shouldReturnAllAnimalsWithFiltersAndPagination() {
        State lookingForHuman = State.lookingForHuman();
        Species dog = Species.DOG;
        PhysicalActivity high = PhysicalActivity.HIGH;
        Size tiny = Size.TINY;
        Pageable pageable = mock(Pageable.class);
        List<JpaAnimal> jpaAnimals = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Characteristics characteristics = CharacteristicsFactory.random()
                                                                    .withPhysicalActivity(high)
                                                                    .withSize(tiny)
                                                                    .build();
            Animal animal = AnimalFactory.random()
                                         .withState(lookingForHuman)
                                         .withSpecies(dog)
                                         .withCharacteristics(characteristics)
                                         .build();
            jpaAnimals.add(JpaAnimalMapper.MAPPER.toJpaAnimal(animal));
        });
        PagedEntity<Animal> expectedPageOfAnimals = new PagedEntity<>(
                jpaAnimals.stream().map(JpaAnimalMapper.MAPPER::toAnimal).toList()
        );
        when(jpaAnimalRepository.findAllByStateNameAndSpeciesOrJpaCharacteristicsPhysicalActivityOrJpaCharacteristicsSize(
                lookingForHuman.getName().name(), dog.name(), high.name(), tiny.name(), pageable)
        ).thenReturn(new PageImpl<>(jpaAnimals));

        PagedEntity<Animal> pageOfAnimals = animalRepositoryPsql.getAllBy(
                lookingForHuman.getName().name(), dog, high, tiny, pageable
        );

        Assertions.assertThat(pageOfAnimals).usingRecursiveFieldByFieldElementComparator().isEqualTo(expectedPageOfAnimals);
    }
}
