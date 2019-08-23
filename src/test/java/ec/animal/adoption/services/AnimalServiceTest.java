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

package ec.animal.adoption.services;

import ec.animal.adoption.TestUtils;
import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.dtos.AnimalDto;
import ec.animal.adoption.exceptions.InvalidStateException;
import ec.animal.adoption.repositories.AnimalRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(MockitoJUnitRunner.class)
public class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private Animal expectedAnimal;

    private AnimalService animalService;

    @Before
    public void setUp() {
        animalService = new AnimalService(animalRepository);
    }

    @Test
    public void shouldCreateAnAnimal() {
        Animal animal = AnimalBuilder.random().build();
        when(animalRepository.save(animal)).thenReturn(expectedAnimal);

        Animal createdAnimal = animalService.create(animal);

        assertThat(createdAnimal, is(expectedAnimal));
    }

    @Test
    public void shouldReturnAnimalByItsUuid() {
        UUID uuid = UUID.randomUUID();
        when(animalRepository.getBy(uuid)).thenReturn(expectedAnimal);

        Animal animal = animalService.getBy(uuid);

        assertThat(animal, is(expectedAnimal));
    }

    @Test(expected = InvalidStateException.class)
    public void shouldThrowInvalidStateExceptionWhenStateNameIsNotValid() {
        String invalidStateName = randomAlphabetic(10);

        animalService.listAllByStateWithPagination(invalidStateName, mock(Pageable.class));
    }

    @Test
    public void shouldReturnAllAnimalsByStateWithPaginationWithSmallPrimaryPictureUrl() {
        State state = TestUtils.getRandomState();
        String stateName = state.getStateName();
        LinkPicture primaryLinkPicture = LinkPictureBuilder.random().withPictureType(PictureType.PRIMARY).build();
        List<Animal> animals = newArrayList(
                AnimalBuilder.random().withState(state).withPrimaryLinkPicture(primaryLinkPicture).build(),
                AnimalBuilder.random().withState(state).withPrimaryLinkPicture(primaryLinkPicture).build(),
                AnimalBuilder.random().withState(state).withPrimaryLinkPicture(primaryLinkPicture).build()
        );
        Pageable pageable = mock(Pageable.class);
        when(animalRepository.getAllBy(state.getStateName(), pageable)).thenReturn(new PagedEntity<>(animals));
        PagedEntity<AnimalDto> expectedPageOfAnimalDtos = new PagedEntity<>(animals.stream().map(AnimalDto::new)
                .collect(Collectors.toList()));

        PagedEntity<AnimalDto> pageOfAnimalDtos = animalService.listAllByStateWithPagination(stateName, pageable);

        assertReflectionEquals(expectedPageOfAnimalDtos, pageOfAnimalDtos);
    }

    @Test
    public void shouldReturnAllAnimalsByStateWithPaginationWithNoSmallPrimaryPictureUrl() {
        State state = TestUtils.getRandomState();
        String stateName = state.getStateName();
        List<Animal> animals = newArrayList(
                AnimalBuilder.random().withState(state).build(),
                AnimalBuilder.random().withState(state).build(),
                AnimalBuilder.random().withState(state).build()
        );
        Pageable pageable = mock(Pageable.class);
        when(animalRepository.getAllBy(state.getStateName(), pageable)).thenReturn(new PagedEntity<>(animals));
        PagedEntity<AnimalDto> expectedPageOfAnimalDtos = new PagedEntity<>(animals.stream().map(AnimalDto::new)
                .collect(Collectors.toList()));

        PagedEntity<AnimalDto> pageOfAnimalDtos = animalService.listAllByStateWithPagination(stateName, pageable);

        assertReflectionEquals(expectedPageOfAnimalDtos, pageOfAnimalDtos);
    }

    @Test
    public void shouldReturnAllAnimals() {
        Pageable pageable = mock(Pageable.class);
        PagedEntity<Animal> expectedPageOfAnimals = new PagedEntity<>(newArrayList(
                AnimalBuilder.random().build(), AnimalBuilder.random().build(), AnimalBuilder.random().build()
        ));
        when(animalRepository.getAll(pageable)).thenReturn(expectedPageOfAnimals);

        PagedEntity<Animal> pageOfAnimals = animalService.listAll(pageable);

        assertThat(pageOfAnimals, is(expectedPageOfAnimals));
    }
}