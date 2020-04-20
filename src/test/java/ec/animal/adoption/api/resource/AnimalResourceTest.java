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

package ec.animal.adoption.api.resource;

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.OrganizationBuilder;
import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.AnimalService;
import ec.animal.adoption.domain.animal.Species;
import ec.animal.adoption.domain.animal.dto.AnimalDto;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationService;
import ec.animal.adoption.domain.state.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomPhysicalActivity;
import static ec.animal.adoption.TestUtils.getRandomSize;
import static ec.animal.adoption.TestUtils.getRandomSpecies;
import static ec.animal.adoption.TestUtils.getRandomState;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnimalResourceTest {

    @Mock
    private AnimalService animalService;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private Animal expectedAnimal;

    @Mock
    private PagedEntity<AnimalDto> expectedPageOfAnimalDtosFiltered;

    @Mock
    private PagedEntity<Animal> expectedPageOfAnimals;

    private AnimalResource animalResource;

    @BeforeEach
    public void setUp() {
        animalResource = new AnimalResource(animalService, organizationService);
    }

    @Test
    public void shouldCreateAnAnimal() {
        Animal animal = AnimalBuilder.random().withOrganization(null).build();
        UUID organizationId = UUID.randomUUID();
        Organization organization = OrganizationBuilder.random().withUuid(organizationId).build();
        ArgumentCaptor<Animal> animalArgumentCaptor = ArgumentCaptor.forClass(Animal.class);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(animalService.create(any(Animal.class))).thenReturn(expectedAnimal);

        Animal createdAnimal = animalResource.create(animal, organizationId);

        assertThat(createdAnimal, is(expectedAnimal));
        verify(animalService).create(animalArgumentCaptor.capture());
        assertEquals(organization, animalArgumentCaptor.getValue().getOrganization());
    }

    @Test
    public void shouldGetAnAnimalByItsIdentifier() {
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