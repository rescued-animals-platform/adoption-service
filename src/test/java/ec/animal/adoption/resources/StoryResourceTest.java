package ec.animal.adoption.resources;

import ec.animal.adoption.domain.Story;
import ec.animal.adoption.services.StoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StoryResourceTest {

    @Mock
    private StoryService storyService;

    @Mock
    private Story story;

    @Test
    public void shouldCreateAStoryForAnimal() {
        UUID animalUuid = UUID.randomUUID();
        Story expectedStory = mock(Story.class);
        when(storyService.create(story)).thenReturn(expectedStory);
        StoryResource storyResource = new StoryResource(storyService);

        Story createdStory = storyResource.create(animalUuid, story);

        verify(story).setAnimalUuid(animalUuid);
        assertThat(createdStory, is(expectedStory));
    }
}