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
import ec.animal.adoption.api.model.characteristics.CharacteristicsRequest;
import ec.animal.adoption.api.model.characteristics.CharacteristicsResponse;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.CharacteristicsFactory;
import ec.animal.adoption.domain.characteristics.CharacteristicsService;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationFactory;
import ec.animal.adoption.domain.organization.OrganizationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CharacteristicsResourceTest {

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
    public void shouldCreateCharacteristicsForAnimalFromOrganization() {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        CharacteristicsRequest characteristicsRequest = mock(CharacteristicsRequest.class);
        Characteristics characteristicsFromRequest = CharacteristicsFactory.random().build();
        when(characteristicsRequest.toDomain()).thenReturn(characteristicsFromRequest);
        Characteristics createdCharacteristics = CharacteristicsFactory.random().build();
        CharacteristicsResponse expectedCharacteristicsResponse = CharacteristicsResponse.from(createdCharacteristics);
        when(characteristicsService.createFor(animalId, organization, characteristicsFromRequest))
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
        CharacteristicsRequest characteristicsRequest = mock(CharacteristicsRequest.class);
        Characteristics characteristicsFromRequest = CharacteristicsFactory.random().build();
        when(characteristicsRequest.toDomain()).thenReturn(characteristicsFromRequest);
        Characteristics updatedCharacteristics = CharacteristicsFactory.random().build();
        CharacteristicsResponse expectedCharacteristicsResponse = CharacteristicsResponse.from(updatedCharacteristics);
        when(characteristicsService.updateFor(animalId, organization, characteristicsFromRequest))
                .thenReturn(updatedCharacteristics);

        CharacteristicsResponse characteristicsResponse = characteristicsResource.update(animalId,
                                                                                         characteristicsRequest,
                                                                                         token);

        Assertions.assertThat(characteristicsResponse)
                  .usingRecursiveComparison()
                  .isEqualTo(expectedCharacteristicsResponse);
    }

    @Test
    public void shouldGetCharacteristicsForAnimal() {
        Characteristics expectedCharacteristics = CharacteristicsFactory.random().build();
        when(characteristicsService.getBy(animalId)).thenReturn(expectedCharacteristics);
        CharacteristicsResponse expectedCharacteristicsResponse = CharacteristicsResponse.from(expectedCharacteristics);

        CharacteristicsResponse characteristicsResponse = characteristicsResource.get(animalId);

        Assertions.assertThat(characteristicsResponse)
                  .usingRecursiveComparison()
                  .isEqualTo(expectedCharacteristicsResponse);
    }
}
