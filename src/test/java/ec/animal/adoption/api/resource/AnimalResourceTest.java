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

import ec.animal.adoption.api.jwt.AdminTokenUtils;
import ec.animal.adoption.api.model.animal.AnimalResponse;
import ec.animal.adoption.api.model.animal.CreateAnimalRequest;
import ec.animal.adoption.api.model.animal.CreateAnimalResponse;
import ec.animal.adoption.api.model.animal.dto.AnimalDtoResponse;
import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.AnimalBuilder;
import ec.animal.adoption.domain.animal.AnimalService;
import ec.animal.adoption.domain.animal.Species;
import ec.animal.adoption.domain.animal.dto.CreateAnimalDto;
import ec.animal.adoption.domain.animal.dto.CreateAnimalDtoBuilder;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationBuilder;
import ec.animal.adoption.domain.organization.OrganizationService;
import ec.animal.adoption.domain.state.StateName;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static ec.animal.adoption.TestUtils.getRandomPhysicalActivity;
import static ec.animal.adoption.TestUtils.getRandomSize;
import static ec.animal.adoption.TestUtils.getRandomSpecies;
import static ec.animal.adoption.TestUtils.getRandomStateName;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnimalResourceTest {

    @Mock
    private AnimalService animalService;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private AdminTokenUtils adminTokenUtils;

    @Mock
    private Jwt token;

    private UUID organizationId;
    private Animal expectedAnimal;
    private AnimalResource animalResource;

    @BeforeEach
    public void setUp() {
        organizationId = UUID.randomUUID();
        expectedAnimal = AnimalBuilder.random().build();
        animalResource = new AnimalResource(animalService, organizationService, adminTokenUtils);
    }

    @Test
    public void shouldCreateAnAnimal() {
        CreateAnimalResponse expectedCreateAnimalResponse = CreateAnimalResponse.from(expectedAnimal);
        Organization organization = OrganizationBuilder.random().withIdentifier(organizationId).build();
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        CreateAnimalDto createAnimalDto = CreateAnimalDtoBuilder.random().withOrganization(organization).build();
        CreateAnimalRequest createAnimalRequest = mock(CreateAnimalRequest.class);
        when(createAnimalRequest.toDomainWith(organization)).thenReturn(createAnimalDto);
        when(animalService.create(createAnimalDto)).thenReturn(expectedAnimal);

        CreateAnimalResponse createAnimalResponse = animalResource.create(createAnimalRequest, token);

        Assertions.assertThat(createAnimalResponse).usingRecursiveComparison().isEqualTo(expectedCreateAnimalResponse);
    }

    @Test
    public void shouldGetAnAnimalByItsIdentifier() {
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationBuilder.random().withIdentifier(organizationId).build();
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        Animal animal = AnimalBuilder.random().build();
        AnimalResponse expectedAnimalResponse = AnimalResponse.from(animal);
        when(animalService.getBy(animalId, organization)).thenReturn(animal);

        AnimalResponse animalResponse = animalResource.get(animalId, token);

        Assertions.assertThat(animalResponse).usingRecursiveComparison().isEqualTo(expectedAnimalResponse);
    }

    @Test
    public void shouldReturnAllAnimalsWithPagination() {
        Pageable pageable = mock(Pageable.class);
        Organization organization = OrganizationBuilder.random().withIdentifier(organizationId).build();
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        PagedEntity<Animal> pageOfAnimals = new PagedEntity<>(Collections.singletonList(AnimalBuilder.random().build()));
        when(animalService.listAllFor(organization, pageable)).thenReturn(pageOfAnimals);

        PagedEntity<AnimalResponse> pageOfAnimalResponses = animalResource.listAll(pageable, token);

        Assertions.assertThat(pageOfAnimalResponses)
                  .usingRecursiveFieldByFieldElementComparator()
                  .isEqualTo(pageOfAnimals.map(AnimalResponse::from));
    }

    @Test
    public void shouldReturnAllAnimalDtosWithFiltersAndPagination() {
        StateName stateName = getRandomStateName();
        Species species = getRandomSpecies();
        PhysicalActivity physicalActivity = getRandomPhysicalActivity();
        Size size = getRandomSize();
        Pageable pageable = mock(Pageable.class);
        List<Animal> animalsFiltered = newArrayList(
                AnimalBuilder.randomWithPrimaryLinkPicture().build(),
                AnimalBuilder.randomWithPrimaryLinkPicture().build(),
                AnimalBuilder.randomWithPrimaryLinkPicture().build()
        );
        PagedEntity<Animal> pageOfAnimalsFiltered = new PagedEntity<>(animalsFiltered);
        when(animalService.listAllWithFilters(stateName, species, physicalActivity, size, pageable))
                .thenReturn(pageOfAnimalsFiltered);
        PagedEntity<AnimalDtoResponse> expectedPageOfAnimalDtosFiltered = pageOfAnimalsFiltered.map(AnimalDtoResponse::new);

        PagedEntity<AnimalDtoResponse> pageOfAnimalDtosFiltered = animalResource.listAllWithFilters(
                stateName, species, physicalActivity, size, pageable
        );

        Assertions.assertThat(pageOfAnimalDtosFiltered)
                  .usingRecursiveFieldByFieldElementComparator()
                  .isEqualTo(expectedPageOfAnimalDtosFiltered);
    }
}