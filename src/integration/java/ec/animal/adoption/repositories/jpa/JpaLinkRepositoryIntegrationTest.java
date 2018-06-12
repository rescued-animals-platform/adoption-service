package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.AbstractIntegrationTest;
import ec.animal.adoption.domain.media.Link;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.models.jpa.JpaMediaLink;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;

public class JpaLinkRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private JpaMediaLinkRepository jpaMediaLinkRepository;

    private JpaAnimal jpaAnimal;

    @Before
    public void setUp() {
        jpaAnimal = createAndSaveJpaAnimal();
    }

    @Test
    public void shouldSaveMediaLink() {
        UUID animalUuid = jpaAnimal.toAnimal().getUuid();
        Link link = new Link(animalUuid, randomAlphabetic(10), randomAlphabetic(10));
        JpaMediaLink entity = new JpaMediaLink(link);

        JpaMediaLink jpaMediaLink = jpaMediaLinkRepository.save(entity);

        assertEquals(jpaMediaLink, entity);
    }
}
