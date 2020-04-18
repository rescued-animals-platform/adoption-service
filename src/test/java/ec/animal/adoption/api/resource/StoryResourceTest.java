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

import ec.animal.adoption.domain.story.Story;
import ec.animal.adoption.domain.story.StoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoryResourceTest {

    @Mock
    private StoryService storyService;

    @Mock
    private Story expectedStory;

    private UUID animalUuid;
    private StoryResource storyResource;

    @BeforeEach
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