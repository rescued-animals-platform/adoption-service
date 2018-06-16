package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.AbstractIntegrationTest;
import ec.animal.adoption.domain.Story;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.models.jpa.JpaStory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;

public class JpaStoryRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private JpaStoryRepository jpaStoryRepository;

    private JpaAnimal jpaAnimal;
    private JpaStory entity;

    @Before
    public void setUp() {
        jpaAnimal = createAndSaveJpaAnimal();
    }

    @Test
    public void shouldSaveStory() {
        Story story = new Story(randomAlphabetic(100));
        story.setAnimalUuid(jpaAnimal.toAnimal().getUuid());
        entity = new JpaStory(story);

        JpaStory jpaStory = jpaStoryRepository.save(entity);

        assertEquals(entity, jpaStory);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldThrowDataIntegrityViolationExceptionWhenCreationStoryForSameAnimal() {
        Story story = new Story(randomAlphabetic(100));
        story.setAnimalUuid(jpaAnimal.toAnimal().getUuid());
        entity = new JpaStory(story);
        jpaStoryRepository.save(entity);

        jpaStoryRepository.save(new JpaStory(story));
    }
}
