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

package ec.animal.adoption.domain.animal;

import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.animal.dto.AnimalDto;
import ec.animal.adoption.domain.animal.dto.AnimalDtoFactory;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.exception.EntityAlreadyExistsException;
import ec.animal.adoption.domain.exception.IllegalUpdateException;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationFactory;
import ec.animal.adoption.domain.state.State;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static ec.animal.adoption.TestUtils.getRandomPhysicalActivity;
import static ec.animal.adoption.TestUtils.getRandomSize;
import static ec.animal.adoption.TestUtils.getRandomSpecies;
import static ec.animal.adoption.TestUtils.getRandomState;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private Animal expectedAnimal;

    @Mock
    private Pageable pageable;

    private Species species;
    private PhysicalActivity physicalActivity;
    private Size size;
    private AnimalService animalService;

    @BeforeEach
    public void setUp() {
        species = getRandomSpecies();
        physicalActivity = getRandomPhysicalActivity();
        size = getRandomSize();
        animalService = new AnimalService(animalRepository);
    }

    @Test
    void shouldCreateAnAnimalIfItDoesNotAlreadyExist() {
        AnimalDto animalDto = AnimalDtoFactory.random().build();
        when(animalRepository.exists(animalDto.getClinicalRecord(), animalDto.getOrganizationId()))
                .thenReturn(false);
        when(animalRepository.create(animalDto)).thenReturn(expectedAnimal);

        Animal createdAnimal = animalService.create(animalDto);

        assertEquals(expectedAnimal, createdAnimal);
    }

    @Test
    void shouldThrowEntityAlreadyExistExceptionWhenCreatingAnimalThatAlreadyExist() {
        AnimalDto animalDto = AnimalDtoFactory.random().build();
        when(animalRepository.exists(animalDto.getClinicalRecord(), animalDto.getOrganizationId()))
                .thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> {
            animalService.create(animalDto);
        });
        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    void shouldUpdateAnimalWhenThereIsNoOtherAnimalInOrganizationWithSameClinicalRecordInUpdateRequest() {
        UUID animalId = UUID.randomUUID();
        AnimalDto animalDtoWithUpdatedData = AnimalDtoFactory.random().build();
        Animal animalToBeUpdated = mock(Animal.class);
        when(animalRepository.getBy(animalId, animalDtoWithUpdatedData.getOrganization())).thenReturn(animalToBeUpdated);
        when(animalRepository.getBy(animalDtoWithUpdatedData.getClinicalRecord(), animalDtoWithUpdatedData.getOrganization()))
                .thenReturn(Optional.empty());
        Animal expectedUpdatedAnimal = mock(Animal.class);
        when(animalToBeUpdated.updateWith(animalDtoWithUpdatedData)).thenReturn(expectedUpdatedAnimal);
        when(animalRepository.save(expectedUpdatedAnimal)).thenReturn(expectedUpdatedAnimal);

        Animal updatedAnimal = animalService.update(animalId, animalDtoWithUpdatedData);

        assertEquals(expectedUpdatedAnimal, updatedAnimal);
    }

    @Test
    void shouldUpdateAnimalWhenAnimalFoundByClinicalRecordInOrganizationIsTheSameAnimalFoundById() {
        UUID animalId = UUID.randomUUID();
        AnimalDto animalDtoWithUpdatedData = AnimalDtoFactory.random().build();
        Animal animalToBeUpdated = mock(Animal.class);
        when(animalRepository.getBy(animalId, animalDtoWithUpdatedData.getOrganization())).thenReturn(animalToBeUpdated);
        Animal animalWithClinicalRecord = mock(Animal.class);
        when(animalRepository.getBy(animalDtoWithUpdatedData.getClinicalRecord(), animalDtoWithUpdatedData.getOrganization()))
                .thenReturn(Optional.of(animalWithClinicalRecord));
        when(animalToBeUpdated.isSameAs(animalWithClinicalRecord)).thenReturn(true);
        Animal expectedUpdatedAnimal = mock(Animal.class);
        when(animalToBeUpdated.updateWith(animalDtoWithUpdatedData)).thenReturn(expectedUpdatedAnimal);
        when(animalRepository.save(expectedUpdatedAnimal)).thenReturn(expectedUpdatedAnimal);

        Animal updatedAnimal = animalService.update(animalId, animalDtoWithUpdatedData);

        assertEquals(expectedUpdatedAnimal, updatedAnimal);
    }

    @Test
    void shouldThrowIllegalUpdateExceptionWhenAnAnimalWithDifferentIdentifierAlreadyExistsInOrganizationWithSameClinicalRecordInUpdateRequest() {
        UUID animalId = UUID.randomUUID();
        AnimalDto animalDtoWithUpdatedData = AnimalDtoFactory.random().build();
        Animal animalToBeUpdated = mock(Animal.class);
        when(animalRepository.getBy(animalId, animalDtoWithUpdatedData.getOrganization())).thenReturn(animalToBeUpdated);
        Animal animalWithClinicalRecord = mock(Animal.class);
        when(animalRepository.getBy(animalDtoWithUpdatedData.getClinicalRecord(), animalDtoWithUpdatedData.getOrganization()))
                .thenReturn(Optional.of(animalWithClinicalRecord));
        when(animalToBeUpdated.isSameAs(animalWithClinicalRecord)).thenReturn(false);

        assertThrows(IllegalUpdateException.class, () -> {
            animalService.update(animalId, animalDtoWithUpdatedData);
        });
    }

    @Test
    void shouldReturnAnimalByItsIdentifier() {
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationFactory.random().build();
        when(animalRepository.getBy(animalId, organization)).thenReturn(expectedAnimal);

        Animal animal = animalService.getBy(animalId, organization);

        assertThat(animal, is(expectedAnimal));
    }

    @Test
    void shouldReturnAllAnimals() {
        PagedEntity<Animal> expectedPageOfAnimals = new PagedEntity<>(newArrayList(
                AnimalFactory.random().build(), AnimalFactory.random().build(), AnimalFactory.random().build()
        ));
        Organization organization = OrganizationFactory.random().build();
        when(animalRepository.getAllFor(organization, pageable)).thenReturn(expectedPageOfAnimals);

        PagedEntity<Animal> pageOfAnimals = animalService.listAllFor(organization, pageable);

        assertThat(pageOfAnimals, is(expectedPageOfAnimals));
    }

    @Test
    void shouldReturnAllAnimalsWithFiltersAndSmallPrimaryPictureUrl() {
        State state = getRandomState();
        List<Animal> animalsFilteredByState = newArrayList(
                AnimalFactory.randomWithPrimaryLinkPicture().withState(state).build(),
                AnimalFactory.randomWithPrimaryLinkPicture().withState(state).build(),
                AnimalFactory.randomWithPrimaryLinkPicture().withState(state).build()
        );
        PagedEntity<Animal> expectedPageOfAnimals = new PagedEntity<>(animalsFilteredByState);
        when(animalRepository.getAllBy(state.getName().name(), species, physicalActivity, size, pageable))
                .thenReturn(expectedPageOfAnimals);

        PagedEntity<Animal> pageOfAnimals = animalService.listAllWithFilters(
                state.getName(), species, physicalActivity, size, pageable
        );

        Assertions.assertThat(pageOfAnimals)
                  .usingRecursiveFieldByFieldElementComparator()
                  .isEqualTo(expectedPageOfAnimals);
    }

    @Test
    void shouldReturnAllAnimalsWithFiltersAndNoSmallPrimaryPictureUrl() {
        State state = getRandomState();
        List<Animal> animalsFilteredByState = newArrayList(
                AnimalFactory.random().withState(state).build(),
                AnimalFactory.random().withState(state).build(),
                AnimalFactory.random().withState(state).build()
        );
        PagedEntity<Animal> expectedPageOfAnimals = new PagedEntity<>(animalsFilteredByState);
        when(animalRepository.getAllBy(state.getName().name(), species, physicalActivity, size, pageable))
                .thenReturn(expectedPageOfAnimals);

        PagedEntity<Animal> pageOfAnimals = animalService.listAllWithFilters(
                state.getName(), species, physicalActivity, size, pageable
        );

        Assertions.assertThat(pageOfAnimals)
                  .usingRecursiveFieldByFieldElementComparator()
                  .isEqualTo(expectedPageOfAnimals);
    }
}