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
import ec.animal.adoption.api.model.story.StoryRequest;
import ec.animal.adoption.api.model.story.StoryResponse;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationFactory;
import ec.animal.adoption.domain.organization.OrganizationService;
import ec.animal.adoption.domain.story.Story;
import ec.animal.adoption.domain.story.StoryFactory;
import ec.animal.adoption.domain.story.StoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoryResourceTest {

    @Mock
    private StoryService storyService;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private AdminTokenUtils adminTokenUtils;

    @Mock
    private Jwt token;

    private UUID organizationId;
    private Organization organization;
    private UUID animalId;
    private StoryResource storyResource;

    @BeforeEach
    public void setUp() {
        organizationId = UUID.randomUUID();
        organization = OrganizationFactory.random().withIdentifier(organizationId).build();
        animalId = UUID.randomUUID();
        storyResource = new StoryResource(storyService, organizationService, adminTokenUtils);
    }

    @Test
    public void shouldCreateAStoryForAnimal() {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        StoryRequest storyRequest = mock(StoryRequest.class);
        Story storyFromRequest = new Story(randomAlphabetic(10));
        when(storyRequest.toDomain()).thenReturn(storyFromRequest);
        Story createdStory = StoryFactory.random().build();
        StoryResponse expectedStoryResponse = StoryResponse.from(createdStory);
        when(storyService.createFor(animalId, organization, storyFromRequest)).thenReturn(createdStory);

        StoryResponse storyResponse = storyResource.create(animalId, storyRequest, token);

        Assertions.assertThat(storyResponse).usingRecursiveComparison().isEqualTo(expectedStoryResponse);
    }

    @Test
    void shouldUpdateStoryForAnimal() {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        StoryRequest storyRequest = mock(StoryRequest.class);
        Story storyFromRequest = new Story(randomAlphabetic(10));
        when(storyRequest.toDomain()).thenReturn(storyFromRequest);
        Story updatedStory = StoryFactory.random().build();
        StoryResponse expectedStoryResponse = StoryResponse.from(updatedStory);
        when(storyService.updateFor(animalId, organization, storyFromRequest)).thenReturn(updatedStory);

        StoryResponse storyResponse = storyResource.update(animalId, storyRequest, token);

        Assertions.assertThat(storyResponse).usingRecursiveComparison().isEqualTo(expectedStoryResponse);
    }

    @Test
    public void shouldGetStoryForAnimal() {
        Story foundStory = StoryFactory.random().build();
        StoryResponse expectedStoryResponse = StoryResponse.from(foundStory);
        when(storyService.getBy(animalId)).thenReturn(foundStory);

        StoryResponse storyResponse = storyResource.get(animalId);

        Assertions.assertThat(storyResponse).usingRecursiveComparison().isEqualTo(expectedStoryResponse);
    }
}