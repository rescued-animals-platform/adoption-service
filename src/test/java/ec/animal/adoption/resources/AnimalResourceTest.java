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
import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.Species;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.dtos.AnimalDto;
import ec.animal.adoption.services.AnimalService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import static ec.animal.adoption.TestUtils.*;
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

    @Mock
    private PagedEntity<AnimalDto> expectedPageOfAnimalDtosFiltered;

    @Mock
    private PagedEntity<Animal> expectedPageOfAnimals;

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

    @Test
    public void shouldReturnAllAnimalsWithPagination() {
        Pageable pageable = mock(Pageable.class);
        when(animalService.listAll(pageable)).thenReturn(expectedPageOfAnimals);

        PagedEntity<Animal> pageOfAnimals = animalResource.listAll(pageable);

        assertThat(pageOfAnimals, is(expectedPageOfAnimals));
    }

    @Test
    public void shouldReturnAllAnimalsWithFiltersAndPagination() {
        State state = getRandomState();
        String stateName = state.getStateName();
        Species species = getRandomSpecies();
        PhysicalActivity physicalActivity = getRandomPhysicalActivity();
        Size size = getRandomSize();
        Pageable pageable = mock(Pageable.class);
        when(animalService.listAllWithFilters(stateName, species, physicalActivity, size, pageable))
                .thenReturn(expectedPageOfAnimalDtosFiltered);

        PagedEntity<AnimalDto> pageOfAnimalDtosFiltered = animalResource.listAllWithFilters(
                stateName, species, physicalActivity, size, pageable
        );

        assertThat(pageOfAnimalDtosFiltered, is(expectedPageOfAnimalDtosFiltered));
    }
}