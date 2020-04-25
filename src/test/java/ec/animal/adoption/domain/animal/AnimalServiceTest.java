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

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.builders.OrganizationBuilder;
import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.animal.dto.AnimalDto;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.exception.EntityAlreadyExistsException;
import ec.animal.adoption.domain.exception.InvalidStateException;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.state.State;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static ec.animal.adoption.TestUtils.getRandomPhysicalActivity;
import static ec.animal.adoption.TestUtils.getRandomSize;
import static ec.animal.adoption.TestUtils.getRandomSpecies;
import static ec.animal.adoption.TestUtils.getRandomState;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
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
public class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private Animal expectedAnimal;

    @Mock
    private Pageable pageable;

    private State state;
    private List<Animal> animalsFilteredByState;
    private Species species;
    private PhysicalActivity physicalActivity;
    private Size size;
    private AnimalService animalService;

    @BeforeEach
    public void setUp() {
        state = getRandomState();
        animalsFilteredByState = newArrayList(
                AnimalBuilder.random().withState(state).build(),
                AnimalBuilder.random().withState(state).build(),
                AnimalBuilder.random().withState(state).build()
        );
        species = getRandomSpecies();
        physicalActivity = getRandomPhysicalActivity();
        size = getRandomSize();
        animalService = new AnimalService(animalRepository);
    }

    @Test
    public void shouldCreateAnAnimalIfItDoesNotAlreadyExist() {
        Animal animal = AnimalBuilder.random().build();
        when(animalRepository.exists(animal)).thenReturn(false);
        when(animalRepository.save(animal)).thenReturn(expectedAnimal);

        Animal createdAnimal = animalService.create(animal);

        assertEquals(expectedAnimal, createdAnimal);
    }

    @Test
    public void shouldThrowEntityAlreadyExistExceptionWhenCreatingAnimalThatAlreadyExist() {
        Animal animal = AnimalBuilder.random().build();
        when(animalRepository.exists(animal)).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> {
            animalService.create(animal);
        });
        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    public void shouldReturnAnimalByItsUuid() {
        UUID uuid = UUID.randomUUID();
        Organization organization = OrganizationBuilder.random().build();
        when(animalRepository.getBy(uuid, organization)).thenReturn(expectedAnimal);

        Animal animal = animalService.getBy(uuid, organization);

        assertThat(animal, is(expectedAnimal));
    }

    @Test
    public void shouldReturnAllAnimals() {
        PagedEntity<Animal> expectedPageOfAnimals = new PagedEntity<>(newArrayList(
                AnimalBuilder.random().build(), AnimalBuilder.random().build(), AnimalBuilder.random().build()
        ));
        Organization organization = OrganizationBuilder.random().build();
        when(animalRepository.getAllFor(organization, pageable)).thenReturn(expectedPageOfAnimals);

        PagedEntity<Animal> pageOfAnimals = animalService.listAllFor(organization, pageable);

        assertThat(pageOfAnimals, is(expectedPageOfAnimals));
    }

    @Test
    public void shouldThrowInvalidStateExceptionWhenStateNameIsNotValid() {
        String invalidStateName = randomAlphabetic(10);

        assertThrows(InvalidStateException.class, () -> {
            animalService.listAllWithFilters(invalidStateName, species, physicalActivity, size, mock(Pageable.class));
        });
    }

    @Test
    public void shouldReturnAllAnimalDtosWithFiltersAndSmallPrimaryPictureUrl() {
        String stateName = state.getStateName();
        animalsFilteredByState.forEach(animal -> animal.setPrimaryLinkPicture(
                LinkPictureBuilder.random().withPictureType(PictureType.PRIMARY).build()
        ));
        when(animalRepository.getAllBy(stateName, species, physicalActivity, size, pageable))
                .thenReturn(new PagedEntity<>(animalsFilteredByState));
        PagedEntity<AnimalDto> expectedPageOfAnimalDtos = new PagedEntity<>(
                animalsFilteredByState.stream().map(AnimalDto::new).collect(Collectors.toList())
        );

        PagedEntity<AnimalDto> pageOfAnimalDtos = animalService.listAllWithFilters(
                stateName, species, physicalActivity, size, pageable
        );

        Assertions.assertThat(pageOfAnimalDtos)
                  .usingRecursiveFieldByFieldElementComparator()
                  .isEqualTo(expectedPageOfAnimalDtos);
    }

    @Test
    public void shouldReturnAllAnimalDataTransferObjectsWithFiltersAndNoSmallPrimaryPictureUrl() {
        String stateName = state.getStateName();
        when(animalRepository.getAllBy(state.getStateName(), species, physicalActivity, size, pageable))
                .thenReturn(new PagedEntity<>(animalsFilteredByState));

        PagedEntity<AnimalDto> expectedPageOfAnimalDtos = new PagedEntity<>(
                animalsFilteredByState.stream().map(AnimalDto::new).collect(Collectors.toList())
        );

        PagedEntity<AnimalDto> pageOfAnimalDtos = animalService.listAllWithFilters(
                stateName, species, physicalActivity, size, pageable
        );

        Assertions.assertThat(pageOfAnimalDtos)
                  .usingRecursiveFieldByFieldElementComparator()
                  .isEqualTo(expectedPageOfAnimalDtos);
    }
}