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
import ec.animal.adoption.api.model.animal.AnimalCreateUpdateRequest;
import ec.animal.adoption.api.model.animal.AnimalCreateUpdateResponse;
import ec.animal.adoption.api.model.animal.AnimalResponse;
import ec.animal.adoption.api.model.animal.dto.AnimalDtoResponse;
import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.AnimalFactory;
import ec.animal.adoption.domain.animal.AnimalService;
import ec.animal.adoption.domain.animal.Species;
import ec.animal.adoption.domain.animal.dto.AnimalDto;
import ec.animal.adoption.domain.animal.dto.AnimalDtoFactory;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationFactory;
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
        expectedAnimal = AnimalFactory.random().build();
        animalResource = new AnimalResource(animalService, organizationService, adminTokenUtils);
    }

    @Test
    public void shouldCreateAnAnimal() {
        AnimalCreateUpdateResponse expectedAnimalCreateUpdateResponse = AnimalCreateUpdateResponse.from(expectedAnimal);
        Organization organization = OrganizationFactory.random().withIdentifier(organizationId).build();
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        AnimalDto animalDto = AnimalDtoFactory.random().withOrganization(organization).build();
        AnimalCreateUpdateRequest animalCreateUpdateRequest = mock(AnimalCreateUpdateRequest.class);
        when(animalCreateUpdateRequest.toDomainWith(organization)).thenReturn(animalDto);
        when(animalService.create(animalDto)).thenReturn(expectedAnimal);

        AnimalCreateUpdateResponse animalCreateUpdateResponse = animalResource.create(animalCreateUpdateRequest, token);

        Assertions.assertThat(animalCreateUpdateResponse).usingRecursiveComparison().isEqualTo(expectedAnimalCreateUpdateResponse);
    }

    @Test
    public void shouldUpdateAnAnimal() {
        UUID animalId = UUID.randomUUID();
        AnimalCreateUpdateResponse expectedAnimalCreateUpdateResponse = AnimalCreateUpdateResponse.from(expectedAnimal);
        Organization organization = OrganizationFactory.random().withIdentifier(organizationId).build();
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        AnimalDto animalDto = AnimalDtoFactory.random().withOrganization(organization).build();
        AnimalCreateUpdateRequest animalCreateUpdateRequest = mock(AnimalCreateUpdateRequest.class);
        when(animalCreateUpdateRequest.toDomainWith(organization)).thenReturn(animalDto);
        when(animalService.update(animalId, animalDto)).thenReturn(expectedAnimal);

        AnimalCreateUpdateResponse animalCreateUpdateResponse = animalResource.update(animalId,
                                                                                      animalCreateUpdateRequest,
                                                                                      token);

        Assertions.assertThat(animalCreateUpdateResponse).usingRecursiveComparison().isEqualTo(expectedAnimalCreateUpdateResponse);
    }

    @Test
    public void shouldGetAnAnimalByItsIdentifier() {
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationFactory.random().withIdentifier(organizationId).build();
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        Animal animal = AnimalFactory.random().build();
        AnimalResponse expectedAnimalResponse = AnimalResponse.from(animal);
        when(animalService.getBy(animalId, organization)).thenReturn(animal);

        AnimalResponse animalResponse = animalResource.get(animalId, token);

        Assertions.assertThat(animalResponse).usingRecursiveComparison().isEqualTo(expectedAnimalResponse);
    }

    @Test
    public void shouldReturnAllAnimalsWithPagination() {
        Pageable pageable = mock(Pageable.class);
        Organization organization = OrganizationFactory.random().withIdentifier(organizationId).build();
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        PagedEntity<Animal> pageOfAnimals = new PagedEntity<>(Collections.singletonList(AnimalFactory.random().build()));
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
                AnimalFactory.randomWithPrimaryLinkPicture().build(),
                AnimalFactory.randomWithPrimaryLinkPicture().build(),
                AnimalFactory.randomWithPrimaryLinkPicture().build()
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