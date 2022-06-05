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
import ec.animal.adoption.adapter.rest.model.characteristics.CharacteristicsRequest;
import ec.animal.adoption.adapter.rest.model.characteristics.CharacteristicsRequestBuilder;
import ec.animal.adoption.adapter.rest.model.characteristics.CharacteristicsResponse;
import ec.animal.adoption.adapter.rest.service.CharacteristicsResponseMapper;
import ec.animal.adoption.application.CharacteristicsService;
import ec.animal.adoption.application.OrganizationService;
import ec.animal.adoption.domain.model.characteristics.Characteristics;
import ec.animal.adoption.domain.model.characteristics.CharacteristicsFactory;
import ec.animal.adoption.domain.model.organization.Organization;
import ec.animal.adoption.domain.model.organization.OrganizationFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacteristicsResourceTest {

    @Mock
    private CharacteristicsService characteristicsService;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private AdminTokenUtils adminTokenUtils;

    @Mock
    private Jwt token;

    private UUID animalId;
    private UUID organizationId;
    private Organization organization;
    private CharacteristicsResource characteristicsResource;

    @BeforeEach
    public void setUp() {
        animalId = UUID.randomUUID();
        organizationId = UUID.randomUUID();
        organization = OrganizationFactory.random().withIdentifier(organizationId).build();
        characteristicsResource = new CharacteristicsResource(characteristicsService, organizationService, adminTokenUtils);
    }

    @Test
    void shouldCreateCharacteristicsForAnimalFromOrganization() {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        CharacteristicsRequest characteristicsRequest = CharacteristicsRequestBuilder.random().build();

        Characteristics createdCharacteristics = CharacteristicsFactory.random().build();
        CharacteristicsResponse expectedCharacteristicsResponse = CharacteristicsResponseMapper.MAPPER.toCharacteristicsResponse(createdCharacteristics);
        when(characteristicsService.createFor(eq(animalId), eq(organization), any()))
                .thenReturn(createdCharacteristics);

        CharacteristicsResponse characteristicsResponse = characteristicsResource.create(animalId,
                                                                                         characteristicsRequest,
                                                                                         token);

        Assertions.assertThat(characteristicsResponse)
                  .usingRecursiveComparison()
                  .isEqualTo(expectedCharacteristicsResponse);
    }

    @Test
    void shouldUpdateCharacteristicsForAnimal() {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        CharacteristicsRequest characteristicsRequest = CharacteristicsRequestBuilder.random().build();
        Characteristics updatedCharacteristics = CharacteristicsFactory.random().build();
        CharacteristicsResponse expectedCharacteristicsResponse = CharacteristicsResponseMapper.MAPPER.toCharacteristicsResponse(updatedCharacteristics);
        when(characteristicsService.updateFor(eq(animalId), eq(organization), any()))
                .thenReturn(updatedCharacteristics);

        CharacteristicsResponse characteristicsResponse = characteristicsResource.update(animalId,
                                                                                         characteristicsRequest,
                                                                                         token);

        Assertions.assertThat(characteristicsResponse)
                  .usingRecursiveComparison()
                  .isEqualTo(expectedCharacteristicsResponse);
    }

    @Test
    void shouldGetCharacteristicsForAnimal() {
        Characteristics expectedCharacteristics = CharacteristicsFactory.random().build();
        when(characteristicsService.getBy(animalId)).thenReturn(expectedCharacteristics);
        CharacteristicsResponse expectedCharacteristicsResponse = CharacteristicsResponseMapper.MAPPER.toCharacteristicsResponse(expectedCharacteristics);

        CharacteristicsResponse characteristicsResponse = characteristicsResource.get(animalId);

        Assertions.assertThat(characteristicsResponse)
                  .usingRecursiveComparison()
                  .isEqualTo(expectedCharacteristicsResponse);
    }
}
