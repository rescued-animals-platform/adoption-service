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

package ec.animal.adoption.adapter.rest.resource;

import ec.animal.adoption.adapter.rest.jwt.AdminTokenUtils;
import ec.animal.adoption.adapter.rest.model.animal.AnimalCreateUpdateRequest;
import ec.animal.adoption.adapter.rest.model.animal.AnimalCreateUpdateResponse;
import ec.animal.adoption.adapter.rest.model.animal.AnimalResponse;
import ec.animal.adoption.adapter.rest.model.animal.dto.AnimalDtoResponse;
import ec.animal.adoption.adapter.rest.model.state.StateRequest;
import ec.animal.adoption.adapter.rest.service.AnimalCreateUpdateResponseMapper;
import ec.animal.adoption.adapter.rest.service.AnimalDtoResponseMapper;
import ec.animal.adoption.adapter.rest.service.AnimalResponseMapper;
import ec.animal.adoption.application.AnimalService;
import ec.animal.adoption.application.OrganizationService;
import ec.animal.adoption.domain.model.animal.Animal;
import ec.animal.adoption.domain.model.animal.AnimalFactory;
import ec.animal.adoption.domain.model.animal.Species;
import ec.animal.adoption.domain.model.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.model.characteristics.Size;
import ec.animal.adoption.domain.model.organization.Organization;
import ec.animal.adoption.domain.model.organization.OrganizationFactory;
import ec.animal.adoption.domain.model.state.StateName;
import ec.animal.adoption.domain.model.PagedEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomPhysicalActivity;
import static ec.animal.adoption.TestUtils.getRandomSize;
import static ec.animal.adoption.TestUtils.getRandomSpecies;
import static ec.animal.adoption.TestUtils.getRandomStateName;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnimalResourceTest {

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
    void shouldCreateAnAnimal() {
        AnimalCreateUpdateResponse expectedAnimalCreateUpdateResponse = AnimalCreateUpdateResponseMapper.MAPPER.toAnimalCreateUpdateResponse(expectedAnimal);
        Organization organization = OrganizationFactory.random().withIdentifier(organizationId).build();
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        StateRequest stateRequest = new StateRequest(expectedAnimal.getState().getName(),
                                                     expectedAnimal.getState().getAdoptionFormId().orElse(null),
                                                     expectedAnimal.getState().getNotes().orElse(null));
        AnimalCreateUpdateRequest animalCreateUpdateRequest = new AnimalCreateUpdateRequest(expectedAnimal.getClinicalRecord(),
                                                                                            expectedAnimal.getName(),
                                                                                            expectedAnimal.getSpecies(),
                                                                                            expectedAnimal.getEstimatedAge(),
                                                                                            expectedAnimal.getSex(),
                                                                                            stateRequest);
        when(animalService.create(any())).thenReturn(expectedAnimal);

        AnimalCreateUpdateResponse animalCreateUpdateResponse = animalResource.create(animalCreateUpdateRequest, token);

        Assertions.assertThat(animalCreateUpdateResponse).usingRecursiveComparison().isEqualTo(expectedAnimalCreateUpdateResponse);
    }

    @Test
    void shouldUpdateAnAnimal() {
        UUID animalId = UUID.randomUUID();
        AnimalCreateUpdateResponse expectedAnimalCreateUpdateResponse = AnimalCreateUpdateResponseMapper.MAPPER.toAnimalCreateUpdateResponse(expectedAnimal);
        Organization organization = OrganizationFactory.random().withIdentifier(organizationId).build();
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        StateRequest stateRequest = new StateRequest(expectedAnimal.getState().getName(),
                                                     expectedAnimal.getState().getAdoptionFormId().orElse(null),
                                                     expectedAnimal.getState().getNotes().orElse(null));
        AnimalCreateUpdateRequest animalCreateUpdateRequest = new AnimalCreateUpdateRequest(expectedAnimal.getClinicalRecord(),
                                                                                            expectedAnimal.getName(),
                                                                                            expectedAnimal.getSpecies(),
                                                                                            expectedAnimal.getEstimatedAge(),
                                                                                            expectedAnimal.getSex(),
                                                                                            stateRequest);
        when(animalService.update(eq(animalId), any())).thenReturn(expectedAnimal);

        AnimalCreateUpdateResponse animalCreateUpdateResponse = animalResource.update(animalId,
                                                                                      animalCreateUpdateRequest,
                                                                                      token);

        Assertions.assertThat(animalCreateUpdateResponse).usingRecursiveComparison().isEqualTo(expectedAnimalCreateUpdateResponse);
    }

    @Test
    void shouldGetAnAnimalByItsIdentifier() {
        UUID animalId = UUID.randomUUID();
        Organization organization = OrganizationFactory.random().withIdentifier(organizationId).build();
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        Animal animal = AnimalFactory.random().build();
        AnimalResponse expectedAnimalResponse = AnimalResponseMapper.MAPPER.toAnimalResponse(animal);
        when(animalService.getBy(animalId, organization)).thenReturn(animal);

        AnimalResponse animalResponse = animalResource.get(animalId, token);

        Assertions.assertThat(animalResponse).usingRecursiveComparison().isEqualTo(expectedAnimalResponse);
    }

    @Test
    void shouldReturnAllAnimalsWithPagination() {
        Pageable pageable = mock(Pageable.class);
        Organization organization = OrganizationFactory.random().withIdentifier(organizationId).build();
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        PagedEntity<Animal> pageOfAnimals = new PagedEntity<>(Collections.singletonList(AnimalFactory.random().build()));
        when(animalService.listAllFor(organization, pageable)).thenReturn(pageOfAnimals);

        PagedEntity<AnimalResponse> pageOfAnimalResponses = animalResource.listAll(pageable, token);

        Assertions.assertThat(pageOfAnimalResponses)
                  .usingRecursiveFieldByFieldElementComparator()
                  .isEqualTo(pageOfAnimals.map(AnimalResponseMapper.MAPPER::toAnimalResponse));
    }

    @Test
    void shouldReturnAllAnimalDtosWithFiltersAndPagination() {
        StateName stateName = getRandomStateName();
        Species species = getRandomSpecies();
        PhysicalActivity physicalActivity = getRandomPhysicalActivity();
        Size size = getRandomSize();
        Pageable pageable = mock(Pageable.class);
        List<Animal> animalsFiltered = Arrays.asList(
                AnimalFactory.randomWithPrimaryLinkPicture().build(),
                AnimalFactory.randomWithPrimaryLinkPicture().build(),
                AnimalFactory.randomWithPrimaryLinkPicture().build()
        );
        PagedEntity<Animal> pageOfAnimalsFiltered = new PagedEntity<>(animalsFiltered);
        when(animalService.listAllWithFilters(stateName, species, physicalActivity, size, pageable))
                .thenReturn(pageOfAnimalsFiltered);
        PagedEntity<AnimalDtoResponse> expectedPageOfAnimalDtosFiltered = pageOfAnimalsFiltered.map(AnimalDtoResponseMapper.MAPPER::toAnimalDtoResponse);

        PagedEntity<AnimalDtoResponse> pageOfAnimalDtosFiltered = animalResource.listAllWithFilters(
                stateName, species, physicalActivity, size, pageable
        );

        Assertions.assertThat(pageOfAnimalDtosFiltered)
                  .usingRecursiveFieldByFieldElementComparator()
                  .isEqualTo(expectedPageOfAnimalDtosFiltered);
    }
}