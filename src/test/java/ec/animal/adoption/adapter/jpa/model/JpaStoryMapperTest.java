package ec.animal.adoption.adapter.jpa.model;

import ec.animal.adoption.domain.model.story.Story;
import ec.animal.adoption.domain.model.story.StoryFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class JpaStoryMapperTest {

    @Test
    void shouldGenerateAnIdWhenCreatingAJpaStoryForAStoryWithNoId() {
        Story story = StoryFactory.random().withIdentifier(null).build();

        JpaStory jpaStory = JpaStoryMapper.MAPPER.toJpaStory(story, mock(JpaAnimal.class));

        assertNotNull(jpaStory.getId());
    }

    @Test
    void shouldGenerateARegistrationDateWhenCreatingAJpaStoryForAStoryWithNoRegistrationDate() {
        Story story = StoryFactory.random().withRegistrationDate(null).build();

        JpaStory jpaStory = JpaStoryMapper.MAPPER.toJpaStory(story, mock(JpaAnimal.class));

        assertNotNull(jpaStory.getRegistrationDate());
    }

    @Test
    void shouldCreateJpaStoryFromStory() {
        UUID storyId = UUID.randomUUID();
        LocalDateTime registrationDate = LocalDateTime.now();
        String text = randomAlphabetic(100);
        Story story = StoryFactory.random()
                                  .withIdentifier(storyId)
                                  .withRegistrationDate(registrationDate)
                                  .withText(text)
                                  .build();

        JpaStory jpaStory = JpaStoryMapper.MAPPER.toJpaStory(story, mock(JpaAnimal.class));

        assertThat(jpaStory.getId(), is(storyId));
        assertThat(jpaStory.getRegistrationDate(), is(registrationDate));
        assertThat(jpaStory.getText(), is(story.getText()));
    }

    @Test
    void shouldCreateStoryFromAJpaStory() {
        UUID id = UUID.randomUUID();
        LocalDateTime registrationDate = LocalDateTime.now();
        String text = randomAlphabetic(100);
        JpaStory jpaStory = new JpaStory(id, mock(JpaAnimal.class), registrationDate, text);

        Story story = JpaStoryMapper.MAPPER.toStory(jpaStory);

        assertThat(story.getIdentifier(), is(id));
        assertThat(story.getRegistrationDate(), is(registrationDate));
        assertThat(story.getText(), is(story.getText()));
    }
}