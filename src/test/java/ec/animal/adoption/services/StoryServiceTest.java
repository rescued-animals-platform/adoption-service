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

package ec.animal.adoption.services;

import ec.animal.adoption.domain.Story;
import ec.animal.adoption.repositories.StoryRepository;
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
public class StoryServiceTest {

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private Story expectedStory;

    private StoryService storyService;

    @Before
    public void setUp() {
        storyService = new StoryService(storyRepository);
    }

    @Test
    public void shouldCreateStory() {
        Story story = mock(Story.class);
        when(storyRepository.save(story)).thenReturn(expectedStory);

        Story createdStory = storyService.create(story);

        assertThat(createdStory, is(expectedStory));
    }

    @Test
    public void shouldGetStoryByAnimalUuid() {
        UUID animalUuid = UUID.randomUUID();
        when(storyRepository.getBy(animalUuid)).thenReturn(expectedStory);

        Story story = storyService.getBy(animalUuid);

        assertThat(story, is(expectedStory));
    }
}