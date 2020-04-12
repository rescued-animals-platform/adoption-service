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

package ec.animal.adoption.service;

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.CharacteristicsBuilder;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.exception.EntityNotFoundException;
import ec.animal.adoption.repository.AnimalRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CharacteristicsServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    private CharacteristicsService characteristicsService;

    @Before
    public void setUp() {
        characteristicsService = new CharacteristicsService(animalRepository);
    }

    @Test
    public void shouldCreateCharacteristics() {
        ArgumentCaptor<Animal> argumentCaptor = ArgumentCaptor.forClass(Animal.class);
        UUID animalUuid = UUID.randomUUID();
        Animal animal = AnimalBuilder.random().withUuid(animalUuid).build();
        Characteristics expectedCharacteristics = CharacteristicsBuilder.random().build();
        when(animalRepository.getBy(animalUuid)).thenReturn(animal);
        Animal animalWithCharacteristics = AnimalBuilder.random().withCharacteristics(expectedCharacteristics)
                .withState(animal.getState()).withClinicalRecord(animal.getClinicalRecord()).withName(animal.getName())
                .withEstimatedAge(animal.getEstimatedAge()).withSex(animal.getSex()).withSpecies(animal.getSpecies())
                .withRegistrationDate(animal.getRegistrationDate()).withUuid(animal.getUuid()).build();
        when(animalRepository.save(any(Animal.class))).thenReturn(animalWithCharacteristics);

        Characteristics createdCharacteristics = characteristicsService.create(animalUuid, expectedCharacteristics);

        verify(animalRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getCharacteristics(), is(expectedCharacteristics));
        assertThat(createdCharacteristics, is(expectedCharacteristics));
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

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenThereIsNoCharacteristicsForAnimal() {
        UUID animalUuid = UUID.randomUUID();
        Animal animal = AnimalBuilder.random().build();
        when(animalRepository.getBy(animalUuid)).thenReturn(animal);

        characteristicsService.getBy(animalUuid);
    }
}