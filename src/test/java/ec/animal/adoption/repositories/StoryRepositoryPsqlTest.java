package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.Story;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaStory;
import ec.animal.adoption.repositories.jpa.JpaStoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StoryRepositoryPsqlTest {

    @Mock
    private JpaStoryRepository jpaStoryRepository;

    @Mock
    private AnimalRepositoryPsql animalRepositoryPsql;

    private UUID animalUuid;
    private Story story;
    private StoryRepositoryPsql storyRepositoryPsql;

    @Before
    public void setUp() {
        animalUuid = UUID.randomUUID();
        story = new Story(randomAlphabetic(100));
        when(animalRepositoryPsql.animalExists(animalUuid)).thenReturn(true);
        storyRepositoryPsql = new StoryRepositoryPsql(jpaStoryRepository, animalRepositoryPsql);
    }

    @Test
    public void shouldBeAnInstanceOfStoryRepository() {
        assertThat(storyRepositoryPsql, is(instanceOf(StoryRepository.class)));
    }

    @Test
    public void shouldSaveJpaStory() {
        ArgumentCaptor<JpaStory> argumentCaptor = ArgumentCaptor.forClass(JpaStory.class);
        story.setAnimalUuid(animalUuid);
        JpaStory expectedJpaStory = new JpaStory(story);
        when(jpaStoryRepository.save(any(JpaStory.class))).thenReturn(expectedJpaStory);

        Story savedStory = storyRepositoryPsql.save(story);

        verify(jpaStoryRepository).save(argumentCaptor.capture());
        JpaStory jpaStory = argumentCaptor.getValue();
        Story story = jpaStory.toStory();
        assertThat(story.getText(), is(this.story.getText()));
        assertThat(savedStory, is(expectedJpaStory.toStory()));
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void shouldThrowEntityAlreadyExistException() {
        story.setAnimalUuid(animalUuid);
        doAnswer((Answer<Object>) invocation -> {
            throw mock(DataIntegrityViolationException.class);
        }).when(jpaStoryRepository).save(any(JpaStory.class));

        storyRepositoryPsql.save(story);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenSavingStoryForNonExistentAnimal() {
        story.setAnimalUuid(UUID.randomUUID());

        storyRepositoryPsql.save(story);
    }
}