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

package ec.animal.adoption.resource;

import ec.animal.adoption.domain.Story;
import ec.animal.adoption.service.StoryService;
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
public class StoryResourceTest {

    @Mock
    private StoryService storyService;

    @Mock
    private Story expectedStory;

    private UUID animalUuid;
    private StoryResource storyResource;

    @Before
    public void setUp() {
        animalUuid = UUID.randomUUID();
        storyResource = new StoryResource(storyService);
    }

    @Test
    public void shouldCreateAStoryForAnimal() {
        Story story = mock(Story.class);
        when(storyService.create(animalUuid, story)).thenReturn(expectedStory);

        Story createdStory = storyResource.create(animalUuid, story);

        assertThat(createdStory, is(expectedStory));
    }

    @Test
    public void shouldGetStoryForAnimal() {
        when(storyService.getBy(animalUuid)).thenReturn(expectedStory);

        Story story = storyResource.get(animalUuid);

        assertThat(story, is(expectedStory));
    }
}