package ec.animal.adoption.services;

import ec.animal.adoption.domain.Story;
import ec.animal.adoption.repositories.StoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StoryServiceTest {

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private Story expectedStory;

    @Test
    public void shouldCreateStory() {
        Story story = mock(Story.class);
        when(storyRepository.save(story)).thenReturn(expectedStory);
        StoryService storyService = new StoryService(storyRepository);

        Story createdStory = storyService.create(story);

        assertThat(createdStory, is(expectedStory));
    }
}