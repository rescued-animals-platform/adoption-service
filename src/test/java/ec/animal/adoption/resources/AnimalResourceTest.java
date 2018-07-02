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

package ec.animal.adoption.resources;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.services.AnimalService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnimalResourceTest {

    @Mock
    private AnimalService animalService;

    @Mock
    private Animal expectedAnimal;

    private AnimalResource animalResource;

    @Before
    public void setUp() {
        animalResource = new AnimalResource(animalService);
    }

    @Test
    public void shouldCreateAnAnimal() {
        Animal animal = mock(Animal.class);
        when(animalService.create(animal)).thenReturn(expectedAnimal);

        Animal createdAnimal = animalResource.create(animal);

        assertThat(createdAnimal, is(expectedAnimal));
    }

    @Test
    public void shouldGetAnAnimalByItsUuid() {
        UUID uuid = UUID.randomUUID();
        when(animalService.getBy(uuid)).thenReturn(expectedAnimal);

        Animal animal = animalResource.get(uuid);

        assertThat(animal, is(expectedAnimal));
    }
}