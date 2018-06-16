package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.Story;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JpaStoryTest {

    @Test
    public void shouldCreateJpaStoryFromStory() {
        UUID animalUuid = UUID.randomUUID();
        String text = randomAlphabetic(100);
        Story story = new Story(text);
        story.setAnimalUuid(animalUuid);
        JpaStory jpaStory = new JpaStory(story);

        assertThat(jpaStory.toStory(), is(story));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaStory.class).usingGetClass().withPrefabValues(
                Timestamp.class,
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusDays(2))
        ).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}