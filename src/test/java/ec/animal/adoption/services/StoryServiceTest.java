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

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.Story;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.repositories.AnimalRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StoryServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    private StoryService storyService;

    @Before
    public void setUp() {
        storyService = new StoryService(animalRepository);
    }

    @Test
    public void shouldCreateStory() {
        ArgumentCaptor<Animal> argumentCaptor = ArgumentCaptor.forClass(Animal.class);
        UUID animalUuid = UUID.randomUUID();
        Animal animal = AnimalBuilder.random().withUuid(animalUuid).build();
        Story expectedStory = mock(Story.class);
        when(animalRepository.getBy(animalUuid)).thenReturn(animal);
        Animal animalWithStory = AnimalBuilder.random().withStory(expectedStory)
                .withState(animal.getState()).withClinicalRecord(animal.getClinicalRecord()).withName(animal.getName())
                .withEstimatedAge(animal.getEstimatedAge()).withSex(animal.getSex()).withSpecies(animal.getSpecies())
                .withRegistrationDate(animal.getRegistrationDate()).withUuid(animal.getUuid()).build();
        when(animalRepository.save(any(Animal.class))).thenReturn(animalWithStory);

        Story createdStory = storyService.create(animalUuid, expectedStory);

        verify(animalRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getStory(), is(expectedStory));
        assertThat(createdStory, is(expectedStory));
    }

    @Test
    public void shouldGetStoryByAnimalUuid() {
        UUID animalUuid = UUID.randomUUID();
        Story expectedStory = mock(Story.class);
        Animal animal = AnimalBuilder.random().withUuid(animalUuid).withStory(expectedStory).build();
        when(animalRepository.getBy(animalUuid)).thenReturn(animal);

        Story story = storyService.getBy(animalUuid);

        assertThat(story, is(expectedStory));
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenThereIsNoStoryForAnimal() {
        UUID animalUuid = UUID.randomUUID();
        Animal animal = AnimalBuilder.random().build();
        when(animalRepository.getBy(animalUuid)).thenReturn(animal);

        storyService.getBy(animalUuid);
    }
}