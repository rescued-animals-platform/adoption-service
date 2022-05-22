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

package ec.animal.adoption.domain.story;

import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.AnimalFactory;
import ec.animal.adoption.domain.animal.AnimalRepository;
import ec.animal.adoption.domain.exception.EntityAlreadyExistsException;
import ec.animal.adoption.domain.exception.EntityNotFoundException;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoryServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    private StoryService storyService;

    @BeforeEach
    public void setUp() {
        storyService = new StoryService(animalRepository);
    }

    @Test
    void shouldCreateStory() {
        ArgumentCaptor<Animal> animalArgumentCaptor = ArgumentCaptor.forClass(Animal.class);
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationFactory.random().build();
        Animal animal = AnimalFactory.random().withIdentifier(animalId).withOrganization(organization).build();
        Story expectedStory = mock(Story.class);
        when(animalRepository.getBy(animalId, organization)).thenReturn(animal);
        Animal animalWithStory = AnimalFactory.random()
                                              .withIdentifier(animalId)
                                              .withOrganization(organization)
                                              .withStory(expectedStory)
                                              .build();
        when(animalRepository.save(any(Animal.class))).thenReturn(animalWithStory);

        Story createdStory = storyService.createFor(animalId, organization, expectedStory);

        verify(animalRepository).save(animalArgumentCaptor.capture());
        assertTrue(animalArgumentCaptor.getValue().getStory().isPresent());
        assertEquals(expectedStory, animalArgumentCaptor.getValue().getStory().get());
        assertEquals(expectedStory, createdStory);
    }

    @Test
    void shouldThrowEntityAlreadyExistsExceptionWhenThereIsAlreadyAStoryForAnimal() {
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationFactory.random().build();
        Animal animal = AnimalFactory.random()
                                     .withIdentifier(animalId)
                                     .withOrganization(organization)
                                     .withStory(StoryFactory.random().build())
                                     .build();
        when(animalRepository.getBy(animalId, organization)).thenReturn(animal);

        assertThrows(EntityAlreadyExistsException.class, () -> {
            storyService.createFor(animalId, organization, mock(Story.class));
        });
    }

    @Test
    void shouldUpdateStoryWithADifferentOneWhenItAlreadyExists() {
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationFactory.random().build();
        Story existingStory = mock(Story.class);
        Story newStory = mock(Story.class);
        Story expectedUpdatedStory = mock(Story.class);
        when(existingStory.updateWith(newStory)).thenReturn(expectedUpdatedStory);
        Animal animalFound = AnimalFactory.random().withStory(existingStory).build();
        when(animalRepository.getBy(animalId, organization)).thenReturn(animalFound);
        when(animalRepository.save(any(Animal.class))).thenReturn(
                AnimalFactory.random().withStory(expectedUpdatedStory).build()
        );
        ArgumentCaptor<Animal> animalArgumentCaptor = ArgumentCaptor.forClass(Animal.class);

        Story updatedStory = storyService.updateFor(animalId, organization, newStory);

        verify(animalRepository).save(animalArgumentCaptor.capture());
        Animal animalSaved = animalArgumentCaptor.getValue();
        assertTrue(animalSaved.getStory().isPresent());
        assertEquals(expectedUpdatedStory, animalSaved.getStory().get());
        assertEquals(expectedUpdatedStory, updatedStory);
    }

    @Test
    void shouldCreateStoryWhenUpdatingStoryForAnimalThatDoesNotHaveAlreadyAStory() {
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationFactory.random().build();
        when(animalRepository.getBy(animalId, organization)).thenReturn(mock(Animal.class));
        Story newStory = mock(Story.class);
        Animal animalWithUpdatedStory = AnimalFactory.random().withStory(newStory).build();
        when(animalRepository.save(any(Animal.class))).thenReturn(animalWithUpdatedStory);
        ArgumentCaptor<Animal> animalArgumentCaptor = ArgumentCaptor.forClass(Animal.class);

        Story updatedStory = storyService.updateFor(animalId, organization, newStory);

        verify(animalRepository).save(animalArgumentCaptor.capture());
        Animal animalSaved = animalArgumentCaptor.getValue();
        assertTrue(animalSaved.getStory().isPresent());
        assertEquals(newStory, animalSaved.getStory().get());
        assertEquals(newStory, updatedStory);
    }

    @Test
    void shouldNotSaveStoryAndReturnTheSameOneWhenUpdatingAnimalWithTheSameStoryItAlreadyHas() {
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationFactory.random().build();
        Story newStory = mock(Story.class);
        Animal foundAnimal = mock(Animal.class);
        when(animalRepository.getBy(animalId, organization)).thenReturn(foundAnimal);
        when(foundAnimal.has(newStory)).thenReturn(true);

        Story updatedStory = storyService.updateFor(animalId, organization, newStory);

        verify(animalRepository, never()).save(any(Animal.class));
        assertEquals(newStory, updatedStory);
    }

    @Test
    void shouldGetStoryByAnimalId() {
        UUID animalId = UUID.randomUUID();
        Story expectedStory = mock(Story.class);
        Animal animal = AnimalFactory.random().withIdentifier(animalId).withStory(expectedStory).build();
        when(animalRepository.getBy(animalId)).thenReturn(animal);

        Story story = storyService.getBy(animalId);

        assertThat(story, is(expectedStory));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenThereIsNoStoryForAnimal() {
        UUID animalId = UUID.randomUUID();
        Animal animal = AnimalFactory.random().build();
        when(animalRepository.getBy(animalId)).thenReturn(animal);

        assertThrows(EntityNotFoundException.class, () -> {
            storyService.getBy(animalId);
        });
    }
}