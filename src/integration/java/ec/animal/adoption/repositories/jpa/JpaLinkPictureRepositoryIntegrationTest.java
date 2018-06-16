package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.AbstractIntegrationTest;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.models.jpa.JpaLinkPicture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ec.animal.adoption.TestUtils.getRandomPictureType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;

public class JpaLinkPictureRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private JpaLinkPictureRepository jpaLinkPictureRepository;

    private JpaAnimal jpaAnimal;

    @Before
    public void setUp() {
        jpaAnimal = createAndSaveJpaAnimal();
    }

    @Test
    public void shouldSaveLinkPicture() {
        LinkPicture linkPicture = new LinkPicture(
                jpaAnimal.toAnimal().getUuid(),
                randomAlphabetic(10),
                getRandomPictureType(),
                new MediaLink(randomAlphabetic(10)),
                new MediaLink(randomAlphabetic(10))
        );
        JpaLinkPicture entity = new JpaLinkPicture(linkPicture);

        JpaLinkPicture jpaLinkPicture = jpaLinkPictureRepository.save(entity);

        assertEquals(entity, jpaLinkPicture);
    }
}
