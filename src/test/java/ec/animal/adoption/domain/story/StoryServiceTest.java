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

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.OrganizationBuilder;
import ec.animal.adoption.builders.StoryBuilder;
import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.AnimalRepository;
import ec.animal.adoption.domain.exception.EntityAlreadyExistsException;
import ec.animal.adoption.domain.exception.EntityNotFoundException;
import ec.animal.adoption.domain.organization.Organization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoryServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    private StoryService storyService;

    @BeforeEach
    public void setUp() {
        storyService = new StoryService(animalRepository);
    }

    @Test
    public void shouldCreateStory() {
        ArgumentCaptor<Animal> animalArgumentCaptor = ArgumentCaptor.forClass(Animal.class);
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationBuilder.random().build();
        Animal animal = AnimalBuilder.random().withIdentifier(animalId).withOrganization(organization).build();
        Story expectedStory = mock(Story.class);
        when(animalRepository.getBy(animalId, organization)).thenReturn(animal);
        Animal animalWithStory = AnimalBuilder.random()
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
        Organization organization = OrganizationBuilder.random().build();
        Animal animal = AnimalBuilder.random()
                                     .withIdentifier(animalId)
                                     .withOrganization(organization)
                                     .withStory(StoryBuilder.random().build())
                                     .build();
        when(animalRepository.getBy(animalId, organization)).thenReturn(animal);

        assertThrows(EntityAlreadyExistsException.class, () -> {
            storyService.createFor(animalId, organization, mock(Story.class));
        });
    }

    @Test
    public void shouldUpdateStory() {
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationBuilder.random().build();
        Animal animalFound = mock(Animal.class);
        when(animalRepository.getBy(animalId, organization)).thenReturn(animalFound);
        when(animalFound.getStory()).thenReturn(Optional.of(mock(Story.class)));
        Story newStory = mock(Story.class);
        Animal animalWithUpdatedStory = mock(Animal.class);
        Story expectedUpdatedStory = mock(Story.class);
        when(animalWithUpdatedStory.getStory()).thenReturn(Optional.of(expectedUpdatedStory));
        when(animalRepository.save(any(Animal.class))).thenReturn(animalWithUpdatedStory);

        Story updatedStory = storyService.updateFor(animalId, organization, newStory);

        verify(animalFound).updateStory(newStory);
        assertEquals(expectedUpdatedStory, updatedStory);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenUpdatingStoryForAnimalThatDoesNotHaveAlreadyAStory() {
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationBuilder.random().build();
        Animal animalFound = mock(Animal.class);
        when(animalRepository.getBy(animalId, organization)).thenReturn(animalFound);
        when(animalFound.getStory()).thenReturn(Optional.empty());
        Story newStory = mock(Story.class);

        assertThrows(EntityNotFoundException.class, () -> {
            storyService.updateFor(animalId, organization, newStory);
        });
    }

    @Test
    public void shouldGetStoryByAnimalId() {
        UUID animalId = UUID.randomUUID();
        Story expectedStory = mock(Story.class);
        Animal animal = AnimalBuilder.random().withIdentifier(animalId).withStory(expectedStory).build();
        when(animalRepository.getBy(animalId)).thenReturn(animal);

        Story story = storyService.getBy(animalId);

        assertThat(story, is(expectedStory));
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenThereIsNoStoryForAnimal() {
        UUID animalId = UUID.randomUUID();
        Animal animal = AnimalBuilder.random().build();
        when(animalRepository.getBy(animalId)).thenReturn(animal);

        assertThrows(EntityNotFoundException.class, () -> {
            storyService.getBy(animalId);
        });
    }
}