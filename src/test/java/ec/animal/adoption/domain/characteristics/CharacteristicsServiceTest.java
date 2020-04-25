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

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.CharacteristicsBuilder;
import ec.animal.adoption.builders.OrganizationBuilder;
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
public class CharacteristicsServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    private CharacteristicsService characteristicsService;

    @BeforeEach
    public void setUp() {
        characteristicsService = new CharacteristicsService(animalRepository);
    }

    @Test
    public void shouldCreateCharacteristics() {
        ArgumentCaptor<Animal> argumentCaptor = ArgumentCaptor.forClass(Animal.class);
        UUID animalUuid = UUID.randomUUID();
        Organization organization = OrganizationBuilder.random().build();
        Animal animal = AnimalBuilder.random().withUuid(animalUuid).withOrganization(organization).build();
        Characteristics expectedCharacteristics = CharacteristicsBuilder.random().build();
        when(animalRepository.getBy(animalUuid, organization)).thenReturn(animal);
        Animal animalWithCharacteristics = AnimalBuilder.random().withCharacteristics(expectedCharacteristics)
                                                        .withState(animal.getState()).withClinicalRecord(animal.getClinicalRecord()).withName(animal.getName())
                                                        .withEstimatedAge(animal.getEstimatedAge()).withSex(animal.getSex()).withSpecies(animal.getSpecies())
                                                        .withRegistrationDate(animal.getRegistrationDate()).withUuid(animal.getUuid()).build();
        when(animalRepository.save(any(Animal.class))).thenReturn(animalWithCharacteristics);

        Characteristics createdCharacteristics = characteristicsService.createFor(animalUuid, organization, expectedCharacteristics);

        verify(animalRepository).save(argumentCaptor.capture());
        assertTrue(argumentCaptor.getValue().getCharacteristics().isPresent());
        assertEquals(expectedCharacteristics, argumentCaptor.getValue().getCharacteristics().get());
        assertThat(createdCharacteristics, is(expectedCharacteristics));
    }

    @Test
    void shouldThrowEntityAlreadyExistsExceptionWhenThereAreAlreadyCharacteristicsForAnimal() {
        UUID animalUuid = UUID.randomUUID();
        Organization organization = OrganizationBuilder.random().build();
        Animal animal = AnimalBuilder.random()
                                     .withUuid(animalUuid)
                                     .withOrganization(organization)
                                     .withCharacteristics(CharacteristicsBuilder.random().build())
                                     .build();
        when(animalRepository.getBy(animalUuid, organization)).thenReturn(animal);

        assertThrows(EntityAlreadyExistsException.class, () -> {
            characteristicsService.createFor(animalUuid, organization, mock(Characteristics.class));
        });
    }

    @Test
    public void shouldGetCharacteristicsByAnimalUuid() {
        UUID animalUuid = UUID.randomUUID();
        Characteristics expectedCharacteristics = CharacteristicsBuilder.random().build();
        Animal animal = AnimalBuilder.random().withUuid(animalUuid).withCharacteristics(expectedCharacteristics)
                                     .build();
        when(animalRepository.getBy(animalUuid)).thenReturn(animal);

        Characteristics characteristics = characteristicsService.getBy(animalUuid);

        assertThat(characteristics, is(expectedCharacteristics));
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenThereAreNoCharacteristicsForAnimal() {
        UUID animalUuid = UUID.randomUUID();
        Animal animal = AnimalBuilder.random().build();
        when(animalRepository.getBy(animalUuid)).thenReturn(animal);

        assertThrows(EntityNotFoundException.class, () -> {
            characteristicsService.getBy(animalUuid);
        });
    }
}