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

package ec.animal.adoption.domain.characteristics;

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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacteristicsServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    private CharacteristicsService characteristicsService;

    @BeforeEach
    public void setUp() {
        characteristicsService = new CharacteristicsService(animalRepository);
    }

    @Test
    void shouldCreateCharacteristics() {
        ArgumentCaptor<Animal> argumentCaptor = ArgumentCaptor.forClass(Animal.class);
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationFactory.random().build();
        Animal animal = AnimalFactory.random().withIdentifier(animalId).withOrganization(organization).build();
        Characteristics expectedCharacteristics = CharacteristicsFactory.random().build();
        when(animalRepository.getBy(animalId, organization)).thenReturn(animal);
        Animal animalWithCharacteristics = AnimalFactory.random().withCharacteristics(expectedCharacteristics)
                                                        .withState(animal.getState()).withClinicalRecord(animal.getClinicalRecord()).withName(animal.getName())
                                                        .withEstimatedAge(animal.getEstimatedAge()).withSex(animal.getSex()).withSpecies(animal.getSpecies())
                                                        .withRegistrationDate(animal.getRegistrationDate()).withIdentifier(animal.getIdentifier()).build();
        when(animalRepository.save(any(Animal.class))).thenReturn(animalWithCharacteristics);

        Characteristics createdCharacteristics = characteristicsService.createFor(animalId, organization, expectedCharacteristics);

        verify(animalRepository).save(argumentCaptor.capture());
        assertTrue(argumentCaptor.getValue().getCharacteristics().isPresent());
        assertEquals(expectedCharacteristics, argumentCaptor.getValue().getCharacteristics().get());
        assertThat(createdCharacteristics, is(expectedCharacteristics));
    }

    @Test
    void shouldThrowEntityAlreadyExistsExceptionWhenThereAreAlreadyCharacteristicsForAnimal() {
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationFactory.random().build();
        Animal animal = AnimalFactory.random()
                                     .withIdentifier(animalId)
                                     .withOrganization(organization)
                                     .withCharacteristics(CharacteristicsFactory.random().build())
                                     .build();
        when(animalRepository.getBy(animalId, organization)).thenReturn(animal);

        assertThrows(EntityAlreadyExistsException.class, () -> {
            characteristicsService.createFor(animalId, organization, mock(Characteristics.class));
        });
    }

    @Test
    void shouldUpdateCharacteristicsWithADifferentOneWhenTheyAlreadyExist() {
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationFactory.random().build();
        Characteristics existingCharacteristics = mock(Characteristics.class);
        Characteristics newCharacteristics = mock(Characteristics.class);
        Characteristics expectedUpdatedCharacteristics = mock(Characteristics.class);
        when(existingCharacteristics.updateWith(newCharacteristics)).thenReturn(expectedUpdatedCharacteristics);
        Animal animalFound = AnimalFactory.random().withCharacteristics(existingCharacteristics).build();
        when(animalRepository.getBy(animalId, organization)).thenReturn(animalFound);
        when(animalRepository.save(any(Animal.class))).thenReturn(
                AnimalFactory.random().withCharacteristics(expectedUpdatedCharacteristics).build()
        );
        ArgumentCaptor<Animal> animalArgumentCaptor = ArgumentCaptor.forClass(Animal.class);

        Characteristics updatedCharacteristics = characteristicsService.updateFor(animalId,
                                                                                  organization,
                                                                                  newCharacteristics);

        verify(animalRepository).save(animalArgumentCaptor.capture());
        Animal animalSaved = animalArgumentCaptor.getValue();
        assertTrue(animalSaved.getCharacteristics().isPresent());
        assertEquals(expectedUpdatedCharacteristics, animalSaved.getCharacteristics().get());
        assertEquals(expectedUpdatedCharacteristics, updatedCharacteristics);
    }

    @Test
    void shouldCreateCharacteristicsWhenUpdatingCharacteristicsForAnimalThatDoesNotHaveAlreadyCharacteristics() {
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationFactory.random().build();
        when(animalRepository.getBy(animalId, organization)).thenReturn(mock(Animal.class));
        Characteristics newCharacteristics = mock(Characteristics.class);
        Animal animalWithUpdatedCharacteristics = AnimalFactory.random().withCharacteristics(newCharacteristics).build();
        when(animalRepository.save(any(Animal.class))).thenReturn(animalWithUpdatedCharacteristics);
        ArgumentCaptor<Animal> animalArgumentCaptor = ArgumentCaptor.forClass(Animal.class);

        Characteristics updatedCharacteristics = characteristicsService.updateFor(animalId, organization, newCharacteristics);

        verify(animalRepository).save(animalArgumentCaptor.capture());
        Animal animalSaved = animalArgumentCaptor.getValue();
        assertTrue(animalSaved.getCharacteristics().isPresent());
        assertEquals(newCharacteristics, animalSaved.getCharacteristics().get());
        assertEquals(newCharacteristics, updatedCharacteristics);
    }

    @Test
    void shouldNotSaveCharacteristicsAndReturnTheSameOnesWhenUpdatingAnimalWithTheSameCharacteristicsItAlreadyHas() {
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationFactory.random().build();
        Characteristics newCharacteristics = mock(Characteristics.class);
        Animal foundAnimal = mock(Animal.class);
        when(animalRepository.getBy(animalId, organization)).thenReturn(foundAnimal);
        when(foundAnimal.has(newCharacteristics)).thenReturn(true);

        Characteristics updatedCharacteristics = characteristicsService.updateFor(animalId,
                                                                                  organization,
                                                                                  newCharacteristics);

        verify(animalRepository, never()).save(any(Animal.class));
        assertEquals(newCharacteristics, updatedCharacteristics);
    }

    @Test
    void shouldGetCharacteristicsByAnimalId() {
        UUID animalId = UUID.randomUUID();
        Characteristics expectedCharacteristics = CharacteristicsFactory.random().build();
        Animal animal = AnimalFactory.random().withIdentifier(animalId).withCharacteristics(expectedCharacteristics)
                                     .build();
        when(animalRepository.getBy(animalId)).thenReturn(animal);

        Characteristics characteristics = characteristicsService.getBy(animalId);

        assertThat(characteristics, is(expectedCharacteristics));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenThereAreNoCharacteristicsForAnimal() {
        UUID animalId = UUID.randomUUID();
        Animal animal = AnimalFactory.random().build();
        when(animalRepository.getBy(animalId)).thenReturn(animal);

        assertThrows(EntityNotFoundException.class, () -> {
            characteristicsService.getBy(animalId);
        });
    }
}